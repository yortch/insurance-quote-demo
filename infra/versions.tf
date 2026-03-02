# versions.tf — Required provider versions and Terraform settings
# Pinning provider versions ensures reproducible infrastructure builds.

terraform {
  required_version = ">= 1.5.0"

  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 3.80"
    }
  }

  # Remote state placeholder — configure Azure Storage backend for team use
  # backend "azurerm" {
  #   resource_group_name  = "tfstate-rg"
  #   storage_account_name = "tfstatethreerivers"
  #   container_name       = "tfstate"
  #   key                  = "terraform.tfstate"
  # }
}
