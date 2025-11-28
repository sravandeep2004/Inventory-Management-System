import React, { useEffect, useState } from 'react';
import api from '../api/axiosConfig';
import { toast } from 'react-toastify';

import { useAuth } from '../context/AuthContext';

const StaffList = () => {
    const { user } = useAuth();
    const [staff, setStaff] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        password: '',
        designation: '',
        department: '',
        phoneNumber: '',
        rights: 'STAFF'
    });

    useEffect(() => {
        fetchStaff();
    }, []);

    const fetchStaff = async () => {
        try {
            const response = await api.get('/staff');
            setStaff(response.data);
        } catch (error) {
            console.error("Error fetching staff", error);
            toast.error("Failed to load staff list");
        }
    };

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const validatePhone = (phone) => {
        const phoneRegex = /^\d{10}$/;
        return phoneRegex.test(phone);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!validatePhone(formData.phoneNumber)) {
            toast.error("Phone number must be exactly 10 digits", { position: "bottom-right" });
            return;
        }

        try {
            await api.post('/staff', formData);
            setShowModal(false);
            setFormData({
                name: '',
                email: '',
                password: '',
                designation: '',
                department: '',
                phoneNumber: '',
                rights: 'STAFF'
            });
            fetchStaff();
            toast.success("Staff member added successfully!", { position: "top-right" });
        } catch (error) {
            console.error("Error creating staff", error);
            let msg = "Failed to create staff member.";
            if (error.response && error.response.data) {
                if (typeof error.response.data === 'string' && error.response.data.includes("Email already exists")) {
                    msg = "This email address is already registered.";
                } else if (error.response.data.message) {
                    if (error.response.data.message.includes("Phone number already exists")) {
                        msg = "A staff member with this phone number already exists.";
                    } else if (error.response.data.message.includes("Email already exists")) {
                        msg = "This email address is already registered.";
                    } else {
                        msg = error.response.data.message;
                    }
                }
            }
            toast.error(msg, { position: "bottom-right" });
        }
    };

    const handleDelete = async (person) => {
        if (person.email === user.email) {
            toast.error("You cannot delete your own account.", { position: "bottom-right" });
            return;
        }

        // Removed window.confirm to prevent blocking issues
        try {
            await api.delete(`/staff/${person.id}`);
            fetchStaff();
            toast.success("Staff member deleted.", { position: "top-right" });
        } catch (error) {
            console.error("Error deleting staff", error);
            const errorMsg = error.response?.data?.message || error.message || "Unknown error";
            toast.error(`Failed to delete staff member: ${errorMsg}`);
        }
    };

    return (
        <div className="space-y-6">
            <div className="flex justify-between items-center">
                <h1 className="text-3xl font-bold text-gray-900">Staff Management</h1>
                <button
                    onClick={() => setShowModal(true)}
                    className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700"
                >
                    Add Staff
                </button>
            </div>

            {/* Modal */}
            {showModal && (
                <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
                    <div className="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
                        <div className="mt-3 text-center">
                            <h3 className="text-lg leading-6 font-medium text-gray-900">Add New Staff</h3>
                            <form className="mt-2 text-left space-y-4" onSubmit={handleSubmit}>
                                <input
                                    name="name"
                                    type="text"
                                    placeholder="Name"
                                    required
                                    className="w-full px-3 py-2 border rounded-md"
                                    value={formData.name}
                                    onChange={handleChange}
                                />
                                <input
                                    name="email"
                                    type="email"
                                    placeholder="Email"
                                    required
                                    className="w-full px-3 py-2 border rounded-md"
                                    value={formData.email}
                                    onChange={handleChange}
                                />
                                <input
                                    name="password"
                                    type="password"
                                    placeholder="Password"
                                    required
                                    className="w-full px-3 py-2 border rounded-md"
                                    value={formData.password}
                                    onChange={handleChange}
                                />
                                <input
                                    name="designation"
                                    type="text"
                                    placeholder="Designation"
                                    required
                                    className="w-full px-3 py-2 border rounded-md"
                                    value={formData.designation}
                                    onChange={handleChange}
                                />
                                <input
                                    name="department"
                                    type="text"
                                    placeholder="Department"
                                    required
                                    className="w-full px-3 py-2 border rounded-md"
                                    value={formData.department}
                                    onChange={handleChange}
                                />
                                <input
                                    name="phoneNumber"
                                    type="tel"
                                    placeholder="Phone Number (10 digits)"
                                    required
                                    className="w-full px-3 py-2 border rounded-md"
                                    value={formData.phoneNumber}
                                    onChange={handleChange}
                                />
                                <select
                                    name="rights"
                                    className="w-full px-3 py-2 border rounded-md"
                                    value={formData.rights}
                                    onChange={handleChange}
                                >
                                    <option value="STAFF">Staff</option>
                                    <option value="ADMIN">Admin</option>
                                </select>
                                <div className="flex justify-end space-x-2">
                                    <button
                                        type="button"
                                        onClick={() => setShowModal(false)}
                                        className="px-4 py-2 bg-gray-300 text-gray-700 rounded-md hover:bg-gray-400"
                                    >
                                        Cancel
                                    </button>
                                    <button
                                        type="submit"
                                        className="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700"
                                    >
                                        Save
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            )}

            <div className="bg-white shadow overflow-hidden sm:rounded-md">
                <ul className="divide-y divide-gray-200">
                    {staff.map((person) => (
                        <li key={person.id}>
                            <div className="px-4 py-4 flex items-center justify-between sm:px-6">
                                <div className="flex items-center">
                                    <div className="text-sm font-medium text-indigo-600 truncate">
                                        {person.name}
                                    </div>
                                    <div className="ml-2 flex-shrink-0 flex">
                                        <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${person.rights === 'ADMIN' ? 'bg-purple-100 text-purple-800' : 'bg-green-100 text-green-800'}`}>
                                            {person.rights}
                                        </span>
                                    </div>
                                </div>
                                <div className="flex items-center space-x-4">
                                    <div className="text-sm text-gray-500">
                                        {person.email}
                                    </div>
                                    <button
                                        onClick={() => handleDelete(person)}
                                        disabled={person.email === user?.email}
                                        className={`text-sm font-medium ${person.email === user?.email ? 'text-gray-400 cursor-not-allowed' : 'text-red-600 hover:text-red-900'}`}
                                        title={person.email === user?.email ? "You cannot delete yourself" : "Delete staff member"}
                                    >
                                        Delete
                                    </button>
                                </div>
                            </div>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
};

export default StaffList;
