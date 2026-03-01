package com.quran.labs.androidquran.pages.data.madani

import com.quran.data.model.SuraAyah
import com.quran.data.source.QuranDataSource

/**
 * A generic [QuranDataSource] for Hafs-based Mushafs that have a different page layout
 * than the classic 604-page Madani Mushaf.
 *
 * The sura/ayah page arrays that cannot be known without the ayahinfo database are
 * delegated to the classic [MadaniDataSource] as a safe fallback so that basic Quran
 * navigation (juz, sura) still works. Accurate per-page data is provided through the
 * ayahinfo SQLite database downloaded alongside each Mushaf.
 *
 * @param totalPages The exact page count for this Mushaf layout.
 */
class GenericHafsDataSource(
  override val numberOfPages: Int,
  private val base: MadaniDataSource = MadaniDataSource(),
) : QuranDataSource {

  // These arrays map **Sura number → first page** and are independent of the
  // specific page layout — they still correctly identify where each sura starts.
  override val pageForSuraArray: IntArray get() = base.pageForSuraArray
  override val pageForJuzArray: IntArray get() = base.pageForJuzArray
  override val juzDisplayPageArrayOverride: Map<Int, Int> get() = base.juzDisplayPageArrayOverride
  override val numberOfAyahsForSuraArray: IntArray get() = base.numberOfAyahsForSuraArray
  override val isMakkiBySuraArray: BooleanArray get() = base.isMakkiBySuraArray
  override val quartersArray: Array<SuraAyah> get() = base.quartersArray
  override val manzilPageArray: Array<Int> get() = base.manzilPageArray

  // These arrays are page-count-specific.  We return empty/stub arrays here because
  // the actual data comes from the ayahinfo database (ayahinfo_<width>.db) that is
  // downloaded with each Mushaf.  The database is the authoritative source for
  // per-page sura/ayah mapping in the non-classic layouts.
  override val suraForPageArray: IntArray
    get() = IntArray(numberOfPages) { 1 }

  override val ayahForPageArray: IntArray
    get() = IntArray(numberOfPages) { 1 }

  override val quarterStartByPage: IntArray
    get() = IntArray(numberOfPages) { -1 }

  override val haveSidelines: Boolean = false
  override val pagesToSkip: Int = 0
}
