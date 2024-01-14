package space.gbsdev.verification;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import space.gbsdev.core.Utilities;
import space.gbsdev.strings.Placeholders;

import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Supplier;
/**
 * Utility class for verifying values against a rule set defined in a JSON file.
 * This class allows users to check if a provided value matches a rule identified by a key.
 * If placeholders are provided, they are applied to the values before checking.
 */
public class Verification {

    /**
     * Supplier for providing placeholders to be applied to values.
     */
    private final Supplier<Placeholders> placeholders;

    /**
     * Rule set defined in a JSON file.
     */
    private final JsonObject ruleSet;

    /**
     * Constructs a new Verification instance from the specified JSON file and optional placeholder supplier.
     *
     * @param jsonFile The path to the JSON file containing the rule set.
     * @param holder   Optional supplier for providing placeholders.
     */
    public Verification(Path jsonFile, Supplier<Placeholders> holder) {
        String contents = Utilities.getFileContents(jsonFile);
        this.ruleSet = JsonParser.parseString(contents).getAsJsonObject();
        this.placeholders = holder;
    }

    /**
     * Creates a new Verification instance from the specified JSON file with no placeholders.
     *
     * @param jsonFile The path to the JSON file containing the rule set.
     * @return A new Verification instance.
     */
    public static Verification of(Path jsonFile) {
        return of(jsonFile, ()->null);
    }

    /**
     * Creates a new Verification instance from the specified JSON file with optional placeholders.
     *
     * @param jsonFile The path to the JSON file containing the rule set.
     * @param holder   Optional supplier for providing placeholders.
     * @return A new Verification instance.
     */
    public static Verification of(Path jsonFile, Supplier<Placeholders> holder) {
        return new Verification(jsonFile, holder);
    }


    /**
     * Creates a new Verification instance from the specified JSON file with optional placeholders.
     *
     * @param jsonFile The path to the JSON file containing the rule set.
     * @param holder  Placeholder instance to apply.
     * @return A new Verification instance.
     */
    public static Verification of(Path jsonFile, Placeholders holder) {
        return new Verification(jsonFile, ()->holder);
    }

    /**
     * Checks whether the given value matches the rule identified by the specified key.
     *
     * @param ruleKey The key identifying the rule to check.
     * @param value   The value to be checked.
     * @return True if the value matches the rule, false otherwise.
     * @throws IllegalArgumentException If the rule identified by the key is not defined.
     */
    public boolean check(String ruleKey, String value) {
        String actual = applyPlaceholder(extractRule(ruleKey));
        return actual.equals(value);
    }

    /**
     * Extracts the rule associated with the specified key from the rule set.
     *
     * @param ruleKey The key identifying the rule.
     * @return The rule associated with the key.
     * @throws IllegalArgumentException If the rule identified by the key is not defined.
     */
    private String extractRule(String ruleKey) {
        if (!ruleSet.has(ruleKey)) {
            throw new IllegalArgumentException("Rule not found: " + ruleKey +
                    ". Make sure the rule is defined in the JSON file.");
        }
        return ruleSet.get(ruleKey).getAsString();
    }

    /**
     * Applies placeholders to the value if a placeholder supplier is provided.
     *
     * @param value The value to which placeholders may be applied.
     * @return The value with placeholders applied, or the original value if no placeholders are provided.
     */
    private String applyPlaceholder(String value) {
        if (Objects.nonNull(placeholders) && Objects.nonNull(placeholders.get())) {
            value = placeholders.get().apply(value);
        }
        return value;
    }
}
