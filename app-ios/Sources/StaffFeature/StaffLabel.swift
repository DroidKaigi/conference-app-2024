import SwiftUI
import Theme
import CommonComponents

struct StaffLabel: View {
    let name: String
    let icon: URL

    var body: some View {
        HStack(alignment: .center, spacing: 12) {
            CircularUserIcon(urlString: icon.absoluteString)
                .frame(width: 52, height: 52)
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
    StaffLabel(name: "hoge", icon: .init(string: "https://avatars.githubusercontent.com/u/10727543?s=156&v=4")!)
}
