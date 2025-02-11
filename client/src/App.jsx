import { react ,useState} from 'react'
import './App.css'
import Signup from './pages/Signup';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import {  Routes, Route } from "react-router-dom";

  function App() {
    
  const [value ,setValue] = useState(500);

  return (
    <>
        <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/dashboard" element={<Dashboard />} />
      </Routes>

    </>
  ) 

}
          
export default App;
          

