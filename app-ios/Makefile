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

.PHONY: build-app-debug
build-app-debug: build-kmp-module
	set -o pipefail && \
		xcodebuild -project $(PROJECT_FILE) \
		-scheme $(SCHEME_NAME_APP) \
		-configuration Debug \
		-destination 'generic/platform=iOS Simulator' \
		clean build

