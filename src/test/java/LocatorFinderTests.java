import org.junit.Test;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import com.gbursali.locators.LocatorFinder;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocatorFinderTests {
    public static final Path LOCATOR_PATH = Path.of("src", "test", "resources", "elements.json");
    private static final LocatorFinder locator = LocatorFinder.ofFile(LOCATOR_PATH);

    @Test
    @DisplayName("LocatorFinder should locate the CSS selector and generate a By element")
    public void locateLoginButtonWithCss() {
        final String expected = "div form button[type='submit']";

        //Single-line use
        LocatorFinder.ofFile(Path.of("src", "test", "resources", "elements.json"))
                .locate("LoginButton");

        //variable type use
        var actual = locator.locate("LoginButton");
        assertEquals(By.cssSelector(expected), actual);
    }

    @Test
    @DisplayName("LocatorFinder should locate the XPath selector and generate a By element")
    public void locateLoginButtonWithXpath() {
        final String expected = "//div//form//button[@type='submit']";
        var actual = locator.locate("LoginXp");
        assertEquals(By.xpath(expected), actual);
    }

    @Test
    @DisplayName("LocatorFinder should throw and IllegalArgumentException when the key is not found")
    public void throwIAEOnKeyNotFound() {
        final String invalidKey = "invalid-key";
        var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> locator.locate(invalidKey));
        assertEquals("JSON does not have the key: " + invalidKey, exception.getMessage());
    }
}
