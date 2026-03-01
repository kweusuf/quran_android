package com.quran.data.page.provider.madani

import com.quran.data.source.DisplaySize
import com.quran.labs.androidquran.pages.common.madani.size.DefaultPageSizeCalculator

class TajweedPageSizeCalculator(displaySize: DisplaySize) : DefaultPageSizeCalculator(displaySize) {
  override fun getWidthParameter(): String {
    return "1280"
  }

  override fun getTabletWidthParameter(): String {
    return "1280"
  }
}
