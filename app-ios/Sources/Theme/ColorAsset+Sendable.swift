#if hasFeature(RetroactiveAttribute)
extension ColorAsset: @retroactive @unchecked Sendable {}
#else
extension ColorAsset: @unchecked Sendable {}
#endif
