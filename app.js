/**
 * Tasbih Tap - Progressive Web App (PWA) Engine
 * Full feature parity: Back-tap shockwave filter, On-Screen Rocker, Keyboard/Media keys,
 * 33x3 Sunnah loop, Web Audio click, Vibration API, Wake Lock, & IndexedDB/LocalStorage.
 */

// --- DHIKR PRESETS WITH BANGLA & ENGLISH LOCALIZATION ---
const DHIKR_PRESETS = [
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
    meaning: "There is no deity except Allah",
    meaning_bn: "আল্লাহ ব্যতীত কোনো সত্য উপাস্য নেই",
    arabic: "لَا إِلٰهَ إِلَّا اللَّهُ",
    defaultTarget: 100,
    category: "praise"
  },
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
    id: "salawat",
    name: "Salawat",
    name_bn: "সালাওয়াত (দরূদ)",
    meaning: "Peace and blessings upon Muhammad",
    meaning_bn: "মুহাম্মদ ﷺ এর উপর শান্তি ও রহমত বর্ষিত হোক",
    arabic: "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ",
    defaultTarget: 100,
    category: "salawat"
  },
  {
    id: "hawqala",
    name: "Hawqala",
    name_bn: "হাওকালাহ",
    meaning: "No power nor strength except in Allah",
    meaning_bn: "আল্লাহর সাহায্য ব্যতীত কোনো শক্তি নেই",
    arabic: "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ",
    defaultTarget: 100,
    category: "supplication"
  },
  {
    id: "hasbunallah",
    name: "Hasbunallahu wa ni'mal wakeel",
    name_bn: "হাসবুনাল্লাহু ওয়া নিমাল ওয়াকিল",
    meaning: "Allah is sufficient for us, the best protector",
    meaning_bn: "আমাদের জন্য আল্লাহই যথেষ্ট, উত্তম কর্মবিধায়ক",
    arabic: "حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ",
    defaultTarget: 100,
    category: "supplication"
  },
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
    id: "sayyidul_istighfar",
    name: "Sayyidul Istighfar",
    name_bn: "সাইয়্যিদুল ইস্তিগফার",
    meaning: "Chief supplication for forgiveness",
    meaning_bn: "ক্ষমা প্রার্থনার শ্রেষ্ঠ দু'আ",
    arabic: "سَيِّدُ الاسْتِغْفَارِ",
    defaultTarget: 33,
    category: "forgiveness"
  },
  {
    id: "yunus_dua",
    name: "Ayat e Kareema (Dua of Yunus)",
    name_bn: "আয়াতে কারীমা (ইউনুস আ. এর দু'আ)",
    meaning: "Exalted are You, indeed I was of the wrongdoers",
    meaning_bn: "তুমি ব্যতীত সত্য উপাস্য নেই, আমি অপরাধী ছিলাম",
    arabic: "لَّا إِلَٰهَ إِلَّا أَنتَ سُبْحَانَكَ",
    defaultTarget: 100,
    category: "supplication"
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
  document.getElementById('arabic-main').textContent = active.arabic || '';
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
