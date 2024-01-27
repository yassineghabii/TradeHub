package com.example.pifinance_back.auth;

import com.example.pifinance_back.Entities.UserEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Table;

@Data
@Builder
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class authResponse {
    private String token;
    private String idAdmin;
    @JsonProperty("authToken")
    private String authToken;
    private String welcome;
    private int id; // Add this field
    private Long id_wallet; // Add this field
    private Long id_card; // Add this field

    private String message;
    private String errorMessage;
    private UserEnum role ;
    private String firstname ;
    private String lastname ;

    private String cin ;
    public String getFullName() {
        return firstname + " " + lastname;
    }
    // getter and setter methods
}
//idadmin pour backoffice supprimé