# outputs.tf — Exported values for CI/CD pipelines and other consumers

output "app_service_url" {
  description = "URL of the Spring Boot backend App Service"
  value       = "https://${azurerm_linux_web_app.backend.default_hostname}"
}

output "static_web_app_url" {
  description = "URL of the React frontend Static Web App"
  value       = "https://${azurerm_static_web_app.frontend.default_host_name}"
}

output "database_connection_string" {
  description = "JDBC connection string for PostgreSQL"
  value       = "jdbc:postgresql://${azurerm_postgresql_flexible_server.main.fqdn}:5432/insurance?sslmode=require"
  sensitive   = true
}

output "resource_group_name" {
  description = "Name of the deployed resource group"
  value       = azurerm_resource_group.main.name
}
