package com.threeriverinsurance;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threeriverinsurance.model.QuoteRequest;
import com.threeriverinsurance.testutil.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Security Integration Tests")
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("API endpoints include security headers")
    void testSecurityHeaders() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Frame-Options", "SAMEORIGIN"))
                .andExpect(header().exists("Content-Security-Policy"));
    }

    @Test
    @DisplayName("POST endpoint with valid data returns 201 with security headers")
    void testSecurityHeadersOnPost() throws Exception {
        QuoteRequest request = TestDataFactory.autoQuoteRequest().build();

        mockMvc.perform(post("/api/quotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("X-Frame-Options", "SAMEORIGIN"))
                .andExpect(header().exists("Content-Security-Policy"));
    }

    @Test
    @DisplayName("POST endpoint with invalid data returns 400 Bad Request")
    void testValidationWithSecurityHeaders() throws Exception {
        QuoteRequest invalidRequest = TestDataFactory.autoQuoteRequest()
                .withCustomerEmail("not-an-email")
                .build();

        mockMvc.perform(post("/api/quotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(header().exists("X-Frame-Options"));
    }

    @Test
    @DisplayName("Swagger UI endpoints are accessible")
    void testSwaggerAccessible() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }
}
