# Spring Boot Inventory Management System

A complete Spring Boot application for managing product inventory and staff with automated low-stock alerts via email and console notifications.

## Features

✅ **Entity Management**
- Staff management with roles (ADMIN, STAFF)
- InventoryStock management with automatic price calculations
- Automatic timestamp tracking (createdDate, updatedDate)

✅ **Alerts & Notifications**
- Automatic alerts when product quantity < threshold (default: 2)
- Email notifications via JavaMailSender (optional SMTP configuration)
- Console notifications for development/debugging
- Scheduled scanner running every minute to check low-stock items

✅ **REST APIs**
- Full CRUD operations for products (/api/products)
- Full CRUD operations for staff (/api/staff)
- PATCH endpoint for quick quantity updates

✅ **Validation & Error Handling**
- Input validation using javax.validation
- Custom exceptions with meaningful error responses
- Global exception handler returning consistent JSON error format

✅ **Data Persistence**
- MySQL database integration using Spring Data JPA
- BigDecimal for monetary values
- LocalDateTime for audit timestamps
- Automatic totalPrice calculation (pricePerUnit × quantity)

✅ **Testing**
- Repository tests with @DataJpaTest
- Service integration tests with @SpringBootTest
- Controller tests with @WebMvcTest / MockMvc
- Unit tests for alert logic with Mockito

## Technology Stack

- **Java 21**
- **Spring Boot 3.5.7**
- **Spring Data JPA**
- **MySQL**
- **Maven**
- **Lombok**
- **JUnit 5**
- **Mockito**
- **Spring Mail**

## Project Structure

```
src/main/java/com/myapp/demo/
├── config/                    # Configuration classes
├── controller/                # REST controllers
├── dto/                       # Data Transfer Objects
├── entity/                    # JPA entities
├── exception/                 # Custom exceptions & global handler
├── notifier/                  # Email and Console notifiers
├── repository/                # Spring Data JPA repositories
├── service/                   # Business logic services
└── DemoApplication.java       # Main application class

src/main/resources/
└── application.properties     # Configuration properties

src/test/java/com/myapp/demo/
├── repository/                # Repository tests
├── service/                   # Service tests
└── controller/                # Controller tests
```

## Prerequisites

- Java 21 or higher
- Maven 3.6+
- MySQL 8.0+

## Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd demo
```

### 2. Setup MySQL Database

```sql
CREATE DATABASE mydb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Configure Database Connection

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=root
spring.datasource.password=admin
```

### 4. (Optional) Configure Email Alerts

Edit `src/main/resources/application.properties` to enable email notifications:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
```

**Note:** For Gmail, use an [App Password](https://myaccount.google.com/apppasswords) instead of your regular password.

### 5. Build the Project

```bash
mvn clean package
```

### 6. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Running Tests

Run all tests:
```bash
mvn test
```

Run specific test class:
```bash
mvn test -Dtest=InventoryStockRepositoryTest
```

Run tests with coverage:
```bash
mvn test jacoco:report
```

## API Documentation

### Product Management (/api/products)

#### Create Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "productName": "Laptop",
    "pricePerUnit": 999.99,
    "quantity": 5,
    "status": "ACTIVE"
  }'
```

Response:
```json
{
  "productId": 1,
  "productName": "Laptop",
  "pricePerUnit": 999.99,
  "quantity": 5,
  "totalPrice": 4999.95,
  "createdDate": "2025-11-11T10:30:00",
  "updatedDate": "2025-11-11T10:30:00",
  "status": "ACTIVE"
}
```

#### Get All Products
```bash
curl -X GET http://localhost:8080/api/products
```

#### Get Product by ID
```bash
curl -X GET http://localhost:8080/api/products/1
```

#### Update Product (Full Update)
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "productName": "Laptop Pro",
    "pricePerUnit": 1299.99,
    "quantity": 3,
    "status": "ACTIVE"
  }'
```

#### Update Product Quantity (Partial Update)
```bash
curl -X PATCH http://localhost:8080/api/products/1/quantity \
  -H "Content-Type: application/json" \
  -d '{
    "quantity": 1
  }'
```

This will trigger an alert since quantity (1) < threshold (2).

#### Delete Product
```bash
curl -X DELETE http://localhost:8080/api/products/1
```

### Staff Management (/api/staff)

#### Create Staff
```bash
curl -X POST http://localhost:8080/api/staff \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "designation": "Manager",
    "department": "Inventory",
    "rights": "ADMIN",
    "phoneNumber": "1234567890",
    "status": "ACTIVE"
  }'
```

#### Get All Staff
```bash
curl -X GET http://localhost:8080/api/staff
```

#### Get Staff by ID
```bash
curl -X GET http://localhost:8080/api/staff/1
```

#### Update Staff
```bash
curl -X PUT http://localhost:8080/api/staff/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Smith",
    "email": "john.smith@example.com",
    "designation": "Senior Manager",
    "department": "Inventory",
    "rights": "ADMIN",
    "phoneNumber": "0987654321",
    "status": "ACTIVE"
  }'
```

