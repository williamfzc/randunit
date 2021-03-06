set -e

SHELL_FOLDER=$(dirname "$0")
cd $SHELL_FOLDER
cd ..
echo `pwd`

./gradlew clean randunit-demo:assembleDebug randunit-demo:test

# ok
echo "ok :)"
