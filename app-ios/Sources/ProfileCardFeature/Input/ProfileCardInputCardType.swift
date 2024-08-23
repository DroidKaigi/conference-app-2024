import shared
import SwiftUI
import Theme

struct ProfileCardInputCardType: View {
    @Binding var selectedCardType: ProfileCardType?

    let columns = [
        GridItem(.flexible()),
        GridItem(.flexible())
    ]

    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text("Select Theme", bundle: .module)
                .textStyle(.titleMedium)
                .foregroundStyle(.white)

            LazyVGrid(columns: columns, spacing: 12) {
                ForEach(ProfileCardType.allCases, id: \.self) { cardType in
                    Button {
                        selectedCardType = cardType
                    } label: {
                        cardType.color
                            .aspectRatio(184 / 112, contentMode: .fill)
                            .overlay {
                                Image(.logo)
                            }
                            .cornerRadius(2)
                            .overlay {
                                if let selectedCardType, selectedCardType == cardType {
                                    ZStack(alignment: .topTrailing) {
                                        RoundedRectangle(cornerRadius: 2)
                                            .stroke(
                                                AssetColors.Surface.surfaceTint.swiftUIColor,
                                                lineWidth: 3
                                            )


                                        TopTrailingTriangle()
                                            .foregroundStyle(AssetColors.Surface.surfaceTint.swiftUIColor)
                                            .frame(width: 44, height: 44)

                                        Image(.check)
                                            .resizable()
                                            .renderingMode(.template)
                                            .frame(width: 16, height: 16)
                                            .padding(2)
                                            .foregroundStyle(AssetColors.Primary.onPrimary.swiftUIColor)
                                            .background(.white)
                                            .clipShape(Circle())
                                            .padding(4)
                                    }
                                }
                            }
                    }
                }
            }
        }
    }
}

#Preview {
    ProfileCardInputCardType(selectedCardType: .constant(nil))
}

extension ProfileCardType {
    var color: Color {
        switch self {
        case .flamingo:
            return AssetColors.Custom.flamingo.swiftUIColor

        case .giraffe:
            return AssetColors.Custom.giraffe.swiftUIColor

        case .hedgehog:
            return AssetColors.Custom.hedgehog.swiftUIColor

        case .iguana:
            return AssetColors.Custom.iguana.swiftUIColor

        case .jellyfish:
            return AssetColors.Custom.jellyfish.swiftUIColor

        case .none:
            return .clear
        }
    }
}

private struct TopTrailingTriangle: Shape {
    func path(in rect: CGRect) -> Path {
        Path { path in
            path.move(to: CGPoint(x: rect.minX, y: rect.minY))
            path.addLine(to: CGPoint(x: rect.maxX, y: rect.maxY))
            path.addLine(to: CGPoint(x: rect.maxX, y: rect.minY))
            path.closeSubpath()
        }
    }
}

