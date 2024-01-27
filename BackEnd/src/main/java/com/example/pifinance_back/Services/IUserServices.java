package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.Client;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface IUserServices {
    List<Client> getAllAdminsExceptById(Long id);
     ClientProfileDTO getClientProfile(Long clientId) throws ChangeSetPersister.NotFoundException ;

    // Récupérer tous les joueurs
    List<Client> getAllPlayers();

    // Récupérer un utilisateur par ID
     Client getUserById(Long id);

    // Mettre à jour un utilisateur par ID
    Client updateUserById(Long id, String email, String phoneNumber,String address,String newPass);

    // Ajouter un admin
    Client addAdmin(Client client);

    void deleteUser(Long id);

    Client findAdminById(String idAdmin) throws ChangeSetPersister.NotFoundException;
     String changePassword(Long userId, String oldPassword, String newPassword) ;

    Client assignFormationToClient(Long idClient, int idFormation);
    Client unassignFormationFromClient(Long idClient, int idFormation);


}
