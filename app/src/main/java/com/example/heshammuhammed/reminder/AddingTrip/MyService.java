package com.example.heshammuhammed.reminder.AddingTrip;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heshammuhammed.reminder.DTO.Note;
import com.example.heshammuhammed.reminder.Database.Tripbase;
import com.example.heshammuhammed.reminder.R;

import java.util.ArrayList;

public class MyService extends Service {

    WindowManager wm;
    LinearLayout linearLayout;
    ListView listView;
    String Name[];
    int Numbers[];
    String States[];
    int TripID;
    ArrayList<Note> noteArrayList;


    @Override
    public IBinder onBind(Intent intent) {
        TripID = intent.getIntExtra("TripID", 9999);
        Log.e("HamadaZZZ", TripID + "");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TripID = intent.getIntExtra("TripID", 9999);
        Log.e("HamadaZZZ", TripID + "");
        linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.activity_my_list_view, null);
        listView = linearLayout.findViewById(R.id.mylistview);

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        final WindowManager.LayoutParams parameters = new WindowManager.LayoutParams(
                500, 600, WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        parameters.gravity = Gravity.CENTER | Gravity.CENTER;
        parameters.x = 0;
        parameters.y = 0;

        Log.e("HAHA", "!!!!");
     /*   Toast.makeText(getApplicationContext(), "Trip ID ?? " + TripID, Toast.LENGTH_LONG).show();*/
        Note note = new Note();
        Log.e("HamadaZZZ", "Myyy " + TripID + "");
        note.setTripId(TripID);

        noteArrayList = new Tripbase(getApplicationContext()).getSpecificNotes(note);
        Log.e("Hamada", noteArrayList.size() + " YES");
        Name = new String[noteArrayList.size()];
        for (int i = 0; i < noteArrayList.size(); i++) {
            Note temp = noteArrayList.get(i);
            Name[i] = temp.getTextNote();
        }
        Numbers = new int[noteArrayList.size()];
        for (int i = 0; i < noteArrayList.size(); i++) {
            Note temp = noteArrayList.get(i);
            Numbers[i] = temp.getIdNote();
        }
        States = new String[noteArrayList.size()];
        for (int i = 0; i < noteArrayList.size(); i++) {
            Note temp = noteArrayList.get(i);
            States[i] = temp.getStatus();
        }

        Log.e("HAHA", "States Length " + States.length);

        listView.setAdapter(new Adapter(this, Name, Numbers, States));
        wm.addView(linearLayout, parameters);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //    Toast.makeText(getApplicationContext(), adapterView.getItemIdAtPosition(i) + " ", Toast.LENGTH_LONG).show();
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        wm.removeViewImmediate(linearLayout);
        stopSelf();
    }

    class Adapter extends ArrayAdapter<String> {
        Context context;
        String noteLists[];
        int Numbers[];
        String States[];

        public Adapter(Context context, String[] objects, int Numbers[], String States[]) {
            super(context, R.layout.my_editview, R.id.mytextcustom, objects);
            this.Numbers = Numbers;
            this.context = context;
            noteLists = objects;
            this.States = States;
        }


        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            CheckBox checkBox;
            TextView textView;
            View view;
            final Tripbase tripbase = new Tripbase(context);
            final Note note = new Note();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.my_editview, parent, false);
            textView = view.findViewById(R.id.mytextcustom);
            checkBox = view.findViewById(R.id.checkBox);
            Log.e("Sta", position + "  -o-  " + States[position]);
            if (States[position].equals("ON")) {
                Log.e("Sta", position + "  --  " + States[position]);
                checkBox.setChecked(true);
                States[position] = "ON";
            } else {
                States[position] = "OFF";
            }
            textView.setText(noteLists[position]);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    if (checked) {
                   //     Toast.makeText(context, "Note is " + noteLists[position] + " Id is" + Numbers[position], Toast.LENGTH_LONG).show();
                        note.setIdNote(Numbers[position]);
                        note.setStatus("ON");
                        tripbase.ChangeNoteOn2(note);
                        States[position] = "ON";
                        notifyDataSetChanged();
                    } else {
                    //    Toast.makeText(context, "Closed", Toast.LENGTH_LONG).show();
                        note.setIdNote(Numbers[position]);
                        note.setStatus("OFF");
                        tripbase.ChangeNoteOff2(note);
                        States[position] = "OFF";
                        notifyDataSetChanged();
                    }
                }
            });
            return view;
        }
    }

}