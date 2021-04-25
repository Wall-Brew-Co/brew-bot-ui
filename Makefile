
MAKE = make
LEIN = lein

# These are the locations of the directories we'll use
TARGET_DIR = target
VERCHG = bin/verchg
TERRAFORM_DIR = deploy/terraform
VER = `sed -n -e "s/^.*defproject .* \"\(.*\)\"/\1/p" project.clj;`

#
# These are the main targets that we'll be making
#

tests/server:
	$(info Running server-side tests...)
	@ HEROKU_ENV=test $(LEIN) test

tests/app:
	$(info Running client-side tests...)
	@ HEROKU_ENV=test $(LEIN) doo once

tests/all:
	$(info Running server-side and client-side tests...)
	@ HEROKU_ENV=test $(LEIN) test-build

artifacts:
	$(info Packaging production-like uberjar and client application...)
	@ HEROKU_ENV=prod $(LEIN) prod-build

run/dev:
	$(info Running the development environment...)
	@ NODE_ENV=development npm run css
	@ HEROKU_ENV=dev $(LEIN) dev-build

run/selenium:
	$(info Running the development environment for automated testing...)
	@ NODE_ENV=development npm run css
	@ HEROKU_ENV=test $(LEIN) selenium-build

version/major:
	$(info Updating major version and adding CHANGELOG entry...)
	@ $(VERCHG) 'major'
	@ NODE_ENV=production npm run css

version/minor:
	$(info Updating minor version and adding CHANGELOG entry...)
	@ $(VERCHG) 'minor'
	@ NODE_ENV=production npm run css

version/bugfix:
	$(info Updating bugfix version and adding CHANGELOG entry...)
	@ $(VERCHG) 'bugfix'
	@ NODE_ENV=production npm run css

version:
	$(info Adding CHANGELOG entry for existing version...)
	@ $(VERCHG) 'push'
	@ NODE_ENV=production npm run css

plan/prod:
	$(info Creating Terraform Plan...)
	@ $(MAKE) -C $(TERRAFORM_DIR) plan/prod

deploy/prod:
	$(info Deploying brew-bot-ui to Production...)
	@ $(MAKE) -C $(TERRAFORM_DIR) apply/prod

release/css:
	@ NODE_ENV=production npm run css
