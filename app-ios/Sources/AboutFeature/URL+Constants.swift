import Foundation

extension URL {
    static let youtube = URL(string: "https://www.youtube.com/c/DroidKaigi")!
    static let xcom = URL(string: "https://x.com/DroidKaigi")!
    static let medium = URL(string: "https://medium.com/droidkaigi")!
    static let privacyPolicy = URL(string: String(localized: "PrivacyPolicyURL", bundle: .module))!
    static let codeOfConduct = URL(string: String(localized: "CodeOfConductURL", bundle: .module))!
}
