import SwiftUI
import Theme

struct ProfileCardInputTextField: View {
    var title: String
    var placeholder: String = ""
    @Binding var text: String

    var body: some View {
        VStack(alignment: .leading) {
            Text(title)
                .textStyle(.titleMedium)
                .foregroundStyle(.white)

            TextField(placeholder, text: $text)
                .padding(.horizontal, 16)
                .padding(.vertical, 4)
                .frame(height: 56)
                .overlay(
                    RoundedRectangle(cornerRadius: 4)
                        .stroke(
                            AssetColors.Outline.outline.swiftUIColor,
                            style: StrokeStyle(lineWidth: 1)
                        )
                )
        }
    }
}

#Preview {
    ProfileCardInputTextField(
        title: "Nickname",
        text: .init(get: { "" }, set: {_ in})
    )
}
