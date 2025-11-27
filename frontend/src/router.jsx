import { Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import RegisterPatient from './pages/RegisterPatient';
import RegisterDoctor from './pages/RegisterDoctor';
import AdminDashboard from './pages/admin/AdminDashboard';
import DoctorDashboard from './pages/doctor/DoctorDashboard';
import PatientDashboard from './pages/patient/PatientDashboard';
import BookAppointment from './pages/patient/BookAppointment';
import ProtectedRoute from './components/ProtectedRoute';

const Router = () => (
  <Routes>
    <Route path="/" element={<Navigate to="/login" />} />
    <Route path="/login" element={<Login />} />
    <Route path="/register/patient" element={<RegisterPatient />} />
    <Route path="/register/doctor" element={<RegisterDoctor />} />

    <Route
      path="/admin"
      element={
        <ProtectedRoute roles={['ROLE_ADMIN']}>
          <AdminDashboard />
        </ProtectedRoute>
      }
    />
    <Route
      path="/doctor"
      element={
        <ProtectedRoute roles={['ROLE_DOCTOR']}>
          <DoctorDashboard />
        </ProtectedRoute>
      }
    />
    <Route
      path="/patient"
      element={
        <ProtectedRoute roles={['ROLE_PATIENT']}>
          <PatientDashboard />
        </ProtectedRoute>
      }
    />
    <Route
      path="/patient/book"
      element={
        <ProtectedRoute roles={['ROLE_PATIENT']}>
          <BookAppointment />
        </ProtectedRoute>
      }
    />
  </Routes>
);

export default Router;

