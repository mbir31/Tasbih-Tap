/**
 * Tasbih Tap - Progressive Web App (PWA) Engine
 * Full feature parity: Back-tap shockwave filter, On-Screen Rocker, Keyboard/Media keys,
 * 33x3 Sunnah loop, Web Audio click, Vibration API, Wake Lock, & IndexedDB/LocalStorage.
 */

// --- DHIKR PRESETS WITH BANGLA, ENGLISH, & ARABIC LOCALIZATION ---
const DHIKR_PRESETS = [
  // --- ESSENTIAL / SUNNAH DHIKRS ---
  {
    id: "subhanallah",
    name: "SubhanAllah",
    name_bn: "সুবহানাল্লাহ",
    meaning: "Glory be to Allah",
    meaning_bn: "আল্লাহ অতি পবিত্র ও মহিমান্বিত",
    arabic: "سُبْحَانَ اللَّهِ",
    defaultTarget: 33,
    category: "essential"
  },
  {
    id: "alhamdulillah",
    name: "Alhamdulillah",
    name_bn: "আলহামদুলিল্লাহ",
    meaning: "All praise is due to Allah",
    meaning_bn: "সকল প্রশংসা একমাত্র আল্লাহর",
    arabic: "الْحَمْدُ لِلَّهِ",
    defaultTarget: 33,
    category: "essential"
  },
  {
    id: "allahu_akbar",
    name: "Allahu Akbar",
    name_bn: "আল্লাহু আকবার",
    meaning: "Allah is the Greatest",
    meaning_bn: "আল্লাহ সর্বশ্রেষ্ঠ",
    arabic: "اللَّهُ أَكْبَرُ",
    defaultTarget: 34,
    category: "essential"
  },
  {
    id: "tahlil",
    name: "La ilaha illallah",
    name_bn: "লা ইলাহা ইল্লাল্লাহ",
    meaning: "There is no deity worthy of worship except Allah",
    meaning_bn: "আল্লাহ ব্যতীত কোনো সত্য উপাস্য নেই",
    arabic: "لَا إِلٰهَ إِلَّا اللَّهُ",
    defaultTarget: 100,
    category: "essential"
  },
  {
    id: "tamjeed",
    name: "SubhanAllahi wal-Hamdulillahi wa La ilaha illallahu Wallahu Akbar",
    name_bn: "কালিমা তামজীদ",
    meaning: "Glory be to Allah, praise be to Allah, there is no god but Allah, and Allah is the Greatest",
    meaning_bn: "আল্লাহ অতি পবিত্র, সকল প্রশংসা আল্লাহর, আল্লাহ ছাড়া কোনো উপাস্য নেই এবং আল্লাহ সর্বশ্রেষ্ঠ",
    arabic: "سُبْحَانَ اللَّهِ وَالْحَمْدُ لِلَّهِ وَلَا إِلٰهَ إِلَّا اللَّهُ وَاللَّهُ أَكْبَرُ",
    defaultTarget: 100,
    category: "essential"
  },

  // --- PRAISE & GLORIFICATION (তাহমীদ ও তাসবীহ) ---
  {
    id: "subhanallah_bihamdihi",
    name: "SubhanAllahi wa bihamdihi",
    name_bn: "সুবহানাল্লাহি ওয়া বিহামদিহী",
    meaning: "Glory and praise be to Allah",
    meaning_bn: "আল্লাহর প্রশংসাসহ তাঁর পবিত্রতা ঘোষণা করছি",
    arabic: "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ",
    defaultTarget: 100,
    category: "praise"
  },
  {
    id: "subhanallah_alazeem",
    name: "SubhanAllahil Azeem",
    name_bn: "সুবহানাল্লাহিল আযীম",
    meaning: "Glory be to Allah the Magnificent",
    meaning_bn: "মহিমান্বিত মহান আল্লাহ অতি পবিত্র",
    arabic: "سُبْحَانَ اللَّهِ الْعَظِيمِ",
    defaultTarget: 100,
    category: "praise"
  },
  {
    id: "subhanallahi_wa_bihamdihi_adada",
    name: "SubhanAllahi wa bihamdihi ‘adada khalqihi",
    name_bn: "সুবহানাল্লাহি ওয়া বিহামদিহী ‘আদাদা খালকিহী",
    meaning: "Glory to Allah and praise Him, according to the number of His creation and weight of His Throne",
    meaning_bn: "তাঁর সৃষ্টির সংখ্যা পরিমাণ, সন্তুষ্টি পরিমাণ ও আরশের ওজন পরিমাণ আল্লাহর পবিত্রতা ও প্রশংসা করছি",
    arabic: "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ عَدَدَ خَلْقِهِ وَرِضَا نَفْسِهِ وَزِنَةَ عَرْشِهِ وَمِدَادَ كَلِمَاتِهِ",
    defaultTarget: 3,
    category: "praise"
  },
  {
    id: "la_ilaha_wahdahu",
    name: "La ilaha illallahu wahdahu la shareeka lah",
    name_bn: "লা ইলাহা ইল্লাল্লাহু ওয়াহদাহু লা শারীকা লাহু",
    meaning: "None has the right to be worshipped except Allah alone, with no partner or associate",
    meaning_bn: "আল্লাহ ব্যতীত কোনো সত্য উপাস্য নেই, তিনি একক, তাঁর কোনো অংশীদার নেই",
    arabic: "لَا إِلٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ",
    defaultTarget: 100,
    category: "praise"
  },
  {
    id: "ya_hayyu_ya_qayyum",
    name: "Ya Hayyu Ya Qayyum",
    name_bn: "ইয়া হাইয়্যু ইয়া কাইয়্যূম",
    meaning: "O Ever-Living, O Self-Sustaining",
    meaning_bn: "হে চিরঞ্জীব, হে সর্বসত্তার ধারক",
    arabic: "يَا حَيُّ يَا قَيُّومُ",
    defaultTarget: 100,
    category: "praise"
  },
  {
    id: "ya_dhal_jalali",
    name: "Ya Dhal Jalali wal Ikram",
    name_bn: "ইয়া যাল জালালি ওয়াল ইকরাম",
    meaning: "O Owner of Majesty and Honor",
    meaning_bn: "হে মহিমা ও পরম অনুগ্রহের অধিকারী",
    arabic: "يَا ذَا الْجَلَالِ وَالإِكْرَامِ",
    defaultTarget: 33,
    category: "praise"
  },
  {
    id: "radheetu_billah",
    name: "Radheetu Billahi Rabba",
    name_bn: "রাদীতু বিল্লাহি রব্বা",
    meaning: "I am pleased with Allah as my Lord, Islam as my religion, and Muhammad ﷺ as my Prophet",
    meaning_bn: "আমি সন্তুষ্টচিত্তে আল্লাহকে রব, ইসলামকে দ্বীন এবং মুহাম্মদ ﷺ কে রাসুল হিসেবে মেনে নিয়েছি",
    arabic: "رَضِيتُ بِاللَّهِ رَبًّا وَبِالإِسْلَامِ دِينًا وَبِمُحَمَّدٍ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ نَبِيًّا",
    defaultTarget: 3,
    category: "praise"
  },

  // --- FORGIVENESS & REPENTANCE (ইস্তিগফার ও তওবা) ---
  {
    id: "istighfar",
    name: "Astaghfirullah",
    name_bn: "আস্তাগফিরুল্লাহ",
    meaning: "I seek forgiveness from Allah",
    meaning_bn: "আমি আল্লাহর নিকট ক্ষমা প্রার্থনা করছি",
    arabic: "أَسْتَغْفِرُ اللَّهَ",
    defaultTarget: 100,
    category: "forgiveness"
  },
  {
    id: "astaghfirullah_wa_atubu",
    name: "Astaghfirullaha wa Atubu Ilayh",
    name_bn: "আস্তাগফিরুল্লাহ ওয়া আতুবু ইলাইহি",
    meaning: "I seek forgiveness of Allah and turn unto Him in repentance",
    meaning_bn: "আমি আল্লাহর ক্ষমা প্রার্থনা করছি এবং তাঁরই দিকে তওবা করছি",
    arabic: "أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ",
    defaultTarget: 100,
    category: "forgiveness"
  },
  {
    id: "sayyidul_istighfar",
    name: "Sayyidul Istighfar",
    name_bn: "সাইয়্যিদুল ইস্তিগফার",
    meaning: "Chief supplication for forgiveness",
    meaning_bn: "ক্ষমা প্রার্থনার শ্রেষ্ঠ দু'আ (সাইয়্যিদুল ইস্তিগফার)",
    arabic: "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلٰهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَىٰ عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
    defaultTarget: 3,
    category: "forgiveness"
  },
  {
    id: "rabbighfirli",
    name: "Rabbighfir li wa tub 'alayya",
    name_bn: "রব্বিগফির লী ওয়া তুব 'আলাইয়্যা",
    meaning: "My Lord! Forgive me and accept my repentance, surely You are the Accepter of Repentance, the Merciful",
    meaning_bn: "হে আমার রব! আমাকে ক্ষমা করুন ও আমার তওবা কবুল করুন, নিশ্চয় আপনি অতি তওবা কবুলকারী ও পরম দয়ালু",
    arabic: "رَبِّ اغْفِرْ لِي وَتُبْ عَلَيَّ إِنَّكَ أَنْتَ التَّوَّابُ الرَّحِيمُ",
    defaultTarget: 100,
    category: "forgiveness"
  },
  {
    id: "astaghfirullah_alazeem_alladhi",
    name: "Astaghfirullahal Azeem alladhi la ilaha illa Huwa",
    name_bn: "আস্তাগফিরুল্লাহাল 'আযীম আল্লাযী লা ইলাহা ইল্লা হু",
    meaning: "I seek forgiveness from Allah the Almighty, whom there is no deity but He, the Living, the Eternal, and I repent to Him",
    meaning_bn: "আমি আল্লাহর ক্ষমা চাই যিনি ছাড়া কোনো উপাস্য নেই, তিনি চিরঞ্জীব, সর্বধারক এবং তাঁর কাছেই তওবা করছি",
    arabic: "أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ الَّذِي لَا إِلٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ وَأَتُوبُ إِلَيْهِ",
    defaultTarget: 33,
    category: "forgiveness"
  },

  // --- SALAWAT & BLESSINGS UPON PROPHET ﷺ (দরূদ ও সালাম) ---
  {
    id: "salawat",
    name: "Allahumma Salli 'ala Muhammad",
    name_bn: "সালাওয়াত (সংক্ষিপ্ত দরূদ)",
    meaning: "O Allah, send blessings and peace upon our Prophet Muhammad",
    meaning_bn: "হে আল্লাহ! আমাদের নবী মুহাম্মদ ﷺ এর উপর শান্তি ও রহমত বর্ষিত করুন",
    arabic: "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ",
    defaultTarget: 100,
    category: "salawat"
  },
  {
    id: "durood_ibrahim",
    name: "Durood-e-Ibrahim",
    name_bn: "দরূদে ইবরাহীম",
    meaning: "The complete and most virtuous prayer for blessings upon Prophet Muhammad ﷺ",
    meaning_bn: "শ্রেষ্ঠ সালাওয়াত (নামাযে পঠিত দরূদে ইবরাহীম)",
    arabic: "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ، اللَّهُمَّ بَارِكْ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ كَمَا بَارَكْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ",
    defaultTarget: 10,
    category: "salawat"
  },
  {
    id: "sallallahu_alayhi_wa_sallam",
    name: "Sallallahu ‘Alayhi Wa Sallam",
    name_bn: "সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম",
    meaning: "May Allah honor him and grant him peace",
    meaning_bn: "তাঁর উপর আল্লাহর দরূদ ও অফুরন্ত শান্তি বর্ষিত হোক",
    arabic: "صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ",
    defaultTarget: 100,
    category: "salawat"
  },

  // --- SUPPLICATION & PROTECTION (দু'আ ও আশ্রয় প্রার্থনা) ---
  {
    id: "hawqala",
    name: "La Hawla wa la Quwwata illa Billah",
    name_bn: "লা হাওলা ওয়ালা কুওয়্যাতা ইল্লা বিল্লাহ",
    meaning: "There is no power nor strength except with Allah (a treasure of Paradise)",
    meaning_bn: "আল্লাহর সাহায্য ব্যতীত কোনো উপায় নেই এবং কোনো শক্তি নেই (জান্নাতের গুপ্তধন)",
    arabic: "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ",
    defaultTarget: 100,
    category: "supplication"
  },
  {
    id: "hasbunallah",
    name: "Hasbunallahu wa ni'mal wakeel",
    name_bn: "হাসবুনাল্লাহু ওয়া নিমাল ওয়াকিল",
    meaning: "Allah is sufficient for us, and He is the best Disposer of affairs",
    meaning_bn: "আমাদের জন্য আল্লাহই যথেষ্ট, আর তিনি কতই না উত্তম কর্মবিধায়ক",
    arabic: "حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ",
    defaultTarget: 100,
    category: "supplication"
  },
  {
    id: "bismillahil_ladhi",
    name: "Bismillahilladhi la yadurru ma'asmihi shay'",
    name_bn: "বিসমিল্লাহিল্লাযী লা ইয়াদুররু মা'আস্মিহী",
    meaning: "In the Name of Allah, with Whose Name nothing can cause harm in earth or heaven",
    meaning_bn: "আল্লাহর নামে, যাঁর নামের সাথে আসমান ও যমীনের কোনো বস্তুই অনিষ্ট করতে পারে না",
    arabic: "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ",
    defaultTarget: 3,
    category: "supplication"
  },
  {
    id: "ya_hayyu_bi_rahmatika",
    name: "Ya Hayyu Ya Qayyum bi-rahmatika astagheeth",
    name_bn: "ইয়া হাইয়্যু ইয়া কাইয়্যূম বিরাহমাতিকা আস্তাগীস",
    meaning: "O Ever-Living, O Sustainer! By Your mercy I seek assistance; rectify all my affairs",
    meaning_bn: "হে চিরঞ্জীব, হে সর্বধারক! আপনার রহমতের উসিলায় সাহায্য চাই, আমার সকল কাজ সঠিক করে দিন",
    arabic: "يَا حَيُّ يَا قَيُّومُ بِرَحْمَتِكَ أَسْتَغِيثُ أَصْلِحْ لِي شَأْنِي كُلَّهُ وَلَا تَكِلْنِي إِلَىٰ نَفْسِي طَرْفَةَ عَيْنٍ",
    defaultTarget: 3,
    category: "supplication"
  },
  {
    id: "allahumma_ajirni",
    name: "Allahumma ajirni minan-nar",
    name_bn: "আল্লাহুম্মা আজিরনী মিনান-নার",
    meaning: "O Allah, save me from the Fire of Hell",
    meaning_bn: "হে আল্লাহ! আমাকে জাহান্নামের আগুন থেকে রক্ষা করুন",
    arabic: "اللَّهُمَّ أَجِرْنِي مِنَ النَّارِ",
    defaultTarget: 7,
    category: "supplication"
  },
  {
    id: "allahumma_innee_as_aluka_jannah",
    name: "Allahumma inni as'alukal-Jannah",
    name_bn: "আল্লাহুম্মা ইন্নী আসআলুকাল জান্নাহ",
    meaning: "O Allah, I ask You for Paradise",
    meaning_bn: "হে আল্লাহ! আমি আপনার নিকট জান্নাত প্রার্থনা করছি",
    arabic: "اللَّهُمَّ إِنِّي أَسْأَلُكَ الْجَنَّةَ",
    defaultTarget: 7,
    category: "supplication"
  },
  {
    id: "allahumma_inffaka_afuwwun",
    name: "Allahumma innaka 'Afuwwun tuhibbul 'afwa fa'fu 'anni",
    name_bn: "আল্লাহুম্মা ইন্নাকা আফুওয়্যুন",
    meaning: "O Allah, You are Most Forgiving, and You love to forgive, so forgive me",
    meaning_bn: "হে আল্লাহ! নিশ্চয় আপনি ক্ষমাশীল, ক্ষমাকে ভালোবাসেন, অতএব আমাকে ক্ষমা করে দিন",
    arabic: "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي",
    defaultTarget: 33,
    category: "supplication"
  },

  // --- QURANIC DUAS (কুরআনের শ্রেষ্ঠ দু'আসমূহ) ---
  {
    id: "yunus_dua",
    name: "Ayat e Kareema (Dua of Prophet Yunus)",
    name_bn: "আয়াতে কারীমা (ইউনুস আ. এর দু'আ)",
    meaning: "There is no deity except You; Exalted are You. Indeed, I have been of the wrongdoers",
    meaning_bn: "তুমি ব্যতীত কোনো সত্য উপাস্য নেই, তুমি অতি পবিত্র! নিশ্চয় আমি অপরাধীদের অন্তর্ভুক্ত",
    arabic: "لَّا إِلٰهَ إِلَّا أَنتَ سُبْحَانَكَ إِنِّي كُنتُ مِنَ الظَّالِمِينَ",
    defaultTarget: 100,
    category: "quranic"
  },
  {
    id: "rabbana_atina",
    name: "Rabbana Atina fid-Dunya Hasanatan",
    name_bn: "রব্বানা আতিনা ফিদ্দুনইয়া হাসানাহ",
    meaning: "Our Lord, give us in this world good and in the Hereafter good and protect us from the Fire",
    meaning_bn: "হে আমাদের প্রতিপালক! আমাদের ইহকালে কল্যাণ দিন, পরকালেও কল্যাণ দিন এবং দোযখের আযাব থেকে রক্ষা করুন",
    arabic: "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ",
    defaultTarget: 33,
    category: "quranic"
  },
  {
    id: "rabbi_innee_lima_anzalta",
    name: "Rabbi inni lima anzalta ilayya min khayrin faqeer",
    name_bn: "দু'আ মূসা (আ.) - রব্বি ইন্নী লিমা আনযালতা",
    meaning: "My Lord, indeed I am, for whatever good You would send down to me, in need",
    meaning_bn: "হে আমার পালনকর্তা, আপনি আমার প্রতি যে অনুগ্রহই নাযিল করবেন, আমি তার মুখাপেক্ষী",
    arabic: "رَبِّ إِنِّي لِمَا أَنزَلْتَ إِلَيَّ مِنْ خَيْرٍ فَقِيرٌ",
    defaultTarget: 33,
    category: "quranic"
  },
  {
    id: "rabbir_hamhuma",
    name: "Rabbir-hamhuma kama rabbayani sagheera",
    name_bn: "পিতামাতার জন্য দু'আ - রব্বির হামহুমা",
    meaning: "My Lord, have mercy upon my parents as they brought me up when I was small",
    meaning_bn: "হে আমার প্রতিপালক! পিতা-মাতার প্রতি দয়া করুন যেমন তাঁরা শৈশবে আমাকে স্নেহভরে প্রতিপালন করেছেন",
    arabic: "رَّبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا",
    defaultTarget: 33,
    category: "quranic"
  },
  {
    id: "rabbana_hablana_min_azwajina",
    name: "Rabbana hab lana min azwajina wa dhurriyyatina",
    name_bn: "পরিবারের কল্যাণে দু'আ - রব্বানা হাবলানা",
    meaning: "Our Lord, grant us from among our spouses and offspring comfort to our eyes",
    meaning_bn: "হে আমাদের রব! আমাদের জীবনসঙ্গী ও সন্তানদের আমাদের জন্য নয়নপ্রীতিকর করুন এবং মুত্তাকীদের নেতা করুন",
    arabic: "رَبَّنَا هَبْ لَنَا مِنْ أَزْوَاجِنَا وَذُرِّيَّاتِنَا قُرَّةَ أَعْيُنٍ وَاجْعَلْنَا لِلْمُتَّقِينَ إِمَامًا",
    defaultTarget: 33,
    category: "quranic"
  },
  {
    id: "rabbi_zidni_ilma",
    name: "Rabbi Zidni 'Ilma",
    name_bn: "রব্বি যিদনী ইলমা",
    meaning: "My Lord, increase me in knowledge",
    meaning_bn: "হে আমার পালনকর্তা! আমার জ্ঞান বৃদ্ধি করে দিন",
    arabic: "رَّبِّ زِدْنِي عِلْمًا",
    defaultTarget: 100,
    category: "quranic"
  },
  {
    id: "rabbish_rahli_sadri",
    name: "Rabbish-rah li sadri wa yassir li amri",
    name_bn: "রব্বিশ রাহলী সদরী",
    meaning: "My Lord, expand for me my breast and ease for me my task",
    meaning_bn: "হে আমার রব! আমার বক্ষ প্রশস্ত করে দিন এবং আমার কাজ সহজ করে দিন",
    arabic: "رَبِّ اشْرَحْ لِي صَدْرِي وَيَسِّرْ لِي أَمْرِي",
    defaultTarget: 33,
    category: "quranic"
  }
];

