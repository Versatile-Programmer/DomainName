import React, { useState, useEffect } from "react";
import RequestedDomains from "../components/RequestedDomains";
import ARMRequestedDomains from "../components/ARMRequestedDomains"; // Import the ARM-specific component
import axios from "axios";
import Filter from "../components/Filter";

import Cookies from "js-cookie"
import { useNavigate } from "react-router-dom";

const Dashboard = () => {
  const [showForm, setShowForm] = useState(false);
  const [showNotifications, setShowNotifications] = useState(false);
  const [notifications, setNotifications] = useState([
    "Welcome to Dashboard!",
    "Your request is being processed.",
  ]);

  const userRole = localStorage.getItem("role");
  const navigate = useNavigate();

  // DRM submits a new domain request
  const handleSubmit = async (e) => {
    e.preventDefault();
    const data = new FormData(e.target);
    const domainName = data.get("domain");


    const userData = JSON.parse(localStorage.getItem("userData") || "{}");

    const drmId = userData.id;
    const dept=userData.dept;
  //   private LocalDate neededTillDate;
	

	// private String dept;
	

    const newDomainRequest = {
      domainName:domainName,
      drmId: drmId,
      dept:dept
      //requestedDate: new Date().toLocaleDateString(),
    };

    try {
      const response = await axios.post(
        "http://localhost:8080/domain/requestDomain", // Replace with actual API
        newDomainRequest,
        {
          headers: { 
            "Content-Type": "application/json"
           },
           withCredentials:true
        }
      );

      console.log("response IN DASHBOARD", response.data);
      if (response.status === 200 || response.status === 201) {
        console.log("Successfully submitted");
         setShowForm(false);
      }
    } catch (error) {
      console.error("Error submitting request:", error);
    }
  };

  

const clearCookies = () => {
  Cookies.remove("JSESSIONID", { path: "/" }); // Remove with path
 // Cookies.remove("another_cookie", { path: "/" }); // Remove with specific path
}



  const handleLogout = () => {
    // // Clear all cookies
    // document.cookie.split(";").forEach((cookie) => {
    //   document.cookie = cookie
    //     .replace(/^ +/, "") // Trim leading spaces
    //     .replace(/=.*/, "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/");
    // });
  
    // // Alternative: Using js-cookie (Recommended)
    // Object.keys(Cookies.get()).forEach((cookie) => Cookies.remove(cookie));
  
    // // Clear localStorage
    // localStorage.clear();
  
    // // Clear sessionStorage (optional)
    // sessionStorage.clear();

    clearCookies();
    localStorage.clear();
    sessionStorage.clear();
    // window.location.reload(); 
  
    // Redirect to login page
    navigate('/login'); // Change the path accordingly
  };

  return (
    <div className="min-h-screen bg-gray-900 text-white p-6">
      {/* Top Bar: Buttons and Notifications */}
      <div className="flex justify-between items-center mb-6">
        {/* Show "Request Domain" button for DRM */}
        {userRole === "DRM" && (
          <button
            className="bg-blue-600 hover:bg-blue-700 px-4 py-2 rounded-lg"
            onClick={() => setShowForm(true)}
          >
            Request Domain
          </button>
        )}

        {/* Show "Approve Requests" button for ARM */}
        {userRole === "ARM" && (
          <button
            className="bg-green-600 hover:bg-green-700 px-4 py-2 rounded-lg"
            onClick={() => setShowARMRequests(!showARMRequests)}
          >
            {showARMRequests ? "Hide Requests" : "Approve Requests"}
          </button>
        )}
        <button
          onClick={handleLogout}
          className="bg-red-600 hover:bg-red-700 px-4 py-2 rounded-lg"
        >
          Logout
        </button>
        {/* Notifications Button (Unchanged for both roles) */}
        {/* <button
          className="relative bg-gray-700 hover:bg-gray-600 p-2 rounded-full"
          onClick={() => setShowNotifications(!showNotifications)}
        >
          🔔
          {showNotifications && (
            <div className="absolute right-0 mt-2 w-60 bg-gray-800 shadow-lg rounded-lg p-3">
              {notifications.length > 0 ? (
                notifications.map((msg, index) => (
                  <p key={index} className="text-sm text-gray-300">
                    {msg}
                  </p>
                ))
              ) : (
                <p className="text-sm text-gray-400">No Notifications</p>
              )}
            </div>
          )}
        </button> */}
      </div>

      {/* DRM Request Form */}
      {showForm && userRole === "DRM" && (
        <div className="bg-gray-800 p-4 rounded-lg shadow-md mb-6">
          <h2 className="text-lg mb-3">Domain Request Form</h2>
          <form onSubmit={handleSubmit}>
            <input
              name="domain"
              type="text"
              placeholder="Enter domain name"
              className="w-full p-2 rounded bg-gray-700 text-white focus:outline-none"
              required
            />
            <button
              type="submit"
              className="mt-3 bg-green-600 hover:bg-green-700 px-4 py-2 rounded-lg"
            >
              Submit
            </button>
          </form>
        </div>
      )}

      {/* Show ARM Requests Table only for ARM Users */}
      {userRole === "ARM" && showARMRequests && <ARMRequestedDomains />}

      {/* Show DRM Requests Table only for DRM Users */}
      {userRole === "DRM" && <RequestedDomains/>}
      <Filter/>
    </div>
  );
};

export default Dashboard;
