# Ash7nly - Delivery Management Application

A **multi-module** Spring Boot application for delivery management with:
- **Spring Cloud Gateway** as single entry point
- **Spring Boot Backend** monolith with JWT authentication
- **React Frontend** (placeholder - to be added)
- **PostgreSQL** database

## Architecture

```
                    ┌─────────────────────────────────────────────────────┐
                    │                    DOCKER COMPOSE                   │
                    │                                                     │
   HTTP :80         │  ┌─────────────┐      ┌─────────────┐              │
────────────────────┼─►│   Gateway   │─────►│  Frontend   │              │
                    │  │  (Port 80)  │      │   (Nginx)   │              │
                    │  └─────────────┘      └─────────────┘              │
                    │         │                                          │
                    │         │ /api/**                                  │
                    │         ▼                                          │
                    │  ┌─────────────┐      ┌─────────────┐              │
                    │  │   Backend   │─────►│  PostgreSQL │              │
                    │  │ (Port 8080) │      │ (Port 5432) │              │
                    │  └─────────────┘      └─────────────┘              │
                    │                                                     │
                    └─────────────────────────────────────────────────────┘
```

## Project Structure

```
ash7nly/
├── pom.xml                    # Parent POM (multi-module)
├── docker-compose.yml         # Docker orchestration
├── backend/                   # Spring Boot monolith
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/com/ash7nly/monolith/
│       ├── MonolithApplication.java
│       ├── config/            # Security & App configuration
│       ├── controller/        # REST API controllers
│       ├── dto/               # Data Transfer Objects
│       ├── entity/            # JPA entities
│       ├── enums/             # Enumerations
│       ├── exception/         # Exception handling
│       ├── mapper/            # Entity-DTO mappers
│       ├── repository/        # JPA repositories
│       ├── security/          # JWT & auth services
│       └── service/           # Business logic
├── gateway/                   # Spring Cloud Gateway
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/com/ash7nly/gateway/
│       └── GatewayApplication.java
└── frontend/                  # React application (placeholder)
    ├── pom.xml
    ├── Dockerfile
    ├── nginx.conf
    └── README.md
```

## Quick Start

### Using Docker Compose (Recommended)

```bash
# Build and start all services
docker-compose up --build

# Access the application
# - Frontend: http://localhost
# - API:      http://localhost/api/health
# - Backend:  http://localhost/api/**
```

### Development Mode (Individual Services)

#### 1. Start Backend
```powershell
cd backend
$env:SPRING_PROFILES_ACTIVE="dev"
.\mvnw.cmd spring-boot:run
```

#### 2. Start Gateway (Optional for local dev)
```powershell
cd gateway
$env:BACKEND_URL="http://localhost:8080"
$env:FRONTEND_URL="http://localhost:3000"
$env:SERVER_PORT="9000"
.\mvnw.cmd spring-boot:run
```

### Build All Modules
```bash
# From root directory
.\mvnw.cmd clean package -DskipTests
```

## Services

| Service   | Internal Port | External Port | Description                    |
|-----------|---------------|---------------|--------------------------------|
| Gateway   | 80            | 80            | Single entry point, routing    |
| Backend   | 8080          | -             | REST APIs, business logic      |
| Frontend  | 80            | -             | React SPA (via Nginx)          |
| PostgreSQL| 5432          | 5432          | Database                       |

## Gateway Routes

| Path        | Destination                | Description              |
|-------------|----------------------------|--------------------------|
| `/api/**`   | `http://backend:8080`      | Backend REST APIs        |
| `/actuator/**` | `http://backend:8080`   | Health checks, metrics   |
| `/**`       | `http://frontend:80`       | React frontend           |

## API Endpoints

### Authentication (Public)
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and get JWT token

### Users (Authenticated)
- `GET /api/users/profile` - Get current user profile
- `PUT /api/users/profile` - Update current user profile
- `GET /api/users/{id}` - Get user by ID (Admin only)

### Shipments
- `POST /api/shipments` - Create shipment (Merchant only)
- `GET /api/shipments/tracking/{trackingNumber}` - Track shipment (Public)
- `POST /api/shipments/cancel` - Cancel shipment
- `GET /api/shipments/my-shipments` - Get merchant's shipments

### Drivers
- `GET /api/drivers` - Get all drivers (Admin only)
- `GET /api/drivers/{id}` - Get driver by ID
- `POST /api/drivers/users/{userId}` - Create driver for user (Admin only)
- `PUT /api/drivers/{id}` - Update driver

### Deliveries
- `GET /api/deliveries/assigned?driverId={id}` - Get assigned deliveries
- `POST /api/deliveries/{id}/accept` - Accept delivery
- `PUT /api/deliveries/{id}/status?status={status}` - Update delivery status
- `POST /api/deliveries/assign?shipmentId={id}&driverId={id}` - Assign delivery (Admin only)

### Health Check
- `GET /api/health` - Application health status

## User Roles

| Role     | Description                              |
|----------|------------------------------------------|
| ADMIN    | Full system access                       |
| MERCHANT | Can create and manage shipments          |
| DRIVER   | Can view and update assigned deliveries  |
| CUSTOMER | Can track shipments                      |

## Authentication

All protected endpoints require a JWT token:
```
Authorization: Bearer <your-jwt-token>
```

## Example Requests

### Register
```json
POST /api/auth/register
{
    "email": "merchant@example.com",
    "password": "password123",
    "fullName": "Test Merchant",
    "role": "MERCHANT"
}
```

### Create Shipment
```json
POST /api/shipments
Authorization: Bearer <token>
{
    "pickupAddress": "123 Main St",
    "deliveryAddress": "456 Oak Ave",
    "customerName": "John Doe",
    "customerPhone": "+1234567890",
    "packageWeight": "2kg",
    "cost": 25.00
}
```

## Configuration

### Environment Variables

| Variable       | Default                    | Description              |
|----------------|----------------------------|--------------------------|
| `DB_HOST`      | `localhost`                | Database host            |
| `DB_PORT`      | `5432`                     | Database port            |
| `DB_NAME`      | `ash7nly_db`               | Database name            |
| `DB_USERNAME`  | `admin`                    | Database username        |
| `DB_PASSWORD`  | `password`                 | Database password        |
| `JWT_SECRET`   | (default key)              | JWT signing key          |
| `JWT_EXPIRATION` | `86400000`               | JWT expiration (ms)      |

### Database Access (Development)

**Option 1: H2 Web Console** (dev profile)
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:ash7nly_db`
- Username: `sa`
- Password: `password`

**Option 2: PostgreSQL** (Docker)
- Host: `localhost`
- Port: `5432`
- Database: `ash7nly_db`
- Username: `admin`
- Password: `password`

## IntelliJ Run Configurations

The project includes pre-configured run configurations:
- **Backend Application** - Run backend with `dev` profile
- **Gateway Application** - Run gateway pointing to local backend
- **Docker Compose - Full Stack** - Run all services with Docker

## Docker Commands

```bash
# Build and start all services
docker-compose up --build

# Start in background
docker-compose up -d --build

# View logs
docker-compose logs -f backend
docker-compose logs -f gateway

# Stop all services
docker-compose down

# Clean up volumes
docker-compose down -v
```

## Adding Frontend

1. Navigate to `frontend/` folder
2. Create React app:
   ```bash
   npx create-react-app . --template typescript
   ```
3. Update `frontend/Dockerfile` for production build
4. Rebuild with Docker Compose

See `frontend/README.md` for detailed instructions.

