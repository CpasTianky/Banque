package app.com;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

class MontantTest {
    Currency euro = Currency.getInstance("EUR");
    Currency yen = Currency.getInstance("JPY");

    @Test
    void value_euro_valide() {
        Montant m = new Montant(new BigDecimal("10.00"),euro );

        assertEquals(new BigDecimal("10.00"), m.value());
        assertEquals(euro, m.currency());
    }

    @Test
    void value_nulle_refuses() {
        assertThrows(NullPointerException.class, () -> new Montant(null, euro));
    }

    @Test
    void euro_nulle_refuses() {
        assertThrows(NullPointerException.class, () -> new Montant(new BigDecimal("10.00"), null));
    }

    @Test
    void value_digits_refused(){
        assertThrows(IllegalArgumentException.class, () -> new Montant(new BigDecimal("10.000"), euro));
    }

    @Test
    void normalization_value() {
        Montant a = new Montant(new BigDecimal("10"), euro);
        Montant b = new Montant(new BigDecimal("10.00"), euro);
        assertEquals(a,b);
    }

    @Test
    void yen_int_value(){
        Montant a = new Montant(new BigDecimal("10"),yen);
        assertEquals(new BigDecimal("10"),a.value());
    }

    @Test
    void yen_double_value(){
        assertThrows(IllegalArgumentException.class, () -> new Montant(new BigDecimal("10.00"), yen));
    }
}