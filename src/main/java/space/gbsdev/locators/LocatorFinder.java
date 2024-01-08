package space.gbsdev.locators;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.openqa.selenium.By;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class LocatorFinder {
    private static final Pattern xpathPattern = Pattern.compile("^((body)|(\\/\\/)).*");
    private final JsonObject baseJson;

    public LocatorFinder(JsonObject locatorList) {
        this.baseJson = locatorList;
    }

    public static LocatorFinder ofFile(Path file){
        try {
            String contents = Files.readString(file);
            return new LocatorFinder(JsonParser.parseString(contents).getAsJsonObject());
        } catch (IOException e) {
            throw new RuntimeException("File not found");
        }
    }
    private By returnWithType(String locator){
        if(xpathPattern.matcher(locator).find())
            return By.xpath(locator);
        return By.cssSelector(locator);
    }

    public By locate(String key){
        String locator = baseJson.get(key).getAsString();
        return returnWithType(locator);
    }
}
