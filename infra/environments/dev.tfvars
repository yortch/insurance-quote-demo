# dev.tfvars — Development environment configuration
# Smallest SKUs and cost-effective settings for local/dev testing

resource_group_name = "three-rivers-insurance-rg"
location            = "eastus"
environment         = "dev"

# Smallest App Service SKU (Basic tier)
app_service_sku = "B1"

# Burstable PostgreSQL tier — cost-effective for dev
postgresql_sku        = "B_Standard_B1ms"
postgresql_storage_mb = 32768
postgresql_version    = "15"

# NOTE: db_admin_username and db_admin_password should be provided via
# environment variables (TF_VAR_db_admin_username, TF_VAR_db_admin_password)
# or a secrets manager — never commit credentials to source control.
