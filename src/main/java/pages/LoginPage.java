package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class LoginPage extends BasePage {

    // ==========================================
    //                  LOCATORS
    // ==========================================
    private final By emailInput = By.id("Email");
    private final By passwordInput = By.id("Password");
    private final By loginButton = By.cssSelector("input.button-1.login-button");

    // Validation message locators
    private final By validationSummaryErrors = By.cssSelector("div.validation-summary-errors");
    private final By emailValidationError = By.cssSelector("span[data-valmsg-for='Email']");
    // --- Add these variable declarations if they are missing ---
    private final By validationSummaryErrorsBy = By.cssSelector("div.validation-summary-errors");

    // THIS IS THE ONE CAUSING YOUR ERROR
    private final By errorMessageForValidEmailBy = By.cssSelector("span[data-valmsg-for='Email']");
    // ==========================================
    //                 CONSTRUCTOR
    // ==========================================
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // ==========================================
    //               CORE ACTIONS
    // ==========================================

    /**
     * Standard full login flow
     */
    public void login(String email, String password) {
        writeText(emailInput, email);
        writeText(passwordInput, password);
        click(loginButton);
    }

    // Granular actions for testing invalid login flows step-by-step
    public void enterEmail(String email) {
        writeText(emailInput, email);
    }

    public void enterPassword(String password) {
        writeText(passwordInput, password);
    }

    public void clickLoginButton() {
        click(loginButton);
    }

    public void focusPasswordField() {
        // Reusing the safe click method from BasePage to focus the field
        click(passwordInput);
    }

    // ==========================================
    //          VALIDATIONS & ERROR CHECKS
    // ==========================================

    public boolean isValidationSummaryErrorsDisplayed() {
        try {
            return wait.until(d -> d.findElement(validationSummaryErrors).isDisplayed());
        } catch (Exception e) {
            return false;
        }
    }

    public String getValidationSummaryErrorsText() {
        return readText(validationSummaryErrors);
    }

    public List<String> getAllErrorMessagesText() {
        List<String> errorMessages = new ArrayList<>();
        try {
            // Wait for the main summary block to be visible
            WebElement summaryBlock = wait.until(ExpectedConditions.visibilityOfElementLocated(validationSummaryErrors));
            List<WebElement> errorItems = summaryBlock.findElements(By.tagName("li"));

            for (WebElement item : errorItems) {
                String text = item.getText().trim();
                if (!text.isEmpty()) {
                    errorMessages.add(text);
                }
            }
        } catch (Exception e) {
            System.out.println("No validation summary errors found or visible.");
        }
        return errorMessages;
    }

    public boolean isEmailValidationErrorMessageDisplayed() {
        try {
            return wait.until(d -> {
                WebElement element = d.findElement(emailValidationError);
                return element.isDisplayed() && !element.getText().isEmpty();
            });
        } catch (Exception e) {
            return false;
        }
    }

    // Inside LoginPage.java
    public String getErrorMessageForValidEmailText() {
        try {
            // Only wait if you are sure it should be there, or return empty string
            return driver.findElement(errorMessageForValidEmailBy).getText();
        } catch (Exception e) {
            return ""; // Return empty string if not found instead of crashing
        }
    }
}