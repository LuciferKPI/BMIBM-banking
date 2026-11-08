# Специфікація UI-сценаріїв (Спринт 4 - ParaBank)

| TC ID | Дія / Крок | Локатор (Target) | Очікуваний результат (Assert / Verify) |
| :--- | :--- | :--- | :--- |
| TC-004 | Відкрити сторінку логіну | `open /parabank/index.htm` | Сторінка завантажена |
| TC-004 | Ввести логін | `name=username` | Поле заповнено |
| TC-004 | Ввести пароль | `name=password` | Поле заповнено |
| TC-004 | Натиснути "Log In" | `css=input.button` | Заголовок сторінки дорівнює "Accounts Overview" (assert title) |
| TC-005 | Натиснути "Log In" з хибними даними | `css=input.button` | Відображення помилки "The username and password could not be verified." (verify text) |
| TC-010 | Відкрити форму переказу | `linkText=Transfer Funds` | Відкрита форма `/parabank/transfer.htm` |
| TC-010 | Ввести суму переказу | `id=amount` | Поле заповнено |
| TC-010 | Вибрати рахунок отримувача | `id=toAccountId` | Рахунок вибрано зі списку |
| TC-010 | Натиснути "Transfer" | `css=input.button` | Поява повідомлення "Transfer Complete!" (verify text) |
| TC-012 | Ввести нульову або від'ємну суму переказу | `id=amount` | Блокування переказу або повідомлення про помилку (assert text) |
| TC-013 | Ввести суму, що перевищує ліміт | `id=amount` | Відображення відмови в операції (verify text) |
