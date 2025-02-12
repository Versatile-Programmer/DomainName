import { react ,useState} from 'react'
import './App.css'
import Signup from './pages/Signup';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import {  Routes, Route } from "react-router-dom";
import ProtectedRoute from './components/ProtectedRoute';

  function App() {
    
 

  return (
    <>
        <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/dashboard" element={<ProtectedRoute>
          <Dashboard />
        </ProtectedRoute>} />
      </Routes>
    </>
  ) 

}
          
export default App;
          

