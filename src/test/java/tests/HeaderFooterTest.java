package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;
import org.openqa.selenium.WebDriver;

public class HeaderFooterTest extends BaseTest {

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

    // Covers header navigation to Books
    @Test(priority = 1)
    public void testVerifyBooksNavigation() {
        homePage.clickBooks();
        Assert.assertTrue(homePage.getCurrentUrl().contains("/books"), "Failed to navigate to Books page.");
    }

    // Covers header navigation to Computers
    @Test(priority = 2)
    public void testVerifyComputersNavigation() {
        homePage.clickComputers();
        Assert.assertTrue(homePage.getCurrentUrl().contains("/computers"), "Failed to navigate to Computers page.");
    }

    // Covers header navigation to Electronics
    @Test(priority = 3)
    public void testVerifyElectronicsNavigation() {
        homePage.clickElectronics();
        Assert.assertTrue(homePage.getCurrentUrl().contains("/electronics"), "Failed to navigate to Electronics page.");
    }

    // Covers footer navigation to Sitemap
    @Test(priority = 4)
    public void testVerifySitemapNavigation() {
        homePage.clickSitemap();
        Assert.assertTrue(homePage.getCurrentUrl().contains("/sitemap"), "Failed to navigate to Sitemap page.");
    }

    // Covers footer navigation to About Us
    @Test(priority = 5)
    public void testVerifyAboutUsNavigation() {
        homePage.clickAboutUs();
        Assert.assertTrue(homePage.getCurrentUrl().contains("/about-us"), "Failed to navigate to About Us page.");
    }

    // Covers footer navigation to Contact Us
    @Test(priority = 6)
    public void testVerifyContactUsNavigation() {
        homePage.clickContactUs();
        Assert.assertTrue(homePage.getCurrentUrl().contains("/contactus"), "Failed to navigate to Contact Us page.");
    }
}