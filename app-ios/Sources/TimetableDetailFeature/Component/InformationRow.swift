import SwiftUI
import Theme

struct InformationRow: View {
    let icon: Image
    let title: String
    let titleColor: Color
    let content: String

    var body: some View {
        HStack {
            icon
                .renderingMode(.template)
                .foregroundStyle(titleColor)
            HStack(spacing: 12) {
                Text(title)
                    .textStyle(.titleSmall)
                    .foregroundStyle(titleColor)
                    .bold()
                HStack {
                    Text(content)
                        .textStyle(.bodyMedium)
                        .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                }

            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .foregroundStyle(
            Color.white
        )
    }
}

#Preview {
    InformationRow(
        icon: Image(systemName: "clock"),
        title: String(localized: "TimeTableDetailDate", bundle: .module),
        titleColor: AssetColors.Custom.flamingo.swiftUIColor,
        content: "2024.09.14 / 11:20 ~ 12:00"
    )
}

