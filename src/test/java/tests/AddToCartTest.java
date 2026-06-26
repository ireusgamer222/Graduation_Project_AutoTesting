package tests;

import base.BaseTest;
import pages.HomePage;
import pages.ProductPage;
import pages.ShoppingCartPage;
import pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.Constants;

public class AddToCartTest extends BaseTest {

    // Page object instances used across all test methods
    private HomePage homePage;
    private ProductPage productPage;
    private ShoppingCartPage shoppingCartPage;
    private LoginPage loginPage;

    // This method runs before EVERY test. It initializes the page objects
    // with the current thread's WebDriver instance so they are ready to use.
    @BeforeMethod
    public void prepareIsolatedTestContext() {
        WebDriver driver = getDriverInstance();
        homePage = new HomePage(driver);
        productPage = new ProductPage(driver);
        shoppingCartPage = new ShoppingCartPage(driver);
        loginPage = new LoginPage(driver);
    }

    // ==========================================
    //                TEST CASES
    // ==========================================

    // Covers ATC_1: Add an in-stock product to the cart from the product details page
    @Test
    public void testAddInStockProductFromDetailsPage() {
        // Log in and ensure cart is empty
        loginAndClearCart();

        // Navigate to the laptop product page and get initial badge count
        productPage.navigateTo(Constants.URL + Constants.PRODUCT_PATH_LAPTOP);
        int initialBadgeCount = homePage.getCartBadgeCountAsInt();

        // Add to cart
        productPage.clickAddToCart();

        // Verify success notification appears
        String notification = productPage.getNotificationMessage();
        Assert.assertTrue(notification.toLowerCase().contains("has been added to your shopping cart"),
                "Success notification layout message mismatch.");

        // Verify the cart badge increments by exactly 1
        homePage.waitForCartBadgeCount("(" + (initialBadgeCount + 1) + ")");
        int updatedBadgeCount = homePage.getCartBadgeCountAsInt();
        Assert.assertEquals(updatedBadgeCount, initialBadgeCount + 1,
                "Cart badge count failed to increment mathematically.");
    }

    // Covers ATC_2: Verify 'Add to Cart' button is disabled or hidden for an out-of-stock product
    @Test
    public void testOutOfStockProductButtonHidden() {
        loginAndClearCart();

        // Navigate to a known out-of-stock product
        productPage.navigateTo(Constants.URL + Constants.PRODUCT_PATH_OUT_OF_STOCK);

        // Verify the button is not clickable
        boolean isClickable = productPage.isAddToCartButtonClickable();
        Assert.assertFalse(isClickable,
                "The 'Add to cart' button was active/clickable for an out-of-stock product.");
    }

    // Covers ATC_3: Add a product to the cart from the product listing page
    @Test
    public void testAddProductFromListingPage() {
        loginAndClearCart();

        // Navigate to a category listing page (notebooks)
        homePage.navigateTo(Constants.URL + "notebooks");
        int initialBadgeCount = homePage.getCartBadgeCountAsInt();

        // Click the add to cart button for the target product
        homePage.clickAddToCartFromListing(Constants.TARGET_PRODUCT);

        // Verify the cart badge increments by 1
        homePage.waitForCartBadgeCount("(" + (initialBadgeCount + 1) + ")");
        int updatedBadgeCount = homePage.getCartBadgeCountAsInt();

        Assert.assertEquals(updatedBadgeCount, initialBadgeCount + 1,
                "Listing page Add-To-Cart button failed to increment badge.");
    }

    // Covers ATC_4: Add a product with the default quantity of 1
    @Test
    public void testAddProductWithDefaultQuantity() {
        loginAndClearCart();

        // Go to product page and click add (leaving quantity at default 1)
        productPage.navigateTo(Constants.URL + Constants.PRODUCT_PATH_LAPTOP);
        productPage.clickAddToCart();

        // Open the cart and verify the quantity is actually 1
        homePage.clickShoppingCart();
        Assert.assertEquals(shoppingCartPage.getQuantityValue(), "1",
                "Default quantity should be 1.");
    }

