package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

/*
 * SearchPage handles interactions with the search functionality.
 * It extends BasePage to reuse the robust wrapper methods
 * with built-in explicit waits.
 */
public class SearchPage extends BasePage {

    // ==========================================
    //                 LOCATORS
    // ==========================================
    private final By searchBox = By.id("small-searchterms");
    private final By searchButton = By.cssSelector("input[value='Search']");
    private final By firstProduct = By.cssSelector("h2.product-title a");

    // Updated Locator
    private final By noResultMessage = By.cssSelector("strong.result");

    // ==========================================
    //               CONSTRUCTOR
    // ==========================================
    public SearchPage(WebDriver driver) {
        super(driver); // Passes driver to BasePage to initialize waits
    }

    // ==========================================
    //               PAGE METHODS
    // ==========================================

    // Search using Search button
    public void searchProduct(String productName) {
        writeText(searchBox, productName);
        click(searchButton);
    }

    // Search using Enter key
    public void searchUsingEnter(String productName) {
        writeText(searchBox, productName);
        // writeText already located the element and sent the text,
        // so we just need to find it again to send the Enter key.
        driver.findElement(searchBox).sendKeys(Keys.ENTER);
    }

    // Get first product name
    public String getFirstProductName() {
        return readText(firstProduct);
    }

    // Get "No Products Found" message
    public String getNoResultMessage() {
        return readText(noResultMessage);
    }

    // Open first product
    // Open first product
    public void openFirstProduct() {
        int attempts = 0;
        while (attempts < 2) {
            try {
                // Wait for it to be clickable, then click immediately
                wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(firstProduct)).click();
                break; // If successful, exit the loop
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                // If the DOM refreshed and the element is stale, loop will try one more time
                attempts++;
            }
        }
    }

    // Get current page title
    public String getPageTitle() {
        return driver.getTitle();
    }
}