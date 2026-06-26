package tests;

import base.BaseTest;
import pages.HomePage;
import pages.ProductPage;
import pages.WishlistPage;
import pages.ShoppingCartPage;
import pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.Constants;

public class WishlistTest extends BaseTest {

    // Page object instances used across all test methods
    private HomePage homePage;
    private ProductPage productPage;
    private WishlistPage wishlistPage;
    private ShoppingCartPage shoppingCartPage;
    private LoginPage loginPage;

    // This method runs before EVERY test. It initializes the page objects
    // with the current thread's WebDriver instance so they are ready to use.
    @BeforeMethod
    public void prepareIsolatedTestContext() {
        WebDriver driver = getDriverInstance();
        homePage = new HomePage(driver);
        productPage = new ProductPage(driver);
        wishlistPage = new WishlistPage(driver);
        shoppingCartPage = new ShoppingCartPage(driver);
        loginPage = new LoginPage(driver);
    }

    // ==========================================
    //                TEST CASES
    // ==========================================

    // Covers WL_1: Add an in-stock product to the wishlist from the product details page
    @Test
    public void testAddInStockProductToWishlist() {
        // Log in and clear wishlist to ensure clean state
        loginAndClearWishlist();

        // Navigate to an in-stock product and verify the button is present
        productPage.navigateTo(Constants.URL + Constants.WISHLIST_PRODUCT_URL_PRIMARY);
        Assert.assertTrue(productPage.isAddToWishlistButtonPresent(),
                "Add to Wishlist button not present on in-stock product.");

        // Add to wishlist
        productPage.clickAddToWishlist();

        // Verify success notification appears
        String notification = productPage.getNotificationMessage();
        Assert.assertTrue(notification.toLowerCase().contains("has been added to your wishlist"),
                "Success notification mismatch. Got: " + notification);

        // Verify product is actually displayed in the wishlist
        wishlistPage.navigateToWishlist();
        Assert.assertEquals(wishlistPage.getProductName(), Constants.WISHLIST_PRODUCT_PRIMARY,
                "Product not found in wishlist.");
    }

    // Covers WL_2: Add an out-of-stock product to the wishlist
    @Test
    public void testOutOfStockProductButtonHidden() {
        loginAndClearWishlist();

        // Navigate to a known out-of-stock product
        productPage.navigateTo(Constants.URL + Constants.PRODUCT_PATH_OUT_OF_STOCK);

        // Verify the 'Add to Wishlist' button is NOT present
        boolean isPresent = productPage.isAddToWishlistButtonPresent();
        Assert.assertFalse(isPresent,
                "Add to Wishlist button should not be present on out-of-stock product.");
    }

    // Covers WL_4: View the wishlist page through the navigation header link
    @Test
    public void testViewWishlistViaHeader() {
        loginAndClearWishlist();

        // Click wishlist link in the header
        wishlistPage.navigateToWishlist();

        // Verify the wishlist page loads correctly
        String pageText = wishlistPage.getWishlistPageText().toLowerCase();
        Assert.assertTrue(pageText.contains("wishlist"),
                "Wishlist page did not open correctly.");
    }

    // Covers WL_5: View an empty wishlist
    @Test
    public void testViewEmptyWishlist() {
        loginAndClearWishlist();

        // Navigate to wishlist
        wishlistPage.navigateToWishlist();

        // Verify the empty wishlist message is displayed
        String emptyMsg = wishlistPage.getEmptyWishlistMessage().toLowerCase();
        Assert.assertTrue(emptyMsg.contains("empty") || emptyMsg.contains("no items"),
                "Empty wishlist message not displayed.");
    }

    // Covers WL_6: Verify product details are correctly displayed in the wishlist
    @Test
    public void testDisplayProductDetailsInWishlist() {
        // Log in, clear wishlist, add product, and open wishlist
        WishlistPage activeWishlistPage = loginAddProductAndOpenWishlist(Constants.WISHLIST_PRODUCT_URL_PRIMARY);

        // Verify name, price, and image are all correct/present using SoftAssert
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(activeWishlistPage.getProductName(), Constants.WISHLIST_PRODUCT_PRIMARY,
                "Product name mismatch.");
        softAssert.assertFalse(activeWishlistPage.getProductPrice().isEmpty(),
                "Product price is empty.");
        softAssert.assertTrue(activeWishlistPage.isProductImageDisplayed(),
                "Product image not displayed.");
        softAssert.assertAll();
    }