#### Delete Staff
```bash
curl -X DELETE http://localhost:8080/api/staff/1
```

## Error Handling

All errors return a consistent JSON format:

### 404 - Not Found
```json
{
  "timestamp": "2025-11-11T10:35:20.123456",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found with ID: 999",
  "path": "/api/products/999"
}
```

### 400 - Bad Request (Validation Error)
```json
{
  "timestamp": "2025-11-11T10:36:15.789012",
  "status": 400,
  "error": "Validation Failed",
  "message": "pricePerUnit: Price must be greater than 0; ",
  "path": "/api/products"
}
```

### 400 - Bad Request (Invalid Request)
```json
{
  "timestamp": "2025-11-11T10:37:00.456789",
  "status": 400,
  "error": "Bad Request",
  "message": "Email already exists: john@example.com",
  "path": "/api/staff"
}
```

## Alert System

### How Alerts Work

1. **Immediate Alerts**: When a product is created or updated, the system checks if quantity < threshold
2. **Scheduled Alerts**: Every minute, the `ScheduledInventoryChecker` scans all products below threshold
3. **Multi-Channel Notifications**: Alerts are sent through:
   - **Console Notifier**: Logs warning messages (always active)
   - **Email Notifier**: Sends email alerts (if SMTP is configured)

### Console Alert Example
```
=== LOW STOCK ALERT ===
Product: Laptop (ID: 1)
Current Quantity: 1
Price Per Unit: 999.99
Total Price: 999.99
========================
```

### Email Alert Example
```
INVENTORY ALERT

Product: Laptop
Product ID: 1
Current Quantity: 1
Price Per Unit: 999.99
Total Price: 999.99

Please review and restock if necessary.
```

## Entity Details

### Staff Entity
| Field | Type | Description |
|-------|------|-------------|
| id | Long | Primary key (auto-generated) |
| name | String | Staff member name |
| email | String | Unique email address |
| designation | String | Job title |
| department | String | Department name |
| rights | Enum | ADMIN or STAFF |
| phoneNumber | String | Contact number |
| createdDate | LocalDateTime | Auto-set on creation |
| updatedDate | LocalDateTime | Auto-updated on modification |
| status | Enum | ACTIVE or INACTIVE |

### InventoryStock Entity
| Field | Type | Description |
|-------|------|-------------|
| productId | Long | Primary key (auto-generated) |
| productName | String | Product name (required) |
| pricePerUnit | BigDecimal | Unit price (required, > 0) |
| quantity | Integer | Stock quantity (≥ 0) |
| totalPrice | BigDecimal | Auto-calculated (pricePerUnit × quantity) |
| createdDate | LocalDateTime | Auto-set on creation |
| updatedDate | LocalDateTime | Auto-updated on modification |
| status | Enum | ACTIVE or INACTIVE |

## Configuration Properties

| Property | Default | Description |
|----------|---------|-------------|
| inventory.alert.threshold | 2 | Stock level threshold for alerts |
| spring.jpa.hibernate.ddl-auto | update | Database schema auto-update |
| spring.mail.host | smtp.example.com | SMTP server hostname |
| spring.mail.port | 587 | SMTP server port |
| spring.mail.username | your@email.com | Email account username |
| spring.mail.password | yourpassword | Email account password |

## Development Notes

### Automatic Price Calculation
The `totalPrice` field is automatically calculated using JPA lifecycle hooks:

```java
@PrePersist
@PreUpdate
protected void calculateTotalPrice() {
    if (this.pricePerUnit != null && this.quantity != null) {
        this.totalPrice = this.pricePerUnit.multiply(new BigDecimal(this.quantity));
    }
}
```

### Service Layer Pattern
All business logic is delegated to service layers:
- **InventoryStockService**: Handles product CRUD and validation
- **StaffService**: Handles staff CRUD and validation
- **AlertService**: Manages alert notifications
- Controllers only delegate to services

### Validation Strategy
- DTO-level validation using `@Valid` and Jakarta validation annotations
- Service-level business logic validation
- Custom exception throwing for specific error cases

## Troubleshooting

### Database Connection Issues
```
ERROR: Cannot get a connection, pool error Timeout waiting for idle object
```
**Solution:** Ensure MySQL is running and connection properties are correct in `application.properties`.

### Email Not Sending
**Solution:** 
1. Verify SMTP credentials in `application.properties`
2. Check firewall/network access to SMTP server
3. Check application logs for detailed error messages
4. For Gmail, ensure 2-step verification is enabled and app password is used

### Tests Failing
**Solution:**
- Ensure MySQL is running (tests use `@SpringBootTest`)
- Check database connectivity
- Run `mvn clean test` to clear any cached issues

## License

This project is provided as-is for educational purposes.

## Support

For issues or questions, please review the application logs and ensure all prerequisites are met.
