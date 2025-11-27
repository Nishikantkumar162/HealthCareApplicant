import { useEffect, useState } from 'react';
import client from '../../api/client';

const DoctorDashboard = () => {
  const [appointments, setAppointments] = useState([]);

  const load = async () => {
    const { data } = await client.get('/doctor/appointments');
    setAppointments(data);
  };

  useEffect(() => {
    load();
  }, []);

  const updateStatus = async (id, status) => {
    await client.put(`/doctor/appointments/${id}/status`, { status });
    load();
  };

  return (
    <div className="grid">
      <div className="card">
        <h2>My Appointments</h2>
        <table width="100%">
          <thead>
            <tr>
              <th>Patient</th>
              <th>Time</th>
              <th>Status</th>
              <th />
            </tr>
          </thead>
          <tbody>
            {appointments.map((app) => (
              <tr key={app.id}>
                <td>{app.patientName}</td>
                <td>{new Date(app.appointmentTime).toLocaleString()}</td>
                <td>{app.status}</td>
                <td>
                  {app.status === 'PENDING' && (
                    <>
                      <button onClick={() => updateStatus(app.id, 'CONFIRMED')}>Confirm</button>
                      <button className="secondary" onClick={() => updateStatus(app.id, 'REJECTED')}>
                        Reject
                      </button>
                    </>
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

export default DoctorDashboard;

