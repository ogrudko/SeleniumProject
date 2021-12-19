import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.sql.SQLOutput;
import java.util.List;

public class AvicTests {
    private WebDriver driver;

    @BeforeTest
    public void profileSetUp(){
        System.setProperty("webdriver.chrome.driver", "/Users/ogrudko/IdeaProjects/FirstWebDriverProject/src/main/resources/chromedriver");
    }

    @BeforeMethod
    public void testSetUp(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://avic.ua/");
    }

    @Test
    public void checkThatUrlContainsSearchedWord() {
        driver.findElement(By.xpath("//input[@id='input_search']")).sendKeys("iphone 12");
        driver.findElement(By.xpath("//button[@class='button-reset search-btn']")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("query=iphone+12"));
    }

    @Test
    public void verifyThatCartContainsChosenItem() {
        driver.findElement(By.xpath("//input[@id='input_search']")).sendKeys("iphone 12");
        driver.findElement(By.xpath("//button[@class='button-reset search-btn']")).click();
        WebDriverWait wait = new WebDriverWait(driver, 15);
        WebElement element = driver.findElement(By.xpath("//a[@class='prod-cart__buy']"));
        element.click();
        String expectedItemDescription = element.getAttribute("data-ecomm-cart");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("js_cart")));
        WebElement actualElement = driver.findElement(By.xpath("//div[@class='cart-parent-limit']/div[@class='item']"));
        String actualItemDescription = actualElement.getAttribute("data-ecomm-cart");
        Assert.assertEquals(expectedItemDescription, actualItemDescription);
    }

    @Test
    public void verifyNumberOfItemsInCart() {
        driver.findElement(By.xpath("//input[@id='input_search']")).sendKeys("iphone 12");
        driver.findElement(By.xpath("//button[@class='button-reset search-btn']")).click();
        WebDriverWait wait = new WebDriverWait(driver, 15);
        List<WebElement> elements = driver.findElements(By.xpath("//a[@class='prod-cart__buy']"));
        for (int i = 0; i < 3; i++) {
            elements.get(i).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("js_cart")));
            driver.findElement(By.xpath("//button[@class='fancybox-button fancybox-close-small']")).click();
        }
        WebElement element = driver.findElement(By.xpath("//div[@class='header-bottom__cart active-cart flex-wrap middle-xs js-btn-open']/div[@class='active-cart-item js_cart_count']"));
        String actualAmountInCart = element.getText();
        String expectedAmountInCart = "3";
        Assert.assertEquals(actualAmountInCart, expectedAmountInCart);
    }

    @Test
    public void signInWithNonExistingCredentials() {
        String signInPageLink = "//div[@class='header-bottom search_mobile_display']//a[@href='https://avic.ua/sign-in']";
        String loginInputField = "//div[@class='sign-holder clearfix']//input[@name='login']";
        String passwordInputField = "//div[@class='sign-holder clearfix']//input[@name='password']";
        String signInButton = "//div[@class='sign-holder clearfix']//button[@type='submit']";
        String popUpErrorMessage = "//div[@id='modalAlert']//div[@class='col-xs-12 js_message']";
        String expectedErrorMessage = "Неверные данные авторизации.";
        driver.findElement(By.xpath(signInPageLink)).click();
        WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(loginInputField)));
        driver.findElement(By.xpath(loginInputField)).sendKeys("example@example.com");
        driver.findElement(By.xpath(passwordInputField)).sendKeys("somePassword");
        driver.findElement(By.xpath(signInButton)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modalAlert")));
        String actualErrorMessage = driver.findElement(By.xpath(popUpErrorMessage)).getText();
        Assert.assertTrue(actualErrorMessage.equals(expectedErrorMessage));
    }

    @AfterMethod
    public void testTearDown(){
        driver.quit();
    }


}
