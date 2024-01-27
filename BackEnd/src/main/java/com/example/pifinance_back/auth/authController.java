package com.example.pifinance_back.auth;

import com.example.pifinance_back.Controllers.WalletController;
import com.example.pifinance_back.Entities.*;
import com.example.pifinance_back.Repositories.CardRepository;
import com.example.pifinance_back.Repositories.ClientRepository;
import com.example.pifinance_back.Repositories.PortfolioRepository;
import com.example.pifinance_back.Repositories.WalletRepository;
import com.example.pifinance_back.Services.IWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.example.pifinance_back.config.JwtService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)


public class authController {
    @Autowired
    private final authServices service;
    @Autowired

    private final ClientRepository UR;
    @Autowired

    private final PortfolioRepository portfolioRepository;

    @Autowired

    private final WalletRepository walletRepository;
    @Autowired

    private final WalletController walletController;

    @Autowired

    private final CardRepository cardRepository;
@Autowired
private final IWalletService walletService ;
    @Autowired
    private final JwtService js;
    @Autowired
    private final UserDetails ud ;
    @Autowired
    private final resetService s ;
    private static final Logger logger = LoggerFactory.getLogger(authController.class);

    @PostMapping("/login")
    public ResponseEntity<authResponse> login(@RequestBody authRequest loginRequest) {
        try {
            // Perform authentication
            String token = service.login(loginRequest.getEmail(), loginRequest.getPwd_user());
            Client authenticatedUser = s.getUserByEmail(loginRequest.getEmail());

            // Get the wallet ID for the authenticated user
            Long walletId = walletRepository.findWalletIdByUserId(authenticatedUser.getId());

            // Get the card ID associated with the wallet
            Long cardId = cardRepository.findCardIdByWalletId(walletId);

            // Create the AuthResponse object and populate its fields
            authResponse response = new authResponse();
            response.setToken(token);
            response.setId(Math.toIntExact(authenticatedUser.getId()));
            response.setWelcome("Welcome, " + authenticatedUser.getFirstname());
            response.setMessage("Authentication successful");
            response.setRole(authenticatedUser.getRole());
            response.setFirstname(authenticatedUser.getFirstname());
            response.setCin(authenticatedUser.getCin());
            response.setLastname(authenticatedUser.getLastname());
            response.setId_wallet(walletId); // Ajout de l'ID du portefeuille dans la réponse
            response.setId_card(cardId); // Ajout de l'ID de la carte dans la réponse

            // Return the AuthResponse object
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            // Handle authentication errors
            authResponse errorResponse = new authResponse();
            errorResponse.setErrorMessage("Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
    @PostMapping("/loginAdmin")
    public ResponseEntity<authResponse> loginAdmin(@RequestBody AdminLoginRequest adminLoginRequest) {
        String id_admin = adminLoginRequest.getId_admin();
        String pwd_user = adminLoginRequest.getPwd_user();

        try {
            String token = service.loginAdmin(id_admin, pwd_user);
            Client authenticatedUser = UR.findByIdAdmin1(adminLoginRequest.getId_admin());

            authResponse response = new authResponse();
            response.setToken(token);
            response.setId(Math.toIntExact(authenticatedUser.getId()));
            response.setWelcome("Welcome, " + authenticatedUser.getFirstname());
            response.setMessage("Authentication successful");
            response.setRole(authenticatedUser.getRole());
            response.setFirstname(authenticatedUser.getFirstname());
            response.setCin(authenticatedUser.getCin());
            response.setLastname(authenticatedUser.getLastname());
            response.setIdAdmin((authenticatedUser.getId_admin()
            ));
            // Return the AuthResponse object
            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            // Handle authentication errors
            authResponse errorResponse = new authResponse();
            errorResponse.setErrorMessage("Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestParam("profileImage") MultipartFile profileImage, @ModelAttribute registerRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            byte[] profileImageBytes = profileImage.getBytes();
            service.register(
                    request.getEmail(),
                    request.getPwd_user(),
                    request.getFirstname(),
                    request.getLastname(),
                    request.getCin(),
                    request.getAddress(),
                    request.getPhonenumber(),
                    profileImageBytes
            );

            // Après une inscription réussie, récupérez l'ID de l'utilisateur nouvellement créé
            // Utilisez cet ID pour créer un portefeuille associé à cet utilisateur
            Client newlyRegisteredUser = UR.findByEmail1(request.getEmail());
            Portfolio newPortfolio = walletService.createDefaultPortfolio(newlyRegisteredUser);
            portfolioRepository.save(newPortfolio);

            // Créer un nouveau portefeuille
            Wallet newWallet = new Wallet();
            newWallet.setType(request.getType()); // Utilisez le champ type de RegisterRequest

            // Appel au contrôleur de portefeuille pour créer un portefeuille pour l'utilisateur
            ResponseEntity<?> walletCreationResponse = walletController.createWallet(newlyRegisteredUser.getId(), newWallet);

            // Vérifiez la réponse de la création du portefeuille
            if (walletCreationResponse.getStatusCode().is2xxSuccessful()) {
                response.put("message", "Inscription et création du portefeuille réussies");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Erreur lors de la création du portefeuille.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (IOException e) {
            response.put("error", "Erreur lors de la gestion de l'image.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/logout/{id}")
    public ResponseEntity<Map<String, String>> logout(@PathVariable Long id) {
        try {
            // Perform logout for the user with the given id
            service.logout(id);

            // Create a map for the response
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "User logged out successfully!");

            // Return a success message in the response
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Handle logout errors
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String identifier) {
        identifier = identifier.trim();

        if (!service.isEmail(identifier) && !service.isValidTunisianPhoneNumber(identifier)) {
            return ResponseEntity.badRequest().body("Invalid identifier format.");
        }

        try {
            service.sendPasswordResetNotification(identifier);
            return ResponseEntity.ok("Password reset notification sent successfully.");
        } catch (UsernameNotFoundException e) {
            // Includes more informative logging and error message specifics.
            logger.error("No user found for given identifier: " + identifier, e);
            return ResponseEntity.badRequest().body("No user found with identifier: " + identifier);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid password reset request, details: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body("The password reset request is invalid: " + e.getMessage());
        } catch (Exception e) {
            logger.error("An error occurred processing your request", e);
            return ResponseEntity.internalServerError().body("An unexpected error occurred: " + e.getMessage());
        }

    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestBody String newPassword) {
        try {
            service.resetPassword(token, newPassword);
            return ResponseEntity.ok("Password reset successful!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/fb-login")
    public ResponseEntity<authResponse> fbLogin(@RequestBody Facebook socialUser) {
        logger.info("Received Social User: {}", socialUser.toString());

        // Extract email and check if this user already exists in your DB
        Optional<Client> userOptional = UR.findByEmail(socialUser.getEmail());

        // Define a user reference outside the conditional check
        Client user;
        if (!userOptional.isPresent()) {
            // Create a new user if it doesn't exist
            user = new Client();
            user.setEmail(socialUser.getEmail());
            user.setFirstname(socialUser.getFirstName());
            user.setLastname(socialUser.getLastName());
            user.setName(socialUser.getName());
            // Here, include setting up the new wallet and card if necessary

        } else {
            // If user exists, fetch from the Optional
            user = userOptional.get();
        }

        // Update the token for both new and existing users
        user.setToken(socialUser.getAuthToken()); // Set idToken from SocialUser to token in Client
        user = UR.save(user); // Save and get the entity back which should have the ID populated


        // Fetch the wallet ID and card ID
        Long walletId = walletRepository.findWalletIdByUserId(user.getId());
        Long cardId = cardRepository.findCardIdByWalletId(walletId);

        authResponse response = new authResponse();
        response.setId(Math.toIntExact(user.getId())); // Set the id with the ID from the saved user
        response.setFirstname(user.getFirstname());
        response.getFullName();

        response.setLastname(user.getLastname());
        response.setToken(user.getToken());

        response.setCin(user.getCin());

        response.setId_wallet(walletId);
        response.setId_card(cardId);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/microsoft-login")
    public ResponseEntity<authResponse> microsoftLogin(@RequestBody SocialUser socialUser) {
        logger.info("Received Social User: {}", socialUser.toString());

        // Extract email and check if this user already exists in your DB
        Optional<Client> userOptional = UR.findByEmail(socialUser.getEmail());

        // Define a user reference outside the conditional check
        Client user;
        if (!userOptional.isPresent()) {
            // Create a new user if it doesn't exist
            user = new Client();
            user.setEmail(socialUser.getEmail());
            user.setFirstname(socialUser.getFirstName());
            user.setLastname(socialUser.getLastName());
            user.setName(socialUser.getName());
            // Here, include setting up the new wallet and card if necessary

        } else {
            // If user exists, fetch from the Optional
            user = userOptional.get();
        }

        // Update the token for both new and existing users
        user.setToken(socialUser.getIdToken()); // Set idToken from SocialUser to token in Client
        user = UR.save(user); // Save and get the entity back which should have the ID populated


        // Fetch the wallet ID and card ID
        Long walletId = walletRepository.findWalletIdByUserId(user.getId());
        Long cardId = cardRepository.findCardIdByWalletId(walletId);

        authResponse response = new authResponse();
        response.setId(Math.toIntExact(user.getId())); // Set the id with the ID from the saved user
        response.setFirstname(user.getFirstname());
        response.setLastname(user.getLastname());
        response.setToken(user.getToken());

        response.setCin(user.getCin());

        response.setId_wallet(walletId);
        response.setId_card(cardId);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/amazon-login")
    public ResponseEntity<authResponse> amazonLogin(@RequestBody Amazon socialUser) {
        logger.info("Received Social User: {}", socialUser.toString());

        // Extract email and check if this user already exists in your DB
        Optional<Client> userOptional = UR.findByEmail(socialUser.getEmail());

        // Define a user reference outside the conditional check
        Client user;
        if (!userOptional.isPresent()) {
            user = new Client();
            user.setEmail(socialUser.getEmail());
            user.setFirstname(socialUser.getFirstName());
            user.setLastname(socialUser.getLastName());

        } else {
            user = userOptional.get();
        }

        user.setToken(socialUser.getAuthToken());
        user = UR.save(user);


        Long walletId = walletRepository.findWalletIdByUserId(user.getId());
        Long cardId = cardRepository.findCardIdByWalletId(walletId);

        authResponse response = new authResponse();
        response.setId(Math.toIntExact(user.getId()));
        response.setFirstname(user.getFirstname());
        response.setLastname(user.getLastname());
        response.setToken(user.getToken());

        response.setCin(user.getCin());

        response.setId_wallet(walletId);
        response.setId_card(cardId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/google-login")
    public ResponseEntity<authResponse> googleLogin(@RequestBody SocialUser socialUser) {
        logger.info("Received Social User: {}", socialUser.toString());

        // Extract email and check if this user already exists in your DB
        Optional<Client> userOptional = UR.findByEmail(socialUser.getEmail());

        // Define a user reference outside the conditional check
        Client user;
        if (!userOptional.isPresent()) {
            user = new Client();
            user.setEmail(socialUser.getEmail());
            user.setFirstname(socialUser.getFirstName());
            user.setLastname(socialUser.getLastName());
            user.setName(socialUser.getName());
        } else {
            user = userOptional.get();
        }

        // This line updates the token for both new and existing users
        user.setToken(socialUser.getIdToken()); // Set idToken from SocialUser to token in Client

        user = UR.save(user); // Save and get the entity back which should have the ID populated
        // Fetch the wallet ID and card ID
        Long walletId = walletRepository.findWalletIdByUserId(user.getId());
        Long cardId = cardRepository.findCardIdByWalletId(walletId);

        // Generate a JWT token for this user
        String token = js.generateToken(user);

        authResponse response = new authResponse();
        response.setId(Math.toIntExact(user.getId())); // Set the id with the ID from the saved user
        response.setFirstname(user.getFirstname());
        response.setLastname(user.getLastname());
        response.setToken(user.getToken());

        response.setCin(user.getCin());

        response.setId_wallet(walletId);
        response.setId_card(cardId);

        return ResponseEntity.ok(response);
    }
    @Transactional
    public void someServiceOrDAOFunction(Client client) {
        UR.save(client);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateClient(
            @PathVariable Long id,
            @RequestParam("pwd_user") String chosenPwd,
            @RequestParam("profile_image") MultipartFile profileImage,
            @RequestParam("cin") String cin,
            @RequestParam("phone_number") String phoneNumber,
            @RequestParam("address") String address,
            @RequestParam("type") WalletEnum type,
            @RequestParam(value = "role", defaultValue = "player") String role) {

        Map<String, Object> response = new HashMap<>();

        try {
            byte[] imageBytes = profileImage.getBytes();
            Client user = service.updateClient(id, chosenPwd, imageBytes, cin, phoneNumber, address, role);

            // Assuming updateClient returns the updated Client object.
            Wallet newWallet = new Wallet();
            newWallet.setType(type); // Use the type field from registerRequest
            ResponseEntity<?> walletCreationResponse = walletController.createWallet(user.getId(), newWallet);
            Client newlyRegisteredUser = UR.findClientById(id);
            Portfolio newPortfolio = walletService.createDefaultPortfolio(newlyRegisteredUser);
            portfolioRepository.save(newPortfolio);

            // Check wallet creation response
            if (walletCreationResponse.getStatusCode().is2xxSuccessful()) {
                // If successful, construct the authResponse
                authResponse authResponse = new authResponse();
                authResponse.setId(Math.toIntExact(user.getId()));
                authResponse.setFirstname(user.getFirstname());
                authResponse.setLastname(user.getLastname());
                authResponse.setToken(user.getToken());
                authResponse.setCin(user.getCin());

                Long walletId = walletRepository.findWalletIdByUserId(user.getId());
                Long cardId = cardRepository.findCardIdByWalletId(walletId);
                authResponse.setId_wallet(walletId);
                authResponse.setId_card(cardId);

                // Add the authResponse object and message to the map
                response.put("authResponse", authResponse);
                response.put("message", "Client update and wallet creation successful.");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Error during wallet creation.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (IOException e) {
            response.put("error", "Error handling the image.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/hasPasswordAndImage/{clientId}")
    public ResponseEntity<Boolean> hasPasswordAndImage(@PathVariable Long clientId) {
        boolean result = service.hasPasswordAndImage(clientId);
        return ResponseEntity.ok(result);
    }

}








