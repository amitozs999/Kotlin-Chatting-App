package com.amitozsingh.chatapp.Notification



import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.content.Context.NOTIFICATION_SERVICE
import android.app.NotificationManager
import android.R
import android.app.Notification
import android.media.RingtoneManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.amitozsingh.chatapp.Activities.MessagesActivity
import com.amitozsingh.chatapp.Fragments.MessagesFragment
import androidx.core.content.ContextCompat.getSystemService
import android.app.NotificationChannel
import android.os.Build
import android.graphics.BitmapFactory








class FriendRequestMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        sendNotification(remoteMessage.data["title"]!!, remoteMessage.data["body"]!!)
    }

    private fun sendNotification(title: String, body: String) {
        val intent = Intent(this, MessagesActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

//
//        val pendingIntent =
//            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val NOTIFICATION_CHANNEL_ID = "my_channel_id_01"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "My Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            // Configure the notification channel.
            notificationChannel.description = "Channel description"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val mNotification = builder
            .setContentTitle(title)
            .setContentText(body)
            //     .setPriority(Notification.PRIORITY_MAX)

            .setContentIntent(pendingIntent)

            .setAutoCancel(true)
            //                .setDefaults(Notification.DEFAULT_ALL)
            //                .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.sym_def_app_icon)
            .build()

        notificationManager.notify(/*notification id*/0, mNotification)

//        val patteren = longArrayOf(500, 500, 500, 500, 500)
//
//        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//
//        val notifcationBuilder = NotificationCompat.Builder(this)
//            .setSmallIcon(R.mipmap.sym_def_app_icon)
//            .setContentTitle(title)
//            .setContentText(body)
//            .setVibrate(patteren)
//            .setLights(Color.BLUE, 1, 1)
//            .setSound(defaultSoundUri)
//            .setContentIntent(pendingIntent)
//            .setPriority(Notification.PRIORITY_HIGH) as NotificationCompat.Builder
//
//        val notificationManagerCompat =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManagerCompat.notify(0, notifcationBuilder.build())



    }

}