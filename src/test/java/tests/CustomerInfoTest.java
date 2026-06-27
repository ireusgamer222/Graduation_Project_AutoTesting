package tests;

import base.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CustomerInfoPage;
import pages.HomePage;
import pages.LoginPage;
import utils.Constants;

public class CustomerInfoTest extends BaseTest {

    private HomePage homePage;
    private LoginPage loginPage;
    private CustomerInfoPage customerInfoPage;

    // This method runs before EVERY test. It initializes page objects
    // and logs in, as customer info requires authentication.
    @BeforeMethod
    public void prepareIsolatedTestContext() {
        WebDriver driver = getDriverInstance();
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        customerInfoPage = new CustomerInfoPage(driver);

        // Pre-condition: Log in to the application
        homePage.navigateTo(Constants.URL + Constants.LOGIN_PATH);
        loginPage.login(Constants.EMAIL, Constants.PASSWORD);
        homePage.waitForVisibilityOfLogout(); // Wait for login to complete
    }

    // ==========================================
    //                TEST CASES
    // ==========================================

    // Covers opening the Customer Info page
    @Test(priority = 1)
    public void testOpenCustomerInfoPage() {
        customerInfoPage.openCustomerInfoPage();

        // Verify URL changed to the customer info section
        Assert.assertTrue(homePage.getCurrentUrl().contains("customer/info"),
                "Failed to navigate to Customer Info page.");
    }

    // Covers updating Customer Info details
    @Test(priority = 2)
    public void testUpdateCustomerInfo() {
        customerInfoPage.openCustomerInfoPage();

        // Update fields using Constants
        customerInfoPage.selectFemale();
        customerInfoPage.enterFirstName(Constants.CUSTOMER_FIRST_NAME);
        customerInfoPage.enterLastName(Constants.CUSTOMER_LAST_NAME);
        customerInfoPage.enterEmail(Constants.CUSTOMER_EMAIL);

        // Save changes
        customerInfoPage.clickSave();

        // Verify the page saved successfully and stayed on the customer info URL
        Assert.assertTrue(homePage.getCurrentUrl().contains("customer/info"),
                "Failed to save customer info.");
    }
}