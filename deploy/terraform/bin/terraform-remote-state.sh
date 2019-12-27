# Target the remote state tracker
export APP_NAME=brew-bot-backend

# Create the database to track terraform state
heroku create $APP_NAME
heroku addons:create heroku-postgresql:hobby-dev --app $APP_NAME

# On each machine where it's used, initialize Terraform
# with the database credentials
export DATABASE_URL=`heroku config:get DATABASE_URL --app $APP_NAME`
terraform init -backend-config="conn_str=$DATABASE_URL"
