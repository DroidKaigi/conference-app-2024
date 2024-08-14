import SwiftUI
import Theme
import Model

struct SessionDescriptionView: View {
    @State private var isDescriptionExpanded: Bool = false
    @State private var canBeExpanded: Bool = false
    let content: String
    let themeColor: Color

    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text(.init(content))
                .textStyle(.bodyLarge)
                .textSelection(.enabled)
                .lineLimit(isDescriptionExpanded ? nil : 5)
                .foregroundStyle(AssetColors.Surface.onSurfaceVariant.swiftUIColor)
                .background {
                    ViewThatFits(in: .vertical) {
                        Text(content)
                            .hidden()
                        // Just for receiving onAppear event if the description exceeds its line limit
                        Color.clear
                            .onAppear {
                                canBeExpanded = true
                            }
                    }
                }
            if canBeExpanded {
                Button {
                    isDescriptionExpanded = true
                    canBeExpanded = false
                } label: {
                    Text(String(localized: "TimeTableDetailReadMore", bundle: .module))
                        .textStyle(.labelLarge)
                        .foregroundStyle(themeColor)
                        .frame(height: 40)
                        .frame(maxWidth: .infinity, alignment: .center)
                        .overlay {
                            Capsule()
                                .stroke(AssetColors.Outline.outline.swiftUIColor)
                        }
                }
            }
        }
        .animation(.default, value: isDescriptionExpanded)
    }
}

#Preview {
    VStack {
        SessionDescriptionView(
            content: "Kotlin Coroutinesは非同期処理をシンプルに記述できるKotlinの言語機能です。実験的な機能としてこれまでも提供されてきましたがKotlin 1.3で正式にリリース予定です。Androidの誕生から10年たちアプリの利用シーンが増えたKotlin Coroutinesは非同期処理をシンプルに記述できるKotlinの言語機能です。実験的な機能としてこれまでも提供されてきましたがKotlin 1.3で正式にリリース予定です。Androidの誕生から10年たちアプリの利用シーンが増えた.",
                   
            themeColor: AssetColors.Custom.flamingo.swiftUIColor
        )
            .padding(.horizontal, 16)
    }
    .background(AssetColors.Surface.surface.swiftUIColor)
}
