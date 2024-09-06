import Dependencies
@preconcurrency import EventKit
import UIKit

extension EventKitClient: DependencyKey {
    public static var liveValue: Self {
        let eventStore = EKEventStore()

        return Self(
            requestAccessIfNeeded: {
                switch EKEventStore.authorizationStatus(for: .event) {
                case .denied, .restricted:
                    Task.detached { @MainActor in
                        _ = await UIApplication.shared.open(
                            .init(string: UIApplication.openSettingsURLString)!
                        )
                    }
                case .authorized, .fullAccess, .writeOnly:
                        return true
                    case .notDetermined:
                        break
                @unknown default:
                    break
                }
                return try await eventStore.requestWriteOnlyAccessToEvents()
            },
            addEvent: { title, startDate, endDate in
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
        )
    }
}
