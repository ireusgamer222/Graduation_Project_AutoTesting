package tests;

import base.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;

public class NewsletterTests extends BaseTest {

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

    // Covers TC_1: Subscribe with valid email
    @Test(priority = 1)
    public void testSubscribeWithValidEmail() {
        homePage.enterNewsletterEmail("test" + System.currentTimeMillis() + "@example.com");
        homePage.clickSubscribe();
        Assert.assertTrue(homePage.getNewsletterResult().contains("Thank you"),
                "TC_1 Failed: Valid email subscription failed.");
    }

    // Covers TC_2: Subscribe with empty email
    @Test(priority = 2)
    public void testSubscribeWithEmptyEmail() {
        homePage.enterNewsletterEmail("");
        homePage.clickSubscribe();
        Assert.assertTrue(homePage.getNewsletterResult().contains("Enter valid email"),
                "TC_2 Failed: Empty email not handled.");
    }

    // Covers TC_3: Subscribe with invalid email format
    @Test(priority = 3)
    public void testSubscribeWithInvalidEmail() {
        homePage.enterNewsletterEmail("invalidemail");
        homePage.clickSubscribe();
        Assert.assertTrue(homePage.getNewsletterResult().contains("Enter valid email"),
                "TC_3 Failed: Invalid email not handled.");
    }

    // Covers TC_4: Subscribe with already registered email
    @Test(priority = 4)
    public void testSubscribeWithAlreadyRegisteredEmail() {
        homePage.enterNewsletterEmail("test@example.com");
        homePage.clickSubscribe();
        String result = homePage.getNewsletterResult();
        Assert.assertTrue(result.contains("already") || result.contains("Thank you"),
                "TC_4 Failed: Duplicate email not handled.");
    }

    // Covers TC_5: Subscribe with email containing spaces
    @Test(priority = 5)
    public void testSubscribeWithEmailWithSpaces() {
        homePage.enterNewsletterEmail("test @example.com");
        homePage.clickSubscribe();
        Assert.assertTrue(homePage.getNewsletterResult().contains("Enter valid email"),
                "TC_5 Failed: Email with spaces not handled.");
    }

    // Covers TC_8: Verify Subscribe button is visible
    @Test(priority = 6)
    public void testVerifySubscribeButtonVisible() {
        Assert.assertTrue(homePage.isSubscribeButtonVisible(),
                "TC_8 Failed: Subscribe button not visible.");
    }

    // Covers TC_9: Subscribe with very long email
    @Test(priority = 7)
    public void testSubscribeWithVeryLongEmail() {
        homePage.enterNewsletterEmail("averylongemailaddressthatexceedsnormallimits123456789@example.com");
        homePage.clickSubscribe();
        Assert.assertTrue(homePage.getNewsletterResult().contains("Enter valid email") ||
                        homePage.getNewsletterResult().contains("Thank you"),
                "TC_9 Failed: Long email not handled.");
    }

    // Covers TC_10: Subscribe with special characters
    @Test(priority = 8)
    public void testSubscribeWithSpecialCharacters() {
        homePage.enterNewsletterEmail("test!#$%@example.com");
        homePage.clickSubscribe();
        Assert.assertTrue(homePage.getNewsletterResult().contains("Enter valid email") ||
                        homePage.getNewsletterResult().contains("Thank you"),
                "TC_10 Failed: Special characters not handled.");
    }
}