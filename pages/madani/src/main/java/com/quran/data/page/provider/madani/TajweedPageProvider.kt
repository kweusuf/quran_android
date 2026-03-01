package com.quran.data.page.provider.madani

import com.quran.data.model.audio.Qari
import com.quran.data.source.DisplaySize
import com.quran.data.source.PageContentType
import com.quran.data.source.PageProvider
import com.quran.data.source.PageSizeCalculator
import com.quran.labs.androidquran.pages.common.madani.size.DefaultPageSizeCalculator
import com.quran.labs.androidquran.pages.data.madani.MadaniDataSource
import com.quran.labs.androidquran.pages.madani.R
import java.util.Locale

/**
 * TajweedPageProvider — Tajweed-coloured Mushaf (tajweed)
 *
 * This Mushaf reuses the classic Madani 604-page layout and metadata (ayahinfo, audio).
 *
 * It uses individual page images from:
 *   HiIAmMoot/quran-android-tajweed-page-provider
 *   https://raw.githubusercontent.com/HiIAmMoot/quran-android-tajweed-page-provider/main/images/
 *
 * Highlighting Alignment:
 * The HiIAmMoot images (1340x1890) are much higher quality and closer in aspect
 * to the official 1260px Madani images than other sources. To ensure highlighting
 * works, we use the Madani ayahinfo metadata and scaling logic.
 */
class TajweedPageProvider : PageProvider {

  override fun getDataSource() = dataSource

  override fun getPageSizeCalculator(displaySize: DisplaySize): PageSizeCalculator =
    DefaultPageSizeCalculator(displaySize)

  // Version 1 is special-cased in QuranFileUtils.isVersion() to return true immediately
  // so no zip download dialog is shown. Individual pages load on-demand.
  override fun getImageVersion() = 1

  // Allow opening the reader without a bulk ZIP download.
  override fun getPageContentType(): PageContentType = PageContentType.Image

  // Point to Madani for non-image data to leverage existing hosted assets.
  override fun getImagesBaseUrl() = "$madaniBaseUrl/"
  override fun getImagesZipBaseUrl() = "$madaniBaseUrl/zips/"
  override fun getPatchBaseUrl() = "$madaniBaseUrl/patches/v"
  override fun getAyahInfoBaseUrl() = "$madaniBaseUrl/databases/ayahinfo/"
  override fun getDatabasesBaseUrl() = "$madaniBaseUrl/databases/"

  override fun getAudioDirectoryName() = "audio"
  override fun getDatabaseDirectoryName() = "databases"
  override fun getAyahInfoDirectoryName() = getDatabaseDirectoryName()
  override fun getAudioDatabasesBaseUrl() = "https://files.quran.app/hafs/databases/audio/"
  override fun getImagesDirectoryName() = "tajweed_v2"

  override fun pageType() = "tajweed"
  override fun getPreviewTitle() = R.string.tajweed_title
  override fun getPreviewDescription() = R.string.tajweed_description
  override fun getDefaultQariId(): Int = 0
  override fun getQaris(): List<Qari> = MadaniPageProvider.allQaris

  /**
   * Maps a standard filename (e.g. "page001.png") to the Moot Tajweed image source.
   * HiIAmMoot repo uses "001.png", "002.png", etc. and is much higher resolution
   * and better aligned with standard Madani coordinates than alternative JPG sources.
   */
  override fun getIndividualPageUrl(pageNumber: Int, filename: String): String {
    val paddedPage = String.format(Locale.US, "%03d", pageNumber)
    return "$githubBaseUrl/$paddedPage.png"
  }

  companion object {
    private const val madaniBaseUrl = "https://files.quran.app/hafs/madani"
    private const val githubBaseUrl =
      "https://raw.githubusercontent.com/HiIAmMoot/quran-android-tajweed-page-provider/main/images"
    private val dataSource by lazy { MadaniDataSource() }
  }
}
