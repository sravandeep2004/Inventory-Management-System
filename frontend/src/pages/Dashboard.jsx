import React, { useEffect, useState } from 'react';
import api from '../api/axiosConfig';
import { useAuth } from '../context/AuthContext';
import { toast } from 'react-toastify';

const Dashboard = () => {
    const [stats, setStats] = useState({
        totalProducts: 0,
        lowStockProducts: 0,
        totalStaff: 0,
        recentProducts: []
    });
    const { user } = useAuth();

    useEffect(() => {
        const fetchStats = async () => {
            try {
                const response = await api.get('/dashboard/stats');
                if (response.data) {
                    setStats(response.data);
                }
            } catch (error) {
                console.error("Error fetching stats", error);
            }
        };

        fetchStats();
    }, []);

    const handleExport = async () => {
        try {
            const response = await api.get('/products');
            const products = response.data;

            if (!products || products.length === 0) {
                toast.info("No products to export.");
                return;
            }

            const headers = ["Product ID", "Product Name", "Price Per Unit", "Quantity", "Status", "Total Price"];
            const csvRows = [headers.join(",")];

            products.forEach(product => {
                const totalPrice = (product.pricePerUnit * product.quantity).toFixed(2);
                const row = [
                    product.productId,
                    `"${product.productName}"`, // Quote name to handle commas
                    product.pricePerUnit,
                    product.quantity,
                    product.status,
                    totalPrice
                ];
                csvRows.push(row.join(","));
            });

            const csvContent = csvRows.join("\n");
            const blob = new Blob([csvContent], { type: "text/csv" });
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement("a");
            a.href = url;
            a.download = "inventory_export.csv";
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            window.URL.revokeObjectURL(url);

            toast.success("Inventory exported successfully!");

        } catch (error) {
            console.error("Error exporting inventory", error);
            toast.error("Failed to export inventory.");
        }
    };

    return (
        <div className="space-y-6">
            <div className="flex justify-between items-center">
                <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
                <button
                    onClick={handleExport}
                    className="bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700 flex items-center shadow-sm"
                >
                    <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4" />
                    </svg>
                    <div className="flex flex-col items-start gap-1">
                        <span className="leading-none">Export Inventory</span>
                        <span className="text-[10px] opacity-75 font-mono leading-none">.csv</span>
                    </div>
                </button>
            </div>

            <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-3">
                <div className="bg-white overflow-hidden shadow rounded-lg">
                    <div className="px-4 py-5 sm:p-6">
                        <dt className="text-sm font-medium text-gray-500 truncate">Total Products</dt>
                        <dd className="mt-1 text-3xl font-semibold text-gray-900">{stats.totalProducts}</dd>
                    </div>
                </div>

                <div className="bg-white overflow-hidden shadow rounded-lg">
                    <div className="px-4 py-5 sm:p-6">
                        <dt className="text-sm font-medium text-gray-500 truncate">Low Stock Products</dt>
                        <dd className="mt-1 text-3xl font-semibold text-red-600">{stats.lowStockProducts}</dd>
                    </div>
                </div>

                {user?.role === 'ADMIN' && (
                    <div className="bg-white overflow-hidden shadow rounded-lg">
                        <div className="px-4 py-5 sm:p-6">
                            <dt className="text-sm font-medium text-gray-500 truncate">Total Staff</dt>
                            <dd className="mt-1 text-3xl font-semibold text-gray-900">{stats.totalStaff}</dd>
                        </div>
                    </div>
                )}
            </div>

            <div className="bg-white shadow overflow-hidden sm:rounded-lg">
                <div className="px-4 py-5 sm:px-6">
                    <h3 className="text-lg leading-6 font-medium text-gray-900">Recent Products</h3>
                </div>
                <div className="border-t border-gray-200">
                    <ul className="divide-y divide-gray-200">
                        {stats.recentProducts && stats.recentProducts.length > 0 ? (
                            stats.recentProducts.map((product) => (
                                <li key={product.productId} className="px-4 py-4 sm:px-6">
                                    <div className="flex items-center justify-between">
                                        <div className="text-sm font-medium text-indigo-600 truncate">
                                            {product.productName}
                                        </div>
                                        <div className="ml-2 flex-shrink-0 flex">
                                            <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${product.quantity < 2
                                                ? 'bg-red-100 text-red-800'
                                                : product.quantity < 10
                                                    ? 'bg-yellow-100 text-yellow-800'
                                                    : 'bg-green-100 text-green-800'
                                                }`}>
                                                Qty: {product.quantity}
                                            </span>
                                        </div>
                                    </div>
                                    <div className="mt-2 sm:flex sm:justify-between">
                                        <div className="sm:flex">
                                            <p className="flex items-center text-sm text-gray-500">
                                                Price: ₹{product.pricePerUnit}
                                            </p>
                                        </div>
                                    </div>
                                </li>
                            ))
                        ) : (
                            <li className="px-4 py-4 sm:px-6 text-gray-500 text-sm">No recent products found.</li>
                        )}
                    </ul>
                </div>
            </div>
        </div>
    );
};

export default Dashboard;
