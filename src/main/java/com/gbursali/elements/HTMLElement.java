package com.gbursali.elements;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

import java.util.Optional;
import java.util.function.Function;

import static org.awaitility.Awaitility.await;

public class HTMLElement {
    protected final By locator;
    public static SearchContext driver;

    protected WebElement cachedElement;

    public Verify verify = new Verify();

    public ElementWaiter waitFor = new ElementWaiter();

    public HTMLElement(By by) {
        if(driver == null)
            throw new NullPointerException("You should set the HTMLElement.driver first.");
        this.locator = by;
    }

    /**
     * Checks if the element associated with this HTMLElement exists in the DOM.
     *
     * @return {@code true} if the element exists, {@code false} otherwise.
     * @throws NoSuchElementException if the element cannot be found.
     */

    public boolean isExist() {
        try {
            return asElement() != null;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Returns the By locator associated with this HTMLElement.
     *
     * @return the By locator associated with this HTMLElement
     */

    public By asBy() {
        return locator;
    }

    /**
     * Retrieves the web element for the associated By locator.
     * <p>
     * If the element has not been retrieved before or is no longer valid,
     * <p>
     * it is retrieved from the web page.
     *
     * @return The web element associated with this HTMLElement
     */

    public WebElement asElement() {
        if (!isElementValid())
            cachedElement = locator.findElement(driver);

        return cachedElement;
    }

    /**
     * Returns true if the cached web element is not null and is displayed.
     * <p>
     * False otherwise or if the element is stale.
     *
     * @return true if the element is valid, false otherwise
     */

    private boolean isElementValid() {
        try {
            return cachedElement != null && cachedElement.isDisplayed();
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Clears the cached web element.
     * <p>
     * This method forces a fresh retrieval
     * <p>
     * of the element from the web page
     * <p>
     * the next time it is needed.
     */

    public HTMLElement forceNew() {
        cachedElement = null;
        return this;
    }

    /**
     * This method is used to decorate page object fields with the necessary logic to
     * <p>
     * locate the corresponding web element. It is used by the PageFactory to
     * <p>
     * initialize the page object fields.
     *
     * @return A FieldDecorator that can be used to decorate page object fields.
     */
    public static FieldDecorator getDecorator(SearchContext driver) {
        HTMLElement.driver = driver;
        return (loader, field) -> {
            Function<By, ?> buildElement = x -> {
                try {
                    return field.getType().getConstructor(By.class).newInstance(x);
                } catch (ReflectiveOperationException e) {
                    throw new IllegalArgumentException("Constructor cannot be set via By element. Cause: "+e.getMessage(),e);
                }
            };
            FindBy annotation = field.getAnnotation(FindBy.class);
            if (annotation != null) {
                FindBy.FindByBuilder byBuilder = new FindBy.FindByBuilder();
                //Check through all of our classes
                if (HTMLElement.class.isAssignableFrom(field.getType()))
                    return buildElement.apply(byBuilder.buildIt(annotation, field));
            }
            return null;
        };

    }

    /**
     * Finds an HTMLElement based on the provided By locator.
     *
     * @param by The By locator used to find the element.
     * @return An Optional containing the found HTMLElement if it exists, otherwise an empty Optional.
     */

    public static Optional<HTMLElement> findElement(By by) {
        HTMLElement elem = new HTMLElement(by);
        if (elem.isExist())
            return Optional.of(elem);
        return Optional.empty();
    }

    /**
     * Clicks the element.
     */
    public void click() {
        waitFor.clickability().asElement().click();
    }

    /**
     * Types the given keys into the element.
     *
     * @param text the sequence of keys to type
     */
    public void sendKeys(CharSequence... text) {
        waitFor.clickability().asElement().sendKeys(String.join("", text));
    }

    /**
     * Retrieves the text content of the element associated with this HTMLElement.
     *
     * @return The text content of the web element.
     */

    public String getText() {
        return asElement().getText();
    }

    /**
     * Retrieves the value of the specified attribute from the element associated with this HTMLElement.
     * <p>
     * If the attribute does not exist, an empty Optional is returned.
     *
     * @param attribute The name of the attribute to retrieve.
     * @return An Optional containing the attribute's value, or an empty Optional if the attribute does not exist.
     */

    public Optional<String> getAttribute(String attribute) {
        return Optional.ofNullable(asElement().getAttribute(attribute));
    }

    public class Verify {

        /**
         * Asserts that the text of the element is equal to the provided text.
         *
         * @param expectedText The expected text content of the element.
         */

        public void text(String expectedText) {
            String actualText = getText();
            Assert.assertEquals(expectedText, actualText);
        }

        /**
         * Asserts that the text of the element contains to the provided text.
         *
         * @param expectedText The expected text content of the element.
         */

        public void textContains(String expectedText) {
            String actualText = getText();
            Assert.assertTrue(actualText + " does not contain the " + expectedText,
                    actualText.contains(expectedText));
        }

        /**
         * Asserts that the element exists.
         */
        public void existence() {
            Assert.assertTrue(isExist());
        }

        /**
         * Asserts that the element does not exist.
         */
        public void nonExistence() {
            Assert.assertFalse(isExist());
        }

        /**
         * Asserts that the element is a required field.
         * <p>
         * This method retrieves the "required" attribute of the element and asserts that it is not null.
         */
        public void isRequired() {
            Assert.assertNotNull(getAttribute("required"));
        }

        /**
         * Asserts that the element is not arequired field.
         * <p>
         * This method retrieves the "required" attribute of the element and asserts that it is null.
         */

        public void isNotRequired() {
            Assert.assertNull(getAttribute("required"));
        }

        /**
         * Asserts that the element is disabled.
         * <p>
         * This method retrieves the "disabled" attribute of the element and asserts that it is not null.
         */
        public void isDisabled() {
            Assert.assertNotNull(getAttribute("disabled"));
        }

        /**
         * Asserts that the element is not disabled.
         * <p>
         * This method retrieves the "disabled" attribute of the element and asserts that it is null.
         */

        public void isNotDisabled() {
            Assert.assertNull(getAttribute("disabled"));
        }

    }

    public class ElementWaiter {

        public HTMLElement clickability() {
            var element = HTMLElement.this;
            await("Waiting for element to be clickable")
                    .ignoreException(NoSuchElementException.class)
                    .ignoreException(StaleElementReferenceException.class)
                    .ignoreException(ElementNotInteractableException.class)
                    .until(() -> element.isExist() &&
                            element.asElement().isDisplayed() &&
                            element.asElement().isEnabled()
                    );
            return element;
        }
    }
}
