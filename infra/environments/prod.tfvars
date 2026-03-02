# prod.tfvars — Production environment configuration
# Right-sized SKUs for production workloads with high availability

resource_group_name = "three-rivers-insurance-rg"
location            = "eastus"
environment         = "prod"

# Premium v3 App Service Plan for production performance
app_service_sku = "P1v3"

# General Purpose PostgreSQL tier for production reliability
postgresql_sku        = "GP_Standard_D2s_v3"
postgresql_storage_mb = 65536
postgresql_version    = "15"

# NOTE: db_admin_username and db_admin_password should be provided via
# environment variables (TF_VAR_db_admin_username, TF_VAR_db_admin_password)
# or a secrets manager — never commit credentials to source control.
