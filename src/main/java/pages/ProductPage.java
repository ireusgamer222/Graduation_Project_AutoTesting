package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductPage extends BasePage {

    // ==========================================
    //           ADD TO CART LOCATORS
    // ==========================================
    private final By dynamicAddToCartButton = By.cssSelector("input[id^='add-to-cart-button'], button[id^='add-to-cart-button']");
    private final By successNotificationBanner = By.id("bar-notification");
    private final By quantityInput = By.cssSelector("input.qty-input");
    private final By closeNotificationButton = By.cssSelector("#bar-notification span.close");

    // ==========================================
    //         ADD TO WISHLIST LOCATORS
    // ==========================================
    private final By addToWishlistButton = By.cssSelector("input[id^='add-to-wishlist-button'], button[id^='add-to-wishlist-button']");
    public ProductPage(WebDriver driver) {
        super(driver);
    }

    // ==========================================
    //         NAVIGATION METHODS
    // ==========================================

    public void navigateToProductAndAddToCart(String productUrl) {
        driver.get(productUrl);
        clickAddToCart();
        wait.until(ExpectedConditions.visibilityOfElementLocated(successNotificationBanner));
    }

    // ==========================================
    //         ADD TO CART METHODS
    // ==========================================

    public void clickAddToCart() {
        click(dynamicAddToCartButton);
    }

    public String getNotificationMessage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(successNotificationBanner));
        return readText(successNotificationBanner);
    }

    public boolean isAddToCartButtonClickable() {
        try {
            org.openqa.selenium.WebElement button = driver.findElement(dynamicAddToCartButton);
            return button.isDisplayed() && button.isEnabled();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void updateQuantity(String qty) {
        org.openqa.selenium.WebElement inputElement = driver.findElement(quantityInput);
        inputElement.clear();
        inputElement.sendKeys(qty);
    }

    public void dismissNotificationBanner() {
        click(closeNotificationButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(successNotificationBanner));
    }

    public boolean isNotificationBannerDisplayed() {
        try {
            return driver.findElement(successNotificationBanner).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isConfigurableProduct() {
        try {
            return driver.findElements(By.cssSelector(".product-variant-list, .attributes")).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    // ==========================================
    //       ADD TO WISHLIST METHODS
    // ==========================================

    public void clickAddToWishlist() {
        click(addToWishlistButton);
    }

    public boolean isAddToWishlistButtonPresent() {
        try {
            return driver.findElement(addToWishlistButton).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}