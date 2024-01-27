package com.example.pifinance_back.Controllers;

import com.example.pifinance_back.Entities.*;
import com.example.pifinance_back.Repositories.ClientRepository;
import com.example.pifinance_back.Repositories.HistoriqueChargementRepository;
import com.example.pifinance_back.Repositories.WalletRepository;
import com.example.pifinance_back.Services.IWalletService;
import com.example.pifinance_back.Services.StripeService;
import com.example.pifinance_back.Services.WalletService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.model.Charge;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.Map;

import static com.neovisionaries.i18n.CurrencyCode.USD;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@RequestMapping("/wallets")
public class WalletController {
@Autowired
    private final IWalletService walletService;
    @Autowired
    private final WalletService wss;

    @Autowired
    private final StripeService stripe;
    @Autowired
    private final WalletRepository wr ;
    @Autowired
    private HistoriqueChargementRepository historiqueChargementRepository;

    @Autowired
private final ClientRepository cr ;
    private final RestTemplate restTemplate = new RestTemplate();

    private final String flaskScrapeUrl = "http://localhost:5000/scrape-data"; // URL du service Flask

    @PostMapping("/create/{userId}")
    public ResponseEntity<?> createWallet(@PathVariable Long userId, @RequestBody Wallet wallet) {
        // Récupérer l'utilisateur en fonction de l'ID, par exemple depuis le service utilisateur
        Client user = cr.findClientById(userId);

        if (user == null) {
            // Gérer le cas où l'utilisateur n'existe pas
            // Vous pouvez renvoyer une réponse d'erreur appropriée ici
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé.");
        }
        // Vérifier si l'utilisateur a déjà un portefeuille associé
        List<Wallet> userWallets = walletService.getWalletsByUser(user);
        if (!userWallets.isEmpty()) {
            // L'utilisateur a déjà un portefeuille, renvoyer une réponse d'erreur
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur a déjà un portefeuille.");
        }
        wallet.setActive(false);
        wallet.setUser(user); // Associer le portefeuille à l'utilisateur complet
        wallet.setVirtual_balance(BigDecimal.ZERO);
        wallet.setReal_balance(BigDecimal.ZERO);
        wallet.setRealCurrency(USD.getCurrency());

        return ResponseEntity.ok(walletService.createWallet(wallet));
    }
    @GetMapping("/scrape-data")
    public String scrapeData() {
        String flaskServiceUrl = "http://localhost:5000/scrape";
        ResponseEntity<String> response = restTemplate.getForEntity(flaskServiceUrl, String.class);

        // Assuming the Flask service returns a string of the page_source
        return response.getBody();
    }
    @GetMapping("/{id}/Portfolio")
    public WalletService.PortfolioDTO getPortfolio(@PathVariable Long id) {
        return wss.getPortfolio(id);
    }

    @GetMapping("/{id}")
    public Wallet getWallet(@PathVariable Long id) {
       return walletService.getWalletDetailsById(id);
    }

    @DeleteMapping("/wallets/{id}")
    public void deleteWallet(@PathVariable Long id) {
        walletService.deleteWallet(id);
    }
    @DeleteMapping("/cards/{id}")
    public void deleteCard(@PathVariable Long id) {
        walletService.deleteCard(id);
    }
    @PutMapping("/update")
    public Wallet updateWallet(@RequestBody Wallet wallet) {
        return walletService.updateWallet(wallet);
    }

    @GetMapping("/active/{userId}")
    public List<Wallet> getActiveWalletsForUser(@PathVariable Long userId) {
        Client user = new Client(); // You should retrieve the user based on the ID
        return walletService.getActiveWalletsForUser(user);
    }

    @PutMapping("/deactivate/{id}")
    public Wallet deactivateWallet(@PathVariable Long id) {
        return walletService.deactivateWallet(id);
    }
    @PutMapping("/activate/{id}")
    public Wallet activateWallet(@PathVariable Long id) {
        return walletService.activateWallet(id);
    }

