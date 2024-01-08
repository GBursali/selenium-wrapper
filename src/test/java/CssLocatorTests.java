import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import space.gbsdev.locators.chain.Css;

public class CssLocatorTests {
    @Test
    public void createCSSLocators() {
        String expected = "main#site-main div.inner section>figure>img:nth-child(1)[height='825']";
        String actual = Css.of()
                .tag("main").id("site-main")
                .descendant().tag("div").wClass("inner")
                .descendant().tag("section")
                .child().tag("figure")
                .child().tag("img").index(1).attr("height", "825")
                .toString();
        Assertions.assertEquals(expected,actual);
    }
}
