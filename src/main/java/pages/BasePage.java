package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/*
 * BasePage is the parent class for all page classes.
 *
 * Instead of writing the same Selenium actions
 * (clicking, typing, waiting, reading text)
 * in every page, we keep them here and let
 * other pages reuse them.
 */
public class BasePage {

    // Browser instance used by all page classes
    protected WebDriver driver;

    // Explicit wait used to handle slow-loading elements
    protected WebDriverWait wait;

    /*
     * Constructor:
     * Receives the browser driver and creates
     * a 10-second explicit wait.
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /*
     * Wait until an element becomes visible on the page.
     *
     * Example:
     * Wait for a login form to appear before interacting with it.
     */
    protected void waitForVisibility(By locator) {
        wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions
                        .visibilityOfElementLocated(locator)
        );
    }

    /*
     * Click an element only when it is ready to be clicked.
     *
     * This helps avoid failures caused by page loading delays.
     */
    protected void click(By locator) {
        wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions
                        .elementToBeClickable(locator)
        ).click();
    }

    /*
     * Enter text into an input field.
     *
     * The field is cleared first to make sure
     * old text does not remain.
     */
    protected void writeText(By locator, String text) {
        wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions
                        .visibilityOfElementLocated(locator)
        );

        driver.findElement(locator).clear();
        driver.findElement(locator).sendKeys(text);
    }

    /*
     * Read and return text from an element.
     *
     * Example:
     * Reading a success message or product name.
     */
    protected String readText(By locator) {
        return wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions
                        .visibilityOfElementLocated(locator)
        ).getText();
    }

    /*
     * Open a specific URL in the browser.
     */
    public void navigateTo(String url) {
        driver.get(url);
    }
}