// --- APP STATE ---
const state = {
  activeDhikrIndex: 0,
  count: 0,
  target: 33,
  round: 1,
  isUnlimited: false,
  is33x3Mode: false,
  sunnahStep: 0, // 0: SubhanAllah (33), 1: Alhamdulillah (33), 2: Allahu Akbar (34)
  theme: "emerald",
  language: "bn", // 'bn' or 'en'
  backTapEnabled: true,
  backTapSensitivity: "medium", // 'low', 'medium', 'high'
  hapticEnabled: true,
  hapticStrength: "medium", // 'light', 'medium', 'strong'
  soundEnabled: false,
  keepScreenAwake: true,
  customDhikrs: [],
  history: []
};

// --- INITIALIZE FROM LOCALSTORAGE ---
function loadPersistedState() {
  try {
    const saved = localStorage.getItem('tasbih_tap_state');
    if (saved) {
      const parsed = JSON.parse(saved);
      Object.assign(state, parsed);
    }
  } catch (e) {
    console.warn("Could not load state from localStorage", e);
  }
}

function savePersistedState() {
  try {
    localStorage.setItem('tasbih_tap_state', JSON.stringify({
      activeDhikrIndex: state.activeDhikrIndex,
      count: state.count,
      target: state.target,
      round: state.round,
      isUnlimited: state.isUnlimited,
      is33x3Mode: state.is33x3Mode,
      sunnahStep: state.sunnahStep,
      theme: state.theme,
      language: state.language,
      backTapEnabled: state.backTapEnabled,
      backTapSensitivity: state.backTapSensitivity,
      hapticEnabled: state.hapticEnabled,
      hapticStrength: state.hapticStrength,
      soundEnabled: state.soundEnabled,
      keepScreenAwake: state.keepScreenAwake,
      customDhikrs: state.customDhikrs,
      history: state.history
    }));
  } catch (e) {
    console.warn("Could not save state", e);
  }
}