    @GetMapping("/balance/{id}")
    public BigDecimal getWalletBalance(@PathVariable Long id) {
        return walletService.getVirtualWalletBalance(id);
    }

    @GetMapping("/byType/{type}")
    public List<Wallet> getWalletsByType(@PathVariable WalletEnum type) {
        return walletService.getWalletsByType(type);
    }

    @GetMapping("/byCurrency/{currency}")
    public List<Wallet> getWalletsByCurrency(@PathVariable Currency currency) {
        return walletService.getWalletsByCurrency(currency);
    }

    @GetMapping("/byUserAndType/{userId}/{type}")
    public List<Wallet> getWalletsByUserAndType(@PathVariable Long userId, @PathVariable WalletEnum type) {
        Client user = new Client();
        return walletService.getWalletsByUserAndType(user, type);
    }
    @PutMapping("/convert/{walletId}")
    public ResponseEntity<?> convertWalletCurrency(@PathVariable Long walletId, @RequestBody Map<String, String> request) {
        try {
            String targetCurrencyCode = request.get("targetCurrency");
            Currency targetCurrency = Currency.getInstance(targetCurrencyCode);
            walletService.convertRealBalanceToCurrency(walletId, targetCurrency);
            return ResponseEntity.ok("Conversion réussie");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    public ResponseEntity<String> createStripeToken(Card card) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer pk_test_51O5H2fKb1DjWinQwPeFFMrkt8EkG8kBXApqPWUnbinsHGvoebY23DzaeQgnRsTLrGplW3O20V05FuLkhLutVe0Js0097mZQNhc");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("card[number]", card.getNumber());
        map.add("card[exp_month]", String.valueOf(card.getExp_month()));
        map.add("card[exp_year]", String.valueOf(card.getExp_year()));
        map.add("card[cvc]", card.getCvc());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity("https://api.stripe.com/v1/tokens", request, String.class);

        return response; // Retournez la réponse complète
    }
    private String extractTokenFromResponse(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            return rootNode.path("id").asText();
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'extraction du token de la réponse.");
        }
    }
    @PostMapping("/charger-portefeuille/{walletId}")
    public ResponseEntity<?> chargerPortefeuille(@PathVariable Long walletId, @RequestBody CardDetailsDTO cardDetails) {
        try {
            // Récupérer le portefeuille
            Wallet wallet = wr.findById(walletId)
                    .orElseThrow(() -> new RuntimeException("Portefeuille non trouvé avec l'ID: " + walletId));

            // Vérification de l'activité du portefeuille
            if (!wallet.isActive()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Votre portefeuille n'est pas activé.");
            }

            // Vérifier si le portefeuille a une carte associée
            Card associatedCard = wallet.getCard();

            // Si le portefeuille a une carte associée, utiliser les détails de cette carte
            if (associatedCard != null) {
                cardDetails.setNumber(associatedCard.getNumber());
                cardDetails.setExpMonth(associatedCard.getExp_month());
                cardDetails.setExpYear(associatedCard.getExp_year());
                cardDetails.setCvc(associatedCard.getCvc());

            }

            // Étape 1: Obtenir le StripeToken
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", "Bearer pk_test_51O5H2fKb1DjWinQwPeFFMrkt8EkG8kBXApqPWUnbinsHGvoebY23DzaeQgnRsTLrGplW3O20V05FuLkhLutVe0Js0097mZQNhc");

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("card[number]", cardDetails.getNumber());
            map.add("card[exp_month]", String.valueOf(cardDetails.getExpMonth()));
            map.add("card[exp_year]", String.valueOf(cardDetails.getExpYear()));
            map.add("card[cvc]", cardDetails.getCvc());

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity("https://api.stripe.com/v1/tokens", request, String.class);

            String stripeToken = extractTokenFromResponse(response.getBody());

            // Étape 2: Charger le portefeuille
            BigDecimal amountToCharge = cardDetails.getAmount();
            if (!cardDetails.getCurrency().equals(wallet.getRealCurrency())) {
                BigDecimal rate = walletService.getRate(String.valueOf(cardDetails.getCurrency()), String.valueOf(wallet.getRealCurrency()));
                amountToCharge = cardDetails.getAmount().multiply(rate);
            }

            Charge charge = stripe.chargeWallet(wallet, stripeToken, amountToCharge);

            // Mettre à jour le solde réel du portefeuille
            BigDecimal newRealBalance = wallet.getReal_balance().add(amountToCharge);
            wallet.setReal_balance(newRealBalance);
            wr.save(wallet);

            // Enregistrement dans HistoriqueChargement
            HistoriqueChargement historique = new HistoriqueChargement();
            historique.setUser(wallet.getUser());
            historique.setStripeChargeId(charge.getId());
            historique.setAmount(new BigDecimal(String.valueOf(cardDetails.getAmount())));
            historique.setAmount_conv(new BigDecimal(charge.getAmount()));
            historique.setCurrency(cardDetails.getCurrency());
            historique.setCard(wallet.getCard());
            historique.setDateTransaction(LocalDateTime.now());
            historiqueChargementRepository.save(historique);

            ChargeResponseDTO responseDTO = new ChargeResponseDTO();
            responseDTO.setId(charge.getId());
            responseDTO.setAmount(charge.getAmount());
            responseDTO.setCurrency(charge.getCurrency());

            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue lors du chargement de votre portefeuille. Veuillez réessayer.");
        }
    }
    @PostMapping("/createCard/{userId}")
    public ResponseEntity<?> createCardForUser(@PathVariable Long userId, @RequestBody Card card) {
        try {
            // Vérifier la carte avec Stripe
            ResponseEntity<String> stripeResponse = createStripeToken(card);

            if (stripeResponse.getStatusCode() == HttpStatus.OK) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(stripeResponse.getBody());
                String cardBrand = rootNode.path("card").path("brand").asText();

                if (cardBrand != null && !cardBrand.isEmpty()) {
                    card.setCardType(cardBrand); // Mettre à jour le type de carte dans votre objet card
                } else {
                    // Gestion si le champ 'brand' n'est pas trouvé ou est vide.
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossible de déterminer le type de la carte.");
                }

                // Si Stripe renvoie un token, la carte est valide.
                Card newCard = walletService.createCardForUser(userId, card);
                return ResponseEntity.ok("La carte de type " + cardBrand + " a été bien enregistrée et associée à votre portefeuille.");
            } else {
                // Si Stripe ne renvoie pas un token, il y a une erreur avec la carte.
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vérifiez que les informations de votre carte sont correctes.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue. Vérifiez que les informations de votre carte sont correctes.");
        }

    }
    @GetMapping("/Historique/{userId}")
    public ResponseEntity<List<HistoriqueChargement>> getHistoriqueByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(walletService.getHistoriqueByUserId(userId));
    }

    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<Card> getCardByIdWallet(@PathVariable Long walletId) {
        return ResponseEntity.ok(walletService.getCardByIdWallet(walletId));
    }

    @GetMapping("/getAllCards")
    public ResponseEntity<List<Card>> getAllCards() {
        return ResponseEntity.ok(walletService.getAllCards());
    }
    @GetMapping("/getAllHistorique")
    public ResponseEntity<List<HistoriqueChargement>> getAllHistorique() {
        return ResponseEntity.ok(walletService.getAllHistorique());
    }

    @PutMapping("/updateCardById/{cardId}")
    public ResponseEntity<Card> updateCardById(@PathVariable Long cardId, @RequestBody Card updatedCard) {
        return ResponseEntity.ok(walletService.updateCardById(cardId, updatedCard));
    }
    @PostMapping("/convertCurrency")
    public ResponseEntity<BigDecimal> convertCurrency(@RequestBody CurrencyConversionRequest request) {

        try {
            BigDecimal convertedAmount = walletService.convertCurrency(
                    request.getAmount(),
                    request.getFromCurrencyCode(),
                    request.getToCurrencyCode()
            );
            return ResponseEntity.ok(convertedAmount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}

