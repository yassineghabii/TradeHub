package com.example.pifinance_back.config;

import com.example.pifinance_back.Entities.Client;
import com.example.pifinance_back.Repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthenticationService implements UserDetailsService {

    @Autowired
    private ClientRepository userRepository; // Assurez-vous d'injecter votre référentiel UserRepository

   /* public UserDetails loadUserByAdminId(String idAdmin) {
        Client user = userRepository.findByIdAdmin(idAdmin).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }
*/
    @Override
    public UserDetails loadUserByUsername(String idAdmin) throws UsernameNotFoundException {
        // Essayez d'abord de charger l'utilisateur par l'ID de l'administrateur
        Client user = userRepository.findByIdAdmin1(idAdmin);

        if (user == null) {
            // Si l'utilisateur n'est pas trouvé par l'ID de l'administrateur, essayez de le charger par l'email
            user = userRepository.findByIdAdmin1(idAdmin);
        }

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }
}
