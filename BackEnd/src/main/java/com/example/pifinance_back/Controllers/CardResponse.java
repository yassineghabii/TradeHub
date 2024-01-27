package com.example.pifinance_back.Controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardResponse {
    @JsonProperty
    private String message;
    @JsonProperty
    private String cardType; // Type de la carte (Visa, MasterCard, etc.)

}
