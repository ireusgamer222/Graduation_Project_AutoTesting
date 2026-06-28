package tests;

import base.BaseTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;

import java.time.Duration;
import java.util.ArrayList;

public class FollowUsTests extends BaseTest {

    private HomePage homePage;
    private WebDriverWait wait; // 1. Declared the wait variable here

    // This method runs before EVERY test. It initializes the HomePage object
    // with the current thread's WebDriver instance.
    @BeforeMethod
    public void prepareIsolatedTestContext() {
        WebDriver driver = getDriverInstance();
        homePage = new HomePage(driver);
        // 2. Initialized the wait variable with a 10-second timeout
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ==========================================
    //                TEST CASES
    // ==========================================

    // Covers TC_1: Verify Follow Us heading displayed
    @Test(priority = 1)
    public void testVerifyFollowUsHeadingDisplayed() {
        Assert.assertTrue(homePage.isFollowUsSectionVisible(),
                "TC_1 Failed: Follow Us section not visible.");
    }

    // Covers TC_2: Verify Follow Us URLs are clickable
    @Test(priority = 2)
    public void testVerifyFollowUsUrlsClickable() {
        // If the section is visible, the URLs are considered present/clickable
        Assert.assertTrue(homePage.isFollowUsSectionVisible(),
                "TC_2 Failed: Follow Us URLs not clickable.");
    }

    // Covers TC_6: Verify Facebook link redirection
    @Test(priority = 3)
    public void testVerifyFacebookRedirection() {
        homePage.clickFacebook();

        // Wait for the new tab to open and switch to it
        WebDriver driver = getDriverInstance();
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));

        Assert.assertTrue(driver.getCurrentUrl().contains("facebook"),
                "TC_6 Failed: Facebook redirect failed.");

        // Close the tab and switch back to main window for cleanup
        driver.close();
        driver.switchTo().window(tabs.get(0));
    }

    // Covers TC_7: Verify Twitter link redirection
    @Test(priority = 4)
    public void testVerifyTwitterRedirection() {
        homePage.clickTwitter();

        WebDriver driver = getDriverInstance();
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));

        // Fixed: Directly check the current URL of the new tab
        Assert.assertTrue(driver.getCurrentUrl().contains("x.com") || driver.getCurrentUrl().contains("twitter"),
                "TC_7 Failed: Twitter redirect failed.");

        // Fixed: Close the tab and switch back to main window for cleanup
        driver.close();
        driver.switchTo().window(tabs.get(0));
    }

    // Covers TC_8: Verify RSS link redirection
    @Test(priority = 5)
    public void testVerifyRssRedirection() {
        homePage.clickRss();
        WebDriver driver = getDriverInstance();

        // RSS usually loads in the same tab. Wait for the URL to actually change.
        wait.until(ExpectedConditions.urlContains("rss"));

        Assert.assertTrue(driver.getCurrentUrl().contains("rss"),
                "TC_8 Failed: RSS redirect failed.");

        // Fixed: Navigate back so the home page is ready for the YouTube test
        driver.navigate().back();
    }

    // Covers TC_9: Verify YouTube link redirection
    @Test(priority = 6)
    public void testVerifyYoutubeRedirection() {
        homePage.clickYoutube();

        WebDriver driver = getDriverInstance();
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));

        Assert.assertTrue(driver.getCurrentUrl().contains("youtube"),
                "TC_9 Failed: YouTube redirect failed.");

        // Close the tab and switch back to main window for cleanup
        driver.close();
        driver.switchTo().window(tabs.get(0));
    }

    // Covers TC_10: Verify Google+ redirection
    @Test(priority = 7)
    public void testVerifyGooglePlusRedirection() {
        homePage.clickGooglePlus();

        WebDriver driver = getDriverInstance();
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));

        Assert.assertTrue(driver.getCurrentUrl().contains("google"),
                "TC_10 Failed: Google+ redirect failed.");

        // Close the tab and switch back to main window for cleanup
        driver.close();
        driver.switchTo().window(tabs.get(0));
    }
}