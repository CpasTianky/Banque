package app.com;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {
    Currency euro = Currency.getInstance("EUR");
    Currency yen = Currency.getInstance("JPY");


    @Nested
    class Creation{
        @Test
        void exposes_amount_and_currency_of_valid_euro_money() {
            Money m = new Money(new BigDecimal("10.00"),euro );

            assertEquals(new BigDecimal("10.00"), m.amount());
            assertEquals(euro, m.currency());
        }

        @Test
        void rejects_null_amount() {
            assertThrows(NullPointerException.class, () -> new Money(null, euro));
        }

        @Test
        void rejects_null_currency() {
            assertThrows(NullPointerException.class, () -> new Money(new BigDecimal("10.00"), null));
        }
    }

    @Nested
    class Normalization{
        @Test
        void rejects_euro_amount_requiring_rounding(){
            assertThrows(IllegalArgumentException.class, () -> new Money(new BigDecimal("10.005"), euro));
        }

        @Test
        void accepts_euro_amount_with_extra_trailing_zeros(){
            Money a = new Money(new BigDecimal("100"), euro);
            Money b = new Money(new BigDecimal("100.000"), euro);

            assertEquals(a,b);
        }

        @Test
        void normalizes_whole_euro_amount_to_two_decimals() {
            Money a = new Money(new BigDecimal("10"), euro);
            Money b = new Money(new BigDecimal("10.00"), euro);
            assertEquals(a,b);
        }

        @Test
        void accepts_whole_yen_amount(){
            Money a = new Money(new BigDecimal("10"),yen);
            assertEquals(new BigDecimal("10"),a.amount());
        }

        @Test
        void rejects_yen_amount_with_decimals(){
            assertThrows(IllegalArgumentException.class, () -> new Money(new BigDecimal("10.05"), yen));
        }

        @Test
        void accepts_yen_amount_with_trailing_zero(){
            Money a = new Money(new BigDecimal("100"), yen);
            Money b = new Money(new BigDecimal("100.0"), yen);

            assertEquals(a,b);
        }
    }

    @Nested
    class Arithmetic {
        @Test
        void adds_two_amounts_of_same_currency(){
            Money a = new Money(new BigDecimal("1"), euro);
            Money b = new Money(new BigDecimal("1"), euro);

            assertEquals(a.add(b),new Money(new BigDecimal("2"),euro));
        }

        @Test
        void does_not_modify_operands_when_adding(){
            Money a = new Money(new BigDecimal("1"), euro);
            Money b = new Money(new BigDecimal("1"), euro);
            a.add(b);

            assertEquals(a,new Money(new BigDecimal("1"), euro));
            assertEquals(b,new Money(new BigDecimal("1"), euro));
        }

        @Test
        void rejects_addition_of_different_currencies(){
            Money a = new Money(new BigDecimal("1"), euro);
            Money b = new Money(new BigDecimal("1"), yen);

            assertThrows(IllegalArgumentException.class, () -> a.add(b));
        }

        @Test
        void subtracts_two_amounts_of_same_currency(){
            Money a = new Money(new BigDecimal("3"), euro);
            Money b = new Money(new BigDecimal("1"), euro);

            assertEquals(a.subtract(b),new Money(new BigDecimal("2"),euro));
        }

        @Test
        void allows_negative_result_when_subtracting(){
            Money a = new Money(new BigDecimal("1"), euro);
            Money b = new Money(new BigDecimal("3"), euro);

            assertEquals(a.subtract(b),new Money(new BigDecimal("-2"),euro));
        }

        @Test
        void rejects_subtraction_of_different_currencies(){
            Money a = new Money(new BigDecimal("1"), euro);
            Money b = new Money(new BigDecimal("1"), yen);

            assertThrows(IllegalArgumentException.class, () -> a.subtract(b));
        }
    }

    @Nested
    class Comparison{

    }
}