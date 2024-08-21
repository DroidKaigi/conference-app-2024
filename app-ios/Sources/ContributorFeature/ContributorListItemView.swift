import SwiftUI
import Theme
import Model
import CommonComponents

struct ContributorListItemView: View {
    let contributor: Contributor
    let onContributorButtonTapped: (URL) -> Void

    var body: some View {
        Button {
            if let url = contributor.profileUrl {
                onContributorButtonTapped(url)
            }
        } label: {
            HStack(alignment: .center, spacing: 12) {
                CircularUserIcon(urlString: contributor.iconUrl.absoluteString)
                    .frame(width: 52, height: 52)

                Text(contributor.userName)
                    .textStyle(.bodyLarge)
                    .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                    .multilineTextAlignment(.leading)
                    .lineLimit(2)

                Spacer()

            }
            .frame(maxWidth: .infinity)
            .padding(.init(top: 12, leading: 16, bottom: 12, trailing: 16))
        }
    }
}

#Preview {
    ContributorListItemView(
        contributor: .init(
            id: 0,
            userName: "hoge",
            profileUrl: URL(string: "https://2024.droidkaigi.jp/"),
            iconUrl: URL(string: "https://avatars.githubusercontent.com/u/10727543?s=200&v=4")!
        )
    ) { _ in }
}
