import SwiftUI
import Theme

public struct ToastState: Equatable {
    public let text: String
    
    public init(text: String) {
        self.text = text
    }
}

struct ToastModifier: ViewModifier {
    @Binding var item: ToastState?
    @State private var task: Task<Void, Never>? = nil
    private let animationDelay = 1.0

    func body(content: Content) -> some View {
        ZStack {
            content

            if item != nil {
                ToastView(item: $item)
                    .zIndex(1)
                    .transition(.move(edge: .bottom).combined(with: .opacity))
                    .onTapGesture {
                        item = nil
                    }
            }
        }
        .onChange(of: item) {
            task?.cancel()
            if item != nil {
                task = Task {
                    try? await Task.sleep(for: .seconds(4))
                    withAnimation(.easeInOut(duration: animationDelay)) {
                        item = nil
                    }
                }
            }
        }
        .animation(.easeInOut(duration: animationDelay), value: item)
    }
}

struct ToastView: View {
    @Binding var item: ToastState?
    var body: some View {
        ZStack {
            Text(item?.text ?? "")
                .textStyle(.bodyMedium)
                .foregroundColor(AssetColors.Inverse.inverseOnSurface.swiftUIColor)
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal, 16)
                .padding(.vertical, 14)
                .background(AssetColors.Inverse.inverseSurface.swiftUIColor)
                .padding(.horizontal, 12)
                .clipShape(RoundedRectangle(cornerRadius: 4))
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .bottom)
        .padding(.vertical, 16)
    }
}

extension View {
    public func toast(_ item: Binding<ToastState?>) -> some View {
        modifier(ToastModifier(item: item))
    }
}
