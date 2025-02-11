import React, { useState, useEffect } from "react";
import axios from "axios";

function Filter() {
  const [domainList, setDomainList] = useState([]);
  const [filteredDomains, setFilteredDomains] = useState([]);
  const [selectedDays, setSelectedDays] = useState(null);
  const userId = localStorage.getItem("userId"); // Assuming you store user ID in localStorage

  useEffect(() => {
    if (!userId) return; // Ensure userId exists before making an API call

    async function fetchData() {
      try {
        const response = await axios.get(`https://your-api-endpoint.com/domains?userId=${userId}`);
        const allDomains = response.data; // Backend already filters by user ID
        setDomainList(allDomains);
        setFilteredDomains(allDomains); // Initially, show all records
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    }

    fetchData();
  }, [userId]);

  // Function to filter based on button click
  const handleFilter = (days) => {
    setSelectedDays(days);
    const filtered = domainList.filter((domain) => domain.remainingDays <= days);
    setFilteredDomains(filtered);
  };

  return (
    <div className="p-4">
      <div className="flex gap-2 mb-4">
        <button onClick={() => handleFilter(30)} className="bg-blue-600 px-4 py-2 text-white rounded">30 days</button>
        <button onClick={() => handleFilter(15)} className="bg-green-600 px-4 py-2 text-white rounded">15 days</button>
        <button onClick={() => handleFilter(7)} className="bg-red-600 px-4 py-2 text-white rounded">7 days</button>
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
                <td className="border border-gray-600 p-2">{domain.status}</td>
                <td className="border border-gray-600 p-2">{domain.remainingDays}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="3" className="text-center text-gray-400 py-4">No matching records found</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default Filter;
