package com.quran.data.page.provider

import com.quran.common.upgrade.LocalDataUpgrade
import com.quran.common.upgrade.PreferencesUpgrade
import com.quran.data.constant.DependencyInjectionConstants
import com.quran.data.page.provider.madani.Madani1441LinesPageProvider
import com.quran.data.page.provider.madani.NewMadani1439LinesPageProvider
import com.quran.data.page.provider.madani.NewMadani1441PageProvider
import com.quran.data.page.provider.madani.NewMadaniPageProvider
import com.quran.data.page.provider.madani.MadaniPageProvider
import com.quran.data.page.provider.madani.TajweedPageProvider
import com.quran.data.pageinfo.mapper.AyahMapper
import com.quran.data.pageinfo.mapper.IdentityAyahMapper
import com.quran.data.source.PageProvider
import com.quran.page.common.draw.ImageDrawHelper
import com.quran.page.common.factory.PageViewFactoryProvider
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ElementsIntoSet
import dev.zacsweers.metro.IntoMap
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.StringKey


@BindingContainer
object QuranDataModule {

  @Provides
  fun providePageViewFactoryProvider(): PageViewFactoryProvider {
    return PageViewFactoryProvider { null }
  }

  @Named(DependencyInjectionConstants.FALLBACK_PAGE_TYPE)
  @JvmStatic
  @Provides
  fun provideFallbackPageType(): String = "tajweed"

  @JvmStatic
  @Provides
  @IntoMap
  @StringKey("madani")
  fun provideMadaniPageSet(): PageProvider {
    return MadaniPageProvider()
  }

  // NOTE: new_madani, new_madani_1441, new_madani_1439_lines and madani_1441_lines are
  // intentionally excluded because their zip/image files return HTTP 404 on
  // files.quran.app as of 2026-03. Re-add them once the server hosts the assets.
/*
  @JvmStatic
  @Provides
  @IntoMap
  @StringKey("new_madani")
  fun provideNewMadaniPageSet(): PageProvider {
    return NewMadaniPageProvider()
  }

  @JvmStatic
  @Provides
  @IntoMap
  @StringKey("new_madani_1441")
  fun provideNewMadani1441PageSet(): PageProvider {
    return NewMadani1441PageProvider()
  }

  @JvmStatic
  @Provides
  @IntoMap
  @StringKey("new_madani_1439_lines")
  fun provideNewMadani1439LinesPageSet(): PageProvider {
    return NewMadani1439LinesPageProvider()
  }

  @JvmStatic
  @Provides
  @IntoMap
  @StringKey("madani_1441_lines")
  fun provideMadani1441LinesPageSet(): PageProvider {
    return Madani1441LinesPageProvider()
  }
*/
  @JvmStatic
  @Provides
  @IntoMap
  @StringKey("tajweed")
  fun provideTajweedPageSet(): PageProvider {
    return TajweedPageProvider()
  }

  @JvmStatic
  @Provides
  @ElementsIntoSet
  fun provideImageDrawHelpers(): Set<ImageDrawHelper> {
    return emptySet()
  }

  @JvmStatic
  @Provides
  fun provideLocalDataUpgrade(): LocalDataUpgrade = object : LocalDataUpgrade {  }

  @JvmStatic
  @Provides
  fun providePreferencesUpgrade(): PreferencesUpgrade = PreferencesUpgrade { _, _, _ -> true }

  @JvmStatic
  @Provides
  fun provideAyahMapper(): AyahMapper = IdentityAyahMapper()
}
