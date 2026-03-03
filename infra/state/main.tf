# Terraform State Storage Infrastructure
#
# This configuration creates the Azure Storage Account for storing Terraform remote state.
# Run this once per team/organization to set up centralized state management.
#
# Usage:
#   terraform init
#   terraform apply
#
# After creation, configure backend.tf in the parent directory (infra/) to use this storage.

terraform {
  required_version = ">= 1.5.0"

  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 3.80"
    }
  }
}

provider "azurerm" {
  features {}
}

# Resource Group for Terraform state storage
resource "azurerm_resource_group" "tfstate" {
  name     = "tfstate-rg"
  location = "East US"

  tags = {
    purpose     = "terraform-state"
    environment = "shared"
    project     = "three-rivers-insurance"
  }
}

# Storage Account for Terraform state files
resource "azurerm_storage_account" "tfstate" {
  name                     = "tfstatethreerivers"  # Must be globally unique; update if needed
  resource_group_name      = azurerm_resource_group.tfstate.name
  location                 = azurerm_resource_group.tfstate.location
  account_tier             = "Standard"
  account_replication_type = "GRS"  # Geo-redundant for high availability

  # Security best practices
  min_tls_version                 = "TLS1_2"
  enable_https_traffic_only       = true
  allow_nested_items_to_be_public = false

  tags = {
    purpose     = "terraform-state"
    environment = "shared"
    project     = "three-rivers-insurance"
  }
}

# Blob container for state files
resource "azurerm_storage_container" "tfstate" {
  name                  = "tfstate"
  storage_account_name  = azurerm_storage_account.tfstate.name
  container_access_type = "private"
}

# Outputs for backend configuration
output "resource_group_name" {
  description = "Resource group name for backend configuration"
  value       = azurerm_resource_group.tfstate.name
}

output "storage_account_name" {
  description = "Storage account name for backend configuration"
  value       = azurerm_storage_account.tfstate.name
}

output "container_name" {
  description = "Container name for backend configuration"
  value       = azurerm_storage_container.tfstate.name
}

output "backend_config" {
  description = "Complete backend configuration block (copy to backend.tf)"
  value       = <<-EOT
    terraform {
      backend "azurerm" {
        resource_group_name  = "${azurerm_resource_group.tfstate.name}"
        storage_account_name = "${azurerm_storage_account.tfstate.name}"
        container_name       = "${azurerm_storage_container.tfstate.name}"
        key                  = "insurance-quote.tfstate"
      }
    }
  EOT
}
