import SwiftUI
import Theme
import class shared.TimetableItem

public struct TimetableCard: View {
    let timetableItem: TimetableItem
    let isFavorite: Bool
    let onTap: (TimetableItem) -> Void
    let onTapFavorite: (TimetableItem, CGPoint?) -> Void
    
    public init(
        timetableItem: TimetableItem,
        isFavorite: Bool,
        onTap: @escaping (TimetableItem) -> Void,
        onTapFavorite: @escaping (TimetableItem, CGPoint?) -> Void
    ) {
        self.timetableItem = timetableItem
        self.isFavorite = isFavorite
        self.onTap = onTap
        self.onTapFavorite = onTapFavorite
    }

    // MEMO: Used to adjust margins by Orientation.
    @Environment(\.horizontalSizeClass) var heightSizeClass
    @Environment(\.verticalSizeClass) var widthSizeClass

    public var body: some View {
        Button {
            onTap(timetableItem)
        } label: {
            VStack(alignment: .leading, spacing: 8) {
                HStack(spacing: 4) {
                    RoomTag(.init(
                        currentLangTitle: timetableItem.room.name.currentLangTitle,
                        enTitle: timetableItem.room.name.enTitle,
                        jaTitle: timetableItem.room.name.jaTitle
                    ))
                    ForEach(timetableItem.language.labels, id: \.self) { label in
                        LanguageTag(label)
                    }
                    Spacer()

                    // [NOTE] In order to calculate the value from GeometryReader, it is supported by assigning DragGesture to the Image element instead of Button.
                    HStack {
                        GeometryReader { geometry in
                            // MEMO: Since the coordinate values ​​are based on the inside of the View, ".local" is specified.
                            let localGeometry = geometry.frame(in: .local)
                            Image(isFavorite ? .icFavoriteFill : .icFavoriteOutline)
                                .resizable()
                                .renderingMode(.template)
                                .foregroundColor(
                                    isFavorite ? AssetColors.Primary.primaryFixed.swiftUIColor : AssetColors.Surface.onSurfaceVariant.swiftUIColor
                                )
                                .frame(width: 24, height: 24)
                                .gesture(DragGesture(minimumDistance: 0, coordinateSpace: .global).onEnded { dragGesture in
                                    // MEMO: The offset value in the Y-axis direction is subtracted for adjustment (decided by device orientation).
                                    let adjustedLocationPoint = CGPoint(x: dragGesture.location.x, y: dragGesture.location.y - calculateTopMarginByDevideOrietation())
                                    onTapFavorite(timetableItem, adjustedLocationPoint)
                                })
                                // MEMO: To adjust horizontal position, I'm subtracting half the size of Image (-12).
                                .position(x: localGeometry.maxX - 12, y: localGeometry.midY)
                        }
                    }
                    .frame(height: 24, alignment: .trailing)
                    .sensoryFeedback(.impact, trigger: isFavorite) { _, newValue in newValue }
                }
                
                Text(timetableItem.title.currentLangTitle)
                    .textStyle(.titleMedium)
                    .foregroundColor(AssetColors.Surface.onSurface.swiftUIColor)
                    .multilineTextAlignment(.leading)
                
                ForEach(timetableItem.speakers, id: \.id) { speaker in
                    HStack(spacing: 8) {
                        CircularUserIcon(urlString: speaker.iconUrl)
                            .frame(width: 32, height: 32)
                        Text(speaker.name)
                            .textStyle(.titleSmall)
                            .foregroundStyle(AssetColors.Surface.onSurfaceVariant.swiftUIColor)
                            .lineLimit(1)
                    }
                }

                let message: String? = switch timetableItem {
                case let session as TimetableItem.Session: session.message?.currentLangTitle
                case let special as TimetableItem.Special: special.message?.currentLangTitle
                default: nil
                }
                if let message, !message.isEmpty {
                    HStack(spacing: 8) {
                        Image(.icInfoFill)
                        Text(message)
                            .textStyle(.bodySmall)
                            .multilineTextAlignment(.leading)
                            .foregroundStyle(AssetColors.Error.error.swiftUIColor)
                    }
                }
            }
            .frame(maxWidth: .infinity)
            .padding(12)
            .background(AssetColors.Surface.surface.swiftUIColor, in: RoundedRectangle(cornerRadius: 4))
            .overlay(RoundedRectangle(cornerRadius: 4).stroke(AssetColors.Outline.outlineVariant.swiftUIColor, lineWidth: 1))
        }
    }

    private func calculateTopMarginByDevideOrietation() -> CGFloat {
        if widthSizeClass == .regular && heightSizeClass == .compact {
            return CGFloat(128)
        } else {
            return CGFloat(96)
        }
    }
}

#Preview {
    VStack {
        TimetableCard(
            timetableItem: TimetableItem.Session.companion.fake(),
            isFavorite: true,
            onTap: { _ in },
            onTapFavorite: { _,_  in }
        )
        .padding(.horizontal, 16)
    }
    .frame(maxWidth: .infinity, maxHeight: .infinity)
    .background(Color.black)
}
