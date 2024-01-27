package com.example.pifinance_back.Controllers;

import com.example.pifinance_back.Entities.*;
import com.example.pifinance_back.Repositories.OrdreAchatRepository;
import com.example.pifinance_back.Repositories.OrdreVenteRepository;
import com.example.pifinance_back.Repositories.TitreRepository;
import com.example.pifinance_back.Repositories.TransactionRepository;
import com.example.pifinance_back.Services.IMariemService;
import com.example.pifinance_back.Services.MariemService;
import com.example.pifinance_back.Services.TitreDTO;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/mariem")
public class MariemController {
    @Autowired
    private final IMariemService mariemService;
    @Autowired
    private TitreRepository titreRepository;
    @Autowired
    private OrdreAchatRepository ordreAchatRepository;

    @Autowired
    private OrdreVenteRepository ordreVenteRepository;

    private static final Logger logger = LoggerFactory.getLogger(MariemService.class);
    @GetMapping("/transaction/{clientId}")
    public ResponseEntity<List<Transaction>> afficherTransactionparid(@PathVariable Long clientId) {
        List<Transaction> transactions = mariemService.getTransactionById(clientId);
        return ResponseEntity.ok(transactions);
    }
    @PutMapping("/annuler-ordrea/{idOrdre}")
    public ResponseEntity<?> annulerOrdreA(@PathVariable int idOrdre) {
        try {
            mariemService.annulerOrdreA(idOrdre);
            return ResponseEntity.ok("Ordre annulé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'annulation de l'ordre");
        }
    }

    @PutMapping("/annuler-ordrev/{idOrdre}")
    public ResponseEntity<?> annulerOrdreV(@PathVariable int idOrdre) {
        try {
            mariemService.annulerOrdreV(idOrdre);
            return ResponseEntity.ok("Ordre annulé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'annulation de l'ordre");
        }
    }

