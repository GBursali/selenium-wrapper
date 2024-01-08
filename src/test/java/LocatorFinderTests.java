import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import space.gbsdev.locators.LocatorFinder;

import java.nio.file.Path;

public class LocatorFinderTests {
    public static final Path LOCATOR_PATH = Path.of("src", "test", "resources", "elements.json");
    private static final LocatorFinder locator = LocatorFinder.ofFile(LOCATOR_PATH);
    @Test
    public void locateLoginButtonWithCss(){
        final String expected = "div form button[type='submit']";

        LocatorFinder.ofFile(Path.of("src", "test", "resources", "elements.json"))
                .locate("LoginButton");
        var actual = locator.locate("LoginButton");
        Assertions.assertEquals(By.cssSelector(expected),actual);
    }
    @Test
    public void locateLoginButtonWithXpath(){
        final String expected = "//div//form//button[@type='submit']";
        var actual = locator.locate("LoginXp");
        Assertions.assertEquals(By.xpath(expected),actual);
    }
}
