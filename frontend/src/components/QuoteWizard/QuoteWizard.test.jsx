import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import QuoteWizard from './QuoteWizard';

describe('QuoteWizard', () => {
  it('renders step 1 (Customer Info) initially', () => {
    render(<QuoteWizard />);
    expect(screen.getByText('Customer Information')).toBeInTheDocument();
  });

  it('shows correct step labels in stepper', () => {
    render(<QuoteWizard />);
    expect(screen.getByText('Customer Info')).toBeInTheDocument();
    expect(screen.getByText('Property/Vehicle')).toBeInTheDocument();
    expect(screen.getByText('Coverage')).toBeInTheDocument();
    expect(screen.getByText('Review')).toBeInTheDocument();
  });

  it('marks current step as active', () => {
    render(<QuoteWizard />);
    const steps = document.querySelectorAll('.step');
    expect(steps[0]).toHaveClass('active');
    expect(steps[1]).not.toHaveClass('active');
  });

  it('navigates to step 2 when Next is clicked with valid data', () => {
    render(<QuoteWizard />);
    
    // Fill in required fields
    fireEvent.change(screen.getByPlaceholderText('John'), { target: { value: 'John' } });
    fireEvent.change(screen.getByPlaceholderText('Doe'), { target: { value: 'Doe' } });
    fireEvent.change(screen.getByPlaceholderText('john.doe@example.com'), { target: { value: 'john@example.com' } });
    fireEvent.change(screen.getByPlaceholderText('555-123-4567'), { target: { value: '555-123-4567' } });
    fireEvent.change(screen.getByPlaceholderText('123 Main St'), { target: { value: '123 Main St' } });
    fireEvent.change(screen.getByPlaceholderText('Springfield'), { target: { value: 'Springfield' } });
    fireEvent.change(screen.getByDisplayValue('Select a state'), { target: { value: 'CA' } });
    fireEvent.change(screen.getByPlaceholderText('12345'), { target: { value: '12345' } });
    
    fireEvent.click(screen.getByText('Next'));
    
    // Should show step 2
    expect(screen.getByText('Property/Vehicle Details')).toBeInTheDocument();
  });

  it('marks completed steps in stepper', () => {
    render(<QuoteWizard />);
    
    // Fill in step 1 and proceed
    fireEvent.change(screen.getByPlaceholderText('John'), { target: { value: 'John' } });
    fireEvent.change(screen.getByPlaceholderText('Doe'), { target: { value: 'Doe' } });
    fireEvent.change(screen.getByPlaceholderText('john.doe@example.com'), { target: { value: 'john@example.com' } });
    fireEvent.change(screen.getByPlaceholderText('555-123-4567'), { target: { value: '555-123-4567' } });
    fireEvent.change(screen.getByPlaceholderText('123 Main St'), { target: { value: '123 Main St' } });
    fireEvent.change(screen.getByPlaceholderText('Springfield'), { target: { value: 'Springfield' } });
    fireEvent.change(screen.getByDisplayValue('Select a state'), { target: { value: 'CA' } });
    fireEvent.change(screen.getByPlaceholderText('12345'), { target: { value: '12345' } });
    fireEvent.click(screen.getByText('Next'));
    
    const steps = document.querySelectorAll('.step');
    expect(steps[0]).toHaveClass('completed');
    expect(steps[1]).toHaveClass('active');
  });

  it('navigates back from step 2 to step 1', () => {
    render(<QuoteWizard />);
    
    // Fill and proceed to step 2
    fireEvent.change(screen.getByPlaceholderText('John'), { target: { value: 'John' } });
    fireEvent.change(screen.getByPlaceholderText('Doe'), { target: { value: 'Doe' } });
    fireEvent.change(screen.getByPlaceholderText('john.doe@example.com'), { target: { value: 'john@example.com' } });
    fireEvent.change(screen.getByPlaceholderText('555-123-4567'), { target: { value: '555-123-4567' } });
    fireEvent.change(screen.getByPlaceholderText('123 Main St'), { target: { value: '123 Main St' } });
    fireEvent.change(screen.getByPlaceholderText('Springfield'), { target: { value: 'Springfield' } });
    fireEvent.change(screen.getByDisplayValue('Select a state'), { target: { value: 'CA' } });
    fireEvent.change(screen.getByPlaceholderText('12345'), { target: { value: '12345' } });
    fireEvent.click(screen.getByText('Next'));
    
    // Click Back
    fireEvent.click(screen.getByText('Back'));
    
    // Should show step 1
    expect(screen.getByText('Customer Information')).toBeInTheDocument();
  });
});
