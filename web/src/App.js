import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Link, useNavigate } from 'react-router-dom';

// --- REGISTER LOGIC ---
const Register = () => {
  const [user, setUser] = useState({ username: '', email: '', password: '' });
  const handleSubmit = async (e) => {
    e.preventDefault();
    const res = await fetch('http://localhost:8080/api/auth/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(user)
    });
    if (res.ok) { alert("Registered! Check your DB."); window.location.href='/login'; }
  };
  return (
    <div style={{padding: '20px'}}>
      <h2>Register Page</h2>
      <form onSubmit={handleSubmit}>
        <input type="text" placeholder="Username" onChange={e => setUser({...user, username: e.target.value})} /><br/>
        <input type="email" placeholder="Email" onChange={e => setUser({...user, email: e.target.value})} /><br/>
        <input type="password" placeholder="Password" onChange={e => setUser({...user, password: e.target.value})} /><br/>
        <button type="submit">Register</button>
      </form>
    </div>
  );
};

// --- LOGIN LOGIC ---
const Login = () => {
  const [user, setUser] = useState({ username: '', password: '' });
  const handleLogin = async (e) => {
    e.preventDefault();
    const res = await fetch('http://localhost:8080/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(user)
    });
    if (res.ok) { window.location.href='/dashboard'; }
  };
  return (
    <div style={{padding: '20px'}}>
      <h2>Login Page</h2>
      <form onSubmit={handleLogin}>
        <input type="text" placeholder="Username" onChange={e => setUser({...user, username: e.target.value})} /><br/>
        <input type="password" placeholder="Password" onChange={e => setUser({...user, password: e.target.value})} /><br/>
        <button type="submit">Login</button>
      </form>
    </div>
  );
};

// --- DASHBOARD ---
const Dashboard = () => (
  <div style={{padding: '20px'}}>
    <h2>User Dashboard</h2>
    <p>Welcome! This is a protected route.</p>
    <button onClick={() => window.location.href='/login'}>Logout</button>
  </div>
);

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
        <Route path="/" element={<Login />} />
      </Routes>
    </Router>
  );
}

export default App;