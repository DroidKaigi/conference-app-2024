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
                
                ForEach(timetableItem.speakers, id: \.id) { speaker in
                    HStack(spacing: 8) {
                        Group {
                            AsyncImage(url: URL(string: speaker.iconUrl)) {
                                $0.resizable()
                            } placeholder: {
                                Color.gray
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
            .frame(width: 192 * CGFloat(cellCount) + CGFloat(12 * (cellCount - 1)), height: 153)
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
