# Backend Core Architecture

**Author:** Fenster (🔧 Backend Dev)  
**Date:** 2025-03-02  
**Scope:** Backend

## Decision

### Data Model & Persistence
- **JPA entities** with validation annotations (@NotNull, @NotBlank, @Email) to enforce constraints at application level
- **Snake_case column names** (customer_first_name) for database portability across PostgreSQL, MySQL, etc.
- **Separate DTOs** (QuoteRequest, QuoteResponse) from entities — keeps API contracts clean and independent of persistence
- **QuoteMapper utility** for entity-DTO conversion — simple static methods, no heavy framework overhead
- **Spring Data JPA** repositories with custom query methods (findByCustomerEmail, findByStatus, findByInsuranceType)

### Service Layer Design
- **Two-service pattern:**
  - `QuoteCalculationService` — pure business logic for premium calculation
  - `QuoteService` — orchestrates CRUD operations, delegates to calculation service
- **Separation of concerns:** calculation logic is testable and reusable without touching persistence

### Premium Calculation Logic
- **BigDecimal for money** — avoids floating-point precision issues, uses explicit scale (2) and rounding (HALF_UP)
- **Factor-based pricing:** base rate × coverage multiplier × type factor × (1 - deductible discount)
  - Auto base: $100/month, Home base: $150/month
  - Coverage: LIABILITY=1.0x, COMPREHENSIVE=1.5x, PREMIUM=2.0x
  - Deductibles: $500=0%, $1000=5% off, $2500=10% off, $5000=15% off
  - Auto age factor: 0-3 yrs=+20%, 4-7 yrs=+10%, 8+ yrs=0%
  - Home size: +$0.05/month per 100 sq ft over 1000 sq ft
- **Annual discount:** 5% off when paying annually (monthly × 12 × 0.95)

### REST API Conventions
- **Resource-based URLs:** `/api/quotes`
- **HTTP semantics:** POST=201 Created, GET=200 OK, PUT=200 OK, DELETE=204 No Content
- **Validation:** @Valid on request bodies triggers automatic 400 Bad Request for constraint violations
- **Optional filters:** query params for status and insuranceType on GET /api/quotes

### API Documentation
- **Swagger/OpenAPI:** springdoc-openapi-starter-webmvc-ui auto-generates docs at `/swagger-ui.html`
- **Annotations:** @Operation, @ApiResponses, @ApiResponse on controller methods for clear endpoint docs

### Code Quality
- **SpotBugs exclusion:** Spring constructor injection triggers false positive EI_EXPOSE_REP2 — suppressed for controller/service packages via spotbugs-exclude.xml
- **Checkstyle + SpotBugs:** bound to verify phase, enforces Google-ish Java style and bug detection

### Database Schema Management
- **Dev mode:** `ddl-auto: update` — Hibernate auto-creates/updates schema for rapid iteration
- **Production:** `ddl-auto: validate` — requires explicit migrations (Flyway/Liquibase future work)
- **Test mode:** `ddl-auto: create-drop` with H2 in-memory DB

## Rationale

- **DTO separation:** API contracts shouldn't change when we tweak the database schema
- **BigDecimal:** Industry standard for financial calculations — no rounding errors
- **Two-service pattern:** Calculation logic is complex enough to warrant isolation; makes testing easier
- **Swagger auto-gen:** Keeps docs in sync with code; no manual YAML editing
- **Factor-based pricing:** Transparent, auditable, easy to adjust individual factors without rewriting formulas

---

**Working as Fenster (Backend Dev)**  
Part of #1 — Phase 2: Backend Core
