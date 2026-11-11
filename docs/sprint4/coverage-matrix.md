# Матриця покриття бізнес-логіки (BL Coverage Matrix)

| ID Вимоги | Правило BL | Клас | Тест-метод | Рецензент | Статус |
| :--- | :--- | :--- | :--- | :--- | :--- |
| BL-40 | Переказ коштів: Денний ліміт 50 000 грн (межа) | `TransferValidatorTest` | `dailyLimitBoundaryCheck` | Пономаренко Р. / Мажуга І. | Failed (Issue #11) |
| BL-50 | Переказ коштів: Формат IBAN валідується | `TransferValidatorTest` | `validIbanFormatReturnsTrue` | Пономаренко Р. / Мажуга І. | Passed |
| BL-40 | Переказ коштів: Від'ємні суми заборонені | `TransferValidatorTest` | `negativeTransferAmountIsInvalid` | Пономаренко Р. / Мажуга І. | Passed |
| BL-30 | Кредитний калькулятор: Мін. строк 3 місяці (нижня межа) | `LoanCalculatorTest` | `monthlyPayment_exactlyMinMonths_noException` | Городиська А. / Мажуга І. | Failed (BUG-010 / Issue #15) |
| BL-10 | Кредитний калькулятор: Точність формули ануїтетного платежу | `LoanCalculatorTest` | `monthlyPayment_variousParams_correctValues` | Городиська А. / Мажуга І. | Failed (BUG-011 / Issue #17) |
| BL-70 | Рахунок: Зняття точної суми поточного балансу | `AccountLedgerTest` | `testWithdrawExactBalance` | Городиська А. / Пономаренко Р. | Failed (BUG-009 / Issue #14) |
| BL-70 | Рахунок: Поповнення балансу (deposit) | `AccountLedgerTest` | `testDepositSuccess` | Городиська А. / Пономаренко Р. | Passed |
| TC-001 | UI: Перевірка заголовку головної сторінки | `LoginUiTest` | `testPageTitle` | Городиська А. | Passed |
| TC-004 | UI: Наявність елементів форми авторизації | `LoginUiTest` | `testLoginPageFormPresent` | Городиська А. | Passed |
| TC-005 | UI: Спроба входу з некоректними даними | `LoginUiTest` | `testInvalidPasswordLoginThrowsError` | Городиська А. | Passed |
