package com.quran.data.core
 
import kotlin.jvm.JvmField

object QuranConstants {
  const val NUMBER_OF_SURAS = 114
  const val PAGES_FIRST = 1
  const val FIRST_SURA = 1
  const val LAST_SURA = 114
  const val MIN_AYAH = 1
  const val MAX_AYAH = 286
  const val JUZ2_COUNT = 30
 
  @JvmField
  val AUDIO_SPEEDS = listOf(0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 1.75f, 2.0f, 2.1f, 2.2f, 2.25f, 2.5f)
}
