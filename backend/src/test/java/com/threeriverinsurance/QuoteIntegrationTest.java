package com.threeriverinsurance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threeriverinsurance.model.CoverageLevel;
import com.threeriverinsurance.model.InsuranceType;
import com.threeriverinsurance.model.QuoteRequest;
import com.threeriverinsurance.model.QuoteResponse;
import com.threeriverinsurance.repository.QuoteRepository;
import com.threeriverinsurance.testutil.TestDataFactory;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Quote Integration Tests")
class QuoteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private QuoteRepository quoteRepository;

    @BeforeEach
    void setUp() {
        quoteRepository.deleteAll();
    }

    @Test
    @DisplayName("Full flow: Create AUTO quote, retrieve it, verify calculation")
    void testCreateAndRetrieveAutoQuote() throws Exception {
        QuoteRequest request = TestDataFactory.autoQuoteRequest()
                .withCoverageLevel(CoverageLevel.LIABILITY)
                .withDeductibleAmount(new BigDecimal("1000.00"))
                .withVehicleYear(2020)
                .build();

        // Create quote
        MvcResult createResult = mockMvc.perform(post("/api/quotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.insuranceType").value("AUTO"))
                .andExpect(jsonPath("$.status").value("QUOTED"))
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        QuoteResponse createdQuote = objectMapper.readValue(responseBody, QuoteResponse.class);
        Long quoteId = createdQuote.getId();

        assertThat(quoteId).isNotNull();
        assertThat(createdQuote.getMonthlyPremium()).isNotNull();
        assertThat(createdQuote.getAnnualPremium()).isNotNull();

        // Retrieve quote
        mockMvc.perform(get("/api/quotes/{id}", quoteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(quoteId))
                .andExpect(jsonPath("$.customerFirstName").value("John"))
                .andExpect(jsonPath("$.customerLastName").value("Doe"))
                .andExpect(jsonPath("$.customerEmail").value("john.doe@example.com"))
                .andExpect(jsonPath("$.insuranceType").value("AUTO"))
                .andExpect(jsonPath("$.vehicleYear").value(2020))
                .andExpect(jsonPath("$.vehicleMake").value("Toyota"))
                .andExpect(jsonPath("$.vehicleModel").value("Camry"))
                .andExpect(jsonPath("$.coverageLevel").value("LIABILITY"))
                .andExpect(jsonPath("$.deductibleAmount").value(1000.00))
                .andExpect(jsonPath("$.monthlyPremium").isNumber())
                .andExpect(jsonPath("$.annualPremium").isNumber())
                .andExpect(jsonPath("$.status").value("QUOTED"));

        // Verify calculation: Base 100 * Coverage 1.0 * (1 - 0.05 discount) * Vehicle factor 1.1 (4 years old)
        // Monthly: 100 * 1.0 * 0.95 * 1.1 = 104.50
        // Annual: 104.50 * 12 * 0.95 = 1191.30
        BigDecimal expectedMonthly = new BigDecimal("104.50");
        BigDecimal expectedAnnual = new BigDecimal("1191.30");
        
        assertThat(createdQuote.getMonthlyPremium()).isEqualByComparingTo(expectedMonthly);
        assertThat(createdQuote.getAnnualPremium()).isEqualByComparingTo(expectedAnnual);
    }

    @Test
    @DisplayName("Full flow: Create HOME quote, retrieve it, verify calculation")
    void testCreateAndRetrieveHomeQuote() throws Exception {
        QuoteRequest request = TestDataFactory.homeQuoteRequest()
                .withCoverageLevel(CoverageLevel.COMPREHENSIVE)
                .withDeductibleAmount(new BigDecimal("2500.00"))
                .withPropertySize(2000)
                .build();

        // Create quote
        MvcResult createResult = mockMvc.perform(post("/api/quotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.insuranceType").value("HOME"))
                .andExpect(jsonPath("$.status").value("QUOTED"))
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        QuoteResponse createdQuote = objectMapper.readValue(responseBody, QuoteResponse.class);
        Long quoteId = createdQuote.getId();

        assertThat(quoteId).isNotNull();

        // Retrieve quote
        mockMvc.perform(get("/api/quotes/{id}", quoteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(quoteId))
                .andExpect(jsonPath("$.insuranceType").value("HOME"))
                .andExpect(jsonPath("$.propertyType").value("Single Family"))
                .andExpect(jsonPath("$.propertySize").value(2000))
                .andExpect(jsonPath("$.yearBuilt").value(2000))
                .andExpect(jsonPath("$.coverageLevel").value("COMPREHENSIVE"))
                .andExpect(jsonPath("$.deductibleAmount").value(2500.00))
                .andExpect(jsonPath("$.status").value("QUOTED"));

        // Verify calculation: Base 150 * Coverage 1.5 * (1 - 0.10 discount) * Property factor 1.5
        BigDecimal expectedMonthly = new BigDecimal("303.75");
        BigDecimal expectedAnnual = expectedMonthly.multiply(new BigDecimal("12"))
                .multiply(new BigDecimal("0.95"));
        
        assertThat(createdQuote.getMonthlyPremium()).isEqualByComparingTo(expectedMonthly);
        assertThat(createdQuote.getAnnualPremium()).isEqualByComparingTo(expectedAnnual);
    }

    @Test
    @DisplayName("List all quotes")
    void testListAllQuotes() throws Exception {
        // Create two quotes
        QuoteRequest autoRequest = TestDataFactory.autoQuoteRequest().build();
        QuoteRequest homeRequest = TestDataFactory.homeQuoteRequest().build();

        mockMvc.perform(post("/api/quotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(autoRequest)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/quotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(homeRequest)))
                .andExpect(status().isCreated());

        // List all quotes
        mockMvc.perform(get("/api/quotes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].insuranceType").exists())
                .andExpect(jsonPath("$[1].insuranceType").exists());
    }

    @Test
    @DisplayName("Filter quotes by insurance type")
    void testFilterQuotesByInsuranceType() throws Exception {
        // Create AUTO and HOME quotes
        QuoteRequest autoRequest = TestDataFactory.autoQuoteRequest().build();
        QuoteRequest homeRequest = TestDataFactory.homeQuoteRequest().build();

        mockMvc.perform(post("/api/quotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(autoRequest)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/quotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(homeRequest)))
                .andExpect(status().isCreated());

        // Filter by AUTO
        mockMvc.perform(get("/api/quotes")
                .param("insuranceType", "AUTO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].insuranceType").value("AUTO"));

        // Filter by HOME
        mockMvc.perform(get("/api/quotes")
                .param("insuranceType", "HOME"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].insuranceType").value("HOME"));
    }

    @Test
    @DisplayName("Filter quotes by status")
    void testFilterQuotesByStatus() throws Exception {
        QuoteRequest request = TestDataFactory.autoQuoteRequest().build();

        mockMvc.perform(post("/api/quotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Filter by QUOTED status
        mockMvc.perform(get("/api/quotes")
                .param("status", "QUOTED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("QUOTED"));
    }

    @Test
    @DisplayName("Update quote and verify recalculation")
    void testUpdateQuote() throws Exception {
        QuoteRequest initialRequest = TestDataFactory.autoQuoteRequest()
                .withCoverageLevel(CoverageLevel.LIABILITY)
                .build();

        // Create initial quote
        MvcResult createResult = mockMvc.perform(post("/api/quotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(initialRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        QuoteResponse initialQuote = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), QuoteResponse.class);
        Long quoteId = initialQuote.getId();

        // Update to PREMIUM coverage
        QuoteRequest updateRequest = TestDataFactory.autoQuoteRequest()
                .withCoverageLevel(CoverageLevel.PREMIUM)
                .build();

        mockMvc.perform(put("/api/quotes/{id}", quoteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(quoteId))
                .andExpect(jsonPath("$.coverageLevel").value("PREMIUM"))
                .andExpect(jsonPath("$.monthlyPremium").isNumber())
                .andExpect(jsonPath("$.annualPremium").isNumber());

        // Verify the premium was recalculated (PREMIUM coverage should be higher)
        MvcResult updateResult = mockMvc.perform(get("/api/quotes/{id}", quoteId))
                .andExpect(status().isOk())
                .andReturn();

        QuoteResponse updatedQuote = objectMapper.readValue(
                updateResult.getResponse().getContentAsString(), QuoteResponse.class);

        assertThat(updatedQuote.getMonthlyPremium()).isGreaterThan(initialQuote.getMonthlyPremium());
        assertThat(updatedQuote.getCoverageLevel()).isEqualTo(CoverageLevel.PREMIUM);
    }

    @Test
    @DisplayName("Delete quote")
    void testDeleteQuote() throws Exception {
        QuoteRequest request = TestDataFactory.autoQuoteRequest().build();

        // Create quote
        MvcResult createResult = mockMvc.perform(post("/api/quotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        QuoteResponse createdQuote = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), QuoteResponse.class);
        Long quoteId = createdQuote.getId();

        // Delete quote
        mockMvc.perform(delete("/api/quotes/{id}", quoteId))
                .andExpect(status().isNoContent());

        // Verify quote is deleted
        mockMvc.perform(get("/api/quotes/{id}", quoteId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get non-existent quote returns 404")
    void testGetNonExistentQuote() throws Exception {
        mockMvc.perform(get("/api/quotes/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Create quote with invalid data returns 400")
    void testCreateQuoteWithInvalidData() throws Exception {
        QuoteRequest invalidRequest = TestDataFactory.autoQuoteRequest()
                .withCustomerEmail("invalid-email")
                .build();

        mockMvc.perform(post("/api/quotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
