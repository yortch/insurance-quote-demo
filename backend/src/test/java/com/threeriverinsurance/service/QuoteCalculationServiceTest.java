package com.threeriverinsurance.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.threeriverinsurance.model.CoverageLevel;
import com.threeriverinsurance.model.InsuranceType;
import com.threeriverinsurance.model.Quote;
import com.threeriverinsurance.model.QuoteRequest;
import com.threeriverinsurance.model.QuoteStatus;
import com.threeriverinsurance.testutil.TestDataFactory;
import java.math.BigDecimal;
import java.time.Year;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("QuoteCalculationService Tests")
class QuoteCalculationServiceTest {

    private QuoteCalculationService calculationService;

    @BeforeEach
    void setUp() {
        calculationService = new QuoteCalculationService();
    }

    @Test
    @DisplayName("Calculate premium for AUTO insurance with LIABILITY coverage")
    void testCalculatePremiumAutoLiability() {
        QuoteRequest request = TestDataFactory.autoQuoteRequest()
                .withCoverageLevel(CoverageLevel.LIABILITY)
                .withDeductibleAmount(new BigDecimal("1000.00"))
                .withVehicleYear(2020)
                .build();

        Quote quote = calculationService.calculatePremium(request);

        // Base rate: 100, Coverage multiplier: 1.0, Deductible discount: 0.05, Vehicle age factor: 1.1 (4 years old)
        // Monthly: 100 * 1.0 * (1 - 0.05) * 1.1 = 104.50
        // Annual: 104.50 * 12 * 0.95 = 1191.30
        assertThat(quote.getMonthlyPremium()).isEqualByComparingTo(new BigDecimal("104.50"));
        assertThat(quote.getAnnualPremium()).isEqualByComparingTo(new BigDecimal("1191.30"));
        assertThat(quote.getStatus()).isEqualTo(QuoteStatus.QUOTED);
        assertThat(quote.getQuoteDate()).isNotNull();
    }

    @Test
    @DisplayName("Calculate premium for AUTO insurance with COMPREHENSIVE coverage")
    void testCalculatePremiumAutoComprehensive() {
        QuoteRequest request = TestDataFactory.autoQuoteRequest()
                .withCoverageLevel(CoverageLevel.COMPREHENSIVE)
                .withDeductibleAmount(new BigDecimal("2500.00"))
                .withVehicleYear(Year.now().getValue() - 2)
                .build();

        Quote quote = calculationService.calculatePremium(request);

        // Base rate: 100, Coverage multiplier: 1.5, Deductible discount: 0.10, Vehicle age: 2 years (factor 1.2)
        // Monthly: 100 * 1.5 * (1 - 0.10) * 1.2 = 100 * 1.5 * 0.9 * 1.2 = 162.00
        // Annual: 162.00 * 12 * 0.95 = 1846.80
        assertThat(quote.getMonthlyPremium()).isEqualByComparingTo(new BigDecimal("162.00"));
        assertThat(quote.getAnnualPremium()).isEqualByComparingTo(new BigDecimal("1846.80"));
    }

    @Test
    @DisplayName("Calculate premium for AUTO insurance with PREMIUM coverage")
    void testCalculatePremiumAutoPremium() {
        QuoteRequest request = TestDataFactory.autoQuoteRequest()
                .withCoverageLevel(CoverageLevel.PREMIUM)
                .withDeductibleAmount(new BigDecimal("5000.00"))
                .withVehicleYear(Year.now().getValue() - 5)
                .build();

        Quote quote = calculationService.calculatePremium(request);

        // Base rate: 100, Coverage multiplier: 2.0, Deductible discount: 0.15, Vehicle age: 5 years (factor 1.1)
        // Monthly: 100 * 2.0 * (1 - 0.15) * 1.1 = 100 * 2.0 * 0.85 * 1.1 = 187.00
        // Annual: 187.00 * 12 * 0.95 = 2131.80
        assertThat(quote.getMonthlyPremium()).isEqualByComparingTo(new BigDecimal("187.00"));
        assertThat(quote.getAnnualPremium()).isEqualByComparingTo(new BigDecimal("2131.80"));
    }

    @Test
    @DisplayName("Calculate premium for HOME insurance with LIABILITY coverage")
    void testCalculatePremiumHomeLiability() {
        QuoteRequest request = TestDataFactory.homeQuoteRequest()
                .withCoverageLevel(CoverageLevel.LIABILITY)
                .withDeductibleAmount(new BigDecimal("1000.00"))
                .withPropertySize(800)
                .build();

        Quote quote = calculationService.calculatePremium(request);

        // Base rate: 150, Coverage multiplier: 1.0, Deductible discount: 0.05, Property size: 800 (no extra charge)
        // Monthly: 150 * 1.0 * (1 - 0.05) * 1.0 = 142.50
        // Annual: 142.50 * 12 * 0.95 = 1624.50
        assertThat(quote.getMonthlyPremium()).isEqualByComparingTo(new BigDecimal("142.50"));
        assertThat(quote.getAnnualPremium()).isEqualByComparingTo(new BigDecimal("1624.50"));
    }

