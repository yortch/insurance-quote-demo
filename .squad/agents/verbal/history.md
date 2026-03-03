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
- PR #9 covers Phase 2 Tasks 2.4, 2.5, 2.6, 2.7, 2.8 (frontend wizard)
- Multi-step wizard pattern: parent component manages state, passes down via props to step components
- Form validation: inline validation on submit, error messages display below fields
- State management: useState for wizard state (currentStep, formData), updateFormData callback pattern
- Conditional rendering: toggle between AUTO/HOME insurance fields based on insuranceType
- Card-based selection UI for coverage levels with visual feedback (selected state, hover effects)
- CSS organization: component-specific CSS modules alongside components (e.g., QuoteWizard.css, CoverageSelection.css)
- Responsive design: grid layouts with media queries, mobile-first approach
- Professional insurance styling: blue (#1a5276) primary, green (#27ae60) for CTAs, clean cards with shadows
- Client-side quote calculation: base rate + multipliers for age/size/coverage/deductible
- Stepper UI: visual progress indicator with numbered circles, active/completed states
- PR #14 covers Phase 4 Task 4.2 (frontend component and unit tests)
- Test infrastructure: Vitest 3.x with jsdom environment for React component testing
- Testing Library stack: @testing-library/react, @testing-library/jest-dom, @testing-library/user-event
- Test setup: `frontend/src/test/setup.js` imports jest-dom matchers globally
- Vite config extended with `test` block: globals: true, environment: 'jsdom', setupFiles
- Test scripts: `npm test` (run once), `npm test:watch` (watch mode)
- Test organization: co-located with source files (.test.jsx alongside .jsx components)
- Test coverage: 44 tests across QuoteWizard (6), CustomerInfo (11), CoverageSelection (13), quoteApi (14)
- Component testing pattern: render component, query elements, simulate user events with fireEvent, assert state/DOM
- API testing pattern: mock global.fetch with vi.fn(), test success/error responses, verify fetch calls
- Form validation testing: submit empty/invalid forms, assert error messages appear, verify nextStep not called
- Navigation testing: fill valid data, click Next/Back, assert correct step rendered
- All tests pass consistently with fast execution (~5s full suite)
