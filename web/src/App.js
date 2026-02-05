import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';

// Simple functional placeholders
const Login = () => <div style={{padding: '20px'}}><h2>Login Page</h2><form><input type="text" placeholder="Username" /><br/><input type="password" placeholder="Password" /><br/><button>Login</button></form></div>;
const Register = () => <div style={{padding: '20px'}}><h2>Register Page</h2><form><input type="text" placeholder="Username" /><br/><input type="email" placeholder="Email" /><br/><input type="password" placeholder="Password" /><br/><button>Register</button></form></div>;
const Dashboard = () => <div style={{padding: '20px'}}><h2>User Dashboard</h2><p>Welcome! This is a protected route.</p><button onClick={() => window.location.href='/login'}>Logout</button></div>;

function App() {
  return (
    <Router>
      <nav style={{ padding: '10px', background: '#eee', display: 'flex', gap: '15px' }}>
        <Link to="/login">Login</Link>
        <Link to="/register">Register</Link>
        <Link to="/dashboard">Dashboard</Link>
      </nav>

      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/" element={<Login />} /> {/* Default to login */}
      </Routes>
    </Router>
  );
}

export default App;