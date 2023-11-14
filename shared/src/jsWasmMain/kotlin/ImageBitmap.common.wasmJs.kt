import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.*

actual fun ByteArray.toImageBitmap(): ImageBitmap {
    return Image.makeFromEncoded(this).toComposeImageBitmap()
}

@Composable
actual fun getPlatformContext(): PlatformContext {
    return PlatformContext()
}

actual class PlatformContext

actual fun blurFilter(
    bitmap: ImageBitmap,
    context: PlatformContext
): ImageBitmap {
    return applyBlurFilter(bitmap.asSkiaBitmap()).asComposeImageBitmap()
}

fun applyBlurFilter(bitmap: Bitmap): Bitmap {
    val result = Bitmap().apply {
        allocN32Pixels(bitmap.width, bitmap.height)
    }
    val blur = Paint().apply {
        imageFilter = ImageFilter.makeBlur(3f, 3f, FilterTileMode.CLAMP)
    }

    val canvas = Canvas(result)
    canvas.saveLayer(null, blur)
    canvas.drawImageRect(Image.makeFromBitmap(bitmap), bitmap.bounds.toRect())
    canvas.restore()
    canvas.readPixels(result, 0, 0)
    canvas.close()

    return result
}
