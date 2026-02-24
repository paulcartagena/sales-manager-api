# Sales Manager API

REST API for invoice and sales management built with Spring Boot.

## Features

- Create sales with multiple items
- Automatic tax calculation (13%)
- Stock validation
- Invoice retrieval by ID
- List all invoices
- Transactional integrity

## Technologies

- Java 17+
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven

## Principal Endpoints

### Get all invoices
GET /api/invoices

### Get invoice by ID
GET /api/invoices/{id}

### Create sale
POST /api/invoices/sales

## ⚙️ Setup

Clone the repository:

```bash
git clone https://github.com/your-username/sales-manager-api.git
cd sales-manager-api
```

Configure your database in `application.properties` or environment variables. 

Run the project:

```bash
mvn clean install
mvn spring-boot:run
```

## 📦 Example JSON for creating a sale

```json
{
  "customerId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

## Architecture

- Controller layer
- Service layer
- Repository layer
- DTO pattern
- Transaction management
- Stock consistency validation

---

