package space.gbsdev.core;

import java.util.function.Function;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Validators {
    protected Validators(){/*Hide*/}

    public static Validator<String> getContainsValidator(String expected, String actual){
        return getContainsValidator(goal->FailMessage.of(goal).constructContains(expected, actual),expected,actual);
    }
    public static Validator<String> getContainsValidator(Function<Boolean,String> message, String expected, String actual){
        return Validator.of(actual)
                .withMessage(message)
                .compareAgainst(expected)
                .withAction((ex, act) -> actual.contains(expected));
    }

    public static Validator<String> getRegexValidator(String pattern, String value){
        return getRegexValidator(goal->FailMessage.of(goal).constructTemplated(pattern,value),pattern,value);
    }
    public static Validator<String> getRegexValidator(Function<Boolean,String> message,String pattern, String value){
        return Validator.of(pattern)
                .withMessage(message)
                .compareAgainst(value)
                .withAction((textToCheck, regexPattern)-> Pattern.compile(regexPattern).matcher(textToCheck).find());
    }
}
