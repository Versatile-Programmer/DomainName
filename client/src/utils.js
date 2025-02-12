import { toast } from "react-toastify";

export const handleSuccess = (msg) => {
    toast.dismiss(); // Clears existing toasts to avoid stacking
    toast.success(msg, {
        position: "top-right",
        autoClose: 3000, // Closes the toast after 3 seconds
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        theme: "light",
    });
};

export const handleError = (msg) => {
    toast.dismiss();
    toast.error(msg, {
        position: "top-right",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        theme: "light",
    });
};
