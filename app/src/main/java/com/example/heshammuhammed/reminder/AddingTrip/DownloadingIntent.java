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
import com.example.heshammuhammed.reminder.MainHome.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class DownloadingIntent extends IntentService {

    static Context context;
    public static ArrayList<String> notes;
    Trip trip;

    public DownloadingIntent() {

        super("Downloading Intent");

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        trip = (Trip) intent.getSerializableExtra("trip");
        Log.e("Hamadazz", trip.getStartDate().toString());
        Log.e("Hamadazz", trip.getStartDate().toString());
        Log.e("Hamada", "this is onHandleIntent");
        final StringBuilder stringBuilder = new StringBuilder();
        context = getApplicationContext();
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(intent.getStringExtra("url"));


            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            int data = in.read();
            while (data != -1) {
                char current = (char) data;
                data = in.read();
                stringBuilder.append((char) current);
                //  myUrl[0] +=""+current;
                //   System.out.print(current);
            }
        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                // long i =
                if (stringBuilder != null)
                    parseJson(stringBuilder.toString(), intent);
                //SendMessage((int) i);
                //    Log.e("Hamada","Trip ID is "+i);
                urlConnection.disconnect();
            }
        }
    }

    public static void AAAA(ArrayList<String> arrayList) {

        notes = arrayList;
        if (notes != null)
            Log.e("u2", notes.size() + " not hopless case");


    }

    public void parseJson(final String response, Intent intent) {

        Log.e("abdallatest", response);
        Log.e("abdallatestTrip", trip.getTripName());
        double dist;
        try {

            JSONObject jsonObject = new JSONObject(response);

            //  Log.i("abdallatest", response);

            JSONArray array = jsonObject.getJSONArray("routes");

            JSONObject routes = array.getJSONObject(0);

            JSONArray legs = routes.getJSONArray("legs");

            JSONObject steps = legs.getJSONObject(0);

            JSONObject distance = steps.getJSONObject("distance");

            Log.e("Distance", distance.toString());
            dist = Double.parseDouble(distance.getString("text").replaceAll("[^\\.0123456789]", ""));

            Log.e("DoubleDistance", "" + dist);
            JSONObject overview_polyline = routes.getJSONObject("overview_polyline");
            Log.e("overview_polyline", overview_polyline.toString());

            trip.setDistance(dist);
            trip.setImageLink(overview_polyline.getString("points"));
            trip.setStatus("UPCOMING");
            //UPCOMING,CANCELLED


            Tripbase tripTest = new Tripbase(getApplicationContext());
            long i = tripTest.updateTrip(trip);
            Log.e("TripUpdate", "Trip ID" + trip.getTripId() + "");
            Log.e("TripUpdate", "Trip Name " + trip.getImageLink());
            Log.e("TripUpdate", "Trip Start Date " + trip.getStartDate());
            Log.e("TripUpdate", "Trip Start Name " + trip.getTripName());

            if (notes != null)
                addNotes();


        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent2);
        stopSelf();
    }

    //abdalla 3_17
    void addNotes() {
        for (int ii = 0; ii < notes.size(); ii++) {
            Log.e("u2", notes.size() + " Zombiee");
            Note note = new Note();
            note.setTripId(trip.getTripId());

            note.setTextNote(notes.get(ii));
            note.setChecked(545);
            note.setStatus("OFF");
            Tripbase tripbase2 = new Tripbase(getApplication());
            int v = (int) tripbase2.CreateNote(note);
            //       Log.e("Hamada", "Message is " + notes.get(i) + "and " + v);

        }
        notes = null;
    }


}
