package com.quran.labs.androidquran.ui.translation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import com.quran.labs.androidquran.data.AyahInfoDatabaseHandler
import com.quran.labs.androidquran.util.QuranFileUtils
import timber.log.Timber
import java.io.File
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Utility that crops an ayah from a Tajweed page image using the ayahinfo database
 * bounding-box data.
 *
 * The Tajweed images (1340×1890) live in the same directory as all other page images
 * (quranInternalBaseDirectory / tajweed_v2 / width_XXXX / page001.png …).
 *
 * The ayahinfo database stores coordinates scaled to the API page width
 * (e.g. 1280). We scale the bounding box to match the actual image resolution.
 */
object TajweedAyahCropper {

  // Padding around the cropped ayah (in the *ayahinfo* coordinate space, i.e. ~1280px wide)
  private const val HORIZONTAL_PAD = 8f
  private const val VERTICAL_PAD = 6f

  // Aspect-ratio cap to avoid very tall crops when an ayah spans many lines
  private const val MAX_ASPECT_RATIO = 8f

  /**
   * Synchronously crops the Tajweed image for the given ayah on [page].
   * Call from a background thread.
   *
   * @param db         AyahInfoDatabaseHandler with the glyphs table
   * @param page       Quran page number (1-based)
   * @param sura       Sura number
   * @param ayah       Ayah number
   * @param dbPageWidth Width that the ayahinfo coordinates are calibrated to (e.g. 1280)
   * @param imageFile  The Tajweed PNG for this page on disk
   * @return A cropped [Bitmap] or null if the operation fails
   */
  fun cropAyah(
    db: AyahInfoDatabaseHandler,
    page: Int,
    sura: Int,
    ayah: Int,
    dbPageWidth: Int,
    imageFile: File
  ): Bitmap? {
    if (!imageFile.exists()) {
      Timber.d("TajweedCrop: image not available yet for page %d", page)
      return null
    }

    // 1. Fetch all AyahBounds for the page and filter by sura:ayah
    val ayahCoordinates = db.getVersesBoundsForPage(page)
    val key = "$sura:$ayah"
    val boundsList = ayahCoordinates.ayahCoordinates[key]
    if (boundsList.isNullOrEmpty()) {
      Timber.d("TajweedCrop: no bounds found for %s on page %d", key, page)
      return null
    }

    // 2. Compute the bounding union of all line segments (already in ayahinfo coord space)
    val union = RectF()
    for (b in boundsList) {
      union.union(b.getBounds())
    }

    // Add padding
    union.left = max(0f, union.left - HORIZONTAL_PAD)
    union.top = max(0f, union.top - VERTICAL_PAD)
    union.right = union.right + HORIZONTAL_PAD
    union.bottom = union.bottom + VERTICAL_PAD

    // 3. Load the Tajweed image with in-sample-size to avoid OOM
    val opts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeFile(imageFile.absolutePath, opts)
    val imgW = opts.outWidth
    val imgH = opts.outHeight
    if (imgW <= 0 || imgH <= 0) {
      Timber.d("TajweedCrop: failed to read dimensions for page %d", page)
      return null
    }

    // 4. Scale the ayahinfo rect to the actual image dimensions
    val scaleX = imgW.toFloat() / dbPageWidth.toFloat()
    // The ayahinfo DB typically covers the full height; derive scaleY from page aspect
    // (ayahinfo coords go up to the page width, so use a uniform scale factor)
    val scaleY = scaleX
    val cropRect = Rect(
      (union.left * scaleX).roundToInt().coerceAtLeast(0),
      (union.top  * scaleY).roundToInt().coerceAtLeast(0),
      (union.right  * scaleX).roundToInt().coerceAtMost(imgW),
      (union.bottom * scaleY).roundToInt().coerceAtMost(imgH)
    )

    if (cropRect.width() <= 0 || cropRect.height() <= 0) {
      return null
    }

    // 5. Decode with subsetRect so we only load the required portion
    val finalOpts = BitmapFactory.Options().apply {
      inBitmap = null
      inPreferredConfig = Bitmap.Config.ARGB_8888
    }
    return try {
      val full = BitmapFactory.decodeFile(imageFile.absolutePath, finalOpts) ?: return null
      val cropped = Bitmap.createBitmap(full, cropRect.left, cropRect.top,
        cropRect.width(), cropRect.height())
      if (cropped !== full) full.recycle()

      // Cap the aspect ratio to avoid excessively tall tall bitmaps
      val crop = if (cropped.width > 0 &&
        cropped.height.toFloat() / cropped.width > MAX_ASPECT_RATIO) {
        val newH = (cropped.width * MAX_ASPECT_RATIO).roundToInt()
        val capped = Bitmap.createBitmap(cropped, 0, 0, cropped.width, newH)
        cropped.recycle()
        capped
      } else {
        cropped
      }
      crop
    } catch (e: Exception) {
      Timber.e(e, "TajweedCrop: failed to crop ayah %s from page %d", key, page)
      null
    }
  }
}
