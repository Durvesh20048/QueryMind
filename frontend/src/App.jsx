import { useState } from 'react';

function App() {
  const [question, setQuestion] = useState('');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  async function handleAsk() {
    setLoading(true);
    setResult(null);

    const response = await fetch('http://localhost:8080/api/query', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ question }),
    });
    const data = await response.json();

    setResult(data);
    setLoading(false);
  }

  return (
    <div style={{ padding: '2rem', fontFamily: 'sans-serif' }}>
      <h1>QueryMind</h1>

      <input
        type="text"
        value={question}
        onChange={(e) => setQuestion(e.target.value)}
        placeholder="Ask a question about the data..."
        style={{ width: '400px', padding: '0.5rem' }}
      />
      <button onClick={handleAsk} style={{ marginLeft: '0.5rem', padding: '0.5rem 1rem' }}>
        Ask
      </button>

      {loading && <p>Thinking... (first request can take 15-30 seconds)</p>}

      {result && (
        <div style={{ marginTop: '1rem' }}>
          <p><strong>Generated SQL:</strong> {result.generatedSql}</p>

          {result.error && <p style={{ color: 'red' }}>Error: {result.error}</p>}

          {result.rows && result.rows.length > 0 && (
            <table style={{ borderCollapse: 'collapse', marginTop: '1rem' }}>
              <thead>
                <tr>
                  {Object.keys(result.rows[0]).map((col) => (
                    <th key={col} style={{ border: '1px solid #ccc', padding: '0.5rem', background: '#eee' }}>
                      {col}
                    </th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {result.rows.map((row, i) => (
                  <tr key={i}>
                    {Object.values(row).map((val, j) => (
                      <td key={j} style={{ border: '1px solid #ccc', padding: '0.5rem' }}>
                        {String(val)}
                      </td>
                    ))}
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}
    </div>
  );
}

export default App;