package tests;

import base.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;
import utils.Constants;

public class HomePageTest extends BaseTest {

    private HomePage homePage;

    // This method runs before EVERY test. It initializes the HomePage object
    // with the current thread's WebDriver instance.
    @BeforeMethod
    public void prepareIsolatedTestContext() {
        WebDriver driver = getDriverInstance();
        homePage = new HomePage(driver);
    }

    // ==========================================
    //                TEST CASES
    // ==========================================

    // Verify the website logo is displayed
    @Test(priority = 1)
    public void testVerifyLogoDisplayed() {
        Assert.assertTrue(homePage.isLogoDisplayed(), "Homepage logo is not displayed.");
    }

    // Verify search functionality navigates to search page
    @Test(priority = 2)
    public void testVerifySearchNavigation() {
        homePage.searchProduct("Laptop");
        Assert.assertTrue(homePage.getCurrentUrl().contains("search"), "Failed to navigate to search page.");
    }

    // Verify Books menu navigation
    @Test(priority = 3)
    public void testVerifyBooksNavigation() {
        homePage.clickBooks();
        Assert.assertTrue(homePage.getCurrentUrl().contains("books"), "Failed to navigate to Books page.");
    }

    // Verify Computers menu navigation
    @Test(priority = 4)
    public void testVerifyComputersNavigation() {
        homePage.clickComputers();
        Assert.assertTrue(homePage.getCurrentUrl().contains("computers"), "Failed to navigate to Computers page.");
    }

    // Verify Electronics menu navigation
    @Test(priority = 5)
    public void testVerifyElectronicsNavigation() {
        homePage.clickElectronics();
        Assert.assertTrue(homePage.getCurrentUrl().contains("electronics"), "Failed to navigate to Electronics page.");
    }

    // Verify Apparel & Shoes menu navigation
    @Test(priority = 6)
    public void testVerifyApparelNavigation() {
        homePage.clickApparel();
        Assert.assertTrue(homePage.getCurrentUrl().contains("apparel-shoes"), "Failed to navigate to Apparel & Shoes page.");
    }

    // Verify Digital Downloads menu navigation
    @Test(priority = 7)
    public void testVerifyDigitalDownloadsNavigation() {
        homePage.clickDigitalDownloads();
        Assert.assertTrue(homePage.getCurrentUrl().contains("digital-downloads"), "Failed to navigate to Digital Downloads page.");
    }

    // Verify Jewelry menu navigation
    @Test(priority = 8)
    public void testVerifyJewelryNavigation() {
        homePage.clickJewelry();
        Assert.assertTrue(homePage.getCurrentUrl().contains("jewelry"), "Failed to navigate to Jewelry page.");
    }

    // Verify Gift Cards menu navigation
    @Test(priority = 9)
    public void testVerifyGiftCardsNavigation() {
        homePage.clickGiftCards();
        Assert.assertTrue(homePage.getCurrentUrl().contains("gift-cards"), "Failed to navigate to Gift Cards page.");
    }

    // Verify Shopping Cart header link navigation
    @Test(priority = 10)
    public void testVerifyShoppingCartNavigation() {
        homePage.clickShoppingCart();
        Assert.assertTrue(homePage.getCurrentUrl().contains("cart"), "Failed to navigate to Shopping Cart page.");
    }

    // Verify Wishlist header link navigation
    @Test(priority = 11)
    public void testVerifyWishlistNavigation() {
        homePage.clickWishlist();
        Assert.assertTrue(homePage.getCurrentUrl().contains("wishlist"), "Failed to navigate to Wishlist page.");
    }
}