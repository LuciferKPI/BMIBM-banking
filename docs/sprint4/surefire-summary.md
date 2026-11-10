# Протокол запуску тестів — Спринт 4

**Дата:** 10.11.2026
**Виконавець:** Пономаренко Р. В. (КТ/СМ)
**Середовище:** JDK, Maven 3.9.11, Linux (Nobara/Fedora)
**Команда:** `mvn clean test`

## Підсумок

| Показник | Значення |
|---|---|
| Всього тестових виконань | 34 |
| Passed | 30 |
| Failed (assertion) | 3 |
| Errors (exception) | 1 |
| Skipped | 0 |
| Час виконання | 3.041 s |
| Результат | BUILD FAILURE (очікувано — є «червоні» тести на зареєстровані дефекти v1) |

## Результати по тест-класах

| Тест-клас | Tests run | Failures | Errors | Skipped |
|---|---|---|---|---|
| LoanCalculatorTest | 22 | 2 | 0 | 0 |
| AccountLedgerTest | 5 | 0 | 1 | 0 |
| TransferValidatorTest | 7 | 1 | 0 | 0 |
| **Разом** | **34** | **3** | **1** | **0** |

## Перелік падаючих тестів та їх прив'язка до дефектів

| Тест | Клас | Рядок | Дефект | GitHub Issue |
|---|---|---|---|---|
| monthlyPayment_exactlyMinMonths_noException | LoanCalculatorTest | 81 | BUG-010 | #15 |
| monthlyPayment_variousParams_correctValues[2] | LoanCalculatorTest | 52 | BUG-011 | #17 |
| testWithdrawExactBalance | AccountLedgerTest | 37 | BUG-009 | #14 |
| dailyLimitBoundaryCheck[2] | TransferValidatorTest | 47 | BUG-008 | #11 |

## Висновок

Усі 4 падіння відповідають зареєстрованим дефектам модуля v1 (GitHub Issues з міткою `module`). «Підгонів» очікуваних значень під фактичні немає. Тести залишаються «червоними» до перевірки виправлень на модулі v2 (спринт 5).

