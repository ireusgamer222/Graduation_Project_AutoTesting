package listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/*
 * TestListener is a TestNG listener that monitors
 * the execution of test cases.
 *
 * It automatically:
 * - Logs test execution progress.
 * - Reports passed, failed, and skipped tests.
 * - Captures screenshots when a test fails.
 *
 * This helps make debugging easier and provides
 * better visibility into test execution.
 */
public class TestListener implements ITestListener {

    /*
     * Runs once when the test suite starts.
     *
     * Useful for logging the beginning of execution.
     */
    @Override
    public void onStart(ITestContext context) {
        System.out.println("====== 🚀 Test Suite Execution Started: " + context.getName() + " ======");
    }

    /*
     * Runs before each test case starts.
     *
     * Displays the name of the test currently running.
     */
    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("▶️ Starting Test Case: [ " + result.getMethod().getMethodName() + " ]");
    }

    /*
     * Runs when a test passes successfully.
     *
     * Useful for tracking completed tests in console logs.
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("✅ SUCCESS: [ " + result.getMethod().getMethodName() + " ] passed successfully.");
    }

    /*
     * Runs whenever a test fails.
     *
     * In addition to printing the failure reason,
     * a screenshot is automatically captured and
     * saved inside the screenshots folder.
     *
     * This makes it easier to investigate failures
     * without rerunning the test.
     */
    @Override
    public void onTestFailure(ITestResult result) {

        System.err.println("❌ FAILURE: [ " + result.getMethod().getMethodName() + " ] failed!");
        System.err.println("⚠️ Reason: " + result.getThrowable());

        try {

            // Get the browser instance used by the failed test
            org.openqa.selenium.WebDriver driver =
                    base.BaseTest.getDriverInstance();

            if (driver != null) {

                // Take a screenshot of the current browser state
                org.openqa.selenium.TakesScreenshot ts =
                        (org.openqa.selenium.TakesScreenshot) driver;

                java.io.File source =
                        ts.getScreenshotAs(org.openqa.selenium.OutputType.FILE);

                /*
                 * Create a unique screenshot name using:
                 * - Test method name
                 * - Current timestamp
                 *
                 * Example:
                 * loginTest_1751020304050.png
                 */
                String destinationPath =
                        System.getProperty("user.dir")
                                + "/screenshots/"
                                + result.getMethod().getMethodName()
                                + "_"
                                + System.currentTimeMillis()
                                + ".png";

                java.io.File finalDestination =
                        new java.io.File(destinationPath);

                // Save the screenshot to the project folder
                org.apache.commons.io.FileUtils.copyFile(
                        source,
                        finalDestination
                );

                System.out.println(
                        "📸 Failure screenshot saved to: "
                                + destinationPath
                );
            }

        } catch (Exception e) {

            System.err.println(
                    "🚨 Failed to capture screenshot: "
                            + e.getMessage()
            );
        }
    }

    /*
     * Runs when a test is skipped.
     *
     * This may happen due to:
     * - Failed dependencies
     * - Conditional test execution
     * - Configuration issues
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("⏭️ SKIPPED: [ " + result.getMethod().getMethodName() + " ] was skipped.");
    }

    /*
     * Runs once after all tests in the suite finish.
     *
     * Useful for logging the end of execution.
     */
    @Override
    public void onFinish(ITestContext context) {
        System.out.println("====== 🏁 Test Suite Execution Finished: " + context.getName() + " ======");
    }
}