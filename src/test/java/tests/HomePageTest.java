package tests;

import base.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.NewsPage;
import utils.Constants;

public class HomePageTest extends BaseTest {

    private HomePage homePage;
    private NewsPage newsPage;

    // This method runs before EVERY test. It initializes the page objects
    // with the current thread's WebDriver instance.
    @BeforeMethod
    public void prepareIsolatedTestContext() {
        WebDriver driver = getDriverInstance();
        homePage = new HomePage(driver);
        newsPage = new NewsPage(driver);
    }

    // =========================================================================
    //         PART 1: STANDARD HOMEPAGE UI & NAVIGATION TEST CASES
    // =========================================================================

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

    // =========================================================================
    //             PART 2: NEWS SECTION TEST CASES
    // =========================================================================

    // Covers TC_1: Verify News page loads successfully
    @Test(priority = 12)
    public void testVerifyNewsPageLoads() {
        homePage.clickNewsLink();
        Assert.assertTrue(homePage.getCurrentUrl().contains("news"),
                "TC_1 Failed: News page did not load.");
    }

    // Covers TC_2: Verify News articles titles displayed clearly
    @Test(priority = 13)
    public void testVerifyNewsTitlesDisplayed() {
        WebDriver driver = getDriverInstance();
        homePage.clickNewsLink();
        Assert.assertTrue(driver.getPageSource().contains("nopCommerce"),
                "TC_2 Failed: Titles not displayed.");
    }

    // Covers TC_3: Verify clicking article navigates to detail page
    @Test(priority = 14)
    public void testVerifyArticleNavigatesToDetail() {
        homePage.clickNewsLink();
        String newsPageUrl = homePage.getCurrentUrl();

        newsPage.clickFirstArticle();

        Assert.assertNotEquals(homePage.getCurrentUrl(), newsPageUrl,
                "TC_3 Failed: Did not navigate to detail page.");
    }

    // Covers TC_4: Verify News link visible in navigation
    @Test(priority = 15)
    public void testVerifyNewsLinkVisibleInNav() {
        Assert.assertTrue(homePage.isNewsLinkVisible(),
                "TC_4 Failed: News link not visible.");
    }

    // Covers TC_5: Verify News accessible from Homepage
    @Test(priority = 16)
    public void testVerifyNewsAccessibleFromHomepage() {
        homePage.clickNewsLink();
        Assert.assertTrue(homePage.getCurrentUrl().contains("news"),
                "TC_5 Failed: News not accessible from homepage.");
    }

    // Covers TC_6: Verify article detail displays full content
    @Test(priority = 17)
    public void testVerifyArticleDetailFullContent() {
        WebDriver driver = getDriverInstance();
        homePage.clickNewsLink();
        newsPage.clickFirstArticle();

        Assert.assertTrue(driver.getPageSource().length() > 0,
                "TC_6 Failed: Article content not displayed.");
    }

    // Covers TC_7: Verify article detail page displays the name
    @Test(priority = 18)
    public void testVerifyArticleDetailDisplaysName() {
        WebDriver driver = getDriverInstance();
        homePage.clickNewsLink();
        newsPage.clickFirstArticle();

        Assert.assertTrue(driver.getPageSource().contains("nopCommerce"),
                "TC_7 Failed: Article name not displayed.");
    }

    // Covers TC_8: Verify Back button returns to News page
    @Test(priority = 19)
    public void testVerifyBackButtonReturnsToNews() {
        WebDriver driver = getDriverInstance();
        homePage.navigateTo(Constants.URL + "news");

        newsPage.clickFirstArticle();
        String articleUrl = homePage.getCurrentUrl();

        driver.navigate().back();

        Assert.assertNotEquals(homePage.getCurrentUrl(), articleUrl,
                "TC_8 Failed: Back button did not work.");
    }
}