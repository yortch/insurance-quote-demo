# LoadingSpinner Component Pattern

**Author:** Verbal (⚛️ Frontend Dev)  
**Date:** 2025-03-03  
**Scope:** Frontend / UI Components

## Decision
Created a reusable `LoadingSpinner` component at `src/components/LoadingSpinner/` with:
- Accessibility-first design (role="status", aria-live="polite")
- Customizable message prop
- Brand-aligned styling (uses #1a5276 brand color)
- Co-located CSS pattern

## Rationale
- **Reusability**: Single source of truth for loading states across the app
- **Accessibility**: Screen reader support via proper ARIA attributes
- **Consistency**: Matches brand colors and design system
- **Performance**: Lightweight, no external dependencies

## Usage Pattern
```jsx
import LoadingSpinner from '../LoadingSpinner/LoadingSpinner';

// In component:
{isLoading && <LoadingSpinner message="Getting your quote..." />}
```

## Related Tasks
Part of Phase 4 Tasks 4.7 (UI Polish), 4.8 (Accessibility), 4.9 (Performance)
