import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import client from '../../api/client';

const PatientDashboard = () => {
  const [appointments, setAppointments] = useState([]);

  const load = async () => {
    const { data } = await client.get('/patient/appointments');
    setAppointments(data);
  };

  useEffect(() => {
    load();
  }, []);

  const cancel = async (id) => {
    await client.put(`/patient/appointments/${id}/cancel`);
    load();
  };

  return (
    <div className="grid">
      <div className="card">
        <div style={{ display: 'flex', justifyContent: 'space-between' }}>
          <h2>My Appointments</h2>
          <Link to="/patient/book">
            <button>Book New</button>
          </Link>
        </div>
        <table width="100%">
          <thead>
            <tr>
              <th>Doctor</th>
              <th>Time</th>
              <th>Status</th>
              <th />
            </tr>
          </thead>
          <tbody>
            {appointments.map((app) => (
              <tr key={app.id}>
                <td>{app.doctorName}</td>
                <td>{new Date(app.appointmentTime).toLocaleString()}</td>
                <td>{app.status}</td>
                <td>
                  {app.status === 'PENDING' && (
                    <button className="secondary" onClick={() => cancel(app.id)}>
                      Cancel
                    </button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default PatientDashboard;

