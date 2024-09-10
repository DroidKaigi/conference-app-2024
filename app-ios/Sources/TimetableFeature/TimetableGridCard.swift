import Foundation
import SwiftUI
import Theme
import class shared.TimetableItem
import CommonComponents

public struct TimetableGridCard: View {
    let timetableItem: TimetableItem
    let onTap: (TimetableItem) -> Void
    let cellCount: Int
    
    public init(
        timetableItem: TimetableItem,
        cellCount: Int,
        onTap: @escaping (TimetableItem) -> Void
    ) {
        self.timetableItem = timetableItem
        self.onTap = onTap
        self.cellCount = cellCount
    }

    public var body: some View {
        Button {
            onTap(timetableItem)
        } label: {
            VStack(alignment: .leading, spacing: 8) {
                HStack(spacing: 4) {
                    if cellCount == 1 {
                        RoomTypeShape(roomType: .init(enTitle: timetableItem.room.name.enTitle))
                        .foregroundStyle(timetableItem.room.roomTheme.primaryColor)
                    }
                    Text("\(timetableItem.startsTimeString) - \(timetableItem.endsTimeString)")
                        .textStyle(.labelMedium)
                        .foregroundStyle(cellCount > 1 ? AssetColors.Surface.onSurfaceVariant.swiftUIColor : timetableItem.room.roomTheme.primaryColor)
                    Spacer()
                }
                
                Text(timetableItem.title.currentLangTitle)
                    .textStyle(.titleMedium)
                    .foregroundStyle(cellCount > 1 ? AssetColors.Surface.onSurface.swiftUIColor : timetableItem.room.roomTheme.primaryColor)
                    .multilineTextAlignment(.leading)
                
                Spacer()
                
                if timetableItem.speakers.count > 1 {
                    HStack(spacing: 4) {
                        ForEach(timetableItem.speakers, id: \.id) { speaker in
                            CircularUserIcon(urlString: speaker.iconUrl)
                                .frame(width: 32, height: 32)
                        }
                    }
                } else if let speaker = timetableItem.speakers.first {
                    HStack(spacing: 8) {
                        CircularUserIcon(urlString: speaker.iconUrl)
                            .frame(width: 32, height: 32)

                        Text(speaker.name)
                            .textStyle(.titleSmall)
                            .foregroundStyle(AssetColors.Surface.onSurfaceVariant.swiftUIColor)
                            .lineLimit(1)
                    }
                }
            }
            .frame(maxWidth: .infinity)
            .padding(12)
            .frame(maxWidth: 192 * CGFloat(cellCount) + CGFloat(12 * (cellCount - 1)))
            .frame(height: 153)
            .background(cellCount > 1 ? AssetColors.Surface.surfaceContainer.swiftUIColor : timetableItem.room.roomTheme.containerColor, in: RoundedRectangle(cornerRadius: 4))
            .overlay(RoundedRectangle(cornerRadius: 4).stroke(cellCount > 1 ? AssetColors.Surface.onSurface.swiftUIColor : timetableItem.room.roomTheme.primaryColor, lineWidth: 1))
        }
    }
}

#Preview {
    VStack {
        TimetableGridCard(
            timetableItem: TimetableItem.Session.companion.fake(), cellCount: 1,
            onTap: { _ in }
        )
        .padding(.horizontal, 16)
    }
    .frame(maxWidth: .infinity, maxHeight: .infinity)
    .background(Color.black)
}
