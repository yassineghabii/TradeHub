package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.Wallet;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }
    public Charge createCharge(Map<String, Object> chargeParams) throws StripeException {
        return Charge.create(chargeParams);
    }

    public Charge chargeWallet(Wallet wallet, String stripeToken, BigDecimal rechargeAmount) {
        int amountInCents = rechargeAmount.multiply(new BigDecimal(100)).intValue();
        String currency = wallet.getRealCurrency().getCurrencyCode();

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", amountInCents);
        chargeParams.put("currency", currency);
        chargeParams.put("description", "Rechargement du portefeuille");
        chargeParams.put("source", stripeToken);

        try {
            return createCharge(chargeParams);
        } catch (StripeException e) {
            throw new RuntimeException("Erreur lors de la recharge du portefeuille : " + e.getMessage());
        }
    }

}
