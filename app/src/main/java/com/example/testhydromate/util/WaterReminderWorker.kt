package com.example.testhydromate.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.example.testhydromate.MainActivity
import com.example.testhydromate.R
import com.example.testhydromate.di.dataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class WaterReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    // Helper untuk membaca DataStore
    private fun getSettings() = runBlocking {
        applicationContext.dataStore.data.map { prefs ->
            mapOf(
                "enabled" to (prefs[booleanPreferencesKey("is_enabled")] ?: true),
                "startTime" to (prefs[stringPreferencesKey("start_time")] ?: "08:00"),
                "endTime" to (prefs[stringPreferencesKey("end_time")] ?: "22:00"),
                "interval" to (prefs[intPreferencesKey("interval")] ?: 60), // Ambil Interval
                "days" to (prefs[stringSetPreferencesKey("repeat_days")] ?: setOf("1","2","3","4","5","6","7")),
                "sound" to (prefs[booleanPreferencesKey("is_sound")] ?: true),
                "vibration" to (prefs[booleanPreferencesKey("is_vibration")] ?: true)
            )
        }.first()
    }

    override fun doWork(): Result {
        val settings = getSettings()

        // 1. Cek Master Switch
        if (settings["enabled"] as Boolean == false) return Result.success()

        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_WEEK).toString()

        // 2. Cek Hari
        val selectedDays = settings["days"] as Set<String>
        if (!selectedDays.contains(currentDay)) return Result.success()

        // 3. Cek Jam (Start - End)
        val startTimeStr = settings["startTime"] as String
        val endTimeStr = settings["endTime"] as String

        val startTime = startTimeStr.split(":")
        val endTime = endTimeStr.split(":")

        val nowMinutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
        val startMinutes = startTime[0].toInt() * 60 + startTime[1].toInt()
        val endMinutes = endTime[0].toInt() * 60 + endTime[1].toInt()

        if (nowMinutes in startMinutes..endMinutes) {
            val withSound = settings["sound"] as Boolean
            val withVibration = settings["vibration"] as Boolean
            val interval = settings["interval"] as Int

            // --- LOGIKA PESAN BARU ---
            val message = generateNotificationMessage(calendar, interval, endMinutes)

            showNotification(withSound, withVibration, message)
        }

        return Result.success()
    }

    private fun generateNotificationMessage(calendar: Calendar, intervalMinutes: Int, endLimitMinutes: Int): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())

        // 1. Ambil Jam Sekarang
        val currentTimeStr = sdf.format(calendar.time)

        // 2. Hitung Jam Berikutnya
        // Clone kalender agar tidak merubah waktu asli saat ini
        val nextCalendar = calendar.clone() as Calendar
        nextCalendar.add(Calendar.MINUTE, intervalMinutes)

        val nextMinutesTotal = nextCalendar.get(Calendar.HOUR_OF_DAY) * 60 + nextCalendar.get(Calendar.MINUTE)
        val nextTimeStr = sdf.format(nextCalendar.time)

        // 3. Tentukan Pesan
        return if (nextMinutesTotal <= endLimitMinutes) {
            // Jika jadwal berikutnya masih dalam jam operasional hari ini
            "Keep up your streak and stay hydrated • Next reminder: $nextTimeStr"
        } else {
            // Jika jadwal berikutnya sudah lewat jam tidur
            "Keep up your streak and stay hydrated • Last reminder for today. See you tomorrow!"
        }
    }

    private fun showNotification(withSound: Boolean, withVibration: Boolean, messageBody: String) {
        val channelId = "water_channel_${withSound}_${withVibration}"
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(channelId) == null) {
                val channel = NotificationChannel(
                    channelId,
                    "Hydration Reminder",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Custom reminders"
                    enableVibration(withVibration)
                    if (!withSound) setSound(null, null)
                    else setSound(Settings.System.DEFAULT_NOTIFICATION_URI, AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build())
                }
                notificationManager.createNotificationChannel(channel)
            }
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("It's time to drink water!")
            .setContentText(messageBody) // Menggunakan pesan dinamis
            .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody)) // Agar teks panjang terlihat semua
            .setSmallIcon(R.drawable.hydromate_logo)
            .setColor(android.graphics.Color.parseColor("#0061FF"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1001, notification)
    }

    companion object {
        fun schedule(context: Context, isEnabled: Boolean, intervalMinutes: Long = 60) {
            val workManager = WorkManager.getInstance(context)
            val workName = "WaterReminderWork"

            if (isEnabled) {
                val safeInterval = if (intervalMinutes < 15) 15 else intervalMinutes

                val constraints = Constraints.Builder()
                    .setRequiresBatteryNotLow(false)
                    .build()

                val request = PeriodicWorkRequestBuilder<WaterReminderWorker>(
                    safeInterval, TimeUnit.MINUTES
                )
                    .setConstraints(constraints)
                    .build()

                workManager.enqueueUniquePeriodicWork(
                    workName,
                    ExistingPeriodicWorkPolicy.UPDATE,
                    request
                )
            } else {
                workManager.cancelUniqueWork(workName)
            }
        }
    }
}