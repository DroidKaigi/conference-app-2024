import Model
import SwiftUI
import Theme

public struct SelectionChips<SelectableCase: Selectable>: View where SelectableCase.AllCases: RandomAccessCollection {
    @Binding public var selected: SelectableCase?
    public var notSelectedTitle: String?
    public let options: [SelectableCase]

    public init(
        selected: Binding<SelectableCase?>,
        notSelectedTitle: String? = nil,
        options: [SelectableCase] = SelectableCase.allCases as! [SelectableCase]
    ) {
            self._selected = selected
            self.notSelectedTitle = notSelectedTitle
            self.options = options
        }

    public var body: some View {
        ScrollView(.horizontal) {
            HStack(spacing: 6) {
                if let notSelectedTitle {
                    SelectionChip(
                        title: notSelectedTitle,
                        isMultiSelect: false,
                        isSelected: selected == nil
                    ) {
                        selected = nil
                    }
                }

                ForEach(options) { selection in
                    SelectionChip(
                        title: selection.caseTitle,
                        isMultiSelect: false,
                        isSelected: selected == selection
                    ) {
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
    public let isMultiSelect: Bool
    public let isSelected: Bool
    public let onTap: () -> Void

    public init(title: String, isMultiSelect: Bool, isSelected: Bool, onTap: @escaping () -> Void) {
        self.title = title
        self.isMultiSelect = isMultiSelect
        self.isSelected = isSelected
        self.onTap = onTap
    }

    public var body: some View {
        Button {
            onTap()
        } label: {
            let foregroundColor = isSelected
                ? AssetColors.Secondary.onSecondaryContainer.swiftUIColor
                : AssetColors.Surface.onSurfaceVariant.swiftUIColor
            let backgroundColor = isSelected
                ? AssetColors.Secondary.secondaryContainer.swiftUIColor
                : Color.clear
            let padding = isSelected
                ? EdgeInsets(top: 6, leading: 8, bottom: 6, trailing: 16)
                : EdgeInsets(top: 6, leading: 16, bottom: 6, trailing: 16)

            HStack {
                if isSelected {
                    Image(.icCheck)
                        .renderingMode(.template)
                        .resizable()
                        .frame(width: 18, height: 18)
                }

                Text(title)
                    .textStyle(.labelLarge)

                if isMultiSelect {
                    Image(.arrowDropDown)
                        .renderingMode(.template)
                }
            }
            .padding(padding)
            .foregroundStyle(foregroundColor)
            .background(backgroundColor)
            .clipShape(RoundedRectangle(cornerRadius: 8))
            .overlay {
                if !isSelected {
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(AssetColors.Outline.outline.swiftUIColor, lineWidth: 1)
                }
            }
        }
        .animation(.easeInOut, value: isSelected)
    }
}

#Preview {
    SelectionChip(title: "Title", isMultiSelect: false, isSelected: false) {}
}
