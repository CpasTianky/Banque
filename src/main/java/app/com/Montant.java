package app.com;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

record Montant(BigDecimal value, Currency currency){
    Montant{
        Objects.requireNonNull(value,"val can't be null");
        Objects.requireNonNull(currency,"devise can't be null");
        if (value.scale() > currency.getDefaultFractionDigits()) throw new IllegalArgumentException("value not match the currency");
        try {
            value = value.setScale(currency.getDefaultFractionDigits(), RoundingMode.UNNECESSARY);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

    }
}
