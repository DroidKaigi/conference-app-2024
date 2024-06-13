import SwiftUI

public struct InformationRow: View {
    private let icon: Image
    private let title: String
    private let content: String

    public init(
        icon: Image,
        title: String,
        content: String
    ) {
        self.icon = icon
        self.title = title
        self.content = content
    }

    public var body: some View {
        HStack {
            icon
            HStack(spacing: 12) {
                Text(title)
                    .font(.callout)
                    .foregroundStyle(Color(.surfaceVariant))
                    .bold()
                HStack {
                    Text(content)
                        .font(.callout)
                        .foregroundStyle(Color(.onSurface))
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
        content: "2023.09.14 / 11:20 ~ 12:00 (40分)"
    )
}

