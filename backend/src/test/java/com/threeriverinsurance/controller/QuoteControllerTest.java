package com.threeriverinsurance.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threeriverinsurance.exception.QuoteNotFoundException;
import com.threeriverinsurance.model.CoverageLevel;
import com.threeriverinsurance.model.InsuranceType;
import com.threeriverinsurance.model.Quote;
import com.threeriverinsurance.model.QuoteRequest;
import com.threeriverinsurance.model.QuoteStatus;
import com.threeriverinsurance.service.QuoteService;
import com.threeriverinsurance.testutil.TestDataFactory;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(QuoteController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("QuoteController Tests")
class QuoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private QuoteService quoteService;

    @Test
    @DisplayName("POST /api/quotes - Create quote successfully")
    void testCreateQuote() throws Exception {
        QuoteRequest request = TestDataFactory.autoQuoteRequest().build();
        Quote savedQuote = TestDataFactory.createQuote(1L, InsuranceType.AUTO, 
                CoverageLevel.LIABILITY, new BigDecimal("1000.00"), 
                new BigDecimal("100.00"), new BigDecimal("1140.00"));

        when(quoteService.createQuote(any(QuoteRequest.class))).thenReturn(savedQuote);

        mockMvc.perform(post("/api/quotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.insuranceType").value("AUTO"))
                .andExpect(jsonPath("$.monthlyPremium").value(100.00))
                .andExpect(jsonPath("$.annualPremium").value(1140.00));

        verify(quoteService).createQuote(any(QuoteRequest.class));
    }

    @Test
    @DisplayName("POST /api/quotes - Validation error for missing required fields")
    void testCreateQuoteValidationError() throws Exception {
        QuoteRequest invalidRequest = new QuoteRequest();

        mockMvc.perform(post("/api/quotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/quotes - Validation error for invalid email")
    void testCreateQuoteInvalidEmail() throws Exception {
        QuoteRequest request = TestDataFactory.autoQuoteRequest()
                .withCustomerEmail("invalid-email")
                .build();

        mockMvc.perform(post("/api/quotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/quotes - Validation error for negative deductible")
    void testCreateQuoteNegativeDeductible() throws Exception {
        QuoteRequest request = TestDataFactory.autoQuoteRequest()
                .withDeductibleAmount(new BigDecimal("-100.00"))
                .build();

        mockMvc.perform(post("/api/quotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/quotes/{id} - Get quote successfully")
    void testGetQuoteFound() throws Exception {
        Long quoteId = 1L;
        Quote quote = TestDataFactory.createQuote(quoteId, InsuranceType.AUTO, 
                CoverageLevel.LIABILITY, new BigDecimal("1000.00"), 
                new BigDecimal("100.00"), new BigDecimal("1140.00"));

        when(quoteService.getQuote(quoteId)).thenReturn(quote);

        mockMvc.perform(get("/api/quotes/{id}", quoteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.insuranceType").value("AUTO"))
                .andExpect(jsonPath("$.monthlyPremium").value(100.00));

        verify(quoteService).getQuote(quoteId);
    }

    @Test
    @DisplayName("GET /api/quotes/{id} - Quote not found returns 404")
    void testGetQuoteNotFound() throws Exception {
        Long quoteId = 999L;

        when(quoteService.getQuote(quoteId)).thenThrow(new QuoteNotFoundException(quoteId));

        mockMvc.perform(get("/api/quotes/{id}", quoteId))
                .andExpect(status().isNotFound());

        verify(quoteService).getQuote(quoteId);
    }

    @Test
    @DisplayName("GET /api/quotes - Get all quotes")
    void testGetAllQuotes() throws Exception {
        List<Quote> quotes = Arrays.asList(
                TestDataFactory.createQuote(1L, InsuranceType.AUTO, CoverageLevel.LIABILITY, 
                        new BigDecimal("1000.00"), new BigDecimal("100.00"), new BigDecimal("1140.00")),
                TestDataFactory.createQuote(2L, InsuranceType.HOME, CoverageLevel.COMPREHENSIVE, 
                        new BigDecimal("2500.00"), new BigDecimal("200.00"), new BigDecimal("2280.00"))
        );

        when(quoteService.getAllQuotes(null, null)).thenReturn(quotes);

        mockMvc.perform(get("/api/quotes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(quoteService).getAllQuotes(null, null);
    }

    @Test
    @DisplayName("GET /api/quotes?status=QUOTED - Get quotes filtered by status")
    void testGetAllQuotesByStatus() throws Exception {
        List<Quote> quotes = Arrays.asList(
                TestDataFactory.createQuote(1L, InsuranceType.AUTO, CoverageLevel.LIABILITY, 
                        new BigDecimal("1000.00"), new BigDecimal("100.00"), new BigDecimal("1140.00"))
        );

        when(quoteService.getAllQuotes(QuoteStatus.QUOTED, null)).thenReturn(quotes);

        mockMvc.perform(get("/api/quotes")
                .param("status", "QUOTED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("QUOTED"));

        verify(quoteService).getAllQuotes(QuoteStatus.QUOTED, null);
    }

    @Test
    @DisplayName("GET /api/quotes?insuranceType=AUTO - Get quotes filtered by insurance type")
    void testGetAllQuotesByInsuranceType() throws Exception {
        List<Quote> quotes = Arrays.asList(
                TestDataFactory.createQuote(1L, InsuranceType.AUTO, CoverageLevel.LIABILITY, 
                        new BigDecimal("1000.00"), new BigDecimal("100.00"), new BigDecimal("1140.00"))
        );

        when(quoteService.getAllQuotes(null, InsuranceType.AUTO)).thenReturn(quotes);

        mockMvc.perform(get("/api/quotes")
                .param("insuranceType", "AUTO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].insuranceType").value("AUTO"));

        verify(quoteService).getAllQuotes(null, InsuranceType.AUTO);
    }

    @Test
    @DisplayName("PUT /api/quotes/{id} - Update quote successfully")
    void testUpdateQuote() throws Exception {
        Long quoteId = 1L;
        QuoteRequest request = TestDataFactory.autoQuoteRequest()
                .withCoverageLevel(CoverageLevel.PREMIUM)
                .build();
        Quote updatedQuote = TestDataFactory.createQuote(quoteId, InsuranceType.AUTO, 
                CoverageLevel.PREMIUM, new BigDecimal("1000.00"), 
                new BigDecimal("200.00"), new BigDecimal("2280.00"));

        when(quoteService.updateQuote(eq(quoteId), any(QuoteRequest.class))).thenReturn(updatedQuote);

        mockMvc.perform(put("/api/quotes/{id}", quoteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.coverageLevel").value("PREMIUM"))
                .andExpect(jsonPath("$.monthlyPremium").value(200.00));

        verify(quoteService).updateQuote(eq(quoteId), any(QuoteRequest.class));
    }

    @Test
    @DisplayName("PUT /api/quotes/{id} - Update quote not found returns 404")
    void testUpdateQuoteNotFound() throws Exception {
        Long quoteId = 999L;
        QuoteRequest request = TestDataFactory.autoQuoteRequest().build();

        when(quoteService.updateQuote(eq(quoteId), any(QuoteRequest.class)))
                .thenThrow(new QuoteNotFoundException(quoteId));

        mockMvc.perform(put("/api/quotes/{id}", quoteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(quoteService).updateQuote(eq(quoteId), any(QuoteRequest.class));
    }

    @Test
    @DisplayName("DELETE /api/quotes/{id} - Delete quote successfully")
    void testDeleteQuote() throws Exception {
        Long quoteId = 1L;

        mockMvc.perform(delete("/api/quotes/{id}", quoteId))
                .andExpect(status().isNoContent());

        verify(quoteService).deleteQuote(quoteId);
    }
}
