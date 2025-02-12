import React ,{useState} from 'react'
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { ToastContainer } from 'react-toastify';
import { handleError, handleSuccess } from '../utils';


export default function Signup() {
  const navigate = useNavigate();
    const handleChange = (e)=>{
        console.log(e.target);
        const {name,value}=e.target;
        setFormData({...formData, [name]:value});
    }   
    const handleSignup = async(e)=>{
        e.preventDefault();

        console.log(e.target);
        // console.log(formData);
        const data = new FormData(e.target);
        const payload = {
            drmName: data.get('name'),
            drmDept: data.get('Department'),
            drmEmail: data.get('email'),
            drmPassword: data.get('password')
        }
        console.log(payload);
        try {
            const res = await axios.post("http://localhost:8080/register/registerDrm",payload,{
                headers: {
                    'Content-Type': 'application/json'
                },
                withCredentials: true
                
            })
            handleSuccess("Signup Successful")
            setTimeout(() => {
              navigate('/login');
            }, 1000);
        } catch (error) {
            // console.error("error message",error);
            handleError("Signup Failed")
        }
    }
  return (
    <div className="min-h-screen bg-gray-900 text-white flex items-center justify-center">
    <div className="w-full max-w-md bg-gray-800 p-6 rounded-lg shadow-md">
      <h2 className="text-3xl font-bold text-center text-teal-400 mb-6">Signup</h2>
      <form onSubmit={handleSignup} className="space-y-4">
        <div>
          <label htmlFor="name" className="block text-sm text-gray-300">
            Name
          </label>
          <input
            type="text"
            id="name"
            name="name"
            // value={formData.name}
            // onChange={handleChange}
            className="w-full p-3 bg-gray-700 rounded-lg text-white focus:ring-2 focus:ring-teal-400"
            placeholder="Enter your name"
            required
          />
        </div>
        <div>
          <label htmlFor="department" className="block text-sm text-gray-300">
            Department
          </label>
          <input
            type="text"
            id="department"
            name="Department"
            // value={formData.Department}
            // onChange={handleChange}
            className="w-full p-3 bg-gray-700 rounded-lg text-white focus:ring-2 focus:ring-teal-400"
            placeholder="Enter your Department"
            required
          />
        </div>
        <div>
          <label htmlFor="email" className="block text-sm text-gray-300">
            Email
          </label>
          <input
            type="email"
            id="email"
            name="email"
            // value={formData.email}
            // onChange={handleChange}
            className="w-full p-3 bg-gray-700 rounded-lg text-white focus:ring-2 focus:ring-teal-400"
            placeholder="Enter your email"
            required    
          />
        </div>
        {/* <div>
          <label htmlFor="contactNumber" className="block text-sm text-gray-300">
            Contact Number
          </label>
          <input
            type="tel"
            id="contactNumber"
            name="contactNumber"
            value={formData.contactNumber}
            onChange={handleChange}
            className="w-full p-3 bg-gray-700 rounded-lg text-white focus:ring-2 focus:ring-teal-400"
            placeholder="Enter your contact number"
            required
          />
        </div> */}
        <div>
          <label htmlFor="password" className="block text-sm text-gray-300">
            Password
          </label>
          <input
            type="password"
            id="password"
            name="password"
            // value={formData.password}
            // onChange={handleChange}
            className="w-full p-3 bg-gray-700 rounded-lg text-white focus:ring-2 focus:ring-teal-400"
            placeholder="Enter your password"
            required    
          />
        </div>
        <button
          type="submit"
          className="w-full bg-teal-500 hover:bg-teal-600 text-white py-3 rounded-lg font-bold transition duration-300"
        >
          Signup
        </button>
      </form>
      <p className="text-gray-400 text-sm text-center mt-4">
        Already have an account?{" "}
        <a href="/login" className="text-teal-400 hover:underline">
          Login
        </a>
      </p>
    </div>
    <ToastContainer/>
  </div>
  )
}
