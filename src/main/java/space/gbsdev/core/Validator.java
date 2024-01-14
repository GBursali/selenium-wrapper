package space.gbsdev.core;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.Assert;

import java.util.function.BiPredicate;
import java.util.function.Function;
@SuppressWarnings("unused")
public class Validator<T> {

    private final T actual;
    private T expected;
    private Function<Boolean,String> messageGenerator = ignored->"Data validation is failed!";
    private BiPredicate<T,T> action;
    private boolean goal = true;

    public Validator(T actual) {
        this.actual = actual;
    }

    public static <T> Validator<T> of(T actual){
        return new Validator<>(actual);
    }

    public Validator<T> compareAgainst(T expected){
        this.expected = expected;
        return this;
    }
    public Validator<T> withMessage(Function<Boolean,String> message){
        this.messageGenerator = message;
        return this;
    }
    public Validator<T> withAction(BiPredicate<T,T> action){
        this.action = action;
        return this;
    }
    public Validator<T> not(){
        this.goal = !goal;
        return this;
    }
    public void check(){
        if(action == null)
            throw new NotImplementedException("There is no action implemented for this validation.");

        if(action.negate().test(expected,actual) != goal)
            Assert.fail(messageGenerator.apply(goal));
    }
}
