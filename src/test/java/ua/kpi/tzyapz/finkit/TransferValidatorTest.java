package ua.kpi.tzyapz.finkit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.List;

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

    @ParameterizedTest(name = "BL-40: переказ суми {0} грн (ліміт 50000) очікується результат {1}") 
    @CsvSource({
            "49999.99, true",
            "50000.00, true",
            "50000.01, false",
            "60000.00, false"
    })
    @DisplayName("BL-40: перевірка денного ліміту переказів на граничних значеннях")
    void dailyLimitBoundaryCheck(String amountStr, boolean expected) {
        BigDecimal amount = new BigDecimal(amountStr);
        BigDecimal balance = new BigDecimal("100000.00"); 
        BigDecimal todaySpent = new BigDecimal("0.00");

        boolean isValid = validator.isValid(validFromIban, validToIban, amount, balance, todaySpent);

        assertEquals(expected, isValid, "Помилка валідації ліміту");
    }

    @Test
    @DisplayName("BL-40: перевірка ліміту з урахуванням уже витрачених коштів")
    void validateReturnsErrorForDailyLimitWithTodaySpent() {
        BigDecimal amount = new BigDecimal("20000.00");
        BigDecimal balance = new BigDecimal("100000.00");
        BigDecimal todaySpent = new BigDecimal("35000.00"); 

        List<TransferError> errors = validator.validate(validFromIban, validToIban, amount, balance, todaySpent);

        assertTrue(errors.contains(TransferError.DAILY_LIMIT_EXCEEDED), "Має бути код DAILY_LIMIT_EXCEEDED");
    }

    @Test
    @DisplayName("BL-40: помилка SAME_ACCOUNT при однакових рахунках")
    void validateReturnsErrorForSameAccount() {
        BigDecimal amount = new BigDecimal("1000.00");
        BigDecimal balance = new BigDecimal("10000.00");
        BigDecimal todaySpent = new BigDecimal("0.00");

        List<TransferError> errors = validator.validate(validFromIban, validFromIban, amount, balance, todaySpent);

        assertTrue(errors.contains(TransferError.SAME_ACCOUNT), "Має бути код SAME_ACCOUNT");
    }

    @Test
    @DisplayName("BL-50: код помилки для невалідного формату IBAN")
    void validateReturnsErrorsForInvalidIbanFormats() {
        BigDecimal amount = new BigDecimal("1000.00");
        BigDecimal balance = new BigDecimal("10000.00");
        BigDecimal todaySpent = new BigDecimal("0.00");
        String invalidFrom = "UA111"; 

        List<TransferError> errors = validator.validate(invalidFrom, validToIban, amount, balance, todaySpent);

        assertTrue(errors.contains(TransferError.INVALID_FROM_IBAN), "Має бути код INVALID_FROM_IBAN");
    }

    @ParameterizedTest(name = "BL-50: валідація IBAN формату для {0}")
    @ValueSource(strings = {"UA123456789012345678901234567", "UA000000000000000000000000000"})
    @DisplayName("BL-50: коректний IBAN (29 символів)")
    void validIbanFormatReturnsTrue(String iban) {
        boolean isValid = TransferValidator.isValidIban(iban);
        assertTrue(isValid, "IBAN коректний");
    }

    @ParameterizedTest(name = "BL-50: негативна валідація IBAN формату для {0}")
    @ValueSource(strings = {"", "UA123", "UK123456789012345678901234567", "UA1234567890123456789012345XX"})
    @DisplayName("BL-50: некоректний IBAN відхиляється")
    void invalidIbanFormatReturnsFalse(String iban) {
        boolean isValid = TransferValidator.isValidIban(iban);
        assertFalse(isValid, "IBAN некоректний");
    }

    @Test
    @DisplayName("BL-50: null IBAN відхиляється")
    void nullIbanFormatReturnsFalse() {
        boolean isValid = TransferValidator.isValidIban(null);
        assertFalse(isValid, "null IBAN некоректний");
    }

    @Test
    @DisplayName("BL-40: від'ємна сума переказу не дозволяється")
    void negativeTransferAmountIsInvalid() {
        BigDecimal negativeAmount = new BigDecimal("-100.00");
        BigDecimal balance = new BigDecimal("1000.00");
        BigDecimal todaySpent = new BigDecimal("0.00");

        boolean isValid = validator.isValid(validFromIban, validToIban, negativeAmount, balance, todaySpent);

        assertFalse(isValid, "Від'ємна сума не проходить");
    }

    @Test
    @DisplayName("BL-40: помилка при недостатньому балансі")
    void validateReturnsErrorsForInsufficientFunds() {
        BigDecimal amount = new BigDecimal("2000.00");
        BigDecimal balance = new BigDecimal("1000.00");
        BigDecimal todaySpent = new BigDecimal("0.00");

        List<TransferError> errors = validator.validate(validFromIban, validToIban, amount, balance, todaySpent);

        assertTrue(errors.contains(TransferError.INSUFFICIENT_FUNDS), "Має бути код INSUFFICIENT_FUNDS");
    }
}
