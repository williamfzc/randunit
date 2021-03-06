set -e

SHELL_FOLDER=$(dirname "$0")
cd $SHELL_FOLDER
cd ..
echo `pwd`

./gradlew ktlintFormat clean randunit-demo:assembleDebug randunit-demo:test

# ok
echo "ok :)"
