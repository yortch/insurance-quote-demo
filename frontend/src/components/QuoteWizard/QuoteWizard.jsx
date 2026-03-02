import { useState } from 'react';
import CustomerInfo from './steps/CustomerInfo';
import PropertyVehicleDetails from './steps/PropertyVehicleDetails';
import CoverageSelection from './steps/CoverageSelection';
import QuoteReview from './steps/QuoteReview';
import './QuoteWizard.css';

const STEPS = [
  { id: 1, name: 'Customer Info', component: CustomerInfo },
  { id: 2, name: 'Property/Vehicle', component: PropertyVehicleDetails },
  { id: 3, name: 'Coverage', component: CoverageSelection },
  { id: 4, name: 'Review', component: QuoteReview },
];

function QuoteWizard() {
  const [currentStep, setCurrentStep] = useState(1);
  const [formData, setFormData] = useState({
    // Step 1: Customer Info
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    street: '',
    city: '',
    state: '',
    zipCode: '',
    // Step 2: Property/Vehicle Details
    insuranceType: 'AUTO',
    vehicleYear: '',
    vehicleMake: '',
    vehicleModel: '',
    propertyType: '',
    propertySize: '',
    yearBuilt: '',
    // Step 3: Coverage Selection
    coverageLevel: 'COMPREHENSIVE',
    deductible: '1000',
  });

  const updateFormData = (field, value) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
  };

  const goToStep = (stepNumber) => {
    setCurrentStep(stepNumber);
  };

  const nextStep = () => {
    if (currentStep < STEPS.length) {
      setCurrentStep((prev) => prev + 1);
    }
  };

  const prevStep = () => {
    if (currentStep > 1) {
      setCurrentStep((prev) => prev - 1);
    }
  };

  const CurrentStepComponent = STEPS[currentStep - 1].component;

  return (
    <div className="quote-wizard">
      <div className="wizard-stepper">
        {STEPS.map((step) => (
          <div
            key={step.id}
            className={`step ${currentStep === step.id ? 'active' : ''} ${currentStep > step.id ? 'completed' : ''}`}
          >
            <div className="step-number">{step.id}</div>
            <div className="step-name">{step.name}</div>
          </div>
        ))}
      </div>

      <div className="wizard-content">
        <CurrentStepComponent
          formData={formData}
          updateFormData={updateFormData}
          nextStep={nextStep}
          prevStep={prevStep}
          goToStep={goToStep}
          currentStep={currentStep}
        />
      </div>
    </div>
  );
}

export default QuoteWizard;
