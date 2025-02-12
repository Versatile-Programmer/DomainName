// ARMRequestedDomains.js

import React, { useEffect, useState } from "react";
import axios from "axios";
import { handleError, handleSuccess } from "../utils";
const ARMRequestedDomains = () => {
  const [armDomainList, setArmDomainList] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const userData = JSON.parse(localStorage.getItem("userData") || "{}");
  const armId = userData.id;

  useEffect(() => {
    fetchARMRequests();
  }, []);

  const fetchARMRequests = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8080/arm/getDomainRequests/${armId}`,
        {
          headers: {
            "Content-Type": "application/json",
          },
          withCredentials: true,
        }
      );
      console.log("ARM Domain Requests:", response.data);
      setArmDomainList(response.data);
    } catch (error) {
      console.error("Error fetching ARM domain requests:", error);
      handleError("Failed to fetch Domain");
    }
  };

  const handleApprove = async (domainId) => {
    setIsLoading(true);
    try {
      const response = await axios.post(
        `http://localhost:8080/arm/verifyDomainRequest/${domainId}`,
        {},
        { 
          headers: { "Content-Type": "application/json" },
          withCredentials: true 
        }
      );
      if (response.status === 200 || response.status === 201 || response.status === 202) {
        fetchARMRequests();
        handleSuccess("Request Approved");
      }
    } catch (error) {
      console.error("Error approving request:", error);
      handleError("internal server error")
    } finally {
      setIsLoading(false);
    }
  };

  const handleReject = async (domainId) => {
    setIsLoading(true);
    try {
      const response = await axios.post(
        `http://localhost:8080/arm/sendForReview/${domainId}`,
        {},
        { 
          headers: { "Content-Type": "application/json" },
          withCredentials: true
        }
      );
      if (response.status === 200) {
        // alert("Request Rejected");
        fetchARMRequests();
      }
      handleSuccess("Request Sent For Review To DRM"); 

    } catch (error) {
      console.error("Error rejecting request:", error);
      handleError("Failed to Reject");
    } finally {
      setIsLoading(false);
    }
  };

  if (armDomainList.length === 0)
    return <h1 className="text-center text-gray-400">No ARM Requests Yet</h1>;

  return (
    <div className="bg-gray-800 p-4 rounded-lg shadow-md">
      <h2 className="text-lg mb-3">All ARM Requested Domains</h2>
      <div className="overflow-x-auto">
        <table className="w-full border-collapse border border-gray-700">
          <thead>
            <tr className="bg-gray-700">
              <th className="border border-gray-600 p-2">Domain Name</th>
              <th className="border border-gray-600 p-2">Active Status</th>
              <th className="border border-gray-600 p-2">ARM Status</th>
              <th className="border border-gray-600 p-2">HOD Status</th>
              <th className="border border-gray-600 p-2">Requested Date</th>
              <th className="border border-gray-600 p-2">Actions</th>
            </tr>
          </thead>
          <tbody>
            {armDomainList.map((domain) => (
              <tr key={domain.id} className="text-center bg-gray-900">
                <td className="border border-gray-600 p-2">{domain.domainName}</td>
                <td className="border border-gray-600 p-2">
                  {domain.status.activeStatus ? "ACTIVE" : "NOT ACTIVE"}
                </td>
                <td className="border border-gray-600 p-2">{domain.status.armStatus}</td>
                <td className="border border-gray-600 p-2">{domain.status.hodStatus}</td>
                <td className="border border-gray-600 p-2">{domain.dates.drmRequestedDate}</td>
                <td className="border border-gray-600 p-2">
                  <button
                    onClick={() => handleApprove(domain.requestId)}
                    className="bg-green-600 text-white px-3 py-1 rounded mr-2 hover:bg-green-700"
                    // disabled={isLoading || domain.status.armStatus !== "NOT_VERIFIED"}
                  >
                    Accept
                  </button>
                  <button
                    onClick={() => handleReject(domain.requestId)}
                    className="bg-red-600 text-white px-3 py-1 rounded hover:bg-red-700"
                    // disabled={isLoading || domain.status.armStatus !== "NOT_VERIFIED"}
                  >
                    Send for Review
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default ARMRequestedDomains;
