package tests;

import base.BaseTest;
import pages.HomePage;
import pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.Constants;

import java.util.Arrays;
import java.util.List;

public class LoginInvalidTest extends BaseTest {

    private HomePage homePage;
    private LoginPage loginPage;
    private SoftAssert softAssert;

    @BeforeMethod
    public void prepareTestContext() {
        WebDriver driver = getDriverInstance();
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        softAssert = new SoftAssert();
    }

    @DataProvider(name = "invalidLoginData")
    public Object[][] getInvalidLoginData() {
        return new Object[][] {
                {
                        Constants.INVALID_EMAIL_FORMAT,
                        Constants.PASSWORD, // using the valid password constant for the format check
                        "Test: Invalid Email Format",
                        new String[]{Constants.ERR_INVALID_EMAIL, ""}
                },
                {
                        Constants.UNREGISTERED_EMAIL,
                        Constants.WRONG_PASSWORD,
                        "Test: Unregistered Credentials",
                        new String[]{"", Constants.ERR_UNSUCCESSFUL_LOGIN, Constants.ERR_NO_ACCOUNT}
                },
                {
                        Constants.EMPTY_EMAIL,
                        Constants.PASSWORD,
                        "Test: Empty Email Field",
                        new String[]{"", Constants.ERR_UNSUCCESSFUL_LOGIN, Constants.ERR_NO_ACCOUNT}
                }
        };
    }

    @Test(dataProvider = "invalidLoginData")
    public void loginWithInvalidCredentialsTest(String email, String password, String description, String[] expectedErrorMessages) {
        List<String> expectedErrorsList = Arrays.asList(expectedErrorMessages);
        System.out.println("--- Starting user login test: " + description + " ---");

        // 1. SETUP: Navigate to page
        homePage.clickLoginLink();

        // 2. EXECUTE: Fill out the entire form AND click Login first
        loginPage.enterEmail(email);
        if (password != null && !password.isEmpty()) {
            loginPage.enterPassword(password);
        }
        loginPage.clickLoginButton();

        // 3. ASSERT: Validate Inline Email Error (Index 0)
        String expectedInlineError = expectedErrorsList.get(0);
        if (expectedInlineError != null && !expectedInlineError.isEmpty()) {
            softAssert.assertTrue(loginPage.isEmailValidationErrorMessageDisplayed(),
                    "Email validation message missing from UI.");

            String actualInlineError = loginPage.getErrorMessageForValidEmailText();
            softAssert.assertTrue(actualInlineError.contains(expectedInlineError),
                    "Expected Inline Error: '" + expectedInlineError + "' but found: '" + actualInlineError + "'");
        }

        // 4. ASSERT: Validate Summary Errors (Index 1+)
        List<String> expectedSummaryErrors = expectedErrorsList.subList(1, expectedErrorsList.size());
        boolean expectsSummaryErrors = expectedSummaryErrors.stream().anyMatch(e -> !e.isEmpty());

        if (expectsSummaryErrors) {
            softAssert.assertTrue(loginPage.isValidationSummaryErrorsDisplayed(),
                    "Validation summary block is completely missing from UI.");

            // Get the entire text block so we don't miss the headers outside of the <li> tags
            String fullSummaryText = loginPage.getValidationSummaryErrorsText();

            for (String expected : expectedSummaryErrors) {
                if (!expected.isEmpty()) {
                    softAssert.assertTrue(fullSummaryText.contains(expected),
                            "Expected summary error not found: '" + expected + "'. \nActual block text was: '" + fullSummaryText + "'");
                }
            }
        }

        // 5. Finalize Assertions
        softAssert.assertAll();
    }
}