    // Covers WL_7: Add multiple different products to the wishlist
    @Test
    public void testAddMultipleProductsToWishlist() {
        loginAndClearWishlist();

        // Add first product
        addProductToWishlist(Constants.WISHLIST_PRODUCT_URL_PRIMARY);
        // Add second product
        addProductToWishlist(Constants.WISHLIST_PRODUCT_URL_SECONDARY);

        // Open wishlist and verify both products are present in the text
        wishlistPage.navigateToWishlist();
        String wishlistText = wishlistPage.getWishlistPageText();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(wishlistText.contains(Constants.WISHLIST_PRODUCT_PRIMARY),
                "First product missing from wishlist.");
        softAssert.assertTrue(wishlistText.contains(Constants.WISHLIST_PRODUCT_SECONDARY),
                "Second product missing from wishlist.");
        softAssert.assertAll();
    }

    // Covers WL_8: Add the same product to the wishlist a second time
    @Test
    public void testIncrementQuantityOnDuplicateAdd() {
        loginAndClearWishlist();

        // Add the exact same product twice
        addProductToWishlist(Constants.WISHLIST_PRODUCT_URL_PRIMARY);
        addProductToWishlist(Constants.WISHLIST_PRODUCT_URL_PRIMARY);

        // Verify the quantity increments to 2 instead of creating a duplicate line item
        wishlistPage.navigateToWishlist();
        String qty = wishlistPage.getQuantityValue();
        Assert.assertEquals(qty, "2",
                "Quantity should increment to 2 on duplicate add. Got: " + qty);
    }

    // Covers WL_9: Remove a product from the wishlist
    @Test
    public void testRemoveProductFromWishlist() {
        // Setup: add a product to wishlist
        WishlistPage activeWishlistPage = loginAddProductAndOpenWishlist(Constants.WISHLIST_PRODUCT_URL_PRIMARY);
        Assert.assertFalse(activeWishlistPage.isWishlistEmpty(),
                "Setup Failed: Product not added to wishlist.");

        // Remove the product
        wishlistPage.removeProduct(Constants.WISHLIST_PRODUCT_PRIMARY);

        // Verify the wishlist is now empty
        Assert.assertTrue(wishlistPage.isWishlistEmpty(),
                "Product was not removed from wishlist.");
    }

    // Covers WL_10: Add a product from the wishlist to the shopping cart
    @Test
    public void testAddFromWishlistToCart() {
        loginAndClearWishlist();
        // Also clear cart to ensure it's empty before moving item over
        shoppingCartPage.navigateTo(Constants.URL + "cart");
        shoppingCartPage.clearCartIfNotEmpty();

        // Add product to wishlist and navigate there
        addProductToWishlist(Constants.WISHLIST_PRODUCT_URL_PRIMARY);
        wishlistPage.navigateToWishlist();

        // Move product from wishlist to cart
        wishlistPage.addProductToCart(Constants.WISHLIST_PRODUCT_PRIMARY);
        shoppingCartPage.navigateTo(Constants.URL + "cart");

        // Verify the product successfully landed in the shopping cart
        Assert.assertEquals(shoppingCartPage.getProductName(), Constants.WISHLIST_PRODUCT_PRIMARY,
                "Product not added to cart from wishlist.");
    }

    // Covers WL_11: Add all wishlist products to the shopping cart at once
    @Test
    public void testAddAllWishlistToCart() {
        loginAndClearWishlist();
        shoppingCartPage.navigateTo(Constants.URL + "cart");
        shoppingCartPage.clearCartIfNotEmpty();

        // Add two different products to wishlist
        addProductToWishlist(Constants.WISHLIST_PRODUCT_URL_PRIMARY);
        addProductToWishlist(Constants.WISHLIST_PRODUCT_URL_SECONDARY);
        wishlistPage.navigateToWishlist();

        // Move all items to cart
        wishlistPage.addAllToCart();
        shoppingCartPage.navigateTo(Constants.URL + "cart");

        // Verify both items successfully landed in the shopping cart
        String cartContent = shoppingCartPage.getCartPageText();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(cartContent.contains(Constants.WISHLIST_PRODUCT_PRIMARY),
                "First product not in cart.");
        softAssert.assertTrue(cartContent.contains(Constants.WISHLIST_PRODUCT_SECONDARY),
                "Second product not in cart.");
        softAssert.assertAll();
    }

