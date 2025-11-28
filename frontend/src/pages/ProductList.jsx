import React, { useEffect, useState } from 'react';
import api from '../api/axiosConfig';
import { toast } from 'react-toastify';

const ProductList = () => {
    const [products, setProducts] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [formData, setFormData] = useState({
        productName: '',
        pricePerUnit: 0,
        quantity: 0,
        status: 'ACTIVE'
    });

    useEffect(() => {
        fetchProducts();
    }, []);

    const fetchProducts = async () => {
        try {
            const response = await api.get('/products');
            setProducts(response.data);
        } catch (error) {
            console.error("Error fetching products", error);
            toast.error("Failed to load products");
        }
    };

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (Number(formData.pricePerUnit) < 0 || Number(formData.quantity) < 0) {
            toast.error("negative values not allowed", { position: "bottom-right" });
            return;
        }

        try {
            await api.post('/products', formData);
            setShowModal(false);
            setFormData({
                productName: '',
                pricePerUnit: 0,
                quantity: 0,
                status: 'ACTIVE'
            });
            fetchProducts();
            toast.success("Product added successfully!", { position: "top-right" });
        } catch (error) {
            console.error("Error creating product", error);
            const errorMsg = error.response?.data?.message || error.message || "Unknown error";
            toast.error(`Failed to add product: ${errorMsg}`, { position: "bottom-right" });
        }
    };

    const handleDelete = async (id) => {
        // Removed window.confirm for debugging/UX
        try {
            await api.delete(`/products/${id}`);
            fetchProducts();
            toast.success("Product deleted.", { position: "top-right" });
        } catch (error) {
            console.error("Error deleting product", error);
            const errorMsg = error.response?.data?.message || error.message || "Unknown error";
            toast.error(`Failed to delete product: ${errorMsg}`);
        }
    };

    return (
        <div className="space-y-6">
            <div className="flex justify-between items-center">
                <h1 className="text-3xl font-bold text-gray-900">Product Management</h1>
                <button
                    onClick={() => setShowModal(true)}
                    className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700"
                >
                    Add Product
                </button>
            </div>

            {/* Modal */}
            {showModal && (
                <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
                    <div className="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
                        <div className="mt-3 text-center">
                            <h3 className="text-lg leading-6 font-medium text-gray-900">Add New Product</h3>
                            <form className="mt-2 text-left space-y-4" onSubmit={handleSubmit}>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Product Name</label>
                                    <input
                                        name="productName"
                                        type="text"
                                        placeholder="Enter product name"
                                        required
                                        className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-1 focus:ring-indigo-500"
                                        value={formData.productName}
                                        onChange={handleChange}
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Price Per Unit</label>
                                    <input
                                        name="pricePerUnit"
                                        type="number"
                                        min="0"
                                        placeholder="0"
                                        required
                                        className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-1 focus:ring-indigo-500"
                                        value={formData.pricePerUnit}
                                        onChange={handleChange}
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Quantity</label>
                                    <input
                                        name="quantity"
                                        type="number"
                                        min="0"
                                        placeholder="0"
                                        required
                                        className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-1 focus:ring-indigo-500"
                                        value={formData.quantity}
                                        onChange={handleChange}
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Status</label>
                                    <select
                                        name="status"
                                        className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-1 focus:ring-indigo-500"
                                        value={formData.status}
                                        onChange={handleChange}
                                    >
                                        <option value="ACTIVE">Active</option>
                                        <option value="INACTIVE">Inactive</option>
                                    </select>
                                </div>
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
                    {products.map((product) => (
                        <li key={product.productId}>
                            <div className="px-4 py-4 flex items-center justify-between sm:px-6">
                                <div className="flex items-center">
                                    <div className="text-sm font-medium text-indigo-600 truncate">
                                        {product.productName}
                                    </div>
                                    <div className="ml-2 flex-shrink-0 flex">
                                        <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${product.status === 'ACTIVE' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                                            }`}>
                                            {product.status}
                                        </span>
                                    </div>
                                </div>
                                <div className="flex items-center space-x-4">
                                    <div className="text-sm text-gray-500">
                                        Qty: {product.quantity} | ₹{product.pricePerUnit}
                                    </div>
                                    <button
                                        onClick={() => handleDelete(product.productId)}
                                        className="text-red-600 hover:text-red-900 text-sm font-medium"
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

export default ProductList;
