package com.example.pifinance_back.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Data
@Builder
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueChargement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private com.example.pifinance_back.Entities.Client user; // Assurez-vous d'avoir une entité 'User' définie

    @Column(name = "stripe_charge_id", nullable = false)
    private String stripeChargeId;

    @Column(nullable = false)
    private BigDecimal amount_conv;
    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = true)
    private Currency currency;
    @ManyToOne
    @JsonIgnore

    @JoinColumn(name = "card_id")
    private Card card;

    @Column(name = "date_transaction", nullable = false)
    private LocalDateTime dateTransaction;
}