// --- AUDIO SYNTHESIS (Natural Wooden Bead Click) ---
let audioCtx = null;
function playBeadClickSound() {
  if (!state.soundEnabled) return;
  try {
    if (!audioCtx) {
      audioCtx = new (window.AudioContext || window.webkitAudioContext)();
    }
    if (audioCtx.state === 'suspended') {
      audioCtx.resume();
    }
    const osc = audioCtx.createOscillator();
    const gain = audioCtx.createGain();
    osc.type = 'triangle';
    osc.frequency.setValueAtTime(440, audioCtx.currentTime);
    osc.frequency.exponentialRampToValueAtTime(120, audioCtx.currentTime + 0.05);

    gain.gain.setValueAtTime(0.3, audioCtx.currentTime);
    gain.gain.exponentialRampToValueAtTime(0.001, audioCtx.currentTime + 0.05);

    osc.connect(gain);
    gain.connect(audioCtx.destination);
    osc.start();
    osc.stop(audioCtx.currentTime + 0.05);
  } catch (e) {
    console.warn("Audio synthesis error", e);
  }
}

// --- HAPTIC FEEDBACK ENGINE ---
function triggerHaptic(isMilestone = false) {
  if (!state.hapticEnabled || !navigator.vibrate) return;
  if (isMilestone) {
    navigator.vibrate([60, 40, 60, 40, 120]);
    return;
  }
  const durations = { light: 15, medium: 28, strong: 45 };
  navigator.vibrate(durations[state.hapticStrength] || 28);
}

