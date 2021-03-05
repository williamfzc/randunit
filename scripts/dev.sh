set -e

SHELL_FOLDER=$(dirname "$0")
cd $SHELL_FOLDER
cd ..
echo `pwd`

# compile sdk
./gradlew clean randunit-android:assembleDebug

# compile demo
cp -rf randunit-android/build/outputs/aar/*.aar randunit-demo/libs/
./gradlew clean randunit-demo:assembleDebug

# ok
echo "ok :)"
