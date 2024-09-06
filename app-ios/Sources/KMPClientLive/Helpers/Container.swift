import Firebase
import ObjectiveC
@preconcurrency import shared

public struct Container: Sendable {
    public static let shared: Container = .init()

    private let entryPoint: KmpEntryPoint
    private init() {
        entryPoint = .init()
        entryPoint.doInit(
            remoteConfigApi: RemoteConfigApiImpl(),
            authenticator: AuthenticatorImpl()
        )
    }

    public func get<TypeProtocol, ReturnType>(type: TypeProtocol) -> ReturnType where TypeProtocol: Protocol {
        guard let object = entryPoint.get(objCProtocol: type) as? ReturnType else {
            fatalError("Not found instance for \(type)")
        }
        return object
    }
}