// --- WAKE LOCK API ---
let wakeLock = null;
async function requestWakeLock() {
  if (!state.keepScreenAwake || !('wakeLock' in navigator)) return;
  try {
    wakeLock = await navigator.wakeLock.request('screen');
    wakeLock.addEventListener('release', () => { wakeLock = null; });
  } catch (err) {
    console.log("WakeLock notice:", err.message);
  }
}

document.addEventListener('visibilitychange', () => {
  if (document.visibilityState === 'visible' && state.keepScreenAwake) {
    requestWakeLock();
  }
});

// --- CORE COUNT ACTIONS ---
function getActiveDhikr() {
  if (state.activeDhikrIndex >= 1000) {
    const custom = state.customDhikrs.find(c => c.id === state.activeDhikrIndex);
    if (custom) return custom;
  }
  return DHIKR_PRESETS[state.activeDhikrIndex] || DHIKR_PRESETS[0];
}

function increment(source = 'tap') {
  state.count++;
  playBeadClickSound();

  const active = getActiveDhikr();
  const currentTarget = state.target;

  // Pulse animation
  const container = document.getElementById('circular-container');
  if (container) {
    container.classList.add('pulse-active');
    setTimeout(() => container.classList.remove('pulse-active'), 120);
  }

  // Check target completion
  if (!state.isUnlimited && state.count >= currentTarget) {
    handleTargetCompleted();
  } else {
    triggerHaptic(false);
  }

  renderUI();
  savePersistedState();
}

