import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { toast } from 'react-toastify';
import { Package } from 'lucide-react';

const Signup = () => {
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        password: '',
        designation: '',
        department: '',
        phoneNumber: '',
        rights: 'STAFF'
    });
    const { signup } = useAuth();
    const navigate = useNavigate();
    const [error, setError] = useState('');

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const validatePhone = (phone) => {
        const phoneRegex = /^\d{10}$/;
        return phoneRegex.test(phone);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        if (!validatePhone(formData.phoneNumber)) {
            const msg = "Phone number must be exactly 10 digits";
            setError(msg);
            toast.error(msg, { position: "bottom-right" });
            return;
        }

        try {
            await signup(formData);
            toast.success("Account created successfully! Please login.", { position: "top-right" });
            navigate('/login');
        } catch (err) {
            console.error("Signup failed", err);
            let msg = "Failed to create account. Please try again.";

            if (err.response && err.response.data) {
                const data = err.response.data;
                // AuthController returns plain string for errors
                if (typeof data === 'string') {
                    if (data.includes("Email already exists")) {
                        msg = "This email address is already registered.";
                    } else if (data.includes("Phone number already exists")) {
                        msg = "This phone number is already registered.";
                    } else {
                        msg = data;
                    }
                } else if (data.message) {
                    // Fallback for JSON error responses
                    msg = data.message;
                }
            }

            setError(msg);
            toast.error(msg, { position: "bottom-right" });
        }
    };

    return (
        <div className="min-h-screen flex">
            {/* Left Side - Signup Form */}
            <div className="w-full md:w-1/2 flex items-center justify-center bg-white p-8 sm:p-12 lg:p-16 animate-slide-in-left">
                <div className="max-w-md w-full space-y-8">
                    <div className="text-center md:text-left">
                        <h2 className="text-3xl font-extrabold text-gray-900 tracking-tight">
                            Create your account
                        </h2>
                        <p className="mt-2 text-sm text-gray-600">
                            Get started with your free account.
                        </p>
                    </div>

                    <form className="mt-8 space-y-5" onSubmit={handleSubmit}>
                        <div className="space-y-4">
                            <div>
                                <label htmlFor="name" className="sr-only">Full Name</label>
                                <input
                                    name="name"
                                    type="text"
                                    required
                                    className="appearance-none block w-full px-4 py-3 border border-gray-300 rounded-lg placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition duration-200 ease-in-out shadow-sm hover:shadow-md focus:shadow-lg focus:shadow-indigo-500/20"
                                    placeholder="Full Name"
                                    value={formData.name}
                                    onChange={handleChange}
                                />
                            </div>
                            <div>
                                <label htmlFor="email" className="sr-only">Email address</label>
                                <input
                                    name="email"
                                    type="email"
                                    required
                                    className="appearance-none block w-full px-4 py-3 border border-gray-300 rounded-lg placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition duration-200 ease-in-out shadow-sm hover:shadow-md focus:shadow-lg focus:shadow-indigo-500/20"
                                    placeholder="Email address"
                                    value={formData.email}
                                    onChange={handleChange}
                                />
                            </div>
                            <div>
                                <label htmlFor="password" className="sr-only">Password</label>
                                <input
                                    name="password"
                                    type="password"
                                    required
                                    className="appearance-none block w-full px-4 py-3 border border-gray-300 rounded-lg placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition duration-200 ease-in-out shadow-sm hover:shadow-md focus:shadow-lg focus:shadow-indigo-500/20"
                                    placeholder="Password"
                                    value={formData.password}
                                    onChange={handleChange}
                                />
                            </div>
                            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
                                <div>
                                    <label htmlFor="designation" className="sr-only">Designation</label>
                                    <input
                                        name="designation"
                                        type="text"
                                        required
                                        className="appearance-none block w-full px-4 py-3 border border-gray-300 rounded-lg placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition duration-200 ease-in-out shadow-sm hover:shadow-md focus:shadow-lg focus:shadow-indigo-500/20"
                                        placeholder="Designation"
                                        value={formData.designation}
                                        onChange={handleChange}
                                    />
                                </div>
                                <div>
                                    <label htmlFor="department" className="sr-only">Department</label>
                                    <input
                                        name="department"
                                        type="text"
                                        required
                                        className="appearance-none block w-full px-4 py-3 border border-gray-300 rounded-lg placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition duration-200 ease-in-out shadow-sm hover:shadow-md focus:shadow-lg focus:shadow-indigo-500/20"
                                        placeholder="Department"
                                        value={formData.department}
                                        onChange={handleChange}
                                    />
                                </div>
                            </div>
                            <div>
                                <label htmlFor="phoneNumber" className="sr-only">Phone Number</label>
                                <input
                                    name="phoneNumber"
                                    type="tel"
                                    required
                                    className="appearance-none block w-full px-4 py-3 border border-gray-300 rounded-lg placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition duration-200 ease-in-out shadow-sm hover:shadow-md focus:shadow-lg focus:shadow-indigo-500/20"
                                    placeholder="Phone Number (10 digits)"
                                    value={formData.phoneNumber}
                                    onChange={handleChange}
                                />
                            </div>
                            <div>
                                <label htmlFor="rights" className="sr-only">Role</label>
                                <select
                                    name="rights"
                                    className="appearance-none block w-full px-4 py-3 border border-gray-300 rounded-lg placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition duration-200 ease-in-out shadow-sm hover:shadow-md focus:shadow-lg focus:shadow-indigo-500/20 bg-white"
                                    value={formData.rights}
                                    onChange={handleChange}
                                >
                                    <option value="STAFF">Staff</option>
                                    <option value="ADMIN">Admin</option>
                                </select>
                            </div>
                        </div>

                        {error && (
                            <div className="rounded-md bg-red-50 p-4 border border-red-100 animate-pulse">
                                <div className="flex">
                                    <div className="flex-shrink-0">
                                        <svg className="h-5 w-5 text-red-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                                            <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clipRule="evenodd" />
                                        </svg>
                                    </div>
                                    <div className="ml-3">
                                        <h3 className="text-sm font-medium text-red-800">{error}</h3>
                                    </div>
                                </div>
                            </div>
                        )}

                        <div>
                            <button
                                type="submit"
                                className="group relative w-full flex justify-center py-3 px-4 border border-transparent text-sm font-bold rounded-lg text-white bg-orange-600 hover:bg-orange-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-orange-500 transition-all duration-200 shadow-lg hover:shadow-orange-500/40 hover:-translate-y-0.5 active:translate-y-0"
                            >
                                Sign up
                            </button>
                        </div>

                        <div className="text-center mt-4">
                            <p className="text-sm text-gray-600">
                                Already have an account?{' '}
                                <Link to="/login" className="font-medium text-indigo-600 hover:text-indigo-500 transition-colors duration-200 hover:underline">
                                    Sign in
                                </Link>
                            </p>
                        </div>
                    </form>
                </div>
            </div>

            {/* Right Side - Branding */}
            <div className="hidden md:flex md:w-1/2 bg-gradient-to-br from-indigo-600 to-blue-500 justify-center items-center text-white p-12 relative overflow-hidden animate-slide-in-right">
                {/* Decorative circles */}
                <div className="absolute top-0 left-0 w-64 h-64 bg-white opacity-10 rounded-full -translate-x-1/2 -translate-y-1/2"></div>
                <div className="absolute bottom-0 right-0 w-96 h-96 bg-orange-500 opacity-20 rounded-full translate-x-1/3 translate-y-1/3 blur-3xl"></div>

                <div className="max-w-md text-center relative z-10">
                    <div className="mb-8 flex justify-center">
                        <div className="bg-white/20 p-4 rounded-2xl backdrop-blur-sm shadow-xl">
                            <Package className="h-16 w-16 text-white" />
                        </div>
                    </div>
                    <h1 className="text-4xl font-bold mb-6 tracking-tight">Join Us Today!</h1>
                    <p className="text-lg text-indigo-100 leading-relaxed">
                        Create your account and start managing your inventory with ease. Experience the power of organized business.
                    </p>
                </div>
            </div>
        </div>
    );
};

export default Signup;
