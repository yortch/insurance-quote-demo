# Decision: Backend Scaffold Choices

**Author:** Fenster (Backend Dev)
**Date:** 2026-03-02

## Context
Scaffolded the Spring Boot backend for the Three Rivers Insurance Quote project.

## Decisions
1. **Spring Boot 3.4.1** with **Java 17** — LTS baseline, widely supported.
2. **Package structure:** `com.threeriverinsurance.{controller,service,model,repository,config}` — standard layered architecture.
3. **PostgreSQL** for production, **H2** for tests — keeps test runs fast and CI-friendly with no external DB dependency.
4. **Checkstyle** uses a custom Google-ish ruleset at `backend/checkstyle.xml`; **SpotBugs** at max effort / medium threshold.
5. **application.yml** uses Spring multi-document format (`---`) for profile separation rather than separate files.
6. **Server port** defaults to 8080; DB credentials use env vars with fallback defaults for local dev.