function decrement() {
  if (state.count > 0) {
    state.count--;
    triggerHaptic(false);
    renderUI();
    savePersistedState();
  }
}

function resetCounter() {
  if (confirm(state.language === 'bn' ? "বর্তমান গণনা রিসেট করতে চান?" : "Reset current count?")) {
    state.count = 0;
    state.round = 1;
    if (state.is33x3Mode) {
      state.sunnahStep = 0;
      state.activeDhikrIndex = 0;
      state.target = 33;
    }
    triggerHaptic(false);
    renderUI();
    savePersistedState();
  }
}

function handleTargetCompleted() {
  triggerHaptic(true);
  showMilestoneToast(state.language === 'bn' ? "আলহামদুলিল্লাহ! লক্ষ্য সম্পন্ন!" : "Target Reached! Alhamdulillah");

  // Record session in history
  const active = getActiveDhikr();
  state.history.unshift({
    id: Date.now(),
    dhikrName: state.language === 'bn' ? (active.name_bn || active.name) : active.name,
    arabic: active.arabic,
    count: state.count,
    target: state.target,
    round: state.round,
    timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
  });
  if (state.history.length > 100) state.history.pop();

  if (state.is33x3Mode) {
    state.sunnahStep = (state.sunnahStep + 1) % 3;
    state.activeDhikrIndex = state.sunnahStep; // 0: SubhanAllah, 1: Alhamdulillah, 2: Allahu Akbar
    state.target = state.sunnahStep === 2 ? 34 : 33;
    state.count = 0;
    if (state.sunnahStep === 0) {
      state.round++;
    }
  } else {
    state.count = 0;
    state.round++;
  }
}

function showMilestoneToast(text) {
  const toast = document.getElementById('milestone-toast');
  if (toast) {
    toast.textContent = text;
    toast.classList.add('show');
    setTimeout(() => toast.classList.remove('show'), 2500);
  }
}

// --- UI RENDERING ---
function renderUI() {
  const active = getActiveDhikr();
  const isBn = state.language === 'bn';

  // Apply theme
  document.documentElement.setAttribute('data-theme', state.theme);

  // Top Card
  document.getElementById('dhikr-card-title').textContent = isBn ? (active.name_bn || active.name) : active.name;
  document.getElementById('dhikr-card-arabic').textContent = active.arabic || '';

  const targetBadge = document.getElementById('dhikr-card-target');
  if (state.isUnlimited) {
    targetBadge.textContent = isBn ? "∞ আনলিমিটেড" : "∞ Unlimited";
  } else if (state.is33x3Mode) {
    targetBadge.textContent = isBn ? `৩৩×৩ • ধাপ ${state.sunnahStep + 1}/৩` : `33×3 • Step ${state.sunnahStep + 1}/3`;
  } else {
    targetBadge.textContent = isBn ? `লক্ষ্য: ${state.target}` : `Target: ${state.target}`;
  }

  // Mode banner / Round
  const modeBanner = document.getElementById('mode-banner');
  if (state.is33x3Mode) {
    modeBanner.textContent = isBn 
      ? `সুন্নাহ ৩৩×৩ মোড • রাউন্ড ${state.round}`
      : `Sunnah 33×3 Mode • Round ${state.round}`;
  } else if (state.isUnlimited) {
    modeBanner.textContent = isBn ? "মুক্ত যিকির (আনলিমিটেড মোড)" : "Freeform Unlimited Mode";
  } else {
    modeBanner.textContent = isBn ? `রাউন্ড ${state.round}` : `Round ${state.round}`;
  }

  // Center Counter
  const arabicMainEl = document.getElementById('arabic-main');
  if (arabicMainEl) {
    arabicMainEl.textContent = active.arabic || '';
    if ((active.arabic || '').length > 35) {
      arabicMainEl.classList.add('arabic-long');
    } else {
      arabicMainEl.classList.remove('arabic-long');
    }
  }
  document.getElementById('translit-main').textContent = isBn ? (active.meaning_bn || active.name_bn) : (active.meaning || active.name);
  document.getElementById('count-number').textContent = state.count;

  // Target indicator
  const targetLabel = document.getElementById('target-badge-main');
  if (state.isUnlimited) {
    targetLabel.textContent = isBn ? "আনলিমিটেড" : "UNLIMITED";
  } else {
    targetLabel.textContent = isBn ? `লক্ষ্য: ${state.target}` : `GOAL: ${state.target}`;
  }

  // Progress ring
  const circle = document.getElementById('progress-ring-indicator');
  if (circle) {
    const circumference = 2 * Math.PI * 125; // r = 125 -> ~785.4
    if (state.isUnlimited) {
      circle.style.strokeDashoffset = '0';
    } else {
      const progress = Math.min(state.count / state.target, 1);
      const offset = circumference - (progress * circumference);
      circle.style.strokeDashoffset = offset.toString();
    }
  }

  // Bottom buttons and helper texts
  document.getElementById('thumb-btn-text').textContent = isBn ? "এখানে ট্যাপ করুন বা পেছনে ট্যাপ করুন" : "TAP HERE OR TAP BACK";
  document.getElementById('rocker-hint-text').textContent = isBn 
    ? "ভলিউম বাটন, কিবোর্ড (Space / ↑) বা স্ক্রিনে ট্যাপ করে গণনা করুন"
    : "Use Volume Rocker, Keyboard (Space/↑), or Tap to count";

  // Back-tap sensitivity UI sync
  const sensitivityRow = document.getElementById('back-tap-sensitivity-row');
  if (sensitivityRow) {
    sensitivityRow.style.display = state.backTapEnabled ? 'flex' : 'none';
  }
  const sensLabel = document.getElementById('sensitivity-label');
  const sensDesc = document.getElementById('sensitivity-desc');
  if (sensLabel) {
    sensLabel.textContent = isBn ? "ব্যাক-ট্যাপ সেনসিটিভিটি" : "Back-Tap Sensitivity";
  }
  if (sensDesc) {
    sensDesc.textContent = isBn ? "আপনার ডিভাইসের জন্য ট্যাপের মাত্রা নির্ধারণ করুন" : "Adjust sensor sensitivity for your device";
  }
  document.querySelectorAll('#sensitivity-segmented .seg-btn').forEach(btn => {
    const s = btn.getAttribute('data-sens');
    if (isBn) {
      btn.textContent = s === 'low' ? 'কম' : (s === 'high' ? 'বেশি' : 'মাঝারি');
    } else {
      btn.textContent = s === 'low' ? 'Low' : (s === 'high' ? 'High' : 'Medium');
    }
    if (s === state.backTapSensitivity) {
      btn.classList.add('active');
    } else {
      btn.classList.remove('active');
    }
  });

  // Settings Install texts
  const instTitle = document.getElementById('settings-install-title');
  const instDesc = document.getElementById('settings-install-desc');
  const instBtn = document.getElementById('settings-install-btn');
  if (instTitle) {
    instTitle.textContent = isBn ? "মোবাইলে অ্যাপ ইনস্টল করুন" : "Install App on Device";
  }
  if (instDesc) {
    instDesc.textContent = isBn ? "হোম স্ক্রিন থেকে সরাসরি অফলাইনে ব্যবহার করতে ১-ট্যাপে ইনস্টল করুন" : "1-Tap install to use offline directly from your home screen";
  }
  if (instBtn) {
    instBtn.textContent = isBn ? "ইনস্টল" : "Install";
  }

  // Category chips translation
  const catTranslations = {
    all: { bn: "সকল", en: "All" },
    essential: { bn: "প্রয়োজনীয়", en: "Essential" },
    praise: { bn: "প্রশংসা", en: "Praise" },
    forgiveness: { bn: "ইস্তিগফার", en: "Forgiveness" },
    salawat: { bn: "সালাওয়াত", en: "Salawat" },
    supplication: { bn: "দু'আ", en: "Duas" },
    quranic: { bn: "কুরআনি দু'আ", en: "Quranic" }
  };
  document.querySelectorAll('.chip').forEach(chip => {
    const cat = chip.getAttribute('data-cat');
    if (cat && catTranslations[cat]) {
      chip.textContent = isBn ? catTranslations[cat].bn : catTranslations[cat].en;
    }
  });

  // Search input placeholder
  const searchInput = document.getElementById('dhikr-search-input');
  if (searchInput) {
    searchInput.placeholder = isBn ? "যিকির বা অর্থ অনুসন্ধান করুন…" : "Search Dhikr or meaning…";
  }
}

