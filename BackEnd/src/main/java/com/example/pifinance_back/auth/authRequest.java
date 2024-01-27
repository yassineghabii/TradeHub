package com.example.pifinance_back.auth;

import com.example.pifinance_back.Entities.UserEnum;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class authRequest {
    private String email;
    private String pwd_user;
    private UserEnum role;
    private String id_admin ;

}
