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
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const data = await login(form);
      if (data.role === 'ROLE_ADMIN') navigate('/admin');
      if (data.role === 'ROLE_DOCTOR') navigate('/doctor');
      if (data.role === 'ROLE_PATIENT') navigate('/patient');
    } catch (err) {
      setError('Invalid credentials');
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
        {error && <span style={{ color: 'crimson' }}>{error}</span>}
        <button type="submit">Login</button>
      </form>
      <p>
        New patient? <a href="/register/patient">Create account</a>
      </p>
    </div>
  );
};

export default Login;

