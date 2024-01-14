package space.gbsdev.core;

import space.gbsdev.strings.Placeholders;

/**
 * Utility class that contains default fail messages.
 */
@SuppressWarnings("unused")
public class FailMessage {

    /**
     * Placeholder for internal template usage
     */
    private final Placeholders placeholder = Placeholders.of();

    /**
     * Please use the {@link FailMessage#of} function
     */
    private FailMessage(boolean goal) {
        placeholder.add("should",should(goal));
    }

    /**
     * Insaniates a Fail Message pull with the goal.
     *
     * @param goal Goal we want to achieve. This determines our sentences should be negative or positive
     * @return A FailMessage instance, ready to select a String construct
     */
    public static FailMessage of(boolean goal) {
        return new FailMessage(goal);
    }

    /**
     * Message for Contains messages.
     *
     * @param expected Replacement for Expected value
     * @param actual   Replacement for Actual value
     * @return String that should be sent to the error if the validation fails
     */
    public String constructContains(String expected, String actual) {
        return placeholder
                .add("actual", actual)
                .add("expected", expected)
                .apply("<actual> <should> contain the text <expected>");
    }

    /**
     * Message for Regular Expression messages.
     *
     * @param template Regex pattern that {@code actual} should or should not follow
     * @param actual   Replacement for Actual value
     * @return String that should be sent to the error if the validation fails
     */
    public String constructTemplated(String template, String actual) {
        return placeholder
                .add("actual", actual)
                .add("template", template)
                .apply("<actual> <should> follow the template of <template>");
    }

    private static String should(boolean isPositive) {
        return isPositive ? "should" : "should not";
    }
}
