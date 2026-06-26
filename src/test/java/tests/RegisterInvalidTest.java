package tests;

import base.BaseTest;
import pages.HomePage;
import pages.RegisterPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.Constants;
import java.util.Random;

public class RegisterInvalidTest extends BaseTest {

    private HomePage homePage;
    private RegisterPage registerPage;
    private SoftAssert softAssert;
    private String randomEmail;

    @BeforeMethod
    public void setupPageObjects() {
        WebDriver driver = getDriverInstance();
        homePage = new HomePage(driver);
        registerPage = new RegisterPage(driver);
        softAssert = new SoftAssert();

        // Generate dynamic test data
        Random rand = new Random();
        randomEmail = "testuser" + rand.nextInt(100000) + "@mail.com";

        // Pre-condition for all tests in this class: Navigate to the Registration page
        homePage.clickRegisterLink();
    }

    @Test
    public void testWithEmptyCredentials() {
        System.out.println("Testing registration with completely empty data...");
        registerPage.clickRegisterButton();

        // 1. First Name
        softAssert.assertTrue(registerPage.isFirstNameRequiredWarningMessageDisplayed(), "First name warning missing.");
        softAssert.assertEquals(registerPage.getFirstNameWarningMessageText(), Constants.ERR_REQ_FIRSTNAME);

        // 2. Last Name
        softAssert.assertTrue(registerPage.isLastNameRequiredWarningMessageDisplayed(), "Last name warning missing.");
        softAssert.assertEquals(registerPage.getLastNameWarningMessageText(), Constants.ERR_REQ_LASTNAME);

        // 3. Email
        softAssert.assertTrue(registerPage.isEmailRequiredWarningMessageDisplayed(), "Email warning missing.");
        softAssert.assertEquals(registerPage.getEmailWarningMessageText(), Constants.ERR_REQ_EMAIL);

        // 4. Password
        softAssert.assertTrue(registerPage.isPasswordRequiredWarningMessageDisplayed(), "Password warning missing.");
        softAssert.assertEquals(registerPage.getPasswordWarningMessageText(), Constants.ERR_REQ_PASSWORD);

        // 5. Confirm Password
        softAssert.assertTrue(registerPage.isConfirmPasswordWarningMessageDisplayed(), "Confirm password warning missing.");
        softAssert.assertEquals(registerPage.getConfirmPasswordWarningMessageText(), Constants.ERR_REQ_CONFIRMPASSWORD);

        softAssert.assertAll();
    }

    @Test
    public void testWithEmptyFirstNameField() {
        System.out.println("Testing registration with empty First Name...");
        registerPage.registerData(
                "",
                Constants.LAST_NAME,
                randomEmail,
                Constants.PASSWORD,
                Constants.PASSWORD
        );

        // Check expected error is present
        softAssert.assertTrue(registerPage.isFirstNameRequiredWarningMessageDisplayed(), "First name warning missing.");
        softAssert.assertEquals(registerPage.getFirstNameWarningMessageText(), Constants.ERR_REQ_FIRSTNAME);

        // Check other errors are NOT present
        softAssert.assertFalse(registerPage.isLastNameRequiredWarningMessageDisplayed(), "Last name warning should not be displayed.");
        softAssert.assertFalse(registerPage.isEmailRequiredWarningMessageDisplayed(), "Email warning should not be displayed.");
        softAssert.assertFalse(registerPage.isPasswordRequiredWarningMessageDisplayed(), "Password warning should not be displayed.");
        softAssert.assertFalse(registerPage.isConfirmPasswordWarningMessageDisplayed(), "Confirm password warning should not be displayed.");

        softAssert.assertAll();
    }

