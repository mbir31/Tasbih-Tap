package com.example.model

import android.content.Context
import androidx.annotation.StringRes
import com.example.R

enum class Sensitivity(@StringRes val labelRes: Int, val threshold: Float) {
    LOW(R.string.sensitivity_low, 5.2f),
    MEDIUM(R.string.sensitivity_medium, 3.8f),
    HIGH(R.string.sensitivity_high, 2.4f)
}

enum class HapticStrength(@StringRes val labelRes: Int, val normalDuration: Long, val normalAmplitude: Int) {
    LIGHT(R.string.haptic_light, 14, 100),
    MEDIUM(R.string.haptic_medium, 24, 170),
    STRONG(R.string.haptic_strong, 40, 255)
}

data class DhikrItem(
    val id: String,
    val name: String,
    val arabic: String,
    val meaning: String,
    val defaultTarget: Int,
    val isCustom: Boolean = false,
    @StringRes val nameRes: Int? = null,
    @StringRes val meaningRes: Int? = null,
    val category: String = "General"
) {
    fun getLocalizedName(context: Context): String {
        return if (nameRes != null) {
            try {
                context.getString(nameRes)
            } catch (e: Exception) {
                name
            }
        } else {
            name
        }
    }

    fun getLocalizedMeaning(context: Context): String {
        return if (meaningRes != null) {
            try {
                context.getString(meaningRes)
            } catch (e: Exception) {
                meaning
            }
        } else {
            meaning
        }
    }
}

object DhikrPresets {
    val PRESETS = listOf(
        DhikrItem(
            id = "subhanallah",
            name = "SubhanAllah",
            arabic = "سُبْحَانَ اللَّهِ",
            meaning = "Glory be to Allah",
            defaultTarget = 33,
            nameRes = R.string.dhikr_subhanallah_name,
            meaningRes = R.string.dhikr_subhanallah_meaning,
            category = "Essential"
        ),
        DhikrItem(
            id = "alhamdulillah",
            name = "Alhamdulillah",
            arabic = "الْحَمْدُ لِلَّهِ",
            meaning = "All praise is due to Allah",
            defaultTarget = 33,
            nameRes = R.string.dhikr_alhamdulillah_name,
            meaningRes = R.string.dhikr_alhamdulillah_meaning,
            category = "Essential"
        ),
        DhikrItem(
            id = "allahu_akbar",
            name = "Allahu Akbar",
            arabic = "اللَّهُ أَكْبَرُ",
            meaning = "Allah is the Greatest",
            defaultTarget = 33,
            nameRes = R.string.dhikr_allahu_akbar_name,
            meaningRes = R.string.dhikr_allahu_akbar_meaning,
            category = "Essential"
        ),
        DhikrItem(
            id = "tahlil",
            name = "La ilaha illallah",
            arabic = "لَا إِلَٰهَ إِلَّا اللَّهُ",
            meaning = "There is no deity except Allah",
            defaultTarget = 100,
            nameRes = R.string.dhikr_tahlil_name,
            meaningRes = R.string.dhikr_tahlil_meaning,
            category = "Essential"
        ),
        DhikrItem(
            id = "istighfar",
            name = "Astaghfirullah",
            arabic = "أَسْتَغْفِرُ اللَّهَ",
            meaning = "I seek forgiveness from Allah",
            defaultTarget = 100,
            nameRes = R.string.dhikr_istighfar_name,
            meaningRes = R.string.dhikr_istighfar_meaning,
            category = "Forgiveness"
        ),
        DhikrItem(
            id = "salawat",
            name = "Salawat",
            arabic = "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ",
            meaning = "Peace and blessings upon Muhammad",
            defaultTarget = 100,
            nameRes = R.string.dhikr_salawat_name,
            meaningRes = R.string.dhikr_salawat_meaning,
            category = "Salawat"
        ),
        DhikrItem(
            id = "hawqala",
            name = "La hawla wa la quwwata illa billah",
            arabic = "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ",
            meaning = "There is no might nor power except in Allah",
            defaultTarget = 100,
            nameRes = R.string.dhikr_hawqala_name,
            meaningRes = R.string.dhikr_hawqala_meaning,
            category = "Supplication"
        ),
        DhikrItem(
            id = "hasbunallah",
            name = "Hasbunallahu wa ni'mal wakeel",
            arabic = "حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ",
            meaning = "Allah is sufficient for us, and He is the best disposer of affairs",
            defaultTarget = 100,
            nameRes = R.string.dhikr_hasbunallah_name,
            meaningRes = R.string.dhikr_hasbunallah_meaning,
            category = "Supplication"
        ),
        DhikrItem(
            id = "subhanallahi_wa_bihamdihi",
            name = "SubhanAllahi wa bihamdihi",
            arabic = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ",
            meaning = "Glory be to Allah and His is the praise",
            defaultTarget = 100,
            nameRes = R.string.dhikr_subhanallahi_wa_bihamdihi_name,
            meaningRes = R.string.dhikr_subhanallahi_wa_bihamdihi_meaning,
            category = "Praise"
        ),
        DhikrItem(
            id = "subhanallah_alazeem",
            name = "SubhanAllahil Azeem",
            arabic = "سُبْحَانَ اللَّهِ الْعَظِيمِ",
            meaning = "Glory be to Allah the Magnificent",
            defaultTarget = 100,
            nameRes = R.string.dhikr_subhanallah_alazeem_name,
            meaningRes = R.string.dhikr_subhanallah_alazeem_meaning,
            category = "Praise"
        ),
        DhikrItem(
            id = "sayyidul_istighfar",
            name = "Sayyidul Istighfar (Chief Forgiveness)",
            arabic = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ",
            meaning = "O Allah, You are my Lord, none has the right to be worshiped but You",
            defaultTarget = 33,
            nameRes = R.string.dhikr_sayyidul_istighfar_name,
            meaningRes = R.string.dhikr_sayyidul_istighfar_meaning,
            category = "Forgiveness"
        ),
        DhikrItem(
            id = "yunus_dua",
            name = "Ayat e Kareema (Dua of Yunus)",
            arabic = "لَا إِلَهَ إِلَّا أَنْتَ سُبْحَانَكَ إِنِّي كُنْتُ مِنَ الظَّالِمِينَ",
            meaning = "None has the right to be worshiped but You, exalted are You, indeed I have been of the wrongdoers",
            defaultTarget = 40,
            nameRes = R.string.dhikr_yunus_dua_name,
            meaningRes = R.string.dhikr_yunus_dua_meaning,
            category = "Supplication"
        )
    )

    val TARGET_OPTIONS = listOf(33, 99, 100, 313, 500, 1000)

    val THIRTY_THREE_TIMES_THREE = listOf(
        PRESETS[0], // SubhanAllah
        PRESETS[1], // Alhamdulillah
        PRESETS[2]  // Allahu Akbar
    )
}
