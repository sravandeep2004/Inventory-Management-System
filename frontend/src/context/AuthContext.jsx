import React, { createContext, useState, useContext, useEffect } from 'react';
import api from '../api/axiosConfig';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const token = localStorage.getItem('token');
        const role = localStorage.getItem('role');
        const name = localStorage.getItem('name');
        const email = localStorage.getItem('email');

        if (token && role && name) {
            setUser({ token, role, name, email });
        }
        setLoading(false);
    }, []);

    const login = async (email, password) => {
        try {
            const response = await api.post('/auth/login', { email, password });
            const { token, role, name, email: userEmail } = response.data;

            localStorage.setItem('token', token);
            localStorage.setItem('role', role);
            localStorage.setItem('name', name);
            localStorage.setItem('email', userEmail);

            setUser({ token, role, name, email: userEmail });
            return true;
        } catch (error) {
            console.error("Login failed", error);
            throw error;
        }
    };

    const logout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        localStorage.removeItem('name');
        localStorage.removeItem('email');
        setUser(null);
    };

    const signup = async (userData) => {
        try {
            // For now, we use the same endpoint or a new one. 
            // The backend AuthController has a placeholder /signup.
            // But usually we might want to use /api/staff if we are admin, or public signup.
            // Let's assume public signup for this demo at /api/auth/signup
            await api.post('/auth/signup', userData);
            return true;
        } catch (error) {
            console.error("Signup failed", error);
            throw error;
        }
    };

    return (
        <AuthContext.Provider value={{ user, login, logout, signup, loading }}>
            {!loading && children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
