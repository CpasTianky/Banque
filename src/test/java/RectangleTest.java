import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RectangleTest {

    @Test
    void height_setter() {
        Rectangle r = new Rectangle(4, 4);      // préparer

        double resultat = r.height();             // agir

        assertEquals(3, resultat);             // vérifier
    }
}