    @GetMapping("/client/{clientId}/achat/limite")
    public ResponseEntity<List<OrdreAchatLimiteDTO>> getOrdresAchatLimiteByClientId(@PathVariable Long clientId) {
        List<OrdreAchat> ordres = ordreAchatRepository.findByTypeOrdreAndClientId(TypeOrdre.LIMITE, clientId);
        List<OrdreAchatLimiteDTO> dtos = ordres.stream()
                .map(mariemService::toOrdreAchatLimiteDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    // Controller method to get limited sale orders by client ID
    @GetMapping("/client/{clientId}/vente/limite")
    public ResponseEntity<List<OrdreVenteLimiteDTO>> getOrdresVenteLimiteByClientId(@PathVariable Long clientId) {
        List<OrdreVente> ordres = ordreVenteRepository.findByTypeOrdreAndClientId(TypeOrdre.LIMITE, clientId);
        List<OrdreVenteLimiteDTO> dtos = ordres.stream()
                .map(mariemService::toOrdreVenteLimiteDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    // Controller method to get market purchase orders by client ID
    @GetMapping("/client/{clientId}/achat/aumarche")
    public ResponseEntity<List<OrdreAchatAuMarcheDTO>> getOrdresAchatAuMarcheByClientId(@PathVariable Long clientId) {
        List<OrdreAchat> ordres = ordreAchatRepository.findByTypeOrdreAndClientId(TypeOrdre.AU_MARCHE, clientId);
        List<OrdreAchatAuMarcheDTO> dtos = ordres.stream()
                .map(mariemService::toOrdreAchatAuMarcheDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    // Controller method to get market sale orders by client ID
    @GetMapping("/client/{clientId}/vente/aumarche")
    public ResponseEntity<List<OrdreVenteAuMarcheDTO>> getOrdresVenteAuMarcheByClientId(@PathVariable Long clientId) {
        List<OrdreVente> ordres = ordreVenteRepository.findByTypeOrdreAndClientId(TypeOrdre.AU_MARCHE, clientId);
        List<OrdreVenteAuMarcheDTO> dtos = ordres.stream()
                .map(mariemService::toOrdreVenteAuMarcheDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping("/matchOrders")
    public ResponseEntity<String> matcherOrdres() {
        mariemService.matcherOrdres();
        return ResponseEntity.ok("Order matching completed successfully");
    }

    // Méthode pour générer un carnet d'ordre pour un titre spécifique par son ID
    @GetMapping("/genererCarnet/{idTitre}")
    public ResponseEntity<?> genererCarnetOrdre(@PathVariable int idTitre) {
        return titreRepository.findById(idTitre)
                .map(titre -> {
                    try {
                        CarnetOrdre carnetOrdres = mariemService.genererCarnetOrdres(titre);
                        return ResponseEntity.ok().body(carnetOrdres); // Retourne 200 OK avec le corps de la réponse
                    } catch (Exception e) {
                        // Gérer les exceptions d'une autre manière si nécessaire
                        return ResponseEntity.internalServerError().build();
                    }
                })
                .orElse(ResponseEntity.notFound().build()); // Retourne 404 Not Found si le titre n'est pas trouvé
    }



    @GetMapping("/AllTitres")
    public List<TitreDTO> getAllTitres() {
        return mariemService.getTitres();
    }


    @PostMapping("/vente/{idTitre}/{idClient}")
    public ResponseEntity<ReponseOrdre> passerOrdreVente(
            @PathVariable int idTitre,
            @PathVariable Long idClient,
            @RequestBody OrdreVente ordre) {

        // Appelez votre service qui peut lancer des exceptions si nécessaire
        mariemService.passerOrdreVente(idTitre, ordre.getTypeOrdre(), ordre.getQuantite(), ordre.getPrixLimiteV(), ordre.getDureeValidite(), idClient);

        // S'il n'y a pas d'exceptions, l'ordre a été passé avec succès
        String successMessage = "Ordre de vente passé avec succès.";
        return ResponseEntity.ok(new ReponseOrdre(successMessage));
    }

    @PostMapping("/ajouterTitre")
    public ResponseEntity<Titre> ajouterTitre(@RequestBody TitreRequest titreRequest) {
        Titre nouveauTitre = mariemService.ajouterTitre(
                titreRequest.getSymbole(),
                titreRequest.getNom(),
                titreRequest.getPrixOuverture(),
                titreRequest.getQuantite()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveauTitre);
    }



    @GetMapping("/afficher-toutes-les-donnees")
    public ResponseEntity<String> afficherToutesLesDonnees(@RequestParam String interval) {
        List<String> symboles = mariemService.obtenirSymbolesDynamiques();
        StringBuilder responseBuilder = new StringBuilder();

        // Récupérer les données pour chaque symbole dans la liste
        for (String symbole : symboles) {
            String donnees = mariemService.afficherDonnees(symbole, interval);
            responseBuilder.append("Données pour ").append(symbole).append(":\n").append(donnees).append("\n\n");
        }

        return ResponseEntity.ok(responseBuilder.toString());
    }


    @GetMapping("/afficher-toutes-les-donnees/{symbol}/{interval}")
    public ResponseEntity<String> afficherToutesLesDonnees(
            @PathVariable String symbol,
            @PathVariable String interval) {

        String donnees = mariemService.afficherDonnees(symbol, interval);

        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("Données pour ").append(symbol).append(" avec un intervalle de ").append(interval).append(":\n").append(donnees).append("\n");

        return ResponseEntity.ok(responseBuilder.toString());
    }


    @GetMapping("/all-ordreAchat")
    public List<OrdreAchat> getAllOrdresA() {
        return mariemService.getAllOrdresA();
    }


    @DeleteMapping("/annuler-ordre/{idOrdre}")
    public void annulerOrdre(@PathVariable int idOrdre) {
        mariemService.annulerOrdre(idOrdre);
    }

    @GetMapping("/all-transactions")
    public List<Transaction> getAllTransactions() {
        return mariemService.getAllTransactions();
    }

    /*@GetMapping("/carnet-ordre/{idTitre}")
    public List<Ordre> afficherCarnetOrdre(@PathVariable int idTitre) {
        return mariemService.afficherCarnetOrdre(idTitre);
    }*/

    //ch
    @PostMapping("/achat/{idTitre}/{idClient}")
    public ResponseEntity<ReponseOrdre> passerOrdreAchat(
            @PathVariable int idTitre,
            @PathVariable Long idClient,
            @RequestBody OrdreAchat ordre) {
        try {
            int quantite = ordre.getQuantite();
            Double prixLimiteA = ordre.getPrixLimiteA();
            TypeOrdre typeOrdre = ordre.getTypeOrdre();
            DureeValidite dureeValidite = ordre.getDureeValidite();
            mariemService.passerOrdreAchat(idTitre, typeOrdre, quantite, prixLimiteA, dureeValidite, idClient);

            String successMessage = "Ordre d'achat passé avec succès.";
            return ResponseEntity.ok(new ReponseOrdre(successMessage));
        } catch (TitreNotFoundException e) {
            String errorMessage = "Erreur : " + e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ReponseOrdre(errorMessage));
        } catch (AucunOrdreVenteLimiteException e) {
            String errorMessage = "Erreur : " + e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ReponseOrdre(errorMessage));
        }

    }




      /* @PostMapping("/achat/{idTitre}")
    public ResponseEntity<String> passerOrdreAchat(
            @PathVariable int idTitre,
            @RequestBody OrdreAchat ordre) {
        try {
            int quantite = ordre.getQuantite(); // Obtenir la quantité en tant que int
            Double prixLimiteA = ordre.getPrixLimiteA(); // Obtenir le prix limite en tant que Double
            TypeOrdre typeOrdre = ordre.getTypeOrdre(); // Obtenir le type d'ordre à partir de l'objet Ordre
            mariemService.passerOrdreAchat(idTitre, typeOrdre, quantite, prixLimiteA);

            // Si l'exécution arrive ici, cela signifie que l'ordre a été passé avec succès
            String successMessage = "Ordre d'achat passé avec succès.";
            return ResponseEntity.ok(successMessage);
        } catch (TitreNotFoundException e) {
            // Gérer l'erreur de Titre non trouvé ici
            String errorMessage = "Erreur : " + e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        } catch (AucunOrdreVenteLimiteException e) {
            // Gérer l'erreur d'absence d'ordre de vente limite
            String errorMessage = "Erreur : " + e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }*/
      @GetMapping("carnet/{idTitre}")
      public ResponseEntity<List<LigneCarnet>> getCarnetOrdres(@PathVariable int idTitre) {
          List<LigneCarnet> carnet = mariemService.genererCarnet(idTitre);
          logger.info("Number of buy orders found: {}", carnet.stream().mapToInt(LigneCarnet::getNbOrdresAchat).sum());
          logger.info("Number of sell orders found: {}", carnet.stream().mapToInt(LigneCarnet::getNbOrdresVente).sum());

          return ResponseEntity.ok(carnet);
      }
      @Getter
      @Setter
      @AllArgsConstructor
      @NoArgsConstructor
      @Data
    public class SymboleDTO {
        private String symbole;

    }

    @GetMapping("/{idTitre}/symbole")
    public @ResponseBody ResponseEntity<SymboleDTO> obtenirSymboleParIdTitre(@PathVariable int idTitre) {
        String symbole = mariemService.obtenirSymboleParIdTitre(idTitre);

        SymboleDTO symboleDTO = new SymboleDTO();
        symboleDTO.setSymbole(symbole);

        return ResponseEntity.ok().body(symboleDTO);
    }

}
