//
//  AboutView.swift
//
//
//  Created by 日野森寛也 on 2024/05/23.
//

import ComposableArchitecture
import SwiftUI

public struct AboutView: View {
    private let store: StoreOf<AboutCore>

    public init(store: StoreOf<AboutCore>) {
        self.store = store
    }

    public var body: some View {
        Text(store.text)
            .onAppear {
                store.send(.onAppear)
            }
    }
}

#Preview {
    AboutView(store: .init(initialState: .init(text: "Hoge"), reducer: { AboutCore() }))
}
