package com.example.pifinance_back.Services;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ClientProfileDTO {
    private Long id;
    private String lastname;
    private String id_admin;
    private String firstname;
    private String cin;
    private String phone_number;
    private String address;
    private String email;

}
