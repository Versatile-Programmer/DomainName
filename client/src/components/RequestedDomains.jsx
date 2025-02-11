import React, { useState, useEffect } from "react";
import axios from "axios"; // Import axios

const RequestedDomains = () => {
  const [domainlist, setDomainlist] = useState([]);
  const userRole = localStorage.getItem("role");

  const userData = JSON.parse(localStorage.getItem("userData") || "{}");
  const drmId = userData.id;

  useEffect(() => {
    if (userRole === "DRM") {
      const fetchDomains = async () => {
        try {
          const response = await axios.get(`http://localhost:8080/drm/requestedDomains/${drmId}`,{
            headers: {
              "Content-Type": "application/json",
            },
            withCredentials: true
          }); // Replace with actual API

          console.log("responseDRM ", response.data);
          setDomainlist(response.data);
        } catch (error) {
          console.error("Error fetching domain list:", error);
        }
      };
      fetchDomains();
    }
  }, [userRole]);

  return (
    <div className="bg-gray-800 p-4 rounded-lg shadow-md">
      <h2 className="text-lg mb-3 text-white">Requested Domains</h2>
      <div className="overflow-x-auto">
        {domainlist.length === 0 ? (
          <p className="text-gray-400">No domains requested</p>
        ) : (
          <table className="w-full border-collapse border border-gray-700">
            <thead>
              <tr className="bg-gray-700 text-white">
                <th className="border border-gray-600 p-2">Domain Name</th>
                <th className="border border-gray-600 p-2">Active Status</th>
                <th className="border border-gray-600 p-2">ARM Status</th>
                <th className="border border-gray-600 p-2">HOD Status</th>
                <th className="border border-gray-600 p-2">Requested Date</th>
              </tr>
            </thead>
            <tbody>
              {domainlist.map((domain, index) => (
                <tr key={index} className="text-center bg-gray-900 text-white">
                  <td className="border border-gray-600 p-2">{domain.domainName}</td>
                  <td className="border border-gray-600 p-2">{(domain.status.activeStatus)?"ACTIVE":"NOT ACTIVE"}</td>
                  <td className="border border-gray-600 p-2">{domain.status.armStatus}</td>
                  <td className="border border-gray-600 p-2">{domain.status.hodStatus}</td>
                  <td className="border border-gray-600 p-2">{domain.dates.drmRequestedDate}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
};

export default RequestedDomains;
