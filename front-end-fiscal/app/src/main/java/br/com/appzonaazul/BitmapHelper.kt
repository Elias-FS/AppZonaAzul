package br.com.appzonaazul

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.DrawableWrapper
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

object BitmapHelper {
    fun vectorToBitMap (
        context: Context,
        @DrawableRes id: Int,
        @ColorInt color: Int

        ): BitmapDescriptor {
        val vectorDrawble = ResourcesCompat.getDrawable(context.resources, id, null)
        if(vectorDrawble == null) {
            return BitmapDescriptorFactory.defaultMarker()

        }
        val bitmap = Bitmap.createBitmap(
            vectorDrawble.intrinsicWidth,
            vectorDrawble.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawble.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawble, color)
        vectorDrawble.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

}