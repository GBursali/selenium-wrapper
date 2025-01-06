import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import com.gbursali.strings.Placeholders;

public class PlaceHolderTests {
    @Test
    public void isPlaceHolderWorkForSimpleString(){
        Placeholders ph = Placeholders.of()
                .add("Type","test")
                .add("Language","Java")
                .add("Framework","JUnit")
                ;
        final String expected = "This test is written in Java with JUnit";
        String input = "This <Type> is written in <Language> with <Framework>";
        String actual = ph.apply(input);
        Assertions.assertEquals(expected,actual);
    }
}
