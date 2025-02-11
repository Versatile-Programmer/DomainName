import React, { useState } from "react";
import axios from 'axios';
import { useNavigate } from "react-router-dom";
const Login = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async (e) => {
      e.preventDefault();
      const basicAuth = `Basic ${btoa(`${email}:${password}`)}`;


    try {
        const res=axios.post("http://localhost:8080/login/loginUser",{},{
            headers: {
                'Content-Type': 'application/json',
                'Authorization': basicAuth
         },
         withCredentials:true
        }
        )
        console.log(res.data);
        navigate('/dashboard');
    } catch (error) {
        console.error("error",error);
    }


      console.log("email: ", email);
      console.log("password: ", password);
      // if(!email || !password) {
    //   handleError("Email and password is required");
    // }
    // Replace this with your actual API endpoint for login
  };

  return (
    <div className="min-h-screen bg-gray-900 text-white flex items-center justify-center">
      <div className="w-full max-w-md bg-gray-800 p-6 rounded-lg shadow-md">
        <h2 className="text-3xl font-bold text-center text-teal-400 mb-6">Login</h2>
        {/* {error && <p className="text-red-500 text-sm mb-4">{error}</p>} */}
        <form onSubmit={handleLogin} className="space-y-4">
          <div>
            <label htmlFor="email" className="block text-sm text-gray-300">
              Email
            </label>
            <input
              type="email"
              id="email"
              value={email}
              onChange={(e) => {
                console.log(e.target.value)
               return setEmail(e.target.value)
              }}
              className="w-full p-3 bg-gray-700 rounded-lg text-white focus:ring-2 focus:ring-teal-400"
              placeholder="Enter your email"
              required
            />
          </div>
          <div>
            <label htmlFor="password" className="block text-sm text-gray-300">
              Password
            </label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => {
                console.log(e.target.value)
                setPassword(e.target.value)}}
              className="w-full p-3 bg-gray-700 rounded-lg text-white focus:ring-2 focus:ring-teal-400"
              placeholder="Enter your password"
              required
            />
          </div>
          <button
            type="submit"
            className="w-full bg-teal-500 hover:bg-teal-600 text-white py-3 rounded-lg font-bold transition duration-300"
          >
            Login
          </button>
        </form>
        <p className="text-gray-400 text-sm text-center mt-4">
          Don't have an account?{" "}
          <a href="/signup" className="text-teal-400 hover:underline">
            Signup
          </a>
        </p>
      </div>
      {/* <ToastContainer/> */}
    </div>
  );
};

export default Login;
