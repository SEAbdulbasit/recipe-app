import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

expect fun ByteArray.toImageBitmap(): ImageBitmap

expect fun blurFilter(bitmap: ImageBitmap, context: PlatformContext): ImageBitmap

expect class PlatformContext

@Composable
expect fun getPlatformContext(): PlatformContext

