import ComposableArchitecture
import CommonComponents
import SwiftUI
import Theme
import shared

public struct TimetableView: View {
    private let store: StoreOf<TimetableReducer>
    public init(store: StoreOf<TimetableReducer>) {
        self.store = store
    }
    
    @State var timetableMode = TimetableMode.list
    @State var switchModeIcon: String = "square.grid.2x2"
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
                Text("Timetable")
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
                            switchModeIcon = "list.bullet.indent"
                        case .grid:
                            timetableMode = .list
                            switchModeIcon = "square.grid.2x2"
                        }
                    } label: {
                        Image(systemName:switchModeIcon).foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
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
                    })
                }
            }.scrollContentBackground(.hidden)
            .onAppear {
                store.send(.onAppear)
            }.background(AssetColors.Surface.surface.swiftUIColor)
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
                        Text(room.name.currentLangTitle).foregroundStyle(room.roomTheme.primaryColor)
                            .frame(width: 192)
                    }
                }
                ForEach(store.timetableItems, id: \.self) { timeBlock in
                    GridRow {
                        VStack {
                            Text(timeBlock.startsTimeString).foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                            Spacer()
                            
                        }.frame(height: 153)
                        
                        
                        ForEach(rooms, id: \.self) { room in
                            
                            timeBlock.getCellForRoom(room: room, onTap: { item in
                                store.send(.view(.timetableItemTapped(item)))
                            })
                        }
                    }
                }
            }
        }
        
    }
}

struct TimeGroupMiniList: View {
    let contents: TimetableTimeGroupItems
    let onItemTap: (TimetableItemWithFavorite) -> Void
    
    var body: some View {
        HStack {
            VStack {
                Text(contents.startsTimeString)
                Text("|")
                Text(contents.endsTimeString)
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
                        onTapFavorite: {_ in
                        })
                }
            }
        }.background(Color.clear)
            
    }
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
    func getCellForRoom(room: RoomType, onTap: @escaping (TimetableItemWithFavorite) -> Void) -> TimetableGridCard {
        return if let cell = getItemForRoom(forRoom: room) {
            TimetableGridCard(timetableItem: cell.timetableItem) { timetableItem in
                onTap(cell)
            }
        } else {
            TimetableGridCard(timetableItem: nil) { _ in
                // Does nothing / Space holder card
            }
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
