import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Tp3Test {
    WebDriver driver;
    JavascriptExecutor javaScriptExecutor;

    @BeforeAll
    public static void initialize() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void prepareDriver(){
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        driver.manage().timeouts().setScriptTimeout(Duration.ofMinutes(2));

        javaScriptExecutor = (JavascriptExecutor) driver;
    }

    @Test
    public void todoTestCase() throws InterruptedException {
        driver.get("https://todomvc.com");
        choosePlatform("Backbone.javaScriptExecutor");
        addTodo("Meet a Friend");
        addTodo("Buy Meat");
        addTodo("clean the car");
        removeTodo();
        Thread.sleep(4000);

        assertLeft(2);

    }
    @ParameterizedTest
    @ValueSource(strings = {"Backbone.javaScriptExecutor",
            "AngularjavaScriptExecutor",
            "Dojo",
            "React"})
    public void todosTestCase(String platform) {
        driver.get("https://todomvc.com");
        choosePlatform(platform);
        addTodo("Meet a Friend");
        addTodo("Buy Meat");
        addTodo("clean the car");
        removeTodo();

        assertLeft(2);

    }

    private void choosePlatform(String platform) {
        WebElement platformToConnect = driver.findElement(By.linkText(platform));
        platformToConnect.click();
    }
    private void addTodo(String todo) {
        WebElement todoAdd = driver.findElement(By.cssSelector("body > section > header > input"));
        todoAdd.sendKeys(todo);
        todoAdd.sendKeys(Keys.RETURN);
    }
    private void removeTodo() {
        WebElement todoRemove = driver.findElement(By.cssSelector("li:nth-child(2) .toggle"));
        todoRemove.click();
    }
    private void assertLeft(int expectedLeft) {
        WebElement assertElement = driver.findElement(By.xpath("//footer/*/span | //footer/span"));
        if(expectedLeft == 1 ) {
            String expectedTest = String.format("$d item left", expectedLeft);
            validateInnerText(assertElement,expectedTest);
        } else {
            String expectedTest = String.format("$d items left", expectedLeft);
            validateInnerText(assertElement,expectedTest);
        }
    }

    private void validateInnerText(WebElement element, String expectedTest) {
        ExpectedConditions.textToBePresentInElement(element, expectedTest);
    }

    @AfterEach
    public void quitDriver() throws InterruptedException {
        driver.quit();
    }
}