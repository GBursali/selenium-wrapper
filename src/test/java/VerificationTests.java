import com.google.gson.JsonObject;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import com.gbursali.core.Utilities;
import com.gbursali.strings.Placeholders;
import com.gbursali.verification.Verification;

import java.nio.file.Path;

public class VerificationTests {

    private final Path jsonFile = Path.of("src","test","resources","verifications.json");
    private final JsonObject jsonObject = Utilities.getJsonFile(jsonFile);

    private final Placeholders phToUse = Placeholders.of()
            .add("role","admin");
    @Test
    @DisplayName("We should get a verification text without any placeholder positive")
    public void withoutPlaceholder(){
        final String ruleKey = "woPlaceholderTextError";
        final String expected = jsonObject.get(ruleKey).getAsString();

        boolean actual = Verification.of(jsonFile)
                .check(ruleKey,expected);
        Assertions.assertTrue(actual);

    }

    @Test
    @DisplayName("We should not get a verification text without any placeholder")
    public void nWithoutPlaceholder(){
        final String ruleKey = "woPlaceholderTextError";
        final String expected = "Wrong text";

        boolean actual = Verification.of(jsonFile)
                .check(ruleKey,expected);
        Assertions.assertFalse(actual);
    }

    @Test
    @DisplayName("We should get a verification text with placeholder supplier")
    public void withPlaceholder(){
        final String ruleKey = "PlaceholderTestError";
        final String expected = "You must be logged in as admin to access this page.";

        boolean actual = Verification.of(jsonFile,()->phToUse)
                .check(ruleKey,expected);
        Assertions.assertTrue(actual);
    }

    @Test
    @DisplayName("We should not get a verification text with placeholder supplier")
    public void nWithPlaceholder(){
        final String ruleKey = "PlaceholderTestError";
        final String expected = "You must be logged in as <role> to access this page.";

        boolean actual = Verification.of(jsonFile,()->phToUse)
                .check(ruleKey,expected);
        Assertions.assertFalse(actual);
    }

    @Test
    @DisplayName("We should get a verification text with placeholder")
    public void withPlaceholderInstance(){
        final String ruleKey = "PlaceholderTestError";
        final String expected = "You must be logged in as admin to access this page.";

        boolean actual = Verification.of(jsonFile,phToUse)
                .check(ruleKey,expected);
        Assertions.assertTrue(actual);
    }

    @Test
    @DisplayName("We should not get a verification text with placeholder")
    public void nWithPlaceholderInstance(){
        final String ruleKey = "PlaceholderTestError";
        final String expected = "You must be logged in as <role> to access this page.";

        boolean actual = Verification.of(jsonFile,phToUse)
                .check(ruleKey,expected);
        Assertions.assertFalse(actual);
    }
}
