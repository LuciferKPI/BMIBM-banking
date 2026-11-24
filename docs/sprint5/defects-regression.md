# Звіт про регресійне тестування та верифікацію дефектів (Спринт 5)

**Команда:** BMIBM-banking  
**Продукт:** «ФінКит» (`ua.kpi.tzyapz.finkit`, оновлення `v1 → v2`) + demo-застосунок ParaBank (`https://parabank.parasoft.com`)  
**Дата виконання:** 23–24.11.2026  
**Виконавці:** Городиська А. А. (`@mellmii`, ІТ — автотести JUnit 5 + Selenium WebDriver, ручні `TC-001`…`TC-013`, верифікація дефектів), Пономаренко Р. В. (`@meowchenikot`, ТА — ручні `TC-014`…`TC-027`, верифікація `#4`, `#5`)  
**Пов'язані артефакти:**
* Базові звіти Surefire `v1`: `docs/sprint5/surefire-v1/`
* Звіти Surefire `v2` (JUnit 5 + WebDriver): `docs/sprint5/surefire-v2/`
* Зведений журнал регресії: `docs/sprint5/regression-log.xlsx`

---

## 1. Зведення результатів регресійного тестування (`v1 → v2`)

Код автотестів у `src/test/java/` під час Спринта 5 **не змінювався** для забезпечення коректного порівняння «до/після».

| Категорія тестів | Усього | v1 Passed | v1 Failed/Error | v1 Blocked | v2 Passed | v2 Failed/Error | v2 Blocked | v2 Pass Rate (без Blocked) |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Юніт-тести JUnit 5 (модуль `finkit`) | 45 | 40 | 5 | 0 | 41 | 4 | 0 | **91.1%** (41/45) |
| UI-автотести Selenium WebDriver (`LoginUiTest`) | 6 | 5 | 1 | 0 | 4 | 2 | 0 | **66.7%** (4/6) |
| Ручні кейси ParaBank (`TC-001`…`TC-013`, `@mellmii`) | 13 | 9 | 4 | 0 | 8 | 5 | 0 | **61.5%** (8/13) |
| Ручні кейси ParaBank (`TC-014`…`TC-027`, `@meowchenikot`) | 14 | 10 | 2 | 2 | 11 | 2 | 1 | **84.6%** (11/13) |
| **Усього по Спринту 5** | **78** | **64** | **12** | **2** | **64** | **13** | **1** | **83.1%** (64/77) |

### Матриця переходів (`v1 → v2`)

* **`Passed → Passed` (стабільні):** **61** (39 JUnit 5 + 4 UI + 18 ручних) — регресії не виявлено.
* **`Failed → Passed` (виправлено / `Verified`):** **2** (обидва у `LoanCalculatorTest`: `BUG-010 #15`, `BUG-011 #17`). Клас `LoanCalculatorTest` на `v2` проходить на 100% (23/23).
* **`Failed/Error → Failed/Error` (не виправлено):** **10** (3 юніт-тести модуля — `Reopened`; 1 UI-тест + 6 ручних кейсів demo ParaBank — підтверджено `Open`).
* **`Passed → Failed/Error` (РЕГРЕСІЯ):** **3** — 1 нова регресія модуля `finkit v2` (`BUG-014 #34`) та 1 критична регресія авторизації demo ParaBank (`BUG-013 #33`), зафіксована одночасно у ручному `TC-005` та UI-автотесті `LoginUiTest.testInvalidPasswordLogin`.
* **`Blocked → Passed / Blocked`:** **2** — `TC-026` розблоковано та успішно пройдено (`Blocked → Passed`: за наявності коштів на перший внесок $100 000 заявку на $1 000 000 схвалено); `TC-027` залишився `Blocked → Blocked` через нестачу коштів на перший внесок після списання в `TC-026`.

---

## 2. Верифікація раніше відкритих дефектів (`#1–#7`, `#11`, `#14`, `#15`, `#17`, `#28`)

