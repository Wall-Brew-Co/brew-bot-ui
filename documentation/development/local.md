# Local Development

## Hot Reloading

The Tailwind CSS assets, Clojurescript apps, and Clojure server each support their own hot-reloading processes.
Starting each of these processes creates the most fluid development experience, and make targets exist for each of them:

For Tailwind:

```shell
make run/dev/tailwind
```

For Clojure:

```shell
make run/dev/server
```

For Clojurescript:

```shell
make run/dev/recipe-builder # Or any of the other independent js apps
```

Each of these targets will start a watcher process and will continue of log updates as files change.

## Selenium Friendly Test Build

To compile the CSS, Clojurescript, and Clojure assets into unoptimized assets and serve these assets from the server, use the following command:

```shell
make run/selenium
```

This build is useful for testing brew-bot-ui as a whole, and is a good sanity check.
If issues arise between this build and the production build, the scope of issues is usually limited to the Javascript and CSS optimization.

Automated browser tests via Selenium exist for all Wall Brew applications, in the private `automation-test-suites` repository.

## Run The Local Production Build

To run the optimized build locally, use the following make target:

```shell
make run/prod
```

This is a good way to debug issues in build optimization, and as a final sanity check before deployment.

## Package Production Assets

To build an uberjar and the production assets, use the following target:

```bash
make artifacts
```

This is useful for testing advanced compilation options and catching what would otherwise be runtime errors in Heroku.
