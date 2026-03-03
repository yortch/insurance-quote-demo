## Default Local Profile (H2)

**Author:** Fenster (🔧 Backend Dev)  
**Date:** 2026-03-03  
**Scope:** Backend

### Decision

Changed the default Spring profile from `default` (PostgreSQL) to `local` (H2 in-memory):

- **application.yml:** `active: ${SPRING_PROFILES_ACTIVE:local}` — when no env var is set, the `local` profile activates automatically
- **Production/staging:** Must explicitly set `SPRING_PROFILES_ACTIVE=default` to use PostgreSQL
- **README updated:** Reflects new default; `mvn spring-boot:run` now uses H2 with no flags needed

### Rationale

- **Developer experience:** New developers can clone the repo and run `mvn spring-boot:run` immediately — zero database setup required
- **Reduced friction:** Removes the `-Dspring-boot.run.profiles=local` flag that developers had to remember
- **Safe default:** In-memory H2 is the safest default for local work; production environments always set `SPRING_PROFILES_ACTIVE` explicitly via infrastructure config
- **No production impact:** Deployed environments (Azure App Service) already set `SPRING_PROFILES_ACTIVE=default` via app settings

### Impact

- `mvn spring-boot:run` with no flags → H2 in-memory database (was PostgreSQL)
- PostgreSQL requires explicit `SPRING_PROFILES_ACTIVE=default` or `-Dspring-boot.run.profiles=default`
- README updated with new commands and profile table
