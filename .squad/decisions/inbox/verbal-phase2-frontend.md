# Phase 2 Frontend — Quote Wizard Architecture

**Author:** Verbal (⚛️ Frontend Dev)  
**Date:** 2025-07-14  
**Scope:** Frontend — Quote wizard multi-step form

## Decision

Implemented a multi-step quote wizard with the following architecture:

### Component Structure
- **QuoteWizard.jsx** — Parent container managing wizard state and navigation
- **Step components** — Separate components for each step (CustomerInfo, PropertyVehicleDetails, CoverageSelection, QuoteReview)
- **Shared CSS** — QuoteWizard.css provides shared form styling, step-specific CSS for complex components

### State Management Pattern
- Parent component holds all form data in single state object
- `updateFormData(field, value)` callback pattern for child components to update state
- `goToStep(number)` enables jumping to specific steps from review page
- Navigation functions: `nextStep()`, `prevStep()` for wizard flow

### Form Validation
- Client-side validation on "Next" button click in each step
- Inline error messages below each field
- Error state styling on form groups
- Validation patterns: email regex, phone regex, zip code regex, year ranges

### Conditional Rendering
- Step 2 toggles between AUTO and HOME insurance fields based on `insuranceType`
- Radio buttons control which field set is displayed
- Validation adapts to active insurance type

### Coverage Selection UI
- Card-based selection interface (not dropdown/radio)
- Visual feedback: hover effects, selected state, popular badge
- Features list in each card to show coverage details
- Clicking anywhere on card selects that coverage level

### Quote Calculation
- Client-side estimate using base rate + multipliers
- Factors: vehicle/property age, size, coverage level, deductible amount
- Display both monthly and annual premiums
- Disclaimer about estimated vs final rates

### Styling Approach
- No CSS framework — custom CSS for full control
- Professional insurance company aesthetic
- Responsive grid layouts with media queries
- Color scheme: primary blue (#1a5276), CTA green (#27ae60), neutral grays
- Card-based UI with shadows and hover states
- Stepper progress indicator with numbered circles

## Rationale

- **Single state object** — Simplifies data management across steps, easier to submit to API later
- **Callback pattern** — Clean props interface, explicit data flow
- **Inline validation** — Immediate feedback improves UX
- **Card-based coverage UI** — More engaging than dropdowns, shows value proposition
- **Client-side calculation** — Provides instant feedback, will be replaced with API call in Phase 2.9
- **No CSS framework** — Keeps bundle small, full styling control for insurance brand
- **Stepper UI** — Clear progress indication builds trust in multi-step forms

## Related Files
- `frontend/src/components/QuoteWizard/QuoteWizard.jsx`
- `frontend/src/components/QuoteWizard/QuoteWizard.css`
- `frontend/src/components/QuoteWizard/steps/CustomerInfo.jsx`
- `frontend/src/components/QuoteWizard/steps/PropertyVehicleDetails.jsx`
- `frontend/src/components/QuoteWizard/steps/CoverageSelection.jsx`
- `frontend/src/components/QuoteWizard/steps/CoverageSelection.css`
- `frontend/src/components/QuoteWizard/steps/QuoteReview.jsx`
- `frontend/src/components/QuoteWizard/steps/QuoteReview.css`

## Next Steps
- Task 2.9: Connect Step 4 "Get Quote" button to backend API
- Add loading states during API calls
- Handle API errors gracefully
- Consider adding form field persistence (localStorage) for partial completion
