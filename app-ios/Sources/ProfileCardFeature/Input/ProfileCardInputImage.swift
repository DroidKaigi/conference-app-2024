import SwiftUI
import Theme

struct ProfileCardInputImage: View {
    var title: String
    var selectedImage: UIImage?

    var body: some View {
        VStack(alignment: .leading) {
            Text(title)
                .textStyle(.titleMedium)
                .foregroundStyle(.white)

            if let selectedImage {
                Text("TODO")
            } else {
                Button {

                } label: {
                    HStack {
                        Image(.add)
                            .resizable()
                            .renderingMode(.template)
                            .frame(width: 18, height: 18)

                        Text("画像を追加", bundle: .module)
                            .textStyle(.labelLarge)
                    }
                    .padding(EdgeInsets(top: 11, leading: 16, bottom: 11, trailing: 24))
                    .foregroundStyle(AssetColors.Primary.primary.swiftUIColor)
                    .overlay(
                        Capsule()
                            .stroke(
                                AssetColors.Outline.outline.swiftUIColor,
                                style: StrokeStyle(lineWidth: 1)
                            )
                    )
                }
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
}

#Preview {
    ProfileCardInputTextField(
        title: "ニックネーム",
        text: .init(get: { "" }, set: {_ in})
    )
}
