set -e

SHELL_FOLDER=$(dirname "$0")
cd $SHELL_FOLDER
cd ..
echo `pwd`

# compile sdk
./gradlew clean randunit:assembleDebug

# compile demo
cp -rf randunit/build/outputs/aar/randunit-debug.aar randunit-demo/libs/
./gradlew clean randunit-demo:assembleDebug

# ok
echo "ok"
