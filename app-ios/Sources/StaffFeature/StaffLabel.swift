import SwiftUI
import Theme

struct StaffLabel: View {
    let name: String
    let icon: URL

    var body: some View {
        HStack(alignment: .center, spacing: 12) {
            AsyncImage(url: icon) {
                $0.image?.resizable()
            }
            .frame(width: 52, height: 52)
            .clipShape(Circle())
            .overlay(
                Circle()
                    .stroke(AssetColors.Outline.outline.swiftUIColor, lineWidth: 1)
            )

            Text(name)
                .textStyle(.bodyLarge)
                .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                .lineLimit(2)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
}

#Preview {
    StaffLabel(name: "hoge", icon: .init(string: "")!)
}
