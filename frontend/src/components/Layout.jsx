import { Link } from 'react-router-dom';
import useAuth from '../hooks/useAuth';

const Layout = ({ children }) => {
  const { user, logout } = useAuth();

  return (
    <>
      <nav className="nav">
        <div>
          <strong>Healthcare Appointment</strong>
        </div>
        <div>
          {user ? (
            <>
              <span style={{ marginRight: '1rem' }}>{user.fullName}</span>
              <button className="secondary" onClick={logout}>
                Logout
              </button>
            </>
          ) : (
            <>
              <Link to="/login">Login</Link>
              <Link to="/register/patient">Register</Link>
            </>
          )}
        </div>
      </nav>
      <main className="content">{children}</main>
    </>
  );
};

export default Layout;

