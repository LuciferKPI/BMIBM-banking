# Протокол запуску тестів — Спринт 4

**Дата:** 11.11.2026
**Виконавець:** Пономаренко Р. В. (КТ/СМ)
**Середовище:** JDK 17 (Temurin), Maven 3.9.11, Linux (Nobara/Fedora), Chrome 153 (headless)
**Команда:** `mvn clean test` (юніт-набір) + `mvn test -DexcludedGroups=none -Dtest=LoginUiTest` (UI-набір)

## Підсумок — юніт-набір (`mvn clean test`)

| Показник | Значення |
|---|---|
| Всього тестових виконань | 45 |
| Passed | 40 |
| Failed (assertion) | 4 |
| Errors (exception) | 1 |
| Skipped | 0 |
| Час виконання | 1.876 s |
| Результат | BUILD FAILURE (очікувано — є «червоні» тести на зареєстровані дефекти v1) |

## Результати по тест-класах (юніт-набір)

| Тест-клас | Tests run | Failures | Errors | Skipped |
|---|---|---|---|---|
| LoanCalculatorTest | 23 | 2 | 0 | 0 |
| AccountLedgerTest | 6 | 1 | 1 | 0 |
| TransferValidatorTest | 16 | 1 | 0 | 0 |
| **Разом** | **45** | **4** | **1** | **0** |

## Перелік падаючих тестів та їх прив'язка до дефектів

| Тест | Клас | Рядок | Дефект | GitHub Issue |
|---|---|---|---|---|
| monthlyPayment_exactlyMinMonths_noException | LoanCalculatorTest | 81 | BUG-010 | #15 |
| monthlyPayment_variousParams_correctValues[2] | LoanCalculatorTest | 52 | BUG-011 | #17 |
| testWithdrawExactBalance | AccountLedgerTest | 33 | BUG-009 | #14 |
| testNullArgumentsThrowNullPointerException | AccountLedgerTest | 74 | BUG-012 | #18 |
| dailyLimitBoundaryCheck[2] | TransferValidatorTest | 45 | BUG-008 | #11 |

## Підсумок — UI-набір (`mvn test -DexcludedGroups=none -Dtest=LoginUiTest`)

| Показник | Значення |
|---|---|
| Всього тестових виконань | 6 |
| Passed | 5 |
| Failures | 1 |
| Errors | 0 |
| Skipped | 0 |
| Час виконання | 20.36 s |
| Результат | BUILD FAILURE (очікувано — виявлено дефект demo BUG-002) |

| Тест | Результат |
|---|---|
| testValidLogin | passed |
| testInvalidPasswordLogin | passed |
| testAccountsOverviewTable | passed |
| testTransferFundsSuccess | passed |
| **testTransferZeroAmount** | **failed → BUG-002 (Issue #2, дефект demo)** |
| testLogout | passed |

## Висновок

**Юніт-набір:** усі 5 падінь відповідають зареєстрованим дефектам модуля v1 (GitHub Issues з міткою `module`): BUG-008, BUG-009, BUG-010, BUG-011, BUG-012. «Підгонів» очікуваних значень під фактичні немає. Тести залишаються «червоними» до перевірки виправлень на модулі v2 (спринт 5).

**UI-набір:** 5 із 6 методів `LoginUiTest` проходять на demo ParaBank у headless Chrome. `testTransferZeroAmount` падає обґрунтовано — ParaBank demo проводить переказ 0 грн з «Transfer Complete», що порушує TC-012. Дефект demo зареєстровано як BUG-002 (GitHub Issue #2, мітка `demo`). Покриті TC-004, TC-005, TC-007, TC-010, TC-012, TC-021.
