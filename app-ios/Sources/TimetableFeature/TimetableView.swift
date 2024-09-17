import ComposableArchitecture
import CommonComponents
import SwiftUI
import Theme
import shared

public struct TimetableView: View {
    @Bindable private var store: StoreOf<TimetableReducer>

    public init(store: StoreOf<TimetableReducer>) {
        self.store = store
    }
    
    @State private var timetableMode = TimetableMode.list
    @State private var switchModeIcon: ImageResource = .icGrid
    
    public var body: some View {
        VStack {
            HStack {
                ForEach(DayTab.allCases) { tabItem in
                    Button(action: {
                        store.send(.view(.selectDay(tabItem)))
                    }, label: {
                        HStack(spacing: 6) {
                            Text(tabItem.rawValue).textStyle(.titleMedium).underline(store.selectedDay == tabItem)
                        }
                        .foregroundStyle(store.selectedDay == tabItem ? AssetColors.Custom.iguana.swiftUIColor : AssetColors.Surface.onSurface.swiftUIColor)
                        .padding(6)
                    })
                }
                Spacer()
            }.padding(5)
            switch timetableMode {
            case TimetableMode.list:
                TimetableListView(store: store)
            case TimetableMode.grid:
                TimetableGridView(store: store)
            }
            Spacer()
        }
        .background(AssetColors.Surface.surface.swiftUIColor)
        .frame(maxWidth: .infinity)
        .toolbar{
            ToolbarItem(placement: .topBarLeading) {
                Text("Timetable", bundle: .module)
                    .textStyle(.headlineMedium)
                    .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                
            }
            ToolbarItem(placement:.topBarTrailing) {
                HStack {
                    Button {
                        store.send(.view(.searchTapped))
                    } label: {
                        Group {
                            Image(systemName:"magnifyingglass").foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                        }
                        .frame(width: 40, height: 40)
                    }

                    Button {
                        switch timetableMode {
                        case .list:
                            timetableMode = .grid
                            switchModeIcon = .icList
                        case .grid:
                            timetableMode = .list
                            switchModeIcon = .icGrid
                        }
                    } label: {
                        Image(switchModeIcon)
                            .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                            .frame(width: 40, height: 40)
                    }
                }
            }
            
        }
    }
}

@MainActor
struct TimetableListView: View {
    private let store: StoreOf<TimetableReducer>

    init(store: StoreOf<TimetableReducer>) {
        self.store = store
    }

    // MEMO: A variable that stores the value of Animation variation. (Only 0 or 1)
    @State private var animationProgress: CGFloat = 0
    // MEMO: Select target targetTimetableItemId & targetLocationPoint (for Animation).
    @State private var targetTimetableItemId: TimetableItemId?
    @State private var targetLocationPoint: CGPoint?

    var body: some View {
        ZStack {
            ScrollView {
                LazyVStack(spacing: 0) {
                    ForEach(store.timetableItems, id: \.self) { item in
                        TimeGroupMiniList(contents: item, onItemTap: { item in
                            store.send(.view(.timetableItemTapped(item)))
                        }, onFavoriteTap: { timetableItemWithFavorite, adjustedLocationPoint in

                            store.send(.view(.favoriteTapped(timetableItemWithFavorite)))

                            // MEMO: When "isFavorited" flag is false, this view executes animation.
                            if timetableItemWithFavorite.isFavorited == false {
                                toggleFavorite(timetableItem: timetableItemWithFavorite.timetableItem, adjustedLocationPoint: adjustedLocationPoint)
                            }
                        })
                    }
                }.scrollContentBackground(.hidden)
                .onAppear {
                    store.send(.view(.onAppear))
                }.background(AssetColors.Surface.surface.swiftUIColor)
                bottomTabBarPadding
            }

            // MEMO: Stack the Image elements that will be animated using ZStack.
            makeHeartAnimationView()
        }
    }
    
