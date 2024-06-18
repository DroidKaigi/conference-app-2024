import SwiftUI

struct SessionDescriptionView: View {
    @State private var isDescriptionExpanded: Bool = false
    @State private var canBeExpanded: Bool = false
    let content: String

    init(content: String) {
        self.content = content
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text(content)
                .textSelection(.enabled)
                .lineLimit(isDescriptionExpanded ? nil : 5)
                .font(.callout)
                .foregroundStyle(Color(.surfaceVariant))
                .background {
                    ViewThatFits(in: .vertical) {
                        Text(content)
                            .hidden()
                        // Just for receiving onAppear event if the description exceeds its line limit
                        Color.clear
                            .onAppear {
                                canBeExpanded = true
                            }
                    }
                }
            if canBeExpanded {
                Button {
                    isDescriptionExpanded = true
                    canBeExpanded = false
                } label: {
                    Text(String(localized: "TimeTableDetailReadMore", bundle: .module))
                        .foregroundStyle(Color(.primary))
                        .frame(width: 120, height: 40, alignment: .center)
                        .overlay {
                            Capsule()
                                .stroke(Color(.outline))
                        }
                }
            }
        }
    }
}

#Preview {
    VStack {
        SessionDescriptionView(content: SampleData.sessionDescription)
            .padding(.horizontal, 16)
    }
    .background(Color(.background))
}
