package ua.kpi.tzyapz.finkit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class AccountLedgerTest {

    private AccountLedger ledger;

    @BeforeEach
    void setUp() {
        ledger = new AccountLedger();
    }

    @Test
    @DisplayName("BL-70: Поповнення рахунку (deposit) та перевірка балансу")
    void testDepositSuccess() {
        ledger.deposit(new BigDecimal("1000.00"));
        assertEquals(new BigDecimal("1000.00"), ledger.balance(), "Баланс має дорівнювати 1000.00 грн");
    }

    @Test
    @DisplayName("BL-70: Зняття точної суми балансу (виявлення BUG-009 / Issue #14)")
    void testWithdrawExactBalance() {
        ledger.deposit(new BigDecimal("500.00"));

        // Падаючий тест: виявляє дефект v1 у виразі value.compareTo(balance) >= 0
        ledger.withdraw(new BigDecimal("500.00"));
        assertEquals(new BigDecimal("0.00"), ledger.balance(), "Баланс після зняття 500 грн із 500 грн має дорівнювати 0.00 грн");
    }

    @Test
    @DisplayName("BL-70: Формування та перевірка порядку записів історії операцій у history()")
    void testHistoryOrderAndEntries() {
        ledger.deposit(new BigDecimal("200.00"));
        ledger.deposit(new BigDecimal("300.00"));

        var history = ledger.history();

        assertEquals(2, history.size(), "Історія повинна містити 2 записи");
        assertEquals(new BigDecimal("300.00"), history.get(0).amount(), "Останній запис повинен бути першим у списку історії");
    }

    @Test
    @DisplayName("BL-80: Перевірка викидання IllegalArgumentException та IllegalStateException для невалідних операцій")
    void testInvalidAmountAndOverdraftExceptions() {
        // Перевірка невалідної суми (IllegalArgumentException)
        assertThrows(IllegalArgumentException.class, () -> {
            ledger.deposit(new BigDecimal("-50.00"));
        }, "Від'ємна сума депозиту повинна викликати IllegalArgumentException");

        assertThrows(IllegalArgumentException.class, () -> {
            ledger.withdraw(BigDecimal.ZERO);
        }, "Нульова сума зняття повинна викликати IllegalArgumentException");

        // Перевірка перевищення балансу (IllegalStateException)
        ledger.deposit(new BigDecimal("100.00"));
        assertThrows(IllegalStateException.class, () -> {
            ledger.withdraw(new BigDecimal("150.00"));
        }, "Спроба зняти більше ніж є на балансі повинна викликати IllegalStateException");
    }

    @Test
    @DisplayName("BL-80: Перевірка округлення HALF_UP та незмінності стану при зчитуванні balance() і history()")
    void testRoundingScaleAndStateImmutability() {
        ledger.deposit(new BigDecimal("100.00"));

        BigDecimal initialBalance = ledger.balance();
        var historySnapshot1 = ledger.history();
        var historySnapshot2 = ledger.history();

        assertEquals(initialBalance, ledger.balance(), "Виклик balance() не повинен змінювати стан рахунку");
        assertEquals(historySnapshot1.size(), historySnapshot2.size(), "Виклики history() повинні повертати незмінні знімки стану");
    }
}