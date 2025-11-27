import { useState } from 'react';
import client from '../api/client';

const RegisterDoctor = () => {
  const [form, setForm] = useState({
    fullName: '',
    email: '',
    password: '',
    specialization: '',
    hospital: ''
  });
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError(''); // Clear error when user types
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setMessage('');
    
    try {
      await client.post('/auth/register/doctor', form);
      setMessage('Registration received. Admin approval pending.');
    } catch (err) {
      if (err.response?.data?.message) {
        setError(err.response.data.message);
      } else if (err.response?.data?.errors) {
        // Handle multiple validation errors
        const errorMessages = Object.values(err.response.data.errors).join(', ');
        setError(errorMessages);
      } else {
        setError('Registration failed. Please try again.');
      }
    }
  };

  return (
    <div className="card" style={{ maxWidth: 640, margin: '2rem auto' }}>
      <h2>Doctor Registration</h2>
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
        <input
          name="specialization"
          placeholder="Specialization"
          value={form.specialization}
          onChange={handleChange}
          required
        />
        <input name="hospital" placeholder="Hospital" value={form.hospital} onChange={handleChange} />
        <button type="submit">Register</button>
      </form>
      {error && <p style={{ color: 'red', marginTop: '1rem' }}>{error}</p>}
      {message && <p style={{ color: 'green', marginTop: '1rem' }}>{message}</p>}
    </div>
  );
};

export default RegisterDoctor;

