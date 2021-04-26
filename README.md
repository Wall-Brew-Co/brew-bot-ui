# brew-bot-ui

The SPA/back-end for brew-bot

## Development

### Client and Server

The entire project's build is managed by [Leiningen.](https://leiningen.org/)
The most important build configurations have all been aliased with corresponding Makefile entries.
Depending on the type of testing or development you need to do, you'll need to invoke one of the following commands:

#### Production

```bash
make artifacts
```

The full production build of the application.
This will create an uberjar and package the client application as well.
This is useful for testing advanced compilation options and catching what would otherwise be runtime errors.

#### Local Development

```bash
make run/dev
```

This will forcibly re-compile and launch the development server.
Once the client-side app has finished compilation, it will open a new tab in your default browser.
This is useful for standard development.

#### Automation Testing

```bash
make run/selenium
```

This will forcibly re-compile and launch the development server in test mode.
Once the client-side app has finished compilation, it will open a new tab in your default browser.
This disables some tooling to create an application version that is easily accessed by the Wall Brew automation test suite.

## Unit Testing

The server-side and client-side tests are both located within this repository's `/test` directory.
JVM tests are controlled by Leiningen, and Javascript tests are controlled by [Karma](https://karma-runner.github.io/latest/index.html) via [doo.](https://github.com/bensu/doo)

If you haven't run the tests before, you'll need to do some basic setup via [npm](https://www.npmjs.com/):

```bash
npm install karma karma-cljs-test --save-dev
npm install karma-chrome-launcher karma-firefox-launcher --save-dev
npm install
```

From there, you can execute both test suites with the following Make target:

```bash
tests/all
```

If you only want to run tests for one runtime environment at a time, similar targets are available:

```bash
tests/server
tests/app
```

## Deployment

The app's entire deployment is managed by [Terraform](https://www.terraform.io/) via Heroku.
The current configuration is set up to load tagged versions of the app from GitHub, which are then built and deployed remotely.
To follow the current deployment conventions, follow these steps for each release.

### Update the Version Number

Each deployment requires a new version number.
The versioning scheme we use is [SemVer](http://semver.org/), and make targets have been added for your convenience.

* If **all** changes are bug fixes, increment the last value in the version with `make version/bugfix`
* If **any** change made **breaks** an external API, increment the first value in the version with `make version/major`
* If it's anything else, increment the **middle** number in the version with `make version/minor`

The version number must change, and the release notes should be updated as well in `CHANGELOG.md`
The above make target will automatically create a new entry to document your changes.

### Tag the Git Repo with the New Version

Once you have updated the version and release notes, you need to check everything in and tag the git repo with the version:

```bash
git tag -a v0.1.0
```

and supply a concise, reasonable, sentence for this version tag.

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

## License

Copyright © 2020-2021 - [Wall Brew Co](https://wallbrew.com/)
