package com.threeriverinsurance.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.threeriverinsurance.exception.QuoteNotFoundException;
import com.threeriverinsurance.model.CoverageLevel;
import com.threeriverinsurance.model.InsuranceType;
import com.threeriverinsurance.model.Quote;
import com.threeriverinsurance.model.QuoteRequest;
import com.threeriverinsurance.model.QuoteStatus;
import com.threeriverinsurance.repository.QuoteRepository;
import com.threeriverinsurance.testutil.TestDataFactory;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("QuoteService Tests")
class QuoteServiceTest {

    @Mock
    private QuoteRepository quoteRepository;

    @Mock
    private QuoteCalculationService calculationService;

    @InjectMocks
    private QuoteService quoteService;

    @Test
    @DisplayName("Create quote successfully")
    void testCreateQuote() {
        QuoteRequest request = TestDataFactory.autoQuoteRequest().build();
        Quote calculatedQuote = TestDataFactory.createQuote(null, InsuranceType.AUTO, 
                CoverageLevel.LIABILITY, new BigDecimal("1000.00"), 
                new BigDecimal("100.00"), new BigDecimal("1140.00"));
        Quote savedQuote = TestDataFactory.createQuote(1L, InsuranceType.AUTO, 
                CoverageLevel.LIABILITY, new BigDecimal("1000.00"), 
                new BigDecimal("100.00"), new BigDecimal("1140.00"));

        when(calculationService.calculatePremium(request)).thenReturn(calculatedQuote);
        when(quoteRepository.save(calculatedQuote)).thenReturn(savedQuote);

        Quote result = quoteService.createQuote(request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(calculationService).calculatePremium(request);
        verify(quoteRepository).save(calculatedQuote);
    }

    @Test
    @DisplayName("Get quote by ID successfully")
    void testGetQuoteFound() {
        Long quoteId = 1L;
        Quote expectedQuote = TestDataFactory.createQuote(quoteId, InsuranceType.AUTO, 
                CoverageLevel.LIABILITY, new BigDecimal("1000.00"), 
                new BigDecimal("100.00"), new BigDecimal("1140.00"));

        when(quoteRepository.findById(quoteId)).thenReturn(Optional.of(expectedQuote));

        Quote result = quoteService.getQuote(quoteId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(quoteId);
        verify(quoteRepository).findById(quoteId);
    }

    @Test
    @DisplayName("Get quote by ID throws QuoteNotFoundException when not found")
    void testGetQuoteNotFound() {
        Long quoteId = 999L;

        when(quoteRepository.findById(quoteId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> quoteService.getQuote(quoteId))
                .isInstanceOf(QuoteNotFoundException.class);
        
        verify(quoteRepository).findById(quoteId);
    }

    @Test
    @DisplayName("Get all quotes without filters")
    void testGetAllQuotesNoFilters() {
        List<Quote> expectedQuotes = Arrays.asList(
                TestDataFactory.createQuote(1L, InsuranceType.AUTO, CoverageLevel.LIABILITY, 
                        new BigDecimal("1000.00"), new BigDecimal("100.00"), new BigDecimal("1140.00")),
                TestDataFactory.createQuote(2L, InsuranceType.HOME, CoverageLevel.COMPREHENSIVE, 
                        new BigDecimal("2500.00"), new BigDecimal("200.00"), new BigDecimal("2280.00"))
        );

        when(quoteRepository.findAll()).thenReturn(expectedQuotes);

        List<Quote> result = quoteService.getAllQuotes(null, null);

        assertThat(result).hasSize(2);
        verify(quoteRepository).findAll();
    }

    @Test
    @DisplayName("Get all quotes filtered by status")
    void testGetAllQuotesByStatus() {
        QuoteStatus status = QuoteStatus.QUOTED;
        List<Quote> expectedQuotes = Arrays.asList(
                TestDataFactory.createQuote(1L, InsuranceType.AUTO, CoverageLevel.LIABILITY, 
                        new BigDecimal("1000.00"), new BigDecimal("100.00"), new BigDecimal("1140.00"))
        );

        when(quoteRepository.findByStatus(status)).thenReturn(expectedQuotes);

        List<Quote> result = quoteService.getAllQuotes(status, null);

        assertThat(result).hasSize(1);
        verify(quoteRepository).findByStatus(status);
    }

    @Test
    @DisplayName("Get all quotes filtered by insurance type")
    void testGetAllQuotesByInsuranceType() {
        InsuranceType insuranceType = InsuranceType.AUTO;
        List<Quote> expectedQuotes = Arrays.asList(
                TestDataFactory.createQuote(1L, InsuranceType.AUTO, CoverageLevel.LIABILITY, 
                        new BigDecimal("1000.00"), new BigDecimal("100.00"), new BigDecimal("1140.00"))
        );

        when(quoteRepository.findByInsuranceType(insuranceType)).thenReturn(expectedQuotes);

        List<Quote> result = quoteService.getAllQuotes(null, insuranceType);

        assertThat(result).hasSize(1);
        verify(quoteRepository).findByInsuranceType(insuranceType);
    }

    @Test
    @DisplayName("Update quote successfully")
    void testUpdateQuote() {
        Long quoteId = 1L;
        QuoteRequest request = TestDataFactory.autoQuoteRequest()
                .withCoverageLevel(CoverageLevel.PREMIUM)
                .build();
        Quote existingQuote = TestDataFactory.createQuote(quoteId, InsuranceType.AUTO, 
                CoverageLevel.LIABILITY, new BigDecimal("1000.00"), 
                new BigDecimal("100.00"), new BigDecimal("1140.00"));
        Quote updatedQuote = TestDataFactory.createQuote(quoteId, InsuranceType.AUTO, 
                CoverageLevel.PREMIUM, new BigDecimal("1000.00"), 
                new BigDecimal("200.00"), new BigDecimal("2280.00"));

        when(quoteRepository.findById(quoteId)).thenReturn(Optional.of(existingQuote));
        when(calculationService.calculatePremium(request)).thenReturn(updatedQuote);
        when(quoteRepository.save(any(Quote.class))).thenReturn(updatedQuote);

        Quote result = quoteService.updateQuote(quoteId, request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(quoteId);
        verify(quoteRepository).findById(quoteId);
        verify(calculationService).calculatePremium(request);
        verify(quoteRepository).save(any(Quote.class));
    }

    @Test
    @DisplayName("Update quote throws QuoteNotFoundException when not found")
    void testUpdateQuoteNotFound() {
        Long quoteId = 999L;
        QuoteRequest request = TestDataFactory.autoQuoteRequest().build();

        when(quoteRepository.findById(quoteId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> quoteService.updateQuote(quoteId, request))
                .isInstanceOf(QuoteNotFoundException.class);
        
        verify(quoteRepository).findById(quoteId);
    }

    @Test
    @DisplayName("Delete quote successfully")
    void testDeleteQuote() {
        Long quoteId = 1L;

        quoteService.deleteQuote(quoteId);

        verify(quoteRepository).deleteById(quoteId);
    }
}