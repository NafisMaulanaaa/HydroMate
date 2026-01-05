# 💧 HydroMate - Pelacak Hidrasi Cerdas

**HydroMate** adalah aplikasi Android pelacak hidrasi modern yang dibangun menggunakan **Kotlin** dan **Jetpack Compose**. Aplikasi ini dirancang untuk membantu pengguna menjaga hidrasi tubuh dengan menghitung target minum harian secara personal—berdasarkan data biometrik, tingkat aktivitas fisik, dan kondisi cuaca lingkungan.

## ✨ Fitur Utama

* **🧪 Target Hidrasi Personal:** Menggunakan algoritma berbasis sains (*Water Turnover*) untuk menghitung kebutuhan air yang akurat berdasarkan:
    * Gender, Usia, Berat, dan Tinggi Badan.
    * Tingkat Aktivitas (*Light, Moderate, Very Active*).
    * Kondisi Cuaca (*Hot, Temperate, Cold*).
* **📊 Laporan & Statistik (Charts):**
    * Visualisasi data minum secara **Mingguan**, **Bulanan**, dan **Tahunan**.
    * Tampilan grafik interaktif dengan opsi **Bar Chart** dan **Line Chart**.
    * Navigasi periode waktu yang dinamis.
* **🏆 Gamifikasi (Streak):** Membangun kebiasaan baik dengan fitur *Daily Streak* yang memotivasi pengguna untuk mencapai target setiap hari.
* **📝 Log Minum Real-time:** Pencatatan konsumsi air yang cepat dengan fitur edit dan hapus riwayat.
* **🔔 Pengingat Cerdas:** Notifikasi otomatis yang berjalan di latar belakang (menggunakan WorkManager) pada jam aktif (05:00 - 22:00) agar tidak mengganggu waktu tidur.
* **☁️ Cloud Sync:** Data tersimpan aman dan tersinkronisasi menggunakan **Firebase Firestore** dan **Authentication**.

## 🛠️ Teknologi & Library

Aplikasi ini dibangun menerapkan prinsip *Modern Android Development*:

* **Bahasa:** Kotlin
* **UI Framework:** [Jetpack Compose](https://developer.android.com/jetbrains/compose) (Material3)
* **Arsitektur:** MVVM (Model-View-ViewModel)
* **Dependency Injection:** [Dagger Hilt](https://dagger.dev/hilt/)
* **Navigasi:** Jetpack Navigation Compose
* **Backend & Auth:** Firebase Firestore & Firebase Auth
* **Asynchronous:** Kotlin Coroutines & Flow
* **Background Tasks:** [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)

## 📂 Struktur Proyek

Kode sumber diorganisir berdasarkan fitur dan layer untuk memudahkan pemeliharaan:

```text
com.example.testhydromate
├── data
│   ├── model          # Data class (User, WaterLog, DailyChartData, ReminderSettings)
│   └── repository     # Manajemen sumber data (Firebase/DataStore)
├── di                 # Modul Dependency Injection
├── ui
│   ├── components     # Komponen UI yang reusable (Tombol, Chart, dll)
│   ├── navigation     # Konfigurasi NavHost dan Rute Layar
│   ├── screens        # Layar aplikasi berdasarkan fitur (Home, Report, Profile, Onboarding, dll)
│   └── theme          # Tema visual global (Warna, Font).
├── util               # Fungsi bantuan
├── HydroMateApp.kt
└── MainActivity.kt
```

## 🚀 Instalasi & Konfigurasi

### Prasyarat

* **Android Studio:** Versi Ladybug atau yang lebih baru.
* **JDK:** Versi 17 atau yang lebih baru.

1.  **Clone Repository**
    Buka terminal dan jalankan perintah berikut untuk mengunduh kode sumber:
    ```bash
    git clone [https://github.com/username-anda/HydroMate.git](https://github.com/username-anda/HydroMate.git)
    ```

2.  **Konfigurasi Firebase**
    * Buat proyek baru di Firebase Console.
    * Aktifkan Authentication (Metode Email/Password).
    * Aktifkan Cloud Firestore (Buat database dalam mode test atau production).
    * Unduh file `google-services.json` dari menu *Project Settings* di Firebase Console.
    * Salin dan tempel file tersebut ke dalam folder `app/` di proyek Android Studio Anda.

3.  **Sinkronisasi Gradle**
    * Buka proyek di Android Studio.
    * Tunggu proses *indexing* selesai.
    * Jika muncul notifikasi *sync*, klik **"Sync Now"** atau buka menu **File > Sync Project with Gradle Files**.

4.  **Jalankan Aplikasi**
    * Sambungkan HP Android (aktifkan USB Debugging) atau gunakan *Android Emulator*.
    * Klik tombol **Run (▶)** di toolbar atas.

## 📸 Tangkapan Layar (Screenshots)

|                     Home                      |                     History                     |                  Report Charts                  |                     Profile                      |
|:---------------------------------------------:|:-----------------------------------------------:|:-----------------------------------------------:|:------------------------------------------------:|
| <img src="screenshots/Home.png" width="180"/> | <img src="screenshots/History.png" width="180"/> | <img src="screenshots/Report.png" width="180"/> | <img src="screenshots/Profile.png" width="180"/> |

## 🔬 Dasar Ilmiah

Logika perhitungan target air dalam aplikasi ini mengadaptasi rumus estimasi *Water Turnover* yang lebih akurat dibandingkan rumus berat badan standar. Rumus ini mempertimbangkan variabel:
* **FFM (Fat Free Mass):** Estimasi massa tubuh tanpa lemak.
* **PAL (Physical Activity Level):** Tingkat aktivitas fisik harian.
* **Faktor Lingkungan:** Suhu dan kelembapan (berdasarkan data cuaca).

*Referensi: Penelitian kebutuhan hidrasi manusia (Yosuke Yamada et al.).*

## 👥 Tim SNAP
* [23523014] Nafis Ilyas Maulana 
* [23523038] Karina
* [23523136] Anindya Putri Aisyah