import { useState } from 'react';

const US_STATES = [
  'AL',
  'AK',
  'AZ',
  'AR',
  'CA',
  'CO',
  'CT',
  'DE',
  'FL',
  'GA',
  'HI',
  'ID',
  'IL',
  'IN',
  'IA',
  'KS',
  'KY',
  'LA',
  'ME',
  'MD',
  'MA',
  'MI',
  'MN',
  'MS',
  'MO',
  'MT',
  'NE',
  'NV',
  'NH',
  'NJ',
  'NM',
  'NY',
  'NC',
  'ND',
  'OH',
  'OK',
  'OR',
  'PA',
  'RI',
  'SC',
  'SD',
  'TN',
  'TX',
  'UT',
  'VT',
  'VA',
  'WA',
  'WV',
  'WI',
  'WY',
];

function CustomerInfo({ formData, updateFormData, nextStep }) {
  const [errors, setErrors] = useState({});

  const validateEmail = (email) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  };

  const validatePhone = (phone) => {
    const phoneRegex = /^\(?([0-9]{3})\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$/;
    return phoneRegex.test(phone);
  };

  const validateZipCode = (zipCode) => {
    const zipRegex = /^\d{5}(-\d{4})?$/;
    return zipRegex.test(zipCode);
  };

  const handleNext = () => {
    const newErrors = {};

    if (!formData.firstName.trim()) {
      newErrors.firstName = 'First name is required';
    }

    if (!formData.lastName.trim()) {
      newErrors.lastName = 'Last name is required';
    }

    if (!formData.email.trim()) {
      newErrors.email = 'Email is required';
    } else if (!validateEmail(formData.email)) {
      newErrors.email = 'Please enter a valid email address';
    }

    if (!formData.phone.trim()) {
      newErrors.phone = 'Phone number is required';
    } else if (!validatePhone(formData.phone)) {
      newErrors.phone = 'Please enter a valid phone number (e.g., 555-123-4567)';
    }

    if (!formData.street.trim()) {
      newErrors.street = 'Street address is required';
    }

    if (!formData.city.trim()) {
      newErrors.city = 'City is required';
    }

    if (!formData.state) {
      newErrors.state = 'State is required';
    }

    if (!formData.zipCode.trim()) {
      newErrors.zipCode = 'ZIP code is required';
    } else if (!validateZipCode(formData.zipCode)) {
      newErrors.zipCode = 'Please enter a valid ZIP code (e.g., 12345 or 12345-6789)';
    }

    setErrors(newErrors);

    if (Object.keys(newErrors).length === 0) {
      nextStep();
    }
  };

  return (
    <div className="wizard-form">
      <h2>Customer Information</h2>

      <div className="form-row">
        <div className={`form-group ${errors.firstName ? 'error' : ''}`}>
          <label>
            First Name<span className="required">*</span>
          </label>
          <input
            type="text"
            value={formData.firstName}
            onChange={(e) => updateFormData('firstName', e.target.value)}
            placeholder="John"
          />
          {errors.firstName && <span className="error-message">{errors.firstName}</span>}
        </div>

        <div className={`form-group ${errors.lastName ? 'error' : ''}`}>
          <label>
            Last Name<span className="required">*</span>
          </label>
          <input
            type="text"
            value={formData.lastName}
            onChange={(e) => updateFormData('lastName', e.target.value)}
            placeholder="Doe"
          />
          {errors.lastName && <span className="error-message">{errors.lastName}</span>}
        </div>
      </div>

      <div className="form-row">
        <div className={`form-group ${errors.email ? 'error' : ''}`}>
          <label>
            Email<span className="required">*</span>
          </label>
          <input
            type="email"
            value={formData.email}
            onChange={(e) => updateFormData('email', e.target.value)}
            placeholder="john.doe@example.com"
          />
          {errors.email && <span className="error-message">{errors.email}</span>}
        </div>

        <div className={`form-group ${errors.phone ? 'error' : ''}`}>
          <label>
            Phone<span className="required">*</span>
          </label>
          <input
            type="tel"
            value={formData.phone}
            onChange={(e) => updateFormData('phone', e.target.value)}
            placeholder="555-123-4567"
          />
          {errors.phone && <span className="error-message">{errors.phone}</span>}
        </div>
      </div>

      <div className={`form-group ${errors.street ? 'error' : ''}`}>
        <label>
          Street Address<span className="required">*</span>
        </label>
        <input
          type="text"
          value={formData.street}
          onChange={(e) => updateFormData('street', e.target.value)}
          placeholder="123 Main St"
        />
        {errors.street && <span className="error-message">{errors.street}</span>}
      </div>

      <div className="form-row">
        <div className={`form-group ${errors.city ? 'error' : ''}`}>
          <label>
            City<span className="required">*</span>
          </label>
          <input
            type="text"
            value={formData.city}
            onChange={(e) => updateFormData('city', e.target.value)}
            placeholder="Springfield"
          />
          {errors.city && <span className="error-message">{errors.city}</span>}
        </div>

        <div className={`form-group ${errors.state ? 'error' : ''}`}>
          <label>
            State<span className="required">*</span>
          </label>
          <select value={formData.state} onChange={(e) => updateFormData('state', e.target.value)}>
            <option value="">Select a state</option>
            {US_STATES.map((state) => (
              <option key={state} value={state}>
                {state}
              </option>
            ))}
          </select>
          {errors.state && <span className="error-message">{errors.state}</span>}
        </div>
      </div>

      <div className="form-row">
        <div className={`form-group ${errors.zipCode ? 'error' : ''}`}>
          <label>
            ZIP Code<span className="required">*</span>
          </label>
          <input
            type="text"
            value={formData.zipCode}
            onChange={(e) => updateFormData('zipCode', e.target.value)}
            placeholder="12345"
          />
          {errors.zipCode && <span className="error-message">{errors.zipCode}</span>}
        </div>
        <div></div>
      </div>

      <div className="wizard-actions">
        <div></div>
        <button type="button" onClick={handleNext}>
          Next
        </button>
      </div>
    </div>
  );
}

export default CustomerInfo;
