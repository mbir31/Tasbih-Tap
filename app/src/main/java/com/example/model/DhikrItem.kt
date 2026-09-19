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
        // --- ESSENTIAL / SUNNAH DHIKRS ---
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
            defaultTarget = 34,
            nameRes = R.string.dhikr_allahu_akbar_name,
            meaningRes = R.string.dhikr_allahu_akbar_meaning,
            category = "Essential"
        ),
        DhikrItem(
            id = "tahlil",
            name = "La ilaha illallah",
            arabic = "لَا إِلٰهَ إِلَّا اللَّهُ",
            meaning = "There is no deity worthy of worship except Allah",
            defaultTarget = 100,
            nameRes = R.string.dhikr_tahlil_name,
            meaningRes = R.string.dhikr_tahlil_meaning,
            category = "Essential"
        ),
        DhikrItem(
            id = "tamjeed",
            name = "SubhanAllahi wal-Hamdulillahi wa La ilaha illallahu Wallahu Akbar",
            arabic = "سُبْحَانَ اللَّهِ وَالْحَمْدُ لِلَّهِ وَلَا إِلٰهَ إِلَّا اللَّهُ وَاللَّهُ أَكْبَرُ",
            meaning = "Glory be to Allah, praise be to Allah, there is no god but Allah, and Allah is the Greatest",
            defaultTarget = 100,
            nameRes = R.string.dhikr_tamjeed_name,
            meaningRes = R.string.dhikr_tamjeed_meaning,
            category = "Essential"
        ),

        // --- PRAISE & GLORIFICATION ---
        DhikrItem(
            id = "subhanallah_bihamdihi",
            name = "SubhanAllahi wa bihamdihi",
            arabic = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ",
            meaning = "Glory and praise be to Allah",
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
            id = "subhanallahi_wa_bihamdihi_adada",
            name = "SubhanAllahi wa bihamdihi ‘adada khalqihi",
            arabic = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ عَدَدَ خَلْقِهِ وَرِضَا نَفْسِهِ وَزِنَةَ عَرْشِهِ وَمِدَادَ كَلِمَاتِهِ",
            meaning = "Glory to Allah and praise Him, according to the number of His creation and weight of His Throne",
            defaultTarget = 3,
            nameRes = R.string.dhikr_subhanallahi_wa_bihamdihi_adada_name,
            meaningRes = R.string.dhikr_subhanallahi_wa_bihamdihi_adada_meaning,
            category = "Praise"
        ),
        DhikrItem(
            id = "la_ilaha_wahdahu",
            name = "La ilaha illallahu wahdahu la shareeka lah",
            arabic = "لَا إِلٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ",
            meaning = "None has the right to be worshipped except Allah alone, with no partner or associate",
            defaultTarget = 100,
            nameRes = R.string.dhikr_la_ilaha_wahdahu_name,
            meaningRes = R.string.dhikr_la_ilaha_wahdahu_meaning,
            category = "Praise"
        ),
        DhikrItem(
            id = "ya_hayyu_ya_qayyum",
            name = "Ya Hayyu Ya Qayyum",
            arabic = "يَا حَيُّ يَا قَيُّومُ",
            meaning = "O Ever-Living, O Self-Sustaining",
            defaultTarget = 100,
            nameRes = R.string.dhikr_ya_hayyu_ya_qayyum_name,
            meaningRes = R.string.dhikr_ya_hayyu_ya_qayyum_meaning,
            category = "Praise"
        ),
        DhikrItem(
            id = "ya_dhal_jalali",
            name = "Ya Dhal Jalali wal Ikram",
            arabic = "يَا ذَا الْجَلَالِ وَالإِكْرَامِ",
            meaning = "O Owner of Majesty and Honor",
            defaultTarget = 33,
            nameRes = R.string.dhikr_ya_dhal_jalali_name,
            meaningRes = R.string.dhikr_ya_dhal_jalali_meaning,
            category = "Praise"
        ),
        DhikrItem(
            id = "radheetu_billah",
            name = "Radheetu Billahi Rabba",
            arabic = "رَضِيتُ بِاللَّهِ رَبًّا وَبِالإِسْلَامِ دِينًا وَبِمُحَمَّدٍ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ نَبِيًّا",
            meaning = "I am pleased with Allah as my Lord, Islam as my religion, and Muhammad ﷺ as my Prophet",
            defaultTarget = 3,
            nameRes = R.string.dhikr_radheetu_billah_name,
            meaningRes = R.string.dhikr_radheetu_billah_meaning,
            category = "Praise"
        ),

        // --- FORGIVENESS & REPENTANCE ---
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
            id = "astaghfirullah_wa_atubu",
            name = "Astaghfirullaha wa Atubu Ilayh",
            arabic = "أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ",
            meaning = "I seek forgiveness of Allah and turn unto Him in repentance",
            defaultTarget = 100,
            nameRes = R.string.dhikr_astaghfirullah_wa_atubu_name,
            meaningRes = R.string.dhikr_astaghfirullah_wa_atubu_meaning,
            category = "Forgiveness"
        ),
        DhikrItem(
            id = "sayyidul_istighfar",
            name = "Sayyidul Istighfar",
            arabic = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلٰهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَىٰ عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
            meaning = "Chief supplication for forgiveness",
            defaultTarget = 3,
            nameRes = R.string.dhikr_sayyidul_istighfar_name,
            meaningRes = R.string.dhikr_sayyidul_istighfar_meaning,
            category = "Forgiveness"
        ),
        DhikrItem(
            id = "rabbighfirli",
            name = "Rabbighfir li wa tub 'alayya",
            arabic = "رَبِّ اغْفِرْ لِي وَتُبْ عَلَيَّ إِنَّكَ أَنْتَ التَّوَّابُ الرَّحِيمُ",
            meaning = "My Lord! Forgive me and accept my repentance, surely You are the Accepter of Repentance, the Merciful",
            defaultTarget = 100,
            nameRes = R.string.dhikr_rabbighfirli_name,
            meaningRes = R.string.dhikr_rabbighfirli_meaning,
            category = "Forgiveness"
        ),
        DhikrItem(
            id = "astaghfirullah_alazeem_alladhi",
            name = "Astaghfirullahal Azeem alladhi la ilaha illa Huwa",
            arabic = "أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ الَّذِي لَا إِلٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ وَأَتُوبُ إِلَيْهِ",
            meaning = "I seek forgiveness from Allah the Almighty, whom there is no deity but He, the Living, the Eternal, and I repent to Him",
            defaultTarget = 33,
            nameRes = R.string.dhikr_astaghfirullah_alazeem_alladhi_name,
            meaningRes = R.string.dhikr_astaghfirullah_alazeem_alladhi_meaning,
            category = "Forgiveness"
        ),

        // --- SALAWAT & BLESSINGS UPON PROPHET ﷺ ---
        DhikrItem(
            id = "salawat",
            name = "Allahumma Salli 'ala Muhammad",
            arabic = "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ",
            meaning = "O Allah, send blessings and peace upon our Prophet Muhammad",
            defaultTarget = 100,
            nameRes = R.string.dhikr_salawat_name,
            meaningRes = R.string.dhikr_salawat_meaning,
            category = "Salawat"
        ),
        DhikrItem(
            id = "durood_ibrahim",
            name = "Durood-e-Ibrahim",
            arabic = "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ، اللَّهُمَّ بَارِكْ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ كَمَا بَارَكْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ",
            meaning = "The complete and most virtuous prayer for blessings upon Prophet Muhammad ﷺ",
            defaultTarget = 10,
            nameRes = R.string.dhikr_durood_ibrahim_name,
            meaningRes = R.string.dhikr_durood_ibrahim_meaning,
            category = "Salawat"
        ),
        DhikrItem(
            id = "sallallahu_alayhi_wa_sallam",
            name = "Sallallahu ‘Alayhi Wa Sallam",
            arabic = "صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ",
            meaning = "May Allah honor him and grant him peace",
            defaultTarget = 100,
            nameRes = R.string.dhikr_sallallahu_alayhi_wa_sallam_name,
            meaningRes = R.string.dhikr_sallallahu_alayhi_wa_sallam_meaning,
            category = "Salawat"
        ),

        // --- SUPPLICATION & PROTECTION ---
        DhikrItem(
            id = "hawqala",
            name = "La Hawla wa la Quwwata illa Billah",
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
            id = "bismillahil_ladhi",
            name = "Bismillahilladhi la yadurru ma'asmihi shay'",
            arabic = "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ",
            meaning = "In the Name of Allah, with Whose Name nothing can cause harm in earth or heaven",
            defaultTarget = 3,
            nameRes = R.string.dhikr_bismillahil_ladhi_name,
            meaningRes = R.string.dhikr_bismillahil_ladhi_meaning,
            category = "Supplication"
        ),
        DhikrItem(
            id = "ya_hayyu_bi_rahmatika",
            name = "Ya Hayyu Ya Qayyum bi-rahmatika astagheeth",
            arabic = "يَا حَيُّ يَا قَيُّومُ بِرَحْمَتِكَ أَسْتَغِيثُ أَصْلِحْ لِي شَأْنِي كُلَّهُ وَلَا تَكِلْنِي إِلَىٰ نَفْسِي طَرْفَةَ عَيْنٍ",
            meaning = "O Ever-Living, O Sustainer! By Your mercy I seek assistance; rectify all my affairs",
            defaultTarget = 3,
            nameRes = R.string.dhikr_ya_hayyu_bi_rahmatika_name,
            meaningRes = R.string.dhikr_ya_hayyu_bi_rahmatika_meaning,
            category = "Supplication"
        ),
        DhikrItem(
            id = "allahumma_ajirni",
            name = "Allahumma ajirni minan-nar",
            arabic = "اللَّهُمَّ أَجِرْنِي مِنَ النَّارِ",
            meaning = "O Allah, save me from the Fire of Hell",
            defaultTarget = 7,
            nameRes = R.string.dhikr_allahumma_ajirni_name,
            meaningRes = R.string.dhikr_allahumma_ajirni_meaning,
            category = "Supplication"
        ),
        DhikrItem(
            id = "allahumma_innee_as_aluka_jannah",
            name = "Allahumma inni as'alukal-Jannah",
            arabic = "اللَّهُمَّ إِنِّي أَسْأَلُكَ الْجَنَّةَ",
            meaning = "O Allah, I ask You for Paradise",
            defaultTarget = 7,
            nameRes = R.string.dhikr_allahumma_innee_as_aluka_jannah_name,
            meaningRes = R.string.dhikr_allahumma_innee_as_aluka_jannah_meaning,
            category = "Supplication"
        ),
        DhikrItem(
            id = "allahumma_inffaka_afuwwun",
            name = "Allahumma innaka 'Afuwwun tuhibbul 'afwa fa'fu 'anni",
            arabic = "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي",
            meaning = "O Allah, You are Most Forgiving, and You love to forgive, so forgive me",
            defaultTarget = 33,
            nameRes = R.string.dhikr_allahumma_inffaka_afuwwun_name,
            meaningRes = R.string.dhikr_allahumma_inffaka_afuwwun_meaning,
            category = "Supplication"
        ),

        // --- QURANIC DUAS ---
        DhikrItem(
            id = "yunus_dua",
            name = "Ayat e Kareema (Dua of Yunus)",
            arabic = "لَّا إِلٰهَ إِلَّا أَنتَ سُبْحَانَكَ إِنِّي كُنتُ مِنَ الظَّالِمِينَ",
            meaning = "There is no deity except You; Exalted are You. Indeed, I have been of the wrongdoers",
            defaultTarget = 100,
            nameRes = R.string.dhikr_yunus_dua_name,
            meaningRes = R.string.dhikr_yunus_dua_meaning,
            category = "Quranic"
        ),
        DhikrItem(
            id = "rabbana_atina",
            name = "Rabbana Atina fid-Dunya Hasanatan",
            arabic = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ",
            meaning = "Our Lord, give us in this world good and in the Hereafter good and protect us from the Fire",
            defaultTarget = 33,
            nameRes = R.string.dhikr_rabbana_atina_name,
            meaningRes = R.string.dhikr_rabbana_atina_meaning,
            category = "Quranic"
        ),
        DhikrItem(
            id = "rabbi_innee_lima_anzalta",
            name = "Rabbi inni lima anzalta ilayya min khayrin faqeer",
            arabic = "رَبِّ إِنِّي لِمَا أَنزَلْتَ إِلَيَّ مِنْ خَيْرٍ فَقِيرٌ",
            meaning = "My Lord, indeed I am, for whatever good You would send down to me, in need",
            defaultTarget = 33,
            nameRes = R.string.dhikr_rabbi_innee_lima_anzalta_name,
            meaningRes = R.string.dhikr_rabbi_innee_lima_anzalta_meaning,
            category = "Quranic"
        ),
        DhikrItem(
            id = "rabbir_hamhuma",
            name = "Rabbir-hamhuma kama rabbayani sagheera",
            arabic = "رَّبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا",
            meaning = "My Lord, have mercy upon my parents as they brought me up when I was small",
            defaultTarget = 33,
            nameRes = R.string.dhikr_rabbir_hamhuma_name,
            meaningRes = R.string.dhikr_rabbir_hamhuma_meaning,
            category = "Quranic"
        ),
        DhikrItem(
            id = "rabbana_hablana_min_azwajina",
            name = "Rabbana hab lana min azwajina wa dhurriyyatina",
            arabic = "رَبَّنَا هَبْ لَنَا مِنْ أَزْوَاجِنَا وَذُرِّيَّاتِنَا قُرَّةَ أَعْيُنٍ وَاجْعَلْنَا لِلْمُتَّقِينَ إِمَامًا",
            meaning = "Our Lord, grant us from among our spouses and offspring comfort to our eyes",
            defaultTarget = 33,
            nameRes = R.string.dhikr_rabbana_hablana_min_azwajina_name,
            meaningRes = R.string.dhikr_rabbana_hablana_min_azwajina_meaning,
            category = "Quranic"
        ),
        DhikrItem(
            id = "rabbi_zidni_ilma",
            name = "Rabbi Zidni 'Ilma",
            arabic = "رَّبِّ زِدْنِي عِلْمًا",
            meaning = "My Lord, increase me in knowledge",
            defaultTarget = 100,
            nameRes = R.string.dhikr_rabbi_zidni_ilma_name,
            meaningRes = R.string.dhikr_rabbi_zidni_ilma_meaning,
            category = "Quranic"
        ),
        DhikrItem(
            id = "rabbish_rahli_sadri",
            name = "Rabbish-rah li sadri wa yassir li amri",
            arabic = "رَبِّ اشْرَحْ لِي صَدْرِي وَيَسِّرْ لِي أَمْرِي",
            meaning = "My Lord, expand for me my breast and ease for me my task",
            defaultTarget = 33,
            nameRes = R.string.dhikr_rabbish_rahli_sadri_name,
            meaningRes = R.string.dhikr_rabbish_rahli_sadri_meaning,
            category = "Quranic"
        )
    )

    val TARGET_OPTIONS = listOf(33, 99, 100, 313, 500, 1000)

    val THIRTY_THREE_TIMES_THREE = listOf(
        PRESETS[0], // SubhanAllah
        PRESETS[1], // Alhamdulillah
        PRESETS[2]  // Allahu Akbar
    )
}
