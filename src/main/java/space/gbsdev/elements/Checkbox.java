package space.gbsdev.elements;

import org.openqa.selenium.By;

public class Checkbox extends HTMLElement {
    public Checkbox(By by) {
        super(by);
    }

    /**
     * Simulates a click on the checkbox. This will toggle the state of the checkbox.
     */
    public void check(){
        asElement().click();
    }
}
