set -e

SHELL_FOLDER=$(dirname "$0")
cd $SHELL_FOLDER
cd ..
echo `pwd`

./gradlew ktlintFormat
./gradlew randunit:jacocoTestReport
./gradlew randunit-android:test
./gradlew randunit-demo:jacocoTestReport
./gradlew randunit-android-demo:test