    @Test
    @DisplayName("Calculate premium for HOME insurance with property size > 1000 sqft")
    void testCalculatePremiumHomeWithLargeProperty() {
        QuoteRequest request = TestDataFactory.homeQuoteRequest()
                .withCoverageLevel(CoverageLevel.COMPREHENSIVE)
                .withDeductibleAmount(new BigDecimal("2500.00"))
                .withPropertySize(2000)
                .build();

        Quote quote = calculationService.calculatePremium(request);

        // Base rate: 150, Coverage multiplier: 1.5, Deductible discount: 0.10
        // Property size: 2000 - 1000 = 1000 extra sqft => 1000/100 * 0.05 = 0.50 additional factor
        // Factor: 1 + 0.50 = 1.5
        // Monthly: 150 * 1.5 * (1 - 0.10) * 1.5 = 150 * 1.5 * 0.9 * 1.5 = 303.75
        // Annual: 303.75 * 12 * 0.95 = 3462.75
        assertThat(quote.getMonthlyPremium()).isEqualByComparingTo(new BigDecimal("303.75"));
        assertThat(quote.getAnnualPremium()).isEqualByComparingTo(new BigDecimal("3462.75"));
    }

    @ParameterizedTest
    @CsvSource({
        "LIABILITY, 1.0",
        "COMPREHENSIVE, 1.5",
        "PREMIUM, 2.0"
    })
    @DisplayName("Coverage level affects premium calculation")
    void testCoverageLevelMultiplier(CoverageLevel coverageLevel, String multiplier) {
        QuoteRequest request = TestDataFactory.autoQuoteRequest()
                .withCoverageLevel(coverageLevel)
                .withDeductibleAmount(new BigDecimal("500.00"))
                .withVehicleYear(Year.now().getValue() - 10)
                .build();

        Quote quote = calculationService.calculatePremium(request);

        // Base rate: 100, Vehicle age > 7: factor 1.0, No deductible discount
        BigDecimal expectedMonthly = new BigDecimal("100.00")
                .multiply(new BigDecimal(multiplier));
        
        assertThat(quote.getMonthlyPremium()).isEqualByComparingTo(expectedMonthly);
    }

    @ParameterizedTest
    @CsvSource({
        "500.00, 0.00",
        "1000.00, 0.05",
        "2500.00, 0.10",
        "5000.00, 0.15"
    })
    @DisplayName("Deductible amount affects discount")
    void testDeductibleDiscount(String deductible, String discount) {
        QuoteRequest request = TestDataFactory.autoQuoteRequest()
                .withCoverageLevel(CoverageLevel.LIABILITY)
                .withDeductibleAmount(new BigDecimal(deductible))
                .withVehicleYear(Year.now().getValue() - 10)
                .build();

        Quote quote = calculationService.calculatePremium(request);

        // Base rate: 100, Coverage: 1.0, Vehicle age > 7: factor 1.0
        BigDecimal expectedMonthly = new BigDecimal("100.00")
                .multiply(BigDecimal.ONE.subtract(new BigDecimal(discount)));
        
        assertThat(quote.getMonthlyPremium()).isEqualByComparingTo(expectedMonthly);
    }

    @Test
    @DisplayName("Annual premium is monthly * 12 * 0.95 (5% discount)")
    void testAnnualPremiumDiscount() {
        QuoteRequest request = TestDataFactory.autoQuoteRequest()
                .withCoverageLevel(CoverageLevel.LIABILITY)
                .withDeductibleAmount(new BigDecimal("1000.00"))
                .withVehicleYear(Year.now().getValue() - 10)
                .build();

        Quote quote = calculationService.calculatePremium(request);

        BigDecimal expectedAnnual = quote.getMonthlyPremium()
                .multiply(new BigDecimal("12"))
                .multiply(new BigDecimal("0.95"));
        
        assertThat(quote.getAnnualPremium()).isEqualByComparingTo(expectedAnnual);
    }

    @Test
    @DisplayName("New vehicle (3 years) gets 20% premium increase")
    void testNewVehicleFactor() {
        QuoteRequest request = TestDataFactory.autoQuoteRequest()
                .withCoverageLevel(CoverageLevel.LIABILITY)
                .withDeductibleAmount(new BigDecimal("500.00"))
                .withVehicleYear(Year.now().getValue())
                .build();

        Quote quote = calculationService.calculatePremium(request);

        // Base: 100, Coverage: 1.0, No discount, Vehicle age 0: factor 1.2
        assertThat(quote.getMonthlyPremium()).isEqualByComparingTo(new BigDecimal("120.00"));
    }

    @Test
    @DisplayName("Mid-age vehicle (4-7 years) gets 10% premium increase")
    void testMidAgeVehicleFactor() {
        QuoteRequest request = TestDataFactory.autoQuoteRequest()
                .withCoverageLevel(CoverageLevel.LIABILITY)
                .withDeductibleAmount(new BigDecimal("500.00"))
                .withVehicleYear(Year.now().getValue() - 6)
                .build();

        Quote quote = calculationService.calculatePremium(request);

        // Base: 100, Coverage: 1.0, No discount, Vehicle age 6: factor 1.1
        assertThat(quote.getMonthlyPremium()).isEqualByComparingTo(new BigDecimal("110.00"));
    }

    @Test
    @DisplayName("Old vehicle (>7 years) gets no premium adjustment")
    void testOldVehicleFactor() {
        QuoteRequest request = TestDataFactory.autoQuoteRequest()
                .withCoverageLevel(CoverageLevel.LIABILITY)
                .withDeductibleAmount(new BigDecimal("500.00"))
                .withVehicleYear(Year.now().getValue() - 10)
                .build();

        Quote quote = calculationService.calculatePremium(request);

        // Base: 100, Coverage: 1.0, No discount, Vehicle age 10: factor 1.0
        assertThat(quote.getMonthlyPremium()).isEqualByComparingTo(new BigDecimal("100.00"));
    }
}
