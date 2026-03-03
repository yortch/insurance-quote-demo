import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import CustomerInfo from './CustomerInfo';

describe('CustomerInfo', () => {
  const mockFormData = {
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    street: '',
    city: '',
    state: '',
    zipCode: '',
  };

  const mockUpdateFormData = vi.fn();
  const mockNextStep = vi.fn();

  it('renders all form fields', () => {
    render(
      <CustomerInfo
        formData={mockFormData}
        updateFormData={mockUpdateFormData}
        nextStep={mockNextStep}
      />
    );

    expect(screen.getByText('Customer Information')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('John')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Doe')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('john.doe@example.com')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('555-123-4567')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('123 Main St')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Springfield')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('12345')).toBeInTheDocument();
    expect(screen.getByDisplayValue('Select a state')).toBeInTheDocument();
  });

  it('calls updateFormData when firstName changes', () => {
    render(
      <CustomerInfo
        formData={mockFormData}
        updateFormData={mockUpdateFormData}
        nextStep={mockNextStep}
      />
    );

    const firstNameInput = screen.getByPlaceholderText('John');
    fireEvent.change(firstNameInput, { target: { value: 'Jane' } });

    expect(mockUpdateFormData).toHaveBeenCalledWith('firstName', 'Jane');
  });

  it('calls updateFormData when email changes', () => {
    render(
      <CustomerInfo
        formData={mockFormData}
        updateFormData={mockUpdateFormData}
        nextStep={mockNextStep}
      />
    );

    const emailInput = screen.getByPlaceholderText('john.doe@example.com');
    fireEvent.change(emailInput, { target: { value: 'test@example.com' } });

    expect(mockUpdateFormData).toHaveBeenCalledWith('email', 'test@example.com');
  });

  it('calls updateFormData when state changes', () => {
    render(
      <CustomerInfo
        formData={mockFormData}
        updateFormData={mockUpdateFormData}
        nextStep={mockNextStep}
      />
    );

    const stateSelect = screen.getByDisplayValue('Select a state');
    fireEvent.change(stateSelect, { target: { value: 'CA' } });

    expect(mockUpdateFormData).toHaveBeenCalledWith('state', 'CA');
  });

  it('shows validation errors when submitting empty form', () => {
    render(
      <CustomerInfo
        formData={mockFormData}
        updateFormData={mockUpdateFormData}
        nextStep={mockNextStep}
      />
    );

    fireEvent.click(screen.getByText('Next'));

    expect(screen.getByText('First name is required')).toBeInTheDocument();
    expect(screen.getByText('Last name is required')).toBeInTheDocument();
    expect(screen.getByText('Email is required')).toBeInTheDocument();
    expect(screen.getByText('Phone number is required')).toBeInTheDocument();
    expect(mockNextStep).not.toHaveBeenCalled();
  });

  it('shows email validation error for invalid email', () => {
    const invalidEmailData = { ...mockFormData, email: 'invalid-email' };
    render(
      <CustomerInfo
        formData={invalidEmailData}
        updateFormData={mockUpdateFormData}
        nextStep={mockNextStep}
      />
    );

    fireEvent.click(screen.getByText('Next'));

    expect(screen.getByText('Please enter a valid email address')).toBeInTheDocument();
    expect(mockNextStep).not.toHaveBeenCalled();
  });

  it('shows phone validation error for invalid phone', () => {
    const invalidPhoneData = { ...mockFormData, phone: '123' };
    render(
      <CustomerInfo
        formData={invalidPhoneData}
        updateFormData={mockUpdateFormData}
        nextStep={mockNextStep}
      />
    );

    fireEvent.click(screen.getByText('Next'));

    expect(screen.getByText('Please enter a valid phone number (e.g., 555-123-4567)')).toBeInTheDocument();
    expect(mockNextStep).not.toHaveBeenCalled();
  });

  it('shows zip validation error for invalid zip code', () => {
    const invalidZipData = { ...mockFormData, zipCode: '123' };
    render(
      <CustomerInfo
        formData={invalidZipData}
        updateFormData={mockUpdateFormData}
        nextStep={mockNextStep}
      />
    );

    fireEvent.click(screen.getByText('Next'));

    expect(screen.getByText('Please enter a valid ZIP code (e.g., 12345 or 12345-6789)')).toBeInTheDocument();
    expect(mockNextStep).not.toHaveBeenCalled();
  });

  it('calls nextStep when form is valid', () => {
    const validFormData = {
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      phone: '555-123-4567',
      street: '123 Main St',
      city: 'Springfield',
      state: 'CA',
      zipCode: '12345',
    };

    render(
      <CustomerInfo
        formData={validFormData}
        updateFormData={mockUpdateFormData}
        nextStep={mockNextStep}
      />
    );

    fireEvent.click(screen.getByText('Next'));

    expect(mockNextStep).toHaveBeenCalled();
  });

  it('accepts valid 5-digit zip code', () => {
    const validFormData = {
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      phone: '555-123-4567',
      street: '123 Main St',
      city: 'Springfield',
      state: 'CA',
      zipCode: '12345',
    };

    render(
      <CustomerInfo
        formData={validFormData}
        updateFormData={mockUpdateFormData}
        nextStep={mockNextStep}
      />
    );

    fireEvent.click(screen.getByText('Next'));

    expect(mockNextStep).toHaveBeenCalled();
  });

  it('accepts valid zip+4 format', () => {
    const validFormData = {
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      phone: '555-123-4567',
      street: '123 Main St',
      city: 'Springfield',
      state: 'CA',
      zipCode: '12345-6789',
    };

    render(
      <CustomerInfo
        formData={validFormData}
        updateFormData={mockUpdateFormData}
        nextStep={mockNextStep}
      />
    );

    fireEvent.click(screen.getByText('Next'));

    expect(mockNextStep).toHaveBeenCalled();
  });
});
