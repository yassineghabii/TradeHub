package com.example.pifinance_back.Controllers;

import com.example.pifinance_back.Entities.Client;
import com.example.pifinance_back.Entities.Formation;
import com.example.pifinance_back.Repositories.ClientRepository;
import com.example.pifinance_back.Repositories.FormationRepository;
import com.example.pifinance_back.Services.ClientProfileDTO;
import com.example.pifinance_back.Services.EmailSenderService;
import com.example.pifinance_back.Services.IUserServices;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@RequestMapping("/users") // URL de base pour ce contrôleur
public class UsersController {
    @Autowired
    private IUserServices us;
    @Autowired
    private FormationRepository formationRepository;
    private final ClientRepository clientRepository;
    @Autowired
    private EmailSenderService service;

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    // Obtenir tous les administrateurs
    @GetMapping("/user/admins/{id}")
    public List<Client> getAllAdmins(@PathVariable Long id) {
        return us.getAllAdminsExceptById(id);
    }

    // Obtenir tous les joueurs
    @GetMapping("/clients")
    public ResponseEntity<List<Client>> getAllPlayers() {
        List<Client> clients = us.getAllPlayers();

        clients.forEach(client -> {
            if (client.getImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(client.getImage());
                client.setImageBase64(base64Image); // Assuming there is a setImageBase64 method in your Client class
            }
        });

        return ResponseEntity.ok(clients);
    }


    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> viewImage(@PathVariable Long id) {
        Optional<Client> optionalClient = Optional.ofNullable(us.getUserById(id));
        if (optionalClient.isPresent()) {
            Client player = optionalClient.get();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(player.getImage());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/user/{id}")
    public Client getUserById(@PathVariable Long id) {
        return us.getUserById(id);
    }

    // Mettre à jour un utilisateur par ID
    @PutMapping("/update-user/{id}")
    public Client updateUserById(@PathVariable Long id, @RequestBody UpdateClientDTO updateClientDTO) {
        return us.updateUserById(id, updateClientDTO.getEmail(), updateClientDTO.getPhone_number(), updateClientDTO.getAddress(), updateClientDTO.getPwd_user());
    }

    @PostMapping("/user/admin")
    public Client addAdmin(@RequestBody Client client) {
        return us.addAdmin(client);
    }

    @DeleteMapping("/delete-user/{id}")
    public void deleteCard(@PathVariable Long id) {
        us.deleteUser(id);
    }


    @GetMapping("/find")
    public int findByIdAdmin(@RequestBody Map<String, String> requestBody) throws ChangeSetPersister.NotFoundException {
        JSONObject json = new JSONObject(requestBody);

        // Obtenez la valeur de id_admin en tant que chaîne
        String idAdmin = json.getAsString("id_admin");

        // Recherchez l'idAdmin dans la base de données
        Client client = us.findAdminById(idAdmin);

        if (client != null) {
            // L'idAdmin a été trouvé, enregistrez un log
            logger.info("id_admin trouvé : {}", idAdmin);
            return 1; // Renvoie 1 si l'idAdmin existe
        } else {
            return 0; // Renvoie 0 si l'idAdmin n'existe pas
        }
    }

    @GetMapping("/{clientId}/profile")
    public ClientProfileDTO getClientProfile(@PathVariable Long clientId) {
        try {
            return us.getClientProfile(clientId);
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Client profile not found", e);
        }
    }

    @PostMapping("/{userId}/change")
    public ResponseEntity<String> changePassword(
            @PathVariable Long userId,
            @RequestBody Map<String, String> passwords) {

        String oldPassword = passwords.get("oldPassword");
        String newPassword = passwords.get("newPassword");

        // Check for null or empty passwords and return an error response if necessary
        if (oldPassword == null || newPassword == null) {
            return ResponseEntity.badRequest().body("Les mots de passe fournis ne doivent pas être nuls.");
        }

        String result = us.changePassword(userId, oldPassword, newPassword);

        // Log de la réponse
        logger.debug("Réponse reçue du service de changement de mot de passe : {}", result);

        return ResponseEntity.ok(result);
    }
    // Classe interne pour obtenir les données de la requête de mise à jour de mot de passe
    @Getter
    public static class ChangePasswordDTO {
        @NotNull
        private String oldPassword;
        @NotNull

        private String newPassword;


    }

    @PutMapping("/assignFormationToClient/{idclient}/{idFormation}")
    public ResponseEntity<?> assignFormationToClient(@PathVariable("idclient") Long idClient, @PathVariable("idFormation") Integer idFormation) throws IOException, MessagingException {
        Optional<Formation> optionalEvent = formationRepository.findById(idFormation);
        Client client = clientRepository.findClientById(idClient);

        if (optionalEvent.isPresent()) {
            Formation formation = optionalEvent.get();

            // Vérifier si le client participe déjà à la formation
            if (client.getFormations().contains(formation)) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Account already assigned to InternalService");
            }

            String meetLink = "https://meet.google.com/udb-nuwu-aas?authuser=" + formation.getId();
            File file = ResourceUtils.getFile("src/main/java/com/example/pifinance_back/wss/participationevent.html");
            System.out.println("File Found : " + file.exists());
            String content = new String(Files.readAllBytes(file.toPath()));

            content = content.replace("${event}", formation.getNom())
                    .replace("${date}", formation.getDate_debut().toString())
                    .replace("${description}", formation.getDescription())
                    .replace("${thematique}", formation.getThematique())
                    .replace("${organisateur}", formation.getOrganisateur())
                    .replace("${meetlink}", meetLink);

            service.sendSimpleEmail(client.getEmail(), content, "Vous participez à l'évènement  " + formation.getNom());

            // Effectuer l'attribution uniquement si le client ne participe pas encore à la formation
            return ResponseEntity.ok((Object) us.assignFormationToClient(idClient, idFormation));
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/unassignFormationFromClient/{idclient}/{idFormation}")
    public ResponseEntity<?> unassignFormationFromClient(@PathVariable("idclient") Long idClient, @PathVariable("idFormation") int idFormation) throws IOException, MessagingException {
        try {
            Optional<Formation> optionalEvent = formationRepository.findById(idFormation);
            Client client = clientRepository.findClientById(idClient);

            if (optionalEvent.isPresent()) {
                Formation formation = optionalEvent.get();

                // Vérifier si le client n'est pas inscrit à la formation
                if (!client.getFormations().contains(formation)) {
                    // Retourner une réponse avec le statut 400 et le message spécifique
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("Account not assigned to InternalService");
                }

                File file = ResourceUtils.getFile("src/main/java/com/example/pifinance_back/wss/annulerparticipationevent.html");
                System.out.println("File Found: " + file.exists());
                String content = new String(Files.readAllBytes(file.toPath()));

                content = content.replace("${event}", formation.getNom())
                        .replace("${date}", formation.getDate_debut().toString())
                        .replace("${description}", formation.getDescription())
                        .replace("${thematique}", formation.getThematique())
                        .replace("${organisateur}", formation.getOrganisateur());

                service.sendSimpleEmail(client.getEmail(), content, "Vous avez annulé votre participation à l'évènement " + formation.getNom());

                return ResponseEntity.ok(us.unassignFormationFromClient(idClient, idFormation));
            }
        } catch (Exception e) {
            // Gérer les exceptions ici
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }

        // Si la formation n'est pas présente, retourner une réponse 404
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/clients-with-formations/{id}")
    public List<Client> Listedesparticipants(@PathVariable("id") int Idformation) {
        return clientRepository.findByFormations_Id(Idformation);

    }


}