package com.example.pifinance_back.Controllers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public  class ChargeResponseDTO  {
    private String id;
    private Long amount;
    private String currency;
}
