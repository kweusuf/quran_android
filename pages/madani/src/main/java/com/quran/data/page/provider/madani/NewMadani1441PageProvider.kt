package com.quran.data.page.provider.madani

import com.quran.data.model.audio.Qari
import com.quran.data.source.DisplaySize
import com.quran.data.source.PageProvider
import com.quran.data.source.PageSizeCalculator
import com.quran.labs.androidquran.pages.common.madani.size.DefaultPageSizeCalculator
import com.quran.labs.androidquran.pages.data.madani.GenericHafsDataSource
import com.quran.labs.androidquran.pages.madani.R

/**
 * NewMadani1441PageProvider — Madani Mushaf 1441 (new_madani_1441)
 * 1440 pages.
 */
class NewMadani1441PageProvider : PageProvider {

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

  override fun pageType() = "new_madani_1441"

  override fun getPreviewTitle() = R.string.newer_new_madani_title

  override fun getPreviewDescription() = R.string.newer_new_madani_description

  override fun getDefaultQariId(): Int = 0

  override fun getQaris(): List<Qari> = MadaniPageProvider.allQaris

  companion object {
    private const val baseUrl = "https://files.quran.app/hafs/madani_1441"
    private val dataSource by lazy { GenericHafsDataSource(numberOfPages = 1440) }
  }
}
