import shared
import Foundation

extension Kotlinx_datetimeInstant {
    func toDate() -> Date {
        Date(timeIntervalSince1970: TimeInterval(epochSeconds))
    }
}
