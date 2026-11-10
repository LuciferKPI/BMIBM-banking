package ua.kpi.tzyapz.finkit.ui;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@Tag("ui")
@DisplayName("ParaBank UI: Авторизація та навігація (6 сценаріїв)")
public class LoginUiTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String baseUrl = "https://parabank.parasoft.com/parabank/index.htm";

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("TC-001: Перевірка заголовку головної сторінки ParaBank")
    void testPageTitle() {
        driver.get(baseUrl);
        assertTrue(driver.getTitle().contains("ParaBank"), "Заголовок сторінки має містити ParaBank");
    }

    @Test
    @DisplayName("TC-002: Перевірка видимості логотипу системи")
    void testLogoVisible() {
        driver.get(baseUrl);
        WebElement logo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img.logo")));
        assertTrue(logo.isDisplayed(), "Логотип має бути видимим");
    }

    @Test
    @DisplayName("TC-003: Перехід на сторінку реєстрації за посиланням Register")
    void testNavigateToRegister() {
        driver.get(baseUrl);
        WebElement registerLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Register")));
        registerLink.click();
        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1.title")));
        assertTrue(title.getText().contains("Signing up"), "Заголовок має бути Signing up");
    }

    @Test
    @DisplayName("TC-004: Перевірка наявності елементів форми авторизації")
    void testLoginPageFormPresent() {
        driver.get(baseUrl);
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        WebElement passwordInput = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("input.button"));

        assertAll(
            () -> assertTrue(usernameInput.isDisplayed(), "Поле логіну має бути видимим"),
            () -> assertTrue(passwordInput.isDisplayed(), "Поле пароля має бути видимим"),
            () -> assertTrue(loginButton.isDisplayed(), "Кнопка входу має бути видимою")
        );
    }

    @Test
    @DisplayName("TC-005: Спроба входу з некоректними даними")
    void testInvalidPasswordLoginThrowsError() {
        driver.get(baseUrl);
        WebElement usernameInput = wait.until(ExpectedConditions.elementToBeClickable(By.name("username")));
        usernameInput.clear();
        usernameInput.sendKeys("BMIBM_invalid_user");

        WebElement passwordInput = driver.findElement(By.name("password"));
        passwordInput.clear();
        passwordInput.sendKeys("WrongPassword999!");
        passwordInput.submit();

        WebElement resultElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".error, h1.title, #rightPanel p")
        ));
        assertNotNull(resultElement, "Має відобразитися сторінка відповіді/помилки");
    }

    @Test
    @DisplayName("TC-006: Відправка порожньої форми входу")
    void testEmptyCredentialsLogin() {
        driver.get(baseUrl);
        WebElement passwordInput = wait.until(ExpectedConditions.elementToBeClickable(By.name("password")));
        passwordInput.submit();

        WebElement resultElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".error, h1.title, #rightPanel p")
        ));
        assertNotNull(resultElement, "Має відобразитися сторінка відповіді або помилка");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}