package com.example.heshammuhammed.reminder.AddingTrip;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.heshammuhammed.reminder.DTO.Note;
import com.example.heshammuhammed.reminder.DTO.Trip;
import com.example.heshammuhammed.reminder.Database.Tripbase;

import java.util.ArrayList;

/**
 * Created by abdalla on 3/21/2018.
 */

public class EditingTrip extends IntentService {

    Trip trip;
    Context context;


    public EditingTrip() {
        super("EditingTrip");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        trip = (Trip) intent.getSerializableExtra("trip1");
        context = getApplicationContext();
        editNotes(trip);


    }


    public static ArrayList<String> notes;

    public static void AAAA(ArrayList<String> arrayList) {
        notes = arrayList;
    }

    public void editNotes(Trip trip) {
        Tripbase tripbase2 = new Tripbase(context);
        tripbase2.DeleteNotes(trip);

        for (int ii = 0; ii < notes.size(); ii++) {
            //  Log.e("u2", notes.size() + " test abdalla");
            Note note = new Note();
            //      Log.e("tripKeyOn", trip.getTripId() + "  111");
            note.setTripId(trip.getTripId());
            note.setTextNote(notes.get(ii));


            note.setStatus("OFF");
            int v = (int) tripbase2.CreateNote(note);

        }

        notes = null;
        stopSelf();
    }
}
