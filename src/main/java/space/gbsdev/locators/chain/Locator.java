package space.gbsdev.locators.chain;

import org.openqa.selenium.By;

/**
 * Base class for constructing locators used in Selenium testing.
 *
 * @param <T> The type of the concrete locator class.
 */
public abstract class Locator<T extends Locator> {
    protected StringBuilder innerLocator = new StringBuilder();

    /**
     * Creates a fallback instance of the locator.
     *
     * @return A new instance of the locator.
     */
    protected abstract T createFallback();

    /**
     * Converts the locator to a Selenium By object.
     *
     * @return The Selenium By object representing the locator.
     */
    public abstract By toBy();

    /**
     * Stringifies the given value for inclusion in the locator.
     *
     * @param value The value to stringify.
     * @return The stringified value.
     */
    protected String stringify(Object value) {
        return (value instanceof String) ? "'" + value + "'" : value.toString();
    }

    /**
     * Adds the specified texts to the inner locator.
     *
     * @param texts The texts to add to the locator.
     */
    protected void add(String... texts) {
        for (String text : texts) {
            innerLocator.append(text);
        }
    }

    /**
     * Returns a new instance of the locator with the specified texts added to the locator.
     *
     * @param texts The texts to add to the locator.
     * @return A new instance of the locator.
     */
    protected T addThenFallback(String... texts) {
        add(texts);
        return createFallback();
    }

    /**
     * Retrieves the locator object
     * @return Locator as String
     */
    @Override
    public String toString() {
        return innerLocator.toString();
    }
}
