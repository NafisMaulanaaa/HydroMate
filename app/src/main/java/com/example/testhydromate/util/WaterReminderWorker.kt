package com.example.testhydromate.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.example.testhydromate.MainActivity // Pastikan import MainActivity-mu benar
import java.util.*
import java.util.concurrent.TimeUnit

class WaterReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)

        // Hanya munculkan notifikasi antara jam 05:00 sampai 22:00
        if (currentHour in 5..21) {
            showNotification()
        }

        return Result.success()
    }

    private fun showNotification() {
        val channelId = "water_reminder_channel"
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 1. Buat Intent untuk membuka MainActivity
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        // 2. Buat PendingIntent (Flag IMMUTABLE wajib untuk Android 12+)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                PendingIntent.FLAG_IMMUTABLE
            else
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Water Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminders to stay hydrated"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Lets Hydrate!")
            .setContentText("Keep your body hydrated everytime.")
            .setSmallIcon(com.example.testhydromate.R.drawable.hydromate_blue_logo)
            .setColor(android.graphics.Color.parseColor("#0061FF"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(pendingIntent) // 3. Set PendingIntent ke notifikasi
            .setAutoCancel(true) // Notifikasi hilang setelah diklik
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    companion object {
        fun schedule(context: Context, isEnabled: Boolean) {
            val workManager = WorkManager.getInstance(context)
            if (isEnabled) {
                val request = PeriodicWorkRequestBuilder<WaterReminderWorker>(
                    2, TimeUnit.HOURS
                )
                    .addTag("water_reminder")
                    .build()

                workManager.enqueueUniquePeriodicWork(
                    "WaterReminderWork",
                    ExistingPeriodicWorkPolicy.UPDATE,
                    request
                )
            } else {
                workManager.cancelUniqueWork("WaterReminderWork")
            }
        }
    }
}
