<div align="center">

# ﷽
### *بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ*
> *"Unquestionably, by the remembrance of Allah hearts are assured."*  
> — **Surah Ar-Ra'd (13:28)**

<br/>

# 📿 Tasbih Tap (تسبيح تـاب)
### **Modern, Mindful & Distraction-Free Dual-Platform Tasbih (PWA & Native Android)**

[![Live Demo](https://img.shields.io/badge/Live%20PWA-tasbihtap.vercel.app-00dfa2?style=for-the-badge&logo=vercel&logoColor=black)](https://tasbihtap.vercel.app/)
[![Android](https://img.shields.io/badge/Platform-Android%20Compose-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://github.com/mbir31/Tasbih-Tap)
[![PWA](https://img.shields.io/badge/App-Progressive%20Web%20App-purple?style=for-the-badge&logo=pwa&logoColor=white)](https://web.dev/progressive-web-apps/)
[![Offline First](https://img.shields.io/badge/Offline-100%25%20Offline-success?style=for-the-badge)](https://tasbihtap.vercel.app/)
[![Ad-Free](https://img.shields.io/badge/Ads-100%25%20Ad--Free-brightgreen?style=for-the-badge)](https://tasbihtap.vercel.app/)
[![Privacy](https://img.shields.io/badge/Privacy-Zero%20Tracking-blue?style=for-the-badge)](https://tasbihtap.vercel.app/)
[![License](https://img.shields.io/badge/License-MIT-amber?style=for-the-badge)](LICENSE)

<br/>

### 🌐 **Live Web App / সরাসরি ব্যবহার করুন**:  
👉 **[https://tasbihtap.vercel.app](https://tasbihtap.vercel.app/)** 👈

<br/>

**Tasbih Tap** is a beautifully crafted, distraction-free Islamic Tasbih application built in two distinct, high-performance implementations:
1. **Standalone Progressive Web App (PWA)**: Runs anywhere on iOS, Android, and Desktop with zero installation requirements.
2. **Native Android Jetpack Compose App**: Modern Android architecture with Room database, hardware accelerometer sensors, dynamic theming, and edge-to-edge Material 3.

Featuring **Back-of-Device Tap Detection**, **Hardware & On-Screen Volume Rockers**, **One-Handed Ergonomic Layout**, **Sunnah 33×3 Multi-Stage Misbaha**, and **100% Offline Privacy**.

**Zero Ads • Zero External Telemetry • 100% Offline • Installable Everywhere**

</div>

---

## 🌟 Why Tasbih Tap?

Traditional counter apps are cluttered with intrusive full-screen video ads, noisy banners, and battery-draining background trackers that disrupt Khushu' (spiritual focus). 

**Tasbih Tap** brings ultimate serenity to your remembrance:

```
+-------------------------------------------------------------------------------+
|                                 TASBIH TAP                                    |
|                                                                               |
|   📲 Back-Tap Counting     │  🔘 Hardware & Rocker Keys│  🕊️ 100% Ad-Free       |
|   Double tap device back   │  Volume buttons, on-screen│  Zero ads or popups  |
|   with sensitivity controls│  rocker, & keyboard keys  │  ever interrupt zikr |
|   ─────────────────────────┼─────────────────────────┼─────────────────────── |
|   📿 33×3 Sunnah Mode      │  🤲 30+ Authentic Azkar │  ⚡ Dual Architecture  |
|   Auto-advance through     │  Arabic, English & বাংলা│  PWA (Web Standards) & |
|   post-salah tasbihat      │  complete translations  │  Native Jetpack Compose|
+-------------------------------------------------------------------------------+
```

---

## 📱 Dual Platform Architecture

Tasbih Tap is engineered to deliver a first-class experience across both the open web and native mobile operating systems:

```
tasbih-tap/
 ├── index.html           # Semantically structured responsive PWA interface
 ├── app.js               # Web Core: Back-tap shockwave filter, Audio, State, & DB
 ├── style.css            # iOS 17/18 glassmorphic & Material 3 Islamic dark styling
 ├── manifest.json        # Standalone PWA manifest, display config & icons
 ├── sw.js                # Offline Service Worker with Cache-First asset strategy
 ├── icon-192.png         # High-resolution PWA launcher icon (192x192 PNG)
 ├── icon-512.png         # High-resolution splash icon (512x512 PNG)
 ├── gradlew / gradlew.bat# Official Gradle build wrapper scripts
 └── app/                 # Native Android Jetpack Compose Application
      ├── src/main/java/com/example/
      │    ├── MainActivity.kt               # Edge-to-edge entry point & key handlers
      │    ├── viewmodel/TasbihViewModel.kt  # StateFlow reactive state & Sunnah 33x3 logic
      │    ├── sensor/BackTapDetector.kt     # Android SensorEventListener back-tap engine
      │    ├── model/                        # Dhikr presets, targets, and theme definitions
      │    ├── data/local/                   # Room SQLite database (Sessions & Custom Dhikr)
      │    └── ui/screens/                   # Counter, Dhikr Sheet, History, and Settings
      └── src/main/res/                      # Vector drawables, mipmaps, strings & localized assets
```

---

## 🚀 Live Production URL

The progressive web app is deployed and globally accessible at:

🔗 **[https://tasbihtap.vercel.app](https://tasbihtap.vercel.app/)**

---

## 📲 How to Install as an App (PWA)

### On iPhone & iPad (iOS Safari)
1. Open **[https://tasbihtap.vercel.app](https://tasbihtap.vercel.app/)** in **Safari**.
2. Tap the **Share** button (square icon with an upward arrow).
3. Scroll down and tap **"Add to Home Screen"** (হোম স্ক্রিনে যোগ করুন).
4. Tap **Add**. The app opens in standalone full-screen mode like a native iOS application.

### On Android (Chrome / Edge / Samsung Internet)
1. Open **[https://tasbihtap.vercel.app](https://tasbihtap.vercel.app/)** in **Chrome**.
2. Tap the **"Add Tasbih Tap to Home screen"** / **"Install app"** button.
3. Tasbih Tap will be added to your app drawer and home screen.

### On PC / Mac / Chromebook (Chrome or Edge)
1. Open **[https://tasbihtap.vercel.app](https://tasbihtap.vercel.app/)** in **Google Chrome** or **Microsoft Edge**.
2. Click the **Install icon** in the address bar.
3. Launch directly from your desktop or dock without browser chrome.

---

## ✨ Standout Features

### 📲 1. Calibrated "Smart Back-Tap" Detection
*Count your Dhikr without looking at your screen.*
- **Customizable Sensitivity Levels**: Choose between **Low (কম)**, **Medium (মাঝারি)**, or **High (বেশি)** sensitivity to match device weight and protective cases.
- **Shockwave Filtering & Lateral Rejection**: Uses dynamic Z-axis delta impulse calculations with lateral shake suppression (`deltaZ >= deltaX * 0.7 && deltaZ >= deltaY * 0.7`) to eliminate false triggers from accidental pocket shifts or walking.
- **Eyes-Closed Ibadah**: Close your eyes during night prayer (Tahajjud), sit peacefully in the Masjid, or commute while your phone rests naturally in your palm.
- **Universal Support**: Full motion permission integration for iOS Safari (`DeviceMotionEvent.requestPermission`) and Android native sensor listeners.

### 🔄 2. Smart 33×3 Sunnah Misbaha Mode
- **Authentic Post-Salah Dhikr Flow**: Seamlessly follows the beloved Sunnah:
  $$\text{SubhanAllah (33)} \longrightarrow \text{Alhamdulillah (33)} \longrightarrow \text{Allahu Akbar (34)} \implies \text{100 Total}$$
- **Stage Completion Celebrations**: Provides gentle milestone sensory feedback at each 33/33 step and automatically moves to the next dhikr on subsequent tap.
- **Session History Logging**: Once all 100 counts are completed, the full round is logged into the offline database with timestamp and round count.

### 🔘 3. Multiple Flexible Counting Inputs
Designed for complete physical comfort in any posture:
- **Luminous Circular Counter**: Tap anywhere on the large serene center canvas.
- **One-Handed Thumb Button**: Ergonomic button placed at the base of the screen for single-hand use.
- **Hardware Volume Key Support**: Physical volume button counting with duplicate-suppression (`event.repeatCount == 0`).
- **Floating Screen-Edge Volume Rocker**: Virtual `VOL+` (count) and `VOL-` (undo) buttons fixed to the screen edge.
- **Physical Keyboard Counting**: Press `Space`, `Arrow Up`, or `Enter` on desktop/laptop to count; press `Arrow Down` to undo.
- **Bluetooth Headset & Media Buttons**: Integrated with the `MediaSession API`—count using your Bluetooth earphone play/pause/skip buttons!

### 🤲 4. Comprehensive Authentic Dhikr Library (3 Languages: বাংলা, English, আরবি)
Pre-loaded with authentic daily adhkar and duas with complete Arabic calligraphy, English transliteration & meaning, and Bengali pronunciation & translation:

| বিভাগ (Category) | যিকির / Transliteration | আরবি হরফে আরবী পাঠ | অর্থ (বাংলা ও English) | লক্ষ্য |
|:---|:---|:---:|:---|:---:|
| **প্রয়োজনীয় (Essential)** | **SubhanAllah** | سُبْحَانَ اللَّهِ | আল্লাহ অতি পবিত্র ও মহিমান্বিত / Glory be to Allah | ৩৩ |
| **প্রয়োজনীয় (Essential)** | **Alhamdulillah** | الْحَمْدُ لِلَّهِ | সকল প্রশংসা একমাত্র আল্লাহর / All praise is due to Allah | ৩৩ |
| **প্রয়োজনীয় (Essential)** | **Allahu Akbar** | اللَّهُ أَكْبَرُ | আল্লাহ সর্বশ্রেষ্ঠ / Allah is the Greatest | ৩৪ |
| **প্রয়োজনীয় (Essential)** | **La ilaha illallah** | لَا إِلٰهَ إِلَّا اللَّهُ | আল্লাহ ব্যতীত কোনো সত্য উপাস্য নেই / There is no deity but Allah | ১০০ |
| **প্রয়োজনীয় (Essential)** | **Kalima Tamjeed** | سُبْحَانَ اللَّهِ وَالْحَمْدُ لِلَّهِ وَلَا إِلٰهَ إِلَّا اللَّهُ وَاللَّهُ أَكْبَرُ | কালিমা তামজীদ / Glory & Praise be to Allah | ১০০ |
| **প্রশংসা (Praise)** | **SubhanAllahi wa bihamdihi** | سُبْحَانَ اللَّهِ وَبِحَمْدِهِ | আল্লাহর প্রশংসাসহ পবিত্রতা ঘোষণা / Glory and praise be to Allah | ১০০ |
| **প্রশংসা (Praise)** | **SubhanAllahil Azeem** | سُبْحَانَ اللَّهِ الْعَظِيمِ | মহিমান্বিত মহান আল্লাহ অতি পবিত্র / Glory be to Allah the Magnificent | ১০০ |
| **প্রশংসা (Praise)** | **SubhanAllahi wa bihamdihi ‘adada** | سُبْحَانَ اللَّهِ وَبِحَمْدِهِ عَدَدَ خَلْقِهِ... | সৃষ্টির সংখ্যা পরিমাণ প্রশংসা ও পবিত্রতা / According to creation & Throne | ৩ |
| **প্রশংসা (Praise)** | **La ilaha illallahu wahdahu** | لَا إِلٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ... | একক আল্লাহর গুণগান / None has right to be worshipped except Allah alone | ১০০ |
| **প্রশংসা (Praise)** | **Ya Hayyu Ya Qayyum** | يَا حَيُّ يَا قَيُّومُ | হে চিরঞ্জীব, হে সর্বসত্তার ধারক / O Ever-Living, O Self-Sustaining | ১০০ |
| **প্রশংসা (Praise)** | **Ya Dhal Jalali wal Ikram** | يَا ذَا الْجَلَالِ وَالإِكْرَامِ | হে মহিমা ও পরম অনুগ্রহের অধিকারী / O Owner of Majesty and Honor | ৩৩ |
| **প্রশংসা (Praise)** | **Radheetu Billahi Rabba** | رَضِيتُ بِاللَّهِ رَبًّا... | ঈমানের তৃপ্তির স্বীকৃতি / Pleased with Allah as Lord, Islam as Deen | ৩ |
| **ইস্তিগফার (Forgiveness)** | **Astaghfirullah** | أَسْتَغْفِرُ اللَّهَ | আমি আল্লাহর ক্ষমা প্রার্থনা করছি / I seek forgiveness from Allah | ১০০ |
| **ইস্তিগফার (Forgiveness)** | **Astaghfirullaha wa Atubu Ilayh** | أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ | আল্লাহর ক্ষমা চাই ও তওবা করছি / I seek forgiveness & turn in repentance | ১০০ |
| **ইস্তিগফার (Forgiveness)** | **Sayyidul Istighfar** | اللَّهُمَّ أَنْتَ رَبِّي لَا إِلٰهَ إِلَّا أَنْتَ... | ক্ষমা প্রার্থনার শ্রেষ্ঠতম দু'আ / Chief Supplication for Forgiveness | ৩ |
| **ইস্তিগফার (Forgiveness)** | **Rabbighfir li wa tub 'alayya** | رَبِّ اغْفِرْ لِي وَتُبْ عَلَيَّ... | হে রব! ক্ষমা করুন ও তওবা কবুল করুন / Forgive me & accept repentance | ১০০ |
| **সালাওয়াত (Salawat)** | **Allahumma Salli 'ala Muhammad** | اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ... | সংক্ষিপ্ত সালাওয়াত / Blessings upon the Prophet ﷺ | ১০০ |
| **সালাওয়াত (Salawat)** | **Durood-e-Ibrahim** | اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ... | পূর্ণাঙ্গ দরূদে ইবরাহীম / Complete Durood Ibrahim | ১০ |
| **সালাওয়াত (Salawat)** | **Sallallahu ‘Alayhi Wa Sallam** | صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ | তাঁর ওপর দরূদ ও সালাম বর্ষিত হোক / Peace and blessings upon him | ১০০ |
| **দু'আ (Supplication)** | **La Hawla wa la Quwwata** | لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ | জান্নাতের গুপ্তধন (হাওকালাহ) / No power nor strength except in Allah | ১০০ |
| **দু'আ (Supplication)** | **Hasbunallahu wa ni'mal wakeel** | حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ | আল্লাহই যথেষ্ট, উত্তম কর্মবিধায়ক / Allah is sufficient for us | ১০০ |
| **দু'আ (Supplication)** | **Ayat e Kareema (Dua Yunus)** | لَّا إِلٰهَ إِلَّا أَنتَ سُبْحَانَكَ إِنِّي كُنتُ مِنَ الظَّالِمِينَ | ইউনুস (আ.)-এর বিখ্যাত দু'আ / There is no deity except You; Exalted! | ১০০ |
| **কুরআনি দু'আ (Quranic)** | **Rabbana Atina fid-Dunya** | رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً... | ইহকাল ও পরকালের সর্বশ্রেষ্ঠ দু'আ / Good in this world & Hereafter | ৩৩ |
| **কুরআনি দু'আ (Quranic)** | **Rabbi Zidni 'Ilma** | رَّبِّ زِدْنِي عِلْمًا | জ্ঞান বৃদ্ধির দু'আ / My Lord, increase me in knowledge | ১০০ |

### 🎨 5. Apple iOS Glassmorphism & Six Sacred Islamic Color Palettes
- 🌿 **Emerald**: True dark canvas with Apple Mint & Al-Masjid an-Nabawi green accents.
- 🌌 **Midnight**: Deep glass canvas with Cupertino Blue (`#0A84FF`) highlights.
- 📜 **Sand**: Warm Medina parchment and desert gold accents.
- 🌑 **Night (OLED Black)**: Pure `#000000` AMOLED canvas with Platinum & Gold accents.
- 🌸 **Rose Gold**: Regal deep obsidian with Apple Pink (`#FF375F`) and purple luster.
- 👑 **Royal Amber**: Warm amber solar glow and deep slate glass.

---

## 🔧 Recent Improvements & Fixes

1. **PWA Runtime & UX Fixes**:
   - Resolved `showToast` definition, allowing milestone toasts and install prompts to execute without uncaught exceptions.
   - Cleared `isUnlimited` mode when selecting preset dhikr cards from the bottom sheet selector.
   - Wired `releaseWakeLock()` to the Settings toggle to immediately release screen lock when toggled off.
   - Enabled overlay background click-to-dismiss for all bottom sheet modals.
   - Added full bilingual DOM translation synchronization across all header pills, sheet titles, and settings items.
2. **Back-Tap Calibration & Lateral Shake Suppression**:
   - Implemented an initial acceleration baseline check (`isFirstAcc`) to prevent false triggers during startup.
   - Enforced lateral suppression (`deltaZ >= deltaX * 0.7 && deltaZ >= deltaY * 0.7`) to filter out horizontal movements and device shakes.
   - Added user-gesture permission request hooks for iOS Safari.
3. **PWA Asset Format & Launcher Icon Recovery**:
   - Repaired corrupted binary WebP and PNG files across `app/src/main/res/mipmap-*` and root icons.
   - Generated valid 192×192 and 512×512 PNG icons meeting PWA install criteria.
   - Updated Service Worker caching to `tasbih-tap-pwa-v2` and constrained fallback offline routing to navigation requests.
4. **Android Native Jetpack Compose Updates**:
   - Fixed Sunnah 33×3 progression in `TasbihViewModel.kt` to target 33, 33, and 34 counts across stages, preserving individual stage visual feedback without premature count jumps.
   - Added stage-aware undo (`decrement()`) that steps backward smoothly within and across stages.
   - Synchronized preset targets (`allahu_akbar` = 34, `yunus_dua` = 100).
   - Guarded volume key counting against continuous repeat events (`event.repeatCount == 0`).
   - Automatically pause the main `backTapDetector` when testing sensitivity in `BackTapTestDialog` and when navigating to History or Settings screens.
   - Added official `gradlew` and `gradlew.bat` wrapper scripts for local and CI builds.

---

## 🛠️ Development & Build Setup

### PWA (Web)
To test the web app locally, serve the directory using any HTTP server:
```bash
# Using Python
python3 -m http.server 8000

# Using Node.js
npx serve .
```
Visit `http://localhost:8000` in your browser.

### Native Android
To compile and assemble the Android APK using the included Gradle wrapper:
```bash
# Build debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew test
```

---

## 🛡️ Privacy & Philosophy

<div align="center">

| Feature | Tasbih Tap | Typical Counter Apps |
|:---|:---:|:---:|
| **Advertisements** | ❌ **ZERO (Never)** | ⚠️ Full-screen video ads |
| **Internet Access** | ❌ **Completely Offline** | ⚠️ Required for ad delivery |
| **Data Collection** | ❌ **NONE (Private)** | ⚠️ Trackers & analytics |
| **Installation** | ✅ **1-Tap PWA / Native APK** | ⚠️ App Store download only |
| **Cross-Platform** | ✅ **iOS, Android, PC, Mac** | ⚠️ OS dependent |
| **Resource Usage** | 🟢 **Ultra Lightweight** | 🔴 50MB+ bundle size |

</div>

> **Our Commitment**: Your worship is sacred. An app designed for remembering Allah ﷻ should never profit from tracking your habits or flashing banners across your screen. Every byte of your Dhikr count remains solely on your physical device.

---

## 📜 License & Credits

Distributed under the **MIT License**.

<div align="center">

```
   ♥ Made with devotion by ©munabbiRMushran
   Dedicated as a humble Sadaqah Jariyah for the Ummah.
```

**[اللَّهُمَّ أَعِنِّي عَلَى ذِكْرِكَ وَشُكْرِكَ وَحُسْنِ عِبَادَتِكَ]**  
*(O Allah, help me to remember You, give thanks to You, and worship You in the best manner)*

</div>
