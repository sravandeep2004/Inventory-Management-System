import React, { useEffect, useState } from 'react';
import api from '../api/axiosConfig';
import { toast } from 'react-toastify';
import { Plus, Minus, ShoppingCart, Package, X } from 'lucide-react';

const Shop = () => {
    const [products, setProducts] = useState([]);
    const [selectedProduct, setSelectedProduct] = useState(null);
    const [buyQuantity, setBuyQuantity] = useState(1);
    const [showModal, setShowModal] = useState(false);

    useEffect(() => {
        fetchProducts();
    }, []);

    const fetchProducts = async () => {
        try {
            const response = await api.get('/products');
            const activeProducts = response.data.filter(p => p.status === 'ACTIVE' && p.quantity > 0);
            setProducts(activeProducts);
        } catch (error) {
            console.error("Error fetching products", error);
            toast.error("Failed to load products");
        }
    };

    const openBuyModal = (product) => {
        setSelectedProduct(product);
        setBuyQuantity(1);
        setShowModal(true);
    };

    const closeBuyModal = () => {
        setShowModal(false);
        setSelectedProduct(null);
        setBuyQuantity(1);
    };

    const handleIncrement = () => {
        if (!selectedProduct) return;
        if (buyQuantity < selectedProduct.quantity) {
            setBuyQuantity(prev => prev + 1);
        } else {
            toast.warn(`Only ${selectedProduct.quantity} items in stock`);
        }
    };

    const handleDecrement = () => {
        if (buyQuantity > 1) {
            setBuyQuantity(prev => prev - 1);
        }
    };

    const handlePurchase = async () => {
        if (!selectedProduct) return;

        try {
            await api.patch(`/products/${selectedProduct.productId}/quantity`, {
                quantity: selectedProduct.quantity - buyQuantity
            });
            toast.success("Purchase successful!", { position: "top-right" });

            // Refresh products
            fetchProducts();
            closeBuyModal();
        } catch (error) {
            console.error("Error buying product", error);
            const errorMsg = error.response?.data?.message || error.message || "Unknown error";
            toast.error(`Purchase failed: ${errorMsg}`, { position: "bottom-right" });
        }
    };

    return (
        <div className="space-y-6">
            <div className="flex justify-between items-center">
                <div>
                    <h1 className="text-3xl font-bold text-gray-900">Shop</h1>
                    <p className="text-gray-500 mt-1">Select products to purchase.</p>
                </div>
            </div>

            <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
                {products.map((product) => (
                    <div key={product.productId} className="bg-white rounded-2xl shadow-sm hover:shadow-md transition-all duration-300 border border-gray-100 overflow-hidden group flex flex-col">
                        <div className="p-6 flex-1">
                            {/* Header: Logo + Name */}
                            <div className="flex items-start justify-between mb-4">
                                <div className="flex items-center space-x-3">
                                    <div className="bg-indigo-50 p-2.5 rounded-xl group-hover:bg-indigo-100 transition-colors">
                                        <Package className="w-6 h-6 text-indigo-600" />
                                    </div>
                                    <h3 className="text-lg font-bold text-gray-900 leading-tight">{product.productName}</h3>
                                </div>
                            </div>

                            {/* Price */}
                            <div className="mb-2">
                                <span className="text-2xl font-bold text-gray-900">₹{product.pricePerUnit}</span>
                            </div>

                            {/* Stock */}
                            <div className="mb-6">
                                <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${product.quantity < 5 ? 'bg-red-100 text-red-800' : 'bg-green-100 text-green-800'
                                    }`}>
                                    {product.quantity} in stock
                                </span>
                            </div>
                        </div>

                        {/* Buy Button - Full Width at Bottom */}
                        <div className="p-4 bg-gray-50 border-t border-gray-100">
                            <button
                                onClick={() => openBuyModal(product)}
                                className="w-full bg-indigo-600 text-white px-4 py-2 rounded-lg text-sm font-bold uppercase tracking-wide hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-colors flex items-center justify-center shadow-sm"
                            >
                                <ShoppingCart className="w-4 h-4 mr-2" />
                                Buy Now
                            </button>
                        </div>
                    </div>
                ))}
            </div>

            {/* Buy Modal */}
            {showModal && selectedProduct && (
                <div className="fixed inset-0 bg-gray-900 bg-opacity-50 backdrop-blur-sm overflow-y-auto h-full w-full z-50 flex items-center justify-center p-4">
                    <div className="relative bg-white rounded-2xl shadow-2xl max-w-md w-full overflow-hidden animate-slide-in-up">
                        {/* Modal Header */}
                        <div className="bg-indigo-600 px-6 py-4 flex justify-between items-center">
                            <h3 className="text-xl font-bold text-white flex items-center">
                                <ShoppingCart className="w-5 h-5 mr-2" />
                                Confirm Purchase
                            </h3>
                            <button
                                onClick={closeBuyModal}
                                className="text-indigo-200 hover:text-white transition-colors"
                            >
                                <X className="w-6 h-6" />
                            </button>
                        </div>

                        <div className="p-6 space-y-6">
                            {/* Product Details */}
                            <div className="flex items-center space-x-4">
                                <div className="bg-indigo-50 p-4 rounded-xl">
                                    <Package className="w-10 h-10 text-indigo-600" />
                                </div>
                                <div>
                                    <h4 className="text-lg font-bold text-gray-900">{selectedProduct.productName}</h4>
                                    <p className="text-sm text-gray-500">Price per unit: <span className="font-semibold text-gray-900">₹{selectedProduct.pricePerUnit}</span></p>
                                    <p className={`text-xs font-medium mt-1 ${selectedProduct.quantity < 5 ? 'text-red-600' : 'text-green-600'}`}>
                                        {selectedProduct.quantity} items in stock
                                    </p>
                                </div>
                            </div>

                            {/* Quantity Controls */}
                            <div className="bg-gray-50 p-4 rounded-xl border border-gray-100">
                                <label className="block text-sm font-medium text-gray-700 mb-3">Select Quantity</label>
                                <div className="flex items-center justify-between">
                                    <div className="flex items-center space-x-3">
                                        <button
                                            onClick={handleDecrement}
                                            disabled={buyQuantity <= 1}
                                            className="p-2 rounded-lg bg-white border border-gray-200 text-gray-600 hover:bg-gray-100 hover:border-gray-300 disabled:opacity-50 disabled:cursor-not-allowed transition-all"
                                        >
                                            <Minus className="w-4 h-4" />
                                        </button>
                                        <span className="text-xl font-bold text-gray-900 w-12 text-center">{buyQuantity}</span>
                                        <button
                                            onClick={handleIncrement}
                                            disabled={buyQuantity >= selectedProduct.quantity}
                                            className="p-2 rounded-lg bg-white border border-gray-200 text-gray-600 hover:bg-gray-100 hover:border-gray-300 disabled:opacity-50 disabled:cursor-not-allowed transition-all"
                                        >
                                            <Plus className="w-4 h-4" />
                                        </button>
                                    </div>
                                    <div className="text-right">
                                        <p className="text-xs text-gray-500 uppercase tracking-wide font-semibold">Total Price</p>
                                        <p className="text-2xl font-bold text-indigo-600">
                                            ₹{(selectedProduct.pricePerUnit * buyQuantity).toFixed(2)}
                                        </p>
                                    </div>
                                </div>
                            </div>

                            {/* Action Buttons */}
                            <div className="flex space-x-3 pt-2">
                                <button
                                    onClick={closeBuyModal}
                                    className="flex-1 px-4 py-3 border border-gray-300 text-gray-700 font-medium rounded-xl hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-colors"
                                >
                                    Cancel
                                </button>
                                <button
                                    onClick={handlePurchase}
                                    className="flex-1 px-4 py-3 bg-indigo-600 text-white font-bold rounded-xl hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 shadow-lg shadow-indigo-200 transition-all hover:-translate-y-0.5"
                                >
                                    Proceed to Buy
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Shop;
