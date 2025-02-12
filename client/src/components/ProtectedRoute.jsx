// src/components/ProtectedRoute.js
import React from "react";
import { Navigate } from "react-router-dom";

const ProtectedRoute = ({ children }) => {
  // Check if necessary info is present in localStorage
  const userRole = localStorage.getItem("role");
  const userData = localStorage.getItem("userData");

  // If the info is present, render the child component
  if (userRole && userData) {
    return children;
  } else {
    // Otherwise, redirect to the login page
    return <Navigate to="/login" />;
  }
};

export default ProtectedRoute;
