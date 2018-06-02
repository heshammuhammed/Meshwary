package com.example.heshammuhammed.reminder.AlarmManager;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.WindowManager;

import com.example.heshammuhammed.reminder.DTO.Trip;
import com.example.heshammuhammed.reminder.Database.Tripbase;
import com.example.heshammuhammed.reminder.HomePage;
import com.example.heshammuhammed.reminder.MainHome.MainActivity;
import com.example.heshammuhammed.reminder.R;

public class MyReceiver extends BroadcastReceiver {
    AlertDialog mAlertDialog;
    AlertDialog.Builder builder;
    Trip myNewTrip;
    @Override
    public void onReceive(final Context context, Intent intent) {
        myNewTrip = (Trip) intent.getSerializableExtra("myNewTrip");

        final MediaPlayer player = MediaPlayer.create(context, R.raw.sound);
        player.start();
        builder = new AlertDialog.Builder(context);
        builder.setMessage(myNewTrip.getTripName()+" Trip Will Start After Minutes").setTitle("Trip Alarm");
        builder.setPositiveButton("Start", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                myNewTrip.setStatus("PROGRESS");
                Tripbase tripbase=new Tripbase(context);
                tripbase.ChangeState(myNewTrip);
                dialogInterface.dismiss();
                player.stop();
                //3_18_5_30_pm
                Intent intent2 = new Intent(context, MainActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent2);

                String myLink = "http://maps.google.com/maps?saddr=" + myNewTrip.getStartPointLatitude() + "," + myNewTrip.getStartPointLongitude() + "&daddr=" + myNewTrip.getEndPointLatitude() + "," + myNewTrip.getEndPointLongitude();
                Log.e("abdallatest1", myLink);
                final Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(myLink));
                context.startActivity(intent);





            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myNewTrip.setStatus("CANCELLED");
                Tripbase tripbase=new Tripbase(context);
                tripbase.ChangeState(myNewTrip);
                dialogInterface.cancel();
                player.stop();
                //3_18_5_30_pm
                Intent intent2 = new Intent(context, MainActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent2);
            }
        }).setNeutralButton("Remind me later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                player.stop();
                String myLink = "http://maps.google.com/maps?saddr=" + myNewTrip.getStartPointLatitude() +
                        "," + myNewTrip.getStartPointLongitude() + "&daddr=" + myNewTrip.getEndPointLatitude() + "," + myNewTrip.getEndPointLongitude();

                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(myLink));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.messenger_button_blue_bg_selector)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.meshwary1))
                        .setContentTitle("Remember"+" "+myNewTrip.getTripName()+" "+"Trip")
                        .setContentText("Start Now")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setOngoing(true);



                notificationManager.notify(1, mBuilder.build());
            }
        });
        mAlertDialog= builder.create();
        mAlertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        WindowManager.LayoutParams lp = mAlertDialog.getWindow().getAttributes();
        lp.dimAmount=0.0f;
        mAlertDialog.getWindow().setAttributes(lp);
        mAlertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        mAlertDialog.setCanceledOnTouchOutside(false);
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
    }

}
