package app.com;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

record Money(BigDecimal amount, Currency currency){
    Money {
        Objects.requireNonNull(amount,"amount can't be null");
        Objects.requireNonNull(currency,"currency can't be null");
        try {
            amount = amount.setScale(currency.getDefaultFractionDigits(), RoundingMode.UNNECESSARY);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("Rounding error",e);
        }

    }
}
