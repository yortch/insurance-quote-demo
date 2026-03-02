# variables.tf — Input variables for Three Rivers Insurance infrastructure

variable "resource_group_name" {
  description = "Name of the Azure Resource Group"
  type        = string
  default     = "three-rivers-insurance-rg"
}

variable "location" {
  description = "Azure region for all resources"
  type        = string
  default     = "eastus"
}

variable "environment" {
  description = "Deployment environment (dev, staging, prod)"
  type        = string
  default     = "dev"

  validation {
    condition     = contains(["dev", "staging", "prod"], var.environment)
    error_message = "Environment must be one of: dev, staging, prod."
  }
}

variable "db_admin_username" {
  description = "Administrator username for PostgreSQL Flexible Server"
  type        = string
  sensitive   = true
}

variable "db_admin_password" {
  description = "Administrator password for PostgreSQL Flexible Server"
  type        = string
  sensitive   = true

  validation {
    condition     = length(var.db_admin_password) >= 8
    error_message = "Database password must be at least 8 characters."
  }
}

variable "app_service_sku" {
  description = "SKU name for the App Service Plan (e.g., B1, P1v3)"
  type        = string
  default     = "B1"
}

variable "postgresql_sku" {
  description = "SKU name for PostgreSQL Flexible Server (e.g., B_Standard_B1ms, GP_Standard_D2s_v3)"
  type        = string
  default     = "B_Standard_B1ms"
}

variable "postgresql_storage_mb" {
  description = "Storage size in MB for PostgreSQL Flexible Server"
  type        = number
  default     = 32768
}

variable "postgresql_version" {
  description = "PostgreSQL major version"
  type        = string
  default     = "15"
}
