# BMIBM-banking — автотести продукту «ФінКит»

Репозиторій командної роботи студентів групи **ФБ-44** з дисципліни  
«Технології забезпечення якості програмних засобів» (КПІ ім. Ігоря Сікорського).

**Продукт:** «ФінКит» — онлайн-банкінг для фізичних осіб (slug: `finkit`).  
**Demo-система:** [ParaBank](https://parabank.parasoft.com/parabank/index.htm).  
**Резервна система:** GlobalSQA Banking Project.  
**Досьє якості (папка команди):** https://drive.google.com/drive/folders/1jbAbiFvRA4iXGWSZh-gUgtGHIafCVJ6X?usp=sharing

---

## 1. Склад команди та ролі у спринті 4

| ПІБ | GitHub | Роль у C4 | Роль у C5 |
|---|---|---|---|
| Пономаренко Роман В. | @meowchenikot | Керівник тестування + Скрам-майстер (КТ/СМ) | 
| Городиська Анна А. | @mellmii| Тест-аналітик (ТА) |
| Мажуга Ілля К. | @LuciferKPI | Інженер з тестування (ІТ / SDET) | 

> **Примітка.** Графік ротації ролей на спринти 1–5 зафіксовано в [`docs/team/charter.md`](https://github.com/LuciferKPI/BMIBM-banking/blob/main/docs/team/charter.md). Для спринта 5 роль кожного члена команди визначається згідно з цим графіком (у команді з 3 осіб КТ/СМ поєднується).

---

## 2. Вимоги до середовища

- **JDK:** 17 (Temurin).
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
mvn test

# Запустити лише конкретний юніт-тест-клас
mvn test -Dtest=LoanCalculatorTest

# Запустити всі тести, включно з UI-сценаріями
mvn test -DexcludedGroups=none

# Запустити лише UI-тести
mvn test -DexcludedGroups=none -Dtest=*UiTest
```

**Звіти Surefire:** `target/surefire-reports/` (файли `*.txt` та `TEST-*.xml`).  
Протокол запуску (підсумок `Tests run: …`) зберігається в _[`docs/sprint4/surefire-summary.md`]_ — _[посилання буде додано після першого повного прогону]_.

---

## 4. Структура репозиторію (спринт 4)

```
.
├── .gitignore
├── README.md
├── pom.xml
├── .github/
│   └── ISSUE_TEMPLATE/bug_report.md        # шаблон звіту про дефект
├── docs/
│   ├── team/charter.md
│   ├── sprint1/ …                          # матеріали спринта 1
│   ├── sprint3/ …                          # тест-кейси та журнал (спринт 3)
│   └── sprint4/                            # артефакти спринта 4
│       ├── coverage-matrix.md              # BL-вимога → тест → рецензент
│       ├── surefire-summary.md             # протокол запуску
│       └── ui-scenarios.md                 # TC → кроки, локатори, тест-метод
├── selenium-ide/
│   ├── finkit.side                         # проєкт Selenium IDE (1–2 сценарії)
│   └── export/                             # експортовані Java-чернетки
└── src/
    ├── main/java/ua/kpi/tzyapz/finkit/     # модуль бізнес-логіки v1
    │   ├── LoanCalculator.java
    │   ├── TransferValidator.java
    │   └── AccountLedger.java
    └── test/java/ua/kpi/tzyapz/finkit/
        ├── LoanCalculatorTest.java
        ├── TransferValidatorTest.java
        ├── AccountLedgerTest.java
        └── ui/
            ├── ParaBankLoginUiTest.java
            ├── ParaBankTransferUiTest.java
            ├── ParaBankLoanUiTest.java
            └── ParaBankBillPayUiTest.java
```

> **Примітка.** Назви UI-тест-класів наведено орієнтовно; вони мають відповідати фактичній реалізації. Перелік обраних UI-сценаріїв та відповідних тест-кейсів — у `docs/sprint4/ui-scenarios.md`.

---

## 5. Що покрито тестами (станом на спринт 4)

### 5.1. Юніт-тести (JUnit 5)

| Тест-клас / метод | Вимога SRS | Тест-кейс (за потреби) | Автор |
|---|---|---|---|
| `LoanCalculatorTest` (параметризовані + граничні) | BL-10, BL-20, BL-30 | — | _[заповнити]_ |
| `TransferValidatorTest` (межі, коди помилок) | BL-40, BL-50 | — | _[заповнити]_ |
| `AccountLedgerTest` (deposit / withdraw, винятки) | BL-70, BL-80 | — | _[заповнити]_ |

> **Джерело очікуваних значень:** розділ 5 «Вимоги до бізнес-логіки» у виправленій SRS v1.1. Очікувані значення обчислено вручну за формулами BL-10, BL-20, BL-30, BL-40, BL-50, BL-70, BL-80, а не взяті з поведінки коду.

### 5.2. UI-тести (Selenium WebDriver 4)

| Тест-клас / метод | Тест-кейс (спринт 3) | Вимога SRS | Автор |
|---|---|---|---|
| `ParaBankLoginUiTest` (успішний / неуспішний вхід) | TC-004, TC-005 | F-20, GUI-10 | _[заповнити]_ |
| `ParaBankTransferUiTest` (переказ, ліміти, помилки) | TC-010, TC-011, TC-012, TC-013 | F-70, BL-40 | _[заповнити]_ |
| `ParaBankLoanUiTest` (межі кредиту) | TC-019, TC-020, TC-025…TC-027 | F-110, BL-30 | _[заповнити]_ |
| `ParaBankBillPayUiTest` (оплата рахунку) | TC-014, TC-015 | F-80, GUI-60 | _[заповнити]_ |

> **Примітка.** Усі UI-тести позначені `@Tag("ui")` і за замовчуванням виключені з `mvn test`. Для запуску використовуйте `mvn test -DexcludedGroups=none`. Кожен тест відкриває власний браузер у `@BeforeEach` і закриває його в `@AfterEach` (`driver.quit()`).

### 5.3. Selenium IDE

- Проєкт: `selenium-ide/finkit.side`.
- Сценарії: _[вказати назви, наприклад «Вхід клієнта», «Переказ коштів»]_.
- Експорт у Java: `selenium-ide/export/` (чернетки, не входять до збірки).

---

## 6. Дефекти модуля (v1)

Знайдені під час юніт-тестування дефекти модуля оформлено за шаблоном спринта 3 у **GitHub Issues** з мітками `module`, `severity:*`, `priority:*`, `status:*`.

- **Шаблон звіту:** [`.github/ISSUE_TEMPLATE/bug_report.md`](https://github.com/LuciferKPI/BMIBM-banking/blob/main/.github/ISSUE_TEMPLATE/bug_report.md).
- **Поточний перелік:** _[посилання на фільтр Issues, наприклад `is:issue label:module`]_.
- **Мінімальна кількість для команди з 3 осіб:** ≥ 3 дефекти (з 6 закладених у модулі v1).

> **Примітка.** Кожен «червоний» юніт-тест, що фіксує дефект, залишається в наборі до спринта 5, де перевіряється на версії v2 (closed / reopened). У коментарі до тесту вказується ID звіту про дефект.

---

## 7. Версії модуля бізнес-логіки

- **v1 (спринт 4):** `src/main/java/ua/kpi/tzyapz/finkit/` (три класи: `LoanCalculator`, `TransferValidator`, `AccountLedger`). Тег: _[`v1-sprint4`]_. Результат `mvn test`: _[заповнити після повного прогону: N passed / M failed]_.
- **v2 (спринт 5):** _[буде додано розробниками]_. Тег: _[`v2-sprint5`]_. Після заміни теки класів тести не змінюються; результати порівнюються для верифікації виправлень і пошуку регресії.

---

## 8. Нормативи для команди з 3 осіб (спринт 4)

| Показник | Норматив | Факт |
|---|---|---|
| Юніт-тести JUnit 5 (кожен параметризований метод = 1 тест) | ≥ 12 | _[заповнити]_ |
| UI-сценарії WebDriver (1 сценарій = 1 тест-метод) | ≥ 6 | _[заповнити]_ |
| Знайдені дефекти модуля (звіт + тест на кожен) | ≥ 3 | _[заповнити]_ |
| Юніт-тести на кожного члена команди (за комітами) | ≥ 3 | _[заповнити]_ |
| Сценарії, записані в Selenium IDE | 1–2 | _[заповнити]_ |

---

## 9. Корисні посилання

- **Матриця покриття BL-вимог:** `docs/sprint4/coverage-matrix.md` _(буде додано)_.
- **Протокол запуску (Surefire):** `docs/sprint4/surefire-summary.md` _(буде додано)_.
- **Перелік UI-сценаріїв і локаторів:** `docs/sprint4/ui-scenarios.md` _(буде додано)_.
- **Звіти про дефекти demo (спринт 3):** GitHub Issues з міткою `sprint:3`.
- **Виправлена SRS v1.1:**` docs/sprint2/SRS-1.1.md`

---

## 10. Примітки

1. **Дані для UI-тестів.** ParaBank не надає готових облікових записів; у тестах користувач створюється через реєстрацію (наприклад, у `@BeforeAll` або на початку кожного тесту). Використовуються лише вигадані тестові дані з префіксом команди (`BMIBM_`).
2. **Стабільність UI-тестів.** Усі очікування — явні (`WebDriverWait` + `ExpectedConditions`); `Thread.sleep` та `implicitlyWait` не використовуються. Локатори — за `id`, `name` або `data-test` (де доступні), без абсолютних XPath.
3. **Code review.** Кожен тест-клас пройшов рецензування за чек-листом 5.3 методички; позначки рецензента — у матриці покриття.
4. **README оновлюється** у спринті 5 за результатами регресії на версії v2.

---

_Останнє оновлення: 8 листопада 2026._
