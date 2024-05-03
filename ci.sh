#!/bin/sh
./gradlew testDevDebugUnitTest
./gradlew assembleSharedXCFramework --no-configuration-cache
./gradlew iosSimulatorArm64Test --no-configuration-cache



