PROJECT_FILE := App/App.xcodeproj
SCHEME_NAME_APP := DroidKaigi2024App

.PHONY: setup
setup:
	brew bundle --no-lock

.PHONY: open
open:
	open $(PROJECT_FILE)

.PHONY: build-kmp-module
build-kmp-module:
	cd .. && \
	./gradlew app-ios-shared:assembleSharedXCFramework --no-configuration-cache

.PHONY: build-kmp-module-debug
build-kmp-module-debug:
	cd .. && \
	./gradlew app-ios-shared:assembleSharedXCFramework -Papp.ios.shared.arch=arm64SimulatorDebug --no-configuration-cache

.PHONY: build-app-debug
build-app-debug: build-kmp-module-debug
	set -o pipefail && \
		xcodebuild -project $(PROJECT_FILE) \
		-scheme $(SCHEME_NAME_APP) \
		-configuration Debug \
		-skipPackagePluginValidation \
		-skipMacroValidation \
		-destination "platform=iOS Simulator,name=iPhone 15 Pro,OS=17.5" \
		clean build | xcbeautify

.PHONY: test-app-debug
test-app-debug:
	set -o pipefail && \
		xcodebuild test -project $(PROJECT_FILE) \
		-scheme $(SCHEME_NAME_APP) \
		-configuration Debug \
		-skipPackagePluginValidation \
		-skipMacroValidation \
		-destination "platform=iOS Simulator,name=iPhone 15 Pro,OS=17.5" \
		| xcbeautify

.PHONY: bootstrap
bootstrap:
	make setup && \
	make build-kmp-module-debug && \
	make open

# Gradle Utility
.PHONY: gradle-clean
gradle-clean:
	cd .. && \
	./gradlew --stop && ./gradlew clean
