package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

/*
 * NewsPage handles interactions with the News section of the website.
 * It extends BasePage to reuse the robust wrapper methods
 * with built-in explicit waits.
 */
public class NewsPage extends BasePage {

    // ==========================================
    //                 LOCATORS
    // ==========================================
    private final By articleTitles = By.cssSelector(".news-items h2");
    private final By articleLinks = By.cssSelector("a.read-more");
    private final By backButton = By.cssSelector("a.back-link");
    private final By commentBox = By.id("new-comment_CommentText");
    private final By commentSubmit = By.cssSelector(".buttons input[type='submit']");
    private final By commentResult = By.cssSelector(".result");

    // ==========================================
    //               CONSTRUCTOR
    // ==========================================
    public NewsPage(WebDriver driver) {
        super(driver); // Passes driver to BasePage to initialize waits
    }

    // ==========================================
    //               PAGE METHODS
    // ==========================================

    public boolean areArticleTitlesDisplayed() {
        // Wait for at least one article title to be present, then verify all are displayed
        wait.until(ExpectedConditions.presenceOfElementLocated(articleTitles));
        List<WebElement> titles = driver.findElements(articleTitles);
        return !titles.isEmpty() && titles.stream().allMatch(WebElement::isDisplayed);
    }

    public void clickFirstArticle() {
        // Wait for the read-more links to be present, then click the first one
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(articleLinks));
        List<WebElement> links = driver.findElements(articleLinks);
        if (!links.isEmpty()) {
            links.get(0).click();
        }
    }

    public void clickBackButton() {
        click(backButton);
    }

    public void writeComment(String comment) {
        writeText(commentBox, comment);
    }

    public void submitComment() {
        click(commentSubmit);
    }

    public String getCommentResult() {
        return readText(commentResult);
    }
}