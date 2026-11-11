package ua.kpi.tzyapz.finkit.ui;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
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

    private void login(String username, String password) {
        driver.get(baseUrl);
        WebElement userInput = wait.until(ExpectedConditions.elementToBeClickable(By.name("username")));
        userInput.clear();
        userInput.sendKeys(username);
        WebElement passInput = driver.findElement(By.name("password"));
        passInput.clear();
        passInput.sendKeys(password);
        driver.findElement(By.cssSelector("input.button")).click();
    }

    @Test
    @DisplayName("TC-004: F-20, GUI-10 — Вхід клієнта у кабінет з коректним логіном та паролем")
    void testValidLogin() {
        login("john", "demo");
        WebElement overviewHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#rightPanel h1.title")));
        assertTrue(overviewHeader.isDisplayed(), "Успішний вхід має відкривати огляд рахунків клієнта");
    }

    @Test
    @DisplayName("TC-005: F-20 — Неуспішний вхід клієнта з некоректним паролем")
    void testInvalidPasswordLogin() {
        login("BMIBM_user1", "WrongPass!");
        WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("p.error")));
        assertTrue(errorElement.getText().contains("The username and password could not be verified"),
                "При некоректному паролі має відображатися конкретна помилка авторизації");
    }

    @Test
    @DisplayName("TC-007: F-40, GUI-40 — Перегляд сторінки «Огляд рахунків» та підсумкової суми")
    void testAccountsOverviewTable() {
        login("john", "demo");
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Accounts Overview"))).click();
        WebElement accountsTable = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accountTable")));
        assertTrue(accountsTable.isDisplayed(), "Таблиця рахунків повинна відображатися у кабінеті");
    }

    @Test
    @DisplayName("TC-010: F-70, BL-40 — Успішний переказ коштів 500 грн між власними рахунками")
    void testTransferFundsSuccess() {
        login("john", "demo");
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Transfer Funds"))).click();

        WebElement amountInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("amount")));
        amountInput.clear();
        amountInput.sendKeys("500");

        wait.until(d -> d.findElements(By.cssSelector("#fromAccountId option")).size() > 0);
        driver.findElement(By.cssSelector("input.button")).click();

        WebElement resultHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[contains(text(),'Transfer Complete')]")));
        assertTrue(resultHeader.isDisplayed(), "Після переказу 500 грн має відображатися 'Transfer Complete!'");

        // TC-010: показано суму 500 грн та номери рахунків
        String confirmationText = driver.findElement(By.cssSelector("#rightPanel")).getText();
        assertTrue(confirmationText.contains("500"),
                "У підтвердженні переказу має бути вказана сума 500 грн");
        assertTrue(confirmationText.toLowerCase().contains("account"),
                "У підтвердженні мають бути вказані номери рахунків");
    }

    @Test
    @DisplayName("TC-012: F-70, BL-40 — Негативна перевірка переказу коштів з нульовою сумою (0 грн)")
    void testTransferZeroAmount() {
        login("john", "demo");
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Transfer Funds"))).click();

        WebElement amountInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("amount")));
        amountInput.clear();
        amountInput.sendKeys("0");

        wait.until(d -> d.findElements(By.cssSelector("#fromAccountId option")).size() > 0);
        driver.findElement(By.cssSelector("input.button")).click();

        // Очікуємо до 5 секунд появи "Transfer Complete".
        // Якщо з'явився — це дефект: переказ 0 грн не повинен проходити, тест падає.
        // Якщо не з'явився за 5 сек — TimeoutException, це очікувано: переказ відхилено.
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//h1[contains(text(),'Transfer Complete')]")));
            fail("Переказ 0 грн не повинен завершуватися успіхом: з'явився заголовок 'Transfer Complete'. " +
                "Дефект demo BUG-002 (див. GitHub Issue #2) — тест лишається «червоним» до виправлення ParaBank");
        } catch (TimeoutException expected) {
            // Очікувана поведінка: "Transfer Complete" не з'явився — переказ відхилено
        }

        // Додатково: форма має залишитися доступною для корекції суми
        WebElement amountField = driver.findElement(By.id("amount"));
        assertTrue(amountField.isDisplayed(),
                "Після відхиленого переказу 0 грн поле amount має залишатися доступним");
    }

    @Test
    @DisplayName("TC-021: F-120, GUI-80 — Завершення сеансу роботи (Log Out) та перевірка виходу")
    void testLogout() {
        login("john", "demo");
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Log Out"))).click();
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
