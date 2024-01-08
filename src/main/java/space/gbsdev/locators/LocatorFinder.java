package space.gbsdev.locators;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Utility class for finding locators in Selenium based on a JSON configuration.
 */
public class LocatorFinder {
    private static final Pattern xpathPattern = Pattern.compile("^((body)|(\\/\\/)).*");
    private final JsonObject baseJson;

    /**
     * Constructs a LocatorFinder with the provided JSON object containing locators.
     *
     * @param locatorList The JSON object containing locators.
     */
    protected LocatorFinder(JsonObject locatorList) {
        this.baseJson = locatorList;
    }

    /**
     * Creates a LocatorFinder instance by reading locators from a JSON file.
     *
     * @param file The path to the JSON file containing locators.
     * @return A LocatorFinder instance initialized with locators from the file.
     * @throws IllegalStateException If the file is not found or cannot be read.
     */
    public static LocatorFinder ofFile(Path file) {
        try {
            String contents = Files.readString(file);
            return new LocatorFinder(JsonParser.parseString(contents).getAsJsonObject());
        } catch (IOException e) {
            throw new IllegalStateException("File not found on: " + file.toAbsolutePath(), e);
        }
    }

    /**
     * Returns a Selenium {@link By} instance based on the locator type (XPath or CSS selector).
     *
     * @param locator The locator string.
     * @return A By instance representing the locator type.
     */
    private By returnWithType(String locator) {
        if (xpathPattern.matcher(locator).find())
            return By.xpath(locator);
        return By.cssSelector(locator);
    }

    /**
     * Locates a Selenium {@link By} instance based on the provided key.
     *
     * @param key The key corresponding to the desired locator in the JSON configuration.
     * @return A By instance representing the located element.
     * @throws IllegalArgumentException If the key is not present in the JSON configuration.
     */
    public By locate(String key) {
        if (!baseJson.has(key))
            throw new IllegalArgumentException("JSON does not have the key: " + key);
        String locator = baseJson.get(key).getAsString();
        return returnWithType(locator);
    }

    /**
     * Locates a {@link WebElement} based on the provided key using the Selenium WebDriver.
     *
     * @param driver The WebDriver instance used to find the element.
     * @param key    The key corresponding to the desired locator in the JSON configuration.
     * @return A {@link WebElement} instance representing the located element.
     * @throws IllegalStateException If the key is not present in the JSON configuration.
     */
    public WebElement locateElement(WebDriver driver, String key){
        return locate(key).findElement(driver);
    }

    /**
     * Locates a list of {@link WebElement}s based on the provided key using the Selenium WebDriver.
     *
     * @param driver The WebDriver instance used to find the elements.
     * @param key    The key corresponding to the desired locator in the JSON configuration.
     * @return A list of {@link WebElement} instances representing the located elements.
     * @throws IllegalStateException If the key is not present in the JSON configuration.
     */
    public List<WebElement> locateElements(WebDriver driver, String key){
        return locate(key).findElements(driver);
    }
}

