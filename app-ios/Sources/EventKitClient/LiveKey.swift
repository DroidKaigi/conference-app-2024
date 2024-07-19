import Dependencies
import EventKit
import UIKit

extension EventKitClient: DependencyKey {
    public static let liveValue: Self = Self {
        switch EKEventStore.authorizationStatus(for: .event) {
        case .denied, .restricted:
            Task.detached { @MainActor in
                _ = await UIApplication.shared.open(URL(string: UIApplication.openSettingsURLString)!)
            }
        case .authorized, .fullAccess, .writeOnly:
            return true
        case .notDetermined:
            break
        @unknown default:
            break
        }
        return try await eventStore.requestWriteOnlyAccessToEvents()
    } addEvent: { title, startDate, endDate in
        guard let defaultCalendar = eventStore.defaultCalendarForNewEvents else {
            return
        }
        let event = EKEvent(eventStore: eventStore)
        event.title = title
        event.startDate = startDate
        event.endDate = endDate
        event.calendar = defaultCalendar

        try eventStore.save(event, span: .thisEvent)
    }
}
