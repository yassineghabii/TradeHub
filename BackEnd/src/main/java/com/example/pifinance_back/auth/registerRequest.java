package com.example.pifinance_back.auth;

import com.example.pifinance_back.Entities.WalletEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class registerRequest {
    private String email;
    private String pwd_user;
    private String firstname;
    private String lastname;
    private String cin;
    private String address;
    private String phonenumber;
    private MultipartFile profileImage;
    private WalletEnum type ;
// getters and setters pour ce champ

}
