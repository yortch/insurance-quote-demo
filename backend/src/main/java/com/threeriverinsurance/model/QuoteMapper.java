package com.threeriverinsurance.model;

/**
 * Mapper utility for converting between Quote entities and DTOs.
 */
public final class QuoteMapper {

    private QuoteMapper() {
    }

    /**
     * Maps a QuoteRequest DTO to a Quote entity.
     *
     * @param request the quote request
     * @return the quote entity
     */
    public static Quote toEntity(QuoteRequest request) {
        Quote quote = new Quote();
        quote.setCustomerFirstName(request.getCustomerFirstName());
        quote.setCustomerLastName(request.getCustomerLastName());
        quote.setCustomerEmail(request.getCustomerEmail());
        quote.setCustomerPhone(request.getCustomerPhone());
        quote.setCustomerStreet(request.getCustomerStreet());
        quote.setCustomerCity(request.getCustomerCity());
        quote.setCustomerState(request.getCustomerState());
        quote.setCustomerZip(request.getCustomerZip());
        quote.setInsuranceType(request.getInsuranceType());
        quote.setVehicleYear(request.getVehicleYear());
        quote.setVehicleMake(request.getVehicleMake());
        quote.setVehicleModel(request.getVehicleModel());
        quote.setPropertyType(request.getPropertyType());
        quote.setPropertySize(request.getPropertySize());
        quote.setYearBuilt(request.getYearBuilt());
        quote.setCoverageLevel(request.getCoverageLevel());
        quote.setDeductibleAmount(request.getDeductibleAmount());
        return quote;
    }

    /**
     * Maps a Quote entity to a QuoteResponse DTO.
     *
     * @param quote the quote entity
     * @return the quote response
     */
    public static QuoteResponse toResponse(Quote quote) {
        QuoteResponse response = new QuoteResponse();
        response.setId(quote.getId());
        response.setCustomerFirstName(quote.getCustomerFirstName());
        response.setCustomerLastName(quote.getCustomerLastName());
        response.setCustomerEmail(quote.getCustomerEmail());
        response.setCustomerPhone(quote.getCustomerPhone());
        response.setCustomerStreet(quote.getCustomerStreet());
        response.setCustomerCity(quote.getCustomerCity());
        response.setCustomerState(quote.getCustomerState());
        response.setCustomerZip(quote.getCustomerZip());
        response.setInsuranceType(quote.getInsuranceType());
        response.setVehicleYear(quote.getVehicleYear());
        response.setVehicleMake(quote.getVehicleMake());
        response.setVehicleModel(quote.getVehicleModel());
        response.setPropertyType(quote.getPropertyType());
        response.setPropertySize(quote.getPropertySize());
        response.setYearBuilt(quote.getYearBuilt());
        response.setCoverageLevel(quote.getCoverageLevel());
        response.setDeductibleAmount(quote.getDeductibleAmount());
        response.setMonthlyPremium(quote.getMonthlyPremium());
        response.setAnnualPremium(quote.getAnnualPremium());
        response.setQuoteDate(quote.getQuoteDate());
        response.setStatus(quote.getStatus());
        return response;
    }
}
