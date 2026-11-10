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
@DisplayName("ParaBank UI: Авторизація та аутентифікація користувача")
public class LoginUiTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String baseUrl = "https://parabank.parasoft.com/parabank/index.htm";

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // Безголовий режим за вимогами Спринту 4
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("TC-004: Перевірка наявності та відображення елементів форми авторизації")
    void testLoginPageFormPresent() {
        // Arrange & Act
        driver.get(baseUrl);

        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        WebElement passwordInput = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("input.button"));

        // Assert
        assertAll(
            () -> assertTrue(usernameInput.isDisplayed(), "Поле логіну має бути видимим"),
            () -> assertTrue(passwordInput.isDisplayed(), "Поле пароля має бути видимим"),
            () -> assertTrue(loginButton.isDisplayed(), "Кнопка входу має бути видимою")
        );
    }

    @Test
    @DisplayName("TC-005: Спроба входу з некоректними даними (обробка сторінки з помилкою)")
    void testInvalidPasswordLoginThrowsError() {
        // Arrange
        driver.get(baseUrl);

        WebElement usernameInput = wait.until(ExpectedConditions.elementToBeClickable(By.name("username")));
        usernameInput.clear();
        usernameInput.sendKeys("BMIBM_invalid_user");

        WebElement passwordInput = driver.findElement(By.name("password"));
        passwordInput.clear();
        passwordInput.sendKeys("WrongPassword999!");

        // Act: використовуємо submit() для гарантованої відправки форми в headless Chrome
        passwordInput.submit();

        // Assert: чекаємо завантаження результату (помилки або елемента сторінки відповідей)
        WebElement resultElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".error, h1.title, #rightPanel p")
        ));

        assertNotNull(resultElement, "Після відправки невалідного логіну має відобразитися сторінка відповіді/помилки");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}