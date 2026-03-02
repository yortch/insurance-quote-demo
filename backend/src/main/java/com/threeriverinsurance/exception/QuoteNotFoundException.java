package com.threeriverinsurance.exception;

/**
 * Exception thrown when a quote is not found by ID.
 */
public class QuoteNotFoundException extends RuntimeException {

    public QuoteNotFoundException(Long id) {
        super("Quote not found with ID: " + id);
    }
}
