package tests;

import base.BaseTest;
import pages.HomePage;
import pages.RegisterPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.Constants;
import java.util.Random;

public class RegisterValidTest extends BaseTest {

    // Class-level variables
    private HomePage homePage;
    private RegisterPage registerPage;
    private String randomEmail;

    @BeforeMethod
    public void setupPageObjects() {
        // Retrieve driver and initialize Page Objects
        WebDriver driver = getDriverInstance();
        homePage = new HomePage(driver);
        registerPage = new RegisterPage(driver);

        // Generate dynamic test data for this specific test run
        Random rand = new Random();
        randomEmail = "testuser" + rand.nextInt(100000) + "@mail.com";
    }

    @Test
    public void testValidRegistration() {
        System.out.println("Navigating to Registration Page...");
        homePage.clickRegisterLink();

        System.out.println("Registering new user with email: " + randomEmail);
        registerPage.registerData(
                Constants.FIRST_NAME,
                Constants.LAST_NAME,
                randomEmail,
                Constants.PASSWORD,
                Constants.PASSWORD
        );

        String actualMessage = registerPage.getResultMessage();
        Assert.assertEquals(actualMessage, "Your registration completed",
                "Registration message does not match expected.");

        String currentUrl = getDriverInstance().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("registerresult"),
                "URL does not contain 'registerresult' after registration.");

        Assert.assertTrue(registerPage.isInfoDisplayed(),
                "Registered customer info is not displayed.");

        Assert.assertEquals(registerPage.getRegisteredCustomerInfo(), randomEmail,
                "Registered customer info does not match expected.");

        System.out.println("Proceeding past the success screen...");
        registerPage.clickContinue();

        Assert.assertEquals(getDriverInstance().getCurrentUrl(), Constants.URL,
                "Dashboard url after registration does not match expected base URL.");
    }

    @AfterMethod
    public void cleanupSession() {
        // Safely logs out the newly created user before BaseTest closes the browser
        if (homePage.isUserLoggedIn()) {
            System.out.println("Cleaning up session: Logging out newly registered user...");
            homePage.clickLogoutButton();
        }
    }
}