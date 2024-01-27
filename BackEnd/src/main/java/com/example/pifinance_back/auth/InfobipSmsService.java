package com.example.pifinance_back.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.apache.tomcat.util.net.SocketEvent.TIMEOUT;

@Service
public class InfobipSmsService {
    private static final Logger logger = LoggerFactory.getLogger(authController.class);

    @Value("${infobip.api.key}")
    private String apiKey;

    @Value("${infobip.base.url}")
    private String baseUrl;
    public void sendSmsResetToken(String phoneNumber, String token) {
        try {
            String message = "Votre code de reinitialisation de mot de passe est  " + token;
            String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString());
            // Replace plus signs with %20 to conform with the expected encoding
            encodedMessage = encodedMessage.replace("+", " ");
            sendSms(phoneNumber, encodedMessage);
        } catch (UnsupportedEncodingException e) {
            // Handle this exception properly
            logger.error("Error encoding SMS message", e);
        }
    }

    public void sendSms(String to, String message) {
        int timeout = 1000000000;  // 10 seconds timeout for both connect and read
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(timeout);
        requestFactory.setReadTimeout(timeout);

        RestTemplate restTemplate = new RestTemplate(requestFactory);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "App " + apiKey);
        headers.set("Accept", "application/json");

        // Adjusting the request body to the expected JSON structure
        Map<String, Object> destination = new HashMap<>();
        destination.put("to", to);

        Map<String, Object> messageContent = new HashMap<>();
        messageContent.put("destinations", new Map[]{ destination });
        messageContent.put("from", "TradeHub");
        messageContent.put("text", message);

        Map<String, Object>[] messages = new Map[]{ messageContent };
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", messages);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/sms/2/text/advanced", request, String.class);

            // Check the response, maybe throw an exception if it's not successful
            if (!response.getStatusCode().is2xxSuccessful()) {
                // Log this error instead of throwing a runtime exception
                logger.error("Failed to send SMS: " + response.getStatusCode() + " Response Body: " + response.getBody());
                throw new RuntimeException("Failed to send SMS: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            // Log the exception here... ResponseEntity.getBody() will retrieve the body content if available
            logger.error("Error sending SMS: " + e.getResponseBodyAsString());
            throw e;
        }
    }
}
