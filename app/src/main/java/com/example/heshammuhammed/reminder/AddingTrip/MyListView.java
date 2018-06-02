package com.example.heshammuhammed.reminder.AddingTrip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.heshammuhammed.reminder.Adapter.MyCustomAdapter;
import com.example.heshammuhammed.reminder.DTO.Note;
import com.example.heshammuhammed.reminder.Database.Tripbase;
import com.example.heshammuhammed.reminder.R;

import java.util.ArrayList;

public class MyListView extends Activity {

    ListView listView;
    String Name[];
    int Numbers[];
    String States[];
    Intent intent;
    ArrayList<Note> noteArrayList;
    Button button;
    int width;
    int height;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list_view);
        moveTaskToBack(true);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        getWindow().setLayout((int) (width*0.8),(int)(height*0.6));
        listView = findViewById(R.id.mylistview);
        intent = getIntent();
        Note note = new Note();
        note.setTripId(intent.getIntExtra("TripID", 0));
        noteArrayList = new Tripbase(getApplicationContext()).getSpecificNotes(note);
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                MyCustomAdapter.state=true;

            }
        });
        listView.setAdapter(new Adapter(this, Name, Numbers,States));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//              //  Toast.makeText(getApplicationContext(), adapterView.getItemIdAtPosition(i) + " ", Toast.LENGTH_LONG).show();
            }
        });
    }

    class Adapter extends ArrayAdapter<String> {
        Context context;
        String noteLists[];
        int Numbers[];
        String States[];

        public Adapter(Context context, String[] objects, int Numbers[],String States[]) {
            super(context, R.layout.my_editview, R.id.mytextcustom, objects);
            this.Numbers = Numbers;
            this.context = context;
            noteLists = objects;
            this.States=States;
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
            Log.e("Sta",position+"  -o-  "+States[position]);
            if (States[position].equals("ON")){
                Log.e("Sta",position+"  --  "+States[position]);
                checkBox.setChecked(true);
                States[position]="ON";
            }else{
                States[position]="OFF";
            }
            textView.setText(noteLists[position]);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    if (checked) {
               //         Toast.makeText(context, "Note is " + noteLists[position] + " Id is" + Numbers[position], Toast.LENGTH_LONG).show();
                        note.setIdNote(Numbers[position]);
                        note.setStatus("ON");
                        tripbase.ChangeNoteOn2(note);
                        States[position]="ON";
                        notifyDataSetChanged();
                    } else {
                  //      Toast.makeText(context, "Closed", Toast.LENGTH_LONG).show();
                        note.setIdNote(Numbers[position]);
                        note.setStatus("OFF");
                        tripbase.ChangeNoteOff2(note);
                        States[position]="OFF";
                        notifyDataSetChanged();
                    }
                }
            });

            return view;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyCustomAdapter.state=true;
    }
}
