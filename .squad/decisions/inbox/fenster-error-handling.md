# Backend Error Handling Pattern

**Author:** Fenster (🔧 Backend Dev)  
**Date:** 2025-03-02  
**Scope:** Backend

## Decision

Implemented centralized error handling for the REST API:

- **GlobalExceptionHandler** — @RestControllerAdvice class handles all controller exceptions in one place
  - `MethodArgumentNotValidException` → 400 Bad Request with field-level error details
  - `QuoteNotFoundException` → 404 Not Found
  - Generic `Exception` → 500 Internal Server Error with safe message (no stack traces exposed)

- **ErrorResponse DTO** — Consistent error response format across all endpoints:
  ```json
  {
    "status": 400,
    "error": "Validation Failed",
    "message": "Invalid input data",
    "details": ["field: error message"],
    "timestamp": "2025-03-02T18:49:00"
  }
  ```

- **Custom exceptions** — Service layer throws domain exceptions (e.g., `QuoteNotFoundException`); controller layer stays clean and delegates to global handler

- **CORS configuration** — WebConfig implements WebMvcConfigurer to allow frontend dev server (localhost:5173) access to API endpoints

## Rationale

- **Separation of concerns** — Controllers focus on routing; exception handling is centralized
- **Consistent API responses** — All errors follow the same JSON structure, making frontend error handling predictable
- **Security** — Generic exceptions return safe messages without exposing internal details or stack traces
- **DRY principle** — No repetitive try-catch blocks in controllers; single handler manages all error scenarios
- **Spring best practices** — Leverages @RestControllerAdvice and @ExceptionHandler for clean, idiomatic error handling
- **Frontend integration** — CORS configuration enables local React dev server to call backend APIs during development

## Impact

- Controllers simplified — removed null checks and ResponseEntity.notFound() logic
- Service layer throws exceptions instead of returning null — clearer contract and easier to reason about
- Frontend can parse consistent error responses for user-friendly error messages
- SpotBugs compliance — avoided dead store warnings by not assigning unused variables
