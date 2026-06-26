package tests;

import base.BaseTest;
import pages.HomePage;
import pages.ShoppingCartPage;
import pages.ProductPage;
import pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.Constants;

public class ShoppingCartTest extends BaseTest {

    // Page object instances used across all test methods
    private ShoppingCartPage shoppingCartPage;
    private ProductPage productPage;
    private LoginPage loginPage;
    private HomePage homePage;

    // This method runs before EVERY test. It initializes the page objects
    // with the current thread's WebDriver instance so they are ready to use.
    @BeforeMethod
    public void prepareIsolatedTestContext() {
        WebDriver driver = getDriverInstance();

        // Initialize Page Objects
        shoppingCartPage = new ShoppingCartPage(driver);
        productPage = new ProductPage(driver);
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);

        // No navigation or cart clearing here!
        // Let the individual test methods dictate the starting navigation.
    }

    // ==========================================
    //                TEST CASES
    // ==========================================

    // Covers SC_1: View empty shopping cart
    @Test
    public void testViewEmptyShoppingCart() {
        // Log in and open an empty cart
        loginAndOpenCart();

        // Verify the empty cart message is displayed
        String emptyMsg = shoppingCartPage.getEmptyCartMessage().toLowerCase();
        Assert.assertTrue(emptyMsg.contains("empty") || emptyMsg.contains("no items"), "Unexpected empty cart layout message.");
    }

    // Covers SC_2: View shopping cart with one product
    @Test
    public void testAddedProductIsDisplayedInCart() {
        // Log in, add a product, and open the cart
        ShoppingCartPage cartPage = loginAddProductAndOpenCart();

        // Verify the correct product name is displayed in the cart
        Assert.assertEquals(cartPage.getProductName(), Constants.TARGET_PRODUCT, "Product name mismatch.");
    }

    // Covers SC_3: Increase product quantity in the shopping cart
    @Test
    public void testIncreaseProductQuantity() {
        ShoppingCartPage cartPage = loginAddProductAndOpenCart();

        // Update quantity to 2 and verify the input field reflects the change
        cartPage.updateQuantity(Constants.QTY_TWO);
        Assert.assertEquals(cartPage.getQuantityValue(), Constants.QTY_TWO, "Quantity failed to scale.");
    }

    // Covers SC_4: Decrease product quantity in the shopping cart
    @Test
    public void testDecreaseProductQuantity() {
        ShoppingCartPage cartPage = loginAddProductAndOpenCart();

        // Start by increasing to 2
        cartPage.updateQuantity(Constants.QTY_TWO);

        // Capture subtotal before decreasing
        double initialSubtotal = parseCurrencyValue(cartPage.getSubtotalText());

        // Decrease the quantity by 1
        cartPage.decreaseQuantity();

        // Wait for the page to update the subtotal price
        cartPage.waitForSubtotalToChange(String.valueOf(initialSubtotal));

        // Capture new subtotal and verify it mathematically decreased
        double updatedSubtotal = parseCurrencyValue(cartPage.getSubtotalText());
        Assert.assertTrue(updatedSubtotal < initialSubtotal, "Subtotal did not decrease mathematically after lowering quantity.");
    }

    // Covers SC_5: Enter a zero quantity for a product in the shopping cart
    @Test
    public void testZeroQuantityRemovesProduct() {
        ShoppingCartPage cartPage = loginAddProductAndOpenCart();

        // Enter "0" in the quantity field
        cartPage.updateQuantity("0");

        // DEFENSIVE STEP: Check if the system auto-removes the item
        // without needing the click (common in modern SPAs)
        if (!cartPage.isCartEmpty()) {
            cartPage.clickUpdateShoppingCart();
        }

        // Final Assertion: Item should be gone
        Assert.assertTrue(cartPage.isCartEmpty(), "Product was not removed on zero quantity.");
    }

    // Covers SC_10 & SC_28: Shopping cart items badge updates after adding a product in the header
    @Test
    public void testCartBadgeIncrementsAfterAddingProduct() {
        // Log in directly from home page
        homePage.login(Constants.EMAIL, Constants.PASSWORD);
        shoppingCartPage.navigateTo(Constants.URL);

        // Add laptop and wait for the badge to show (1)
        homePage.addLaptopToCart();
        homePage.waitForCartBadgeCount("(1)");

        // Verify the badge text is exactly "(1)"
        Assert.assertEquals(homePage.getCartBadgeCount(), "(1)", "Badge count did not increment.");
    }

    // Covers SC_12: Apply an invalid coupon code
    @Test
    public void testCouponValidationError() {
        ShoppingCartPage cartPage = loginAddProductAndOpenCart();

        // Apply a fake coupon code
        cartPage.applyCoupon("INVALID_COUPON_CODE");

        // Verify the expected error message is displayed
        Assert.assertTrue(cartPage.getCouponErrorMessage().contains("The coupon code you entered couldn't be applied to your order"), "Coupon error missing.");
    }

    // Covers SC_29: Verify product name, price, and details are correctly displayed in the cart
    @Test
    public void testFullProductDetailsDisplay() {
        ShoppingCartPage cartPage = loginAddProductAndOpenCart();

        // Verify name, price, and subtotal fields are populated and correct
        Assert.assertEquals(cartPage.getProductName(), Constants.TARGET_PRODUCT, "Product name mismatch.");
        Assert.assertFalse(cartPage.getProductPrice().isEmpty(), "Unit price is empty.");
        Assert.assertFalse(cartPage.getCartSubtotal().isEmpty(), "Subtotal row is empty.");
    }

    // Covers SC_30: Verify product image is displayed and not broken in the cart
    @Test
    public void testProductImageValidation() {
        ShoppingCartPage cartPage = loginAddProductAndOpenCart();

        // Verify the image is visible and has a valid source URL
        Assert.assertTrue(cartPage.isProductImageDisplayed(), "Product thumbnail image is missing.");
    }

    // Covers SC_6: Enter a very large number for the product quantity
    @Test
    public void testMaxQuantityLimitValidation() {
        ShoppingCartPage cartPage = loginAddProductAndOpenCart();

        // Enter max limit quantity
        cartPage.updateQuantity(Constants.QTY_MAX_LIMIT);

        // Verify the system displays a restriction error message
        String validationMessage = cartPage.getNotificationMessage();
        Assert.assertTrue(validationMessage.contains("The maximum quantity allowed for purchase is 10000"), "Ceiling restriction error missing.");
    }

    // Covers SC_8: Shopping cart persists after a page refresh
    @Test
    public void testCartPersistenceAfterRefresh() {
        ShoppingCartPage cartPage = loginAddProductAndOpenCart();
        Assert.assertEquals(cartPage.getProductName(), Constants.TARGET_PRODUCT, "Initial item state missing.");

        // Refresh the browser and reinitialize the page object
        cartPage.refreshPage();
        cartPage = new ShoppingCartPage(getDriverInstance());

        // Verify the product is still in the cart
        Assert.assertEquals(cartPage.getProductName(), Constants.TARGET_PRODUCT, "Cart items dropped after page reload.");
    }

    // Covers SC_9: Shopping cart persists after a log out and re-login
    @Test
    public void testCartPersistenceAfterRelogin() {
        ShoppingCartPage cartPage = loginAddProductAndOpenCart();
        Assert.assertEquals(cartPage.getProductName(), Constants.TARGET_PRODUCT, "Initial item state missing.");

        // Log out, then log back in
        cartPage.logoutSession();
        homePage.login(Constants.EMAIL, Constants.PASSWORD);
        homePage.clickShoppingCart();

        // Reinitialize cart page and verify the item persisted
        cartPage = new ShoppingCartPage(getDriverInstance());
        Assert.assertEquals(cartPage.getProductName(), Constants.TARGET_PRODUCT, "Cart items dropped post-reauthentication.");
    }

    // Covers SC_16: Enter an invalid gift card code
    @Test
    public void testGiftCardValidationError() {
        ShoppingCartPage cartPage = loginAddProductAndOpenCart();

        // Apply fake gift card code
        cartPage.applyGiftCardCode("BAD_GIFT_CARD_123");

        // Verify the expected error message is displayed
        String validationMessage = cartPage.getGiftCardValidationMessage();
        Assert.assertTrue(validationMessage.toLowerCase().contains("failed") || validationMessage.toLowerCase().contains("couldn't be applied"), "Gift card error missing.");
    }

    // Covers SC_7: Enter a negative number for the product quantity
    @Test
    public void testNegativeQuantityValidation() {
        ShoppingCartPage cartPage = loginAddProductAndOpenCart();

        // Enter negative quantity and click update
        cartPage.updateQuantity("-23");
        cartPage.clickUpdateShoppingCart();

        // Capture any error message
        String errorMsg = cartPage.getNotificationMessage();

        // ADJUSTED ASSERTION: Look for the error message as per your test case (SC_7)
        // Check that the error is present
        Assert.assertTrue(errorMsg.contains("invalid") || errorMsg.contains("error"),
                "Expected an error message for negative quantity, but none appeared.");

        // Optionally: Ensure the item was NOT deleted (if that is the requirement)
        Assert.assertFalse(cartPage.isCartEmpty(), "Product should not be removed when invalid quantity is entered.");
    }

    // Covers SC_19: Add multiple different products to the shopping cart
    @Test
    public void testMultipleDifferentProductsDisplay() {
        // Clean cart
        loginAndOpenCart();

        // Add Product 1
        shoppingCartPage.navigateTo(Constants.URL);
        homePage.addProductToCart(Constants.TARGET_PRODUCT);

        // Add Product 2
        shoppingCartPage.navigateTo(Constants.URL);
        homePage.addProductToCart(Constants.SECONDARY_PRODUCT);

        // Go to cart
        homePage.clickShoppingCart();

        // Verify both products are present in the cart text using SoftAssert
        String cartContent = shoppingCartPage.getCartPageText();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(cartContent.contains(Constants.TARGET_PRODUCT), "Primary item layout element missing.");
        softAssert.assertTrue(cartContent.contains(Constants.SECONDARY_PRODUCT), "Secondary item layout element missing.");
        softAssert.assertAll();
    }

    // Covers SC_20: Add the same product a second time to the shopping cart
    @Test
    public void testAddSameProductTwiceIncrementsQuantity() {
        loginAndOpenCart();

        // Add the same product twice
        shoppingCartPage.navigateTo(Constants.URL);
        homePage.addProductToCart(Constants.TARGET_PRODUCT);

        shoppingCartPage.navigateTo(Constants.URL);
        homePage.addProductToCart(Constants.TARGET_PRODUCT);

        // Go to cart
        homePage.clickShoppingCart();

        // Verify quantity stacked to 2 (Note: Manual test SC_20 found a bug where it creates 2 lines instead of stacking)
        Assert.assertEquals(shoppingCartPage.getQuantityValue(), Constants.QTY_TWO, "Identical items failed to stack quantities.");
    }

    // Covers SC_21: View shopping cart through a guest user
    @Test
    public void testGuestUserCartView() {
        // Navigate to home as guest, open cart
        shoppingCartPage.navigateTo(Constants.URL);
        homePage.clickShoppingCart();

        // Verify empty message
        String emptyMsg = shoppingCartPage.getEmptyCartMessage().toLowerCase();
        Assert.assertTrue(emptyMsg.contains("empty") || emptyMsg.contains("no items"), "Guest layout validation mismatch.");
    }

    // Covers SC_22: Add a product to the shopping cart through a guest user
    @Test
    public void testGuestAddProductDisplaysInCart() {
        // Add product as guest
        shoppingCartPage.navigateTo(Constants.URL);
        homePage.addProductToCart(Constants.SECONDARY_PRODUCT);

        // Open cart and verify it is there
        homePage.clickShoppingCart();
        Assert.assertEquals(shoppingCartPage.getProductName(), Constants.SECONDARY_PRODUCT, "Guest item addition failed to save.");
    }

    // Covers SC_23: Verify cart merge after login
    @Test
    public void testCartMergeAfterLogin() {
        // Add product as guest
        shoppingCartPage.navigateTo(Constants.URL);
        homePage.addProductToCart(Constants.SECONDARY_PRODUCT);

        // Log in
        homePage.login(Constants.EMAIL, Constants.PASSWORD);

        // Wait for the post-login page refresh to settle before grabbing the top nav link
        homePage.waitForVisibilityOfLogout();
        homePage.clickShoppingCart();

        // Verify the guest item successfully merged into the user's logged-in cart
        String cartContent = shoppingCartPage.getCartPageText();
        Assert.assertTrue(cartContent.contains(Constants.SECONDARY_PRODUCT), "Guest items failed to merge with user account profile.");
    }

    // Covers SC_24: Proceed to checkout through the shopping cart
    @Test
    public void testProceedToCheckoutRedirect() {
        ShoppingCartPage cartPage = loginAddProductAndOpenCart();

        // Accept terms and click checkout
        cartPage.acceptTermsAndCheckout();

        // Verify URL changes to checkout/login/gateway
        String url = cartPage.getCurrentPageUrl().toLowerCase();
        Assert.assertTrue(url.contains("checkout") || url.contains("login") || url.contains("gateway"),
                "Failed to redirect out of the main cart during checkout sequence. Landed instead on: " + url);
    }

    // Covers SC_25 & SC_26: Verify cart total price & Verify cart sub-total price
    @Test
    public void testVerifySubtotalAndTotalPriceLayouts() {
        ShoppingCartPage cartPage = loginAddProductAndOpenCart();

        // Parse string values into mathematical doubles
        double unitPrice = parseCurrencyValue(cartPage.getProductPrice());
        double subtotal = parseCurrencyValue(cartPage.getCartSubtotal());
        double grandTotal = parseCurrencyValue(cartPage.getOrderTotalText());

        // Using SoftAssert to check both math rules
        SoftAssert softAssert = new SoftAssert();
        // SC_26: Subtotal should equal unit price (since qty is 1)
        softAssert.assertEquals(subtotal, unitPrice, "Subtotal numeric evaluation error.");
        // SC_25: Grand total should equal subtotal (assuming no tax/shipping applied)
        softAssert.assertEquals(grandTotal, subtotal, "Invoice baseline matching calculation mismatch.");
        softAssert.assertAll();
    }

    // ==========================================
    //          REUSABLE POM HELPER METHODS
    // ==========================================

    /**
     * Logs into the application and navigates to an empty cart.
     * Used as a setup step to ensure a clean state before testing.
     */
    private ShoppingCartPage loginAndOpenCart() {
        shoppingCartPage.navigateTo(Constants.URL + "login");
        loginPage.login(Constants.EMAIL, Constants.PASSWORD);
        shoppingCartPage.navigateTo(Constants.URL + "cart");

        // Single, centralized account cart cleanup immediately following login instantiation
        shoppingCartPage.clearCartIfNotEmpty();
        return shoppingCartPage;
    }

    /**
     * Logs in, opens an empty cart, adds a specific product,
     * and navigates back to the cart page.
     */
    private ShoppingCartPage loginAddProductAndOpenCart() {
        // Reuses the safe, clean setup pipeline above without duplicating commands
        loginAndOpenCart();
        productPage.navigateToProductAndAddToCart(Constants.URL + Constants.PRODUCT_PATH_LAPTOP);
        shoppingCartPage.navigateTo(Constants.URL + "cart");
        return shoppingCartPage;
    }

    /**
     * Helper utility to strip currency characters (like $ and commas)
     * and safely parse to clean numerical values for mathematical assertions.
     */
    private double parseCurrencyValue(String text) {
        return Double.parseDouble(text.replaceAll("[^0-9.]", "").trim());
    }
}