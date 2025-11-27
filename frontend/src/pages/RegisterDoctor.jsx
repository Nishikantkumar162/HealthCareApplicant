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

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    await client.post('/auth/register/doctor', form);
    setMessage('Registration received. Admin approval pending.');
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
      {message && <p>{message}</p>}
    </div>
  );
};

export default RegisterDoctor;

