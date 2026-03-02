# Routing Rules

## Domain Routing

| Domain | Primary Agent | Backup |
|--------|--------------|--------|
| Architecture, design decisions, code review | Keaton | — |
| React, UI, components, styling, frontend | Verbal | Keaton |
| Java, APIs, services, database, backend | Fenster | Keaton |
| Terraform, Azure, CI/CD, GitHub Actions, infrastructure, deployment | McManus | Keaton |
| Tests, QA, quality, edge cases, test plans | Hockney | Keaton |
| Autonomous issue work, bug fixes, small features, tests, docs | @copilot | Keaton |

## File Routing

| Pattern | Agent |
|---------|-------|
| `src/main/java/**`, `pom.xml`, `build.gradle` | Fenster |
| `src/frontend/**`, `*.jsx`, `*.tsx`, `*.css`, `package.json` | Verbal |
| `*.tf`, `*.tfvars`, `.github/workflows/**` | McManus |
| `*test*`, `*spec*`, `*Test.java` | Hockney |

## Reviewer Gates

| Artifact | Reviewer |
|----------|----------|
| Architecture proposals | Keaton |
| Frontend code | Keaton (review), Hockney (test) |
| Backend code | Keaton (review), Hockney (test) |
| Infrastructure code | Keaton (review) |
| Test plans | Keaton |
