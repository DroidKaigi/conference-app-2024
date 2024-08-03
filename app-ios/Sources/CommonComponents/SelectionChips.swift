import SwiftUI
import Theme

public protocol Selectable: CaseIterable, Equatable, Identifiable {
    var title: String { get }
}

public struct SelectionChips<SelectableCase: Selectable>: View where SelectableCase.AllCases: RandomAccessCollection {
    @Binding public var selected: SelectableCase?
    public var notSelectedTitle: String?

    public init(selected: Binding<SelectableCase?>, notSelectedTitle: String? = nil) {
        self._selected = selected
        self.notSelectedTitle = notSelectedTitle
    }

    public var body: some View {
        ScrollView(.horizontal) {
            HStack(spacing: 6) {
                if let notSelectedTitle {
                    SelectionChip(title: notSelectedTitle, isSelected: selected == nil) {
                        selected = nil
                    }
                }

                ForEach(SelectableCase.allCases) { selection in
                    SelectionChip(title: selection.title, isSelected: selected == selection) {
                        selected = selection
                    }
                }
            }
            .padding(.horizontal, 16)
            .padding(.top, 8)
            .padding(.bottom, 12)
        }
    }
}

public struct SelectionChip: View {
    public let title: String
    public let isSelected: Bool
    public let onTap: () -> Void

    public init(title: String, isSelected: Bool, onTap: @escaping () -> Void) {
        self.title = title
        self.isSelected = isSelected
        self.onTap = onTap
    }

    public var body: some View {
        Button {
            onTap()
        } label: {
            if isSelected {
                HStack {
                    Image(.icCheck)
                        .renderingMode(.template)
                        .resizable()
                        .frame(width: 18, height: 18)

                    Text(title)
                        .textStyle(.labelLarge)
                }
                .foregroundStyle(AssetColors.Secondary.onSecondaryContainer.swiftUIColor)
                .padding(EdgeInsets(top: 6, leading: 8, bottom: 6, trailing: 16))
                .background(AssetColors.Secondary.secondaryContainer.swiftUIColor)
                .clipShape(RoundedRectangle(cornerRadius: 8))
            } else {
                Text(title)
                    .textStyle(.labelLarge)
                    .foregroundStyle(AssetColors.Surface.onSurfaceVariant.swiftUIColor)
                    .padding(.horizontal, 16)
                    .padding(.vertical, 6)
                    .background(AssetColors.Surface.surface.swiftUIColor)
                    .overlay {
                        RoundedRectangle(cornerRadius: 8)
                            .stroke(AssetColors.Outline.outline.swiftUIColor, lineWidth: 1)
                    }
            }
        }
        .animation(.easeInOut, value: isSelected)
    }
}

#Preview {
    SelectionChip(title: "Title", isSelected: false) {}
}
