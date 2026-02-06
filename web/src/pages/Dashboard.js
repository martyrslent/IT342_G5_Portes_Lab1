import React from 'react';

const Dashboard = () => {
  const handleLogout = () => {
    localStorage.removeItem('user'); // Basic logout logic
    window.location.href = '/login';
  };

  return (
    <div style={{ padding: '20px' }}>
      <h2>Dashboard / Profile</h2>
      <p>Welcome to your protected profile page!</p>
      <button onClick={handleLogout} style={{ backgroundColor: 'red', color: 'white' }}>Logout</button>
    </div>
  );
};

export default Dashboard;