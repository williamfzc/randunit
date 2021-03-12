set -e

SHELL_FOLDER=$(dirname "$0")
cd $SHELL_FOLDER
cd ..
echo `pwd`

./gradlew ktlintFormat clean randunit-android-demo:assembleDebug randunit-android-demo:test

# ok
echo "ok :)"