    // Covers WL_12: Verify wishlist item count badge updates in the header after adding a product
    @Test
    public void testIncrementWishlistBadge() {
        loginAndClearWishlist();

        // Get initial badge count
        int initialCount = wishlistPage.getWishlistItemCount();

        // Add product to wishlist
        addProductToWishlist(Constants.WISHLIST_PRODUCT_URL_PRIMARY);

        // Go home to check the header badge
        homePage.navigateTo(Constants.URL);
        int updatedCount = wishlistPage.getWishlistItemCount();

        // Verify badge incremented by 1
        Assert.assertEquals(updatedCount, initialCount + 1,
                "Wishlist badge did not increment.");
    }

    // Covers WL_13: Verify wishlist item count badge updates in the header after removing a product
    @Test
    public void testDecrementWishlistBadge() {
        loginAndClearWishlist();

        // Add product so we have 1 item
        addProductToWishlist(Constants.WISHLIST_PRODUCT_URL_PRIMARY);
        homePage.navigateTo(Constants.URL);
        int initialCount = wishlistPage.getWishlistItemCount();
        Assert.assertEquals(initialCount, 1, "Setup Failed: Wishlist should have 1 item.");

        // Remove the product
        wishlistPage.navigateToWishlist();
        wishlistPage.removeProduct(Constants.WISHLIST_PRODUCT_PRIMARY);

        // Go home to check the header badge
        homePage.navigateTo(Constants.URL);
        int updatedCount = wishlistPage.getWishlistItemCount();

        // Verify badge decremented to 0
        Assert.assertEquals(updatedCount, 0,
                "Wishlist badge did not decrement to 0.");
    }

    // Covers WL_14: Wishlist persists after a page refresh
    @Test
    public void testPersistWishlistAfterRefresh() {
        WishlistPage activeWishlistPage = loginAddProductAndOpenWishlist(Constants.WISHLIST_PRODUCT_URL_PRIMARY);
        Assert.assertEquals(activeWishlistPage.getProductName(), Constants.WISHLIST_PRODUCT_PRIMARY,
                "Setup Failed: Product not in wishlist.");

        // Refresh the browser page
        activeWishlistPage.refreshPage();

        // Verify the product is still in the wishlist
        Assert.assertEquals(activeWishlistPage.getProductName(), Constants.WISHLIST_PRODUCT_PRIMARY,
                "Wishlist product lost after refresh.");
    }

    // Covers WL_15: Wishlist persists after logout and re-login
    @Test
    public void testPersistWishlistAfterRelogin() {
        WishlistPage activeWishlistPage = loginAddProductAndOpenWishlist(Constants.WISHLIST_PRODUCT_URL_PRIMARY);
        Assert.assertEquals(activeWishlistPage.getProductName(), Constants.WISHLIST_PRODUCT_PRIMARY,
                "Setup Failed: Product not in wishlist.");

        // Log out of the account
        homePage.logoutSession();

        // Log back in
        homePage.clickLoginLink();
        loginPage.login(Constants.EMAIL, Constants.PASSWORD);
        homePage.waitForVisibilityOfLogout();

        // Verify the product is still in the wishlist
        wishlistPage.navigateToWishlist();
        Assert.assertEquals(wishlistPage.getProductName(), Constants.WISHLIST_PRODUCT_PRIMARY,
                "Wishlist product lost after re-login.");
    }

    // Covers WL_20: Increase the quantity of a product in the wishlist
    @Test
    public void testIncreaseQuantityInWishlist() {
        loginAddProductAndOpenWishlist(Constants.WISHLIST_PRODUCT_URL_PRIMARY);

        // Increase quantity to 3
        wishlistPage.updateQuantity(Constants.WISHLIST_PRODUCT_PRIMARY, "3");

        // Verify the quantity field reflects 3
        Assert.assertEquals(wishlistPage.getQuantityValue(), "3",
                "Quantity not updated to 3.");
    }

