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
            throw new IllegalArgumentException("Rounding error "+amount+" - "+currency,e);
        }
    }
    static Money zero(Currency currency){
        return new Money(BigDecimal.ZERO,currency);
    }

    boolean isZero(){
        return amount.signum() == 0;
    }

    boolean isNegative(){
        return amount.signum() == -1;
    }

    boolean isGreaterThan(Money x){
        checkCurrency(x);
        return amount.compareTo(x.amount) > 0;
    }

    Money add(Money x){
        checkCurrency(x);
        return new Money(amount.add(x.amount),currency);
    }

    Money subtract(Money x){
        checkCurrency(x);
        return new Money(amount.subtract(x.amount),currency);
    }

    private void checkCurrency(Money a){
        if (currency != a.currency){
            throw new IllegalArgumentException("Different currency");
        }
    }
}
