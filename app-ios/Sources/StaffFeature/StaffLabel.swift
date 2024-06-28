import SwiftUI
import Theme

struct StaffLabel: View {
    let name: String
    let icon: URL

    var body: some View {
        HStack(alignment: .center) {
            AsyncImage(url: icon) { image in
                image.resizable()
            } placeholder: {
                Color.gray
            }
            .frame(width: 60, height: 60)
            .scaledToFill()
            .clipShape(RoundedRectangle(cornerRadius: 12))
            .overlay(
                RoundedRectangle(cornerRadius: 12)
                    .stroke(AssetColors.Outline.outline.swiftUIColor, lineWidth: 1)
            )
            Text(name)
                .textStyle(TypographyTokens.bodyLarge)
                .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                .lineLimit(2)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
}

#Preview {
    StaffLabel(name: "hoge", icon: .init(string: "")!)
}
