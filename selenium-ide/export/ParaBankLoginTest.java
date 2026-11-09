package ua.kpi.tzyapz.finkit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;

import java.util.HashMap;
import java.util.Map;

public class ParaBankLoginTest {
    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<String, Object>();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void tC004LoginTest() {
        driver.get("https://parabank.parasoft.com/parabank/index.htm");
        driver.manage().window().setSize(new Dimension(1280, 800));
        driver.findElement(By.name("username")).sendKeys("BMIBM_user1");
        driver.findElement(By.name("password")).sendKeys("TestPass123!");
        driver.findElement(By.cssSelector("input.button")).click();
    }
}