| Дефект / Issue | Об'єкт | Пов'язаний тест / TC | v1 | v2 | Статус у С5 | Висновок верифікації (24.11.2026) |
| :--- | :--- | :--- | :---: | :---: | :--- | :--- |
| **BUG-010 (`#15`)** | `finkit` (`LoanCalculator`) | `monthlyPayment_exactlyMinMonths_noException` | Failed | **Passed** | **Verified** | Виправлено: `months=3` (BL-30) більше не кидає `IllegalArgumentException`. |
| **BUG-011 (`#17`)** | `finkit` (`LoanCalculator`) | `monthlyPayment_variousParams_correctValues[2]` | Failed | **Passed** | **Verified** | Виправлено: платіж для 1000 грн, 5%, 60 міс дорівнює `18.87` (у `v1` було `18.89`). |
| **BUG-008 (`#11`)** | `finkit` (`TransferValidator`) | `dailyLimitBoundaryCheck[2]` | Failed | Failed | **Reopened** | Не виправлено: межа денного ліміту 50 000 грн (BL-40) досі повертає `false` замість `true`. |
| **BUG-009 (`#14`)** | `finkit` (`AccountLedger`) | `testWithdrawExactBalance` | Error | Error | **Reopened** | Не виправлено: зняття точної суми балансу кидає `IllegalStateException` (`AccountLedger.java:40`). |
| **BUG-012 (`#28`)** | `finkit` (`AccountLedger`) | `testNullArgumentsThrowNullPointerException` | Failed | Failed | **Reopened** | Не виправлено: `deposit(null)` кидає `IllegalArgumentException` замість `NullPointerException` (`AccountLedger.java:66`). |
| **BUG-001 (`#1`)** | Demo ParaBank | `TC-011` | Failed | Failed | **Open** | Підтверджено: дозволено переказ суми, що перевищує баланс (овердрафт). |
| **BUG-002 (`#2`)** | Demo ParaBank | `TC-012` / `testTransferZeroAmount` | Failed | Failed | **Open** | Підтверджено вручну та UI-автотестом: переказ `0 грн` завершується `'Transfer Complete'`. |
| **BUG-003 (`#3`)** | Demo ParaBank | `TC-013` | Failed | Failed | **Open** | Підтверджено: дозволено переказ `50 001 грн` при денному ліміті `50 000 грн`. |
| **BUG-004 (`#4`)** | Demo ParaBank | `TC-020` | Failed | Failed | **Open** | Підтверджено (`@meowchenikot`): заявку на кредит `999 грн` схвалено при мінімумі `1 000 грн`. |
| **BUG-005 (`#5`)** | Demo ParaBank | `TC-021` | Failed | Failed | **Open** | Підтверджено (`@meowchenikot`): після `Log Out` прямий перехід на `overview.htm` виводить системну помилку. |
| **BUG-006 (`#6`)** | Demo ParaBank | `TC-003` | Failed | Failed | **Open** | Підтверджено: поля `Password` та `Confirm` очищуються при помилці валідації форми реєстрації. |
| **BUG-007 (`#7`)** | Demo ParaBank | `TC-003` | Failed | **Passed** | **Closed (not reproducible)** | Не відтворюється на `v2`: кирилиця («Іваненко») після помилки валідації відображається коректно без HTML-ентіті. |

---

## 3. Нові дефекти та регресії, виявлені у Спринті 5

### BUG-014 (`#34`): AccountLedger повертає історію операцій у хибному порядку — регресія v2 (порушення BL-80)

* **GitHub Issue:** `#34`
* **Severity:** Major
* **Priority:** P2
* **Status:** Open (`Regression v2`)
* **Environment:** модуль `finkit v2`, Java 25 (компіляція `--release 17`), Maven Surefire 3.2.5, Windows 11
* **Linked TC / requirement:** BL-80 (SRS v1.1); юніт-тест `AccountLedgerTest#testHistoryOrderAndEntries`
* **Reporter:** Городиська А. А., 24.11.2026
* **Reviewer:** Пономаренко Р. В., 24.11.2026

#### Description
Після оновлення модуля `finkit` до версії `v2` виявлено **регресію**: метод отримання історії операцій класу `AccountLedger` перестав повертати останню виконану операцію першою у списку.  
На базовій версії **`finkit v1`** тест `AccountLedgerTest.testHistoryOrderAndEntries` проходив успішно (**`Passed`**, див. `docs/sprint5/surefire-v1/ua.kpi.tzyapz.finkit.AccountLedgerTest.txt`). Після переходу на `v2` результат змінився на **`Failed`** (`Passed → Failed`).  
**Severity = Major**, оскільки дефект спотворює порядок відображення фінансових операцій клієнта і є прямою регресією оновлення `v2` (порушення контракту `BL-80`); **Priority = P2**, бо базові розрахунки балансу працюють, але некоректна історія потребує виправлення до релізу.

