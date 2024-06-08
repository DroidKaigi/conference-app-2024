import SwiftUI

struct KeyVisual: View {
    var body: some View {
        VStack(spacing: 12) {
            Rectangle()
                .aspectRatio(282 / 105.14, contentMode:.fit)
                .padding(.horizontal, 49)
                .foregroundStyle(.green)
                .padding(.bottom, 12)
            Text("DroidKaigiはAndroid技術情報の共有とコミュニケーションを目的に開催されるエンジニアが主役のAndroidカンファレンスです。")
                .foregroundStyle(Color(.surfaceOnSurfaceVariant))
                .font(.body)
                .lineSpacing(5)
            VStack(alignment: .leading, spacing: 12) {
                HStack(spacing: 8) {
                    Image(systemName: "clock")
                        .foregroundStyle(Color(.surfaceOnSurfaceVariant))
                        .fontWeight(.bold)
                    Text("日時")
                        .foregroundStyle(Color(.surfaceOnSurfaceVariant))
                        .font(.headline)
                        .padding(.trailing, 4)
                    Text("2023.09.14(木) 〜 16(土) 3日間")
                        .foregroundStyle(Color(.surfaceOnSurface))
                        .font(.callout)
                }
                HStack(spacing: 8) {
                    Image(systemName: "mappin.circle")
                        .foregroundStyle(Color(.surfaceOnSurfaceVariant))
                        .fontWeight(.bold)
                    Text("場所")
                        .foregroundStyle(Color(.surfaceOnSurfaceVariant))
                        .font(.headline)
                        .padding(.trailing, 4)
                    Text("ベルサール渋谷ガーデン")
                        .foregroundStyle(Color(.surfaceOnSurface))
                        .font(.callout)
                    Link("地図を見る", destination: URL(string: "https://2024.droidkaigi.jp/")!)
                        .tint(Color(.primaryPrimary))
                        .fontWeight(.bold)
                }
            }
            .padding(.vertical, 20)
            .padding(.horizontal, 16)
            .background(Color(.keyVisualInformationBase), in: RoundedRectangle(cornerRadius: 10))
        }
    }
}

#Preview {
    VStack {
        Spacer()
        KeyVisual()
            .background(Color(.background))
        Spacer()
    }
    .padding(.horizontal, 16)
}
