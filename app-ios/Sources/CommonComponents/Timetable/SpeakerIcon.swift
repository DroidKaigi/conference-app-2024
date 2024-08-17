import Foundation
import SwiftUI

private actor SpeakerIconInMemoryCache {
    static let shared = SpeakerIconInMemoryCache()
    private init() {}
    
    private var cache: [String: Data] = [:]
    
    func data(urlString: String) -> Data? {
        return cache[urlString]
    }
    
    func set(data: Data, urlString: String) {
        cache[urlString] = data
    }
}

struct SpeakerIcon: View {
    let urlString: String
    @State private var iconData: Data?
    
    init(urlString: String) {
        self.urlString = urlString
    }
    
    var body: some View {
        Group {
            if let data = iconData,
               let uiImage = UIImage(data: data) {
                Image(uiImage: uiImage)
                    .resizable()
            } else {
                Circle().stroke(Color.gray)
            }
        }
        .frame(width: 32, height: 32)
        .clipShape(Circle())
        .task {
            if let data = await SpeakerIconInMemoryCache.shared.data(urlString: urlString) {
                iconData = data
                return
            }
            
            guard let url = URL(string: urlString) else {
                return
            }
            let urlRequest = URLRequest(url: url, cachePolicy: .useProtocolCachePolicy)
            if let (data, _) = try? await URLSession.shared.data(for: urlRequest) {
                iconData = data
                await SpeakerIconInMemoryCache.shared.set(data: data, urlString: urlString)
            }
        }
    }
}

#Preview {
    SpeakerIcon(urlString: "https://github.com/mltokky.png")
}
