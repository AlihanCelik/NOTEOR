package com.example.notesapp

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        val noteTitle = intent?.getStringExtra("NOTE_TITLE")
        val noteId=intent?.getIntExtra("NOTE_ID",-1)
        val i=Intent(context,CreateNoteActivity::class.java)
        i.putExtra("itemid",noteId)
        intent!!.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingintent=PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val builder =NotificationCompat.Builder(context!!,"foxandroid")
            .setSmallIcon(R.mipmap.logo).setContentTitle("NOTEOR")
            .setContentText(noteTitle)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingintent)
        val notificationManager= NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(123,builder.build())
    }

}