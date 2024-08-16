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
                Text("Grid view placeholder")
                    .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
            }
            Spacer()
        }
        .toast($store.toast)
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
                    }) {
                        store.send(.view(.favoriteTapped($0)))
                    }
                }
            }.scrollContentBackground(.hidden)
            .onAppear {
                store.send(.view(.onAppear))
            }.background(AssetColors.Surface.surface.swiftUIColor)
            // bottom floating tabbar padding
            Color.clear.padding(.bottom, 60)
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
                        onTapFavorite: { _ in
                            onFavoriteTap(item)
                        })
                }
            }
        }.background(Color.clear)
            
    }
}

struct TagView: View {
    let tagText: String
    let highlight: Bool
    
    var body: some View {
        HStack {
            if highlight {
                Image(systemName: "diamond.fill").resizable().frame(width: 11,height: 11).foregroundStyle(AssetColors.Custom.flamingo.swiftUIColor)
                    .padding(-3)
            }
            Text(tagText).foregroundStyle(highlight ? AssetColors.Custom.flamingo.swiftUIColor : AssetColors.Surface.onSurface.swiftUIColor)
        }
        .padding(
            EdgeInsets(top: 2,leading: 7, bottom: 2, trailing: 7))
        .border(highlight ? AssetColors.Custom.flamingo.swiftUIColor : AssetColors.Surface.onSurface.swiftUIColor)
        .padding(-2)
    }
}

struct PhotoView: View {
    let photo: String
    let name: String
    
    var body: some View {
        HStack {
            Image(systemName:photo).resizable().frame(width: 32,height: 32).foregroundStyle(AssetColors.Custom.flamingo.swiftUIColor)
            Text(name)
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
