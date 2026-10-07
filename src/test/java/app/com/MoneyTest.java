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

        @Test
        void detects_zero(){
            Money a = new Money(BigDecimal.ZERO, euro);
            Money b = new Money(new BigDecimal("1"), euro);
            Money c = new Money(BigDecimal.ZERO, yen);
            assertTrue(a.isZero());
            assertFalse(b.isZero());
            assertTrue(c.isZero());
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
            Money a = new Money(new BigDecimal("1.50"), euro);
            Money b = new Money(new BigDecimal("3.25"), euro);

            assertEquals(new Money(new BigDecimal("4.75"),euro),a.add(b));
        }

        @Test
        void does_not_modify_operands_when_adding(){
            Money a = new Money(new BigDecimal("1"), euro);
            Money b = new Money(new BigDecimal("1"), euro);
            a.add(b);

            assertEquals(new Money(new BigDecimal("1"), euro),a);
            assertEquals(new Money(new BigDecimal("1"), euro),b);
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

            assertEquals(new Money(new BigDecimal("2"),euro),a.subtract(b));
        }

        @Test
        void allows_negative_result_when_subtracting(){
            Money a = new Money(new BigDecimal("1"), euro);
            Money b = new Money(new BigDecimal("3"), euro);

            assertEquals(new Money(new BigDecimal("-2"),euro),a.subtract(b));
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
        @Test
        void creates_zero_in_given_currency(){
            Money a = new Money(new BigDecimal("0.00"), euro);
            assertEquals(a, Money.zero(euro));
        }

        @Test
        void detects_negative_amount(){
            Money a = new Money(new BigDecimal("-10"), euro);
            Money b = new Money(new BigDecimal("10"), euro);
            assertTrue(a.isNegative());
            assertFalse(b.isNegative());
        }

        @Test
        void compares_amounts_of_same_currency(){
            Money a = new Money(new BigDecimal("10"), euro);
            Money b = new Money(new BigDecimal("20"), euro);
            assertTrue(b.isGreaterThan(a));
            assertFalse(a.isGreaterThan(b));
        }

        @Test
        void rejects_comparison_of_different_currencies(){
            Money a = new Money(new BigDecimal("10"), euro);
            Money b = new Money(new BigDecimal("20"), yen);
            assertThrows(IllegalArgumentException.class, () -> a.isGreaterThan(b));
        }
    }
}