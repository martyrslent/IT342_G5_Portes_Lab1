import React, { useState } from 'react';

const Login = () => {
  const [credentials, setCredentials] = useState({ username: '', password: '' });

  const handleLogin = async (e) => {
    e.preventDefault();
    const response = await fetch('http://localhost:8080/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(credentials)
    });

    if (response.ok) {
      alert("Login Successful!");
      window.location.href = '/dashboard'; // Redirect to Dashboard
    } else {
      alert("Login Failed");
    }
  };

  return (
    <div style={{ padding: '20px' }}>
      <h2>Login</h2>
      <form onSubmit={handleLogin}>
        <input type="text" placeholder="Username" onChange={(e) => setCredentials({...credentials, username: e.target.value})} /><br/><br/>
        <input type="password" placeholder="Password" onChange={(e) => setCredentials({...credentials, password: e.target.value})} /><br/><br/>
        <button type="submit">Login</button>
      </form>
    </div>
  );
};

export default Login;