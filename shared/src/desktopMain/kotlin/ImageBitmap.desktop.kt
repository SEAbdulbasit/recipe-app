import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image
import java.awt.image.BufferedImage
import java.awt.image.ConvolveOp
import java.awt.image.Kernel

actual fun ByteArray.toImageBitmap(): ImageBitmap =
    Image.makeFromEncoded(this).toComposeImageBitmap()

actual fun blurFilter(bitmap: ImageBitmap, context: PlatformContext): ImageBitmap {
    return applyBlurFilter(bitmap.toAwtImage()).toComposeImageBitmap()
}

private fun applyBlurFilter(bitmap: BufferedImage): BufferedImage {
    var result = BufferedImage(bitmap.width, bitmap.height, bitmap.type)
    val graphics = result.graphics

    graphics.drawImage(bitmap, 0, 0, null)
    graphics.dispose()

    val radius = 9
    val size = 9
    val weight: Float = 1.0f / (size * size)
    val matrix = FloatArray(size * size)

    for (i in matrix.indices) {
        matrix[i] = weight
    }

    val kernel = Kernel(radius, size, matrix)
    val op = ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null)
    result = op.filter(result, null)

    return result.getSubimage(
        radius,
        radius,
        result.width - radius * 2,
        result.height - radius * 2
    )
}


actual class PlatformContext

@Composable
actual fun getPlatformContext(): PlatformContext = PlatformContext()