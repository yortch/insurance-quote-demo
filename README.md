# Three Rivers Insurance Quote Website

A full-stack web application for generating insurance quotes, built with React and Spring Boot, deployed on Azure.

## Tech Stack

| Layer          | Technology                              |
| -------------- | --------------------------------------- |
| Frontend       | React 19, Vite 7, ESLint, Prettier     |
| Backend        | Spring Boot 3.4.1, Java 17, Maven      |
| Database       | PostgreSQL (H2 for tests)              |
| Infrastructure | Terraform, Azure App Service            |
| CI/CD          | GitHub Actions                          |

## Prerequisites

- **Node.js** 20+ and npm
- **Java** 17+ (JDK)
- **Maven** 3.9+
- **PostgreSQL** 15+ (for backend local development)
- **Terraform** 1.5+ (for infrastructure changes only)

## Project Structure

```
├── frontend/          # React + Vite application
├── backend/           # Spring Boot REST API
├── infra/             # Terraform infrastructure (Azure)
└── .github/workflows/ # CI pipelines
```

## Local Development

### Frontend

```bash
cd frontend
npm install
npm run dev
```

The dev server starts at **http://localhost:5173** with hot reload.

Available scripts:

| Script           | Description                     |
| ---------------- | ------------------------------- |
| `npm run dev`    | Start dev server                |
| `npm run build`  | Production build to `dist/`     |
| `npm run lint`   | Run ESLint                      |
| `npm run lint:fix`| Auto-fix lint issues           |
| `npm run format` | Format code with Prettier       |
| `npm run preview`| Preview production build        |

### Backend

1. **Set up PostgreSQL** — create a database named `insurance_quote`:

   ```bash
   createdb insurance_quote
   ```

2. **Set environment variables:**

   ```bash
   export DB_USERNAME=your_db_user
   export DB_PASSWORD=your_db_password
   # Optional: override DB URL (default: jdbc:postgresql://localhost:5432/insurance_quote)
   # export DB_URL=jdbc:postgresql://localhost:5432/insurance_quote
   ```

3. **Run the application:**

   ```bash
   cd backend
   mvn spring-boot:run
   ```

The API starts at **http://localhost:8080**. Health check: `GET /api/health`

Available Maven commands:

| Command              | Description                                    |
| -------------------- | ---------------------------------------------- |
| `mvn spring-boot:run`| Start the application                          |
| `mvn clean verify`   | Build, test, checkstyle, and SpotBugs analysis |
| `mvn test`           | Run tests only (uses H2 in-memory database)    |

### Infrastructure

```bash
cd infra
terraform init
terraform plan -var-file=environments/dev.tfvars
```

See `infra/variables.tf` for available configuration options.

## CI/CD

GitHub Actions workflows run automatically on pull requests:

- **Frontend CI** — triggered by changes in `frontend/`: lint and build
- **Backend CI** — triggered by changes in `backend/`: compile, test, checkstyle, SpotBugs

## Deployment

The application uses GitHub Actions for continuous deployment to Azure:

- **Frontend** — Deployed to Azure Static Web App on push to `main`
- **Backend** — Deployed to Azure App Service on push to `main`

### Required GitHub Secrets

Configure these secrets in your repository settings (Settings → Secrets and variables → Actions):

| Secret                              | Description                                                                 |
| ----------------------------------- | --------------------------------------------------------------------------- |
| `AZURE_CREDENTIALS`                 | Azure service principal JSON for authentication (format: `{"clientId": "...", "clientSecret": "...", "subscriptionId": "...", "tenantId": "..."}`) |
| `AZURE_STATIC_WEB_APPS_API_TOKEN`   | Deployment token for Azure Static Web App (found in Azure Portal)          |
| `AZURE_APP_NAME`                    | Name of the Azure App Service instance (e.g., `threerivers-insurance-api`) |
| `TF_VAR_db_admin_username`          | PostgreSQL database admin username (for Terraform provisioning)            |
| `TF_VAR_db_admin_password`          | PostgreSQL database admin password (for Terraform provisioning)            |

### Manual Deployment

Deployments can be triggered manually via the GitHub Actions UI using the `workflow_dispatch` event on the respective workflow.

## Implementation Plan

See [Issue #1](https://github.com/yortch/insurance-quote-demo/issues/1) for the full implementation plan.

## License

This project is for demonstration purposes.
