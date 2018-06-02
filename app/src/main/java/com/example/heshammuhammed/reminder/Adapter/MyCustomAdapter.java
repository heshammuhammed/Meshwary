package com.example.heshammuhammed.reminder.Adapter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.heshammuhammed.reminder.DTO.Note;
import com.example.heshammuhammed.reminder.DTO.Trip;
import com.example.heshammuhammed.reminder.Database.Tripbase;
import com.example.heshammuhammed.reminder.EditingTrip.EditTrip;
import com.example.heshammuhammed.reminder.MainHome.MainActivity;
import com.example.heshammuhammed.reminder.AlarmManager.MyReceiver;
import com.example.heshammuhammed.reminder.AddingTrip.MyService;
import com.example.heshammuhammed.reminder.R;
import com.example.heshammuhammed.reminder.MainHome.Trip_Details;
import com.nex3z.notificationbadge.NotificationBadge;
import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;
import com.txusballesteros.bubbles.OnInitializedCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by HeshamMuhammed on 3/6/2018.1
 */

public class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.MyViewHolder> {

    //abdalla 18_3
   static Boolean btnStartTripFlag = true;


    Activity context;
    LayoutInflater layoutInflater;
    ArrayList<Trip> arrayList;

    private BubblesManager bubblesManager;
    private NotificationBadge notificationBadge;
    public static boolean state = true;

    //abdalla delete trip 3_16
    HashMap<Integer, Calendar> hm = new HashMap<Integer, java.util.Calendar>();
    PendingIntent pi;
    AlarmManager alarmManager;
    Intent intent;

