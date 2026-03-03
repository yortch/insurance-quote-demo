import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import CoverageSelection from './CoverageSelection';

describe('CoverageSelection', () => {
  const mockFormData = {
    coverageLevel: 'COMPREHENSIVE',
    deductible: '1000',
  };

  const mockUpdateFormData = vi.fn();
  const mockNextStep = vi.fn();
  const mockPrevStep = vi.fn();

  it('renders coverage level options', () => {
    render(
      <CoverageSelection
        formData={mockFormData}
        updateFormData={mockUpdateFormData}
        nextStep={mockNextStep}
        prevStep={mockPrevStep}
      />
    );

    expect(screen.getByText('Coverage Selection')).toBeInTheDocument();
    expect(screen.getByText('Basic Coverage')).toBeInTheDocument();
    expect(screen.getByText('Full Protection')).toBeInTheDocument();
    expect(screen.getByText('Complete Peace of Mind')).toBeInTheDocument();
  });

  it('shows popular badge on comprehensive coverage', () => {
    render(
      <CoverageSelection
        formData={mockFormData}
        updateFormData={mockUpdateFormData}
        nextStep={mockNextStep}
        prevStep={mockPrevStep}
      />
    );

    expect(screen.getByText('Most Popular')).toBeInTheDocument();
  });

  it('renders deductible options', () => {
    render(
      <CoverageSelection
        formData={mockFormData}
        updateFormData={mockUpdateFormData}
        nextStep={mockNextStep}
        prevStep={mockPrevStep}
      />
    );

    expect(screen.getByDisplayValue('$1,000')).toBeInTheDocument();
    expect(screen.getByText('$500')).toBeInTheDocument();
    expect(screen.getByText('$2,500')).toBeInTheDocument();
    expect(screen.getByText('$5,000')).toBeInTheDocument();
  });

  it('marks selected coverage level', () => {
    render(
      <CoverageSelection
        formData={mockFormData}
        updateFormData={mockUpdateFormData}
        nextStep={mockNextStep}
        prevStep={mockPrevStep}
      />
    );

    const coverageCards = document.querySelectorAll('.coverage-card');
    expect(coverageCards[1]).toHaveClass('selected'); // COMPREHENSIVE is index 1
  });

  it('calls updateFormData when coverage level is clicked', () => {
    render(
      <CoverageSelection
        formData={mockFormData}
        updateFormData={mockUpdateFormData}
        nextStep={mockNextStep}
        prevStep={mockPrevStep}
      />
    );

    const liabilityCard = screen.getByText('Basic Coverage').closest('.coverage-card');
    fireEvent.click(liabilityCard);

    expect(mockUpdateFormData).toHaveBeenCalledWith('coverageLevel', 'LIABILITY');
  });

  it('calls updateFormData when coverage level radio is changed', () => {
    render(
      <CoverageSelection
        formData={mockFormData}
        updateFormData={mockUpdateFormData}
        nextStep={mockNextStep}
        prevStep={mockPrevStep}
      />
    );

    const radios = screen.getAllByRole('radio');
    fireEvent.change(radios[0], { target: { value: 'LIABILITY' } });

    expect(mockUpdateFormData).toHaveBeenCalledWith('coverageLevel', 'LIABILITY');
  });

  it('calls updateFormData when deductible changes', () => {
    render(
      <CoverageSelection
        formData={mockFormData}
        updateFormData={mockUpdateFormData}
        nextStep={mockNextStep}
        prevStep={mockPrevStep}
      />
    );

    const deductibleSelect = screen.getByDisplayValue('$1,000');
    fireEvent.change(deductibleSelect, { target: { value: '2500' } });

    expect(mockUpdateFormData).toHaveBeenCalledWith('deductible', '2500');
  });

  it('calls nextStep when Next button is clicked', () => {
    render(
      <CoverageSelection
        formData={mockFormData}
        updateFormData={mockUpdateFormData}
        nextStep={mockNextStep}
        prevStep={mockPrevStep}
      />
    );

    fireEvent.click(screen.getByText('Next'));

    expect(mockNextStep).toHaveBeenCalled();
  });

  it('calls prevStep when Back button is clicked', () => {
    render(
      <CoverageSelection
        formData={mockFormData}
        updateFormData={mockUpdateFormData}
        nextStep={mockNextStep}
        prevStep={mockPrevStep}
      />
    );

    fireEvent.click(screen.getByText('Back'));

    expect(mockPrevStep).toHaveBeenCalled();
  });

  it('shows coverage features for each level', () => {
    render(
      <CoverageSelection
        formData={mockFormData}
        updateFormData={mockUpdateFormData}
        nextStep={mockNextStep}
        prevStep={mockPrevStep}
      />
    );

    // LIABILITY features
    expect(screen.getByText('✓ Third-party liability')).toBeInTheDocument();
    
    // COMPREHENSIVE features
    expect(screen.getByText('✓ Collision coverage')).toBeInTheDocument();
    
    // PREMIUM features
    expect(screen.getByText('✓ 24/7 roadside assistance')).toBeInTheDocument();
  });

  it('displays deductible explanation text', () => {
    render(
      <CoverageSelection
        formData={mockFormData}
        updateFormData={mockUpdateFormData}
        nextStep={mockNextStep}
        prevStep={mockPrevStep}
      />
    );

    expect(screen.getByText(/Higher deductibles mean lower monthly premiums/)).toBeInTheDocument();
  });

  it('selects liability when clicking on card', () => {
    render(
      <CoverageSelection
        formData={mockFormData}
        updateFormData={mockUpdateFormData}
        nextStep={mockNextStep}
        prevStep={mockPrevStep}
      />
    );

    const liabilityCard = screen.getByText('Minimum required').closest('.coverage-card');
    fireEvent.click(liabilityCard);

    expect(mockUpdateFormData).toHaveBeenCalledWith('coverageLevel', 'LIABILITY');
  });

  it('selects premium when clicking on card', () => {
    render(
      <CoverageSelection
        formData={mockFormData}
        updateFormData={mockUpdateFormData}
        nextStep={mockNextStep}
        prevStep={mockPrevStep}
      />
    );

    const premiumCard = screen.getByText('Best value').closest('.coverage-card');
    fireEvent.click(premiumCard);

    expect(mockUpdateFormData).toHaveBeenCalledWith('coverageLevel', 'PREMIUM');
  });
});
