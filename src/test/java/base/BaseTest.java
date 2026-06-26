package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import utils.Constants;
import listeners.TestListener;

/*
 * BaseTest is the parent class for all test classes.
 *
 * Responsibilities:
 * - Open a browser before each test.
 * - Close the browser after each test.
 * - Provide access to the current browser instance.
 * - Connect TestNG with the custom TestListener.
 *
 * Any test class that extends BaseTest automatically
 * gets browser setup and cleanup.
 */
@Listeners(TestListener.class)
public class BaseTest {

    /*
     * ThreadLocal stores a separate browser instance
     * for each running test.
     *
     * This helps avoid conflicts when tests are executed
     * in parallel.
     */
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /*
     * Runs before every test method.
     *
     * Steps:
     * 1. Download/setup the correct ChromeDriver.
     * 2. Open a new Chrome browser.
     * 3. Maximize the browser window.
     * 4. Navigate to the test website.
     * 5. Save the browser instance for the current test.
     */
    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        WebDriver localDriver = new ChromeDriver();
        localDriver.manage().window().maximize();
        localDriver.get(Constants.URL);

        driverThreadLocal.set(localDriver);
    }

    /*
     * Returns the browser instance currently being used.
     *
     * This method is used by:
     * - Test classes
     * - TestListener
     * - Screenshot capture logic
     */
    public static WebDriver getDriverInstance() {
        return driverThreadLocal.get();
    }

    /*
     * Runs after every test method.
     *
     * Steps:
     * 1. Close the browser.
     * 2. Remove the stored driver instance.
     *
     * Removing the driver helps keep memory clean,
     * especially when running many tests.
     */
    @AfterMethod
    public void tearDown() {
        WebDriver localDriver = getDriverInstance();

        if (localDriver != null) {
            localDriver.quit();
        }

        driverThreadLocal.remove();
    }
}