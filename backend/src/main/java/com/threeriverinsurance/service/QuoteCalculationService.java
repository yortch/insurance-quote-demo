package com.threeriverinsurance.service;

import com.threeriverinsurance.model.CoverageLevel;
import com.threeriverinsurance.model.InsuranceType;
import com.threeriverinsurance.model.Quote;
import com.threeriverinsurance.model.QuoteMapper;
import com.threeriverinsurance.model.QuoteRequest;
import com.threeriverinsurance.model.QuoteStatus;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.Year;
import org.springframework.stereotype.Service;

/**
 * Service for calculating insurance quote premiums.
 */
@Service
public class QuoteCalculationService {

    private static final BigDecimal AUTO_BASE_RATE = new BigDecimal("100.00");
    private static final BigDecimal HOME_BASE_RATE = new BigDecimal("150.00");
    private static final BigDecimal ANNUAL_DISCOUNT = new BigDecimal("0.95");
    private static final int MONTHS_PER_YEAR = 12;

    /**
     * Calculates premium for a quote request and returns a Quote entity with calculated values.
     *
     * @param request the quote request
     * @return quote with calculated premiums
     */
    public Quote calculatePremium(QuoteRequest request) {
        Quote quote = QuoteMapper.toEntity(request);

        BigDecimal baseRate = getBaseRate(quote.getInsuranceType());
        BigDecimal coverageMultiplier = getCoverageMultiplier(quote.getCoverageLevel());
        BigDecimal deductibleDiscount = getDeductibleDiscount(quote.getDeductibleAmount());
        BigDecimal typeFactor = getTypeFactor(quote);

        BigDecimal monthlyPremium = baseRate
                .multiply(coverageMultiplier)
                .multiply(BigDecimal.ONE.subtract(deductibleDiscount))
                .multiply(typeFactor)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal annualPremium = monthlyPremium
                .multiply(new BigDecimal(MONTHS_PER_YEAR))
                .multiply(ANNUAL_DISCOUNT)
                .setScale(2, RoundingMode.HALF_UP);

        quote.setMonthlyPremium(monthlyPremium);
        quote.setAnnualPremium(annualPremium);
        quote.setQuoteDate(LocalDateTime.now());
        quote.setStatus(QuoteStatus.QUOTED);

        return quote;
    }

    private BigDecimal getBaseRate(InsuranceType insuranceType) {
        return insuranceType == InsuranceType.AUTO ? AUTO_BASE_RATE : HOME_BASE_RATE;
    }

    private BigDecimal getCoverageMultiplier(CoverageLevel coverageLevel) {
        return switch (coverageLevel) {
            case LIABILITY -> new BigDecimal("1.0");
            case COMPREHENSIVE -> new BigDecimal("1.5");
            case PREMIUM -> new BigDecimal("2.0");
        };
    }

    private BigDecimal getDeductibleDiscount(BigDecimal deductible) {
        if (deductible.compareTo(new BigDecimal("5000")) >= 0) {
            return new BigDecimal("0.15");
        } else if (deductible.compareTo(new BigDecimal("2500")) >= 0) {
            return new BigDecimal("0.10");
        } else if (deductible.compareTo(new BigDecimal("1000")) >= 0) {
            return new BigDecimal("0.05");
        } else {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal getTypeFactor(Quote quote) {
        if (quote.getInsuranceType() == InsuranceType.AUTO) {
            return getAutoFactor(quote);
        } else {
            return getHomeFactor(quote);
        }
    }

    private BigDecimal getAutoFactor(Quote quote) {
        BigDecimal factor = BigDecimal.ONE;
        if (quote.getVehicleYear() != null) {
            int vehicleAge = Year.now().getValue() - quote.getVehicleYear();
            if (vehicleAge <= 3) {
                factor = factor.add(new BigDecimal("0.20"));
            } else if (vehicleAge <= 7) {
                factor = factor.add(new BigDecimal("0.10"));
            }
        }
        return factor;
    }

    private BigDecimal getHomeFactor(Quote quote) {
        BigDecimal factor = BigDecimal.ONE;
        if (quote.getPropertySize() != null && quote.getPropertySize() > 1000) {
            int excessSqFt = quote.getPropertySize() - 1000;
            BigDecimal additionalCharge = new BigDecimal(excessSqFt)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("0.05"));
            factor = factor.add(additionalCharge);
        }
        return factor;
    }
}
