package com.example.pifinance_back.auth;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigInteger;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Facebook {

    @JsonProperty("email") // Optional, as the name matches
    private String email;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("authToken")
    private String authToken;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("name") // Optional, as the name matches
    private String name;



    // getters, setters, and other methods
}
