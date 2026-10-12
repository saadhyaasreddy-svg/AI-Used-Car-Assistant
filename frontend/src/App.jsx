import { useEffect, useState } from 'react';
import { Activity, AlertTriangle, CarFront, CheckCircle2, CircleDollarSign, Gauge, History, LoaderCircle, Search, ShieldCheck, Wrench } from 'lucide-react';

const initialForm = {
  brand: 'Volkswagen', model: 'Jetta', year: 2015, mileage: 80000,
  fuelType: 'Diesel', transmission: 'Manual', owners: 1, askingPrice: 500000,
  serviceHistory: 'Complete', accidentHistory: 'No', engineCondition: 'Good',
  acCondition: 'Good', brakeCondition: 'Good', tyreCondition: 'Good'
};
const money = value => new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 }).format(Number(value || 0));

function Field({ label, name, value, onChange, options, type = 'text', min, max, step }) {
  return <label className="field"><span>{label}</span>{options ? <select name={name} value={value} onChange={onChange}>{options.map(option => <option key={option} value={option}>{option}</option>)}</select> : <input name={name} type={type} value={value} min={min} max={max} step={step} onChange={onChange} required />}</label>;
}
function Metric({ icon: Icon, label, value, detail, accent = '' }) {
  return <div className={`metric ${accent}`}><div className="metric-icon"><Icon size={19}/></div><span className="metric-label">{label}</span><strong>{value}</strong>{detail && <small>{detail}</small>}</div>;
}

