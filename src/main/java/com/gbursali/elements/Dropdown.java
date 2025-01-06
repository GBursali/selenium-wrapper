package com.gbursali.elements;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class Dropdown extends HTMLElement {
    public static String dropdownOptionsLocator = "//div[@data-ref='menu-dropdown']//div[contains(@id,'react-select')][@tabindex]";

    private final Map<String, Function<WebElement, String>> optionsActionList = Map.of(
            "text", WebElement::getText
    );

    public Dropdown(By locator) {
        super(locator);
    }

    /**
     * Opens the dropdown by clicking on the element associated with this
     * Dropdown object.
     */
    public Dropdown openDropdown() {
        asElement().click();
        return this;
    }

    /**
     * Retrieves all options available in the dropdown.
     *
     * <p>
     * This method locates all elements within the dropdown that are
     * identified by the `dropdownOptionsLocator`. It returns a list of
     * WebElement objects representing each option in the dropdown.
     *
     * @return A list of WebElement objects representing the options
     * in the dropdown.
     */
    public List<WebElement> getOptions() {
        return By.xpath(dropdownOptionsLocator).findElements(driver);
    }

    /**
     * Retrieves an option in the dropdown with the given text.
     *
     * <p>
     * The option is searched for in the dropdown options by using the
     * {@code .} XPath function. If the option is found, an
     * {@link Optional} containing the found option is returned. If the option is
     * not found, an empty {@link Optional} is returned.
     *
     * @param text The text of the option to be retrieved
     * @return An Optional containing the found option, or an empty Optional if
     * the option is not found
     */
    public Optional<HTMLElement> getOption(String text) {
        String optionLocator = dropdownOptionsLocator + String.format("//div[.='%s']", text);
        return findElement(By.xpath(optionLocator));
    }

    /**
     * Retrieves an option in the dropdown that contains the given text.
     *
     * <p>
     * The option is searched for in the dropdown options by using the
     * {@code contains} XPath function. If the option is found, an
     * {@link Optional} containing the found option is returned. If the option is
     * not found, an empty {@link Optional} is returned.
     *
     * @param text The text that the option should contain.
     * @return An {@link Optional} containing the found option.
     */
    public Optional<HTMLElement> getOptionContains(String text) {
        String locator = dropdownOptionsLocator + String.format("//div[contains(.,'%s')]", text);
        return findElement(By.xpath(locator));
    }

    public void verifyOptions(String attributeToCheck, List<String> expected) {
        Function<WebElement, String> action = optionsActionList.getOrDefault(attributeToCheck, x -> x.getAttribute(attributeToCheck));
        List<String> actual = getOptions().stream()
                .map(action)
                .toList();

        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    /**
     * Selects an option in the dropdown with the given text.
     * <p>
     * If the dropdown is not open, it will be opened first. If the option is not
     * found, a NoSuchElementException will be thrown.
     *
     * @param text The text of the option to be selected.
     */
    public void select(String text) {
        if (!isOpen())
            openDropdown();
        if (!isOpen())
            asElement().sendKeys(text);
        HTMLElement htmlElement = getOption(text).orElseThrow(() -> new NoSuchElementException("Could not find option with text: " + text));
        htmlElement.click();
        close();
    }

    /**
     * Selects an option in the dropdown that contains the given text.
     * <p>
     * If the dropdown is not open, it will be opened first. If the option is not
     * found, a NoSuchElementException will be thrown.
     *
     * @param text The text to search for in the dropdown options.
     */
    public void selectContains(String text) {
        if (!isOpen())
            openDropdown();
        if (!isOpen())
            asElement().sendKeys(text);
        HTMLElement htmlElement = getOptionContains(text).orElseThrow(() -> new NoSuchElementException("Could not find option with text: " + text));
        htmlElement.click();
        close();
    }

    /**
     * Returns true if the dropdown is currently open, false otherwise.
     * A dropdown is considered open if an element with the attribute
     * data-ref="menu-dropdown" exists in the DOM.
     *
     * @return true if the dropdown is open, false otherwise
     */
    public static boolean isOpen() {
        return findElement(By.cssSelector("div[data-ref=\"menu-dropdown\"]")).isPresent();
    }

    /**
     * Closes a popup by clicking on its header if it exists, otherwise clicks on the first h1 element found on the page.
     */
    public static void close() {
        findElement(By.cssSelector("div.popup h1"))
                .or(() -> findElement(By.cssSelector("h1")))
                .or(() -> findElement(By.cssSelector("h3")))
                .ifPresent(HTMLElement::click);
    }

}