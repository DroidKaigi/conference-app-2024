//
//  File.swift
//  
//
//  Created by 日野森寛也 on 2024/07/25.
//

import Foundation

extension Array {
    func split(_ amount: Int) -> [[Element]] {
        return stride(from: 0, to: count, by: amount)
            .map { Self(self[$0..<Swift.min($0 + amount, count)]) }
    }
}
