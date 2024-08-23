import SwiftUI
import _PhotosUI_SwiftUI
import Theme

struct ProfileCardInputImage: View {
    @State var isPickerPresented = false
    @State var selectedImage: Image?
    @Binding var selectedPhoto: PhotosPickerItem?
    var title: String

    var body: some View {
        VStack(alignment: .leading) {
            Text(title)
                .textStyle(.titleMedium)
                .foregroundStyle(.white)

            if let image = selectedImage {
                ZStack(alignment: .topTrailing) {
                    image
                        .resizable()
                        .frame(width: 120, height: 120)
                        .padding(.top, 12)
                        .padding(.trailing, 17)
                    Button {
                        selectedPhoto = nil
                        selectedImage = nil
                    } label: {
                        Image(.close)
                            .resizable()
                            .renderingMode(.template)
                            .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                            .frame(width: 16, height: 16)
                            .padding(4)
                            .background(AssetColors.Surface.surfaceVariant.swiftUIColor)
                            .clipShape(Circle())
                            .padding(8)
                    }
                }
            } else {
                Button {
                    isPickerPresented = true
                } label: {
                    HStack {
                        Image(.add)
                            .resizable()
                            .renderingMode(.template)
                            .frame(width: 18, height: 18)

                        Text("Add Image", bundle: .module)
                            .textStyle(.labelLarge)
                    }
                    .padding(EdgeInsets(top: 11, leading: 16, bottom: 11, trailing: 24))
                    .foregroundStyle(AssetColors.Primary.primary.swiftUIColor)
                    .overlay(
                        Capsule()
                            .stroke(
                                AssetColors.Outline.outline.swiftUIColor,
                                style: StrokeStyle(lineWidth: 1)
                            )
                    )
                }
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .photosPicker(isPresented: $isPickerPresented, selection: $selectedPhoto)
        .onChange(of: selectedPhoto) { _, newValue in
            newValue?.loadTransferable(type: Image.self) { result in
                switch result {
                case let .success(image):
                    self.selectedImage = image

                case .failure:
                    break
                }
            }
        }
    }
}

#Preview {
    ProfileCardInputTextField(
        title: "Nickname",
        text: .init(get: { "" }, set: {_ in})
    )
}
