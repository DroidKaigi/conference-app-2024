import SwiftUI
import Model

public struct RoomTypeShape: View {
    let roomType: RoomType
    
    public init(roomType: RoomType) {
        self.roomType = roomType
    }
    
    public var body: some View {
        Group {
            switch roomType {
            case .roomG: Image(.icCircleFill).renderingMode(.template)
            case .roomH: Image(.icDiamondFill).renderingMode(.template)
            case .roomF: Image(.icSharpDiamondFill).renderingMode(.template)
            case .roomI: Image(.icSquareFill).renderingMode(.template)
            case .roomJ: Image(.icTriangleFill).renderingMode(.template)
            case .roomIj: Image(.icSquareFill).renderingMode(.template)
            }
        }
        .foregroundStyle(roomType.theme.primaryColor)
        .frame(width: 12, height: 12)
    }
}

#Preview {
    Group {
        RoomTypeShape(roomType: .roomF)
        RoomTypeShape(roomType: .roomG)
        RoomTypeShape(roomType: .roomH)
        RoomTypeShape(roomType: .roomI)
        RoomTypeShape(roomType: .roomJ)
        RoomTypeShape(roomType: .roomIj)
    }
}
