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
    
    @State var timetableMode = TimetableMode.list
    @State var switchModeIcon: ImageResource = .icGrid
    @State var selectedTab: DayTab = DayTab.day1
    
    public var body: some View {
        VStack {
            HStack {
                ForEach(DayTab.allCases) { tabItem in
                    Button(action: {
                        store.send(.view(.selectDay(tabItem)))
                        selectedTab = tabItem
                    }, label: {
                        HStack(spacing: 6) {
                            Text(tabItem.rawValue).textStyle(.titleMedium).underline(selectedTab == tabItem)
                        }
                        .foregroundStyle(selectedTab == tabItem ? AssetColors.Custom.iguana.swiftUIColor : AssetColors.Surface.onSurface.swiftUIColor)
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

struct TimetableListView: View {
    private let store: StoreOf<TimetableReducer>

    public init(store: StoreOf<TimetableReducer>) {
        self.store = store
    }

    var body: some View {
        ScrollView{
            LazyVStack {
                ForEach(store.timetableItems, id: \.self) { item in
                    TimeGroupMiniList(contents: item, onItemTap: { item in
                        store.send(.view(.timetableItemTapped(item)))
                    }) {
                        store.send(.view(.favoriteTapped($0)))
                    }
                }
            }.scrollContentBackground(.hidden)
            .onAppear {
                store.send(.view(.onAppear))
            }.background(AssetColors.Surface.surface.swiftUIColor)
            
            bottomTabBarPadding
        }
    }
}

struct TimetableGridView: View {
    private let store: StoreOf<TimetableReducer>
    public init(store: StoreOf<TimetableReducer>) {
        self.store = store
    }

    var body: some View {
        let rooms = RoomType.allCases.filter {$0 != RoomType.roomIj}
        
        ScrollView([.horizontal, .vertical]) {
            Grid {
                GridRow {
                    Color.clear
                        .gridCellUnsizedAxes([.horizontal, .vertical])
                    
                    ForEach(rooms, id: \.self) { column in
                        let room = column.toRoom()
                        Text(room.name.currentLangTitle).foregroundStyle(room.roomTheme.primaryColor).textStyle(.titleMedium)
                            .frame(width: 192)
                    }
                }
                ForEach(store.timetableItems, id: \.self) { timeBlock in
                    GridRow {
                        VStack {
                            Text(timeBlock.startsTimeString).foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor).textStyle(.labelMedium)
                            Spacer()
                            
                        }.frame(height: 153)
                        
                        ForEach(rooms, id: \.self) { room in
                            
                            if let cell = timeBlock.getCellForRoom(room: room, onTap: { item in
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
            }
            .padding(.trailing)
            
            bottomTabBarPadding
        }
    }
}

struct TimeGroupMiniList: View {
    let contents: TimetableTimeGroupItems
    let onItemTap: (TimetableItemWithFavorite) -> Void
    let onFavoriteTap: (TimetableItemWithFavorite) -> Void
    
    var body: some View {
        HStack {
            VStack {
                Text(contents.startsTimeString).textStyle(.titleMedium)
                Text("|").font(.system(size: 8))
                Text(contents.endsTimeString).textStyle(.titleMedium)
                Spacer()
            }.padding(10).foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
            VStack {
                ForEach(contents.items, id: \.self) { item in
                    TimetableCard(
                        timetableItem: item.timetableItem,
                        isFavorite: item.isFavorited,
                        onTap: {_ in
                            onItemTap(item)
                        },
                        onTapFavorite: { _ in
                            onFavoriteTap(item)
                        })
                }
            }
        }.background(Color.clear)
            
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
    func getCellForRoom(room: RoomType, onTap: @escaping (TimetableItemWithFavorite) -> Void) -> TimetableGridCard? {
        return if let cell = getItem(for: room) {
            TimetableGridCard(timetableItem: cell.timetableItem) { timetableItem in
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
