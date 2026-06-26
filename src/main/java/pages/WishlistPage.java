package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class WishlistPage extends BasePage {

    // ==========================================
    //            HEADER LOCATORS
    // ==========================================
    private final By wishlistHeaderLink = By.className("ico-wishlist");
    private final By wishlistBadge = By.className("wishlist-qty");

    // ==========================================
    //          WISHLIST PAGE LOCATORS
    // ==========================================
    private final By wishlistPageContainer = By.className("wishlist-page");
    private final By emptyWishlistMessage = By.cssSelector(".wishlist-content");

    // Product Items - nopCommerce uses table rows with class "cart-item-row"
    private final By productRows = By.cssSelector(".wishlist-page .cart-item-row");
    private final By productName = By.cssSelector("td.product a");
    private final By productPrice = By.cssSelector(".product-unit-price");
    private final By productImage = By.cssSelector(".product-picture img");
    private final By qtyInput = By.cssSelector(".qty-input");

    // Remove checkbox
    private final By removeCheckbox = By.name("removefromcart");

    // Add to cart from wishlist
    private final By addToCartCheckbox = By.name("addtocart");
    private final By addToCartButton = By.name("addtocartbutton");

    // Action Buttons
    private final By updateWishlistButton = By.cssSelector("input.update-wishlist-button");
    private final By emailAFriendButton = By.cssSelector(".email-a-friend-button");

    // Share
    private final By shareWishlistLink = By.cssSelector(".share-link");

    public WishlistPage(WebDriver driver) {
        super(driver);
    }

    // ==========================================
    //         NAVIGATION METHODS
    // ==========================================

    public void navigateToWishlist() {
        click(wishlistHeaderLink);
        waitForVisibility(wishlistPageContainer);
    }

    // ==========================================
    //         WISHLIST MANAGEMENT
    // ==========================================

    public void clearWishlist() {
        navigateToWishlist();
        waitForVisibility(wishlistPageContainer);

        // Check if already empty
        List<WebElement> emptyMsgs = driver.findElements(emptyWishlistMessage);
        if (!emptyMsgs.isEmpty()) {
            String text = emptyMsgs.get(0).getText().toLowerCase();
            if (text.contains("empty") || text.contains("no items")) {
                return;
            }
        }

        // Wait for checkboxes to be present, then clear
        List<WebElement> checkboxes = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(removeCheckbox));

        if (checkboxes.isEmpty()) {
            return;
        }

        for (WebElement checkbox : checkboxes) {
            if (!checkbox.isSelected()) {
                checkbox.click();
            }
        }

        click(updateWishlistButton);

        // Verify cleared
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                emptyWishlistMessage, "The wishlist is empty!"));
    }

    // ==========================================
    //      REUSABLE PRODUCT ROW FINDER
    // ==========================================

    private WebElement findProductRow(String productNameText) {
        List<WebElement> rows = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(productRows));

        for (WebElement row : rows) {
            // 🎯 FIX: Use td.product a selector matching actual HTML
            String rowProductName = row.findElement(productName).getText().trim();
            if (rowProductName.equals(productNameText)) {
                return row;
            }
        }
        throw new NoSuchElementException("Product not found in wishlist: " + productNameText);
    }

    // ==========================================
    //         WISHLIST ACTIONS
    // ==========================================

    public void removeProduct(String productNameText) {
        WebElement row = findProductRow(productNameText);
        WebElement checkbox = row.findElement(removeCheckbox);
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
        click(updateWishlistButton);
    }

    public void updateQuantity(String productNameText, String quantity) {
        WebElement row = findProductRow(productNameText);
        WebElement input = row.findElement(qtyInput);
        input.clear();
        input.sendKeys(quantity);
        click(updateWishlistButton);
    }

    // ==========================================
    //      ADD TO CART FROM WISHLIST
    // ==========================================

    public void addProductToCart(String productNameText) {
        WebElement row = findProductRow(productNameText);
        WebElement checkbox = row.findElement(addToCartCheckbox);
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
        click(addToCartButton);
    }

    public void addAllToCart() {
        List<WebElement> checkboxes = driver.findElements(addToCartCheckbox);
        for (WebElement checkbox : checkboxes) {
            if (!checkbox.isSelected()) {
                checkbox.click();
            }
        }
        click(addToCartButton);
    }

    // ==========================================
    //            GETTER METHODS
    // ==========================================

    public String getEmptyWishlistMessage() {
        return readText(emptyWishlistMessage).trim();
    }

    public String getWishlistPageText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(wishlistPageContainer)).getText();
    }

    public String getProductName() {
        By[] selectors = {
                By.cssSelector(".wishlist-page td.product a"),
                By.cssSelector("td.product a"),
                By.cssSelector(".product-name"),
                By.cssSelector(".product a")
        };

        for (By selector : selectors) {
            try {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(selector)).getText().trim();
            } catch (TimeoutException e) {
                continue;
            }
        }
        throw new NoSuchElementException("Could not find product name in wishlist");
    }

    public String getProductPrice() {
        return readText(productPrice).trim();
    }

    public String getQuantityValue() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(qtyInput)).getAttribute("value");
    }

    public int getWishlistItemCount() {
        try {
            String badgeText = readText(wishlistBadge);
            String numericText = badgeText.replaceAll("[^0-9]", "");
            return numericText.isEmpty() ? 0 : Integer.parseInt(numericText);
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isProductImageDisplayed() {
        try {
            WebElement img = wait.until(ExpectedConditions.visibilityOfElementLocated(productImage));
            String src = img.getAttribute("src");
            return img.isDisplayed() && src != null && !src.trim().isEmpty();
        } catch (NoSuchElementException | TimeoutException e) {
            return false;
        }
    }

    public boolean isWishlistEmpty() {
        try {
            String text = wait.until(ExpectedConditions.visibilityOfElementLocated(emptyWishlistMessage)).getText();
            return text.toLowerCase().contains("empty") || text.toLowerCase().contains("no items");
        } catch (TimeoutException e) {
            return true;
        }
    }

    public boolean isEmailAFriendButtonDisplayed() {
        try {
            return driver.findElement(emailAFriendButton).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isShareLinkDisplayed() {
        try {
            return driver.findElement(shareWishlistLink).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public String getShareLinkUrl() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(shareWishlistLink)).getAttribute("href");
    }

    public void clickShareLink() {
        click(shareWishlistLink);
    }

    public void clickEmailAFriend() {
        click(emailAFriendButton);
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }
}