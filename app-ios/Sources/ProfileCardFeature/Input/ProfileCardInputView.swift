import SwiftUI
import CommonComponents
import ComposableArchitecture
import Theme

public struct ProfileCardInputView: View {
    @Bindable private var store: StoreOf<ProfileCardInputReducer>

    public init(store: StoreOf<ProfileCardInputReducer>) {
        self.store = store
    }

    public var body: some View {
        ScrollView {
            VStack(spacing: 32) {
                Text("会場やSNSで自分を紹介できるプロフィールカードを作ってみましょう！", bundle: .module)
                    .textStyle(.bodyLarge)
                    .foregroundStyle(AssetColors.Surface.onSurfaceVariant.swiftUIColor)

                ProfileCardInputTextField(
                    title: String(localized: "ニックネーム", bundle: .module),
                    text: .init(
                        get: {
                            store.nickname
                        }, set: {
                            store.send(.view(.nicknameChanged($0)))
                        }
                    )
                )

                ProfileCardInputTextField(
                    title: String(localized: "職種", bundle: .module),
                    text: .init(
                        get: {
                            store.occupation
                        }, set: {
                            store.send(.view(.occupationChanged($0)))
                        }
                    )
                )

                ProfileCardInputTextField(
                    title: String(localized: "リンク（ex.X、Instagram...）", bundle: .module),
                    placeholder: "https://",
                    text: .init(
                        get: {
                            store.link
                        }, set: {
                            store.send(.view(.linkChanged($0)))
                        }
                    )
                )

                ProfileCardInputImage(
                    selectedPhoto: .init(
                        get: {
                            store.photo
                        },
                        set: {
                            store.send(.view(.photoChanged($0)))
                        }
                    ), 
                    title: String(localized: "画像", bundle: .module)
                )

                ProfileCardInputCardType(
                    selectedCardType: .init(
                        get: {
                            store.cardType
                        },
                        set: {
                            store.send(.view(.cardTypeChanged($0)))
                        }
                    )
                )

                Button {
                    store.send(.view(.createCardTapped))
                } label: {
                    Text("カードを作成する", bundle: .module)
                        .textStyle(.labelLarge)
                        .frame(maxWidth: .infinity, minHeight: 56, alignment: .center)
                        .foregroundStyle(AssetColors.Primary.onPrimary.swiftUIColor)
                        .background(AssetColors.Primary.primary.swiftUIColor)
                        .clipShape(Capsule())
                }
            }
        }
        .padding(.horizontal, 16)
        // bottom floating tabbar padding
        .padding(.bottom, 60)
        .background(AssetColors.Surface.surface.swiftUIColor)
        .navigationBarTitleDisplayMode(.large)
        .navigationTitle(String(localized: "プロフィールカード", bundle: .module))
        .onAppear { 
            store.send(.view(.onAppear))
        }
    }
}

#Preview {
    ProfileCardInputView(store: .init(initialState: .init(), reducer: {
        EmptyReducer()
    }))
}
