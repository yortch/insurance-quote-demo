import { useState, lazy, Suspense } from 'react';
import LoadingSpinner from '../LoadingSpinner/LoadingSpinner';
import './QuoteWizard.css';

const CustomerInfo = lazy(() => import('./steps/CustomerInfo'));
const PropertyVehicleDetails = lazy(() => import('./steps/PropertyVehicleDetails'));
const CoverageSelection = lazy(() => import('./steps/CoverageSelection'));
const QuoteReview = lazy(() => import('./steps/QuoteReview'));

const STEPS = [
  { id: 1, name: 'Customer Info', component: CustomerInfo },
  { id: 2, name: 'Property/Vehicle', component: PropertyVehicleDetails },
  { id: 3, name: 'Coverage', component: CoverageSelection },
  { id: 4, name: 'Review', component: QuoteReview },
];

function QuoteWizard() {
  const [currentStep, setCurrentStep] = useState(1);
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    street: '',
    city: '',
    state: '',
    zipCode: '',
    insuranceType: 'AUTO',
    vehicleYear: '',
    vehicleMake: '',
    vehicleModel: '',
    propertyType: '',
    propertySize: '',
    yearBuilt: '',
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
    <div className="quote-wizard" role="main" aria-label="Insurance Quote Wizard">
      <nav className="wizard-stepper" aria-label="Quote wizard progress">
        {STEPS.map((step) => (
          <div
            key={step.id}
            className={`step ${currentStep === step.id ? 'active' : ''} ${currentStep > step.id ? 'completed' : ''}`}
            aria-current={currentStep === step.id ? 'step' : undefined}
          >
            <div className="step-number" aria-hidden="true">{step.id}</div>
            <div className="step-name">{step.name}</div>
          </div>
        ))}
      </nav>

      <div className="wizard-content" aria-live="polite">
        <Suspense fallback={<LoadingSpinner message="Loading step..." />}>
          <CurrentStepComponent
            formData={formData}
            updateFormData={updateFormData}
            nextStep={nextStep}
            prevStep={prevStep}
            goToStep={goToStep}
            currentStep={currentStep}
          />
        </Suspense>
      </div>
    </div>
  );
}

export default QuoteWizard;
