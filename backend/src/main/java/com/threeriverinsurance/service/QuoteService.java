package com.threeriverinsurance.service;

import com.threeriverinsurance.model.InsuranceType;
import com.threeriverinsurance.model.Quote;
import com.threeriverinsurance.model.QuoteRequest;
import com.threeriverinsurance.model.QuoteStatus;
import com.threeriverinsurance.repository.QuoteRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for CRUD operations on quotes.
 */
@Service
public class QuoteService {

    private final QuoteRepository quoteRepository;
    private final QuoteCalculationService calculationService;

    public QuoteService(QuoteRepository quoteRepository, QuoteCalculationService calculationService) {
        this.quoteRepository = quoteRepository;
        this.calculationService = calculationService;
    }

    /**
     * Creates a new quote with calculated premiums.
     *
     * @param request the quote request
     * @return the saved quote
     */
    @Transactional
    public Quote createQuote(QuoteRequest request) {
        Quote quote = calculationService.calculatePremium(request);
        return quoteRepository.save(quote);
    }

    /**
     * Gets a quote by ID.
     *
     * @param id the quote ID
     * @return the quote or null if not found
     */
    public Quote getQuote(Long id) {
        return quoteRepository.findById(id).orElse(null);
    }

    /**
     * Gets all quotes, optionally filtered by status or insurance type.
     *
     * @param status the status filter (optional)
     * @param insuranceType the insurance type filter (optional)
     * @return list of quotes
     */
    public List<Quote> getAllQuotes(QuoteStatus status, InsuranceType insuranceType) {
        if (status != null) {
            return quoteRepository.findByStatus(status);
        } else if (insuranceType != null) {
            return quoteRepository.findByInsuranceType(insuranceType);
        } else {
            return quoteRepository.findAll();
        }
    }

    /**
     * Updates an existing quote and recalculates premiums.
     *
     * @param id the quote ID
     * @param request the updated quote request
     * @return the updated quote or null if not found
     */
    @Transactional
    public Quote updateQuote(Long id, QuoteRequest request) {
        Quote existing = quoteRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }

        Quote updatedQuote = calculationService.calculatePremium(request);
        updatedQuote.setId(id);
        return quoteRepository.save(updatedQuote);
    }

    /**
     * Deletes a quote by ID.
     *
     * @param id the quote ID
     */
    @Transactional
    public void deleteQuote(Long id) {
        quoteRepository.deleteById(id);
    }
}
