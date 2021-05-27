# brew-bot-ui

The SPA version of [brew-bot.](https://github.com/Wall-Brew-Co/brew-bot)
To view the current production build, please visit [Wall Brew's main site.](https://brewbot.wallbrew.com/)
To view the current health and running state of the application, please navigate to Heroku.

## Development

### Client and Server

The entire project's build is managed by [Leiningen.](https://leiningen.org/)
The most important build configurations have all been aliased with corresponding Makefile targets.
Depending on the type of testing or development you need to do, you'll need to invoke one or more of them.
Full documentation is available in the [local development documentation.](/documentation/development/local.md)

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
make tests/all
```

If you only want to run tests for one runtime environment at a time, similar targets are available:

```bash
make tests/server
make tests/app
```

## Deployment

The app's entire deployment is managed by [Terraform](https://www.terraform.io/) via Heroku.
The current configuration is set up to load tagged versions of the app from GitHub, which are then built and deployed remotely.
Full documentation is available in the [local deployment documentation.](/documentation/development/local.md)

## License

Copyright © 2020-2021 - [Wall Brew Co](https://wallbrew.com/)