    public MyCustomAdapter(Activity context, ArrayList<Trip> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        layoutInflater = LayoutInflater.from(context);
        bubblesManager = new BubblesManager.Builder(context)
                .setTrashLayout(R.layout.bubble_remove)
                .setInitializationCallback(new OnInitializedCallback() {
                    @Override
                    public void onInitialized() {

                    }
                }).build();
        bubblesManager.initialize();
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(context)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + context.getPackageName()));
                context.startActivityForResult(intent, 1000);
            }
        } else {
            Intent intent = new Intent(context, Service.class);
            context.startService(intent);
        }
    }

    private void addNewBubble() {
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = layoutInflater.inflate(R.layout.row_cardview, parent, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, myViewHolder.textView.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyCustomAdapter.MyViewHolder holder, final int position) {
        final Trip trip = arrayList.get(position);
        String fullLink = "https://maps.googleapis.com/maps/api/staticmap?size=350x200&path=color%3A0xff0000ff%7Cweight%3A5%7Cenc%3A" + trip.getImageLink();
//         Picasso.with(context).load(fullLink).fit()
//                 .into(holder.imageView);
      if(trip.getStatus().equals("DONE")|| trip.getStatus().equals("CANCELLED"))  {
          holder.startTrip.setVisibility(View.GONE);
          holder.editTrip.setVisibility(View.GONE);
      }
        if(trip.getStatus().equals("DONE"))  {
            holder.txtTripState.setTextColor(Color.rgb(124,252,0));
        }
        if(trip.getStatus().equals("CANCELLED"))  {
            holder.txtTripState.setTextColor(Color.YELLOW);
        }
        if(trip.getStatus().equals("PROGRESS"))  {
            holder.txtTripState.setTextColor(Color.RED);
        }
        if(trip.getStatus().equals("UPCOMING"))  {
            holder.txtTripState.setTextColor(Color.BLACK);
        }

        Glide.with(context)
                .load(fullLink)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(holder.imageView);
        //holder.imageView.setImageResource(R.drawable.ic_launcher_background);

        holder.textView.setText(trip.getTripName());
        holder.txtTripState.setText(trip.getStatus());
        holder.startTrip.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                if (btnStartTripFlag) {
                    btnStartTripFlag=false;
                    if (trip.getStatus().equals("UPCOMING")) {
                        //abdalla 3_17
                        hm.remove(trip.getTripId());
                        Intent intentAlarm = new Intent(context, MyReceiver.class);
                        pi = PendingIntent.getBroadcast(context, trip.getTripId(), intentAlarm, 0);
                        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        alarmManager.cancel(pi);


                        //    if(trip.getStatus().equals("UPCOMING")) {
                        Tripbase tripbase = new Tripbase(context);
                        trip.setStatus("PROGRESS");
                        tripbase.updateTrip(trip);
                        // a1
                        Toast.makeText(context, "Start Was Clicked " + trip.getTripName(), Toast.LENGTH_LONG).show();
                        String myLink = "http://maps.google.com/maps?saddr=" + trip.getStartPointLatitude() + "," + trip.getStartPointLongitude() + "&daddr=" + trip.getEndPointLatitude() + "," + trip.getEndPointLongitude();
                        Log.e("abdallatest1", myLink);

                        //3_18_5_30_pm
                        Intent intent2 = new Intent(context, MainActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent2);


                        final Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(myLink));
                        context.startActivity(intent);
                        // a2
                        ///////////////////////////////////////////////////
                        BubbleLayout bubbleView = (BubbleLayout) LayoutInflater.from(context)
                                .inflate(R.layout.bubble_layout, null);

                        bubbleView.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
                            @Override
                            public void onBubbleRemoved(BubbleLayout bubble) {

                                //3_18_5_30_pm
                                Intent intent2 = new Intent(context, MainActivity.class);
                                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent2);

                                btnStartTripFlag=true;
                                Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();
                                //abdalla 3_17
                                Tripbase tripbase2 = new Tripbase(context);
                                trip.setStatus("DONE");
                                trip.setEndDate(new Date());
                                tripbase2.updateTrip(trip);
                                if (trip.getRoundTrip().equals("ROUND")) {
                                    trip.setRoundTrip("NOTROUND");
                                    Trip nextTrip = new Trip(trip.getTripName(), trip.getEndPointLatitude(), trip.getEndPointLongitude(), trip.getStartPointLatitude(), trip.getStartPointLongitude(), trip.getEndPointName(), trip.getEndPointName(), new Date(), new Date(), "UPCOMING", trip.getDistance(), trip.getImageLink(), "NOTROUND");
                                    int tripId = (int) tripbase2.CreateTrip(nextTrip);
                                    nextTrip.setTripId(tripId);
                                    Log.e("3_17", "trip update");
                                    ArrayList<Note> oldTripNotes = tripbase2.getSpecificNotes(trip);
                                    if (oldTripNotes != null) {
                                        for (int ii = 0; ii < oldTripNotes.size(); ii++) {
                                            Note oldNote = oldTripNotes.get(ii);
//                            note.setTripId(trip.getTripId());
                                            Log.e("3_17", oldNote.getTextNote());
                                            Log.e("3_17", oldNote.getStatus());
                                            Log.e("3_17", "" + oldNote.getChecked());
                                            Note newNote = new Note();
                                            newNote.setTripId(nextTrip.getTripId());
                                            newNote.setChecked(oldNote.getChecked());
                                            newNote.setTextNote(oldNote.getTextNote());
                                            newNote.setStatus(oldNote.getStatus());
                                            int v = (int) tripbase2.CreateNote(newNote);
                                            Log.e("3_17", "" + v);
                                            Toast.makeText(context, "Round Trip Start successful", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }


                            }
                        });

                        bubbleView.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {
                            @Override
                            public void onBubbleClick(BubbleLayout bubble) {
                             //   Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                                Tripbase tripbase = new Tripbase(context);
                                Note note = new Note();
                                note.setTripId(trip.getTripId());
                            //    Toast.makeText(context, "Trip Id is " + trip.getTripId(), Toast.LENGTH_LONG).show();
                                ArrayList<Note> arrayList = tripbase.getSpecificNotes(note);
                             //   Toast.makeText(context, arrayList.size() + "", Toast.LENGTH_LONG).show();
                                String Message = "";
                                for (int i = 0; i < arrayList.size(); i++) {
                                    Note note0 = arrayList.get(i);
                             //       Toast.makeText(context, note0.getTextNote(), Toast.LENGTH_LONG).show();
                             //       Toast.makeText(context, note0.getStatus(), Toast.LENGTH_LONG).show();
                                    Message = Message + " \n" + note0.getTextNote();
                                    Log.e("Hamada", "Iteration " + i + note0.getTextNote());
                                }
                                Log.e("Hamada9", Message);


                                if (state) {
                                    Intent intentView = new Intent(context, MyService.class);
                                    intentView.putExtra("TripID", trip.getTripId());
                                    context.startService(intentView);
                                    state = false;
                                    Log.e("HAHA", "Start Service");
                                } else {
                                    state = true;
                                    Intent intentView = new Intent(context, MyService.class);
                                    context.stopService(intentView);
                                }
                                // Hesham End 1
                            }
                        });
                        bubbleView.setShouldStickToWall(true);
                        bubblesManager.addBubble(bubbleView, 60, 20);
                        //    }
                    } else {
                        //abdalla 3_17


                        // abdalla 3_17

                        //3_18_5_30_pm
                        Intent intent2 = new Intent(context, MainActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent2);



                        String myLink = "http://maps.google.com/maps?saddr=" + trip.getStartPointLatitude() + "," + trip.getStartPointLongitude() + "&daddr=" + trip.getEndPointLatitude() + "," + trip.getEndPointLongitude();
                        Log.e("abdallatest1", myLink);
                        final Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(myLink));
                        context.startActivity(intent);
                        // abdalla 3_17
                        ///////////////////////////////////////////////////
                        BubbleLayout bubbleView = (BubbleLayout) LayoutInflater.from(context)
                                .inflate(R.layout.bubble_layout, null);

                        bubbleView.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
                            @Override
                            public void onBubbleRemoved(BubbleLayout bubble) {



                                btnStartTripFlag=true;
                          //      Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();
                                //abdalla 3_17
                                Tripbase tripbase2 = new Tripbase(context);
                                trip.setStatus("DONE");
                                trip.setEndDate(new Date());
                                tripbase2.updateTrip(trip);
                                if (trip.getRoundTrip().equals("ROUND")) {
                                    trip.setRoundTrip("NOTROUND");
                                    Trip nextTrip = new Trip(trip.getTripName(), trip.getEndPointLatitude(), trip.getEndPointLongitude(), trip.getStartPointLatitude(), trip.getStartPointLongitude(), trip.getEndPointName(), trip.getEndPointName(), new Date(), new Date(), "UPCOMING", trip.getDistance(), trip.getImageLink(), "NOTROUND");
                                    int tripId = (int) tripbase2.CreateTrip(nextTrip);
                                    nextTrip.setTripId(tripId);
                                    Log.e("3_17", "trip update");
                                    ArrayList<Note> oldTripNotes = tripbase2.getSpecificNotes(trip);
                                    if (oldTripNotes != null) {
                                        for (int ii = 0; ii < oldTripNotes.size(); ii++) {
                                            Note oldNote = oldTripNotes.get(ii);
//                            note.setTripId(trip.getTripId());
                                            Log.e("3_17", oldNote.getTextNote());
                                            Log.e("3_17", oldNote.getStatus());
                                            Log.e("3_17", "" + oldNote.getChecked());
                                            Note newNote = new Note();
                                            newNote.setTripId(nextTrip.getTripId());
                                            newNote.setChecked(oldNote.getChecked());
                                            newNote.setTextNote(oldNote.getTextNote());
                                            newNote.setStatus(oldNote.getStatus());
                                            int v = (int) tripbase2.CreateNote(newNote);
                                            Log.e("3_17", "" + v);
                                        //    Toast.makeText(context, "Round Trip Start successful", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                                Intent intentView = new Intent(context, MyService.class);
                                context.stopService(intentView);

                            }
                        });

                        bubbleView.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {
                            @Override
                            public void onBubbleClick(BubbleLayout bubble) {
                            //    Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                                Tripbase tripbase = new Tripbase(context);
                                Note note = new Note();
                                note.setTripId(trip.getTripId());
                           //     Toast.makeText(context, "Trip Id is " + trip.getTripId(), Toast.LENGTH_LONG).show();
                                ArrayList<Note> arrayList = tripbase.getSpecificNotes(note);
                          //      Toast.makeText(context, arrayList.size() + "", Toast.LENGTH_LONG).show();
                                String Message = "";
                                for (int i = 0; i < arrayList.size(); i++) {
                                    Note note0 = arrayList.get(i);
                                 //   Toast.makeText(context, note0.getTextNote(), Toast.LENGTH_LONG).show();
                              //      Toast.makeText(context, note0.getStatus(), Toast.LENGTH_LONG).show();
                                    Message = Message + " \n" + note0.getTextNote();

                                }
                                Log.e("Hamada9", Message);


                                if (state) {
                                    Intent intentView = new Intent(context, MyService.class);
                                    intentView.putExtra("TripID", trip.getTripId());
                                    context.startService(intentView);
                                    state = false;
                                    Log.e("HAHA", "Start Service");
                                } else {
                                    state = true;
                                    Intent intentView = new Intent(context, MyService.class);
                                    context.stopService(intentView);
                                }
                                // Hesham End 1
                            }
                        });
                        bubbleView.setShouldStickToWall(true);
                        bubblesManager.addBubble(bubbleView, 60, 20);
                    }

                    //abdalla 3_18
                }
            }
        });
        holder.editTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
