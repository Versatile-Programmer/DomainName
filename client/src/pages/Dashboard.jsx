import React, { useState, useEffect } from "react";
import RequestedDomains from "../components/RequestedDomains";
import ARMRequestedDomains from "../components/ARMRequestedDomains";
import axios from "axios";
import Filter from "../components/Filter";
import { ToastContainer } from "react-toastify";
import { useNavigate } from "react-router-dom";
import { handleError, handleSuccess } from "../utils";

const Dashboard = () => {
  const [showForm, setShowForm] = useState(false);
  const [showARMRequests, setShowARMRequests] = useState(false);
  const [userRole, setUserRole] = useState("");

  const navigate = useNavigate();

  useEffect(() => {
    setUserRole(localStorage.getItem("role"));
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const data = new FormData(e.target);
    const domainName = data.get("domain");

    const userData = JSON.parse(localStorage.getItem("userData") || "{}");

    const newDomainRequest = {
      domainName,
      drmId: userData.id,
      dept: userData.dept
    };

    try {
      const response = await axios.post(
        "http://localhost:8080/domain/requestDomain",
        newDomainRequest,
        {
          headers: { "Content-Type": "application/json" },
          withCredentials: true
        }
      );

      if ([200, 201, 202].includes(response.status)) {
        handleSuccess("Request Created Successfully");
        setShowForm(false);
      }
    } catch (error) {
      console.error("Error submitting request:", error);
      handleError("Failed to create Request");
    }
  };

  const handleLogout = async () => {
    try {
      localStorage.clear();
      sessionStorage.clear();
      await axios.post("http://localhost:8080/logout",{},{
        headers: { "Content-Type": "application/json" }
        ,withCredentials: true
      });
      handleSuccess("You have been logged out successfully");
    } catch (error) {
      console.error("Error logging out:", error);
      handleError("Logout Failed");
    } finally {
      setTimeout(() => {
        navigate('/login');
      }, 1000);
    }
  };

  return (
    <div className="min-h-screen bg-gray-900 text-white p-6">
      <div className="flex justify-between items-center mb-6">
        {userRole === "DRM" && (
          <button
            className="bg-blue-600 hover:bg-blue-700 px-4 py-2 rounded-lg"
            onClick={() => setShowForm(true)}
          >
            Request Domain
          </button>
        )}

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
      </div>

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

      {userRole === "ARM" && showARMRequests && <ARMRequestedDomains />}
      {userRole === "DRM" && <RequestedDomains />}
      <Filter />
      <ToastContainer />
    </div>
  );
};

export default Dashboard;
