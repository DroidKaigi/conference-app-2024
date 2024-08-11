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
                TimeTableGridView(store: store)
//                Text("Grid view placeholder")
//                    .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
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
                        },
                        onFavoriteTap: { item in
                            store.send(.view(.favoriteItemTapped))
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

struct TimeGroupMiniList: View {
    let contents: TimetableTimeGroupItems
    let onItemTap: (TimetableItemWithFavorite) -> Void
    let onFavoriteTap: (TimetableItemWithFavorite) -> Void
    
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
                            onFavoriteTap(item)
                        })
                }
            }
        }.background(Color.clear)
            
    }
}

//TODO:  Figure out sorting and displaying grid items...
struct TimeTableGridView: View {
    private let store: StoreOf<TimetableReducer>

    public init(store: StoreOf<TimetableReducer>) {
        self.store = store
    }
    
    var body: some View {
            
        ScrollView([.vertical]) {
            Grid {
                GridRow {
                    Color.clear
                        .gridCellUnsizedAxes([.horizontal, .vertical])
                    ForEach(1..<4) { column in
                        Text("C\(column)")
                    }
                }
                ForEach(1..<4) { row in
                    GridRow {
                        Text("R\(row)")
                        ForEach(1..<4) { _ in
                            Circle().foregroundStyle(.mint)
                        }
                    }
                }
                

                
                
//                ForEach(store.timetableItems, id: \.self) { item in
//                    HStack {
//                        VStack {
//                            Text(item.startsTimeString)
//                            Spacer()
//                        }.padding(10).foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
//                        ScrollView([.horizontal]) {
//                            GridRow {
//                                ForEach(item.items) { rowItem in
//                                    TimetableCard(
//                                        timetableItem: item.timetableItem,
//                                        isFavorite: item.isFavorited,
//                                        onTap: {_ in
//                                            onItemTap(item)
//                                        },
//                                        onTapFavorite: {_ in
//                                            onFavoriteTap(item)
//                                        })
////                                    TimetableGridItem(listItem: rowItem)
//                                }
//                            }//.frame(width: 192, height: 153)
//                        }
//                    }
//                }
            }
        }
    }
}

////TODO: Figure out best way to handle room selection, and all rooms (fills width)
//struct TimetableGridItem: View {
//    let listItem: TimetableItem
//    
//    var body: some View {
//        VStack {
//            HStack {
//                //TODO: Replace shape with our actual shapes
//                Image(systemName: "diamond.fill").resizable().frame(width: 11,height: 11).foregroundStyle(AssetColors.Custom.arcticFox.swiftUIColor)
//                    .padding(EdgeInsets(top: 0,leading: 7, bottom: 0, trailing: 2))
//                Text("\(listItem.startsAt.formatted(.dateTime.hour().minute())) - \(listItem.endsAt.formatted(.dateTime.hour().minute()))").foregroundStyle(listItem.room.getForegroundColor())
//                Spacer()
//                
//            }
//            Text(listItem.title).foregroundStyle(listItem.room.getForegroundColor())
//            Spacer()
//            ForEach(listItem.speakers, id: \.self){ speaker in
//                PhotoView(photo:"person.circle.fill",
//                          name: speaker,
//                          color: AssetColors.Surface.onSurface.swiftUIColor)
//            }
//        }
//        .padding(
//            EdgeInsets(top: 10,leading: 10, bottom: 10, trailing: 10))
//        .border(listItem.room.getForegroundColor())
//        .frame(width: listItem.room==Room.allRooms ? .infinity : 192, height: 153, alignment: .center)
//        .padding(5)
//        .background(listItem.room.getBackgroundColor())
//    }
//}

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

//#Preview {
//    ZStack {
//        Color(AssetColors.Surface.surface.swiftUIColor)
//
//        TimetableGridItem(
//            listItem: TimetableItemWithFavorite(timetableItem: TimetableItem.Session.companion.fake(), isFavorited: false)
//        )
//    }
//}

#Preview {
    ZStack {
        Color(AssetColors.Surface.surface.swiftUIColor)

        TimeTableGridView(
            store: .init(
                initialState:
                        .init(timetableItems: SampleData.init().workdayResults),
                reducer: { TimetableReducer() }
            )
        )
    }
}
