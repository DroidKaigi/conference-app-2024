import Foundation

public enum RoomType {
    case roomF
    case roomG
    case roomH
    case roomI
    case roomJ
    case roomIj
    
    enum ThemeKey: String {
        case iguana
        case hedgehog
        case giraffe
        case flamingo
        case jellyfish
    }

    public init(enTitle: String) {
        self = switch enTitle.lowercased() {
        case ThemeKey.flamingo.rawValue: Self.roomF
        case ThemeKey.giraffe.rawValue: Self.roomG
        case ThemeKey.hedgehog.rawValue: Self.roomH
        case ThemeKey.iguana.rawValue: Self.roomI
        case ThemeKey.jellyfish.rawValue: Self.roomJ
        default: Self.roomIj
        }
    }
}
