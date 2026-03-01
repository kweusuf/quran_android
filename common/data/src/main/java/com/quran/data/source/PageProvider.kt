package com.quran.data.source

import androidx.annotation.StringRes
import com.quran.data.model.audio.Qari

interface PageProvider {
  fun getDataSource(): QuranDataSource
  fun getPageSizeCalculator(displaySize: DisplaySize): PageSizeCalculator

  fun getImageVersion(): Int

  fun getImagesBaseUrl(): String
  fun getImagesZipBaseUrl(): String
  fun getPatchBaseUrl(): String
  fun getAyahInfoBaseUrl(): String
  fun getDatabasesBaseUrl(): String
  fun getAudioDatabasesBaseUrl(): String

  fun getAudioDirectoryName(): String
  fun getDatabaseDirectoryName(): String
  fun getAyahInfoDirectoryName(): String
  fun getImagesDirectoryName(): String

  fun ayahInfoDbHasGlyphData(): Boolean = false

  /**
   * Optional override: produce the full URL to download a single page image.
   * Return null to use the default URL constructed by QuranFileUtils:
   *   getImagesBaseUrl() + "width" + widthParam + "/" + filename
   *
   * Override this when the page images live at a CDN that uses a different
   * path or filename convention (e.g. Tajweed images on GitHub which use
   * "001.png" instead of "page001.png" and have no width subdirectory).
   *
   * @param pageNumber  the 1-based page number
   * @param filename    the standard filename (e.g. "page001.png")
   */
  fun getIndividualPageUrl(pageNumber: Int, filename: String): String? = null

  @StringRes fun getPreviewTitle(): Int
  @StringRes fun getPreviewDescription(): Int

  fun getPageContentType(): PageContentType = PageContentType.Image
  fun getFallbackPageType(): String? = null
  fun getQaris(): List<Qari>
  fun getDefaultQariId(): Int
  fun pageType(): String = ""
}
