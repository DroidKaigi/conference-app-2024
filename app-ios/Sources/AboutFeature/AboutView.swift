import ComposableArchitecture
import SwiftUI

public struct AboutView: View {
    @Bindable var store: StoreOf<AboutReducer>

    public init(store: StoreOf<AboutReducer>) {
        self.store = store
    }

    public var body: some View {
        NavigationStack(path: $store.scope(state: \.path, action: \.path)) {
            ScrollView {
                VStack(spacing: 0) {
                    KeyVisual()
                        .padding(.top, 28)
                        .padding(.bottom, 32)
                    
                    VStack {
                        HStack {
                            Text("Credits")
                                .foregroundStyle(Color(.surfaceOnSurfaceVariant))
                                .font(.headline)
                            Spacer()
                        }

                        Button(action: {
                            store.send(.view(.staffsTapped))
                        }, label: {
                            Label("Staffs", systemImage: "face.smiling")
                                .foregroundStyle(Color(.surfaceOnSurface))
                            Spacer()
                        })
                        .padding(.init(top: 24, leading: 14, bottom: 24, trailing: 14))
                        
                        Divider()
                            .background(Color(.outlineOutlineVariant))

                        Button(action: {
                            store.send(.view(.contributersTapped))
                        }, label: {
                            Label("Contributers", systemImage: "person.3")
                                .foregroundStyle(Color(.surfaceOnSurface))
                            Spacer()
                        })
                        .padding(.init(top: 24, leading: 14, bottom: 24, trailing: 14))

                        Divider()
                            .background(Color(.outlineOutlineVariant))

                        Button(action: {
                            store.send(.view(.sponsorsTapped))
                        }, label: {
                            Label("Sponsors", systemImage: "building.2")
                                .foregroundStyle(Color(.surfaceOnSurface))
                            Spacer()
                        })
                        .padding(.init(top: 24, leading: 14, bottom: 24, trailing: 14))

                        Divider()
                            .background(Color(.outlineOutlineVariant))

                    }
                    
                }
                .padding(.horizontal, 16)
            }
            .background(Color(.background))
        } destination: { store in
            switch store.state {
            case .staffs:
                Text("Staff")
            case .contributers:
                Text("Contributers")
            case .sponsors:
                Text("Sponsors")
            case .acknowledgements:
                Text("Acknowledgements")
            }
        }
    }
}

#Preview {
    AboutView(store: .init(initialState: .init(), reducer: { AboutReducer() }))
}
