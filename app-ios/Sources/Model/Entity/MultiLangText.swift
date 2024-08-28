import Foundation

public struct MultiLangText {
    public let currentLangTitle: String
    public let enTitle: String
    public let jaTitle: String
    
    public init(currentLangTitle: String, enTitle: String, jaTitle: String) {
        self.currentLangTitle = currentLangTitle
        self.enTitle = enTitle
        self.jaTitle = jaTitle
    }
}
