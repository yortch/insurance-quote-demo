package com.threeriverinsurance.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing an insurance quote.
 */
@Entity
@Table(name = "quotes")
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "customer_first_name", nullable = false)
    private String customerFirstName;

    @NotBlank
    @Column(name = "customer_last_name", nullable = false)
    private String customerLastName;

    @NotBlank
    @Email
    @Column(name = "customer_email", nullable = false)
    private String customerEmail;

    @NotBlank
    @Column(name = "customer_phone", nullable = false)
    private String customerPhone;

    @NotBlank
    @Column(name = "customer_street", nullable = false)
    private String customerStreet;

    @NotBlank
    @Column(name = "customer_city", nullable = false)
    private String customerCity;

    @NotBlank
    @Column(name = "customer_state", nullable = false)
    private String customerState;

    @NotBlank
    @Column(name = "customer_zip", nullable = false)
    private String customerZip;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "insurance_type", nullable = false)
    private InsuranceType insuranceType;

    @Column(name = "vehicle_year")
    private Integer vehicleYear;

    @Column(name = "vehicle_make")
    private String vehicleMake;

    @Column(name = "vehicle_model")
    private String vehicleModel;

    @Column(name = "property_type")
    private String propertyType;

    @Column(name = "property_size")
    private Integer propertySize;

    @Column(name = "year_built")
    private Integer yearBuilt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "coverage_level", nullable = false)
    private CoverageLevel coverageLevel;

    @NotNull
    @Column(name = "deductible_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal deductibleAmount;

    @Column(name = "monthly_premium", precision = 10, scale = 2)
    private BigDecimal monthlyPremium;

    @Column(name = "annual_premium", precision = 10, scale = 2)
    private BigDecimal annualPremium;

    @NotNull
    @Column(name = "quote_date", nullable = false)
    private LocalDateTime quoteDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private QuoteStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerStreet() {
        return customerStreet;
    }

    public void setCustomerStreet(String customerStreet) {
        this.customerStreet = customerStreet;
    }

    public String getCustomerCity() {
        return customerCity;
    }

    public void setCustomerCity(String customerCity) {
        this.customerCity = customerCity;
    }

    public String getCustomerState() {
        return customerState;
    }

    public void setCustomerState(String customerState) {
        this.customerState = customerState;
    }

    public String getCustomerZip() {
        return customerZip;
    }

    public void setCustomerZip(String customerZip) {
        this.customerZip = customerZip;
    }

    public InsuranceType getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(InsuranceType insuranceType) {
        this.insuranceType = insuranceType;
    }

    public Integer getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleYear(Integer vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    public String getVehicleMake() {
        return vehicleMake;
    }

    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public Integer getPropertySize() {
        return propertySize;
    }

    public void setPropertySize(Integer propertySize) {
        this.propertySize = propertySize;
    }

    public Integer getYearBuilt() {
        return yearBuilt;
    }

    public void setYearBuilt(Integer yearBuilt) {
        this.yearBuilt = yearBuilt;
    }

    public CoverageLevel getCoverageLevel() {
        return coverageLevel;
    }

    public void setCoverageLevel(CoverageLevel coverageLevel) {
        this.coverageLevel = coverageLevel;
    }

    public BigDecimal getDeductibleAmount() {
        return deductibleAmount;
    }

    public void setDeductibleAmount(BigDecimal deductibleAmount) {
        this.deductibleAmount = deductibleAmount;
    }

    public BigDecimal getMonthlyPremium() {
        return monthlyPremium;
    }

    public void setMonthlyPremium(BigDecimal monthlyPremium) {
        this.monthlyPremium = monthlyPremium;
    }

    public BigDecimal getAnnualPremium() {
        return annualPremium;
    }

    public void setAnnualPremium(BigDecimal annualPremium) {
        this.annualPremium = annualPremium;
    }

    public LocalDateTime getQuoteDate() {
        return quoteDate;
    }

    public void setQuoteDate(LocalDateTime quoteDate) {
        this.quoteDate = quoteDate;
    }

    public QuoteStatus getStatus() {
        return status;
    }

    public void setStatus(QuoteStatus status) {
        this.status = status;
    }
}
