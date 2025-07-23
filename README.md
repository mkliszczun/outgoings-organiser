# Outgoings Organiser

![CI](https://github.com/mkliszczun/outgoings-organiser/actions/workflows/ci.yml/badge.svg)

A lightweight Spring Boot micro‑service for planning and tracking monthly expenses.
The project has been modernised to **Java 17** and **Spring Boot 2.7 LTS**, ships with a ready‑to‑run Docker image, and exposes an **OpenAPI 3** interface for hassle‑free integration.

---

## Table of Contents
1. [Key Features](#key-features)
2. [Tech Stack](#tech-stack)
3. [Prerequisites](#prerequisites)
4. [Getting Started](#getting-started)
5. [API Documentation](#api-documentation)
6. [Quality Gate](#quality-gate)
7. [Docker Usage](#docker-usage)
8. [Running Tests](#running-tests)
9. [Contributing](#contributing)
10. [License](#license)

---

## Key Features

- **Expense CRUD** – create, update, delete and list recurring or one‑off outgoings
- **In‑memory H2 database** by default, **MySQL** ready via `application.yml`
- **Spring Security** with in‑memory user store (can be swapped for JDBC/JWT)
- **OpenAPI 3** spec + Swagger UI generated automatically (springdoc-openapi)
- **Code quality gate** – Checkstyle, SpotBugs, Spotless (Google Java Format)
- **GitHub Actions CI** – build, test, static analysis and Docker image build

---

## Tech Stack

| Layer             | Technology                            | Version |
|-------------------|---------------------------------------|---------|
| Language / JDK    | OpenJDK Temurin                       | **17**  |
| Framework         | Spring Boot                           | **2.7.18 LTS** |
| Persistence       | Spring Data JPA, Hibernate            | 5.6.x   |
| Database (dev)    | H2 (embedded)                         | 2.2.x   |
| Database (prod)   | MySQL (optional)                      | 8.x     |
| API Docs          | `springdoc-openapi-ui`                | 1.7.0   |
| Build Tool        | Maven                                 | 3.9.x   |
| Container         | Docker (Temurin 17 JRE Alpine image)  | latest  |
| CI / CD           | GitHub Actions                        | yaml    |

---

## Prerequisites

- **Java 17 SDK**
- **Maven 3.9+**
- (Optional) **Docker** if you plan to use the container image

---

## Getting Started

```bash
# 1 · Clone the repository
git clone https://github.com/<your‑github‑username>/<your‑repo‑name>.git
cd <your‑repo‑name>

# 2 · Build the project and run all checks
mvn clean verify

# 3 · Run the application
mvn spring-boot:run
