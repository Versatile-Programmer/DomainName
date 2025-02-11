import React, { useEffect, useState } from "react";
import axios from "axios";

const ARMRequestedDomains = () => {
  const [armDomainList, setArmDomainList] = useState([]);
  const userData = JSON.parse(localStorage.getItem("userData") || "{}");
  const armId = userData.id;
  useEffect(() => {
    const fetchARMRequests = async () => {
      try {
        const response = await axios.get(
        `http://localhost:8080/arm/getDomainRequests/${armId}`,{
          headers: {
            "Content-Type": "application/json",
          },
          withCredentials: true
        }
        ); // Replace with correct API endpoint
        console.log("response ARM", response.data);
        setArmDomainList(response.data);
      } catch (error) {
        console.error("Error fetching ARM domain requests:", error);
      }
    };

    fetchARMRequests();
  }, []);

  if (armDomainList.length === 0)
    return <h1 className="text-center text-gray-400">No ARM Requests Yet</h1>;

  return (
    <div className="bg-gray-800 p-4 rounded-lg shadow-md">
      <h2 className="text-lg mb-3">All ARM Requested Domains</h2>
      <div className="overflow-x-auto">
        {armDomainList.length === 0 ? (
          <p className="text-gray-400">No domains requested</p>
        ) : (
          <table className="w-full border-collapse border border-gray-700">
            <thead>
              <tr className="bg-gray-700">
                <th className="border border-gray-600 p-2">Domain Name</th>
                <th className="border border-gray-600 p-2">Active Status</th>
                <th className="border border-gray-600 p-2">ARM Status</th>
                <th className="border border-gray-600 p-2">HOD Status</th>
                <th className="border border-gray-600 p-2">Requested Date</th>
              </tr>
            </thead>
            <tbody>
              {armDomainList.map((domain, index) => (
                <tr key={index} className="text-center bg-gray-900">
                  <td className="border border-gray-600 p-2">
                    {domain.domainName}
                  </td>
                  <td className="border border-gray-600 p-2">
                  {(domain.status.activeStatus)?"ACTIVE":"NOT ACTIVE"}
                  </td>
                  <td className="border border-gray-600 p-2">
                  {domain.status.armStatus}
                  </td>
                  <td className="border border-gray-600 p-2">
                  {domain.status.hodStatus}
                  </td>
                  <td className="border border-gray-600 p-2">
                  {domain.dates.drmRequestedDate}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
};

export default ARMRequestedDomains;
