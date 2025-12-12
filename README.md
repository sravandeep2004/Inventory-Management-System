# Inventory Management System

A complete full-stack application for managing product inventory and staff with automated low-stock alerts.

## Tech Stack

### Backend
- **Java 21**
- **Spring Boot 3.5.7**
- **Spring Data JPA** (MySQL)
- **Spring Security** (JWT Authentication)
- **Spring Mail** (Email Notifications)
- **Spring Dotenv** (Environment Configuration)
- **Maven**

### Frontend
- **React 19**
- **Vite**
- **TailwindCSS**

## Prerequisites

- **Java 21** or higher
- **Node.js** (v18+ recommended)
- **MySQL 8.0+**
- **Maven 3.6+**

## Setup & Installation

### 1. Environment Setup

Create a `.env` file in the root directory (same level as `pom.xml`) with the following configuration:

```env
DB_URL=jdbc:mysql://localhost:3306/inventory_db
DB_USERNAME=root
DB_PASSWORD=your_password
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password
```

> **Note**: For Gmail, use an [App Password](https://support.google.com/accounts/answer/185833) if 2-Step Verification is enabled.

### 2. Backend Setup

```bash
# Clone the repository
git clone <repository-url>
cd demo

# Run the application
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`.

### 3. Frontend Setup

Open a new terminal and navigate to the frontend directory:

```bash
cd frontend

# Install dependencies
npm install

# Start the development server
npm run dev
```

The frontend will start on `http://localhost:5173` (or the port shown in the terminal).

## API Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| **POST** | `/api/auth/login` | User login & JWT generation | No |
| **GET** | `/api/products` | Get all products | Yes |
| **POST** | `/api/products` | Create a new product | Yes (Admin) |
| **PUT** | `/api/products/{id}` | Update a product | Yes (Admin) |
| **DELETE** | `/api/products/{id}` | Delete a product | Yes (Admin) |
| **GET** | `/api/staff` | Get all staff members | Yes (Admin) |
| **POST** | `/api/staff` | Add a new staff member | Yes (Admin) |

## Features

- **Role-Based Access Control**: Admin and Staff roles.
- **Inventory Tracking**: Real-time updates of stock levels.
- **Low Stock Alerts**: Automated email and console notifications when stock is low.
- **Dashboard**: Visual overview of inventory status.

## Troubleshooting

- **Database Connection Failed**: Ensure MySQL is running and credentials in `.env` are correct.
- **Email Not Sending**: Verify `MAIL_USERNAME` and `MAIL_PASSWORD` in `.env`. Check if your email provider blocks less secure apps.
- **CORS Issues**: Ensure the backend allows requests from the frontend URL (default `http://localhost:5173`).
