import SwiftUI

extension View {
    @MainActor
    public func navigationBarTitleStyle(color: Color, titleTextStyle: TextStyle, largeTitleTextStyle: TextStyle) -> some View {
        let uiColor = UIColor(color)
        UINavigationBar.appearance().titleTextAttributes = [
            .foregroundColor: uiColor,
            .font: titleTextStyle.font
        ]
        UINavigationBar.appearance().largeTitleTextAttributes = [
            .foregroundColor: uiColor,
            .font: largeTitleTextStyle.font
        ]
        return self
    }
}
