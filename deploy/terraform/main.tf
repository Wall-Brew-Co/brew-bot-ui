provider "heroku" {
  version = "~> 2.0"
}

# Remote state repository for terraform
terraform {
  backend "pg" {
  }
}

variable "app_name" {
  description = "Name of the Heroku application"
}

resource "heroku_app" "server" {
  name = "${var.app_name}"
  region = "us"
}

resource "heroku_addon" "database" {
  app  = "${heroku_app.server.name}"
  plan = "heroku-postgresql:hobby-dev"
}

# Logging via papertrail
resource "heroku_addon" "logging" {
  app  = "${heroku_app.server.name}"
  plan = "papertrail:choklad"
}

# Build code & release to the app
resource "heroku_build" "server" {
  app = "${heroku_app.server.name}"
  buildpacks = ["https://github.com/heroku/heroku-buildpack-clojure.git"]

  source = {
    url = "https://github.com/nnichols/brew-bot-ui/archive/v0.0.0.tar.gz"
    version = "0.0.0"
  }
}

# Launch the app's web process by scaling-up
resource "heroku_formation" "server" {
  app        = "${heroku_app.server.name}"
  type       = "web"
  quantity   = 1
  size       = "free"
  depends_on = ["heroku_build.server"]
}

output "app_url" {
  value = "https://${heroku_app.server.name}.herokuapp.com"
}
