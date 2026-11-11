package ua.kpi.tzyapz.finkit;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юніт-тести кредитного калькулятора.
 * BL-10 — ануїтетний платіж, BL-20 — переплата, BL-30 — валідація меж.
 * Автор: Пономаренко Р. В. (КТ/СМ)
 */
@Tag("unit")
@DisplayName("LoanCalculator — ануїтетний платіж і переплата (BL-10..BL-30)")
class LoanCalculatorTest {

    private LoanCalculator calc;

    @BeforeEach
    void setUp() {
        calc = new LoanCalculator();
    }

    @Test
    @DisplayName("BL-10: 100 000 грн, 12%, 12 міс → 8 884.88 грн")
    void monthlyPayment_standard_correctValue() {
        // i = 12/1200 = 0.01; A = 100000·0.01/(1−1.01^−12) = 8884.8788... → 8884.88
        BigDecimal principal = new BigDecimal("100000");
        BigDecimal rate      = new BigDecimal("12");

        BigDecimal result = calc.monthlyPayment(principal, rate, 12);

        assertEquals(new BigDecimal("8884.88"), result);
    }

    @ParameterizedTest(name = "BL-10: principal={0}, rate={1}, months={2} → payment={3}")
    @CsvSource({
        "10000,  12, 12,  888.49",
        "1000,    5, 60,   18.87"
    })
    @DisplayName("BL-10: різні параметри — платіж відповідає формулі")
    void monthlyPayment_variousParams_correctValues(
            String principal, String rate, int months, String expected) {
        // Набір r=5% падає через BUG-011: LoanCalculator.java:35 округлює monthlyRate
        // до 4 знаків (0.0042 замість 0.0041666...), що порушує BL-10
        // «проміжні обчислення не округлюються». Issue: BUG-011.
        BigDecimal result = calc.monthlyPayment(
                new BigDecimal(principal), new BigDecimal(rate), months);
        assertEquals(new BigDecimal(expected), result,
                "Платіж для " + principal + " грн, " + rate + "%, " + months + " міс");
    }

    @Test
    @DisplayName("BL-20: 100 000 грн, 12%, 12 міс → переплата 6 618.56 грн")
    void totalInterest_standard_correctValue() {
        // 8884.88 × 12 − 100000 = 106618.56 − 100000 = 6618.56
        BigDecimal expected = new BigDecimal("6618.56");

        BigDecimal result = calc.totalInterest(
                new BigDecimal("100000"), new BigDecimal("12"), 12);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("BL-20: переплата не від'ємна для будь-яких допустимих параметрів")
    void totalInterest_alwaysNonNegative() {
        BigDecimal interest = calc.totalInterest(
                new BigDecimal("50000"), new BigDecimal("10"), 24);
        assertTrue(interest.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("BL-30: мінімальний строк = 3 місяці не кидає виняток (нижня межа)")
    void monthlyPayment_exactlyMinMonths_noException() {
        // BUG-010: validate() використовує months <= MIN_MONTHS замість months < MIN_MONTHS,
        // тому months=3 відхиляється (BL-30 вимагає межі 3..60 включно). Issue: BUG-010.
        assertDoesNotThrow(() ->
            calc.monthlyPayment(new BigDecimal("10000"), new BigDecimal("12"), 3),
            "months=3 — допустима нижня межа за BL-30");
    }

    @Test
    @DisplayName("BL-30: максимальний строк = 60 місяців не кидає виняток (верхня межа)")
    void monthlyPayment_exactlyMaxMonths_noException() {
        assertDoesNotThrow(() ->
            calc.monthlyPayment(new BigDecimal("10000"), new BigDecimal("12"), 60));
    }

    @ParameterizedTest(name = "BL-30: months={0} → IllegalArgumentException")
    @CsvSource({"2", "0", "61", "100"})
    @DisplayName("BL-30: строк поза межами [3..60] → IllegalArgumentException")
    void monthlyPayment_monthsOutOfRange_throwsException(int months) {
        assertThrows(IllegalArgumentException.class, () ->
            calc.monthlyPayment(new BigDecimal("10000"), new BigDecimal("12"), months));
    }

    @ParameterizedTest(name = "BL-30: principal={0} → IllegalArgumentException")
    @CsvSource({"999.99", "0", "1000000.01", "9999999"})
    @DisplayName("BL-30: сума поза межами [1 000..1 000 000] → IllegalArgumentException")
    void monthlyPayment_principalOutOfRange_throwsException(String principal) {
        assertThrows(IllegalArgumentException.class, () ->
            calc.monthlyPayment(new BigDecimal(principal), new BigDecimal("12"), 12));
    }

    @ParameterizedTest(name = "BL-30: rate={0} → IllegalArgumentException")
    @CsvSource({"4.99", "0", "40.01", "100"})
    @DisplayName("BL-30: ставка поза межами [5..40] % → IllegalArgumentException")
    void monthlyPayment_rateOutOfRange_throwsException(String rate) {
        assertThrows(IllegalArgumentException.class, () ->
            calc.monthlyPayment(new BigDecimal("10000"), new BigDecimal(rate), 12));
    }

    @Test
    @DisplayName("BL-30: null-сума → IllegalArgumentException")
    void monthlyPayment_nullPrincipal_throwsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            calc.monthlyPayment(null, new BigDecimal("12"), 12));
        assertNotNull(ex.getMessage());
    }

    @Test
    @DisplayName("BL-30: мінімальна сума 1 000 грн — на межі, виняток не кидається")
    void monthlyPayment_exactlyMinPrincipal_noException() {
        assertDoesNotThrow(() ->
            calc.monthlyPayment(new BigDecimal("1000"), new BigDecimal("12"), 12));
    }

    @Test
    @DisplayName("BL-30: максимальна сума 1 000 000 грн — на межі, виняток не кидається")
    void monthlyPayment_exactlyMaxPrincipal_noException() {
        assertDoesNotThrow(() ->
            calc.monthlyPayment(new BigDecimal("1000000"), new BigDecimal("12"), 12));
    }

    @Test
    @DisplayName("BL-30: максимальна ставка 40 % — на межі, виняток не кидається")
    void monthlyPayment_exactlyMaxRate_noException() {
        assertDoesNotThrow(() ->
            calc.monthlyPayment(new BigDecimal("10000"), new BigDecimal("40"), 12));
    }

    @AfterEach
    void tearDown() {
        calc = null;
    }
}
