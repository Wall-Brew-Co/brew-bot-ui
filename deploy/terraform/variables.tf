# Terraform variables
variable "app_name" {
  type = string
  description = "Name of the Heroku application"
  default = "brew-bot-server"
}

variable "app_version" {
  type = string
  description = "Current, tagged version of the application"
  default = "v0.8.1"
}
