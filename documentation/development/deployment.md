# Deployment

## Deployment Steps

These are the steps to move code from a Pull Request and in to Production.

### Update the Version Number

Each deployment requires a new version number.
The versioning scheme we use is [SemVer](http://semver.org/), and make targets have been added for your convenience.

* If **all** changes are bug fixes, increment the last value in the version with `make version/bugfix`
* If **any** change made **breaks** an external API, increment the first value in the version with `make version/major`
* If it's anything else, increment the **middle** number in the version with `make version/minor`

The version number must change, and the release notes should be updated as well in `CHANGELOG.md`
The above make target will automatically create a new entry to document your changes.

This should be the last change in most Pull Requests.
Once this is complete and the Pull Request has been approved, merge your changes into master.

### Tag the Git Repo with the New Version

Once you have updated the version and release notes, you need to check everything in and tag the git repo with the version:

```bash
git tag -a v0.1.0
```

Supply a concise, reasonable, sentence for this version tag.
In most cases, the CHANGELOG entry you created in the previous step will be sufficient.

You can then push the tag with:

```bash
git push --tags
```

### Plan the application stack with Terraform

The application, and all of its resources are managed by Terraform.
To deploy a new version of the app, we must first build the deployment plan.
In the example below, we'd be preparing to deploy the tag `v0.1.0` to production.

```bash
cd deploy/terraform
terraform plan -var app_name=brew-bot-server -var app_version=v0.1.0 -out=current.tfplan
```

To create a plan for the most recently tagged version of brew-bot-ui, use the following make target:

```bash
make plan/prod
```

### Execute that plan remotely

Once the plan has been reported back to you, and you're comfortable with the reported changes, you can execute that plan against remote state.

```bash
terraform apply "current.tfplan"
```

To execute the plan for the most recently tagged version of brew-bot-ui, use the following make target:

```bash
make deploy/prod
```
