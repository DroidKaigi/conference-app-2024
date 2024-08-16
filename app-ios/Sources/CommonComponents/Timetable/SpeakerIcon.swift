import SwiftUI

struct SpeakerIcon: View {
    let urlString: String
    @State private var iconData: Data?
    
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
            guard let url = URL(string: urlString) else {
                return
            }
            let urlRequest = URLRequest(url: url)
            if let cachedResponse = URLCache.shared.cachedResponse(for: urlRequest) {
                iconData = cachedResponse.data
                return
            }
            
            do {
                let (data, response) = try await URLSession.shared.data(for: urlRequest)
                URLCache.shared.storeCachedResponse(CachedURLResponse(response: response, data: data), for: urlRequest)
                iconData = data
            } catch {
                iconData = nil
            }
        }
    }
}

#Preview {
    SpeakerIcon(urlString: "https://github.com/mltokky.png")
}
