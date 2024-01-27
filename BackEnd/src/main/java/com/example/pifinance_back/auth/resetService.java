package com.example.pifinance_back.auth;

import com.example.pifinance_back.Entities.Client;
import com.example.pifinance_back.Repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Service
public class resetService {

    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private final ClientRepository userRepository;
    @Autowired
    authServices emailService ;

    @Autowired
    private JavaMailSender mailSender;
    private static final Logger logger = LoggerFactory.getLogger(resetService.class); // replace 'YourClassName' with the name of your class
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public Client getUserByEmail(String email) {
        return userRepository.findByEmail1(email);
    }

}