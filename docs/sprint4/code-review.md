# Code Review тест-класів — Спринт 4

**Дата:** 11.11.2026
**Рецензенти:** Пономаренко Р. В. (КТ/СМ), Городиська А. А. (ТА), Мажуга І. К. (ІТ) — перехресно
**Чек-лист:** розділ 5.3 методички спринта 4 (8 пунктів)
**Позначки:** так / ні / частково / N/A (пункт не застосовний до класу)

---

## 1. AccountLedgerTest.java

- **Автор:** Мажуга І. К.
- **Рецензент:** Пономаренко Р. В.

| # | Пункт чек-листа | Результат | Зауваження |
|---|---|---|---|
| 1 | Перевіряє правило BL, ID у @DisplayName | так | BL-70 і BL-80 видно. `testHistoryOrderAndEntries` без BL-мітки — правильно, бо `history()` не описаний у SRS v1.1 |
| 2 | Структура AAA | так | arrange/act/assert читається в усіх 6 тестах |
| 3 | Ім'я методу описує поведінку | так | `testWithdrawExactBalance`, `testNullArgumentsThrowNullPointerException`, `testRoundingScaleAndStateImmutability` |
| 4 | Межі з обох боків | так | точний баланс (500/500), перевищення (100/150), округлення HALF_UP (100.005 → 100.01), null-аргументи |
| 5 | `assertThrows` з конкретним класом; BigDecimal зі scale | так | `IllegalArgumentException`, `IllegalStateException`, `NullPointerException` — всі три перевіряються з конкретним типом. Порівняння BigDecimal зі scale (`1000.00`, `0.01`) |
| 6 | Незалежність тестів | так | `@BeforeEach` створює новий `AccountLedger` перед кожним тестом |
| 7 | Немає `Thread.sleep` | N/A | Юніт-тести |
| 8 | Падаючі тести мають Issue | так | `testWithdrawExactBalance` → BUG-009 (#14); `testNullArgumentsThrowNullPointerException` → BUG-012 (#18) |

**Висновок:** схвалено. Тест `testWithdrawExactBalance` ловить дефект BL-70 (точне зняття балансу), `testNullArgumentsThrowNullPointerException` — дефект BL-80 (код кидає IAE замість NPE). Обидва «червоні» тести — обґрунтовані, прив'язані до Issues. Зауважень немає.

---

## 2. TransferValidatorTest.java

- **Автор:** Городиська А. А.
- **Рецензент:** Пономаренко Р. В. / Мажуга І. К.

| # | Пункт чек-листа | Результат | Зауваження |
|---|---|---|---|
| 1 | Перевіряє правило BL, ID у @DisplayName | так | BL-40, BL-50 у всіх 9 методах |
| 2 | Структура AAA | так | Читається |
| 3 | Ім'я методу описує поведінку | так | `dailyLimitBoundaryCheck`, `validateReturnsErrorForSameAccount`, `validateReturnsErrorsForInvalidIbanFormats` |
| 4 | Межі з обох боків | так | BL-40: 49999.99 / 50000.00 / 50000.01 / 60000.00 + `todaySpent + amount` окремо. BL-50: валідні (2) + невалідні (4) + null |
| 5 | `assertThrows` з конкретним класом | ні | Відхилено з обґрунтуванням: `isValid()` повертає `boolean`, `validate()` — `List<TransferError>`. Обидва не кидають винятків, `assertThrows` не застосовний за сигнатурою. Замість цього — `assertTrue(errors.contains(TransferError.XXX))` для конкретних кодів |
| 6 | Незалежність тестів | так | `@BeforeEach` створює новий `TransferValidator` |
| 7 | Немає `Thread.sleep` | N/A | Юніт-тести |
| 8 | Падаючі тести мають Issue | так | `dailyLimitBoundaryCheck[2]` → BUG-008 (#11) |

**Висновок:** схвалено. Покрито всі 4 коди BL-40 (`AMOUNT_NOT_POSITIVE`, `INSUFFICIENT_FUNDS`, `DAILY_LIMIT_EXCEEDED`, `SAME_ACCOUNT`) + BL-50 IBAN (валідні/невалідні/null). Пункт 5 відхилено з обґрунтуванням — `assertThrows` не застосовний до методів, що повертають значення. `dailyLimitBoundaryCheck` коректно ловить дефект BL-40 (відхиляє рівно 50 000).

---

## 3. LoanCalculatorTest.java

- **Автор:** Пономаренко Р. В.
- **Рецензент:** Городиська А. А.

| # | Пункт чек-листа | Результат | Зауваження |
|---|---|---|---|
| 1 | Перевіряє правило BL, ID у @DisplayName | так | BL-10, BL-20, BL-30 у всіх 13 методах |
| 2 | Структура AAA | так | Чіткий поділ у кожному методі |
| 3 | Ім'я методу описує поведінку | так | `monthlyPayment_exactlyMaxRate_noException`, `totalInterest_standard_correctValue` |
| 4 | Межі з обох боків | так | P (999.99 / 1000 / 1000000 / 1000000.01), n (2 / 3 / 60 / 61), r (4.99 / 5 / 40 / 40.01) — усі з обох боків |
| 5 | `assertThrows` з конкретним класом; BigDecimal зі scale | так | `IllegalArgumentException` у 4 параметризованих методах. Порівняння зі scale (`8884.88`, `18.87`, `6618.56`) |
| 6 | Незалежність тестів | так | `@BeforeEach` — новий `LoanCalculator` |
| 7 | Немає `Thread.sleep` | N/A | Юніт-тести |
| 8 | Падаючі тести мають Issue | так | BUG-010 (#15), BUG-011 (#17), посилання в коментарях до тестів |

**Висновок:** схвалено. Очікувані значення обчислені вручну за формулою BL-10, не взяті з коду. Межі перевірено з обох боків параметризованими тестами. Виявлено 2 дефекти: BUG-010 (граничний рядок n=3) і BUG-011 (проміжне округлення). Зауважень немає.

---

## 4. LoginUiTest.java (Selenium WebDriver)

- **Автор:** Мажуга І. К.
- **Рецензент:** Пономаренко Р. В.

| # | Пункт чек-листа | Результат | Зауваження |
|---|---|---|---|
| 1 | Перевіряє крок тест-кейсу, ID у @DisplayName | так | 6 методів відповідають TC-004, TC-005, TC-007, TC-010, TC-012, TC-021. `testTransferZeroAmount` падає через дефект demo BUG-002 (#2) — це доказ правильності тесту |
| 2 | Структура AAA | так | Усі 6 сценаріїв читаються. `testTransferZeroAmount` — try-catch + `fail()` з осмисленим повідомленням |
| 3 | Ім'я методу описує поведінку | так | `testValidLogin`, `testInvalidPasswordLogin`, `testTransferFundsSuccess`, `testTransferZeroAmount` |
| 4 | Межі з обох сторін | так | `testTransferFundsSuccess` (500 грн, валідна сума) vs `testTransferZeroAmount` (0 грн, граничне невалідне) |
| 5 | Анотації та режими | так | `@Tag("ui")`, `ChromeOptions --headless=new`, `--no-sandbox`, `--window-size` |
| 6 | Незалежність тестів | так | `@BeforeEach` — новий `ChromeDriver`, `@AfterEach` — `driver.quit()`. Кожен тест — власний браузер |
| 7 | Немає `Thread.sleep`, явні очікування, стійкі локатори | так | `Thread.sleep` прибрано. `WebDriverWait` + `ExpectedConditions` для всіх динамічних елементів. Локатори — `By.id`, `By.name`, `#rightPanel h1.title` (без combined-selector і без абсолютних XPath) |
| 8 | Падаючі тести мають Issue | так | `testTransferZeroAmount` → BUG-002 (#2), посилання в `fail()` |

**Висновок:** схвалено. 5 із 6 тестів passed; `testTransferZeroAmount` падає обґрунтовано — виявляє дефект demo ParaBank (переказ 0 грн проводиться з «Transfer Complete»). Helper `login()` прибирає дублювання. Зауважень немає.

---

## Підсумок по ревʼю

| Клас | Findings від reviewer | Закрито | Відхилено з обґрунтуванням |
|---|---|---|---|
| `AccountLedgerTest` | 3 (A1, A2, A3) | 3 | — |
| `TransferValidatorTest` | 3 (T1, T2, T3) | 2 | 1 (T2 — `assertThrows` непридатний) |
| `LoanCalculatorTest` | 2 (L1, L2) | 1 | 1 (L2 — r=5 вже покритий) |
| `LoginUiTest` | 4 (U1, U2/U3, U4, Thread.sleep) | 4 | — |

**Загальний висновок:** усі 4 тест-класи схвалено. 12 findings опрацьовано: 10 виправлено, 2 відхилено з обґрунтуванням (пробел SRS, непридатність конструкції). Кожне падіння прив'язане до GitHub Issue (5 module + 1 demo). «Підгонів» очікуваних значень під фактичні не виявлено.

## Що автори виправили після ревʼю

| # | Зауваження | Рішення автора | Статус |
|---|---|---|---|
| A1 | `history()` з міткою BL-70 | BL-70 прибрано з `@DisplayName` — `history()` не описаний в SRS v1.1 | ✅ виправлено |
| A2 | Округлення HALF_UP не перевірялося | Додано `roundingLedger.deposit("100.005")` → `assertEquals("100.01", balance)` | ✅ виправлено |
| A3 | Немає тесту на NPE при null | Додано `testNullArgumentsThrowNullPointerException` (очікує NPE за BL-80). Підтверджено дефект v1 — BUG-012 (#18) | ✅ виправлено |
| T1 | IBAN лише з валідного боку | Додано `invalidIbanFormatReturnsFalse` (4 негативних) + `nullIbanFormatReturnsFalse` | ✅ виправлено |
| T2 | Немає `assertThrows` | Відхилено: `isValid()` повертає `boolean`, `validate()` — `List`. `assertThrows` не застосовний | ⛔ відхилено з обґрунтуванням |
| T3 | `validate()` перевіряє лише «список не порожній» | Замінено на `assertTrue(errors.contains(TransferError.XXX))` для 4 кодів | ✅ виправлено |
| L1 | Немає r=40 (max, включно) | Додано `monthlyPayment_exactlyMaxRate_noException` | ✅ виправлено |
| L2 | r=5 без valid-кейсу | Відхилено: r=5 вже в `@CsvSource` (`"1000, 5, 60, 18.87"`) | ⛔ відхилено з обґрунтуванням |
| U1 | `testTransferFundsForm` не робить переказ | Переписано як `testTransferFundsSuccess` — реальний переказ 500 грн + перевірка суми в підтвердженні | ✅ виправлено |
| U2/U3 | `assertNotNull(amountInput)` — no-op | Замінено на `try-catch` з `fail()` при появі «Transfer Complete»; підтверджено дефект demo BUG-002 (#2) | ✅ виправлено |
| U4 | Combined CSS locator | Звужено до `#rightPanel h1.title` | ✅ виправлено |
| Thread.sleep | `Thread.sleep(1500)` в `testTransferZeroAmount` | Прибрано, замінено на `WebDriverWait` + `TimeoutException` + `fail()` | ✅ виправлено |

## Фінальні цифри прогону (11.11.2026)

| Тест-клас | Methods | Executions | F | E |
|---|---|---|---|---|
| `LoanCalculatorTest` | 13 | 23 | 2 | 0 |
| `TransferValidatorTest` | 9 | 16 | 1 | 0 |
| `AccountLedgerTest` | 6 | 6 | 1 | 1 |
| **Юніт, разом** | **28** | **45** | **4** | **1** |
| `LoginUiTest` | 6 | 6 | 1 | 0 |

Дефекти: **6** (5 module: BUG-008, BUG-009, BUG-010, BUG-011, BUG-012 + 1 demo: BUG-002).