    @Test
    public void testWithEmptyLastNameField() {
        System.out.println("Testing registration with empty Last Name...");
        registerPage.registerData(
                Constants.FIRST_NAME,
                "",
                randomEmail,
                Constants.PASSWORD,
                Constants.PASSWORD
        );

        softAssert.assertTrue(registerPage.isLastNameRequiredWarningMessageDisplayed(), "Last name warning missing.");
        softAssert.assertEquals(registerPage.getLastNameWarningMessageText(), Constants.ERR_REQ_LASTNAME);

        softAssert.assertFalse(registerPage.isFirstNameRequiredWarningMessageDisplayed(), "First name warning should not be displayed.");
        softAssert.assertFalse(registerPage.isEmailRequiredWarningMessageDisplayed(), "Email warning should not be displayed.");
        softAssert.assertFalse(registerPage.isPasswordRequiredWarningMessageDisplayed(), "Password warning should not be displayed.");
        softAssert.assertFalse(registerPage.isConfirmPasswordWarningMessageDisplayed(), "Confirm password warning should not be displayed.");

        softAssert.assertAll();
    }

    @Test
    public void testWithEmptyEmailField() {
        System.out.println("Testing registration with empty Email...");
        registerPage.registerData(
                Constants.FIRST_NAME,
                Constants.LAST_NAME,
                "",
                Constants.PASSWORD,
                Constants.PASSWORD
        );

        softAssert.assertTrue(registerPage.isEmailRequiredWarningMessageDisplayed(), "Email warning missing.");
        softAssert.assertEquals(registerPage.getEmailWarningMessageText(), Constants.ERR_REQ_EMAIL);

        softAssert.assertFalse(registerPage.isFirstNameRequiredWarningMessageDisplayed(), "First name warning should not be displayed.");
        softAssert.assertFalse(registerPage.isLastNameRequiredWarningMessageDisplayed(), "Last name warning should not be displayed.");
        softAssert.assertFalse(registerPage.isPasswordRequiredWarningMessageDisplayed(), "Password warning should not be displayed.");
        softAssert.assertFalse(registerPage.isConfirmPasswordWarningMessageDisplayed(), "Confirm password warning should not be displayed.");

        softAssert.assertAll();
    }

    @Test
    public void testWithEmptyPasswordField() {
        System.out.println("Testing registration with empty Password...");
        registerPage.registerData(
                Constants.FIRST_NAME,
                Constants.LAST_NAME,
                randomEmail,
                "",
                Constants.PASSWORD
        );

        softAssert.assertTrue(registerPage.isPasswordRequiredWarningMessageDisplayed(), "Password warning missing.");
        softAssert.assertEquals(registerPage.getPasswordWarningMessageText(), Constants.ERR_REQ_PASSWORD);

        softAssert.assertTrue(registerPage.isConfirmPasswordWarningMessageDisplayed(), "Confirm password warning missing (due to mismatch).");

        softAssert.assertFalse(registerPage.isFirstNameRequiredWarningMessageDisplayed(), "First name warning should not be displayed.");
        softAssert.assertFalse(registerPage.isLastNameRequiredWarningMessageDisplayed(), "Last name warning should not be displayed.");
        softAssert.assertFalse(registerPage.isEmailRequiredWarningMessageDisplayed(), "Email warning should not be displayed.");

        softAssert.assertAll();
    }

    @Test
    public void testWithEmptyConfirmPasswordField() {
        System.out.println("Testing registration with empty Confirm Password...");
        registerPage.registerData(
                Constants.FIRST_NAME,
                Constants.LAST_NAME,
                randomEmail,
                Constants.PASSWORD,
                ""
        );

        softAssert.assertTrue(registerPage.isConfirmPasswordWarningMessageDisplayed(), "Confirm password warning missing.");
        softAssert.assertEquals(registerPage.getConfirmPasswordWarningMessageText(), Constants.ERR_REQ_CONFIRMPASSWORD);

        softAssert.assertFalse(registerPage.isFirstNameRequiredWarningMessageDisplayed(), "First name warning should not be displayed.");
        softAssert.assertFalse(registerPage.isLastNameRequiredWarningMessageDisplayed(), "Last name warning should not be displayed.");
        softAssert.assertFalse(registerPage.isEmailRequiredWarningMessageDisplayed(), "Email warning should not be displayed.");

        softAssert.assertAll();
    }
}