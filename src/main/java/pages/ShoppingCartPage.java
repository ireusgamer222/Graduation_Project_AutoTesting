package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.StaleElementReferenceException;
import java.util.List;

public class ShoppingCartPage extends BasePage {

    // ==========================================
    //                 LOCATORS
    // ==========================================
    private final By emptyCartMessage = By.cssSelector(".order-summary-content");
    private final By productName = By.className("product-name");
    private final By qtyInput = By.className("qty-input");
    private final By updateCartButton = By.cssSelector("input[name='updatecart'], button[name='updatecart']");
    private final By removeCheckbox = By.name("removefromcart");
    private final By continueShoppingButton = By.name("continueshopping");
    private final By couponInput = By.name("discountcouponcode");
    private final By applyCouponButton = By.name("applydiscountcouponcode");
    private final By couponMessage = By.cssSelector(".coupon-box .message");
    private final By giftCardInput = By.name("giftcardcouponcode");
    private final By applyGiftCardButton = By.name("applygiftcardcouponcode");
    private final By giftCardMessage = By.cssSelector(".giftcard-box .message");
    private final By termsOfServiceCheckbox = By.id("termsofservice");
    private final By checkoutButton = By.id("checkout");
    private final By productPrice = By.cssSelector(".product-unit-price");
    private final By cartSubtotal = By.cssSelector(".product-subtotal");
    private final By productImage = By.cssSelector(".product-picture img");
    private final By notificationBanner = By.cssSelector(".bar-notification.error, .message-error, .quantity-error, .text-danger");
    private final By orderTotalLabel = By.cssSelector(".order-total strong, .cart-total p, .totals strong");
    private final By shoppingCartPageContainer = By.className("shopping-cart-page");
    private final By logoutLink = By.className("ico-logout");
    private final By fallbackLogoutLink = By.linkText("Log out");

    // ==========================================
    //               CONSTRUCTOR
    // ==========================================
    public ShoppingCartPage(WebDriver driver) {
        super(driver);
    }

    // ==========================================
    //               PAGE METHODS
    // ==========================================

    public void navigateTo(String url) {
        driver.get(url);
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }

    public void logoutSession() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
        } catch (Exception e) {
            wait.until(ExpectedConditions.elementToBeClickable(fallbackLogoutLink)).click();
        }
    }

    public String getCartPageText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(shoppingCartPageContainer)).getText();
    }

    public String getCurrentPageUrl() {
        return driver.getCurrentUrl();
    }

    public void clearCartIfNotEmpty() {
        // Ensure cart page DOM is settled
        wait.until(ExpectedConditions.presenceOfElementLocated(shoppingCartPageContainer));

        // Check 1: Is the empty cart message present? (uses findElements to avoid exception)
        List<WebElement> emptyMsgs = driver.findElements(emptyCartMessage);
        if (!emptyMsgs.isEmpty()) {
            String text = emptyMsgs.get(0).getText().toLowerCase();
            if (text.contains("empty")) {
                System.out.println("Cart state verification: Cart is already empty.");
                return;
            }
        }

        // Check 2: Are there any remove checkboxes? (retry once if page is slow)
        List<WebElement> checkboxes = driver.findElements(removeCheckbox);
        if (checkboxes.isEmpty()) {
            try {
                Thread.sleep(1500); // Brief pause for AJAX render
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            checkboxes = driver.findElements(removeCheckbox);
            if (checkboxes.isEmpty()) {
                System.out.println("Cart state verification: No removable items found.");
                return;
            }
        }

        // Remove all items
        for (WebElement checkbox : checkboxes) {
            if (!checkbox.isSelected()) {
                checkbox.click();
            }
        }
        click(updateCartButton);

        // Verify cart is actually empty
        try {
            wait.until(ExpectedConditions.textToBePresentInElementLocated(
                    emptyCartMessage, "Your Shopping Cart is empty!"));
            System.out.println("Cart state verification: Cart cleared successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to clear shopping cart", e);
        }
    }

    public String getEmptyCartMessage() {
        return readText(emptyCartMessage).trim();
    }

    public String getProductName() {
        return readText(productName).trim();
    }

    public void updateQuantity(String quantity) {
        WebElement qtyField = wait.until(ExpectedConditions.visibilityOfElementLocated(qtyInput));
        qtyField.clear();
        qtyField.sendKeys(quantity);
        click(updateCartButton);
    }

    public void clickUpdateShoppingCart() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(updateCartButton)).click();
        } catch (StaleElementReferenceException e) {
            // 🎯 FIX: If an AJAX refresh replaced the button in the DOM, re-find it and retry
            wait.until(ExpectedConditions.elementToBeClickable(updateCartButton)).click();
        }
    }

    public boolean isCartEmptyMessageDisplayed() {
        try {
            WebElement emptyMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(emptyCartMessage));
            return emptyMessage.getText().contains("Your Shopping Cart is empty!");
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isCartEmpty() {
        try {
            String cartText = wait.until(ExpectedConditions.visibilityOfElementLocated(emptyCartMessage)).getText();
            return cartText.contains("Your Shopping Cart is empty!");
        } catch (TimeoutException e) {
            return true;
        }
    }

    public String getQuantityValue() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(qtyInput)).getAttribute("value");
    }

    public HomePage clickContinueShopping() {
        click(continueShoppingButton);
        return new HomePage(driver);
    }

    public void applyCoupon(String code) {
        writeText(couponInput, code);
        click(applyCouponButton);
    }

    public String getCouponErrorMessage() {
        return readText(couponMessage).trim();
    }

    public void applyGiftCardCode(String code) {
        writeText(giftCardInput, code);
        click(applyGiftCardButton);
    }

    public String getGiftCardValidationMessage() {
        return readText(giftCardMessage).trim();
    }

    public void acceptTermsOfService() {
        click(termsOfServiceCheckbox);
    }

    public void clickCheckout() {
        click(checkoutButton);
    }

    public void acceptTermsAndCheckout() {
        acceptTermsOfService();
        clickCheckout();
    }

    public String getProductPrice() {
        return readText(productPrice).trim();
    }

    public String getCartSubtotal() {
        return readText(cartSubtotal).trim();
    }

    public String getOrderTotalText() {
        try {
            return readText(orderTotalLabel).trim();
        } catch (Exception e) {
            return readText(cartSubtotal).trim();
        }
    }

    public boolean isProductImageDisplayed() {
        try {
            WebElement img = wait.until(ExpectedConditions.visibilityOfElementLocated(productImage));
            String srcValue = img.getAttribute("src");
            return img.isDisplayed() && srcValue != null && !srcValue.trim().isEmpty();
        } catch (NoSuchElementException | TimeoutException e) {
            return false;
        }
    }

    public String getSubtotalText() {
        return getCartSubtotal();
    }

    public void decreaseQuantity() {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(qtyInput));
        int currentQty = Integer.parseInt(input.getAttribute("value").trim());
        if (currentQty > 1) {
            updateQuantity(String.valueOf(currentQty - 1));
        }
    }

    public void waitForSubtotalToChange(String previousSubtotal) {
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(cartSubtotal, previousSubtotal)));
    }

    public String getNotificationMessage() {
        return readText(notificationBanner);
    }
}