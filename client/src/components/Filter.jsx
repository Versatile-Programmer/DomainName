import React, { useState, useEffect } from "react";
import axios from "axios";

function Filter() {
  const [domainList, setDomainList] = useState([]);
  const [filteredDomains, setFilteredDomains] = useState([]);
  const [loading, setLoading] = useState(true); // Loading state for fetching data

  // Fetch user details from localStorage
  const userData = JSON.parse(localStorage.getItem("userData") || "{}");
  const role = userData.role;
  const id = userData.id;

  // Fetch data on component mount
  useEffect(() => {
    if (!id) return; // Ensure `id` exists before making an API call

    async function fetchData() {
      try {
        setLoading(true); // Set loading to true while fetching
        const endpoint =
          role === "ROLE_DRM"
            ? `http://localhost:8080/drm/requestedDomains/${id}`
            : `http://localhost:8080/arm/getDomainRequests/${id}`;

        const response = await axios.get(endpoint, {
          headers: { "Content-Type": "application/json" },
          withCredentials: true, // Ensure cookies are sent with the request
        });

        // Filter out inactive domains
        const validDomains = response.data.filter(
          (domain) =>
            domain.daysLeftTillExpiry !== null &&
            domain.daysLeftTillExpiry !== undefined &&
            domain.status.activeStatus
        );

        // Set both the domain list and filtered list
        setDomainList(validDomains);
        setFilteredDomains(validDomains); // Set initial filtered domains
      } catch (error) {
        console.error("Error fetching data:", error);
      } finally {
        setLoading(false); // Set loading to false once the fetch is complete
      }
    }

    fetchData();
  }, [id, role]);

  // Function to filter records based on days left till expiry
  const handleFilter = (days) => {
    const filtered = domainList.filter(
      (domain) =>
        domain.daysLeftTillExpiry <= days && domain.status.activeStatus
    );
    setFilteredDomains(filtered);
  };

  if (loading) {
    return <div className="text-center">Loading...</div>; // Show loading message while fetching data
  }

  return (
    <div className="p-4">
      <div className="flex gap-2 mb-4">
        <button
          onClick={() => handleFilter(30)}
          className="bg-blue-600 px-4 py-2 text-white rounded"
        >
          30 days
        </button>
        <button
          onClick={() => handleFilter(15)}
          className="bg-green-600 px-4 py-2 text-white rounded"
        >
          15 days
        </button>
        <button
          onClick={() => handleFilter(7)}
          className="bg-red-600 px-4 py-2 text-white rounded"
        >
          7 days
        </button>
      </div>

      <table className="w-full border-collapse border border-gray-700">
        <thead>
          <tr className="bg-gray-700 text-white">
            <th className="border border-gray-600 p-2">Domain Name</th>
            <th className="border border-gray-600 p-2">Status</th>
            <th className="border border-gray-600 p-2">Remaining Days</th>
          </tr>
        </thead>
        <tbody>
          {filteredDomains.length > 0 ? (
            filteredDomains.map((domain, index) => (
              <tr key={index} className="text-center bg-gray-900 text-white">
                <td className="border border-gray-600 p-2">{domain.domainName}</td>
                <td className="border border-gray-600 p-2">
                  {domain.status.activeStatus ? "ACTIVE" : "NOT ACTIVE"}
                </td>
                <td className="border border-gray-600 p-2">
                  {domain.daysLeftTillExpiry}
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="3" className="text-center text-gray-400 py-4">
                No matching records found
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default Filter;
