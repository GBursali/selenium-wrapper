package space.gbsdev;

import org.junit.Assert;
import space.gbsdev.core.Validators;

@SuppressWarnings("unused")
public class Validation extends Assert {
    private Validation() {
        // Private constructor to enforce the use of the builder.
    }

    // === Validation Methods ===
    /**
     * Asserts that the expected string is contained within the actual string.
     *
     * @param expected The expected string to check for.
     * @param actual   The actual string to search in.
     */
    public static void assertContains(String expected, String actual) {
        Validators.getContainsValidator(expected, actual).check();
    }

    /**
     * Asserts that the expected string is contained within the actual string.
     *
     * @param message  Custom message to display on failure.
     * @param expected The expected string to check for.
     * @param actual   The actual string to search in.
     */
    public static void assertContains(String message, String expected, String actual) {
        Validators.getContainsValidator(
                        ignored -> message,
                        expected,
                        actual)
                .check();
    }

    /**
     * Asserts that the expected string is not contained within the actual string.
     *
     * @param unexpected The expected string to check for.
     * @param actual   The actual string to search in.
     */
    public static void assertNotContains(String unexpected, String actual) {
        Validators.getContainsValidator(unexpected, actual).not().check();
    }

    /**
     * Asserts that the expected string is not contained within the actual string.
     *
     * @param message  Custom message to display on failure.
     * @param unexpected The expected string to check for.
     * @param actual   The actual string to search in.
     */
    public static void assertNotContains(String message, String unexpected, String actual) {
        Validators.getContainsValidator(
                        ignored -> message,
                        unexpected,
                        actual)
                .not()
                .check();
    }

    // === Regular Expression Methods ===
    /**
     * Asserts that the given value matches the specified Regular Expression pattern.
     * @param pattern Regular Expression pattern to validate
     * @param value value to check
     */
    public static void assertRegexMatching(String pattern, String value){
        Validators.getRegexValidator(pattern,value).check();
    }

    /**
     * Asserts that the given value matches the specified Regular Expression pattern.
     * @param pattern Regular Expression pattern to validate
     * @param value value to check
     */
    public static void assertRegexMatching(String message,String pattern, String value){
        Validators.getRegexValidator(ignored->message,pattern,value).check();
    }

    /**
     * Asserts that the given value does not match the specified Regular Expression pattern.
     * @param pattern Regular Expression pattern to validate
     * @param value value to check
     */
    public static void assertRegexNotMatching(String pattern, String value){
        Validators.getRegexValidator(pattern,value).not().check();
    }

    /**
     * Asserts that the given value does not match the specified Regular Expression pattern.
     * @param pattern Regular Expression pattern to validate
     * @param value value to check
     */
    public static void assertRegexNotMatching(String message,String pattern, String value){
        Validators.getRegexValidator(ignored->message,pattern,value).not().check();
    }
}
