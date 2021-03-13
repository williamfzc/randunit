set -e

SHELL_FOLDER=$(dirname "$0")
cd $SHELL_FOLDER
cd ..
echo `pwd`

# check first
./gradlew ktlintFormat licenseFormat

# build
./gradlew clean randunit:build randunit:publishToMavenLocal
./gradlew randunit-android:assembleRelease randunit-android:publishToMavenLocal

# ok
echo "ok :)"