////                Toast.makeText(context, "Edit Was Clicked " + trip.getTripName(), Toast.LENGTH_LONG).show();
////                addNewBubble();
//                Log.e("hodaTest", " name  = " + trip.getTripId());
//                Log.e("hodaTest", " name  = " + trip.getTripName());
                Intent intent = new Intent(context, EditTrip.class);
                intent.putExtra("edtTrip", trip);
                context.startActivity(intent);
            }
        });
        holder.deleteTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tempValue = position;
                Trip temptrip = new Trip();
                Tripbase tripbase = new Tripbase(context);
                temptrip.setTripId(trip.getTripId());
//                long a  =tripbase.CancelledStatus(temptrip);
//                Log.e("DIDO","Hamada "+a);

                arrayList.remove(tempValue);
                notifyItemRemoved(tempValue);
                notifyItemRangeChanged(tempValue, getItemCount());
                //abdalla delete trip 15_3

                //  trip.getTripId()= Integer.parseInt(number.getText().toString());
                hm.remove(trip.getTripId());
                Intent intent = new Intent(context, MyReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, trip.getTripId(), intent, 0);
                pi = PendingIntent.getBroadcast(context, trip.getTripId(), intent, 0);
                alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pi);
                tripbase.DeleteTrip(temptrip);

            }
        });
        holder.detailTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Trip_Details.class);
                intent.putExtra("TRIPDETAILS", trip);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        //abdalla 3_17
        TextView txtTripState;
        ImageView imageView;
        Button startTrip, editTrip, deleteTrip, detailTrip;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tripname);
            imageView = itemView.findViewById(R.id.imageView);
            startTrip = itemView.findViewById(R.id.startTrip);
            editTrip = itemView.findViewById(R.id.editTrip);
            deleteTrip = itemView.findViewById(R.id.deleteTrip);
            detailTrip = itemView.findViewById(R.id.detailTrip);
            txtTripState = itemView.findViewById(R.id.txtTripState);

        }
    }
}
