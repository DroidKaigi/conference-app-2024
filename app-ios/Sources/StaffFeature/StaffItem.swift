import SwiftUI
import CommonComponents

struct StaffItem: View {
    let data: StaffData
    @State var isPresentSafari = false
    
    var body: some View {
        Button {
            isPresentSafari = true
        } label: {
            StaffLabel(name: data.name, icon: data.icon)
        }
        .padding(.vertical, 12)
        .sheet(isPresented: $isPresentSafari, content: {
            SafariView(url: data.github)
                .ignoresSafeArea()
        })
    }
}

#Preview {
    StaffItem(data: .init(id: 100, name: "Hoge", icon: .init(string: "hoge")!, github: .init(string: "https://localhost:8080")!))
}
