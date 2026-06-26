package pages;

// Ensure this import matches your BasePage location
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegisterPage extends BasePage {

    // Locators
    private final By femaleRadioButton = By.id("gender-female");
    private final By firstNameInput = By.id("FirstName");
    private final By lastNameInput = By.id("LastName");
    private final By emailInput = By.id("Email");
    private final By passwordInput = By.id("Password");
    private final By confirmPasswordInput = By.id("ConfirmPassword");
    private final By registerButton = By.id("register-button");
    private final By registrationResultMessage = By.xpath("//div[@class='result']");
    private final By registeredInfo = By.xpath("(//a[@href='/customer/info'])[1]");
    private final By continueButton = By.cssSelector("input[value='Continue']");

    private final By firstNameIsRequiredMessage = By.cssSelector("span[data-valmsg-for='FirstName'] > span");
    private final By lastNameIsRequiredMessage = By.cssSelector("span[data-valmsg-for='LastName'] > span");
    private final By emailRequiredMessage = By.cssSelector("span[data-valmsg-for='Email'] > span");
    private final By passwordRequiredMessage = By.cssSelector("span[data-valmsg-for='Password'] > span");
    private final By confirmPasswordRequiredMessage = By.cssSelector("span[data-valmsg-for='ConfirmPassword'] > span");

    // Constructor updated to accept WebDriver
    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    public void registerData(String firstName, String lastName, String email, String password, String confirmPassword) {
        click(femaleRadioButton);
        writeText(firstNameInput, firstName);
        writeText(lastNameInput, lastName);
        writeText(emailInput, email);
        writeText(passwordInput, password);
        writeText(confirmPasswordInput, confirmPassword);
        click(registerButton);
    }

    public String getResultMessage() {
        return readText(registrationResultMessage);
    }

    public void clickContinue() {
        click(continueButton);
    }

    public void clickRegisterButton() {
        click(registerButton);
    }

    public boolean isInfoDisplayed() {
        try {
            return driver.findElement(registeredInfo).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getRegisteredCustomerInfo() {
        return readText(registeredInfo).trim();
    }

    // Warning message checks
    public boolean isFirstNameRequiredWarningMessageDisplayed() { return isErrorMessageDisplayed(firstNameIsRequiredMessage); }
    public boolean isLastNameRequiredWarningMessageDisplayed() { return isErrorMessageDisplayed(lastNameIsRequiredMessage); }
    public boolean isEmailRequiredWarningMessageDisplayed() { return isErrorMessageDisplayed(emailRequiredMessage); }
    public boolean isPasswordRequiredWarningMessageDisplayed() { return isErrorMessageDisplayed(passwordRequiredMessage); }
    public boolean isConfirmPasswordWarningMessageDisplayed() { return isErrorMessageDisplayed(confirmPasswordRequiredMessage); }

    // Warning message text extraction
    public String getFirstNameWarningMessageText() { return readText(firstNameIsRequiredMessage); }
    public String getLastNameWarningMessageText() { return readText(lastNameIsRequiredMessage); }
    public String getEmailWarningMessageText() { return readText(emailRequiredMessage); }
    public String getPasswordWarningMessageText() { return readText(passwordRequiredMessage); }
    public String getConfirmPasswordWarningMessageText() { return readText(confirmPasswordRequiredMessage); }

    // Helper method
    // Helper method
    private boolean isErrorMessageDisplayed(By locator) {
        try {
            // Using findElements is much faster because it doesn't wait for a timeout
            // if the element is absent. It just returns an empty list immediately.
            java.util.List<org.openqa.selenium.WebElement> elements = driver.findElements(locator);

            if (elements.isEmpty()) {
                return false; // Element not in DOM, so not displayed
            }

            // If element is in DOM, check if it's visible and has text
            return elements.get(0).isDisplayed() && !elements.get(0).getText().isEmpty();

        } catch (Exception e) {
            return false;
        }
    }


}