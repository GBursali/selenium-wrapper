package space.gbsdev.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

public class Textbox extends HTMLElement {
    public Textbox(By by) {
        super(by);
    }

     /**
     * Retrieves the value of the textbox element.
     *
     * @return The value of the textbox element.
     */
    public String getValue(){
        // Get the underlying WebElement and retrieve its "value" attribute
        return asElement().getAttribute("value");
    }

    /**
     * Clears the content of the textbox element.
     * <p>
     * This method forces the element to refresh and clears any existing text content from the textbox.
     *
     * @return This Textbox instance after the clear operation.
     */
    public Textbox clear(){
        forceNew().asElement().clear();
        return this;
    }

    /**
     * Forces the content of the textbox element to be cleared, even if it is read-only.
     * This method is useful when the element is read-only, as the {@link #clear()} method will not work.
     *
     * @return This Textbox instance after the clear operation.
     */
    public Textbox forceClear() {
        clear().sendKeys("a", Keys.BACK_SPACE);
        return this;

    }
}
