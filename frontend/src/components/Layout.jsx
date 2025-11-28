import React from 'react';
import { Outlet, Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Package, LayoutDashboard, ShoppingBag, Users, LogOut, ShoppingCart } from 'lucide-react';

const Layout = ({ children }) => {
    const { user, logout } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    const isActive = (path) => {
        return location.pathname === path ? 'bg-indigo-50 text-indigo-600' : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900';
    };

    return (
        <div className="min-h-screen bg-gray-50 flex">
            {/* Sidebar */}
            <div className="w-64 bg-white shadow-lg fixed h-full z-10">
                <div className="flex items-center px-4 py-4 border-b border-gray-200">
                    <Package className="w-10 h-10 text-indigo-600 mr-3 flex-shrink-0" />
                    <div className="flex flex-col">
                        <span className="text-lg font-bold text-gray-800 leading-tight">Inventory Management</span>
                        <span className="text-lg font-bold text-gray-800 leading-tight">System</span>
                    </div>
                </div>

                <nav className="mt-6 px-4 space-y-2">
                    <Link
                        to="/dashboard"
                        className={`flex items-center px-4 py-3 text-sm font-medium rounded-md transition-colors duration-150 ${isActive('/dashboard')}`}
                    >
                        <LayoutDashboard className="w-5 h-5 mr-3" />
                        Dashboard
                    </Link>

                    <Link
                        to="/products"
                        className={`flex items-center px-4 py-3 text-sm font-medium rounded-md transition-colors duration-150 ${isActive('/products')}`}
                    >
                        <ShoppingBag className="w-5 h-5 mr-3" />
                        Products
                    </Link>

                    {user?.role === 'ADMIN' && (
                        <Link
                            to="/staff"
                            className={`flex items-center px-4 py-3 text-sm font-medium rounded-md transition-colors duration-150 ${isActive('/staff')}`}
                        >
                            <Users className="w-5 h-5 mr-3" />
                            Staff
                        </Link>
                    )}

                    {user?.role !== 'ADMIN' && (
                        <Link
                            to="/shop"
                            className={`flex items-center px-4 py-3 text-sm font-medium rounded-md transition-colors duration-150 ${isActive('/shop')}`}
                        >
                            <ShoppingCart className="w-5 h-5 mr-3" />
                            Shop (Test)
                        </Link>
                    )}

                    <button
                        onClick={handleLogout}
                        className="w-full flex items-center px-4 py-3 text-sm font-medium text-red-600 rounded-md hover:bg-red-50 transition-colors duration-150 mt-8"
                    >
                        <LogOut className="w-5 h-5 mr-3" />
                        Logout
                    </button>
                </nav>

                <div className="absolute bottom-0 w-full p-4 border-t border-gray-200 bg-gray-50">
                    <div className="flex items-center">
                        <div className="flex-shrink-0">
                            <div className="h-8 w-8 rounded-full bg-indigo-100 flex items-center justify-center text-indigo-600 font-bold">
                                {user?.name?.charAt(0) || 'U'}
                            </div>
                        </div>
                        <div className="ml-3">
                            <p className="text-sm font-medium text-gray-700 truncate w-40">{user?.name}</p>
                            <p className="text-xs text-gray-500 truncate w-40">{user?.role}</p>
                        </div>
                    </div>
                </div>
            </div>

            {/* Main Content */}
            <div className="flex-1 ml-64 p-8">
                {children}
            </div>
        </div>
    );
};

export default Layout;
