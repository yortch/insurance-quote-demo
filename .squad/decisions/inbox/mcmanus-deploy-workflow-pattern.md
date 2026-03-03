# Deployment Workflow Pattern

**Author:** McManus (⚙️ DevOps)  
**Date:** 2026-03-03  
**Scope:** CI/CD, Infrastructure

## Decision

- **Separate deploy workflows**: Created dedicated `deploy-frontend.yml` and `deploy-backend.yml` workflows that trigger on push to main with path filters.
- **Azure Static Web App**: Frontend deploys using `Azure/static-web-apps-deploy@v1` action with API token authentication.
- **Azure App Service**: Backend deploys using `azure/webapps-deploy@v3` action with service principal authentication via `azure/login@v2`.
- **Skip tests in deploy**: Deploy workflows use `-DskipTests` flag since tests already run in separate CI workflows.
- **Remote state storage**: Use Azure Storage backend for Terraform state with blob versioning and 30-day soft delete enabled.
- **Backend config**: Keep `backend.tf` out of git using `backend.tf.example` template pattern.

## Rationale

- **Path-based triggers**: Avoid deploying both services when only one changed, reducing deployment time and Azure costs.
- **Workflow separation**: Keep CI (validation) and CD (deployment) concerns separate for clearer workflow purpose and easier debugging.
- **Test efficiency**: Tests run in PR CI workflow, no need to re-run on deploy since main branch is protected.
- **State protection**: Blob versioning + soft delete prevent accidental state file corruption or deletion, critical for production infrastructure.
- **Template pattern**: `backend.tf.example` allows per-environment configuration without exposing Azure storage account names in git.

## Required Secrets

Teams adopting this pattern must configure:
- `AZURE_CREDENTIALS` — Service principal JSON with contributor access
- `AZURE_STATIC_WEB_APPS_API_TOKEN` — From Static Web App deployment settings
- `AZURE_APP_NAME` — Target App Service name
- `TF_VAR_db_admin_username` — PostgreSQL admin username
- `TF_VAR_db_admin_password` — PostgreSQL admin password