    // Covers ATC_5: Add a product with a valid quantity greater than 1
    @Test
    public void testAddMultipleQuantitiesSimultaneously() {
        loginAndClearCart();

        productPage.navigateTo(Constants.URL + Constants.PRODUCT_PATH_LAPTOP);
        int initialBadgeCount = homePage.getCartBadgeCountAsInt();

        // Change quantity to 3 and add to cart
        productPage.updateQuantity("3");
        productPage.clickAddToCart();

        // Verify the cart badge correctly reflects the total quantity added (initial + 3)
        homePage.waitForCartBadgeCount("(" + (initialBadgeCount + 3) + ")");
        int updatedBadgeCount = homePage.getCartBadgeCountAsInt();

        Assert.assertEquals(updatedBadgeCount, initialBadgeCount + 3,
                "Failed to add a custom quantity of items to the cart simultaneously.");
    }

    // Covers ATC_6: Add a product with a quantity of zero
    @Test
    public void testAddProductWithZeroQuantity() {
        loginAndClearCart();

        productPage.navigateTo(Constants.URL + Constants.PRODUCT_PATH_LAPTOP);
        productPage.updateQuantity("0");
        productPage.clickAddToCart();

        // Verify the system displays a validation error
        String errorMsg = productPage.getNotificationMessage();
        Assert.assertTrue(errorMsg.toLowerCase().contains("quantity") || errorMsg.toLowerCase().contains("positive"),
                "Expected validation error for zero quantity. Got: " + errorMsg);
    }

    // Covers ATC_7: Add a product with a negative quantity
    @Test
    public void testAddProductWithNegativeQuantity() {
        loginAndClearCart();

        productPage.navigateTo(Constants.URL + Constants.PRODUCT_PATH_LAPTOP);
        productPage.updateQuantity("-5");
        productPage.clickAddToCart();

        // Verify the system displays a validation error
        String errorMsg = productPage.getNotificationMessage();
        Assert.assertTrue(errorMsg.toLowerCase().contains("quantity") || errorMsg.toLowerCase().contains("positive"),
                "Expected validation error for negative quantity. Got: " + errorMsg);
    }

    // Covers ATC_8: Add a product with a very large quantity
    @Test
    public void testAddProductWithMaxQuantity() {
        loginAndClearCart();

        productPage.navigateTo(Constants.URL + Constants.PRODUCT_PATH_LAPTOP);
        productPage.updateQuantity(Constants.QTY_MAX_LIMIT);
        productPage.clickAddToCart();

        // Verify the system handles the input and displays a max limit message
        String errorMsg = productPage.getNotificationMessage();
        Assert.assertTrue(errorMsg.toLowerCase().contains("maximum") || errorMsg.contains("10000"),
                "Expected max quantity validation. Got: " + errorMsg);
    }

    // Covers ATC_9: Add a product with a non-numeric quantity
    @Test
    public void testAddProductWithNonNumericQuantity() {
        loginAndClearCart();

        productPage.navigateTo(Constants.URL + Constants.PRODUCT_PATH_LAPTOP);
        productPage.updateQuantity("abc");
        productPage.clickAddToCart();

        // Verify the system displays a validation error
        String errorMsg = productPage.getNotificationMessage();
        Assert.assertTrue(errorMsg.toLowerCase().contains("quantity") || errorMsg.toLowerCase().contains("positive"),
                "Expected validation error for non-numeric quantity. Got: " + errorMsg);
    }

    // Covers ATC_10: Add a product with a decimal quantity
    @Test
    public void testAddProductWithDecimalQuantity() {
        loginAndClearCart();

        productPage.navigateTo(Constants.URL + Constants.PRODUCT_PATH_LAPTOP);
        productPage.updateQuantity("2.5");
        productPage.clickAddToCart();

        // Verify the system displays a validation error
        String errorMsg = productPage.getNotificationMessage();
        Assert.assertTrue(errorMsg.toLowerCase().contains("quantity") || errorMsg.toLowerCase().contains("positive"),
                "Expected validation error for decimal quantity. Got: " + errorMsg);
    }

