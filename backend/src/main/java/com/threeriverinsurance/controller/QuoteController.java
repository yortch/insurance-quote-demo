package com.threeriverinsurance.controller;

import com.threeriverinsurance.model.InsuranceType;
import com.threeriverinsurance.model.Quote;
import com.threeriverinsurance.model.QuoteMapper;
import com.threeriverinsurance.model.QuoteRequest;
import com.threeriverinsurance.model.QuoteResponse;
import com.threeriverinsurance.model.QuoteStatus;
import com.threeriverinsurance.service.QuoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for insurance quote operations.
 */
@RestController
@RequestMapping("/api/quotes")
public class QuoteController {

    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    /**
     * Creates and calculates a new insurance quote.
     *
     * @param request the quote request
     * @return the created quote response
     */
    @Operation(summary = "Create a new quote", description = "Creates and calculates a new insurance quote")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Quote created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<QuoteResponse> createQuote(@Valid @RequestBody QuoteRequest request) {
        Quote quote = quoteService.createQuote(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(QuoteMapper.toResponse(quote));
    }

    /**
     * Gets a specific quote by ID.
     *
     * @param id the quote ID
     * @return the quote response
     */
    @Operation(summary = "Get a quote by ID", description = "Retrieves a specific insurance quote by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quote found"),
        @ApiResponse(responseCode = "404", description = "Quote not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<QuoteResponse> getQuote(@PathVariable Long id) {
        Quote quote = quoteService.getQuote(id);
        if (quote == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(QuoteMapper.toResponse(quote));
    }

    /**
     * Lists all quotes with optional filters.
     *
     * @param status optional status filter
     * @param insuranceType optional insurance type filter
     * @return list of quote responses
     */
    @Operation(summary = "List all quotes", description = "Retrieves all quotes with optional status and type filters")
    @ApiResponse(responseCode = "200", description = "Quotes retrieved successfully")
    @GetMapping
    public ResponseEntity<List<QuoteResponse>> getAllQuotes(
            @RequestParam(required = false) QuoteStatus status,
            @RequestParam(required = false) InsuranceType insuranceType) {
        List<Quote> quotes = quoteService.getAllQuotes(status, insuranceType);
        List<QuoteResponse> responses = quotes.stream()
                .map(QuoteMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Updates an existing quote and recalculates premiums.
     *
     * @param id the quote ID
     * @param request the updated quote request
     * @return the updated quote response
     */
    @Operation(summary = "Update a quote", description = "Updates an existing quote and recalculates premiums")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quote updated successfully"),
        @ApiResponse(responseCode = "404", description = "Quote not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<QuoteResponse> updateQuote(
            @PathVariable Long id,
            @Valid @RequestBody QuoteRequest request) {
        Quote quote = quoteService.updateQuote(id, request);
        if (quote == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(QuoteMapper.toResponse(quote));
    }

    /**
     * Deletes a quote by ID.
     *
     * @param id the quote ID
     * @return no content response
     */
    @Operation(summary = "Delete a quote", description = "Deletes an insurance quote by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Quote deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Quote not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuote(@PathVariable Long id) {
        quoteService.deleteQuote(id);
        return ResponseEntity.noContent().build();
    }
}
