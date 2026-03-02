import QuoteWizard from './components/QuoteWizard/QuoteWizard';
import './App.css';

function App() {
  return (
    <div className="app">
      <header className="app-header">
        <h1>Three Rivers Insurance</h1>
        <p>Get your personalized insurance quote in minutes.</p>
      </header>
      <main className="app-main">
        <QuoteWizard />
      </main>
    </div>
  );
}

export default App;
