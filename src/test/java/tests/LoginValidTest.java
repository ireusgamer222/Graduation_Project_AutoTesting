package tests;

import base.BaseTest;
import pages.HomePage;
import pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.Constants;

public class LoginValidTest extends BaseTest {

    // Class-level variables so all methods can access them
    private HomePage homePage;
    private LoginPage loginPage;

    @BeforeMethod
    public void setupPageObjects() {
        // Retrieve the driver created by BaseTest and initialize Page Objects
        WebDriver driver = getDriverInstance();
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
    }

    @Test
    public void testLoginWithValidCredentials() {
        System.out.println("Navigating to Login Page...");
        homePage.clickLoginLink();

        System.out.println("Filling login form with valid data...");
        loginPage.login(Constants.EMAIL, Constants.PASSWORD);

        // Step 1: Verify successful redirect to the main dashboard
        Assert.assertEquals(getDriverInstance().getCurrentUrl(), Constants.URL,
                "Current URL does not match the expected dashboard URL after login.");

        // Step 2: Verify the account email is displayed in the header
        Assert.assertEquals(homePage.getLoggedCustomerEmail(), Constants.EMAIL,
                "Displayed customer info does not match the valid logged-in user email.");
    }

    @AfterMethod
    public void logoutLoggedUser() {
        // Safely logs out before BaseTest closes the browser
        if (homePage.isUserLoggedIn()) {
            System.out.println("Cleaning up session: Logging out...");
            homePage.clickLogoutButton();
        }
    }
}