# McManus — History

## Project Context
- **Project:** Three Rivers Insurance — Insurance Quote Website
- **Frontend:** ReactJS
- **Backend:** Java
- **Infrastructure:** Azure with Terraform
- **CI/CD:** GitHub Actions
- **User:** Jorge Balderas

## Learnings

### CI Workflow Patterns (2025-07-15)
- **Path-based triggers**: Use `paths: ['frontend/**']` / `paths: ['backend/**']` to scope workflows to their directory, avoiding unnecessary CI runs.
- **Frontend caching**: `actions/setup-node@v4` has built-in `cache: 'npm'` support — point `cache-dependency-path` at the lockfile in the subdirectory.
- **Backend caching**: `actions/setup-java@v4` has built-in `cache: 'maven'` support — no manual `.m2` caching needed.
- **Maven flags**: Use `--batch-mode --no-transfer-progress` in CI to suppress interactive prompts and noisy download logs.
- **Test step**: Frontend package.json has no `test` script yet, so the frontend CI omits a test step. Add it when tests are introduced.
- **Timeouts**: 10 min for frontend (Node), 15 min for backend (Maven + checkstyle + spotbugs) are safe defaults.
- **Working directory**: Use `defaults.run.working-directory` at job level to avoid repeating `cd` in every step.

### Deployment Workflows (2026-03-03)
- **Deploy Frontend**: Uses `Azure/static-web-apps-deploy@v1` to deploy React build to Azure Static Web App. Requires `AZURE_STATIC_WEB_APPS_API_TOKEN` secret.
- **Deploy Backend**: Uses `azure/webapps-deploy@v3` to deploy Spring Boot JAR to Azure App Service. Requires `AZURE_CREDENTIALS` and `AZURE_APP_NAME` secrets.
- **Workflow triggers**: Deploy on push to main with path filters (`frontend/**` or `backend/**`) + manual `workflow_dispatch`.
- **Azure login**: Use `azure/login@v2` with service principal credentials for authentication, always logout in cleanup step.
- **JAR packaging**: Build with `mvn clean package -DskipTests` to skip tests in deploy workflow (tests already run in CI).

### Terraform Remote State (2026-03-03)
- **State storage**: Created `infra/state/main.tf` to provision Azure Storage for Terraform remote state (Resource Group + Storage Account + Blob Container).
- **Backend config**: Created `infra/backend.tf.example` template showing azurerm backend configuration. Copy to `backend.tf` and exclude from git.
- **State protection**: Enabled blob versioning and 30-day soft delete retention on state storage account to prevent accidental loss.
- **One-time setup**: Run `terraform apply` in `infra/state/` first, then configure backend in main `infra/` directory and run `terraform init -reconfigure`.
