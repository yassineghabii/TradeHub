package com.example.pifinance_back.Entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTransaction ;
    private double PrixExecution ;
    private LocalDateTime DateExecution;
    private int QuantiteExecute;
    @Enumerated(EnumType.STRING)
    private TypeTransaction typeTransaction;

}
