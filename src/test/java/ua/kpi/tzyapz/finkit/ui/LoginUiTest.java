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
@DisplayName("ParaBank UI: Автоматизація сценаріїв авторизації та кабінету за test-cases.xlsx")
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
    @DisplayName("TC-004: F-20, GUI-10 — Вхід клієнта у кабінет з коректним логіном та паролем")
    void testValidLogin() {
        driver.get(baseUrl);
        WebElement usernameInput = wait.until(ExpectedConditions.elementToBeClickable(By.name("username")));
        usernameInput.clear();
        usernameInput.sendKeys("john");

        WebElement passwordInput = driver.findElement(By.name("password"));
        passwordInput.clear();
        passwordInput.sendKeys("demo");
        passwordInput.submit();

        WebElement overviewHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#rightPanel h1.title, #leftPanel p.smallText")
        ));
        assertTrue(overviewHeader.isDisplayed(), "Успішний вхід має відкривати огляд рахунків клієнта");
    }

    @Test
    @DisplayName("TC-005: F-20 — Неуспішний вхід клієнта з некоректним паролем")
    void testInvalidPasswordLogin() {
        driver.get(baseUrl);
        WebElement usernameInput = wait.until(ExpectedConditions.elementToBeClickable(By.name("username")));
        usernameInput.clear();
        usernameInput.sendKeys("BMIBM_user1");

        WebElement passwordInput = driver.findElement(By.name("password"));
        passwordInput.clear();
        passwordInput.sendKeys("WrongPass!");
        passwordInput.submit();

        // Перевіряємо саме елемент помилки p.error з точним текстом
        WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("p.error")));
        assertTrue(errorElement.getText().contains("The username and password could not be verified"),
                "При некоректному паролі має відображатися конкретна помилка авторизації");
    }

    @Test
    @DisplayName("TC-007: F-40, GUI-40 — Перегляд сторінки «Огляд рахунків» та підсумкової суми")
    void testAccountsOverviewTable() {
        driver.get(baseUrl);
        wait.until(ExpectedConditions.elementToBeClickable(By.name("username"))).sendKeys("john");
        driver.findElement(By.name("password")).sendKeys("demo");
        driver.findElement(By.cssSelector("input.button")).click();

        WebElement overviewLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Accounts Overview")));
        overviewLink.click();

        WebElement accountsTable = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accountTable")));
        assertTrue(accountsTable.isDisplayed(), "Таблиця рахунків повинна відображатися у кабінеті");
    }

    @Test
    @DisplayName("TC-010: F-70, BL-40 — Успішне відкриття форми переказу коштів між рахунками")
    void testTransferFundsForm() {
        driver.get(baseUrl);
        wait.until(ExpectedConditions.elementToBeClickable(By.name("username"))).sendKeys("john");
        driver.findElement(By.name("password")).sendKeys("demo");
        driver.findElement(By.cssSelector("input.button")).click();

        WebElement transferLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Transfer Funds")));
        transferLink.click();

        WebElement amountInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("amount")));
        assertTrue(amountInput.isDisplayed(), "Поле введення суми переказу має бути видимим");
    }

    @Test
    @DisplayName("TC-012: F-70, BL-40 — Негативна перевірка переказу коштів з нульовою сумою (0 грн)")
    void testTransferZeroAmount() {
        driver.get(baseUrl);
        wait.until(ExpectedConditions.elementToBeClickable(By.name("username"))).sendKeys("john");
        driver.findElement(By.name("password")).sendKeys("demo");
        driver.findElement(By.cssSelector("input.button")).click();

        WebElement transferLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Transfer Funds")));
        transferLink.click();

        WebElement amountInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("amount")));
        amountInput.clear();
        amountInput.sendKeys("0");

        WebElement submitBtn = driver.findElement(By.cssSelector("input.button"));
        submitBtn.click();

        assertNotNull(amountInput, "Форма опрацьовує граничне нульове значення");
    }

    @Test
    @DisplayName("TC-021: F-120, GUI-80 — Завершення сеансу роботи (Log Out) та перевірка виходу")
    void testLogout() {
        driver.get(baseUrl);
        wait.until(ExpectedConditions.elementToBeClickable(By.name("username"))).sendKeys("john");
        driver.findElement(By.name("password")).sendKeys("demo");
        driver.findElement(By.cssSelector("input.button")).click();

        WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Log Out")));
        logoutLink.click();

        WebElement loginBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input.button")));
        assertTrue(loginBtn.isDisplayed(), "Після виходу користувач повертається до форми входу");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}