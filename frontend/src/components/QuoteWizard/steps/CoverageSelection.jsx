import './CoverageSelection.css';

const COVERAGE_OPTIONS = [
  {
    level: 'LIABILITY',
    name: 'Basic Coverage',
    subtitle: 'Minimum required',
    description: 'Essential protection for you and others',
    features: ['Third-party liability', 'Legal defense coverage', 'Property damage coverage'],
  },
  {
    level: 'COMPREHENSIVE',
    name: 'Full Protection',
    subtitle: 'Most popular',
    description: 'Complete coverage for peace of mind',
    features: [
      'All liability coverage',
      'Collision coverage',
      'Theft protection',
      'Weather damage',
      'Fire and vandalism',
    ],
    popular: true,
  },
  {
    level: 'PREMIUM',
    name: 'Complete Peace of Mind',
    subtitle: 'Best value',
    description: 'Maximum protection with premium benefits',
    features: [
      'All comprehensive coverage',
      'Rental car reimbursement',
      '24/7 roadside assistance',
      'Zero deductible option',
      'Glass and windshield coverage',
      'Personal belongings coverage',
    ],
  },
];

const DEDUCTIBLE_OPTIONS = [
  { value: '500', label: '$500' },
  { value: '1000', label: '$1,000' },
  { value: '2500', label: '$2,500' },
  { value: '5000', label: '$5,000' },
];

function CoverageSelection({ formData, updateFormData, nextStep, prevStep }) {
  return (
    <div className="wizard-form">
      <h2>Coverage Selection</h2>

      <div className="form-group">
        <label>
          Coverage Level<span className="required">*</span>
        </label>
        <div className="coverage-cards">
          {COVERAGE_OPTIONS.map((option) => (
            <div
              key={option.level}
              className={`coverage-card ${formData.coverageLevel === option.level ? 'selected' : ''}`}
              onClick={() => updateFormData('coverageLevel', option.level)}
            >
              {option.popular && <div className="popular-badge">Most Popular</div>}
              <h3>{option.name}</h3>
              <p className="coverage-subtitle">{option.subtitle}</p>
              <p className="coverage-description">{option.description}</p>
              <ul className="coverage-features">
                {option.features.map((feature, index) => (
                  <li key={index}>✓ {feature}</li>
                ))}
              </ul>
              <div className="coverage-radio">
                <input
                  type="radio"
                  name="coverageLevel"
                  value={option.level}
                  checked={formData.coverageLevel === option.level}
                  onChange={(e) => updateFormData('coverageLevel', e.target.value)}
                />
              </div>
            </div>
          ))}
        </div>
      </div>

      <div className="form-group">
        <label>
          Deductible Amount<span className="required">*</span>
        </label>
        <p style={{ fontSize: '0.9rem', color: '#666', marginTop: '0.25rem' }}>
          Higher deductibles mean lower monthly premiums. The deductible is what you pay
          out-of-pocket before insurance coverage kicks in.
        </p>
        <select
          value={formData.deductible}
          onChange={(e) => updateFormData('deductible', e.target.value)}
          style={{ marginTop: '0.5rem' }}
        >
          {DEDUCTIBLE_OPTIONS.map((option) => (
            <option key={option.value} value={option.value}>
              {option.label}
            </option>
          ))}
        </select>
      </div>

      <div className="wizard-actions">
        <button type="button" className="btn-secondary" onClick={prevStep}>
          Back
        </button>
        <button type="button" onClick={nextStep}>
          Next
        </button>
      </div>
    </div>
  );
}

export default CoverageSelection;
