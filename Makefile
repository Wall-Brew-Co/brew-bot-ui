
MAKE = make
LEIN = lein

# These are the locations of the directories we'll use
TARGET_DIR = target
VERCHG = bin/verchg
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

version/major:
	$(info Updating major version and adding CHANGELOG entry...)
	@ $(VERCHG) 'major'

version/minor:
	$(info Updating minor version and adding CHANGELOG entry...)
	@ $(VERCHG) 'minor'

version/bugfix:
	$(info Updating bugfix version and adding CHANGELOG entry...)
	@ $(VERCHG) 'bugfix'

version:
	$(info Adding CHANGELOG entry for existing version...)
	@ $(VERCHG) 'push'
