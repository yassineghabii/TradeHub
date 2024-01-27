package com.example.pifinance_back.Services;

import com.example.pifinance_back.Controllers.UsersController;
import com.example.pifinance_back.Entities.Client;
import com.example.pifinance_back.Entities.Formation;
import com.example.pifinance_back.Entities.UserEnum;
import com.example.pifinance_back.Entities.Wallet;
import com.example.pifinance_back.Repositories.ClientRepository;
import com.example.pifinance_back.Repositories.FormationRepository;
import com.example.pifinance_back.Repositories.WalletRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.util.*;

@Service
@AllArgsConstructor
public class UserService implements IUserServices {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private FormationRepository formationRepository;

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private WalletService walletService;
    @Override
    public ClientProfileDTO getClientProfile(Long clientId) throws ChangeSetPersister.NotFoundException {
        Optional<Client> client = clientRepository.findById(clientId);
        if (client.isPresent()) {
            return mapClientToClientProfileDTO(client.get());
        }
        throw new ChangeSetPersister.NotFoundException();
    }

    private ClientProfileDTO mapClientToClientProfileDTO(Client client) {
        ClientProfileDTO dto = new ClientProfileDTO();
        dto.setId(client.getId());
        dto.setLastname(client.getLastname());
        dto.setFirstname(client.getFirstname());
        dto.setCin(client.getCin());
        dto.setPhone_number(client.getPhone_number());
        dto.setAddress(client.getAddress());
        dto.setEmail(client.getEmail());

        return dto;
    }
@Override
public String changePassword(Long userId, String oldPassword, String newPassword) {
    // Ensure parameters are not null
    if (oldPassword == null || newPassword == null) {
        throw new IllegalArgumentException("Les mots de passe fournis ne doivent pas être nuls.");
    }

    logger.debug("Changement de mot de passe pour l'utilisateur avec l'ID : {}, ancien mot de passe : {}, nouveau mot de passe : {}", userId, oldPassword, newPassword);
    logger.debug("Received oldPassword: {}", oldPassword);
    logger.debug("Received newPassword: {}", newPassword);

    Optional<Client> userOptional = clientRepository.findById(userId);
    if (!userOptional.isPresent()) {
        return "Le client n'existe pas dans la base de données.";
    }

    Client user = userOptional.get();
    String currentEncodedPassword = user.getPwd_user();

    // Check that the current encoded password is not null
    if (currentEncodedPassword == null) {
        throw new IllegalStateException("Le mot de passe actuel est indéfini dans la base de données.");
    }

    // Check if the old password matches the one stored in the database
    if (!passwordEncoder.matches(oldPassword, currentEncodedPassword)) {
        return "L'ancien mot de passe est incorrect.";
    }

    // Check if the new password is the same as the old password
    if (passwordEncoder.matches(newPassword, currentEncodedPassword)) {
        return "Le nouveau mot de passe doit être différent de l'ancien.";
    }

    // Encode the new password and set it on the user entity
    String encodedNewPassword = passwordEncoder.encode(newPassword);
    user.setPwd_user(encodedNewPassword);

    // Save the updated user to the repository to persist the new password
    clientRepository.save(user);

    // Journaliser le succès de la modification du mot de passe
    logger.debug("Le mot de passe a été modifié avec succès pour l'utilisateur avec l'ID : {}", userId);

    return "Votre mot de passe a été bien modifié.";
}

    @Override
    public List<Client> getAllAdminsExceptById(Long id) {
        return clientRepository.findAllByRoleAndIdNot(UserEnum.admin, id);
    }

    @Override
    public List<Client> getAllPlayers() {
        List<Client> clients = clientRepository.findAllByRole(UserEnum.player);

        clients.forEach(client -> {
            if (client.getImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(client.getImage());
                client.setImageBase64(base64Image); // Assuming there is a setImageBase64 method in your Client class
            }
        });

        return clients;
    }

