import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import client from '../api/client';

const RegisterPatient = () => {
  const [form, setForm] = useState({
    fullName: '',
    email: '',
    password: '',
    phone: '',
    address: ''
  });
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    await client.post('/auth/register/patient', form);
    setMessage('Registration successful. You can login now.');
    setTimeout(() => navigate('/login'), 1500);
  };

  return (
    <div className="card" style={{ maxWidth: 640, margin: '2rem auto' }}>
      <h2>Patient Registration</h2>
      <form onSubmit={handleSubmit} className="grid">
        <input name="fullName" placeholder="Full name" value={form.fullName} onChange={handleChange} required />
        <input name="email" placeholder="Email" value={form.email} onChange={handleChange} required />
        <input
          name="password"
          type="password"
          placeholder="Password"
          value={form.password}
          onChange={handleChange}
          required
        />
        <input name="phone" placeholder="Phone" value={form.phone} onChange={handleChange} />
        <input name="address" placeholder="Address" value={form.address} onChange={handleChange} />
        <button type="submit">Register</button>
      </form>
      {message && <p>{message}</p>}
    </div>
  );
};

export default RegisterPatient;

