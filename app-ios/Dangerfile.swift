import Danger
import Foundation

let danger = Danger()

SwiftLint.lint(inline: true, configFile: ".swiftlint.yml")
