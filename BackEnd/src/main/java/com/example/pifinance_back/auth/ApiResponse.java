package com.example.pifinance_back.auth;

import lombok.*;

import javax.persistence.Table;

@Data
@Builder
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ApiResponse {
    private String message;

}