    @Override
    public Client getUserById(Long id) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        return clientOptional.orElse(null);
    }


        @Override
        public Client updateUserById(Long id, String newEmail, String newPhoneNumber, String newAddress, String newPass) {
            Optional<Client> clientOptional = clientRepository.findById(id);

            if (!clientOptional.isPresent()) {
                throw new UserNotFoundException(id);
            }

            Client client = clientOptional.get();
            String oldEmail = client.getEmail();
            String oldPhoneNumber = client.getPhone_number();
            String oldAddress = client.getAddress();
            String oldPass = client.getPwd_user();
            String encryptedPassword = passwordEncoder.encode(newPass);

            if (newEmail != null) {
                client.setEmail(newEmail);
            }
            if (newPhoneNumber != null) {
                client.setPhone_number(newPhoneNumber);
            }
            if (newAddress != null) {
                client.setAddress(newAddress);
            }
            if (encryptedPassword != null) {
                client.setPwd_user(encryptedPassword);
            }
            Client updatedClient = clientRepository.save(client);

            String destinataire = updatedClient.getEmail();
            String sujet = "Mise à jour de vos informations administrateur";
            String contenu = "Bonjour,<br><br>" +
                    "Vos informations administrateur ont été mises à jour :<br><br>" +
                    "Ancienne adresse email : " + oldEmail + "<br>" +
                    "Nouvelle adresse email : " + updatedClient.getEmail() + "<br><br>" +
                    "Ancien numéro de téléphone : " + oldPhoneNumber + "<br>" +
                    "Nouveau numéro de téléphone : " + updatedClient.getPhone_number() + "<br><br>" +
                    "Ancienne adresse : " + oldAddress + "<br>" +
                    "Nouvelle adresse : " + updatedClient.getAddress() + "<br><br>" +
                    "Ancien Mot de Passe : ************ "  + "<br>" +
                    "Nouvelle adresse : " + newPass + "<br><br>" +

                    "Cordialement";

            envoyerEmail(destinataire, sujet, contenu);

            return updatedClient;
        }
    public boolean ifEmailExist(String mail){
        return clientRepository.existsByEmail(mail);
    }
    public Client getUserByMail(String mail){
        return clientRepository.findByEmail1(mail);
    }

    public Client addAdmin(Client client) {
        client.setRole(UserEnum.admin);
        client.setToken(null);
        client.setId_admin(genererIdAdmin(client.getFirstname(), client.getLastname(), client.getCin()));
        String motDePasseNonEncode = genererMotDePasseSecurise();
        String motDePasseEncode = passwordEncoder.encode(motDePasseNonEncode);
        client.setPwd_user(motDePasseEncode);
        clientRepository.save(client);
        String destinataire = client.getEmail();
        String sujet = "Vos informations de connexion";

        String contenu = "Bonjour," +
                "Voici vos informations de connexion :\n\n" +
                "ID Admin : " + client.getId_admin() + "\n" +
                "Mot de passe : " + motDePasseNonEncode + "\n\n" +
                "Cordialement";
        envoyerEmailAvecMotDePasse(destinataire, sujet, contenu);
        return client;
    }

    private String genererIdAdmin(String firstname, String lastname, String cin) {
        String firstLetter = lastname.substring(0, 1);
        String lastTwoLetters = firstname.substring(firstname.length() - 1);
        String idAdmin = "admin-" + firstLetter + lastTwoLetters + cin;
        return idAdmin;
    }

    private void envoyerEmailAvecMotDePasse(String destinataire, String sujet, String contenu) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinataire);
        message.setSubject(sujet);
        message.setText(contenu);

        mailSender.send(message);
    }

    public void envoyerEmail(String destinataire, String sujet, String contenu) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(destinataire);
            helper.setSubject(sujet);
            helper.setText(contenu, true);
            mailSender.send(message);
        } catch (Exception e) {
        }
    }

    @Override
    public void deleteUser(Long id) {
        Client adminToDelete = clientRepository.findById(id).orElse(null);
        Wallet userWallet = walletRepository.findByUserId(id);

        if (adminToDelete != null) {
            if (userWallet != null) {
                walletService.deleteWallet(userWallet.getId_wallet());
            }
            clientRepository.deleteById(id);
            String destinataire = adminToDelete.getEmail();
            String sujet = "Suppression de votre compte administrateur"+"<br><br>";
            String contenu = "Bonjour,\n\nVotre compte administrateur a été supprimé"+"<br><br>"+"Vous n'aurez plus l'accés a notre PLATEFORME ."+"<br><br>" +
                    "Cordialement";;

            envoyerEmail(destinataire, sujet, contenu);
        }
    }


    private String genererMotDePasseSecurise() {
        String caracteresPossibles = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        int longueurMotDePasse = 12;
        StringBuilder motDePasse = new StringBuilder(longueurMotDePasse);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < longueurMotDePasse; i++) {
            int index = random.nextInt(caracteresPossibles.length());
            char caractere = caracteresPossibles.charAt(index);
            motDePasse.append(caractere);
        }
        return motDePasse.toString();
    }
    @Override
    public Client findAdminById(String idAdmin)  {

        return clientRepository.findByIdAdmin1(idAdmin);
    }

    @Override
    public Client assignFormationToClient(Long idClient, int idFormation) {
        Client client= clientRepository.findById(idClient).orElse(null);
        Formation formation=formationRepository.findById(idFormation).orElse(null);
        formation.setCapacite(formation.getCapacite()-1);
        if(client.getFormations()==null){
            Set<Formation> formationSet= new HashSet<>();
            formationSet.add(formation);
            client.setFormations(formationSet);
        }
        else{
            client.getFormations().add(formation);
        }
        return  clientRepository.save(client);
    }

    @Override
    public Client unassignFormationFromClient(Long idClient, int idFormation) {
        Client client = clientRepository.findById(idClient).orElse(null);
        Formation formation = formationRepository.findById(idFormation).orElse(null);
        if (client != null && formation != null) {
            if (client.getFormations() != null) {
                client.getFormations().remove(formation);
            }
            formation.setCapacite(formation.getCapacite() + 1);
            formationRepository.save(formation);
            return clientRepository.save(client);
        }
        return null;
    }

}
