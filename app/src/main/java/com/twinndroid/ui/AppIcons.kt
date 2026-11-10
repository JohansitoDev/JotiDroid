package com.twinndroid.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.twinndroid.MainActivity
import com.twinndroid.R
import kotlin.math.min

/** A launcher icon for JotiDroid itself, backed by an <activity-alias> in the manifest. */
class AppIcon(
    val alias: String,
    val name: String,
    val foreground: Int,
    val bgStart: Color,
    val bgEnd: Color,
)

object AppIcons {
    private const val PREFIX = "com.twinndroid.Launcher"
    private const val PREF = "launcher_icon"

    val all = listOf(
        AppIcon("White", "Blanco", R.drawable.ic_launcher_foreground, Color(0xFFFFFFFF), Color(0xFFE0E0E0)),
        AppIcon("Black", "Negro", R.drawable.ic_launcher_foreground, Color(0xFF000000), Color(0xFF121212)),
    )

    fun byAlias(alias: String): AppIcon? = all.firstOrNull { it.alias == alias }

    private fun component(context: Context, icon: AppIcon) = ComponentName(context.packageName, PREFIX + icon.alias)

    private fun prefs(context: Context) = context.getSharedPreferences("theme", Context.MODE_PRIVATE)

    private fun enabled(context: Context): AppIcon {
        val pm = context.packageManager
        return all.firstOrNull { icon ->
            when (pm.getComponentEnabledSetting(component(context, icon))) {
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED -> true
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT -> icon === all.first()
                else -> false
            }
        } ?: all.first()
    }

    fun current(context: Context): AppIcon =
        prefs(context).getString(PREF, null)?.let { a -> all.firstOrNull { it.alias == a } } ?: enabled(context)

    fun choose(context: Context, icon: AppIcon) {
        prefs(context).edit().putString(PREF, icon.alias).apply()
    }

    fun applyPending(context: Context) {
        val icon = current(context)
        if (icon === enabled(context)) return
        val pm = context.packageManager
        pm.setComponentEnabledSetting(component(context, icon), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
        for (other in all) {
            if (other === icon) continue
            pm.setComponentEnabledSetting(component(context, other), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
        }
    }

    fun pinCustomShortcut(context: Context, image: Uri, label: String): Boolean {
        if (!ShortcutManagerCompat.isRequestPinShortcutSupported(context)) return false
        val bitmap = loadAdaptive(context, image) ?: return false
        val info = ShortcutInfoCompat.Builder(context, "jotiodroid-custom-${System.currentTimeMillis()}")
            .setShortLabel(label.ifBlank { "JotiDroid" })
            .setIcon(IconCompat.createWithAdaptiveBitmap(bitmap))
            .setIntent(Intent(context, MainActivity::class.java).setAction(Intent.ACTION_MAIN))
            .build()
        return ShortcutManagerCompat.requestPinShortcut(context, info, null)
    }

    fun loadAdaptive(context: Context, image: Uri): Bitmap? {
        val src = context.contentResolver.openInputStream(image)?.use { BitmapFactory.decodeStream(it) } ?: return null
        val size = 432
        val side = min(src.width, src.height)
        val crop = Rect((src.width - side) / 2, (src.height - side) / 2, (src.width + side) / 2, (src.height + side) / 2)
        val out = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        Canvas(out).drawBitmap(src, crop, Rect(0, 0, size, size), Paint(Paint.FILTER_BITMAP_FLAG or Paint.ANTI_ALIAS_FLAG))
        return out
    }
}