// --- HARDWARE & DEVICE MOTION BACK-TAP DETECTION ---
let lastTapTime = 0;
let lastZ = 0;
const SENSITIVITIES = {
  low: 16.0,
  medium: 11.5,
  high: 8.0
};

function initBackTapDetection() {
  if (!window.DeviceMotionEvent) {
    console.log("DeviceMotionEvent not supported on this device.");
    return;
  }

  window.addEventListener('devicemotion', (event) => {
    if (!state.backTapEnabled) return;
    const accel = event.accelerationIncludingGravity || event.acceleration;
    if (!accel) return;

    const currentZ = accel.z || 0;
    const deltaZ = Math.abs(currentZ - lastZ);
    lastZ = currentZ;

    const threshold = SENSITIVITIES[state.backTapSensitivity] || 11.5;
    const now = Date.now();

    // Shockwave filter: sharp Z-axis impulse and debounce interval (320ms)
    if (deltaZ > threshold && (now - lastTapTime > 320)) {
      lastTapTime = now;
      increment('back_tap');
    }
  }, { passive: true });
}

// Request permission for iOS 13+ motion sensors
async function requestMotionPermission() {
  if (typeof DeviceMotionEvent !== 'undefined' && typeof DeviceMotionEvent.requestPermission === 'function') {
    try {
      const response = await DeviceMotionEvent.requestPermission();
      if (response === 'granted') {
        initBackTapDetection();
      }
    } catch (e) {
      console.warn("Motion permission error", e);
    }
  } else {
    initBackTapDetection();
  }
}

// --- PHYSICAL VOLUME BUTTONS & KEYBOARD COUNTING ---
function initKeyboardAndMediaControls() {
  // Listen for desktop & keyboard inputs
  window.addEventListener('keydown', (e) => {
    if (['Space', 'ArrowUp', 'Enter'].includes(e.code)) {
      e.preventDefault();
      increment('keyboard');
    } else if (e.code === 'ArrowDown') {
      e.preventDefault();
      decrement();
    }
  });

  // MediaSession API integration (Headset / Bluetooth volume & media buttons)
  if ('mediaSession' in navigator) {
    try {
      navigator.mediaSession.metadata = new MediaMetadata({
        title: "Tasbih Tap",
        artist: "Dhikr Counter",
        album: "Islamic Remembrance"
      });

      navigator.mediaSession.setActionHandler('nexttrack', () => increment('media_btn'));
      navigator.mediaSession.setActionHandler('previoustrack', () => decrement());
      navigator.mediaSession.setActionHandler('play', () => increment('media_btn'));
    } catch (e) {
      console.log("MediaSession handler error", e);
    }
  }
}

// --- MODAL DIALOGS (Dhikr Sheet, History, Settings) ---
function openModal(id) {
  const modal = document.getElementById(id);
  if (modal) modal.classList.add('active');
}

function closeModal(id) {
  const modal = document.getElementById(id);
  if (modal) modal.classList.remove('active');
}

