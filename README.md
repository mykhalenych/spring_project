# Spring Boot Portfolio Project

This repository contains a Spring Boot application built with Java 17, Maven, MySQL, Liquibase, and JPA/Hibernate. The project is fully containerized using Docker and Docker Compose.

---

## Technical Stack

*   **Language:** Java 17
*   **Framework:** Spring Boot 3.1.5
*   **Database:** MySQL 8.0.33
*   **Database Migrations:** Liquibase
*   **Build Tool:** Maven
*   **Containerization:** Docker & Docker Compose
*   **API Documentation:** Springdoc OpenAPI / Swagger UI

---

## Getting Started

### Prerequisites

Make sure you have the following installed on your machine:
*   [Docker](https://www.docker.com/get-started) (including Docker Compose)
*   [Git](https://git-scm.com/)

### 1. Configure Environment Variables

The application reads database configuration and ports from a `.env` file. A template `.env.template` is provided in the repository.

1.  Duplicate the template file and rename it to `.env`:
    ```bash
    cp .env.template .env
    ```
2.  Open `.env` and fill in the values:
    *   `MYSQLDB_USER`: The username for the MySQL database (e.g., `root`).
    *   `MYSQLDB_ROOT_PASSWORD`: The root password for the MySQL database.
    *   `MYSQLDB_DATABASE`: The name of the database to create (e.g., `online_book_store`).
    *   `MYSQLDB_LOCAL_PORT`: The port on your local machine to map to MySQL (e.g., `3307`).
    *   `MYSQLDB_DOCKER_PORT`: The internal MySQL docker port (default is `3306`).
    *   `SPRING_LOCAL_PORT`: The port on your local machine to map to the Spring application (e.g., `8066`).
    *   `SPRING_DOCKER_PORT`: The internal Spring application docker port (default is `8066`).

> [!IMPORTANT]
> The `.env` file contains sensitive information and is excluded from Git tracking via `.gitignore`. Never commit `.env` directly to a repository.

### 2. Run the Application

You can spin up both the database and the application in containerized mode using Docker Compose:

```bash
docker compose up --build
```

This command will:
1.  Pull the MySQL image and initialize a database with your `.env` credentials.
2.  Perform a multi-stage Docker build for the Spring Boot application (including static analysis checkstyle checks).
3.  Start the application, applying database migrations via Liquibase once MySQL is healthy and ready.

To stop the services, run:
```bash
docker compose down
```

---

## API Documentation

Once the containers are running, you can access the Swagger UI documentation to interact with the API endpoints:

*   **API Documentation URL:** `http://localhost:8066/api/swagger-ui/index.html`
*   **API Base Path:** `http://localhost:8066/api`