export default function App() {
  const [form, setForm] = useState(initialForm);
  const [report, setReport] = useState(null);
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [activeTab, setActiveTab] = useState('analyze');

  async function loadHistory() {
    try {
      const response = await fetch('/api/vehicles');
      if (!response.ok) throw new Error('Could not load saved analyses.');
      setHistory(await response.json());
    } catch { setHistory([]); }
  }
  useEffect(() => { loadHistory(); }, []);

  function handleChange(event) {
    const { name, value, type } = event.target;
    setForm(old => ({ ...old, [name]: type === 'number' ? (value === '' ? '' : Number(value)) : value }));
  }
  async function analyze(event) {
    event.preventDefault(); setLoading(true); setError(''); setReport(null);
    try {
      const response = await fetch('/api/vehicles/analyze', {
        method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(form)
      });
      const data = await response.json();
      if (!response.ok) throw new Error(data.message || 'Analysis failed. Check the backend and form values.');
      setReport(data); setActiveTab('analyze'); await loadHistory();
    } catch (e) {
      setError(`${e.message} Make sure Spring Boot and MySQL are running.`);
    } finally { setLoading(false); }
  }

  return <div className="app-shell">
    <aside className="sidebar">
      <div className="brand"><div className="brand-mark"><CarFront size={25}/></div><div><strong>AutoWise AI</strong><small>USED CAR INTELLIGENCE</small></div></div>
      <div className="side-label">WORKSPACE</div>
      <button className={`nav-item ${activeTab === 'analyze' ? 'selected' : ''}`} onClick={() => setActiveTab('analyze')}><Search size={18}/> Car analysis</button>
      <button className={`nav-item ${activeTab === 'history' ? 'selected' : ''}`} onClick={() => { setActiveTab('history'); loadHistory(); }}><History size={18}/> Saved reports <span className="nav-count">{history.length}</span></button>
      <div className="sidebar-bottom"><div className="sidebar-help"><ShieldCheck size={20}/><strong>Buy with confidence</strong><p>Review the estimate and get an independent mechanic's inspection before purchasing.</p></div><small className="sidebar-foot">PROJECT DEMO · v1.0</small></div>
    </aside>

    <main className="main-content">
      <header className="topbar"><div><span className="eyebrow">USED CAR DECISION SUPPORT</span><h1>{activeTab === 'history' ? 'Saved reports' : 'AI Used-Car Buying Assistant'}</h1></div><div className="status-pill"><span/> Analysis workspace</div></header>
      {activeTab === 'history' ? <section className="panel history-panel"><div className="section-heading"><div><h2>Previous analyses</h2><p>Reports saved to your MySQL database.</p></div><button className="secondary-btn" onClick={loadHistory}>Refresh</button></div>
        {history.length === 0 ? <div className="empty-state"><History size={30}/><strong>No saved reports yet</strong><p>Analyze a vehicle to see it here.</p><button className="primary-btn" onClick={() => setActiveTab('analyze')}>Analyze a car</button></div> : <div className="history-list">{history.map(item => <button className="history-row" key={item.id} onClick={() => { setReport(item); setActiveTab('analyze'); }}><div className="history-car"><CarFront size={21}/></div><div className="history-info"><strong>{item.brand} {item.model} ({item.year})</strong><span>{Number(item.mileage).toLocaleString('en-IN')} km · {item.owners} owner(s)</span></div><div className="history-price"><strong>{money(item.askingPrice)}</strong><span>Asking price</span></div><div className="history-score">{item.healthScore}/100</div></button>)}</div>}
      </section> : <div className="workspace-grid">
        <section className="panel form-panel"><div className="section-heading"><div><div className="step-label"><span>01</span> VEHICLE DETAILS</div><h2>Tell us about the car</h2><p>Enter the details from the listing and inspection.</p></div><div className="heading-icon"><CarFront size={22}/></div></div>
          <form onSubmit={analyze}>
            <div className="form-section-title">BASIC INFORMATION</div><div className="form-grid">
              <Field label="Brand" name="brand" value={form.brand} onChange={handleChange} options={['Volkswagen','Maruti Suzuki','Hyundai','Honda','Toyota','Tata','Mahindra','Kia','Skoda','BMW','Mercedes-Benz','Other']}/>
              <Field label="Model" name="model" value={form.model} onChange={handleChange}/>
              <Field label="Manufacturing year" name="year" value={form.year} onChange={handleChange} type="number" min="1990" max="2026"/>
              <Field label="Mileage (km)" name="mileage" value={form.mileage} onChange={handleChange} type="number" min="0"/>
              <Field label="Fuel type" name="fuelType" value={form.fuelType} onChange={handleChange} options={['Petrol','Diesel','CNG','Electric','Hybrid']}/>
              <Field label="Transmission" name="transmission" value={form.transmission} onChange={handleChange} options={['Manual','Automatic']}/>
              <Field label="Number of owners" name="owners" value={form.owners} onChange={handleChange} options={['1','2','3','4','5']}/>
              <Field label="Asking price (₹)" name="askingPrice" value={form.askingPrice} onChange={handleChange} type="number" min="1"/>
            </div>
            <div className="form-section-title condition-title">HISTORY & VEHICLE CONDITION</div><div className="form-grid">
              <Field label="Service history" name="serviceHistory" value={form.serviceHistory} onChange={handleChange} options={['Complete','Partial','Unavailable']}/>
              <Field label="Accident history" name="accidentHistory" value={form.accidentHistory} onChange={handleChange} options={['No','Minor','Major','Unknown']}/>
              <Field label="Engine condition" name="engineCondition" value={form.engineCondition} onChange={handleChange} options={['Excellent','Good','Average','Poor']}/>
              <Field label="AC condition" name="acCondition" value={form.acCondition} onChange={handleChange} options={['Excellent','Good','Average','Poor']}/>
              <Field label="Brake condition" name="brakeCondition" value={form.brakeCondition} onChange={handleChange} options={['Excellent','Good','Average','Poor']}/>
              <Field label="Tyre condition" name="tyreCondition" value={form.tyreCondition} onChange={handleChange} options={['Excellent','Good','Average','Poor']}/>
            </div>
            {error && <div className="error-message"><AlertTriangle size={17}/>{error}</div>}
            <button className="primary-btn submit-btn" type="submit" disabled={loading}>{loading ? <><LoaderCircle className="spin" size={18}/> Analyzing vehicle...</> : <><Activity size={18}/> Analyze vehicle <span>→</span></>}</button>
            <p className="form-note"><ShieldCheck size={14}/> Your report is saved to your local project database.</p>
          </form>
        </section>

        <section className="results-column"><div className="results-header"><div><div className="step-label"><span>02</span> ANALYSIS REPORT</div><h2>Vehicle assessment</h2></div><span className="live-badge"><span/> LIVE REPORT</span></div>
          {!report ? <div className="empty-report"><div className="empty-illustration"><Gauge size={38}/></div><h3>Your report will appear here</h3><p>Complete the vehicle details and select <strong>Analyze vehicle</strong> to generate an estimated valuation and condition report.</p><div className="empty-checks"><span><CheckCircle2 size={16}/> Market price estimate</span><span><CheckCircle2 size={16}/> Health score</span><span><CheckCircle2 size={16}/> Repair cost estimate</span></div></div> : <>
            <div className="report-car-card"><div className="report-car-icon"><CarFront size={26}/></div><div className="report-car-name"><span>VEHICLE ASSESSED</span><h3>{report.brand} {report.model}</h3><p>{report.year} · {report.fuelType} · {report.transmission} · {Number(report.mileage).toLocaleString('en-IN')} km</p></div><div className="health-circle" style={{'--score': `${report.healthScore * 3.6}deg`}}><strong>{report.healthScore}</strong><small>/100</small></div></div>
            <div className="metric-grid"><Metric icon={CircleDollarSign} label="EST. MARKET PRICE" value={money(report.estimatedMarketPrice)} detail="Indicative estimate"/><Metric icon={Wrench} label="EST. REPAIR COST" value={money(report.estimatedRepairCost)} detail="Based on reported condition" accent="orange"/><Metric icon={Activity} label="EFFECTIVE COST" value={money(report.effectiveBuyingCost)} detail="Asking price + repairs" accent="purple"/><Metric icon={ShieldCheck} label="HEALTH SCORE" value={`${report.healthScore}/100`} detail={report.healthLabel} accent="green"/></div>
            <div className={`recommendation ${report.recommendation === 'Needs careful inspection' ? 'warning' : report.recommendation === 'Negotiate' ? 'negotiate' : ''}`}><div className="recommendation-icon">{report.recommendation === 'Good candidate' ? <CheckCircle2 size={20}/> : <AlertTriangle size={20}/>}</div><div><span>BUYING GUIDANCE</span><strong>{report.recommendation}</strong><p>{report.recommendationReason}</p></div></div>
            <div className="report-note"><AlertTriangle size={16}/><p><strong>Estimate only:</strong> This demo uses a basic rule-based formula, not live market listings or a trained AI model. Verify the vehicle with a qualified mechanic before buying.</p></div>
          </>}
        </section>
      </div>}
      <footer className="page-footer"><span>AutoWise AI · Academic project prototype</span><span>React <b>·</b> Spring Boot <b>·</b> MySQL</span></footer>
    </main>
  </div>;
}
