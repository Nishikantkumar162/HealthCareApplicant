import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import useAuth from '../hooks/useAuth';

const Login = () => {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: '', password: '' });
  const [error, setError] = useState('');

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError(''); // Clear error when user types
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const data = await login(form);
      if (data.role === 'ROLE_ADMIN') navigate('/admin');
      if (data.role === 'ROLE_DOCTOR') navigate('/doctor');
      if (data.role === 'ROLE_PATIENT') navigate('/patient');
    } catch (err) {
      const errorMessage = err.response?.data?.message || err.message || 'Invalid credentials. Please try again.';
      setError(errorMessage);
    }
  };

  return (
    <div className="card" style={{ maxWidth: 480, margin: '2rem auto' }}>
      <h2>Sign in</h2>
      <form onSubmit={handleSubmit} className="grid">
        <input name="email" placeholder="Email" value={form.email} onChange={handleChange} required />
        <input
          name="password"
          type="password"
          placeholder="Password"
          value={form.password}
          onChange={handleChange}
          required
        />
        {error && <p style={{ color: 'red', marginTop: '0.5rem' }}>{error}</p>}
        <button type="submit">Login</button>
      </form>
      <p>
        New patient? <a href="/register/patient">Create account</a>
      </p>
    </div>
  );
};

export default Login;