function renderDhikrSelectionList(filterCategory = 'all', searchQuery = '') {
  const container = document.getElementById('dhikr-list-container');
  if (!container) return;
  container.innerHTML = '';

  const isBn = state.language === 'bn';
  const allList = [...DHIKR_PRESETS, ...state.customDhikrs];

  const filtered = allList.filter(item => {
    const matchCategory = filterCategory === 'all' || item.category === filterCategory;
    const nameSearch = (item.name || '').toLowerCase() + (item.name_bn || '').toLowerCase();
    const matchSearch = !searchQuery || nameSearch.includes(searchQuery.toLowerCase());
    return matchCategory && matchSearch;
  });

  filtered.forEach(item => {
    const isSelected = (state.activeDhikrIndex === item.id) || 
                       (typeof state.activeDhikrIndex === 'number' && DHIKR_PRESETS[state.activeDhikrIndex] && DHIKR_PRESETS[state.activeDhikrIndex].id === item.id);

    const card = document.createElement('div');
    card.className = `dhikr-item-card ${isSelected ? 'selected' : ''}`;
    card.innerHTML = `
      <div class="dhikr-item-info">
        <span class="dhikr-item-name">${isBn ? (item.name_bn || item.name) : item.name}</span>
        <span class="dhikr-item-meaning">${isBn ? (item.meaning_bn || '') : (item.meaning || '')}</span>
      </div>
      <div class="dhikr-item-arabic">${item.arabic || ''}</div>
    `;

    card.addEventListener('click', () => {
      const idx = DHIKR_PRESETS.findIndex(p => p.id === item.id);
      if (idx !== -1) {
        state.activeDhikrIndex = idx;
        state.target = item.defaultTarget || 33;
      } else {
        state.activeDhikrIndex = item.id;
        state.target = item.defaultTarget || 100;
      }
      state.count = 0;
      state.round = 1;
      state.is33x3Mode = false;
      closeModal('dhikr-sheet-modal');
      renderUI();
      savePersistedState();
    });

    container.appendChild(card);
  });
}

function renderHistoryList() {
  const container = document.getElementById('history-items-container');
  if (!container) return;
  container.innerHTML = '';

  if (state.history.length === 0) {
    container.innerHTML = `<div style="text-align: center; color: var(--text-muted); padding: 30px 0;">
      ${state.language === 'bn' ? "এখনো কোনো যিকির রেকর্ড নেই।" : "No recorded Dhikr history yet."}
    </div>`;
    return;
  }

  state.history.forEach(item => {
    const card = document.createElement('div');
    card.className = 'dhikr-item-card';
    card.innerHTML = `
      <div class="dhikr-item-info">
        <span class="dhikr-item-name">${item.dhikrName}</span>
        <span class="dhikr-item-meaning">${item.timestamp} • ${state.language === 'bn' ? `রাউন্ড ${item.round}` : `Round ${item.round}`}</span>
      </div>
      <div style="font-weight: 800; font-size: 1.1rem; color: var(--accent-gold);">
        ${item.count} / ${item.target}
      </div>
    `;
    container.appendChild(card);
  });
}

