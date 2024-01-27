package com.example.pifinance_back.Services;
import com.example.pifinance_back.Entities.*;
import com.example.pifinance_back.Repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class MariemService implements IMariemService  {
    private static final Logger logger = LoggerFactory.getLogger(MariemService.class);

    @Autowired
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    private final OrdreAchatRepository ordreAchatRepository;
    @Autowired
    private final PortfolioRepository portfolioRepository;

    @Autowired
    private final OrdreVenteRepository ordreVenteRepository;
    @Autowired
    private final TransactionRepository transactionRepository;
    @Autowired
    private final TitreRepository titreRepository ;
    /* private  boolean marcheOuvert; // Par défaut, le marché est ouvert*/

    private final AlphaVantageConfig alphaVantageConfig;

    private final ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ClientRepository clientRepository;
@Autowired
private  WalletRepository walletRepository ;
    @Autowired
    private JavaMailSender mailSender;




@Override
public List<TitreDTO> getTitres() {
            List<Titre> titres = titreRepository.findAll();
            List<TitreDTO> titresDTO = new ArrayList<>();

            for (Titre titre : titres) {
                TitreDTO dto = TitreDTO.mapToDTO(titre);
                titresDTO.add(dto);
            }

            return titresDTO;
        }
    @Override

    public List<OrdreAchat> getOrdresByType(TypeOrdre typeOrdre) {
        return ordreAchatRepository.findByTypeOrdre(typeOrdre);
    }
    @Override

    public List<OrdreVente> getOrdresVenteByType(TypeOrdre typeOrdre) {
        return ordreVenteRepository.findByTypeOrdre(typeOrdre);
    }

@Override

    public OrdreAchatLimiteDTO toOrdreAchatLimiteDTO(OrdreAchat ordre) {
        return new OrdreAchatLimiteDTO(
                ordre.getIdOrdreA(),
                ordre.getPrixLimiteA(),
                ordre.getQuantite(),
                ordre.getDate_ordreA(),
                ordre.getStatut(),
                ordre.getDureeValidite(),
                ordre.getTitre().getSymbole()
                // autres attributs
        );
    }
@Override
    public OrdreVenteLimiteDTO toOrdreVenteLimiteDTO(OrdreVente ordre){
        return new OrdreVenteLimiteDTO(
                ordre.getIdOrdreV(),
                ordre.getPrixLimiteV(),
                ordre.getQuantite(),
                ordre.getDate_ordreV(),
                ordre.getStatut(),
                ordre.getTitre().getSymbole(),
                ordre.getDureeValidite()


                // autres attributs
        );
    }

    @Override

    public OrdreAchatAuMarcheDTO toOrdreAchatAuMarcheDTO(OrdreAchat ordre) {
        return new OrdreAchatAuMarcheDTO(
                ordre.getIdOrdreA(),
                ordre.getPrixAuMarcheA(),
                ordre.getQuantite(),
                ordre.getDate_ordreA(),
                ordre.getStatut(),
                ordre.getTitre().getSymbole()


                // autres attributs
        );
    }
    @Override

    public OrdreVenteAuMarcheDTO toOrdreVenteAuMarcheDTO(OrdreVente ordre){

        return new OrdreVenteAuMarcheDTO(
                ordre.getIdOrdreV(),
                ordre.getPrixAuMarcheV(),
                ordre.getQuantite(),
                ordre.getDate_ordreV(),
                ordre.getStatut(),
                ordre.getTitre().getSymbole()

                // autres attributs
        );
    }
    @Override
    public String afficherDonnees(String symbol, String interval) {
        // Utilisez la clé API Alpha Vantage pour récupérer les données
        String apiKey = alphaVantageConfig.getAlphaVantageApiKey();

        // Construire l'URL pour l'appel API
        String apiUrl = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol="
                + symbol + "&interval=" + interval + "&apikey=" + apiKey;

        // Utiliser RestTemplate pour effectuer la requête HTTP
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

        // Retourner la réponse au lieu de l'imprimer dans la console
        return response.getBody();
    }
    @Override
    public void afficherToutesLesDonnees(List<String> symbols, String interval) {
        // Récupérer les données pour chaque symbole dans la liste
        for (String symbol : symbols) {
            afficherDonnees(symbol, interval);
        }
    }
    @Override
    public List<String> obtenirSymbolesDynamiques() {
        // Liste de 10 symboles pour la démonstration
        return Arrays.asList("AAPL", "MSFT", "GOOGL", "AMZN", "TSLA", "FB", "NVDA", "JPM", "V", "DIS");
    }




    public void passerOrdreVente(int idTitre, TypeOrdre typeOrdre, int quantite, Double prixLimiteV, DureeValidite dureeValidite, Long clientId){
        Titre titre = titreRepository.findById(idTitre).orElse(null);

        if (titre == null) {
            // Gérer l'erreur : Titre n'existe pas
            throw new TitreNotFoundException("Le titre avec l'ID " + idTitre + " n'existe pas.");
        }

        // Now the clientId is included in the signatures of the methods below.
        if (typeOrdre == TypeOrdre.AU_MARCHE) {
            passerOrdreVenteAuMarche(titre, quantite, clientId);
        } else if (typeOrdre == TypeOrdre.LIMITE) {
            passerOrdreVenteLimite(titre, quantite, prixLimiteV, dureeValidite, clientId);
        }
    }
    private void passerOrdreVenteAuMarche(Titre titre, int quantite, Long clientId) {
        logger.info("Passer un ordre de vente au marché pour le titre avec ID {} et quantité {}", titre.getIdTitre(), quantite);
        Client client = clientRepository.findClientById(clientId);
        Portfolio portfolio = portfolioRepository.findByClientId((long) Math.toIntExact(clientId)); // Assurez-vous que cette méthode existe dans votre repository

        if (portfolio == null) {
            throw new RuntimeException("Le portefeuille pour ce client n'existe pas.");
        }

        String symboleTitre = titre.getSymbole();
        List<String> symbolesDansPortfolio = portfolio.getSymboles();

        if (!symbolesDansPortfolio.contains(symboleTitre)) {
            throw new RuntimeException("Le titre spécifié n'existe pas dans le portefeuille de ce client.");
        }

// Réalisez l'opération sur le titre avec ce symbole existant dans le portfolio
        int indexSymbol = symbolesDansPortfolio.indexOf(symboleTitre);
        int quantiteDisponibleDansPortefeuille = portfolio.getQuantites().get(indexSymbol);

        if (quantiteDisponibleDansPortefeuille < quantite) {
            throw new RuntimeException("La quantité disponible dans le portefeuille est insuffisante pour effectuer cette vente.");
        }

        // Réduisez la quantité ou supprimez le symbole du portefeuille
        int nouvelleQuantite = quantiteDisponibleDansPortefeuille - quantite;
        if (nouvelleQuantite > 0) {
            portfolio.getQuantites().set(indexSymbol, nouvelleQuantite);
        } else {
            portfolio.getSymboles().remove(indexSymbol);
            portfolio.getQuantites().remove(indexSymbol);
        }


        logger.info("Le portefeuille du client a été mis à jour.");


        // On filtre pour exclure les ordres du client en question
        List<OrdreAchat> ordresAchatLimiteExclus = ordreAchatRepository.findByTitreAndTypeOrdre(titre, TypeOrdre.LIMITE)
                .stream()
                .filter(ordreAchat -> !ordreAchat.getClient().getId().equals(clientId)) // Exclure les ordres du client
                .filter(ordreAchat -> {
                    Wallet acheteurWallet = walletRepository.findWalletDetailsById(ordreAchat.getClient().getId());
                    return acheteurWallet != null && acheteurWallet.isActive(); // Conserver uniquement si le wallet est actif
                })
                .collect(Collectors.toList());

        if (ordresAchatLimiteExclus.isEmpty()) {
            throw new AucunOrdreAchatLimiteException("Aucun ordre d'achat limite disponible pour ce titre après exclusion des ordres du client.");
        }

        // Tri des ordres par prix décroissant et par date d'ordre croissante en cas de même prix
        ordresAchatLimiteExclus.sort((o1, o2) -> {
            int comparePrix = Double.compare(o2.getPrixLimiteA(), o1.getPrixLimiteA());
            if (comparePrix == 0) {
                return o1.getDate_ordreA().compareTo(o2.getDate_ordreA());
            }
            return comparePrix;
        });
        // Retrieve and check the wallet for active status
        Long id = walletRepository.findWalletIdByUserId(clientId);
        Wallet clientWallet = walletRepository.findWalletDetailsById(id);
        Client acheteur = clientRepository.findClientById(clientId);
        String EmailVendeur = acheteur.getEmail();
        String NameVendeur = acheteur.getName();
        logger.info("Fetching wallet for client ID: {}", id);
        logger.info("Adresse e-mail du Vendeur: {}", EmailVendeur);

        logger.info("Wallet retrieved: {}, Active status: {}", clientWallet, clientWallet != null ? clientWallet.isActive() : "Wallet is null");

        if (clientWallet == null || !clientWallet.isActive()) {
            throw new WalletNotActiveException("Veuillez activer votre portefeuille.");
        }


        double prixTotal = 0;
        int quantiteTotalVendu = 0;

        String EmailAcheteur = null;
        String NameAcheteur = null;
        int quantitéachat = 0;
        double prixachat = 0;
        TypeTransaction typeachat = null;
        LocalDateTime dateachat = null;
        int quantiteachat = 0;
        for (OrdreAchat ordreAchatLimite : ordresAchatLimiteExclus) {
            if (quantiteTotalVendu < quantite) {
                int quantiteDisponible = ordreAchatLimite.getQuantite();
                int quantiteAexecuter = Math.min(quantite - quantiteTotalVendu, quantiteDisponible);
                double prixAchat = ordreAchatLimite.getPrixLimiteA();

                prixTotal += prixAchat * quantiteAexecuter;
                quantiteTotalVendu += quantiteAexecuter;

                ordreAchatLimite.setQuantite(quantiteDisponible - quantiteAexecuter);
                ordreAchatLimite.setStatut(ordreAchatLimite.getQuantite() > 0 ? StatutOrdre.Partiellement_Executé : StatutOrdre.Executé);
                ordreAchatRepository.save(ordreAchatLimite);
                // Mettre à jour le solde du vendeur
                Long idAcheteur = ordreAchatLimite.getClient().getId();
                Long idWalletacheteur = walletRepository.findWalletIdByUserId(idAcheteur);
                EmailAcheteur = ordreAchatLimite.getClient().getEmail();
                NameAcheteur = ordreAchatLimite.getClient().getName();

                logger.info("Adresse e-mail de l'acheteur: {}", EmailAcheteur);

                Wallet acheteurWallet = walletRepository.findWalletDetailsById(idWalletacheteur);
                logger.info("ID du vendeur: {}", idAcheteur);
                logger.info("Vendeur Wallet retrieved: {}, Active status: {}", acheteurWallet, acheteurWallet != null ? acheteurWallet.isActive() : "Wallet is null");
                if (acheteurWallet != null) {
                    double montantAchat = prixAchat * quantiteAexecuter;
                    BigDecimal montantAchatBigDecimal = BigDecimal.valueOf(montantAchat);
                    // Soustraire le montant de l'achat du solde virtuel actuel
                    BigDecimal newVirtualBalance = acheteurWallet.getVirtual_balance().subtract(montantAchatBigDecimal);

                    // Mettre à jour le solde virtuel du wallet de l'acheteur
                    walletRepository.updateVirtualBalance(newVirtualBalance, idWalletacheteur);
                } else {
                    // Handle the case where the vendeurWallet could not be retrieved
                    logger.error("Le wallet du vendeur avec ID {} n'a pas été trouvé ou est nul.", idAcheteur);
                    // You should also handle this scenario appropriately, e.g., throw an exception or whatever is appropriate in your application context.
                }

                Transaction transactionAchat = new Transaction();
                transactionAchat.setPrixExecution(prixAchat);
                transactionAchat.setDateExecution(LocalDateTime.now());
                transactionAchat.setTypeTransaction(TypeTransaction.ACHAT_LIMITE);
                transactionAchat.setQuantiteExecute(quantiteAexecuter);
                ordreAchatLimite.setTransaction(transactionAchat);
                ordreAchatRepository.save(ordreAchatLimite);

                if (quantiteTotalVendu >= quantite) {
                    break;
                }
            }
            quantiteachat = ordreAchatLimite.getTransaction().getQuantiteExecute();
            prixachat = ordreAchatLimite.getTransaction().getPrixExecution();
            typeachat = ordreAchatLimite.getTransaction().getTypeTransaction();
            dateachat = ordreAchatLimite.getTransaction().getDateExecution();

        }

        if (quantiteTotalVendu < quantite) {
            throw new QuantiteInsuffisanteException("Quantité insuffisante pour exécuter l'ordre de vente au marché.");
        }

        // Deduct total purchase cost from wallet's virtual balance
        BigDecimal newClientVirtualBalance = clientWallet.getVirtual_balance().add(BigDecimal.valueOf(prixTotal));
        walletRepository.updateVirtualBalance(newClientVirtualBalance, id);
        // Sauvegardez les changements dans le portefeuille avant d’exécuter la vente
        portfolioRepository.save(portfolio);

        double prixAuMarcheV = prixTotal / quantiteTotalVendu;

        OrdreVente ordreVente = new OrdreVente();
        ordreVente.setPrixAuMarcheV(prixAuMarcheV);
        ordreVente.setQuantite(quantite);
        ordreVente.setDate_ordreV(LocalDateTime.now());
        ordreVente.setTitre(titre);
        ordreVente.setTypeOrdre(TypeOrdre.AU_MARCHE);
        ordreVente.setStatut(StatutOrdre.Executé);
        ordreVente.setClient(client);

        Transaction transactionVente = new Transaction();
        transactionVente.setPrixExecution(prixAuMarcheV);
        transactionVente.setDateExecution(LocalDateTime.now());
        transactionVente.setTypeTransaction(TypeTransaction.VENTE_AU_MARCHE);
        transactionVente.setQuantiteExecute(quantiteTotalVendu);
        ordreVente.setTransaction(transactionVente);
        int quantitévente = transactionVente.getQuantiteExecute();
        double prixvente = transactionVente.getPrixExecution();
        TypeTransaction typevente = transactionVente.getTypeTransaction();
        LocalDateTime dateVente = transactionVente.getDateExecution();

        ordreVenteRepository.save(ordreVente);

        logger.info("Ordre de vente au marché exécuté: {} titres à un prix moyen pondéré de {}", quantiteTotalVendu, prixAuMarcheV);

        TransactionEvent transactionEvent = new TransactionEvent(this, transactionVente);
        applicationEventPublisher.publishEvent(transactionEvent);
        sendNotificationVendeur(EmailVendeur, NameVendeur, quantitévente, prixvente, typevente, dateVente);
        SendNotificationAcheteur(EmailAcheteur, NameAcheteur, quantiteachat, prixachat, typeachat, dateachat);

    }
    public void sendNotificationVendeur(String recipientEmail, String recipientName, int quantite, double prixvente, TypeTransaction typevente, LocalDateTime dateVente) {
        StringBuilder messageBody = new StringBuilder();
        messageBody.append("Cher(e) ").append(recipientName).append(",\n\n");
        messageBody.append("Félicitations ! Votre Passage Ordre de Vente Au marché a été exécuté avec succès.\n\n");
        messageBody.append("Informations de la transaction :\n");
        messageBody.append("Quantité exécutée : ").append(quantite).append("\n");
        messageBody.append("Prix d'exécution : ").append(prixvente).append("\n");
        messageBody.append("Type de transaction : ").append(typevente).append("\n");
        messageBody.append("Date d'exécution : ").append(dateVente).append("\n\n");
        messageBody.append("TradeHub");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Passage Ordre de Vente au Marché est Passé Avec Succès !");
        message.setText(messageBody.toString());

        mailSender.send(message);
    }
    public void SendNotificationAcheteur(String recipientEmail, String recipientName , int quantite, double prixvente, TypeTransaction typevente, LocalDateTime dateVente) {
        StringBuilder messageBody = new StringBuilder();
        messageBody.append("Cher(e) ").append(recipientName).append(",\n\n");
        messageBody.append("Félicitations ! Votre Passage Ordre d'achat limite a été exécuté avec succès.\n\n");
        messageBody.append("Informations de la transaction :\n");
        messageBody.append("Quantité exécutée : ").append(quantite).append("\n");
        messageBody.append("Prix d'exécution : ").append(prixvente).append("\n");
        messageBody.append("Type de transaction : ").append(typevente).append("\n");
        messageBody.append("Date d'exécution : ").append(dateVente).append("\n\n");
        messageBody.append("TradeHub");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Passage Ordre d'achat au Limite est Passé Avec Succès !");
        message.setText(messageBody.toString());

        mailSender.send(message);
    }

    private void passerOrdreVenteLimite(Titre titre, int quantite, Double prixLimiteV, DureeValidite dureeValidite, Long clientId){
        // Récupérer l'utilisateur par son ID
        Client client = clientRepository.findClientById(clientId);
        Portfolio portfolio = portfolioRepository.findByClientId(clientId);

        if (portfolio == null) {
            throw new RuntimeException("Le portefeuille pour ce client n'existe pas.");
        }

        String symboleTitre = titre.getSymbole();
        List<String> symbolesDansPortfolio = portfolio.getSymboles();

        if (!symbolesDansPortfolio.contains(symboleTitre)) {
            throw new RuntimeException("Le titre spécifié n'existe pas dans le portefeuille de ce client.");
        }

        int indexSymbol = symbolesDansPortfolio.indexOf(symboleTitre);
        int quantiteDisponibleDansPortefeuille = portfolio.getQuantites().get(indexSymbol);

        if (quantiteDisponibleDansPortefeuille < quantite) {
            throw new RuntimeException("La quantité disponible dans le portefeuille est insuffisante pour effectuer cette vente.");
        }

        // Enregistrement d'un nouvel ordre de vente à prix limite en attente
        OrdreVente ordreVente = new OrdreVente();
        ordreVente.setPrixLimiteV(prixLimiteV);
        ordreVente.setQuantite(quantite);
        ordreVente.setDate_ordreV(LocalDateTime.now());
        ordreVente.setTypeOrdre(TypeOrdre.LIMITE);
        ordreVente.setStatut(StatutOrdre.EnAttente);
        ordreVente.setTitre(titre);
        ordreVente.setClient(client);

        // Vérifier si la valeur est spécifiée dans la requête Postman
        if (dureeValidite != null) {
            ordreVente.setDureeValidite(dureeValidite);
        } else {
            // Utiliser la valeur par défaut si aucune valeur n'est spécifiée
            ordreVente.setDureeValidite(DureeValidite.JOUR_MEME);
        }

        ordreVenteRepository.save(ordreVente);
        // Mettre à jour la quantité du titre avec la nouvelle valeur calculée
        updateQuantiteTitre(titre);
        titreRepository.save(titre);
    }

    private void updateQuantiteTitre(Titre titre) {
        // Récupérer tous les ordres de vente limites associés à ce titre
        List<OrdreVente> ordresVenteLimite = ordreVenteRepository.findByTitreAndTypeOrdre(titre, TypeOrdre.LIMITE);

        // Utiliser Stream pour calculer la somme des quantités de tous les ordres de vente limites
        int nouvelleQuantite = ordresVenteLimite.stream().mapToInt(OrdreVente::getQuantite).sum();

        // Mettre à jour la quantité du titre avec la nouvelle valeur calculée
        titre.setQuantite(nouvelleQuantite);
    }



    public void passerOrdreAchat(int idTitre, TypeOrdre typeOrdre, int quantite, Double prixLimiteA, DureeValidite dureeValidite, Long clientId) {
        Titre titre = titreRepository.findById(idTitre).orElse(null);

        if (titre == null) {
            // Gérer l'erreur : Titre n'existe pas
            throw new TitreNotFoundException("Le titre avec l'ID " + idTitre + " n'existe pas.");
        }

        if (typeOrdre == TypeOrdre.AU_MARCHE) {
            passerOrdreAchatAuMarche(titre, quantite, clientId);
        } else if (typeOrdre == TypeOrdre.LIMITE) {
            passerOrdreAchatLimite(titre, quantite, prixLimiteA, dureeValidite,clientId);
        }
    }


@Transactional
    public void passerOrdreAchatAuMarche(Titre titre, int quantite, Long clientId) {
    List<OrdreVente> ordresVenteLimite = ordreVenteRepository.findByTitreAndTypeOrdre(titre, TypeOrdre.LIMITE);

    logger.info("Passer un ordre d'achat au marché pour le titre avec ID {} et quantité {}",
                titre.getIdTitre(), quantite);

        // Retrieve active orders that aren't by the client
    List<OrdreVente> ordresVenteLimiteExclus = ordreVenteRepository.findByTitreAndTypeOrdre(titre, TypeOrdre.LIMITE)
            .stream()
            .filter(ordreVente -> !ordreVente.getClient().getId().equals(clientId))
            .filter(ordreVente -> {
                Wallet vendeurWallet = walletRepository.findWalletDetailsById(ordreVente.getClient().getId());
                return vendeurWallet != null && vendeurWallet.isActive();
            })
            .collect(Collectors.toList());

        if (ordresVenteLimiteExclus.isEmpty()) {
            throw new AucunOrdreVenteLimiteException("Aucun ordre de vente limite disponible pour ce titre.");
        }
    Collections.sort(ordresVenteLimite, (o1, o2) -> {
        int comparePrix = Double.compare(o1.getPrixLimiteV(), o2.getPrixLimiteV());
        if (comparePrix == 0) {
            return o1.getDate_ordreV().compareTo(o2.getDate_ordreV());
        }
        return comparePrix;
    });

        // Retrieve and check the wallet for active status
        Long id = walletRepository.findWalletIdByUserId(clientId);
        Wallet clientWallet = walletRepository.findWalletDetailsById(id);

        logger.info("Fetching wallet for client ID: {}", id);

        logger.info("Wallet retrieved: {}, Active status: {}", clientWallet, clientWallet != null ? clientWallet.isActive() : "Wallet is null");

        if (clientWallet == null || !clientWallet.isActive()) {
            throw new WalletNotActiveException("Veuillez activer votre portefeuille.");
        }

        // Calculate total possible cost and check if the client's wallet has enough funds
        double estimatedTotalCost = ordresVenteLimiteExclus.stream()
                .mapToDouble(ordreVente -> ordreVente.getPrixLimiteV() * Math.min(quantite, ordreVente.getQuantite()))
                .sum();

        if (clientWallet.getVirtual_balance().compareTo(BigDecimal.valueOf(estimatedTotalCost)) < 0) {
            throw new InsufficientFundsException("Fonds virtuels insuffisants pour effectuer cette transaction.");
        }

        double prixTotal = 0;
        int quantiteTotalAchetee = 0;

        // Process sales orders while there's enough quantity and buy orders
        for (OrdreVente ordreVenteLimite : ordresVenteLimiteExclus) {
            if (quantiteTotalAchetee < quantite) {
                int quantiteDisponible = ordreVenteLimite.getQuantite();
                int quantiteAexecuter = Math.min(quantite - quantiteTotalAchetee, quantiteDisponible);
                double prixVente = ordreVenteLimite.getPrixLimiteV();

                prixTotal += prixVente * quantiteAexecuter;
                quantiteTotalAchetee += quantiteAexecuter;

                ordreVenteLimite.setQuantite(quantiteDisponible - quantiteAexecuter);
                ordreVenteLimite.setStatut(ordreVenteLimite.getQuantite() > 0 ? StatutOrdre.Partiellement_Executé : StatutOrdre.Executé);
                // Mettre à jour le solde du vendeur
                Long idVendeur = ordreVenteLimite.getClient().getId();
                Long idWalletvendeur = walletRepository.findWalletIdByUserId(idVendeur);

                Wallet vendeurWallet = walletRepository.findWalletDetailsById(idWalletvendeur);
                logger.info("ID du vendeur: {}", idVendeur);
                logger.info("Vendeur Wallet retrieved: {}, Active status: {}", vendeurWallet, vendeurWallet != null ? vendeurWallet.isActive() : "Wallet is null");

                if (vendeurWallet != null) {
                    double montantVente = prixVente * quantiteAexecuter;
                    BigDecimal montantVenteBigDecimal = BigDecimal.valueOf(montantVente);
                    BigDecimal newVirtualBalance = vendeurWallet.getVirtual_balance().add(montantVenteBigDecimal);
                    walletRepository.updateVirtualBalance(newVirtualBalance, idWalletvendeur);

                    logger.info("Le solde du wallet du vendeur a été augmenté de: {}", montantVente);
                } else {
                    // Handle the case where the vendeurWallet could not be retrieved
                    logger.error("Le wallet du vendeur avec ID {} n'a pas été trouvé ou est nul.", idVendeur);
                    // You should also handle this scenario appropriately, e.g., throw an exception or whatever is appropriate in your application context.
                }
                // Log ID du vendeur et wallet avant la mise à jour

                Transaction transactionVente = new Transaction();
                transactionVente.setPrixExecution(prixVente);
                transactionVente.setDateExecution(LocalDateTime.now());
                transactionVente.setTypeTransaction(TypeTransaction.VENTE_LIMITE);
                transactionVente.setQuantiteExecute(quantiteAexecuter);

                // Set the sale transaction to the order
                ordreVenteLimite.setTransaction(transactionVente);
                ordreVenteRepository.save(ordreVenteLimite);

                if (quantiteTotalAchetee >= quantite) {
                    break;
                }
            }
        }

        if (quantiteTotalAchetee < quantite) {
            throw new QuantiteInsuffisanteException("Quantité insuffisante pour exécuter l'ordre de vente au marché.");
        }

        // Deduct total purchase cost from wallet's virtual balance
    BigDecimal newClientVirtualBalance = clientWallet.getVirtual_balance().subtract(BigDecimal.valueOf(prixTotal));

    Client client = clientRepository.findClientById(clientId);
    walletRepository.updateVirtualBalance(newClientVirtualBalance, id);

        // Create and save purchase order
        double prixAuMarcheA = prixTotal / quantiteTotalAchetee;
        OrdreAchat ordreAchat = new OrdreAchat();
        ordreAchat.setPrixAuMarcheA(prixAuMarcheA);
        ordreAchat.setQuantite(quantite);
        ordreAchat.setDate_ordreA(LocalDateTime.now());
        ordreAchat.setTitre(titre);
        ordreAchat.setTypeOrdre(TypeOrdre.AU_MARCHE);
        ordreAchat.setStatut(StatutOrdre.Executé);
        ordreAchat.setClient(client);
    Portfolio portfolioAcheteur = portfolioRepository.findByClientId(clientId);
    if (portfolioAcheteur == null) {
        portfolioAcheteur = new Portfolio();
        // Initialiser les autres champs nécessaires du portfolio
    }
    List<String> symbolesDansPortfolio = portfolioAcheteur.getSymboles();

    int indexSymbol;
    int nouvelleQuantite;
    if (symbolesDansPortfolio.contains(titre.getSymbole())) {
        indexSymbol = symbolesDansPortfolio.indexOf(titre.getSymbole());
        int quantiteActuelle = portfolioAcheteur.getQuantites().get(indexSymbol);
        nouvelleQuantite = quantiteActuelle + quantiteTotalAchetee;
        portfolioAcheteur.getQuantites().set(indexSymbol, nouvelleQuantite);
    } else {
        symbolesDansPortfolio.add(titre.getSymbole());
        portfolioAcheteur.getQuantites().add(quantiteTotalAchetee);
    }
    portfolioRepository.save(portfolioAcheteur);

        Transaction transactionAchat = new Transaction();
        transactionAchat.setPrixExecution(prixAuMarcheA);
        transactionAchat.setDateExecution(LocalDateTime.now());
        transactionAchat.setTypeTransaction(TypeTransaction.ACHAT_AU_MARCHE);
        transactionAchat.setQuantiteExecute(quantiteTotalAchetee);

        // Associate transaction with purchase order
        ordreAchat.setTransaction(transactionAchat);
        ordreAchatRepository.save(ordreAchat);

        logger.info("Ordre d'achat au marché exécuté: {} titres à un prix moyen pondéré de {}", quantiteTotalAchetee, prixAuMarcheA);

        // Publish event to signal transaction completion
        TransactionEvent transactionEvent = new TransactionEvent(this, transactionAchat);
        applicationEventPublisher.publishEvent(transactionEvent);
    }

    private void passerOrdreAchatLimite(Titre titre, int quantite, Double prixLimiteA, DureeValidite dureeValidite, Long clientId) {
        // Récupérer l'utilisateur par son ID
        Client client = clientRepository.findClientById(clientId);

        logger.info("Passer un ordre d'achat limite pour le titre avec ID {}, quantité {} et prix limite {}", titre.getIdTitre(), quantite, prixLimiteA);

        // Enregistrement d'un nouvel ordre d'achat à prix limite en attente
        OrdreAchat ordreAchat = new OrdreAchat();
        ordreAchat.setPrixLimiteA(prixLimiteA);
        ordreAchat.setQuantite(quantite);
        ordreAchat.setDate_ordreA(LocalDateTime.now());
        ordreAchat.setTypeOrdre(TypeOrdre.LIMITE);
        ordreAchat.setStatut(StatutOrdre.EnAttente);
        ordreAchat.setTitre(titre);
        ordreAchat.setClient(client);

        // Vérifier si la valeur est spécifiée dans la requête Postman
        if (dureeValidite != null) {
            ordreAchat.setDureeValidite(dureeValidite);
        } else {
            // Utiliser la valeur par défaut si aucune valeur n'est spécifiée
            ordreAchat.setDureeValidite(DureeValidite.JOUR_MEME);
        }

        // Assurez-vous de définir le prix limite avant de sauvegarder
        ordreAchatRepository.save(ordreAchat);

        // Mettre à jour la quantité du titre avec la nouvelle valeur calculée
        updateQuantiteTitre(titre);
        titreRepository.save(titre);
    }




   /* @Override
    @Scheduled(fixedRate = 180000) // Exécution toutes les 3 minutes (en millisecondes)
    public void simulerVariationPrixPourTousLesTitres() {
        // Obtenez la liste de tous les actifs financiers en utilisant la méthode findAll
        List<Titre> tousLesTitres = titreRepository.findAll();

        double elasticite = 0.01; // Exemple : 1%

        for (Titre titre : tousLesTitres) {
            simulerVariationPrix(titre, elasticite);
        }
    }
    public void simulerVariationPrix(Titre titre, double elasticite) {
        double offreTotale = 0;
        double demandeTotale = 0;

        // Calculer l'offre et la demande totales comme expliqué précédemment
        for (Ordre ordre : titre.getOrdres()) {
            if (ordre.getSens() == SensOrdre.Achat) {
                demandeTotale += ordre.getQuantite();
            } else if (ordre.getSens() == SensOrdre.Vente) {
                offreTotale += ordre.getQuantite();
            }
        }

        // Élasticité des prix (paramètre fourni à la méthode)
        double deltaPrix = 0; // Initialisation

        if (demandeTotale > offreTotale) {
            // Si la demande dépasse l'offre, les prix augmentent de 1% lorsque la demande dépasse l'offre de 10%
            deltaPrix = (demandeTotale - offreTotale) / (offreTotale * 0.1) * elasticite;
        } else if (offreTotale > demandeTotale) {
            // Si l'offre dépasse la demande, les prix diminuent de 1% lorsque l'offre dépasse la demande de 10%
            deltaPrix = (offreTotale - demandeTotale) / (demandeTotale * 0.1) * elasticite;
        }

        // Mettre à jour le prix de l'actif financier en utilisant la méthode setter
        double nouveauPrix = titre.getPrixActuel() + deltaPrix;
        titre.setPrixActuel(nouveauPrix);

        // Enregistrer la mise à jour du prix actuel dans la base de données
        titreRepository.save(titre);

        System.out.println("Nouveau prix pour l'actif financier " + titre.getNom() + ": " + nouveauPrix);
    }
hedhi heya eli chenrajaaha */



    @Override
    @Transactional
    public List<OrdreAchat> getAllOrdresA() {
        return ordreAchatRepository.findAll();
    }




    @Override
    public Titre ajouterTitre(String symbole, String nom, double prixOuverture, int quantite) {
        Titre nouveauTitre = new Titre();
        nouveauTitre.setSymbole(symbole);
        nouveauTitre.setNom(nom);
        nouveauTitre.setPrixOuverture(prixOuverture);
        nouveauTitre.setQuantite(quantite);

        // Valeurs par défaut
        nouveauTitre.setPrixPlusHaut(prixOuverture);
        nouveauTitre.setPrixPlusBas(prixOuverture);
        nouveauTitre.setPrixActuel(prixOuverture);
        nouveauTitre.setDate_Creation(LocalDateTime.now());
        nouveauTitre.setDateMaj(nouveauTitre.getDate_Creation());

        return titreRepository.save(nouveauTitre);
    }


    @Override
    public void annulerOrdre(int idOrdre) {
        OrdreAchat ordre = ordreAchatRepository.findById(idOrdre).orElse(null);
        if (ordre != null) {
            ordre.setStatut(StatutOrdre.Annulé);
            ordreAchatRepository.save(ordre);
        }
    }
    @Override
    @Transactional
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
    @Override
    @Transactional
    public List<Transaction> getTransactionById(Long id) {
        return transactionRepository.findTransactionsByClientId(id);
    }
    @Override
    public void annulerOrdreA(int idOrdre) {
        OrdreAchat ordre = ordreAchatRepository.findById(idOrdre).orElse(null);
        ordre.setStatut(StatutOrdre.Annulé);
        ordreAchatRepository.save(ordre);
    }

    @Override
    public void annulerOrdreV(int idOrdre) {
        OrdreVente ordre = ordreVenteRepository.findById(idOrdre).orElse(null);
        ordre.setStatut(StatutOrdre.Annulé);
        ordreVenteRepository.save(ordre);
    }

    public CarnetOrdre genererCarnetOrdres(Titre titre) {
        // Fetch all the active limit buy orders for a given stock
        List<StatutOrdre> statuts = Arrays.asList(StatutOrdre.EnAttente, StatutOrdre.Partiellement_Executé);

        // Supposons que ordreAchatRepository et ordreVenteRepository sont déjà définis et injectés
        List<OrdreAchat> ordresAchatActifs = ordreAchatRepository.findByTitreAndTypeOrdreAndStatutIn(titre, TypeOrdre.LIMITE, statuts);
        List<OrdreVente> ordresVenteActifs = ordreVenteRepository.findByTitreAndTypeOrdreAndStatutIn(titre, TypeOrdre.LIMITE, statuts);

        // Tri et agrégation des ordres d'achat en OrdreDetail
        List<OrdreDetail> ordresAchatDetails = ordresAchatActifs.stream()
                .collect(Collectors.groupingBy(OrdreAchat::getPrixLimiteA,
                        TreeMap::new,
                        Collectors.summingInt(OrdreAchat::getQuantite)))
                .descendingMap().entrySet()
                .stream()
                .map(e -> {
                    OrdreDetail detail = new OrdreDetail(); // Utilisez le constructeur sans paramètres
                    detail.setPrix(e.getKey()); // Définissez le prix
                    detail.setQuantite(e.getValue()); // Définissez la quantité
                    return detail;
                })
                .collect(Collectors.toList());

        // Tri et agrégation des ordres de vente en OrdreDetail
        List<OrdreDetail> ordresVenteDetails = ordresVenteActifs.stream()
                .collect(Collectors.groupingBy(OrdreVente::getPrixLimiteV,
                        TreeMap::new,
                        Collectors.summingInt(OrdreVente::getQuantite)))
                .entrySet()
                .stream()
                .map(e -> {
                    OrdreDetail detail = new OrdreDetail(); // Utilisez le constructeur sans paramètres
                    detail.setPrix(e.getKey()); // Définissez le prix
                    detail.setQuantite(e.getValue()); // Définissez la quantité
                    return detail;
                })
                .collect(Collectors.toList());

        // Créer le carnet d'ordre et lui affecter les listes d'OrdreDetail
        CarnetOrdre carnetOrdre = new CarnetOrdre();
        carnetOrdre.setOrdresAchat(ordresAchatDetails);
        carnetOrdre.setOrdresVente(ordresVenteDetails);
        carnetOrdre.setTitre(titre);

        // L'ID de CarnetOrdre sera généré automatiquement lors de la sauvegarde
        return carnetOrdre;
    }

 /* @Transactional

    public void matcherOrdres() {
        // Récupérer tous les ordres d'achat et de vente en attente
        List<OrdreAchat> ordresAchat = ordreAchatRepository.findAllByStatut(StatutOrdre.EnAttente);
        List<OrdreVente> ordresVente = ordreVenteRepository.findAllByStatut(StatutOrdre.EnAttente);

        // Trier les listes pour avoir les ordres avec le prix le plus compétitif en premier
        ordresAchat.sort(Comparator.comparing(OrdreAchat::getPrixLimiteA).reversed());
        ordresVente.sort(Comparator.comparing(OrdreVente::getPrixLimiteV));

        // Parcourir les listes pour trouver des correspondances
        for (OrdreAchat ordreAchat : ordresAchat) {
            for (OrdreVente ordreVente : ordresVente) {
                // Vérifier si le prix limite de l'achat est supérieur ou égal au prix limite de la vente
                if (ordreAchat.getPrixLimiteA() >= ordreVente.getPrixLimiteV()) {
                    // Déterminer la quantité négociable
                    int quantiteNego = Math.min(ordreAchat.getQuantite(), ordreVente.getQuantite());

                    // Mise à jour de la quantité des ordres d'achat et de vente
                    ordreAchat.setQuantite(ordreAchat.getQuantite() - quantiteNego);
                    ordreVente.setQuantite(ordreVente.getQuantite() - quantiteNego);

                    // Création des transactions
                    Transaction transactionAchat = new Transaction();
                    transactionAchat.setPrixExecution(ordreVente.getPrixLimiteV());
                    transactionAchat.setDateExecution(LocalDateTime.now());
                    transactionAchat.setTypeTransaction(TypeTransaction.ACHAT_LIMITE);
                    transactionAchat.setQuantiteExecute(quantiteNego);
                    // Enregistrez la transaction - à adapter en fonction de votre implémentation.

                    Transaction transactionVente = new Transaction();
                    transactionVente.setPrixExecution(ordreVente.getPrixLimiteV());
                    transactionVente.setDateExecution(LocalDateTime.now());
                    transactionVente.setTypeTransaction(TypeTransaction.VENTE_LIMITE);
                    transactionVente.setQuantiteExecute(quantiteNego);
                    // Enregistrez la transaction - à adapter en fonction de votre implémentation.

                    // Mise à jour des statuts si l'ordre est complètement exécuté
                    if (ordreAchat.getQuantite() == 0) {
                        ordreAchat.setStatut(StatutOrdre.Executé);
                        // Associer la transaction à l'ordre
                        ordreAchat.setTransaction(transactionAchat);
                        ordreAchatRepository.save(ordreAchat);
                    }
                    if (ordreVente.getQuantite() == 0) {
                        ordreVente.setStatut(StatutOrdre.Executé);
                        // Associer la transaction à l'ordre
                        ordreVente.setTransaction(transactionVente);
                        ordreVenteRepository.save(ordreVente);
                        TransactionEvent transactionEvent = new TransactionEvent(this, transactionAchat);
                        applicationEventPublisher.publishEvent(transactionEvent);
                    }

                    if (ordreAchat.getQuantite() == 0 || ordreVente.getQuantite() == 0) {
                        // Si l'un des ordres a une quantité de 0, alors il a été entièrement exécuté.
                        break; // On sort de la boucle de traitement pour cet ordre
                    }
                }
            }
        }
    }

*/





    // hedhi jawha behi juste ki quantite > 0 statut ma yetbadlch partiellement yokod en attente ama les tests lokhrin validés
   /* @Scheduled(cron = "0 0/10 9-20 * * *", zone = "Europe/Paris")
    @Transactional
    public void matcherOrdres() {
        System.out.println("Début du Matching des ordres - " + LocalDateTime.now(ZoneId.of("Europe/Paris")));

        List<OrdreAchat> ordresAchat = ordreAchatRepository.findAllByStatutIn(EnumSet.of(StatutOrdre.EnAttente, StatutOrdre.Partiellement_Executé));
        List<OrdreVente> ordresVente = ordreVenteRepository.findAllByStatutIn(EnumSet.of(StatutOrdre.EnAttente, StatutOrdre.Partiellement_Executé));
*/
        /*ordresAchat.sort(Comparator.comparing(OrdreAchat::getPrixLimiteA).reversed());
        ordresVente.sort(Comparator.comparing(OrdreVente::getPrixLimiteV));*/
        // Tri des ordres d'achat par prix décroissant, puis par date de création croissante
       /* ordresAchat.sort(
                Comparator.comparing(OrdreAchat::getPrixLimiteA).reversed()
                        .thenComparing(OrdreAchat::getDate_ordreA)
        );

        // Tri des ordres de vente par prix croissant, puis par date de création croissante
        ordresVente.sort(
                Comparator.comparing(OrdreVente::getPrixLimiteV)
                        .thenComparing(OrdreVente::getDate_ordreV)
        );

        for (OrdreAchat ordreAchat : ordresAchat) {
            for (OrdreVente ordreVente : ordresVente) {
                if (ordreAchat.getPrixLimiteA() >= ordreVente.getPrixLimiteV()) {
                    int quantiteNego = Math.min(ordreAchat.getQuantite(), ordreVente.getQuantite());

                    if (quantiteNego > 0) {
                        // Création de la transaction d'achat
                        Transaction transactionAchat = new Transaction();
                        transactionAchat.setPrixExecution(ordreVente.getPrixLimiteV());
                        transactionAchat.setDateExecution(LocalDateTime.now());
                        transactionAchat.setTypeTransaction(TypeTransaction.ACHAT_LIMITE);
                        transactionAchat.setQuantiteExecute(quantiteNego);
                        transactionRepository.save(transactionAchat);

                        // Création de la transaction de vente
                        Transaction transactionVente = new Transaction();
                        transactionVente.setPrixExecution(ordreVente.getPrixLimiteV());
                        transactionVente.setDateExecution(LocalDateTime.now());
                        transactionVente.setTypeTransaction(TypeTransaction.VENTE_LIMITE);
                        transactionVente.setQuantiteExecute(quantiteNego);
                        transactionRepository.save(transactionVente);

                        // Mise à jour des quantités d'ordres
                        ordreAchat.setQuantite(ordreAchat.getQuantite() - quantiteNego);
                        ordreVente.setQuantite(ordreVente.getQuantite() - quantiteNego);

                        // Mise à jour des statuts des ordres
                        if (ordreAchat.getQuantite() > 0) {
                            ordreAchat.setStatut(StatutOrdre.Partiellement_Executé);
                        } else {
                            ordreAchat.setStatut(StatutOrdre.Executé);
                        }

                        // Associe la transaction indépendamment du statut d'exécution de l'ordre
                        ordreAchat.setTransaction(transactionAchat);
                        ordreAchatRepository.save(ordreAchat);

                        if (ordreVente.getQuantite() > 0) {
                            ordreVente.setStatut(StatutOrdre.Partiellement_Executé);
                        } else {
                            ordreVente.setStatut(StatutOrdre.Executé);
                        }

                        // Associe la transaction indépendamment du statut d'exécution de l'ordre
                        ordreVente.setTransaction(transactionVente);
                        ordreVenteRepository.save(ordreVente);


                        // Publication de l'événement
                        TransactionEvent transactionEventAchat = new TransactionEvent(this, transactionAchat);
                        applicationEventPublisher.publishEvent(transactionEventAchat);

                        TransactionEvent transactionEventVente = new TransactionEvent(this, transactionVente);
                        applicationEventPublisher.publishEvent(transactionEventVente);

                        // Si l'ordre d'achat OU de vente est complètement exécuté, arrêtons cette boucle interne
                        if (ordreAchat.getQuantite() == 0 || ordreVente.getQuantite() == 0) {
                            break;
                        }
                    }
                }
            }
        }
        System.out.println("Fin du Matching des ordres - " + LocalDateTime.now(ZoneId.of("Europe/Paris")));

    }



*/
 //   @Scheduled(cron = "0 0/10 9-20 * * *", zone = "Europe/Paris")
    @Transactional
    public void matcherOrdres() {

        // Obtention des ordres achat et vente en attente ou partiellement exécutés
        List<OrdreAchat> ordresAchat = ordreAchatRepository.findAllByStatutIn(EnumSet.of(StatutOrdre.EnAttente, StatutOrdre.Partiellement_Executé));
        List<OrdreVente> ordresVente = ordreVenteRepository.findAllByStatutIn(EnumSet.of(StatutOrdre.EnAttente, StatutOrdre.Partiellement_Executé));

        // Tri des ordres par prix et date
        ordresAchat.sort(
                Comparator.comparing(OrdreAchat::getPrixLimiteA).reversed()
                        .thenComparing(OrdreAchat::getDate_ordreA)
        );
        ordresVente.sort(
                Comparator.comparing(OrdreVente::getPrixLimiteV)
                        .thenComparing(OrdreVente::getDate_ordreV)
        );

        // Matching des ordres
        for (OrdreAchat ordreAchat : ordresAchat) {
            for (OrdreVente ordreVente : ordresVente) {
                // Vérification de la compatibilité des prix
                if (ordreAchat.getPrixLimiteA() >= ordreVente.getPrixLimiteV()) {
                    int quantiteNego = Math.min(ordreAchat.getQuantite(), ordreVente.getQuantite());

                    // S'il y a une quantité négociable
                    if (quantiteNego > 0) {
                        Long idVendeur = ordreVente.getClient().getId();
                        Long idAcheteur = ordreAchat.getClient().getId();
                        Long id_WalletV = walletRepository.findWalletIdByUserId(idVendeur);
                        Long id_WalletA = walletRepository.findWalletIdByUserId(idAcheteur);

                        // Récupérer et vérifier les portefeuilles des clients
                        Wallet walletAcheteur = walletRepository.findWalletDetailsById(id_WalletA);
                        Wallet walletVendeur = walletRepository.findWalletDetailsById(id_WalletV);
// Ajout de la condition pour vérifier si le portefeuille de l'acheteur est actif
                        if (walletAcheteur == null || !walletAcheteur.isActive()) {
                            throw new WalletNotActiveException("Le portefeuille de l'acheteur n'est pas actif.");
                        }

// Ajout de la condition pour vérifier si le portefeuille du vendeur est actif
                        if (walletVendeur == null || !walletVendeur.isActive()) {
                            throw new WalletNotActiveException("Le portefeuille du vendeur n'est pas actif.");
                        }

                        // Calculer le montant de la transaction
                        double montantTransaction = ordreVente.getPrixLimiteV() * quantiteNego;

                        // Vérification des fonds de l'acheteur
                        if (walletAcheteur.getVirtual_balance().compareTo(BigDecimal.valueOf(montantTransaction)) >= 0) {
                            // Création des transactions
                            Transaction transactionAchat = createAndSaveTransactionAchat(ordreAchat, quantiteNego, TypeTransaction.ACHAT_LIMITE);
                            Transaction transactionVente = createAndSaveTransactionVente(ordreVente, quantiteNego, TypeTransaction.VENTE_LIMITE);

                            // Mise à jour des quantités restantes des ordres
                            updateRemainingQuantities(ordreAchat, ordreVente, quantiteNego);

                            // Mise à jour des soldes des portefeuilles
                            updateWalletBalances(walletAcheteur, walletVendeur, montantTransaction);
                            updateBuyerPortfolio(idAcheteur, ordreVente.getTitre().getSymbole(), quantiteNego);

// Mise à jour du portefeuille du vendeur
                            updateSellerPortfolio(idVendeur, ordreVente.getTitre().getSymbole(), quantiteNego);

                            // Publication des événements de transaction
                            publishTransactionEvents(transactionAchat, transactionVente);

                            // Si un ordre est complètement exécuté, quitter la boucle interne
                            if (ordreAchat.getQuantite() == 0 || ordreVente.getQuantite() == 0) {
                                break;
                            }
                        } else {
                            // L'acheteur n'a pas suffisamment de fonds pour la transaction
                            throw new InsufficientFundsException("Fonds insuffisants dans le portefeuille pour terminer la transaction.");
                        }
                    }
                }
            }
        }
    }
    private void updateBuyerPortfolio(Long buyerId, String symbole, int quantiteAchetee) {
        Portfolio portfolioAcheteur = portfolioRepository.findByClientId(buyerId);
        // Vérifiez si le symbole existe déjà, augmentez la quantité sinon ajoutez le nouveau symbole avec sa quantité.
        if (portfolioAcheteur != null) {
            int index = portfolioAcheteur.getSymboles().indexOf(symbole);
            if (index != -1) { // Le symbole existe déjà, alors mise à jour de la quantité
                int nouvelleQuantite = portfolioAcheteur.getQuantites().get(index) + quantiteAchetee;
                portfolioAcheteur.getQuantites().set(index, nouvelleQuantite);
            } else { // Ajout d'un nouveau symbole avec quantité
                portfolioAcheteur.getSymboles().add(symbole);
                portfolioAcheteur.getQuantites().add(quantiteAchetee);
            }
            portfolioRepository.save(portfolioAcheteur);
        } else {
            // Gestion du cas où le Portfolio est null. Création d’un nouveau portfolio peut-être ?
        }
    }

    private void updateSellerPortfolio(Long sellerId, String symbole, int quantiteVendue) {
        Portfolio portfolioVendeur = portfolioRepository.findByClientId(sellerId);
        if (portfolioVendeur != null) {
            int index = portfolioVendeur.getSymboles().indexOf(symbole);
            if (index != -1) { // le symbole existe, mise à jour ou suppression
                int nouvelleQuantite = portfolioVendeur.getQuantites().get(index) - quantiteVendue;
                if (nouvelleQuantite <= 0) { // si la nouvelle quantité est 0 ou moins, retirez le symbole
                    portfolioVendeur.getSymboles().remove(index);
                    portfolioVendeur.getQuantites().remove(index);
                } else {
                    portfolioVendeur.getQuantites().set(index, nouvelleQuantite);
                }
            } // Pas besoin d'ajouter un else car un vendeur devrait toujours avoir le symbole initialement.
            portfolioRepository.save(portfolioVendeur);
        } else {
            // Gestion du cas où le Portfolio est null.
        }
    }

    private Transaction createAndSaveTransactionAchat(OrdreAchat ordreAchat, int quantiteNego, TypeTransaction type) {
        Transaction transaction = new Transaction();
        transaction.setPrixExecution(ordreAchat.getPrixLimiteA());
        transaction.setDateExecution(LocalDateTime.now());
        transaction.setTypeTransaction(type);
        transaction.setQuantiteExecute(quantiteNego);
        transaction = transactionRepository.save(transaction);

        ordreAchat.setTransaction(transaction); // Si vous avez un champ transaction dans votre entité OrdreAchat
        ordreAchatRepository.save(ordreAchat);

        return transaction;
    }

    private Transaction createAndSaveTransactionVente(OrdreVente ordreVente, int quantiteNego, TypeTransaction type) {
        Transaction transaction = new Transaction();
        transaction.setPrixExecution(ordreVente.getPrixLimiteV());
        transaction.setDateExecution(LocalDateTime.now());
        transaction.setTypeTransaction(type);
        transaction.setQuantiteExecute(quantiteNego);
        ordreVente.setTransaction(transaction);
        transaction = transactionRepository.save(transaction);
        ordreVenteRepository.save(ordreVente);

        return transaction;
    }

    private void updateRemainingQuantities(OrdreAchat ordreAchat, OrdreVente ordreVente, int quantiteNego) {
        ordreAchat.setQuantite(ordreAchat.getQuantite() - quantiteNego);
        ordreVente.setQuantite(ordreVente.getQuantite() - quantiteNego);
        ordreAchat.setStatut(ordreAchat.getQuantite() > 0 ? StatutOrdre.Partiellement_Executé : StatutOrdre.Executé);
        ordreVente.setStatut(ordreVente.getQuantite() > 0 ? StatutOrdre.Partiellement_Executé : StatutOrdre.Executé);

        ordreAchatRepository.save(ordreAchat);
        ordreVenteRepository.save(ordreVente);
    }

    private void updateWalletBalances(Wallet walletAcheteur, Wallet walletVendeur, double montantTransaction) {
        BigDecimal newBalanceAcheteur = walletAcheteur.getVirtual_balance().subtract(BigDecimal.valueOf(montantTransaction));
        BigDecimal newBalanceVendeur = walletVendeur.getVirtual_balance().add(BigDecimal.valueOf(montantTransaction));

        int updatedRowsAcheteur = walletRepository.updateVirtualBalance(newBalanceAcheteur, walletAcheteur.getId_wallet());
        int updatedRowsVendeur = walletRepository.updateVirtualBalance(newBalanceVendeur, walletVendeur.getId_wallet());

        if (updatedRowsAcheteur != 1 || updatedRowsVendeur != 1) {
            throw new RuntimeException("Échec de la mise à jour des soldes virtuels des portefeuilles.");
        }
    }

    private void publishTransactionEvents(Transaction transactionAchat, Transaction transactionVente) {
        TransactionEvent transactionEventAchat = new TransactionEvent(this, transactionAchat);
        applicationEventPublisher.publishEvent(transactionEventAchat);
        TransactionEvent transactionEventVente = new TransactionEvent(this, transactionVente);
        applicationEventPublisher.publishEvent(transactionEventVente);
    }
     @Scheduled(cron = "0 0 17 * * MON-FRI", zone = "Europe/Paris")
  //  @Scheduled(cron = "0 34 15 * * ?", zone = "Europe/Paris")
    @Transactional
    public void annulerOrdresAchatEtVenteDuJour() {
        List<StatutOrdre> statuts = Arrays.asList(
                StatutOrdre.EnAttente,
                StatutOrdre.Partiellement_Executé
        );

        // Annuler les ordres d'achat du jour en attente ou partiellement exécutés
        List<OrdreAchat> ordresAchat = ordreAchatRepository.findByDureeValiditeAndStatut(
                DureeValidite.JOUR_MEME,
                statuts
        );
        ordresAchat.forEach(ordre -> {
            ordre.setStatut(StatutOrdre.Annulé);
            ordreAchatRepository.save(ordre);
        });

        // Annuler les ordres de vente du jour en attente ou partiellement exécutés
        List<OrdreVente> ordresVente = ordreVenteRepository.findByDureeValiditeAndStatut(
                DureeValidite.JOUR_MEME,
                statuts
        );
        ordresVente.forEach(ordre -> {
            ordre.setStatut(StatutOrdre.Annulé);
            ordreVenteRepository.save(ordre);
        });

        // Log des opérations
        System.out.println("Ordres d'achat et de vente du jour annulés: " +
                (ordresAchat.size() + ordresVente.size()));
    }
    @Override
    public List<LigneCarnet> genererCarnet(int idTitre) {
        List<StatutOrdre> statutsValides = Arrays.asList(
                StatutOrdre.EnAttente,
                StatutOrdre.Partiellement_Executé);
        TypeOrdre typeLimite = TypeOrdre.LIMITE;

        List<OrdreAchat> ordresAchat = ordreAchatRepository.findByTitreIdTitreAndStatutsAndTypeOrdre(idTitre, statutsValides, typeLimite)
                .stream()
                .sorted(Comparator.comparing(OrdreAchat::getPrixLimiteA))
                .collect(Collectors.toList());
        List<OrdreVente> ordresVente = ordreVenteRepository.findByTitreIdTitreAndStatutsAndTypeOrdre(idTitre, statutsValides, typeLimite)
                .stream()
                .sorted(Comparator.comparing(OrdreVente::getPrixLimiteV).reversed())
                .collect(Collectors.toList());

        Map<Double, LigneCarnet> achatMap = new TreeMap<>(Collections.reverseOrder());
        for (OrdreAchat achat : ordresAchat) {
            achatMap.putIfAbsent(achat.getPrixLimiteA(), new LigneCarnet());
            achatMap.get(achat.getPrixLimiteA()).ajouterOrdreAchat(achat);
        }

        Map<Double, LigneCarnet> venteMap = new TreeMap<>();
        for (OrdreVente vente : ordresVente) {
            venteMap.putIfAbsent(vente.getPrixLimiteV(), new LigneCarnet());
            venteMap.get(vente.getPrixLimiteV()).ajouterOrdreVente(vente);
        }

        List<LigneCarnet> lignesCarnet = new ArrayList<>();

        Iterator<LigneCarnet> itAchat = achatMap.values().iterator();
        Iterator<LigneCarnet> itVente = venteMap.values().iterator();

        while (itAchat.hasNext() || itVente.hasNext()) {
            LigneCarnet ligne = new LigneCarnet();

            if (itAchat.hasNext()) {
                LigneCarnet ligneAchat = itAchat.next();
                ligne.setPrixAchat(ligneAchat.getPrixAchat());
                ligne.setQuantiteAchat(ligneAchat.getQuantiteAchat());
                ligne.setNbOrdresAchat(ligneAchat.getNbOrdresAchat());
            } else {
                // Si la liste d'achats est épuisée mais la liste de ventes a encore des éléments
                // Remplir les attributs d'achat avec des valeurs par défaut (0 ou null)
                ligne.setPrixAchat(0.0); // Remplacez par la valeur par défaut souhaitée pour le prix d'achat
                ligne.setQuantiteAchat(0); // Remplacez par la valeur par défaut souhaitée pour la quantité d'achat
                ligne.setNbOrdresAchat(0); // Remplacez par la valeur par défaut souhaitée pour le nombre d'ordres d'achat
            }

            if (itVente.hasNext()) {
                LigneCarnet ligneVente = itVente.next();
                ligne.setPrixVente(ligneVente.getPrixVente());
                ligne.setQuantiteVente(ligneVente.getQuantiteVente());
                ligne.setNbOrdresVente(ligneVente.getNbOrdresVente());
            } else {
                // Si la liste de ventes est épuisée mais la liste d'achats a encore des éléments
                // Remplir les attributs de vente avec des valeurs par défaut (0 ou null)
                ligne.setPrixVente(0.0); // Remplacez par la valeur par défaut souhaitée pour le prix de vente
                ligne.setQuantiteVente(0); // Remplacez par la valeur par défaut souhaitée pour la quantité de vente
                ligne.setNbOrdresVente(0); // Remplacez par la valeur par défaut souhaitée pour le nombre d'ordres de vente
            }

            lignesCarnet.add(ligne);
        }


        return lignesCarnet;
    }
    @Override
    public String obtenirSymboleParIdTitre(int idTitre) {
        return titreRepository.findSymbolTitreById(idTitre);
    }

}
