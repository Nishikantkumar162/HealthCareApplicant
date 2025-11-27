import { useEffect, useState } from 'react';
import client from '../../api/client';
import useAuth from '../../hooks/useAuth';

const AdminDashboard = () => {
  const { user } = useAuth();
  const [stats, setStats] = useState(null);
  const [doctors, setDoctors] = useState([]);
  const [appointments, setAppointments] = useState([]);

  const load = async () => {
    const [statsRes, doctorsRes, appointmentsRes] = await Promise.all([
      client.get('/admin/dashboard'),
      client.get('/admin/doctors'),
      client.get('/admin/appointments')
    ]);
    setStats(statsRes.data);
    setDoctors(doctorsRes.data);
    setAppointments(appointmentsRes.data);
  };

  useEffect(() => {
    load();
  }, []);

  const approve = async (id) => {
    await client.put(`/admin/doctors/${id}/approve`);
    load();
  };

  return (
    <div className="grid">
      <div className="card">
        <h2>Welcome back, {user?.fullName}</h2>
        {stats && (
          <div className="grid grid-3">
            <div>
              <h3>{stats.totalPatients}</h3>
              <p>Patients</p>
            </div>
            <div>
              <h3>{stats.totalDoctors}</h3>
              <p>Doctors</p>
            </div>
            <div>
              <h3>{stats.pendingAppointments}</h3>
              <p>Pending</p>
            </div>
          </div>
        )}
      </div>

      <div className="card">
        <h3>Pending Doctors</h3>
        {doctors
          .filter((d) => !d.approved)
          .map((doc) => (
            <div key={doc.id} style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 8 }}>
              <div>
                <strong>{doc.fullName}</strong> — {doc.specialization}
              </div>
              <button onClick={() => approve(doc.id)}>Approve</button>
            </div>
          ))}
      </div>

      <div className="card">
        <h3>Recent Appointments</h3>
        <table width="100%">
          <thead>
            <tr>
              <th>ID</th>
              <th>Doctor</th>
              <th>Patient</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {appointments.slice(0, 5).map((app) => (
              <tr key={app.id}>
                <td>{app.id}</td>
                <td>{app.doctorName}</td>
                <td>{app.patientName}</td>
                <td>{app.status}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default AdminDashboard;

