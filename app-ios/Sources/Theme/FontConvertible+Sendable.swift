#if hasFeature(RetroactiveAttribute)
extension FontConvertible: @retroactive @unchecked Sendable {}
#else
extension FontConvertible: @unchecked Sendable {}
#endif
