# Матриця покриття бізнес-логіки (BL Coverage Matrix)

| ID Вимоги | Правило BL / UI | Клас | Тест-метод | Рецензент | Статус |
| :--- | :--- | :--- | :--- | :--- | :--- |
| BL-40 | Переказ коштів: Денний ліміт 50 000 грн (межа) | `TransferValidatorTest` | `dailyLimitBoundaryCheck` | Пономаренко Р. / Мажуга І. | Failed (Issue #11) |
| BL-50 | Переказ коштів: Формат IBAN валідується | `TransferValidatorTest` | `validIbanFormatReturnsTrue` | Пономаренко Р. / Мажуга І. | Passed |
| BL-40 | Переказ коштів: Від'ємні суми заборонені | `TransferValidatorTest` | `negativeTransferAmountIsInvalid` | Пономаренко Р. / Мажуга І. | Passed |
| BL-30 | Кредитний калькулятор: Мін. строк 3 місяці (нижня межа) | `LoanCalculatorTest` | `monthlyPayment_exactlyMinMonths_noException` | Городиська А. / Мажуга І. | Failed (BUG-010 / Issue #15) |
| BL-10 | Кредитний калькулятор: Точність формули ануїтетного платежу | `LoanCalculatorTest` | `monthlyPayment_variousParams_correctValues` | Городиська А. / Мажуга І. | Failed (BUG-011 / Issue #17) |
| BL-70 | Рахунок: Зняття точної суми поточного балансу | `AccountLedgerTest` | `testWithdrawExactBalance` | Городиська А. / Пономаренко Р. | Failed (BUG-009 / Issue #14) |
| BL-70 | Рахунок: Поповнення балансу (deposit) | `AccountLedgerTest` | `testDepositSuccess` | Городиська А. / Пономаренко Р. | Passed |
| BL-80 | Рахунок: Перевірка NullPointerException для null-аргументів | `AccountLedgerTest` | `testNullArgumentsThrowException` | Городиська А. / Пономаренко Р. | Passed |
| TC-004 | UI: Вхід клієнта у кабінет з коректним логіном та паролем | `LoginUiTest` | `testValidLogin` | Городиська А. | Passed |
| TC-005 | UI: Неуспішний вхід клієнта з некоректним паролем | `LoginUiTest` | `testInvalidPasswordLogin` | Городиська А. | Passed |
| TC-007 | UI: Перегляд сторінки «Огляд рахунків» та підсумкової суми | `LoginUiTest` | `testAccountsOverviewTable` | Городиська А. | Passed |
| TC-010 | UI: Успішне відкриття форми переказу коштів між рахунками | `LoginUiTest` | `testTransferFundsForm` | Городиська А. | Passed |
| TC-012 | UI: Негативна перевірка переказу коштів з нульовою сумою | `LoginUiTest` | `testTransferZeroAmount` | Городиська А. | Passed |
| TC-021 | UI: Завершення сеансу роботи (Log Out) та перевірка виходу | `LoginUiTest` | `testLogout` | Городиська А. | Passed |
