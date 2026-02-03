# Angular Bootstrap Spring

[![CI](https://github.com/Rob-Leggett/angular_bootstrap_spring/actions/workflows/ci.yml/badge.svg)](https://github.com/Rob-Leggett/angular_bootstrap_spring/actions/workflows/ci.yml)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-green.svg)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-19-red.svg)](https://angular.dev/)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-purple.svg)](https://getbootstrap.com/)

A modern single-page application built with **Angular 19** and **Bootstrap 5** for the frontend, backed by **Spring Boot 3.3** with **Spring Security 6** for the API.

## Tech Stack

### Backend (API)
- **Java 21** (LTS)
- **Spring Boot 3.3** with Spring Security 6
- **Stateless JWT-style token authentication**
- **HSQLDB** in-memory database (for demo purposes)
- **JPA/Hibernate** for persistence
- **JUnit 5** for testing

### Frontend (Client)
- **Angular 19** with standalone components
- **Bootstrap 5.3** for styling
- **TypeScript 5.7**
- **Angular CLI** for builds

## Architecture

The application is split into two modules:

- **API** - Spring Boot REST backend (stateless, token-based auth)
- **Client** - Angular SPA frontend

## Quick Start

### Prerequisites
- **Java 21** or higher
- **Maven 3.8+**
- **Node.js 22** (optional - Maven downloads it automatically)

### Build Everything

```bash
mvn clean package
```

This builds both the API and client modules.

### Run the API

```bash
cd api
mvn spring-boot:run
```

The API will be available at `http://localhost:8080/api`

### Run the Client (Development)

```bash
cd client
npm install
npm start
```

The client dev server runs at `http://localhost:4200` with API proxy to `http://localhost:8080`.

## Authentication

The API uses stateless token-based authentication:

1. Login with Basic Auth to `/api/auth/login`
2. Receive a token in the `X-AUTH-TOKEN` response header
3. Include the token in subsequent requests via `X-AUTH-TOKEN` header

### Default Credentials

| Username | Password |
|----------|----------|
| user@tester.com.au | password |

## API Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/login` | Login (Basic Auth) | No |
| GET | `/api/user` | Get current user | Yes |
| GET | `/api/customer` | List all customers | Yes |
| GET | `/api/customer/{id}` | Get customer by ID | Yes |
| POST | `/api/customer` | Create customer | Yes |
| PUT | `/api/customer/{id}` | Update customer | Yes |
| DELETE | `/api/customer/{id}` | Delete customer | Yes |

## Development

### Backend Development

```bash
cd api
mvn clean verify        # Run tests
mvn spring-boot:run     # Run with hot reload
```

### Frontend Development

```bash
cd client
npm install             # Install dependencies
npm start               # Dev server with hot reload
npm run build           # Production build
npm run test            # Run unit tests
```

### IDE Setup

**IntelliJ IDEA** is recommended. Import the project as a Maven project.

For the frontend, ensure the Angular Language Service plugin is installed.

## Project Structure

```
angular_bootstrap_spring/
├── api/                          # Spring Boot backend
│   ├── src/main/java/
│   │   └── au/com/example/
│   │       ├── Application.java  # Spring Boot entry point
│   │       ├── spring/           # Security & config
│   │       ├── controller/       # REST controllers
│   │       ├── service/          # Business logic
│   │       ├── repository/       # Data access
│   │       └── entity/           # JPA entities
│   └── src/main/resources/
│       ├── application.properties
│       └── data.sql              # Sample data
├── client/                       # Angular frontend
│   ├── src/app/
│   │   ├── components/           # Angular components
│   │   ├── services/             # HTTP services
│   │   ├── guards/               # Route guards
│   │   └── models/               # TypeScript interfaces
│   └── angular.json
└── pom.xml                       # Parent POM
```

## License

[MIT License](LICENSE)

## Author

**Robert Leggett**
- [Blog](https://robertleggett.com.au)

---

## Support

If you find this project helpful, consider supporting further development:

[![paypal](https://www.paypal.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=EV2ZLZBABFJ34&lc=AU&item_name=Research%20%26%20Development&currency_code=AUD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted)
