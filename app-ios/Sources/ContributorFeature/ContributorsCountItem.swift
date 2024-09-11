import SwiftUI
import Theme

struct ContributorsCountItem: View {
    let totalContributor: Int
    let duration: Double = 1.0
    @State private var tracker = 0
    
    var body: some View {
        VStack(alignment: .leading) {
            
            Text(String(localized: "Total", bundle: .module))
                .textStyle(.titleMedium)
                .foregroundStyle(AssetColors.Surface.onSurfaceVariant.swiftUIColor)
                .frame(maxWidth: .infinity, alignment: .leading)
            
            HStack {
                Text("\(tracker)")
                    .textStyle(.headlineLarge)
                    .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                    .onAppear() {
                        animate()
                    }
                
                Text(String(localized: "Person", bundle: .module))
                    .textStyle(.headlineSmall)
                    .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
            }
            
            Spacer()
                .frame(height: 16)
            Divider()
        }
    }
    
    func animate() {
        if tracker >= totalContributor { return }

        let interval = duration / Double(totalContributor)
        Timer.scheduledTimer(withTimeInterval: interval, repeats: true) { time in
            tracker += 1
            if tracker == totalContributor {
                time.invalidate()
            }
        }
    }
}

#Preview {
    ContributorsCountItem(totalContributor: 112)
}
