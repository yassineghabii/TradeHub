package com.example.pifinance_back.Entities;
import com.example.pifinance_back.Entities.Card;
import com.example.pifinance_back.Entities.Client;
import com.example.pifinance_back.Entities.VirtualCurrency;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Data
@Builder
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_wallet;
    @OneToOne
    private Client user;
    @JsonProperty("Virtual_balance")
    @Column(nullable = true, precision = 10, scale = 2)
    private BigDecimal Virtual_balance;
    @JsonProperty("Real_balance")
    @Column(nullable = true, precision = 10, scale = 2)
    private BigDecimal Real_balance;
    @JsonProperty("Real_Currency")
    private Currency realCurrency;
    @JsonProperty("Virtual_Currency")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255) default 'TRD'")
    private VirtualCurrency virtualCurrency = VirtualCurrency.TRD;
    @JsonProperty("createdAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = new Date();
        }
    }

    @JsonProperty("isActive")
    private boolean isActive;
    @JsonProperty("tokenTransaction")
    @Column(length = 255, unique = true)
    private String tokenTransaction;
    @JsonProperty("type")
    @Enumerated(EnumType.STRING)
    private com.example.pifinance_back.Entities.WalletEnum type;
    /*  @OneToMany(cascade = CascadeType.ALL, mappedBy = "wallet")
      private List<Transaction> transactions; // Historique des transactions */
    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Card card;
    public Wallet(Long id_wallet, BigDecimal virtual_balance, BigDecimal real_balance, Currency realCurrency, com.example.pifinance_back.Entities.WalletEnum type, VirtualCurrency virtualCurrency, Date createdAt, boolean isActive, String tokenTransaction) {
        this.id_wallet = id_wallet;
        this.Virtual_balance = virtual_balance;
        this.Real_balance = real_balance;
        this.realCurrency = realCurrency;
this.type=type;
this.virtualCurrency = virtualCurrency;
        this.createdAt = createdAt;
        this.isActive = isActive;
        this.tokenTransaction = tokenTransaction;
    }
    // Méthode pour ajouter une paire actif-quantité


}
