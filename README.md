# brew-bot-ui
The SPA/back-end for brew-bot

## Deployment

TODO: Heroku config/setup, terraform setup, connect to remote terraform state, add make targets

### Update the Version Number in `project.clj`

Each deployment requires a new version number.
The versioning scheme we use is [SemVer](http://semver.org/), and make targets have been added for your convenience.

* If **all** changes are bug fixes, increment the last value in the version with `make version/bugfix`
* If **any** change made **breaks** an external API, increment the first value in the version with `make version/major`
* If it's anything else, increment the _middle_ number in the version with `make version/minor`

The version number must change, and the release notes should be updated as well in `CHANGELOG.md`

### Tag the Git Repo with the New Version

Once you have updated the version and release notes, you need to check everything in and tag the git repo with the version:
```bash
$ git tag -a v0.1.0
```
and supply a concise, reasonable, sentence for this version tag.

You can then push the tag with:
```bash
$ git push --tags
```

### Plan the application stack with Terraform

The application, and all of its resources are managed by Terraform.
To deploy a new version of the app, we must first build the deployment plan.
In the example below, we'd be preparing to deploy the tag `v0.1.0` to production.

```bash
terraform plan -var app_name=brew-bot-server -var app_version=v0.1.0 -out=current.tfplan
```

## Execute that plan remotely

Once the plan has been reported back to you, and you're comfortable with the reported changes, you can execute that plan against remote state.

```bash
terraform apply "current.tfplan"
```
