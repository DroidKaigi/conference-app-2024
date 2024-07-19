import ComposableArchitecture
import SwiftUI

public struct TimetableView: View {
    private let store: StoreOf<TimetableReducer>

    public init(store: StoreOf<TimetableReducer>) {
        self.store = store
    }
    
    @State var timetableMode = TimetableMode.List
    @State var switchModeIcon: String = "square.grid.2x2"
    
    public var body: some View {
        NavigationView {
            VStack {
                HStack {
                    ForEach(DayTab.allCases) { tabItem in
                        Button(action: {
                            store.send(.view(.selectDay(tabItem)))
                        }, label: {
                            //TODO: Only selected button should be green and underlined
                            Text(tabItem.rawValue).foregroundStyle(Color(.greenSelectColorset))
                                .underline()
                        })
                    }
                    Spacer()
                }.padding(5)
                switch timetableMode {
                case TimetableMode.List:
                    TimetableListView(store: store)
                case TimetableMode.Grid:
                    Text("Grid view placeholder")
                        .foregroundStyle(Color(.onSurfaceColorset))
                }
                Spacer()
            }
            .background(Color(.backgroundColorset))
            .frame(maxWidth: .infinity)
            .toolbar{
                ToolbarItem(placement: .topBarLeading) {
                    Text("Timetable")
                        .font(.title)
                        .foregroundStyle(Color(.onSurfaceColorset))
                    
                }
                ToolbarItem(placement:.topBarTrailing) {
                    HStack {
                        Button {
                            // TODO: Search?
                        } label: {
                            Group {
                                Image(systemName:"magnifyingglass").foregroundStyle(Color(.onSurfaceColorset))
                            }
                            .frame(width: 40, height: 40)
                        }
                        
                        Button {
                            switch timetableMode {
                            case .List:
                                timetableMode = .Grid
                                switchModeIcon = "list.bullet.indent"
                            case .Grid:
                                timetableMode = .List
                                switchModeIcon = "square.grid.2x2"
                            }
                        } label: {
                            Image(systemName:switchModeIcon).foregroundStyle(Color(.onSurfaceColorset))
                            .frame(width: 40, height: 40)
                        }
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
                }.background(Color(.backgroundColorset))
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
            }.padding(10).foregroundStyle(Color(.onSurfaceColorset))
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
                Image(systemName: listItem.isFavorite ? "heart.fill" : "heart").foregroundStyle(Color(.onSurfaceColorset))
            }
            Text(listItem.title).font(.title)
            ForEach(listItem.speakers, id: \.self){ speaker in
                PhotoView(photo:"person.circle.fill",
                          name: speaker)
            }
            
            
        }.foregroundStyle(Color(.onSurfaceColorset)).padding(10)
            .overlay(
                RoundedRectangle(cornerRadius: 5)
                    .stroke(Color(.onSurfaceColorset), lineWidth: 1)
            )
    }
}

struct TagView: View {
    let tagText: String
    let highlight: Bool
    var body: some View {
        HStack {
            if highlight {
                Image(systemName: "diamond.fill").resizable().frame(width: 11,height: 11).foregroundStyle(Color(.greenSelectColorset))
                    .padding(-3)
            }
            Text(tagText).foregroundStyle(highlight ? Color(.greenSelectColorset) : Color(.onSurfaceColorset))
        }
        .padding(
            EdgeInsets(top: 2,leading: 7, bottom: 2, trailing: 7))
        .border(highlight ? Color(.greenSelectColorset) : Color(.onSurfaceColorset))
        .padding(-2)
    }
}

struct PhotoView: View {
    //TODO: Replace this with an actual photo render
    let photo: String
    let name: String
    
    var body: some View {
        HStack {
            Image(systemName:photo).resizable().frame(width: 32,height: 32).foregroundStyle(Color(.greenSelectColorset))
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