    @ViewBuilder
    private func makeHeartAnimationView() -> some View {
        GeometryReader { geometry in
            if targetTimetableItemId != nil {
                Image(systemName: "heart.fill")
                    .foregroundColor(
                        AssetColors.Primary.primaryFixed.swiftUIColor
                    )
                    .frame(width: 24, height: 24)
                    .position(animationPosition(geometry: geometry))
                    .opacity(1 - animationProgress)
                    .zIndex(99)
            }
        }
    }
    
    private func animationPosition(geometry: GeometryProxy) -> CGPoint {

        // MEMO: Get the value calculated from both the default and .global GeometryReader.
        let globalGeometrySize = geometry.frame(in: .global).size
        let defaultGeometrySize = geometry.size

        // MEMO: Calculate the offset value in the Y-axis direction using GeometryReader.
        let startPositionY = targetLocationPoint?.y ?? 0
        let endPositionY = defaultGeometrySize.height - 25
        let targetY = startPositionY + (endPositionY - startPositionY) * animationProgress

        // MEMO: Calculate the offset value in the X-axis direction using GeometryReader.
        let adjustedPositionX = animationProgress * (globalGeometrySize.width / 2 - globalGeometrySize.width + 50)
        let targetX = defaultGeometrySize.width - 50 + adjustedPositionX

        return CGPoint(x: targetX, y: targetY)
    }

    private func toggleFavorite(timetableItem: TimetableItem, adjustedLocationPoint: CGPoint?) {

        targetLocationPoint = adjustedLocationPoint
        targetTimetableItemId = timetableItem.id

        // MEMO: Execute animation.
        if targetTimetableItemId != nil {
            withAnimation(.easeOut(duration: 1)) {
                animationProgress = 1
            }
            Task {
                try await Task.sleep(nanoseconds: 1_000_000_000)
                targetTimetableItemId = nil
                targetLocationPoint = nil
                animationProgress = 0
            }
        }
    }
}

struct TimetableGridView: View {
    private let store: StoreOf<TimetableReducer>

    init(store: StoreOf<TimetableReducer>) {
        self.store = store
    }

    var body: some View {
        let rooms = RoomType.allCases.filter {$0 != RoomType.roomIj}
        
        ScrollView([.horizontal, .vertical]) {
            Grid(alignment: .leading, horizontalSpacing: 4, verticalSpacing: 2) {
                GridRow {
                    Color.clear
                        .gridCellUnsizedAxes([.horizontal, .vertical])
                    
                    ForEach(rooms, id: \.self) { column in
                        let room = column.toRoom()
                        Text(room.name.currentLangTitle).foregroundStyle(room.roomTheme.primaryColor).textStyle(.titleMedium)
                            .frame(width: 192)

                    }
                }
                DashedDivider(axis: .horizontal)
                ForEach(store.timetableItems, id: \.self) { timeBlock in
                    GridRow {
                        VStack {
                            Text(timeBlock.startsTimeString).foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor).textStyle(.labelMedium)
                            Spacer()
                            
                        }.frame(width: 40, height: 153)
                        
                        if (timeBlock.items.count == 1 && timeBlock.isTopLunch()) {
                            
                            timeBlock.getCellForRoom(
                                room: RoomType.roomJ,
                                cellCount: 5,
                                onTap: { item in
                                    store.send(.view(.timetableItemTapped(item)))
                                }).gridCellColumns(5)
                            
                        } else {
                            ForEach(rooms, id: \.self) { room in
                                if let cell = timeBlock.getCellForRoom(room: room, cellCount: 1, onTap: { item in
                                    store.send(.view(.timetableItemTapped(item)))}) {
                                    cell
                                } else {
                                    Color.clear
                                        .frame(maxWidth: .infinity)
                                        .padding(12)
                                        .frame(width: 192, height: 153)
                                        .background(Color.clear, in: RoundedRectangle(cornerRadius: 4))
                                }
                            }
                        }
                    }
                    DashedDivider(axis: .horizontal)
                }
            }.fixedSize(horizontal: false, vertical: true)
            .padding(.trailing)
            
            bottomTabBarPadding
        }
    }
}

struct TimeGroupMiniList: View {
    let contents: TimetableTimeGroupItems
    let onItemTap: (TimetableItemWithFavorite) -> Void
    let onFavoriteTap: (TimetableItemWithFavorite, CGPoint?) -> Void
    
