import ComposableArchitecture
import SwiftUI
import Theme

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
                Text("Grid view placeholder")
                    .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
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
                        // TODO: Search?
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
                        TimeGroupMiniList(contents: item)
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
                    ListViewItem(listItem: item)
                }
            }
        }.background(Color.clear)
            
    }
}

struct ListViewItem: View {
    let listItem: TimetableItem
    
    public var body: some View {
        VStack(alignment: .leading) {
            HStack {
                TagView(tagText: listItem.room, highlight: true)
                ForEach(listItem.languages, id: \.self) { lang in
                    TagView(tagText: lang, highlight: false)
                }
                Spacer()
                Image(systemName: listItem.isFavorite ? "heart.fill" : "heart").foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
            }
            Text(listItem.title).font(.title)
            ForEach(listItem.speakers, id: \.self){ speaker in
                PhotoView(photo:"person.circle.fill",
                          name: speaker)
            }
            
            
        }.foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor).padding(10)
            .overlay(
                RoundedRectangle(cornerRadius: 5)
                    .stroke(AssetColors.Surface.onSurface.swiftUIColor, lineWidth: 1)
            )
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
    //TODO: Replace this with an actual photo render
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
