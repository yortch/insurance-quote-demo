import { useState } from 'react';

const PROPERTY_TYPES = ['House', 'Condo', 'Apartment', 'Townhouse'];

function PropertyVehicleDetails({ formData, updateFormData, nextStep, prevStep }) {
  const [errors, setErrors] = useState({});

  const handleNext = () => {
    const newErrors = {};

    if (formData.insuranceType === 'AUTO') {
      if (!formData.vehicleYear) {
        newErrors.vehicleYear = 'Vehicle year is required';
      } else if (
        formData.vehicleYear < 1900 ||
        formData.vehicleYear > new Date().getFullYear() + 1
      ) {
        newErrors.vehicleYear = 'Please enter a valid year';
      }

      if (!formData.vehicleMake.trim()) {
        newErrors.vehicleMake = 'Vehicle make is required';
      }

      if (!formData.vehicleModel.trim()) {
        newErrors.vehicleModel = 'Vehicle model is required';
      }
    } else if (formData.insuranceType === 'HOME') {
      if (!formData.propertyType) {
        newErrors.propertyType = 'Property type is required';
      }

      if (!formData.propertySize) {
        newErrors.propertySize = 'Property size is required';
      } else if (formData.propertySize < 1 || formData.propertySize > 50000) {
        newErrors.propertySize = 'Please enter a valid property size';
      }

      if (!formData.yearBuilt) {
        newErrors.yearBuilt = 'Year built is required';
      } else if (formData.yearBuilt < 1800 || formData.yearBuilt > new Date().getFullYear()) {
        newErrors.yearBuilt = 'Please enter a valid year';
      }
    }

    setErrors(newErrors);

    if (Object.keys(newErrors).length === 0) {
      nextStep();
    }
  };

  return (
    <div className="wizard-form">
      <h2>Property/Vehicle Details</h2>

      <div className="form-group">
        <label>
          Insurance Type<span className="required">*</span>
        </label>
        <div style={{ display: 'flex', gap: '1rem', marginTop: '0.5rem' }}>
          <label
            style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', cursor: 'pointer' }}
          >
            <input
              type="radio"
              name="insuranceType"
              value="AUTO"
              checked={formData.insuranceType === 'AUTO'}
              onChange={(e) => updateFormData('insuranceType', e.target.value)}
            />
            Auto Insurance
          </label>
          <label
            style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', cursor: 'pointer' }}
          >
            <input
              type="radio"
              name="insuranceType"
              value="HOME"
              checked={formData.insuranceType === 'HOME'}
              onChange={(e) => updateFormData('insuranceType', e.target.value)}
            />
            Home Insurance
          </label>
        </div>
      </div>

      {formData.insuranceType === 'AUTO' && (
        <>
          <div className="form-row">
            <div className={`form-group ${errors.vehicleYear ? 'error' : ''}`}>
              <label>
                Vehicle Year<span className="required">*</span>
              </label>
              <input
                type="number"
                value={formData.vehicleYear}
                onChange={(e) => updateFormData('vehicleYear', e.target.value)}
                placeholder="2020"
                min="1900"
                max={new Date().getFullYear() + 1}
              />
              {errors.vehicleYear && <span className="error-message">{errors.vehicleYear}</span>}
            </div>

            <div className={`form-group ${errors.vehicleMake ? 'error' : ''}`}>
              <label>
                Vehicle Make<span className="required">*</span>
              </label>
              <input
                type="text"
                value={formData.vehicleMake}
                onChange={(e) => updateFormData('vehicleMake', e.target.value)}
                placeholder="Toyota"
              />
              {errors.vehicleMake && <span className="error-message">{errors.vehicleMake}</span>}
            </div>
          </div>

          <div className={`form-group ${errors.vehicleModel ? 'error' : ''}`}>
            <label>
              Vehicle Model<span className="required">*</span>
            </label>
            <input
              type="text"
              value={formData.vehicleModel}
              onChange={(e) => updateFormData('vehicleModel', e.target.value)}
              placeholder="Camry"
            />
            {errors.vehicleModel && <span className="error-message">{errors.vehicleModel}</span>}
          </div>
        </>
      )}

      {formData.insuranceType === 'HOME' && (
        <>
          <div className="form-row">
            <div className={`form-group ${errors.propertyType ? 'error' : ''}`}>
              <label>
                Property Type<span className="required">*</span>
              </label>
              <select
                value={formData.propertyType}
                onChange={(e) => updateFormData('propertyType', e.target.value)}
              >
                <option value="">Select property type</option>
                {PROPERTY_TYPES.map((type) => (
                  <option key={type} value={type}>
                    {type}
                  </option>
                ))}
              </select>
              {errors.propertyType && <span className="error-message">{errors.propertyType}</span>}
            </div>

            <div className={`form-group ${errors.propertySize ? 'error' : ''}`}>
              <label>
                Property Size (sq ft)<span className="required">*</span>
              </label>
              <input
                type="number"
                value={formData.propertySize}
                onChange={(e) => updateFormData('propertySize', e.target.value)}
                placeholder="2000"
                min="1"
              />
              {errors.propertySize && <span className="error-message">{errors.propertySize}</span>}
            </div>
          </div>

          <div className="form-row">
            <div className={`form-group ${errors.yearBuilt ? 'error' : ''}`}>
              <label>
                Year Built<span className="required">*</span>
              </label>
              <input
                type="number"
                value={formData.yearBuilt}
                onChange={(e) => updateFormData('yearBuilt', e.target.value)}
                placeholder="2010"
                min="1800"
                max={new Date().getFullYear()}
              />
              {errors.yearBuilt && <span className="error-message">{errors.yearBuilt}</span>}
            </div>
            <div></div>
          </div>
        </>
      )}

      <div className="wizard-actions">
        <button type="button" className="btn-secondary" onClick={prevStep}>
          Back
        </button>
        <button type="button" onClick={handleNext}>
          Next
        </button>
      </div>
    </div>
  );
}

export default PropertyVehicleDetails;
