import Router from './router';
import Layout from './components/Layout';

function App() {
  return (
    <div className="app-shell">
      <Layout>
        <Router />
      </Layout>
    </div>
  );
}

export default App;

