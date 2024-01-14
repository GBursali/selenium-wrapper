package space.gbsdev.locators.chain;

import org.openqa.selenium.By;
import space.gbsdev.strings.MatchType;

@SuppressWarnings("unused")
public class Css extends Locator<Css> {
    /**
     * Creates a new instance of {@code Css} as a fallback when needed.
     *
     * @return A new instance of {@code Css}.
     */
    @Override
    protected Css createFallback() {
        return this;
    }

    /**
     * Converts this {@code Css} object to a Selenium {@code By} object with a CSS selector.
     *
     * @return A Selenium {@code By} object representing the CSS selector.
     */
    @Override
    public By toBy() {
        return By.cssSelector(innerLocator.toString());
    }


    /**
     * Creates a new instance of Css with the given initial text.
     *
     * @param initialText The initial text for the CSS selector.
     */
    protected Css(String initialText) {
        innerLocator = new StringBuilder(initialText);
    }


    /**
     * Creates a new instance of Css from the given selector.
     *
     * @param selector The CSS selector.
     * @return A new instance of Css.
     */
    public static Css fromSelector(String selector) {
        return new Css(selector);
    }

    /**
     * Creates a new instance of {@code Css} with an empty initial text.
     *
     * @return A new instance of {@code Css} with an empty initial text.
     */
    public static Css of() {
        return new Css("");
    }

    /**
     * Appends the ":nth-child()" pseudo-class to the current CSS selector with the specified index.
     * This is often used to select elements based on their position among a group of siblings.
     *
     * @param index The index of the child element.
     * @return A new instance of {@code Css} with the ":nth-child()" pseudo-class appended.
     */
    public Css index(int index){
        return addThenFallback(":nth-child(%d)".formatted(index));
    }


    /**
     * Creates a new instance of Css for a specific tag name.
     *
     * @param tagName The tag name.
     * @return A new instance of Css.
     */
    public static Css fromTagName(String tagName) {
        return of().tag(tagName);
    }

    public Css descendant() {
        return addThenFallback(" ");
    }

    /**
     * Appends a child selector (>) and returns a new instance of Css.
     *
     * @return A new instance of Css.
     */
    public Css child() {
        return addThenFallback(">");
    }

    /**
     * Appends a CSS attribute selector to the current selector. The attribute selector
     * matches elements with the specified attribute having the specified value.
     * The matching type is set to MatchType.IS by default.
     *
     * @param attributeName The name of the attribute.
     * @param value         The expected value of the attribute.
     * @return A new instance of Css with the added attribute selector.
     */
    public Css attr(String attributeName, Object value) {
        return attr(attributeName, value, MatchType.IS);
    }

    /**
     * Appends a CSS attribute selector to the current selector. The attribute selector
     * matches elements with the specified attribute and value, based on the specified matching type.
     *
     * @param attributeName The name of the attribute.
     * @param value         The expected value of the attribute.
     * @param type          The type of matching to be performed (IS, HAS, STARTS_WITH, ENDS_WITH).
     * @return A new instance of Css with the added attribute selector.
     */
    public Css attr(String attributeName, Object value, MatchType type) {
        String attrValue = stringify(value);
        String typeSign = getSignOf(type);
        return addThenFallback(
                "[",
                attributeName,
                typeSign,
                attrValue,
                "]"
        );
    }

    /**
     * Appends a CSS class selector to the current selector. Matches elements that have the specified class.
     *
     * @param classname The name of the class.
     * @return A new instance of Css with the added class selector.
     */
    public Css wClass(String classname) {
        return addThenFallback(".",classname);
    }

    /**
     * Appends a CSS tag selector to the current selector. Matches elements with the specified tag name.
     *
     * @param tagName The name of the tag.
     * @return A new instance of Css with the added tag selector.
     */
    public Css tag(String tagName) {
        return addThenFallback(tagName);
    }

    /**
     * Appends a CSS id selector to the current selector. Matches elements with the specified id value.
     *
     * @param id ID value of the element.
     * @return A new instance of Css with the added id selector.
     */
    public Css id(String id) {
        return addThenFallback("#",id);
    }

    /**
     * Gets the CSS selector sign corresponding to the specified matching type.
     *
     * @param type The matching type (IS, HAS, STARTS_WITH, ENDS_WITH).
     * @return The CSS selector sign.
     */
    private String getSignOf(MatchType type) {
        return switch (type) {
            case IS -> "=";
            case HAS -> "*=";
            case STARTS_WITH -> "^=";
            case ENDS_WITH -> "$=";
        };
    }
}
