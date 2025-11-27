import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import client from '../../api/client';

const BookAppointment = () => {
  const [doctors, setDoctors] = useState([]);
  const [form, setForm] = useState({ doctorId: '', appointmentTime: '', reason: '' });
  const navigate = useNavigate();

  useEffect(() => {
    const load = async () => {
      const { data } = await client.get('/public/doctors');
      setDoctors(data.filter((doc) => doc.approved));
    };
    load();
  }, []);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    await client.post('/patient/appointments', {
      doctorId: Number(form.doctorId),
      appointmentTime: form.appointmentTime,
      reason: form.reason
    });
    navigate('/patient');
  };

  return (
    <div className="card" style={{ maxWidth: 640, margin: '2rem auto' }}>
      <h2>Book Appointment</h2>
      <form onSubmit={handleSubmit} className="grid">
        <select name="doctorId" value={form.doctorId} onChange={handleChange} required>
          <option value="">Select doctor</option>
          {doctors.map((doc) => (
            <option key={doc.id} value={doc.id}>
              {doc.fullName} — {doc.specialization}
            </option>
          ))}
        </select>
        <input
          type="datetime-local"
          name="appointmentTime"
          value={form.appointmentTime}
          onChange={handleChange}
          required
        />
        <textarea name="reason" placeholder="Reason" value={form.reason} onChange={handleChange} rows={3} />
        <button type="submit">Submit</button>
      </form>
    </div>
  );
};

export default BookAppointment;

