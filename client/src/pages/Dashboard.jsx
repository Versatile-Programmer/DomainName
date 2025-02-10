import React, { useState, useEffect } from "react";
import RequestedDomains from "../components/RequestedDomains";
import ARMRequestedDomains from "../components/ARMRequestedDomains"; // Import the ARM-specific component
import axios from "axios";

const Dashboard = () => {
  const [showForm, setShowForm] = useState(false);
  const [domainlist, setDomainlist] = useState([]);
  const [showNotifications, setShowNotifications] = useState(false);
  const [notifications, setNotifications] = useState([
    "Welcome to Dashboard!",
    "Your request is being processed.",
  ]);

  const userRole = localStorage.getItem("role");

  // Fetch DRM domain requests
  useEffect(() => {
    if (userRole === "DRM") {
      const fetchDomains = async () => {
        try {
          const response = await axios.get("https://api.example.com/domains"); // Replace with actual API
          setDomainlist(response.data);
        } catch (error) {
          console.error(error);
        }
      };
      fetchDomains();
    }
  }, [userRole]);

  // DRM submits a new domain request
  const handleSubmit = async (e) => {
    e.preventDefault();
    const data = new FormData(e.target);
    const domainName = data.get("domain");

    const newDomainRequest = {
      domainName,
      status: "pending",
      armStatus: "pending",
      hodStatus: "pending",
      requestedDate: new Date().toLocaleDateString(),
    };

    try {
      const response = await axios.post(
        "https://apiforpost.com", // Replace with actual API
        newDomainRequest,
        {
          headers: { "Content-Type": "application/json" },
        }
      );
      if (response.status === 200 || response.status === 201) {
        // Successfully submitted, update UI
        setDomainlist([...domainlist, newDomainRequest]);
        setShowForm(false);
      }
    } catch (error) {
      console.error("Error submitting request:", error);
    }
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
          <button className="bg-green-600 hover:bg-green-700 px-4 py-2 rounded-lg">
            Approve Requests
          </button>
        )}

        {/* Notifications Button (Unchanged for both roles) */}
        <button
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
        </button>
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
      {userRole === "ARM" && <ARMRequestedDomains />}

      {/* Show DRM Requests Table only for DRM Users */}
      {userRole === "DRM" && <RequestedDomains domainlist={domainlist} />}
    </div>
  );
};

export default Dashboard;
