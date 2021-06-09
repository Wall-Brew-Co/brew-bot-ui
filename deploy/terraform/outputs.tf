output "app_url" {
  value = "https://${heroku_app.server.name}.herokuapp.com"
}

output "build_log_stream" {
  value = heroku_build.server.output_stream_url
}

output "build_trigger_user" {
  value = heroku_build.server.user
}
