package space.gbsdev.core;

/**
 * Utility class that contains default fail messages.
 */
@SuppressWarnings("unused")
public class FailMessage {
    /**
     * Goal we wanted to achieve with this validator.
     */
    private boolean goal = true;

    /**
     * Please use the {@link FailMessage#of} function
     */
    private FailMessage(){}

    /**
     * Insaniates a Fail Message pull with the goal.
     * @param goal Goal we want to achieve. This determines our sentences should be negative or positive
     * @return A FailMessage instance, ready to select a String construct
     */
    public static FailMessage of(boolean goal){
        FailMessage str = new FailMessage();
        str.goal = goal;
        return str;
    }

    /**
     * Message for Contains messages.
     * @param expected Replacement for Expected value
     * @param actual Replacement for Actual value
     * @return String that should be sent to the error if the validation fails
     */
    public String constructContains(String expected,String actual){
        return "<%s> %s contain the text <%s>".formatted(actual,should(goal),expected);
    }

    /**
     * Message for Regular Expression messages.
     * @param template Regex pattern that {@code actual} should or should not follow
     * @param actual Replacement for Actual value
     * @return String that should be sent to the error if the validation fails
     */
    public String constructTemplated(String template, String actual){
        return "<%s> %s follow the template of <%s>".formatted(actual,should(goal), template);
    }

    private static String should(boolean isPositive){
        return isPositive ? "should" : "should not";
    }
}
