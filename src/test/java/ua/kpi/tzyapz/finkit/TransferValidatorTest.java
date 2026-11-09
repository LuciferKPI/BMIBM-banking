package ua.kpi.tzyapz.finkit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@DisplayName("Перевірка валідатора переказів (TransferValidator)")
class TransferValidatorTest {

    private TransferValidator validator;

    private final String validFromIban = "UA123456789012345678901234567";
    private final String validToIban = "UA987654321098765432109876543";

    @BeforeEach
    void setUp() {
        validator = new TransferValidator();
    }

    @ParameterizedTest(name = "BL-10: переказ суми {0} грн (ліміт 50000) очікується результат {1}") // Зареєстровано дефект модуля: Bug #008
    @CsvSource({
            "49999.99, true",
            "50000.00, true",
            "50000.01, false",
            "60000.00, false"
    })
    @DisplayName("BL-10: перевірка денного ліміту переказів на граничних значеннях")
    void dailyLimitBoundaryCheck(String amountStr, boolean expected) {
        // Arrange
        BigDecimal amount = new BigDecimal(amountStr);
        BigDecimal balance = new BigDecimal("100000.00"); 
        BigDecimal todaySpent = new BigDecimal("0.00");

        // Act
        boolean isValid = validator.isValid(validFromIban, validToIban, amount, balance, todaySpent);

        // Assert
        assertEquals(expected, isValid, "Перевірка ліміту 50 000 грн не пройшла");
    }

    @ParameterizedTest(name = "BL-11: валідація IBAN формату для {0}")
    @ValueSource(strings = {"UA123456789012345678901234567", "UA000000000000000000000000000"})
    @DisplayName("BL-11: коректний IBAN має довжину 29 символів і починається з UA")
    void validIbanFormatReturnsTrue(String iban) {
        // Act (оскільки метод статичний, викликаємо його через ім'я класу)
        boolean isValid = TransferValidator.isValidIban(iban);

        // Assert
        assertTrue(isValid, "IBAN має бути коректним");
    }

    @Test
    @DisplayName("BL-12: від'ємна сума переказу не дозволяється")
    void negativeTransferAmountIsInvalid() {
        // Arrange
        BigDecimal negativeAmount = new BigDecimal("-100.00");
        BigDecimal balance = new BigDecimal("1000.00");
        BigDecimal todaySpent = new BigDecimal("0.00");

        // Act
        boolean isValid = validator.isValid(validFromIban, validToIban, negativeAmount, balance, todaySpent);

        // Assert
        assertFalse(isValid, "Від'ємна сума не повинна пропускатися системою");
    }
}