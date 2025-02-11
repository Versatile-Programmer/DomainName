import React, { useState } from "react";

const RequestedDomains = () => {
  const [domainlist,setDomainlist] = useState([]);
  if (domainlist.length === 0) return null;
  const userRole = localStorage.getItem("role");
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

  return (
    <div className="bg-gray-800 p-4 rounded-lg shadow-md">
      <h2 className="text-lg mb-3">Requested Domains</h2>
      <div className="overflow-x-auto">
        <table className="w-full border-collapse border border-gray-700">
          <thead>
            <tr className="bg-gray-700">
              <th className="border border-gray-600 p-2">Domain Name</th>
              <th className="border border-gray-600 p-2">Status</th>
              <th className="border border-gray-600 p-2">ARM Status</th>
              <th className="border border-gray-600 p-2">HOD Status</th>
              <th className="border border-gray-600 p-2">Requested Date</th>
            </tr>
          </thead>
          <tbody>
            {domainlist.map((domain, index) => (
              <tr key={index} className="text-center bg-gray-900">
                <td className="border border-gray-600 p-2">{domain.domainName}</td>
                <td className="border border-gray-600 p-2">{domain.status}</td>
                <td className="border border-gray-600 p-2">{domain.armStatus}</td>
                <td className="border border-gray-600 p-2">{domain.hodStatus}</td>
                <td className="border border-gray-600 p-2">{domain.requestedDate}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default RequestedDomains;
