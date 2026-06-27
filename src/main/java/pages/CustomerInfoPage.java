package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/*
 * CustomerInfoPage handles interactions with the user's account information page.
 * It extends BasePage to reuse the robust wrapper methods
 * with built-in explicit waits.
 */
public class CustomerInfoPage extends BasePage {

    // ==========================================
    //                 LOCATORS
    // ==========================================
    private final By accountEmail = By.cssSelector("a[href='/customer/info']");
    private final By male = By.id("gender-male");
    private final By female = By.id("gender-female");
    private final By firstName = By.id("FirstName");
    private final By lastName = By.id("LastName");
    private final By email = By.id("Email");
    private final By saveButton = By.className("save-customer-info-button");

    // ==========================================
    //               CONSTRUCTOR
    // ==========================================
    public CustomerInfoPage(WebDriver driver) {
        super(driver); // Passes driver to BasePage to initialize waits
    }

    // ==========================================
    //               PAGE METHODS
    // ==========================================

    public void openCustomerInfoPage() {
        click(accountEmail);
    }

    public void selectMale() {
        click(male);
    }

    public void selectFemale() {
        click(female);
    }

    public void enterFirstName(String name) {
        writeText(firstName, name);
    }

    public void enterLastName(String name) {
        writeText(lastName, name);
    }

    public void enterEmail(String mail) {
        writeText(email, mail);
    }

    public void clickSave() {
        click(saveButton);
    }
}