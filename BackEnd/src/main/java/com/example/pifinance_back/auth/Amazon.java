package com.example.pifinance_back.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Amazon {
    @JsonProperty("email") // Optional, as the name matches
    private String email;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("authToken")
    private String authToken;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("role") // Optional, as the name matches
    private String role;

}
