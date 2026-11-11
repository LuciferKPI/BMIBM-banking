# Специфікація UI-сценаріїв (Спринт 4 - ParaBank)

| TC ID | Дія / Крок | Локатор (Target) | Очікуваний результат (Assert / Verify) | Тест-метод |
| :--- | :--- | :--- | :--- | :--- |
| **TC-004** | Відкрити сторінку логіну (`/parabank/index.htm`)<br>Ввести валідний логін<br>Ввести валідний пароль<br>Натиснути "Увійти" | `name=username`<br>`name=password`<br>`css=input.button` | Відображається заголовок огляду рахунків (`#rightPanel h1.title` або `#leftPanel p.smallText`) | `LoginUiTest.testValidLogin` |
| **TC-005** | Відкрити сторінку логіну<br>Ввести логін<br>Ввести невалідний пароль<br>Натиснути "Увійти" | `name=username`<br>`name=password`<br>`css=input.button` | Відображається повідомлення про помилку (`p.error`), текст містить "The username and password could not be verified" | `LoginUiTest.testInvalidPasswordLogin` |
| **TC-007** | Увійти в систему<br>Натиснути "Accounts Overview" | `linkText=Accounts Overview` | Відображається таблиця рахунків клієнта (`id=accountTable`) | `LoginUiTest.testAccountsOverviewTable` |
| **TC-010** | Увійти в систему<br>Натиснути "Transfer Funds" | `linkText=Transfer Funds` | Відображається поле введення суми переказу (`id=amount`) | `LoginUiTest.testTransferFundsForm` |
| **TC-012** | Увійти в систему<br>Натиснути "Transfer Funds"<br>Ввести суму "0"<br>Натиснути "Transfer" | `linkText=Transfer Funds`<br>`id=amount`<br>`css=input.button` | Форма опрацьовує нульове значення (поле `amount` залишається видимим/доступним) | `LoginUiTest.testTransferZeroAmount` |
| **TC-021** | Увійти в систему<br>Натиснути "Log Out" | `linkText=Log Out` | Користувач повертається на сторінку з формою входу (`css=input.button`) | `LoginUiTest.testLogout` |
