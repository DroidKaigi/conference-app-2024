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
    
    public var body: some View {
        VStack {
            HStack {
                ForEach(DayTab.allCases) { tabItem in
                    Button(action: {
                        store.send(.view(.selectDay(tabItem)))
                    }, label: {
                        //TODO: Only selected button should be green and underlined
                        Text(tabItem.rawValue).foregroundStyle(
                            AssetColors.Custom.flamingo.swiftUIColor)
                            .underline()
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
                    Button {
                        store.send(.view(.timetableItemTapped))
                    } label: {
                        TimeGroupMiniList(contents: item, onItemTap: { item in
                            store.send(.view(.timetableItemTapped))
                        })
                    }
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
        
        ScrollView{
            Grid {
                GridRow {

                    Color.clear
                        .gridCellUnsizedAxes([.horizontal, .vertical])
                    
                    ForEach(rooms, id: \.self) { column in
                        
                        let room = getTimetableRoom(type: column)
                        
                        Text(room.name.currentLangTitle).foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                    }
                }
                ForEach(store.timetableItems, id: \.self) { item in
                    GridRow {
                        Text(item.startsTimeString).foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
//                        Text("R\(row)")
                        ForEach(rooms, id: \.self) { room in
                            
                            
                            Circle().foregroundStyle(.mint)
                            
                            
                            
//                            let cell = timeRow.items.filter{$0.timetableItem.room == room}.first
//                            
//                            TimetableCard(
//                                timetableItem: cell,
//                                isFavorite: cell.isFavorited,
//                                onTap: { item in
//                                    store.send(.view(.timetableItemTapped))
//                                },
//                                onTapFavorite: { item in
//                                       // Handle favorite tap
//                                })
                            
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

//struct TagView: View {
//    let tagText: String
//    let highlight: Bool
//    
//    var body: some View {
//        HStack {
//            if highlight {
//                Image(systemName: "diamond.fill").resizable().frame(width: 11,height: 11).foregroundStyle(AssetColors.Custom.flamingo.swiftUIColor)
//                    .padding(-3)
//            }
//            Text(tagText).foregroundStyle(highlight ? AssetColors.Custom.flamingo.swiftUIColor : AssetColors.Surface.onSurface.swiftUIColor)
//        }
//        .padding(
//            EdgeInsets(top: 2,leading: 7, bottom: 2, trailing: 7))
//        .border(highlight ? AssetColors.Custom.flamingo.swiftUIColor : AssetColors.Surface.onSurface.swiftUIColor)
//        .padding(-2)
//    }
//}
//
//struct PhotoView: View {
//    //TODO: Replace this with an actual photo render
//    let photo: String
//    let name: String
//    
//    var body: some View {
//        HStack {
//            Image(systemName:photo).resizable().frame(width: 32,height: 32).foregroundStyle(AssetColors.Custom.flamingo.swiftUIColor)
//            Text(name)
//        }
//    }
//}

func getTimetableRoom(type: RoomType) -> TimetableRoom {
    switch (type) {
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
                jaTitle: "All",
                enTitle: "All"
            ),
            type: .roomIj,
            sort: 6
        )
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