    // Covers WL_21: Decrease the quantity of a product in the wishlist
    @Test
    public void testDecreaseQuantityInWishlist() {
        loginAddProductAndOpenWishlist(Constants.WISHLIST_PRODUCT_URL_PRIMARY);

        // First, increase to 3
        wishlistPage.updateQuantity(Constants.WISHLIST_PRODUCT_PRIMARY, "3");

        // Then, decrease back to 1
        wishlistPage.updateQuantity(Constants.WISHLIST_PRODUCT_PRIMARY, "1");

        // Verify the quantity field reflects 1
        Assert.assertEquals(wishlistPage.getQuantityValue(), "1",
                "Quantity not decreased to 1.");
    }

    // Covers WL_22: Enter a very large number for a product quantity in the wishlist
    @Test
    public void testDocumentMaxQuantityBehavior() {
        loginAddProductAndOpenWishlist(Constants.WISHLIST_PRODUCT_URL_SECONDARY);

        // Enter a very large number (1000)
        wishlistPage.updateQuantity(Constants.WISHLIST_PRODUCT_SECONDARY, "1000");

        String qty = wishlistPage.getQuantityValue();

        // DOCUMENTATION:
        // Excel Expected: System should reject or cap at max limit
        // Excel Actual: System accepted 1000 (BUG - no validation)
        // This test documents the CURRENT behavior for regression tracking
        Assert.assertEquals(qty, "1000",
                "Documenting current behavior - system accepts 1000. " +
                        "This is a known issue per Excel test case.");
    }

    // Covers WL_23: Enter a negative number for a product quantity in the wishlist
    @Test
    public void testRemoveOnNegativeQuantity() {
        loginAddProductAndOpenWishlist(Constants.WISHLIST_PRODUCT_URL_PRIMARY);

        // Enter a negative number (-5)
        wishlistPage.updateQuantity(Constants.WISHLIST_PRODUCT_PRIMARY, "-5");

        // DOCUMENTATION: System removes the item instead of displaying an error.
        // Test asserts that the wishlist is now empty to match actual app behavior.
        Assert.assertTrue(wishlistPage.isWishlistEmpty(),
                "Product should be removed on negative quantity.");
    }

    // Covers WL_24: Enter a non-numeric value for a product quantity in the wishlist
    @Test
    public void testRevertOnNonNumericQuantity() {
        loginAddProductAndOpenWishlist(Constants.WISHLIST_PRODUCT_URL_PRIMARY);

        // Capture original quantity (should be 1)
        String originalQty = wishlistPage.getQuantityValue();

        // Enter non-numeric text ("abc")
        wishlistPage.updateQuantity(Constants.WISHLIST_PRODUCT_PRIMARY, "abc");

        // Verify the system reverted to the original valid quantity without saving the text
        String finalQty = wishlistPage.getQuantityValue();
        Assert.assertEquals(finalQty, originalQty,
                "System should revert to previous value on non-numeric input.");
    }

    // ==========================================
    //          REUSABLE POM HELPER METHODS
    // ==========================================

    /**
     * Logs into the application and removes all items from the wishlist.
     * Used as a setup step to ensure a clean state before testing.
     */
    private WishlistPage loginAndClearWishlist() {
        shoppingCartPage.navigateTo(Constants.URL + "login");
        loginPage.login(Constants.EMAIL, Constants.PASSWORD);
        wishlistPage.clearWishlist();
        return wishlistPage;
    }

    /**
     * Navigates to a product page, clicks the 'Add to Wishlist' button,
     * and handles dismissing the success notification banner.
     */
    private ProductPage addProductToWishlist(String productUrlSlug) {
        productPage.navigateTo(Constants.URL + productUrlSlug);
        if (productPage.isAddToWishlistButtonPresent()) {
            productPage.clickAddToWishlist();
            if (productPage.isNotificationBannerDisplayed()) {
                productPage.dismissNotificationBanner();
            }
        } else {
            throw new RuntimeException("Add to Wishlist button not present for: " + productUrlSlug);
        }
        return productPage;
    }

    /**
     * Combines login, clearing wishlist, adding a specific product,
     * and navigating to the wishlist page into one single setup step.
     */
    private WishlistPage loginAddProductAndOpenWishlist(String productUrlSlug) {
        loginAndClearWishlist();
        addProductToWishlist(productUrlSlug);
        wishlistPage.navigateToWishlist();
        return wishlistPage;
    }
}