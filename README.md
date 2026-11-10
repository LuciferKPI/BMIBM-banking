# BMIBM-banking — автотести продукту «ФінКит»

Репозиторій командної роботи студентів групи **ФБ-44** з дисципліни  
«Технології забезпечення якості програмних засобів» (КПІ ім. Ігоря Сікорського).

**Продукт:** «ФінКит» — онлайн-банкінг для фізичних осіб (slug: `finkit`).  
**Demo-система:** [ParaBank](https://parabank.parasoft.com/parabank/index.htm).  
**Резервна система:** GlobalSQA Banking Project.  
**Досьє якості (папка команди):** https://drive.google.com/drive/folders/1jbAbiFvRA4iXGWSZh-gUgtGHIafCVJ6X

---

## 1. Склад команди та ролі

| ПІБ | GitHub | Роль у C4 | Роль у C5 |
|---|---|---|---|
| Пономаренко Роман В. | @meowchenikot | Керівник тестування + Скрам-майстер (КТ/СМ) | Тест-аналітик (ТА) |
| Городиська Анна А. | @mellmii | Тест-аналітик (ТА) | Інженер з тестування (ІТ / SDET) |
| Мажуга Ілля К. | @LuciferKPI | Інженер з тестування (ІТ / SDET) | Керівник тестування + Скрам-майстер (КТ/СМ) |

> **Примітка.** Графік ротації ролей на спринти 1–5 зафіксовано в [`docs/team/charter.md`](https://github.com/LuciferKPI/BMIBM-banking/blob/main/docs/team/charter.md). Ротація C4 → C5 для команди з 3 осіб: КТ/СМ → ТА → ІТ → КТ/СМ (циклічно).

---

## 2. Вимоги до середовища

- **JDK:** 17 (Temurin) — проєкт компілюється з `release=17`.
- **Maven:** 3.9.
- **Браузер:** Chrome або Firefox (драйвер автоматично завантажується Selenium Manager).
- **Залежності (з `pom.xml`):**
  - JUnit 5 (Jupiter 5.10.2) — `junit-jupiter`.
  - Selenium 4 (selenium-java 4.21.0).
- **IDE (рекомендовано):** IntelliJ IDEA Community.

> Selenium Manager (вбудований у Selenium 4.6+) сам визначає версію браузера й завантажує сумісний драйвер у `~/.cache/selenium`. Для першого запуску UI-тестів потрібен доступ до Інтернету.

---

## 3. Як зібрати та запустити тести

```bash
# Клонувати репозиторій
git clone https://github.com/LuciferKPI/BMIBM-banking.git
cd BMIBM-banking

# Запустити всі юніт-тести (UI-тести з тегом ui вимкнені за замовчуванням)
mvn clean test

# Запустити лише конкретний юніт-тест-клас
mvn test -Dtest=LoanCalculatorTest

# Запустити всі тести, включно з UI-сценаріями
mvn test -DexcludedGroups=none

# Запустити лише UI-тести
mvn test -DexcludedGroups=none -Dtest=*UiTest
```

**Звіти Surefire:** `target/surefire-reports/` (файли `*.txt` та `TEST-*.xml`).  
Протокол запуску (підсумок `Tests run: …`) зберігається в [`docs/sprint4/surefire-summary.md`](docs/sprint4/surefire-summary.md).

---

## 4. Структура репозиторію (спринт 4)

```text
.
├── .gitignore
├── README.md
├── pom.xml
├── .github/
│   └── ISSUE_TEMPLATE/bug_report.md        # шаблон звіту про дефект
├── docs/
│   ├── team/charter.md
│   ├── sprint1/ …                          # матеріали спринта 1
│   ├── sprint2/ …                          # SRS v1.1, реєстр дефектів вимог
│   ├── sprint3/ …                          # тест-кейси та журнал (спринт 3)
│   └── sprint4/
│       ├── coverage-matrix.md              # BL-вимога → тест → рецензент
│       ├── code-review.md                  # рецензії тест-класів
│       ├── retro.md                        # протокол ретроспективи
│       ├── surefire-summary.md             # протокол запуску
│       └── ui-scenarios.md                 # TC → кроки, локатори, тест-метод
├── selenium-ide/
│   ├── finkit.side                         # проєкт Selenium IDE
│   └── export/                             # експортовані Java-чернетки
└── src/
    ├── main/java/ua/kpi/tzyapz/finkit/     # модуль бізнес-логіки v1
    │   ├── AccountLedger.java
    │   ├── LedgerEntry.java
    │   ├── LoanCalculator.java
    │   ├── TransferError.java
    │   └── TransferValidator.java
    └── test/java/ua/kpi/tzyapz/finkit/
        ├── AccountLedgerTest.java
        ├── LoanCalculatorTest.java
        ├── TransferValidatorTest.java
        └── ui/
            └── LoginUiTest.java
```

> **Примітка.** Назви UI-тест-класів наведено орієнтовно. Перелік обраних UI-сценаріїв — у [`docs/sprint4/ui-scenarios.md`](docs/sprint4/ui-scenarios.md).

---

## 5. Що покрито тестами (станом на спринт 4)

### 5.1. Юніт-тести (JUnit 5) — 34 виконання

| Тест-клас | Вимога SRS | К-сть виконань | Автор |
|---|---|---|---|
| `LoanCalculatorTest` | BL-10, BL-20, BL-30 | 22 | Пономаренко Р. В. |
| `TransferValidatorTest` | BL-40, BL-50 | 7 | Городиська А. А. |
| `AccountLedgerTest` | BL-70, BL-80 | 5 | Мажуга І. К. |

> **Джерело очікуваних значень:** розділ 5 «Вимоги до бізнес-логіки» у виправленій SRS v1.1. Значення обчислено вручну за формулами BL-10, BL-20, BL-30, BL-40, BL-50, BL-70, BL-80, а не взяті з поведінки коду.

### 5.2. UI-тести (Selenium WebDriver 4)

| Тест-клас | Тест-кейс (спринт 3) | Вимога SRS | К-сть методів | Автор |
|---|---|---|---|---|
| `LoginUiTest` (вхід клієнта) | TC-004, TC-005 | F-20, GUI-10 | 6 | Мажуга І. К. |

> **Примітка.** Усі UI-тести позначені `@Tag("ui")` і за замовчуванням виключені з `mvn test`. Для запуску — `mvn test -DexcludedGroups=none`. Кожен тест відкриває власний браузер у `@BeforeEach` і закриває у `@AfterEach` (`driver.quit()`).

### 5.3. Selenium IDE

- Проєкт: 
`selenium-ide/finkit.side`
- Сценарії: «Авторизація користувача в ParaBank (Login)»
- Експорт у Java: 
`selenium-ide/export/ParaBankLoginTest.java` (чернетки, не входять до збірки)

---

## 6. Дефекти модуля (v1) — 4 знайдені

Знайдені під час юніт-тестування дефекти оформлено у **GitHub Issues** з мітками `module`, `severity:*`, `priority:*`, `status:new`.

- **Шаблон звіту:** [`.github/ISSUE_TEMPLATE/bug_report.md`](https://github.com/LuciferKPI/BMIBM-banking/blob/main/.github/ISSUE_TEMPLATE/bug_report.md).
- **Фільтр Issues:** [`label:module`](https://github.com/LuciferKPI/BMIBM-banking/issues?q=label%3Amodule)

| ID | Тест | Клас | Вимога | Severity |
|---|---|---|---|---|
| BUG-010 | `monthlyPayment_exactlyMinMonths_noException` | LoanCalculatorTest | BL-30 | Major |
| BUG-011 | `monthlyPayment_variousParams_correctValues[2]` | LoanCalculatorTest | BL-10 | Minor |
| BUG-009 | `testWithdrawExactBalance` | AccountLedgerTest | BL-70 | Major |
| BUG-008 | `dailyLimitBoundaryCheck[2]` | TransferValidatorTest | BL-40 | Major |

> **Примітка.** Кожен «червоний» юніт-тест залишається в наборі до спринта 5, де перевіряється на версії v2 (closed / reopened). У коментарі до тесту — ID звіту.

---

## 7. Версії модуля бізнес-логіки

- **v1 (спринт 4):** `src/main/java/ua/kpi/tzyapz/finkit/` (п'ять класів: `LoanCalculator`, `TransferValidator`, `AccountLedger`, `TransferError`, `LedgerEntry`).  
  Результат `mvn clean test`: **34 tests run, 3 Failures, 1 Error, 30 Passed** — 4 «червоні» тести на зареєстровані дефекти.
- **v2 (спринт 5):** _[буде додано розробниками]_. Після заміни теки класів тести не змінюються; результати порівнюються для верифікації виправлень і пошуку регресії.

---

## 8. Нормативи для команди з 3 осіб (спринт 4)

| Показник | Норматив | Факт |
|---|---|---|
| Юніт-тести JUnit 5 (кожен параметризований метод = 1 тест) | ≥ 12 | 34 |
| UI-сценарії WebDriver (1 сценарій = 1 тест-метод) | ≥ 6 | 6 |
| Знайдені дефекти модуля (звіт + тест на кожен) | ≥ 3 | 4 |
| Юніт-тести на кожного члена команди (за комітами) | ≥ 3 | 22 / 7 / 5 |
| Сценарії, записані в Selenium IDE | 1–2 | 1 |

---

## 9. Корисні посилання

- **Матриця покриття BL-вимог:** [`docs/sprint4/coverage-matrix.md`](docs/sprint4/coverage-matrix.md)
- **Протокол запуску (Surefire):** [`docs/sprint4/surefire-summary.md`](docs/sprint4/surefire-summary.md)
- **Code review тест-класів:** [`docs/sprint4/code-review.md`](docs/sprint4/code-review.md)
- **Протокол ретроспективи:** [`docs/sprint4/retro.md`](docs/sprint4/retro.md)
- **Перелік UI-сценаріїв і локаторів:** [`docs/sprint4/ui-scenarios.md`](docs/sprint4/ui-scenarios.md)
- **Звіти про дефекти demo (спринт 3):** GitHub Issues з міткою `sprint:3`
- **Звіти про дефекти модуля (спринт 4):** GitHub Issues з міткою `module`
- **Виправлена SRS v1.1:** [`docs/sprint2/SRS-1.1.md`](docs/sprint2/SRS-1.1.md)

---

## 10. Примітки

1. **Дані для UI-тестів.** ParaBank не надає готових облікових записів; у тестах користувач створюється через реєстрацію (наприклад, у `@BeforeAll` або на початку кожного тесту). Використовуються лише вигадані тестові дані з префіксом команди (`BMIBM_`).
2. **Стабільність UI-тестів.** Усі очікування — явні (`WebDriverWait` + `ExpectedConditions`); `Thread.sleep` та `implicitlyWait` не використовуються. Локатори — за `id`, `name` або `data-test`, без абсолютних XPath.
3. **Code review.** Кожен тест-клас пройшов рецензування за чек-листом 5.3 методички; позначки рецензента — у [`docs/sprint4/code-review.md`](docs/sprint4/code-review.md).
4. **README оновлюється** у спринті 5 за результатами регресії на версії v2.

---

_Останнє оновлення: 10 листопада 2026._
