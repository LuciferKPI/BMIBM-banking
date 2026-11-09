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
    @DisplayName("BL-70: Успішне поповнення рахунку (deposit) та перевірка балансу")
    void testDepositSuccess() {
        // Arrange & Act
        ledger.deposit(new BigDecimal("1000.00"));

        // Assert
        assertEquals(new BigDecimal("1000.00"), ledger.balance(), "Баланс має дорівнювати 1000.00 грн");
    }

    @Test
    @DisplayName("BL-70: Зняття коштів та виявлення дефекту зняття точної суми балансу")
    void testWithdrawExactBalance() {
        // Arrange
        ledger.deposit(new BigDecimal("500.00"));

        // Act & Assert (Цей тест впаде через дефект v1 у виразі value.compareTo(balance) >= 0)
        ledger.withdraw(new BigDecimal("500.00"));
        assertEquals(new BigDecimal("0.00"), ledger.balance(), "Баланс після зняття 500 грн із 500 грн має дорівнювати 0.00 грн");
    }

    @Test
    @DisplayName("BL-70: Спроба зняття суми, що перевищує баланс (виняток IllegalStateException)")
    void testWithdrawExceedsBalanceThrowsException() {
        // Arrange
        ledger.deposit(new BigDecimal("100.00"));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            ledger.withdraw(new BigDecimal("150.00"));
        }, "Спроба зняти більше ніж є на балансі повинна викликати IllegalStateException");
    }

    @Test
    @DisplayName("BL-80: Перевірка викидання IllegalArgumentException при від'ємній або нульовій сумі")
    void testInvalidAmountThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            ledger.deposit(new BigDecimal("-50.00"));
        }, "Від'ємна сума депозиту повинна викликати виняток");

        assertThrows(IllegalArgumentException.class, () -> {
            ledger.withdraw(BigDecimal.ZERO);
        }, "Нульова сума зняття повинна викликати виняток");
    }

    @Test
    @DisplayName("BL-80: Перевірка збереження історії операцій та порядку в history()")
    void testHistoryOrderAndImmutability() {
        // Arrange
        ledger.deposit(new BigDecimal("200.00"));
        ledger.deposit(new BigDecimal("300.00"));

        // Act
        var history = ledger.history();

        // Assert
        assertEquals(2, history.size(), "Історія повинна містити 2 записи");
        assertEquals(new BigDecimal("300.00"), history.get(0).amount(), "Останній запис повинен бути першим у списку");
    }
}