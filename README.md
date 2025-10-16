# 🛒 Appliance Store — Spring Boot Web App

This is a demo Java web application built with **Spring Boot**, simulating an appliance store. The project was developed as part of a learning assignment and follows a layered architecture (Controller → Service → Repository).

> ⚠️ Note: This version does not include unit or integration tests.

## 🚀 Features

- Role-based access for **Employees** and **Clients**
- Employees can:
  - View all data
  - Add, update, and delete:
    - Employees
    - Clients
    - Manufacturers
    - Appliances
  - Approve orders
- Clients can:
  - Create, update, and delete their orders
- Internationalization support (English + Ukrainian)
- Secure login with **Spring Security (In-Memory)**
- In-memory **H2 database** with initial data
- Form validation and basic error handling

## 📦 Tech Stack

- Java 17+
- Spring Boot
- Spring Security
- Spring Data JPA (Hibernate)
- H2 Database
- Thymeleaf
- Lombok


## 🗃️ Initial Data

Sample SQL data is loaded automatically on startup:
```properties
spring.sql.init.data-locations=classpath:manufacturer.sql, classpath:client.sql, classpath:employee.sql, classpath:appliance.sql