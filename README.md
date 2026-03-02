# Three Rivers Insurance Quote Website

An online insurance quote platform for Three Rivers Insurance, built with a React/Vite frontend, Spring Boot backend, and Terraform-managed Azure infrastructure.

> 📋 See [issue #1](https://github.com/yortch/insurance-quote-demo/issues/1) for the full implementation plan.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Frontend | React 19, Vite 7 |
| Backend | Java 17, Spring Boot 3, Maven |
| Database | PostgreSQL |
| Infrastructure | Terraform, Azure |
| CI/CD | GitHub Actions |

---

## Prerequisites

| Tool | Version |
|------|---------|
| Node.js | 20+ |
| Java | 17+ |
| Maven | 3.9+ |
| Terraform | 1.5+ |

---

## Local Development

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Runs at **http://localhost:5173**

### Backend

A local PostgreSQL instance is required. Create a database named `insurance_quote` (default URL: `jdbc:postgresql://localhost:5432/insurance_quote`), then set credentials and start the server:

```bash
export DB_USERNAME=<your-db-username>
export DB_PASSWORD=<your-db-password>

cd backend
mvn spring-boot:run
```

Runs at **http://localhost:8080**

> The `DB_URL` environment variable can be set to override the default database URL.

---

## Available Scripts

### Frontend (`frontend/`)

| Script | Description |
|--------|-------------|
| `npm run dev` | Start development server (port 5173) |
| `npm run build` | Build for production |
| `npm run preview` | Preview production build |
| `npm run lint` | Run ESLint |
| `npm run lint:fix` | Run ESLint with auto-fix |
| `npm run format` | Format source files with Prettier |

### Backend (`backend/`)

| Command | Description |
|---------|-------------|
| `mvn spring-boot:run` | Start development server (port 8080) |
| `mvn clean verify` | Compile, test, and package |
| `mvn checkstyle:check` | Run Checkstyle linter |
| `mvn spotbugs:check` | Run SpotBugs static analysis |

---

## Project Structure

```
insurance-quote-demo/
├── frontend/          # React + Vite application
│   ├── src/           # Source files (components, pages, assets)
│   ├── public/        # Static assets
│   └── package.json
├── backend/           # Spring Boot API
│   ├── src/
│   │   ├── main/java/ # Application source code
│   │   └── test/java/ # Unit and integration tests
│   └── pom.xml
└── infra/             # Terraform infrastructure (Azure)
    ├── environments/  # Per-environment variable files
    ├── main.tf
    ├── variables.tf
    └── outputs.tf
```
