import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
actual class RecipesList actual constructor() : Screen, Parcelable

@Parcelize
actual class RecipeDetails actual constructor(actual val pictureIndex: Int) : Screen, Parcelable
