import shared
import SwiftUI
import Theme

struct ContributorListItemView: View {
    let contributor: Contributor
    let onContributorButtonTapped: (URL) -> Void

    var body: some View {
        Button {
            if let urlString = contributor.profileUrl,
               let url = URL(string: urlString) {
                onContributorButtonTapped(url)
            }
        } label: {
            HStack(alignment: .center, spacing: 12) {
                AsyncImage(url: URL(string: contributor.iconUrl)) {
                    $0.image?.resizable()
                }
                .frame(width: 52, height: 52)
                .clipShape(Circle())

                Text(contributor.username)
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
        contributor: Contributor.companion.fakes().first!
    ) { _ in }
}