// --- DOM CONTENT LOADED INITIALIZATION ---
document.addEventListener('DOMContentLoaded', () => {
  loadPersistedState();
  renderUI();
  requestWakeLock();
  initBackTapDetection();
  initKeyboardAndMediaControls();

  // Tap Canvas Click
  const canvas = document.getElementById('counter-canvas');
  if (canvas) {
    canvas.addEventListener('click', (e) => {
      // Don't trigger if clicked inside controls
      if (e.target.closest('.controls-row')) return;
      increment('screen_tap');
    });
  }

  // Thumb Big Button
  const thumbBtn = document.getElementById('big-thumb-btn');
  if (thumbBtn) {
    thumbBtn.addEventListener('click', () => increment('thumb_btn'));
  }

  // Floating Rocker Buttons
  const rockerPlus = document.getElementById('rocker-plus');
  if (rockerPlus) {
    rockerPlus.addEventListener('click', () => increment('rocker_plus'));
  }
  const rockerMinus = document.getElementById('rocker-minus');
  if (rockerMinus) {
    rockerMinus.addEventListener('click', () => decrement());
  }

  // Undo & Reset Buttons
  document.getElementById('undo-btn')?.addEventListener('click', (e) => {
    e.stopPropagation();
    decrement();
  });
  document.getElementById('reset-btn')?.addEventListener('click', (e) => {
    e.stopPropagation();
    resetCounter();
  });

  // Top Card Click -> Open Dhikr Sheet
  document.getElementById('dhikr-selector-card')?.addEventListener('click', () => {
    renderDhikrSelectionList();
    openModal('dhikr-sheet-modal');
  });

  // History Action Button
  document.getElementById('history-btn')?.addEventListener('click', () => {
    renderHistoryList();
    openModal('history-modal');
  });

  // Settings Action Button
  document.getElementById('settings-btn')?.addEventListener('click', () => {
    openModal('settings-modal');
  });

  // Close buttons
  document.querySelectorAll('.close-modal-btn').forEach(btn => {
    btn.addEventListener('click', (e) => {
      const modal = e.target.closest('.modal-overlay');
      if (modal) modal.classList.remove('active');
    });
  });

  // Category filter chips in Dhikr Sheet
  let activeCat = 'all';
  document.querySelectorAll('.chip').forEach(chip => {
    chip.addEventListener('click', (e) => {
      document.querySelectorAll('.chip').forEach(c => c.classList.remove('active'));
      e.target.classList.add('active');
      activeCat = e.target.getAttribute('data-cat') || 'all';
      const searchVal = document.getElementById('dhikr-search-input')?.value || '';
      renderDhikrSelectionList(activeCat, searchVal);
    });
  });

  // Search input in Dhikr Sheet
  document.getElementById('dhikr-search-input')?.addEventListener('input', (e) => {
    renderDhikrSelectionList(activeCat, e.target.value);
  });

  // 33x3 Mode toggle card in sheet
  document.getElementById('mode-33x3-card')?.addEventListener('click', () => {
    state.is33x3Mode = true;
    state.isUnlimited = false;
    state.sunnahStep = 0;
    state.activeDhikrIndex = 0;
    state.target = 33;
    state.count = 0;
    state.round = 1;
    closeModal('dhikr-sheet-modal');
    renderUI();
    savePersistedState();
  });

  // Unlimited mode toggle button in sheet
  document.getElementById('mode-unlimited-btn')?.addEventListener('click', () => {
    state.isUnlimited = !state.isUnlimited;
    state.is33x3Mode = false;
    closeModal('dhikr-sheet-modal');
    renderUI();
    savePersistedState();
  });

  // Settings toggles & options
  const backTapToggle = document.getElementById('toggle-back-tap');
  if (backTapToggle) {
    backTapToggle.checked = state.backTapEnabled;
    backTapToggle.addEventListener('change', (e) => {
      state.backTapEnabled = e.target.checked;
      if (state.backTapEnabled) requestMotionPermission();
      savePersistedState();
    });
  }

  const hapticToggle = document.getElementById('toggle-haptic');
  if (hapticToggle) {
    hapticToggle.checked = state.hapticEnabled;
    hapticToggle.addEventListener('change', (e) => {
      state.hapticEnabled = e.target.checked;
      savePersistedState();
    });
  }

  const soundToggle = document.getElementById('toggle-sound');
  if (soundToggle) {
    soundToggle.checked = state.soundEnabled;
    soundToggle.addEventListener('change', (e) => {
      state.soundEnabled = e.target.checked;
      savePersistedState();
    });
  }

  const wakeLockToggle = document.getElementById('toggle-wakelock');
  if (wakeLockToggle) {
    wakeLockToggle.checked = state.keepScreenAwake;
    wakeLockToggle.addEventListener('change', (e) => {
      state.keepScreenAwake = e.target.checked;
      if (state.keepScreenAwake) requestWakeLock();
      savePersistedState();
    });
  }

  // Theme selection cards
  document.querySelectorAll('.theme-card').forEach(card => {
    card.addEventListener('click', () => {
      const theme = card.getAttribute('data-theme');
      if (theme) {
        state.theme = theme;
        renderUI();
        savePersistedState();
      }
    });
  });

  // Clear History
  document.getElementById('clear-history-btn')?.addEventListener('click', () => {
    if (confirm(state.language === 'bn' ? "সকল হিস্ট্রি মুছে ফেলতে চান?" : "Clear all history?")) {
      state.history = [];
      renderHistoryList();
      savePersistedState();
    }
  });

  // Back-Tap Sensitivity selection
  document.querySelectorAll('#sensitivity-segmented .seg-btn').forEach(btn => {
    btn.addEventListener('click', (e) => {
      const sens = e.target.getAttribute('data-sens');
      if (sens && SENSITIVITIES[sens]) {
        state.backTapSensitivity = sens;
        document.querySelectorAll('#sensitivity-segmented .seg-btn').forEach(b => b.classList.remove('active'));
        e.target.classList.add('active');
        savePersistedState();
        showToast(state.language === 'bn' ? `সেনসিটিভিটি: ${e.target.textContent}` : `Sensitivity: ${sens.toUpperCase()}`);
      }
    });
  });

  // Language toggle (Bangla / English)
  document.getElementById('lang-toggle-btn')?.addEventListener('click', () => {
    state.language = state.language === 'bn' ? 'en' : 'bn';
    renderUI();
    savePersistedState();
  });

  // --- ONE-TAP PWA INSTALLATION PROMPT ---
  let deferredInstallPrompt = null;
  const headerInstallBtn = document.getElementById('install-app-btn');
  const settingsInstallBtn = document.getElementById('settings-install-btn');

  window.addEventListener('beforeinstallprompt', (e) => {
    // Prevent default mini-infobar or banner
    e.preventDefault();
    deferredInstallPrompt = e;
    // Show one-tap install button in top header and enable in settings
    if (headerInstallBtn) headerInstallBtn.style.display = 'flex';
    console.log("PWA beforeinstallprompt captured, ready for 1-tap install");
  });

  // Check if app is already running as installed standalone PWA
  const isStandalone = window.matchMedia('(display-mode: standalone)').matches || window.navigator.standalone === true;
  if (isStandalone) {
    if (headerInstallBtn) headerInstallBtn.style.display = 'none';
    const settingsSection = document.getElementById('settings-install-section');
    if (settingsSection) settingsSection.style.display = 'none';
  } else {
    // Even if beforeinstallprompt is pending, show header button so users can tap anytime
    if (headerInstallBtn) headerInstallBtn.style.display = 'flex';
  }

  async function triggerOneTapInstall() {
    if (deferredInstallPrompt) {
      deferredInstallPrompt.prompt();
      const { outcome } = await deferredInstallPrompt.userChoice;
      console.log(`User response to install prompt: ${outcome}`);
      if (outcome === 'accepted') {
        showToast(state.language === 'bn' ? "ইনস্টল সম্পন্ন হচ্ছে! জাযাকাল্লাহু খাইরান" : "Installing app... JazakAllah Khair!");
        if (headerInstallBtn) headerInstallBtn.style.display = 'none';
        const settingsSection = document.getElementById('settings-install-section');
        if (settingsSection) settingsSection.style.display = 'none';
      }
      deferredInstallPrompt = null;
    } else {
      // Fallback instruction for iOS Safari or browsers without beforeinstallprompt
      const isIOS = /iPad|iPhone|iPod/.test(navigator.userAgent) && !window.MSStream;
      if (isIOS) {
        alert(state.language === 'bn' 
          ? "আইফোনে ইনস্টল করতে:\n১. Safari এর নিচে Share বাটনে (স্কয়ার আইকন) ট্যাপ করুন।\n২. নিচে স্ক্রোল করে 'Add to Home Screen' চাপুন।" 
          : "To install on iPhone:\n1. Tap the Share button in Safari.\n2. Scroll and select 'Add to Home Screen'.");
      } else {
        alert(state.language === 'bn'
          ? "ব্রাউজারের মেনু (⋮) থেকে 'Install application' বা 'Add to Home screen' নির্বাচন করুন।"
          : "Tap browser menu (⋮) and select 'Install app' or 'Add to Home screen'.");
      }
    }
  }

  headerInstallBtn?.addEventListener('click', triggerOneTapInstall);
  settingsInstallBtn?.addEventListener('click', triggerOneTapInstall);

  window.addEventListener('appinstalled', () => {
    console.log('Tasbih Tap PWA installed successfully!');
    if (headerInstallBtn) headerInstallBtn.style.display = 'none';
    const settingsSection = document.getElementById('settings-install-section');
    if (settingsSection) settingsSection.style.display = 'none';
    showToast(state.language === 'bn' ? "অ্যাপ সফলভাবে ইনস্টল হয়েছে!" : "App installed successfully!");
  });

  // Register PWA Service Worker
  if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('./sw.js')
      .then(() => console.log("Tasbih Tap ServiceWorker Registered!"))
      .catch((err) => console.log("ServiceWorker Registration Failed:", err));
  }
});
