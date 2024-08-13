//
//  File.swift
//  
//
//  Created by CHARLES BOND on 2024/08/12.
//

import Foundation

import SwiftUI
import Theme
import class shared.TimetableItem

public struct TimetableGridCard: View {
    let timetableItem: TimetableItem?
    let onTap: (TimetableItem) -> Void
    
    public init(
        timetableItem: TimetableItem?,
        onTap: @escaping (TimetableItem) -> Void
    ) {
        self.timetableItem = timetableItem
        self.onTap = onTap
    }

    public var body: some View {
        if let timetableItem {
            Button {
                onTap(timetableItem)
            } label: {
                VStack(alignment: .leading, spacing: 8) {
                    HStack(spacing: 4) {
                        timetableItem.room.shape
                            .foregroundColor(timetableItem.room.roomTheme.primaryColor)
                        Text("\(timetableItem.startsTimeString) - \(timetableItem.endsTimeString)")
                            .textStyle(.labelMedium)
                            .foregroundColor(timetableItem.room.roomTheme.primaryColor)
                        Spacer()
                    }
                    
                    Text(timetableItem.title.currentLangTitle)
                        .textStyle(.titleMedium)
                        .foregroundColor(timetableItem.room.roomTheme.primaryColor)
                        .multilineTextAlignment(.leading)
                    
                    Spacer()
                    
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
                .frame(width: 192, height: 153)
                .background(timetableItem.room.roomTheme.containerColor, in: RoundedRectangle(cornerRadius: 4))
                .overlay(RoundedRectangle(cornerRadius: 4).stroke(timetableItem.room.roomTheme.primaryColor, lineWidth: 1))
            }
        } else {
            VStack {
                //Empty on purpose
            }
            .frame(maxWidth: .infinity)
            .padding(12)
            .frame(width: 192, height: 153)
            .background(Color.clear, in: RoundedRectangle(cornerRadius: 4))
        }

    }
}

#Preview {
    VStack {
        TimetableGridCard(
            timetableItem: TimetableItem.Session.companion.fake(),
            onTap: { _ in }
        )
        .padding(.horizontal, 16)
    }
    .frame(maxWidth: .infinity, maxHeight: .infinity)
    .background(Color.black)
}
