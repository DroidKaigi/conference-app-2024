import SwiftUI
import Theme
import class shared.TimetableItem

public struct TimetableCard: View {
    let timetableItem: TimetableItem
    let isFavorite: Bool
    let onTap: (TimetableItem) -> Void
    let onTapFavorite: (TimetableItem) -> Void
    
    public init(
        timetableItem: TimetableItem,
        isFavorite: Bool,
        onTap: @escaping (TimetableItem) -> Void,
        onTapFavorite: @escaping (TimetableItem) -> Void
    ) {
        self.timetableItem = timetableItem
        self.isFavorite = isFavorite
        self.onTap = onTap
        self.onTapFavorite = onTapFavorite
    }

    public var body: some View {
        Button {
            onTap(timetableItem)
        } label: {
            VStack(alignment: .leading, spacing: 8) {
                HStack(spacing: 4) {
                    RoomTag(timetableItem.room.name)
                    ForEach(timetableItem.language.labels, id: \.self) { label in
                        LanguageTag(label)
                    }
                    Spacer()
                    Button {
                        onTapFavorite(timetableItem)
                    } label: {
                        Image(isFavorite ? .icFavoriteFill : .icFavoriteOutline)
                            .resizable()
                            .renderingMode(.template)
                            .foregroundColor(
                                isFavorite ?
                                    AssetColors.Primary.primaryFixed.swiftUIColor
                                    :
                                    AssetColors.Surface.onSurfaceVariant.swiftUIColor
                            )
                            .frame(width: 24, height: 24)
                    }
                }
                
                Text(timetableItem.title.currentLangTitle)
                    .textStyle(.titleMedium)
                    .foregroundColor(AssetColors.Surface.onSurface.swiftUIColor)
                    .multilineTextAlignment(.leading)
                
                ForEach(timetableItem.speakers, id: \.id) { speaker in
                    HStack(spacing: 8) {
                        Group {
                            if let url = URL(string: speaker.iconUrl) {
                                AsyncImage(url: url) {
                                    $0.image?.resizable()
                                }
                            } else {
                                Circle().stroke(Color.gray)
                            }
                        }
                        .frame(width: 32, height: 32)
                        .clipShape(Circle())

                        Text(speaker.name)
                            .textStyle(.titleSmall)
                            .foregroundStyle(AssetColors.Surface.onSurfaceVariant.swiftUIColor)
                            .lineLimit(1)
                    }
                }
            }
            .frame(maxWidth: .infinity)
            .padding(12)
            .background(AssetColors.Surface.surface.swiftUIColor, in: RoundedRectangle(cornerRadius: 4))
            .overlay(RoundedRectangle(cornerRadius: 4).stroke(AssetColors.Outline.outlineVariant.swiftUIColor, lineWidth: 1))
        }
    }
}

#Preview {
    VStack {
        TimetableCard(
            timetableItem: TimetableItem.Session.companion.fake(),
            isFavorite: true,
            onTap: { _ in },
            onTapFavorite: { _ in }
        )
        .padding(.horizontal, 16)
    }
    .frame(maxWidth: .infinity, maxHeight: .infinity)
    .background(Color.black)
}
