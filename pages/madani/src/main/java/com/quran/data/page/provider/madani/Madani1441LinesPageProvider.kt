package com.quran.data.page.provider.madani

import com.quran.data.model.audio.Qari
import com.quran.data.source.DisplaySize
import com.quran.data.source.PageProvider
import com.quran.data.source.PageSizeCalculator
import com.quran.labs.androidquran.pages.common.madani.size.DefaultPageSizeCalculator
import com.quran.labs.androidquran.pages.data.madani.GenericHafsDataSource
import com.quran.labs.androidquran.pages.madani.R

/**
 * Madani1441LinesPageProvider — Madani Mushaf 1441 Lines (madani_1441_lines)
 * 1352 pages — optimised for larger screen widths.
 */
class Madani1441LinesPageProvider : PageProvider {

  override fun getDataSource() = dataSource

  override fun getPageSizeCalculator(displaySize: DisplaySize): PageSizeCalculator =
    DefaultPageSizeCalculator(displaySize)

  override fun getImageVersion() = 1

  override fun getImagesBaseUrl() = "$baseUrl/"

  override fun getImagesZipBaseUrl() = "$baseUrl/zips/"

  override fun getPatchBaseUrl() = "$baseUrl/patches/v"

  override fun getAyahInfoBaseUrl() = "$baseUrl/databases/ayahinfo/"

  override fun getAudioDirectoryName() = "audio"

  override fun getDatabaseDirectoryName() = "databases"

  override fun getAyahInfoDirectoryName() = getDatabaseDirectoryName()

  override fun getDatabasesBaseUrl() = "$baseUrl/databases/"

  override fun getAudioDatabasesBaseUrl() = "https://files.quran.app/hafs/databases/audio/"

  override fun getImagesDirectoryName() = ""

  override fun pageType() = "madani_1441_lines"

  override fun getPreviewTitle() = R.string.madani_1441_lines_title

  override fun getPreviewDescription() = R.string.madani_1441_lines_description

  override fun getDefaultQariId(): Int = 0

  override fun getQaris(): List<Qari> = MadaniPageProvider.allQaris

  companion object {
    private const val baseUrl = "https://files.quran.app/hafs/madani_1441"
    private val dataSource by lazy { GenericHafsDataSource(numberOfPages = 1352) }
  }
}
