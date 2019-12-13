# Pick a unique app name
export APP_NAME=my-terraform-backend

# Nuke the existing Stack
# NOTE: THIS DROPS ALL EXISTING RESOURCES
terraform destroy

## Plan the new stack / TF
terraform plan -var app_name=$APP_NAME -out=current.tfplan

## Execute that plan remotely
terraform apply "current.tfplan"
