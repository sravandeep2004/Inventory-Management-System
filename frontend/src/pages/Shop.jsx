import React, { useEffect, useState } from 'react';
import api from '../api/axiosConfig';
import { toast } from 'react-toastify';
import { Plus, Minus, ShoppingCart, Package } from 'lucide-react';

const Shop = () => {
    const [products, setProducts] = useState([]);
    const [quantities, setQuantities] = useState({});

    useEffect(() => {
        fetchProducts();
    }, []);

    const fetchProducts = async () => {
        try {
            const response = await api.get('/products');
            const activeProducts = response.data.filter(p => p.status === 'ACTIVE' && p.quantity > 0);
            setProducts(activeProducts);

            // Initialize quantities
            const initialQuantities = {};
            activeProducts.forEach(p => {
                initialQuantities[p.productId] = 0;
            });
            setQuantities(initialQuantities);
        } catch (error) {
            console.error("Error fetching products", error);
            toast.error("Failed to load products");
        }
    };

    const handleQuantityChange = (productId, change, maxStock) => {
        setQuantities(prev => {
            const currentQty = prev[productId] || 0;
            const newQty = currentQty + change;

            if (newQty < 0) return prev;
            if (newQty > maxStock) {
                toast.warn(`Cannot buy more than available stock (${maxStock})`);
                return prev;
            }

            return { ...prev, [productId]: newQty };
        });
    };

    const handleBuy = async (product) => {
        const buyQty = quantities[product.productId] || 0;

        if (buyQty === 0) {
            toast.warn("Please select a quantity to buy");
            return;
        }

        // Removed window.confirm for debugging/UX
        try {
            await api.patch(`/products/${product.productId}/quantity`, {
                quantity: product.quantity - buyQty
            });
            toast.success("Purchase successful!", { position: "top-right" });

            // Refresh products and reset quantity for this product
            fetchProducts();
            setQuantities(prev => ({ ...prev, [product.productId]: 0 }));
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
                    <p className="text-gray-500 mt-1">Select quantity and buy products.</p>
                </div>
            </div>

            <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
                {products.map((product) => (
                    <div key={product.productId} className="bg-white rounded-2xl shadow-sm hover:shadow-md transition-all duration-300 border border-gray-100 overflow-hidden group">
                        <div className="p-6">
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

                            {/* Controls Row */}
                            <div className="flex items-center gap-2 mt-4">
                                {/* Quantity Controls */}
                                <div className="flex items-center bg-gray-50 rounded-md p-0.5 border border-gray-200">
                                    <button
                                        onClick={() => handleQuantityChange(product.productId, -1, product.quantity)}
                                        className="p-1 rounded hover:bg-white hover:shadow-sm text-gray-600 transition-all disabled:opacity-50"
                                        disabled={(quantities[product.productId] || 0) <= 0}
                                    >
                                        <Minus className="w-3 h-3" />
                                    </button>
                                    <span className="w-6 text-center font-bold text-gray-900 text-xs">
                                        {quantities[product.productId] || 0}
                                    </span>
                                    <button
                                        onClick={() => handleQuantityChange(product.productId, 1, product.quantity)}
                                        className="p-1 rounded hover:bg-white hover:shadow-sm text-gray-600 transition-all disabled:opacity-50"
                                        disabled={(quantities[product.productId] || 0) >= product.quantity}
                                    >
                                        <Plus className="w-3 h-3" />
                                    </button>
                                </div>

                                {/* Buy Button */}
                                <button
                                    onClick={() => handleBuy(product)}
                                    disabled={(quantities[product.productId] || 0) === 0}
                                    className="flex-1 bg-indigo-600 text-white px-3 py-1.5 rounded-md text-xs font-bold uppercase tracking-wide hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed transition-colors flex items-center justify-center shadow-sm whitespace-nowrap"
                                >
                                    <ShoppingCart className="w-3 h-3 mr-1.5" />
                                    Buy Now
                                </button>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Shop;
