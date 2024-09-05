import FirebaseRemoteConfig
import shared

class RemoteConfigApiImpl: RemoteConfigApi {
    private let remoteConfig = RemoteConfig.remoteConfig()

    init() {
        remoteConfig.addOnConfigUpdateListener { [weak remoteConfig] configUpdate, error in
            guard error == nil else {
                return
            }

            remoteConfig?.activate()
        }
    }

    func __getBoolean(key: String) async throws -> KotlinBoolean {
        .init(
            bool: remoteConfig.configValue(forKey: key).boolValue
        )
    }
    func __getString(key: String) async throws -> String {
        return remoteConfig.configValue(forKey: key).stringValue ?? ""
    }
}