#### Steps to reproduce
1. Запустити юніт-тест на модулі `finkit v2`:
   ```bash
   mvn test "-Dtest=AccountLedgerTest#testHistoryOrderAndEntries"
   ```

#### Actual result
Тест завершується помилкою `AssertionFailedError` (`AccountLedgerTest.java:47`) — першим елементом історії повертається попередній запис (`200.00`) замість останнього (`300.00`):
```text
org.opentest4j.AssertionFailedError: Останній запис повинен бути першим у списку історії ==> expected: <300.00> but was: <200.00>
    at ua.kpi.tzyapz.finkit.AccountLedgerTest.testHistoryOrderAndEntries(AccountLedgerTest.java:47)
```

#### Expected result
Останній запис операції (`300.00`) повинен бути першим у списку історії `AccountLedger`, як це працювало у версії `finkit v1` (`Passed`).

#### Attachments
* `docs/sprint5/surefire-v1/ua.kpi.tzyapz.finkit.AccountLedgerTest.txt` (тест `Passed`)
* `docs/sprint5/surefire-v2/ua.kpi.tzyapz.finkit.AccountLedgerTest.txt` (тест `Failed`)

---

### BUG-013 (`#33`): ParaBank дозволяє вхід із некоректним паролем без повідомлення про помилку (TC-005, порушення F-20)

* **GitHub Issue:** `#33`
* **Severity:** Critical
* **Priority:** P1
* **Status:** Open (`New Demo Regression`)
* **Environment:** demo `https://parabank.parasoft.com` (24.11.2026); Google Chrome 157.0.8103.79 (ChromeDriver 157.0.8103.78, Selenium 4.21.0); Windows 11 (amd64); облікові дані `BMIBM_user1` / `WrongPass!`
* **Linked TC / requirement:** `TC-005` / F-20 (SRS v1.1); UI-автотест `LoginUiTest#testInvalidPasswordLogin`
* **Reporter:** Городиська А. А., 24.11.2026
* **Reviewer:** Пономаренко Р. В., 24.11.2026

#### Description
Під час регресійного тестування Спринта 5 на поточному demo-застосунку ParaBank виявлено критичну зміну поведінки автентифікації: вхід із некоректним паролем (`BMIBM_user1` / `WrongPass!`) не відхиляється. Замість відображення помилки `p.error` система авторизує користувача та відкриває кабінет клієнта `John Smith` (`overview.htm`).  
У Спринтах 3–4 (v1) `TC-005` та `LoginUiTest.testInvalidPasswordLogin` проходили успішно (**`Passed`**). Перехід: `Passed → Failed` (ручний) / `Passed → Error` (UI-автотест).  
**Severity = Critical**, оскільки порушено базову функцію безпеки та автентифікації клієнта (`F-20`); **Priority = P1**, бо некоректні облікові дані надають доступ до чужого банківського кабінету та блокують проходження UI-автотесту.

#### Steps to reproduce
1. Відкрити `https://parabank.parasoft.com` у Google Chrome (нова сесія / інкогніто).
2. У блоці «Customer Login» ввести `Username`: `BMIBM_user1`, `Password`: `WrongPass!`.
3. Натиснути кнопку **Log In**.

Або запустити UI-автотест:
```bash
mvn test "-DexcludedGroups=none" "-Dtest=LoginUiTest#testInvalidPasswordLogin"
```

#### Actual result
* **Вручну (Chrome):** повідомлення про помилку відсутнє, відкривається `https://parabank.parasoft.com/parabank/overview.htm` із привітанням `Welcome John Smith` та таблицею рахунків.
* **Автотест (`LoginUiTest.java:61`):** падає по таймауту очікування елемента `p.error`:
  ```text
  org.openqa.selenium.TimeoutException: Expected condition failed: waiting for visibility of element located by By.cssSelector: p.error (tried for 15 second(s) with 500 milliseconds interval)
  ```

#### Expected result
Вхід із некоректним паролем відхиляється; користувач залишається на сторінці входу з повідомленням `p.error`: `The username and password could not be verified.` (вимога `F-20`, `TC-005`).

#### Attachments
* `docs/sprint5/surefire-v2/ua.kpi.tzyapz.finkit.ui.LoginUiTest.txt`
