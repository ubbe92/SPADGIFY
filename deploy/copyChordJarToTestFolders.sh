#!/usr/bin/env bash

cd ./../testChord/8185
rm spadgify-1.0-SNAPSHOT.jar

cd ./../8187
rm spadgify-1.0-SNAPSHOT.jar

cd ./../8188
rm spadgify-1.0-SNAPSHOT.jar

cd ./../8193
rm spadgify-1.0-SNAPSHOT.jar

cd ./../8206
rm spadgify-1.0-SNAPSHOT.jar

echo "Removed old jar in test folders!"

#cd ./../java/spadgify/target
cd ./../../java/spadgify/target
cp spadgify-1.0-SNAPSHOT.jar ../../../testChord/8185
cp spadgify-1.0-SNAPSHOT.jar ../../../testChord/8187
cp spadgify-1.0-SNAPSHOT.jar ../../../testChord/8188
cp spadgify-1.0-SNAPSHOT.jar ../../../testChord/8193
cp spadgify-1.0-SNAPSHOT.jar ../../../testChord/8206

echo "Copied new jar to test folders!"
