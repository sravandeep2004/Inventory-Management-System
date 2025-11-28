import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Package } from 'lucide-react';
import { toast } from 'react-toastify';

const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            await login(email, password);
            toast.success("Login successful!", { position: "top-right" });
            navigate('/dashboard');
        } catch (err) {
            const msg = 'Failed to login. Please check your credentials.';
            setError(msg);
            toast.error(msg, { position: "bottom-right" });
        }
    };

    return (
        <div className="min-h-screen flex">
            {/* Left Side - Branding */}
            <div className="hidden md:flex md:w-1/2 bg-gradient-to-br from-indigo-600 to-blue-500 justify-center items-center text-white p-12 relative overflow-hidden animate-slide-in-left">
                {/* Decorative circles */}
                <div className="absolute top-0 left-0 w-64 h-64 bg-white opacity-10 rounded-full -translate-x-1/2 -translate-y-1/2"></div>
                <div className="absolute bottom-0 right-0 w-96 h-96 bg-orange-500 opacity-20 rounded-full translate-x-1/3 translate-y-1/3 blur-3xl"></div>

                <div className="max-w-md text-center relative z-10">
                    <div className="mb-8 flex justify-center">
                        <div className="bg-white/20 p-4 rounded-2xl backdrop-blur-sm shadow-xl">
                            <Package className="h-16 w-16 text-white" />
                        </div>
                    </div>
                    <h1 className="text-4xl font-bold mb-6 tracking-tight">Welcome Back!</h1>
                    <p className="text-lg text-indigo-100 leading-relaxed">
                        Streamline your inventory management with our powerful and intuitive system. Access your dashboard to manage products and staff efficiently.
                    </p>
                </div>
            </div>

            {/* Right Side - Login Form */}
            <div className="w-full md:w-1/2 flex items-center justify-center bg-white p-8 sm:p-12 lg:p-16 animate-slide-in-right">
                <div className="max-w-md w-full space-y-8">
                    <div className="text-center md:text-left">
                        <h2 className="text-3xl font-extrabold text-gray-900 tracking-tight">
                            Sign in to your account
                        </h2>
                        <p className="mt-2 text-sm text-gray-600">
                            Please enter your details to continue.
                        </p>
                    </div>

                    <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
                        <input type="hidden" name="remember" value="true" />
                        <div className="space-y-5">
                            <div>
                                <label htmlFor="email-address" className="block text-sm font-medium text-gray-700 mb-1">Email address</label>
                                <input
                                    id="email-address"
                                    name="email"
                                    type="email"
                                    autoComplete="email"
                                    required
                                    className="appearance-none block w-full px-4 py-3 border border-gray-300 rounded-lg placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition duration-200 ease-in-out shadow-sm hover:shadow-md focus:shadow-lg focus:shadow-indigo-500/20"
                                    placeholder="Enter your email"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                />
                            </div>
                            <div>
                                <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-1">Password</label>
                                <input
                                    id="password"
                                    name="password"
                                    type="password"
                                    autoComplete="current-password"
                                    required
                                    className="appearance-none block w-full px-4 py-3 border border-gray-300 rounded-lg placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition duration-200 ease-in-out shadow-sm hover:shadow-md focus:shadow-lg focus:shadow-indigo-500/20"
                                    placeholder="Enter your password"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                />
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
                                Sign in
                            </button>
                        </div>

                        <div className="text-center mt-4">
                            <p className="text-sm text-gray-600">
                                Don't have an account?{' '}
                                <Link to="/signup" className="font-medium text-indigo-600 hover:text-indigo-500 transition-colors duration-200 hover:underline">
                                    Sign up now
                                </Link>
                            </p>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default Login;
