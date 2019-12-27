# Configure variables used below
export APP_NAME=brew-bot-server
export APP_VERSION=CHANGE_ME

# Nuke the existing Stack
# NOTE: THIS DROPS ALL EXISTING RESOURCES
terraform destroy

## Plan the new stack / TF
terraform plan -var app_name=$APP_NAME -var app_version=$APP_VERSION -out=current.tfplan

## Execute that plan remotely
terraform apply "current.tfplan"
