import shared

struct Container {
    static let shared: Container = .init()

    private let entryPoint: KmpEntryPoint
    private init() {
        entryPoint = .init()
        class DummyAuthenticator: Authenticator {
            
            func currentUser() async throws -> User? {
              return User(idToken: "")
            }
            
            
            func signInAnonymously() async throws -> User? {
                return User(idToken: "")
            }
            
        }
        entryPoint.doInit(
            remoteConfigApi: FakeRemoteConfigApi(),
            authenticator: DummyAuthenticator()
        )
    }

    func get<TypeProtocol, ReturnType>(type: TypeProtocol) -> ReturnType where TypeProtocol: Protocol {
            guard let object = entryPoint.get(objCProtocol: type) as? ReturnType else {
                fatalError("Not found instance for \(type)")
            }
            return object
        }
    
    func get<TypeProtocol>(type: TypeProtocol) -> TypeProtocol where TypeProtocol: Protocol {
            guard let object = entryPoint.get(objCProtocol: type) as? TypeProtocol else {
                fatalError("Not found instance for \(type)")
            }
            return object
        }
}