    // Covers ATC_11: Add a product with the quantity field left empty
    @Test
    public void testAddProductWithEmptyQuantity() {
        loginAndClearCart();

        productPage.navigateTo(Constants.URL + Constants.PRODUCT_PATH_LAPTOP);
        productPage.updateQuantity("");
        productPage.clickAddToCart();

        // Verify an error message is triggered (not empty)
        String errorMsg = productPage.getNotificationMessage();
        Assert.assertFalse(errorMsg.isEmpty(),
                "Expected validation error for empty quantity.");
    }

    // Covers ATC_19 & ATC_20: Verify the cart badge count increments after adding a product & reflects total quantity
    @Test
    public void testAddDifferentProductsStacksInBadge() {
        loginAndClearCart();
        int initialBadgeCount = homePage.getCartBadgeCountAsInt();

        // Add first product and wait for badge to update
        productPage.navigateTo(Constants.URL + Constants.PRODUCT_PATH_LAPTOP);
        productPage.clickAddToCart();
        homePage.waitForCartBadgeCount("(" + (initialBadgeCount + 1) + ")");

        // Add second distinct product and wait for badge to update
        productPage.navigateTo(Constants.URL + Constants.PRODUCT_PATH_SECONDARY);
        productPage.clickAddToCart();
        homePage.waitForCartBadgeCount("(" + (initialBadgeCount + 2) + ")");

        // Verify final badge count reflects total items added (initial + 2)
        int finalBadgeCount = homePage.getCartBadgeCountAsInt();
        Assert.assertEquals(finalBadgeCount, initialBadgeCount + 2,
                "Adding distinctly different products failed to scale the badge count correctly.");
    }

    // Covers ATC_21: Verify a success notification is displayed after adding a product
    @Test
    public void testSuccessNotificationLayoutAndDismissal() {
        loginAndClearCart();

        productPage.navigateTo(Constants.URL + Constants.PRODUCT_PATH_LAPTOP);
        productPage.clickAddToCart();

        // Verify notification has text and contains success phrase
        String notificationText = productPage.getNotificationMessage();
        Assert.assertFalse(notificationText.isEmpty(),
                "Notification banner rendered without text.");
        Assert.assertTrue(notificationText.toLowerCase().contains("has been added"),
                "Notification missing success text. Got: " + notificationText);

        // Dismiss the banner and verify it disappears
        productPage.dismissNotificationBanner();
        Assert.assertFalse(productPage.isNotificationBannerDisplayed(),
                "Notification banner failed to disappear after dismissal.");
    }

    // Covers ATC_23: Add a product to the cart as a guest user
    @Test
    public void testGuestUserAddToCart() {
        // Navigate directly to product page WITHOUT logging in
        productPage.navigateTo(Constants.URL + Constants.PRODUCT_PATH_SECONDARY);
        int initialBadgeCount = homePage.getCartBadgeCountAsInt();

        // Add to cart
        productPage.clickAddToCart();

        // Verify the guest session successfully saved the item (badge incremented)
        homePage.waitForCartBadgeCount("(" + (initialBadgeCount + 1) + ")");
        int updatedBadgeCount = homePage.getCartBadgeCountAsInt();

        Assert.assertEquals(updatedBadgeCount, initialBadgeCount + 1,
                "Guest session failed to save added product to anonymous cart.");
    }

    // ==========================================
    //          REUSABLE POM HELPER METHODS
    // ==========================================

    /**
     * Logs into the application, navigates to the cart, clears any existing items,
     * and returns the user to the homepage.
     * Used as a setup step to ensure a clean state before testing.
     */
    private HomePage loginAndClearCart() {
        shoppingCartPage.navigateTo(Constants.URL + "login");
        loginPage.login(Constants.EMAIL, Constants.PASSWORD);
        shoppingCartPage.navigateTo(Constants.URL + "cart");
        shoppingCartPage.clearCartIfNotEmpty();
        homePage.navigateTo(Constants.URL);
        return homePage;
    }
}