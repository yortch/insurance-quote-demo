# Verbal — History

## Project Context
- **Project:** Three Rivers Insurance — Insurance Quote Website
- **Frontend:** ReactJS
- **Backend:** Java
- **Infrastructure:** Azure with Terraform
- **CI/CD:** GitHub Actions
- **User:** Jorge Balderas

## Learnings
- Frontend lives at `frontend/` in repo root, scaffolded with Vite + React (v19)
- Vite 7.x used (declined Vite 8 beta)
- ESLint flat config at `frontend/eslint.config.js` with Prettier integration via `eslint-plugin-prettier`
- Prettier config at `frontend/.prettierrc`: single quotes, semicolons, trailing commas, 100 char width
- npm scripts: `dev`, `build`, `lint`, `lint:fix`, `format`, `preview`
- Dev server runs on port 5173 by default
- Directory structure: `src/components/`, `src/pages/`, `src/services/`, `src/hooks/`, `src/styles/`
- Brand color: `#1a5276` (deep blue) used in styles
- PR #4 covers Phase 1 Tasks 1.1 and 1.6
