package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.JavascriptExecutor;

public class HomePage extends BasePage {

    // Core Locators
    private final By loginHeaderLink = By.className("ico-login");
    private final By logoutLink = By.className("ico-logout"); // 🎯 ADDED: Added locator for logout session state
    private final By emailField = By.id("Email");
    private final By passwordField = By.id("Password");
    private final By loginButton = By.cssSelector("input.login-button");
    private final By shoppingCartLink = By.className("cart-label");
    private final By registerLink = By.linkText("Register");

    // Dynamic Product Search & Addition Locators
    private final By searchBox = By.id("small-searchterms");
    private final By searchButton = By.cssSelector(".search-box-button");
    private final By genericAddToCartButton = By.cssSelector(".product-box-add-to-cart-button, input.product-box-add-to-cart-button");

    // Notification Bar Locators
    private final By successNotificationBar = By.id("bar-notification");
    private final By closeNotificationButton = By.cssSelector("#bar-notification span.close");

    // Product and Badge Locators
    private final By laptopAddToCartButton = By.xpath("//a[text()='14.1-inch Laptop']/ancestor::div[@class='details']//input[@value='Add to cart']");
    private final By cartBadge = By.className("cart-qty");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage login(String email, String password) {
        click(loginHeaderLink);
        writeText(emailField, email);
        writeText(passwordField, password);
        click(loginButton);
        return this;
    }

    public ShoppingCartPage clickShoppingCart() {
        click(shoppingCartLink);
        return new ShoppingCartPage(driver);
    }

    /**
     * Adds a specific predefined laptop to the cart using optimized JS scrolling
     */
    public void addLaptopToCart() {
        org.openqa.selenium.WebElement addToCartBtn = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(
                        org.openqa.selenium.By.xpath("//a[contains(text(),'Laptop')]/ancestor::div[@class='product-item']//input[@value='Add to cart' or @value='View details']")
                )
        );
        addToCartBtn.click();
    }

    /**
     * Searches for any product dynamically by name and adds it to the cart
     */
    public void addProductToCart() {
        org.openqa.selenium.WebElement addBtn = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(
                        org.openqa.selenium.By.cssSelector("input.product-box-add-to-cart-button, .product-box-add-to-cart-button")
                )
        );
        addBtn.click();
    }

    public void addProductToCart(String productName) {
        if (productName.equalsIgnoreCase("Fiction")) {
            driver.get("https://demowebshop.tricentis.com/fiction");
            org.openqa.selenium.WebElement addBtn = wait.until(
                    org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(
                            org.openqa.selenium.By.id("add-to-cart-button-45")
                    )
            );
            addBtn.click();
        } else {
            org.openqa.selenium.WebElement fallbackBtn = wait.until(
                    org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(
                            org.openqa.selenium.By.xpath("//a[contains(text(),'" + productName + "')]/ancestor::div[@class='product-item']//input[@value='Add to cart']")
                    )
            );
            fallbackBtn.click();
        }
    }

    private void dismissNotificationBanner() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(successNotificationBar));
        click(closeNotificationButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(successNotificationBar));
    }

    public void waitForCartBadgeCount(String count) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(cartBadge, count));
    }

    public String getCartBadgeCount() {
        return readText(cartBadge);
    }

    // 🎯 ADDED: This connects to your BasePage visibility helper to stabilize the post-login state
    public void waitForVisibilityOfLogout() {
        waitForVisibility(logoutLink);
    }


    // ==========================================
    //       NEW METHODS FOR ADD TO CART TESTS
    // ==========================================

    /**
     * Safely checks if the user is logged in by looking for the logout link.
     * Does not throw an exception if the element is missing.
     */
    public boolean isUserLoggedIn() {
        try {
            return driver.findElement(logoutLink).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Clicks the login link to initiate the authentication flow.
     */
    public void clickLoginLink() {
        click(loginHeaderLink);
    }

    /**
     * Parses the cart badge text (e.g., "(1)") into an integer for mathematical assertions.
     * Returns 0 if the badge is empty or missing.
     */
    public int getCartBadgeCountAsInt() {
        try {
            String badgeText = getCartBadgeCount();
            // Strip out parentheses and whitespace
            String numericText = badgeText.replaceAll("[^0-9]", "");
            if (numericText.isEmpty()) {
                return 0;
            }
            return Integer.parseInt(numericText);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Waits for the cart badge text to explicitly update to the expected incremented value.
     */
    public void waitForCartBadgeToUpdate(int initialCount) {
        int expectedCount = initialCount + 1;
        String expectedText = "(" + expectedCount + ")";

        // Wait for the exact expected string to appear in the DOM
        wait.until(ExpectedConditions.textToBePresentInElementLocated(cartBadge, expectedText));
    }

    /**
     * Wrapper method to match the AddToCartTest syntax.
     */
    public void clickAddToCartFromListing(String productName) {
        addProductToCart(productName);
    }


    public void logoutSession() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
        } catch (Exception e) {
            // Fallback if primary locator fails
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Log out"))).click();
        }
    }


    public void clickRegisterLink() {
        click(registerLink);
    }

    public void clickLogoutButton() {
        click(logoutLink);
    }


    // ==========================================
    //            HEADER LOCATORS
    // ==========================================
    private final By headerAccountEmailLink = By.cssSelector("div.header-links a.account");
    // ==========================================
    //            SESSION METHODS
    // ==========================================

    /**
     * Retrieves the email text displayed in the header for the logged-in user.
     */
    public String getLoggedCustomerEmail() {
        return readText(headerAccountEmailLink);
    }

}