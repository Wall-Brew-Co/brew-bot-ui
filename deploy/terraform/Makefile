plan/prod:
	@ terraform plan -var app_name=brew-bot-server -var app_version=v0.5.4 -out=current.tfplan

apply/prod:
	@ terraform apply "current.tfplan"
