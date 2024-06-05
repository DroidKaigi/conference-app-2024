import ComposableArchitecture
import SwiftUI

/// View of Timetable Screen.
public struct TimetableView: View {
    private let store: StoreOf<TimetableReducer>

    public init(store: StoreOf<TimetableReducer>) {
        self.store = store
    }
    
    public var body: some View {
        VStack {
            HStack {
                ForEach(DayTab.allCases) { tabItem in
                    Button(action: {
                        store.send(.selectDay(tabItem))
                    }, label: {
                        Text(tabItem.rawValue).foregroundColor(.green)
                            .underline()
                    })
                }
                Spacer()
            }.padding(5)
            TimetableListView(store: store)
            Spacer()
        }
    }
}

/// A list of conference items
public struct TimetableListView: View {
    private let store: StoreOf<TimetableReducer>

    public init(store: StoreOf<TimetableReducer>) {
        self.store = store
    }

    public var body: some View {
        ForEach(store.timetableItems, id: \.self) { item in
            ListViewItem(listItem: item)
        }
        .onAppear {
            store.send(.onAppear)
        }
    }
}

///A single conference list item
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
                Image(systemName: listItem.isFavorite ? "heart.fill" : "heart").foregroundColor(Color.gray)
            }
            Text(listItem.title).font(.title)
            PhotoView(photo:"person.circle.fill",
                      name: listItem.speaker)
            
        }.padding(10)
            .overlay(
                RoundedRectangle(cornerRadius: 5)
                    .stroke(Color.gray, lineWidth: 1)
            )
    }
}

/// Information tag for a speech (room, language, etc)
struct TagView: View {
    let tagText: String
    let highlight: Bool
    public var body: some View {
        HStack {
            if highlight {
                Image(systemName: "diamond.fill").resizable().frame(width: 11,height: 11).foregroundColor(.green)
                    .padding(-3)
            }
            Text(tagText).foregroundColor (highlight ? Color.green : Color.gray)
        }.padding(EdgeInsets(top: 2,leading: 7,bottom: 2,trailing: 7)).border(highlight ? Color.green : Color.gray).padding(-2)
    }
}

// Photo view for Speakers, etc
struct PhotoView: View {
    //TODO: Replace this with an actual photo render
    let photo: String
    let name: String
    
    public var body: some View {
        HStack {
            Image(systemName:photo).resizable().frame(width: 32,height: 32).foregroundColor(.green)
            Text(name)
        }
    }
}

#Preview {
    TimetableView(
        store: .init(initialState: .init(timetableItems: SampleData.init().day1Data),
                     reducer: { TimetableReducer() })
    )
}

#Preview {
    TimetableListView(
        store: .init(
            initialState: 
                    .init(timetableItems: SampleData.init().day1Data),
            reducer: { TimetableReducer() }
        )
    )
}
