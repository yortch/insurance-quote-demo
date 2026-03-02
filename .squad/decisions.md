# Decisions

> Canonical decision ledger. Append-only. Agents write to `.squad/decisions/inbox/`, Scribe merges here.

---

## React Scaffold with Vite

**Author:** Verbal (⚛️ Frontend Dev)  
**Date:** 2025-07-14  
**Scope:** Frontend

### Decision
- Used **Vite 7.x** (stable) as the React build tool, declined Vite 8 beta
- Used **ESLint flat config** (`eslint.config.js`) with Prettier integration via `eslint-plugin-prettier` + `eslint-config-prettier`
- Prettier rules: single quotes, semicolons, trailing commas, 100 char print width
- Brand color `#1a5276` (deep blue) for Three Rivers Insurance — used in global styles and buttons
- Light theme only for initial scaffold (no dark mode toggle yet)

### Rationale
- Vite 7 is stable and production-ready; Vite 8 is experimental
- ESLint flat config is the modern standard going forward (ESLint 9+)
- Prettier integration via ESLint plugin catches formatting issues during lint, reducing CI friction

---
