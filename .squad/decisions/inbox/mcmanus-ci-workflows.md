# Decision: CI Workflow Conventions

**Author:** McManus (⚙️ DevOps)
**Date:** 2025-07-15
**Scope:** CI/CD

## Decision
- CI workflows use **path-based triggers** scoped to `frontend/` and `backend/` directories to avoid unnecessary runs.
- Frontend CI uses **Node.js 20 LTS** with built-in npm caching via `actions/setup-node@v4`.
- Backend CI uses **Java 17 Temurin** with built-in Maven caching via `actions/setup-java@v4`.
- Backend verification runs `mvn clean verify` which includes compile, test, checkstyle, and spotbugs in a single command.
- Frontend test step is intentionally omitted until a test framework is added to `package.json`.

## Rationale
- Path-scoped triggers keep CI fast and cost-effective — no reason to build backend when only frontend changes.
- Built-in caching from official setup actions is simpler and more reliable than manual cache steps.
- `mvn clean verify` is the idiomatic Maven lifecycle for CI — it runs all quality gates in dependency order.
