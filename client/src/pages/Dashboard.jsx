import React, { useState, useEffect } from "react";
import RequestedDomains from "../components/RequestedDomains";

const Dashboard = () => {
  const [showForm, setShowForm] = useState(false);
  const [domainlist, setDomainlist] = useState([]);
  const [showNotifications, setShowNotifications] = useState(false);
  const [notifications, setNotifications] = useState([
    "Welcome to Dashboard!",
    "Your request is being processed.",
  ]);

  // Load data from localStorage when the component mounts
  useEffect(() => {
    const storedDomains = JSON.parse(localStorage.getItem("domains")) || [];
    setDomainlist(storedDomains);
  }, []);

  // Function to handle domain request
  const handleSubmit = (e) => {
    e.preventDefault();
    const data = new FormData(e.target);
    const domainName = data.get("domain");
    const newUser = {
        domainName,
        status :"pending",
        armStatus :"pending",
        hodStatus: "pending",
        requestedDate: new Date().toLocaleDateString()
    }
    // Update state and localStorage
    const updatedDomainList = [...domainlist, newUser];
    setDomainlist(updatedDomainList);
    localStorage.setItem("domains", JSON.stringify(updatedDomainList));
    setShowForm(false);
  };

  return (
    <div className="min-h-screen bg-gray-900 text-white p-6">
      {/* Buttons */}
      <div className="flex justify-between items-center mb-6">
        <button
          className="bg-blue-600 hover:bg-blue-700 px-4 py-2 rounded-lg"
          onClick={() => setShowForm(true)}
        >
          Request Domain
        </button>
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

      {/* Request Form */}
      {showForm && (
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

      {/* Requested Domains Table */}
      {domainlist.length > 0 && <RequestedDomains domainlist={domainlist} />}

      {domainlist.length === 0 && !showForm && (
        <h1 className="text-center text-gray-400">No Requests Yet</h1>
      )}
    </div>
  );
};

export default Dashboard;