    var body: some View {
        HStack(spacing: 16) {
            VStack {
                Text(contents.startsTimeString).textStyle(.titleMedium)
                Text("|").font(.system(size: 8))
                Text(contents.endsTimeString).textStyle(.titleMedium)
                Spacer()
            }.foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
            VStack(spacing: 12) {
                ForEach(contents.items, id: \.self) { item in
                    TimetableCard(
                        timetableItem: item.timetableItem,
                        isFavorite: item.isFavorited,
                        onTap: {_ in
                            onItemTap(item)
                        },
                        onTapFavorite: { _, point in
                            onFavoriteTap(item, point)
                        })
                }
            }
        }.padding(16).background(Color.clear)
    }
}

struct DashedDivider: View {
    let axis: Axis
    
    var body: some View {
        let shape = LineShape(axis: axis)
            .stroke(style: .init(dash: [2]))
            .foregroundStyle(AssetColors.Outline.outlineVariant.swiftUIColor)
        if axis == .horizontal {
            shape.frame(height: 1)
        } else {
            shape.frame(width: 1).padding(0)
        }
    }
}

struct LineShape: Shape {
    let axis: Axis
    
    func path(in rect: CGRect) -> Path {
        var path = Path()
        path.move(to: CGPoint(x: 0, y: 0))
       
        if axis == .horizontal {
            path.addLine(to: CGPoint(x: rect.width, y: 0))
        } else {
            path.addLine(to: CGPoint(x: 0, y: rect.height))
        }
        
        return path
    }
}

fileprivate var bottomTabBarPadding: some View {
    // bottom floating tabbar padding
    Color.clear.padding(.bottom, 60)
}

extension RoomType {
    func toRoom() -> TimetableRoom {
        switch self {
        case .roomI:
            return TimetableRoom(
                id: 1,
                name: MultiLangText(
                    jaTitle: "Iguana",
                    enTitle: "Iguana"
                ),
                type: .roomI,
                sort: 1
            )
        case .roomG:
            return TimetableRoom(
                id: 2,
                name: MultiLangText(
                    jaTitle: "Giraffe",
                    enTitle: "Giraffe"
                ),
                type: .roomG,
                sort: 2
            )
        case .roomH:
            return TimetableRoom(
                id: 3,
                name: MultiLangText(
                    jaTitle: "Hedgehog",
                    enTitle: "Hedgehog"
                ),
                type: .roomH,
                sort: 3
            )
        case .roomF:
            return TimetableRoom(
                id: 4,
                name: MultiLangText(
                    jaTitle: "Flamingo",
                    enTitle: "Flamingo"
                ),
                type: .roomF,
                sort: 4
            )
        case .roomJ:
            return TimetableRoom(
                id: 5,
                name: MultiLangText(
                    jaTitle: "Jellyfish",
                    enTitle: "Jellyfish"
                ),
                type: .roomJ,
                sort: 5
            )
        case .roomIj:
            return TimetableRoom(
                id: 6,
                name: MultiLangText(
                    jaTitle: "Iguana and Jellyfish",
                    enTitle: "Iguana and Jellyfish"
                ),
                type: .roomIj,
                sort: 6
            )
        }
    }
}

extension TimetableTimeGroupItems {
    func getCellForRoom(room: RoomType, cellCount: Int, onTap: @escaping (TimetableItemWithFavorite) -> Void) -> TimetableGridCard? {
        return if let cell = getItem(for: room) {
            TimetableGridCard(timetableItem: cell.timetableItem, cellCount: cellCount) { timetableItem in
                onTap(cell)
            }
        } else {
            nil
        }
    }
}


#Preview {
    TimetableView(
        store: .init(initialState: .init(timetableItems: SampleData.init().workdayResults),
                     reducer: { TimetableReducer() })
    )
}

#Preview {
    TimetableListView(
        store: .init(
            initialState: 
                    .init(timetableItems: SampleData.init().workdayResults),
            reducer: { TimetableReducer() }
        )
    )
}
