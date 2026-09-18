package com.example

import android.content.Context
import android.content.res.Configuration
import androidx.test.core.app.ApplicationProvider
import com.example.model.DhikrPresets
import com.example.model.TasbihTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context in default locale`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Tasbih Tap", appName)
  }

  @Test
  fun `verify expanded dhikr presets count and content`() {
    val presets = DhikrPresets.PRESETS
    assertTrue(presets.size >= 12)
    assertEquals("SubhanAllah", presets[0].name)
    assertEquals("Alhamdulillah", presets[1].name)
    assertEquals("Allahu Akbar", presets[2].name)
    assertEquals("La ilaha illallah", presets[3].name)
    assertEquals("Astaghfirullah", presets[4].name)
    assertEquals("Salawat", presets[5].name)
  }

  @Test
  fun `verify Bengali language localization strings`() {
    val baseContext = ApplicationProvider.getApplicationContext<Context>()
    val config = Configuration(baseContext.resources.configuration)
    config.setLocale(Locale.forLanguageTag("bn"))
    val bnContext = baseContext.createConfigurationContext(config)

    assertEquals("তাসবীহ ট্যাপ", bnContext.getString(R.string.app_name))
    assertEquals("কাউন্টার", bnContext.getString(R.string.counter))
    assertEquals("ইতিহাস", bnContext.getString(R.string.history))
    assertEquals("সেটিংস", bnContext.getString(R.string.settings))
    assertEquals("গণনা করতে পেছনে ট্যাপ করুন", bnContext.getString(R.string.tap_back_to_count))
    assertEquals("সুবহানাল্লাহ", bnContext.getString(R.string.dhikr_subhanallah_name))
    assertEquals("আল্লাহ অতি পবিত্র ও মহিমান্বিত", bnContext.getString(R.string.dhikr_subhanallah_meaning))
    assertEquals("আল্লাহু আকবার", bnContext.getString(R.string.dhikr_allahu_akbar_name))
  }

  @Test
  fun `verify localized dhikr items return Bengali translations`() {
    val baseContext = ApplicationProvider.getApplicationContext<Context>()
    val config = Configuration(baseContext.resources.configuration)
    config.setLocale(Locale.forLanguageTag("bn"))
    val bnContext = baseContext.createConfigurationContext(config)

    val subhanAllah = DhikrPresets.PRESETS[0]
    assertEquals("সুবহানাল্লাহ", subhanAllah.getLocalizedName(bnContext))
    assertEquals("আল্লাহ অতি পবিত্র ও মহিমান্বিত", subhanAllah.getLocalizedMeaning(bnContext))
    assertEquals("سُبْحَانَ اللَّهِ", subhanAllah.arabic)

    val alhamdulillah = DhikrPresets.PRESETS[1]
    assertEquals("আলহামদুলিল্লাহ", alhamdulillah.getLocalizedName(bnContext))

    val allahuAkbar = DhikrPresets.PRESETS[2]
    assertEquals("আল্লাহু আকবার", allahuAkbar.getLocalizedName(bnContext))
  }

  @Test
  fun `verify new Islamic themes exist`() {
    val themes = TasbihTheme.entries
    assertTrue(themes.any { it.name == "ROSE_GOLD" })
    assertTrue(themes.any { it.name == "ROYAL_AMBER" })
  }
}
