package com.example.pifinance_back.Controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateClientDTO {
    private String email;
    private String phone_number;
    private String address;
    private String pwd_user;


}
