package com.threeriverinsurance.testutil;

import com.threeriverinsurance.model.CoverageLevel;
import com.threeriverinsurance.model.InsuranceType;
import com.threeriverinsurance.model.Quote;
import com.threeriverinsurance.model.QuoteRequest;
import com.threeriverinsurance.model.QuoteResponse;
import com.threeriverinsurance.model.QuoteStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Factory class for creating test data objects with sensible defaults.
 */
public class TestDataFactory {

    public static class QuoteRequestBuilder {
        private String customerFirstName = "John";
        private String customerLastName = "Doe";
        private String customerEmail = "john.doe@example.com";
        private String customerPhone = "555-1234";
        private String customerStreet = "123 Main St";
        private String customerCity = "Pittsburgh";
        private String customerState = "PA";
        private String customerZip = "15222";
        private InsuranceType insuranceType = InsuranceType.AUTO;
        private Integer vehicleYear;
        private String vehicleMake;
        private String vehicleModel;
        private String propertyType;
        private Integer propertySize;
        private Integer yearBuilt;
        private CoverageLevel coverageLevel = CoverageLevel.LIABILITY;
        private BigDecimal deductibleAmount = new BigDecimal("1000.00");

        public QuoteRequestBuilder withCustomerFirstName(String customerFirstName) {
            this.customerFirstName = customerFirstName;
            return this;
        }

        public QuoteRequestBuilder withCustomerLastName(String customerLastName) {
            this.customerLastName = customerLastName;
            return this;
        }

        public QuoteRequestBuilder withCustomerEmail(String customerEmail) {
            this.customerEmail = customerEmail;
            return this;
        }

        public QuoteRequestBuilder withCustomerPhone(String customerPhone) {
            this.customerPhone = customerPhone;
            return this;
        }

        public QuoteRequestBuilder withCustomerStreet(String customerStreet) {
            this.customerStreet = customerStreet;
            return this;
        }

        public QuoteRequestBuilder withCustomerCity(String customerCity) {
            this.customerCity = customerCity;
            return this;
        }

        public QuoteRequestBuilder withCustomerState(String customerState) {
            this.customerState = customerState;
            return this;
        }

        public QuoteRequestBuilder withCustomerZip(String customerZip) {
            this.customerZip = customerZip;
            return this;
        }

        public QuoteRequestBuilder withInsuranceType(InsuranceType insuranceType) {
            this.insuranceType = insuranceType;
            return this;
        }

        public QuoteRequestBuilder withVehicleYear(Integer vehicleYear) {
            this.vehicleYear = vehicleYear;
            return this;
        }

        public QuoteRequestBuilder withVehicleMake(String vehicleMake) {
            this.vehicleMake = vehicleMake;
            return this;
        }

        public QuoteRequestBuilder withVehicleModel(String vehicleModel) {
            this.vehicleModel = vehicleModel;
            return this;
        }

        public QuoteRequestBuilder withPropertyType(String propertyType) {
            this.propertyType = propertyType;
            return this;
        }

        public QuoteRequestBuilder withPropertySize(Integer propertySize) {
            this.propertySize = propertySize;
            return this;
        }

        public QuoteRequestBuilder withYearBuilt(Integer yearBuilt) {
            this.yearBuilt = yearBuilt;
            return this;
        }

        public QuoteRequestBuilder withCoverageLevel(CoverageLevel coverageLevel) {
            this.coverageLevel = coverageLevel;
            return this;
        }

        public QuoteRequestBuilder withDeductibleAmount(BigDecimal deductibleAmount) {
            this.deductibleAmount = deductibleAmount;
            return this;
        }

        public QuoteRequest build() {
            QuoteRequest request = new QuoteRequest();
            request.setCustomerFirstName(customerFirstName);
            request.setCustomerLastName(customerLastName);
            request.setCustomerEmail(customerEmail);
            request.setCustomerPhone(customerPhone);
            request.setCustomerStreet(customerStreet);
            request.setCustomerCity(customerCity);
            request.setCustomerState(customerState);
            request.setCustomerZip(customerZip);
            request.setInsuranceType(insuranceType);
            request.setVehicleYear(vehicleYear);
            request.setVehicleMake(vehicleMake);
            request.setVehicleModel(vehicleModel);
            request.setPropertyType(propertyType);
            request.setPropertySize(propertySize);
            request.setYearBuilt(yearBuilt);
            request.setCoverageLevel(coverageLevel);
            request.setDeductibleAmount(deductibleAmount);
            return request;
        }
    }

    public static QuoteRequestBuilder autoQuoteRequest() {
        return new QuoteRequestBuilder()
                .withInsuranceType(InsuranceType.AUTO)
                .withVehicleYear(2022)
                .withVehicleMake("Toyota")
                .withVehicleModel("Camry");
    }

    public static QuoteRequestBuilder homeQuoteRequest() {
        return new QuoteRequestBuilder()
                .withInsuranceType(InsuranceType.HOME)
                .withPropertyType("Single Family")
                .withPropertySize(1500)
                .withYearBuilt(2000);
    }

    public static Quote createQuote(Long id, InsuranceType insuranceType, CoverageLevel coverageLevel, 
                                   BigDecimal deductibleAmount, BigDecimal monthlyPremium, BigDecimal annualPremium) {
        Quote quote = new Quote();
        quote.setId(id);
        quote.setCustomerFirstName("John");
        quote.setCustomerLastName("Doe");
        quote.setCustomerEmail("john.doe@example.com");
        quote.setCustomerPhone("555-1234");
        quote.setCustomerStreet("123 Main St");
        quote.setCustomerCity("Pittsburgh");
        quote.setCustomerState("PA");
        quote.setCustomerZip("15222");
        quote.setInsuranceType(insuranceType);
        quote.setCoverageLevel(coverageLevel);
        quote.setDeductibleAmount(deductibleAmount);
        quote.setMonthlyPremium(monthlyPremium);
        quote.setAnnualPremium(annualPremium);
        quote.setQuoteDate(LocalDateTime.now());
        quote.setStatus(QuoteStatus.QUOTED);

        if (insuranceType == InsuranceType.AUTO) {
            quote.setVehicleYear(2022);
            quote.setVehicleMake("Toyota");
            quote.setVehicleModel("Camry");
        } else if (insuranceType == InsuranceType.HOME) {
            quote.setPropertyType("Single Family");
            quote.setPropertySize(1500);
            quote.setYearBuilt(2000);
        }

        return quote;
    }

    public static QuoteResponse createQuoteResponse(Long id, InsuranceType insuranceType, 
                                                   BigDecimal monthlyPremium, BigDecimal annualPremium) {
        QuoteResponse response = new QuoteResponse();
        response.setId(id);
        response.setCustomerFirstName("John");
        response.setCustomerLastName("Doe");
        response.setCustomerEmail("john.doe@example.com");
        response.setCustomerPhone("555-1234");
        response.setCustomerStreet("123 Main St");
        response.setCustomerCity("Pittsburgh");
        response.setCustomerState("PA");
        response.setCustomerZip("15222");
        response.setInsuranceType(insuranceType);
        response.setCoverageLevel(CoverageLevel.LIABILITY);
        response.setDeductibleAmount(new BigDecimal("1000.00"));
        response.setMonthlyPremium(monthlyPremium);
        response.setAnnualPremium(annualPremium);
        response.setQuoteDate(LocalDateTime.now());
        response.setStatus(QuoteStatus.QUOTED);
        return response;
    }
}
