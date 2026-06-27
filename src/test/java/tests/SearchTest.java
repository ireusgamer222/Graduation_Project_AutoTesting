package tests;

import base.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.SearchPage;
import utils.Constants;

public class SearchTest extends BaseTest {

    private SearchPage searchPage;

    // This method runs before EVERY test. It initializes the SearchPage object
    // with the current thread's WebDriver instance.
    @BeforeMethod
    public void prepareIsolatedTestContext() {
        WebDriver driver = getDriverInstance();
        searchPage = new SearchPage(driver);
    }

    // ==========================================
    //                TEST CASES
    // ==========================================

    // Covers searching with a valid product name
    @Test(priority = 1)
    public void testVerifySearchWithValidProduct() {
        searchPage.searchProduct(Constants.SEARCH_TERM_VALID);

        Assert.assertTrue(
                searchPage.getFirstProductName().contains(Constants.SEARCH_TERM_VALID),
                "Valid product not found in search results."
        );
    }

    // Covers searching with an invalid product name
    @Test(priority = 2)
    public void testVerifySearchWithInvalidProduct() {
        searchPage.searchProduct(Constants.SEARCH_TERM_INVALID);

        Assert.assertEquals(
                searchPage.getNoResultMessage(),
                Constants.ERR_NO_SEARCH_RESULTS,
                "Expected 'no products found' message mismatch."
        );
    }

    // Covers searching with a partial product name
    @Test(priority = 3)
    public void testVerifySearchWithPartialProductName() {
        searchPage.searchProduct(Constants.SEARCH_TERM_PARTIAL);

        Assert.assertTrue(
                searchPage.getFirstProductName().contains("Laptop"), // Verifying it maps back to the full word
                "Product not found using partial name."
        );
    }

    // Covers searching using the Enter key instead of the search button
    @Test(priority = 4)
    public void testVerifySearchUsingEnterKey() {
        searchPage.searchUsingEnter(Constants.SEARCH_TERM_VALID);

        Assert.assertTrue(
                searchPage.getFirstProductName().contains(Constants.SEARCH_TERM_VALID),
                "Product not found when searching with Enter key."
        );
    }

    // Covers opening a product details page directly from search results
    @Test(priority = 5)
    public void testVerifyProductDetailsPageOpens() {
        searchPage.searchProduct(Constants.SEARCH_TERM_VALID);
        searchPage.openFirstProduct();

        Assert.assertTrue(
                searchPage.getPageTitle().contains(Constants.SEARCH_TERM_VALID),
                "Failed to open product details page from search."
        );
    }
}