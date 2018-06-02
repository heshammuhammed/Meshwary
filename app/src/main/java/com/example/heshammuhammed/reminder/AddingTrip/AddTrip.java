package com.example.heshammuhammed.reminder.AddingTrip;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.heshammuhammed.reminder.AlarmManager.MyReceiver;
import com.example.heshammuhammed.reminder.AlarmManager.reapatingActivity;
import com.example.heshammuhammed.reminder.DTO.Note;
import com.example.heshammuhammed.reminder.DTO.Trip;
import com.example.heshammuhammed.reminder.Database.Tripbase;
import com.example.heshammuhammed.reminder.MainHome.MainActivity;
import com.example.heshammuhammed.reminder.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddTrip extends AppCompatActivity {
    //abdalla 3_17
    Switch swRound;


    private static final String TAG = "StartAndEndTageAddTrip";
    FloatingActionButton floatingActionButton;
    private LatLng Startlocation;
    private LatLng Endlocation;
    private Button btnAddd;
    private EditText txtTripName;
    private String tripName;
    private String startPointName;
    private String endPointName;
    ArrayList<String> arrayList;
    public static ArrayList<String> notes;

    static long Value;


    //alarm manager & 3_16
    TimePicker time;
    DatePicker date;
    Button setTrip;
    int f = 0;
    int i = 0;
    int id;
    int trip_id;
    ArrayList intentArray = new ArrayList<PendingIntent>();
    java.util.Calendar current;
    HashMap<Integer, java.util.Calendar> hm = new HashMap<Integer, java.util.Calendar>();
    EditText number;
    EditText tripid;
    Button delete;
    Button repeat;
    String casetest;
    int nnumber;
    String item;
    String tripname = "hoda";
    SharedPreferences.Editor editor;
    // AlarmManager alarmManager= new AlarmManager();
    PendingIntent pendingIntent;
    Intent intent;
    AlarmManager alarmManager;
    PendingIntent pi;
    private AwesomeValidation awesomeValidation;
    //end of alarm manager


    //abdalla new Trip
    Trip myNewTrip;
    Date startDate;
    Date endDate;

    //end new Trip
    public AddTrip() {
        Startlocation = null;
        Endlocation = null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        //abdalla 3_17
        swRound = findViewById(R.id.swRound);


        //hoda alarm 3_16
        repeat = findViewById(R.id.repeat);


        //end hoda
        //abdalla 3_13
        Intent intent = getIntent();
        myNewTrip = (Trip) intent.getSerializableExtra("myNewTrip");


        //alarm manager
        time = findViewById(R.id.edtTimePicker);
        date = findViewById(R.id.datePicker);
        java.util.Calendar now = java.util.Calendar.getInstance();
        date.init(
                now.get(java.util.Calendar.YEAR),
                now.get(java.util.Calendar.MONTH),
                now.get(java.util.Calendar.DAY_OF_MONTH),
                null);
        time.setCurrentHour(now.get(java.util.Calendar.HOUR_OF_DAY));
        time.setCurrentMinute(now.get(java.util.Calendar.MINUTE));
        // end alarm manager

        txtTripName = (EditText) findViewById(R.id.edt_trip_name);
        floatingActionButton = findViewById(R.id.flbtnToDoList);
        btnAddd = (Button) findViewById(R.id.btnAdd);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.edt_trip_name, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        /*  auto complete Fragment  configuration */
        final PlaceAutocompleteFragment autocompleteFragmentStart = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_Start);

        final PlaceAutocompleteFragment autocompleteFragmentEnd = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_End);
/*
* The following code example shows setting an AutocompleteFilter on a PlaceAutocompleteFragment to
* set a filter returning only results with a precise address.
*/
//        tripName = "meshwar";
        //   tripName=  txtTripName.getText().toString();
        Log.i("Hamada1", txtTripName.getText().toString() + "555555");
        AutocompleteFilter typeFilterStart = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS).setCountry("EG")
                .build();
        AutocompleteFilter typeFilterEnd = new AutocompleteFilter.Builder()
                .setCountry("EG")
                .build();

        autocompleteFragmentStart.setFilter(typeFilterStart);
        autocompleteFragmentStart.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Startlocation = place.getLatLng();
                startPointName = place.getName().toString();
                Log.e(TAG, "Place: " + place.getName() + "Startlocation " + Startlocation);//get place details here
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Startlocation = null;
                startPointName = null;
                Log.i(TAG, "An error occurred: " + status);
            }

        });

        autocompleteFragmentEnd.setFilter(typeFilterEnd);
        autocompleteFragmentEnd.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Endlocation = place.getLatLng();
                endPointName = place.getName().toString();
                Log.e(TAG, "Place: " + place.getName() + "Endlocation " + Endlocation);//get place details here
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Endlocation = null;
                endPointName = null;
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        btnAddd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //abdalla 3_17
                Boolean switchState = false;
                switchState = swRound.isChecked();
                if (switchState) {
                    myNewTrip.setRoundTrip("ROUND");
                } else {
                    myNewTrip.setRoundTrip("NOTROUND");
                }

                Log.i("ROUND", myNewTrip.getRoundTrip());

                //hoda 3_16
                SharedPreferences sharedd;
                sharedd = getSharedPreferences("mypreff", 0);
                casetest = sharedd.getString("test", "non");
                nnumber = sharedd.getInt("number", 0);
                item = sharedd.getString("item", "non");
                //hoda 3_16 end

                //alarm manager
                //   trip_id=Integer.parseInt(tripid.getText().toString());
                if (awesomeValidation.validate() && Startlocation != null && Endlocation != null) {
                    current = java.util.Calendar.getInstance();
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.set(date.getYear(), date.getMonth(), date.getDayOfMonth(), time.getCurrentHour(), time.getCurrentMinute(), 00);
                    java.util.Calendar cal2 = java.util.Calendar.getInstance();
                    cal2.set(date.getYear(), date.getMonth(), date.getDayOfMonth(), time.getCurrentHour(), time.getCurrentMinute(), 00);
                    //abdalla 3_13 add new Trip
                    startDate = new Date(cal.getTimeInMillis());
                    endDate = new Date(cal2.getTimeInMillis());
                    Log.e("startDate", startDate.toString());
                    Log.e("endDate", endDate.toString());
                    //end Trip
                    if (cal.compareTo(current) <= 0) {
                        Toast.makeText(getApplicationContext(), "Invalid Date/Time",
                                Toast.LENGTH_LONG).show();
                    } else {


                        //end alarm manger

                        tripName = txtTripName.getText().toString();


                        if (Startlocation != null && Endlocation != null) {


                            //abdalla key soultion 3_15
                            String urls = "https://maps.googleapis.com/maps/api/directions/json?origin="
                                    + Startlocation.latitude + "," + Startlocation.longitude + "&destination="
                                    + Endlocation.latitude + "," + Endlocation.longitude +
                                    "&mode=driving&sensor=false";
                            //      "&key=AIzaSyCbVvAmsj1o1LB0S8GoFoz_p8Ciiu5eOZ8";
                            Log.i("Hamada", "Y3ny Eh ?????????");
                            Intent launchDownloadService = new Intent(AddTrip.this, DownloadingIntent.class);
                            launchDownloadService.putExtra("url", urls);
                            Log.e("Hamada", urls);
                            //abdalla 13_3 add new trip
                            myNewTrip.setTripName(tripName);
                            myNewTrip.setStartPointLatitude(Startlocation.latitude);
                            myNewTrip.setStartPointLongitude(Startlocation.longitude);
                            myNewTrip.setEndPointLatitude(Endlocation.latitude);
                            myNewTrip.setEndPointLongitude(Endlocation.longitude);
                            myNewTrip.setStartPointName(startPointName);
                            myNewTrip.setEndPointName(endPointName);
                            myNewTrip.setStartDate(startDate);
                            myNewTrip.setEndDate(endDate);
                            myNewTrip.setImageLink(urls);
                            myNewTrip.setStatus("UPCOMING");
                            Tripbase tripTest = new Tripbase(getApplicationContext());
                            long i = tripTest.updateTrip(myNewTrip);
                            launchDownloadService.putExtra("trip", myNewTrip);
                            Log.e("lol", "ccccccccccccccccccccccc" + i);

                            startService(launchDownloadService);
                            Log.e("lol", "dddddddddddddddddd");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                            abdalla 3_17
                            setAlarm(cal2);

//                            abdalla 3_18
//                            if(notes!=null)
//                                addNotes();

                            startActivity(intent);
                            finish();
                            //
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Enter Place",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        //hoda 16_3
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddTrip.this, reapatingActivity.class));
            }
        });
//hod end 16_3

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), AddingList.class);
                // Hamada
                if (arrayList != null) {
                    Log.e("Array", arrayList.size() + "");
                    intent.putExtra("myarraylist", arrayList);
                    intent.putExtra("InfArraylist", "fromAddTrip");
                }
                // Hamada
                startActivityForResult(intent, 1);

            }
        });
    }

    private void setAlarm(java.util.Calendar cal) {
        //calend.add(cal);
        hm.put(myNewTrip.getTripId(), cal);
        intentArray = new ArrayList<PendingIntent>();
        f = 0;
        Log.i("abdalla", "" + hm.size());
        for (Map.Entry<Integer, java.util.Calendar> me : hm.entrySet()) {
//            intent = new Intent(getBaseContext(), MyReceiver.class);
//            pendingIntent = PendingIntent.getBroadcast(getBaseContext(), me.getKey(), intent, 0);
//            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//            alarmManager.set(AlarmManager.RTC_WAKEUP, me.getValue().getTimeInMillis(), pendingIntent);
//            intentArray.add(pendingIntent);
            java.util.Calendar neww = me.getValue();
            intent = new Intent(getBaseContext(), MyReceiver.class);
            intent.putExtra("myNewTrip", myNewTrip);
            pendingIntent = PendingIntent.getBroadcast(getBaseContext(), me.getKey(), intent, 0);
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (casetest.equals("Daily")) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, neww.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                Log.i("hoda", "" + casetest);
            } else if (casetest.equals("Weekly")) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, neww.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
                Log.i("hoda", "" + casetest);
            } else if (casetest.equals("Every 30 Days")) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, neww.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 30, pendingIntent);
                Log.i("hoda", "" + casetest);
            } else if (casetest.equals("Every(Days)")) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, neww.getTimeInMillis(), AlarmManager.INTERVAL_DAY * nnumber, pendingIntent);
                Log.i("hoda", "" + casetest + nnumber);
            } else if (casetest.equals("Every(Weeks)")) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, neww.getTimeInMillis(), AlarmManager.INTERVAL_DAY * nnumber * 7, pendingIntent);
                Log.i("hoda", "" + casetest + nnumber);
            } else if (casetest.equals("Select Day Of Week")) {
                Log.i("hoda", "" + casetest);
                if (item.equals("Saturday")) {
                    Log.i("hoda", "" + item);
                    neww.set(java.util.Calendar.DAY_OF_WEEK, 0);
                    long interval = neww.getTimeInMillis() + 604800000L;
                    Log.i("Next Millis = ", "" + interval);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, neww.getTimeInMillis(), interval, pendingIntent);
                } else if (item.equals("Sunday")) {
                    Log.i("hoda", "" + item);
                    neww.set(java.util.Calendar.DAY_OF_WEEK, 1);
                    long interval = neww.getTimeInMillis() + 604800000L;
                    Log.i("Next Millis = ", "" + interval);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, neww.getTimeInMillis(), interval, pendingIntent);
                } else if (item.equals("Monday")) {
                    Log.i("hoda", "" + item);
                    neww.set(java.util.Calendar.DAY_OF_WEEK, 2);
                    long interval = neww.getTimeInMillis() + 604800000L;
                    Log.i("Next Millis = ", "" + interval);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, neww.getTimeInMillis(), interval, pendingIntent);
                } else if (item.equals("Tuesday")) {
                    Log.i("hoda", "" + item);
                    neww.set(java.util.Calendar.DAY_OF_WEEK, 3);
                    long interval = neww.getTimeInMillis() + 604800000L;
                    Log.i("Next Millis = ", "" + interval);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, neww.getTimeInMillis(), interval, pendingIntent);
                } else if (item.equals("Wednesday")) {
                    Log.i("hoda", "" + item);
                    neww.set(java.util.Calendar.DAY_OF_WEEK, 4);
                    long interval = neww.getTimeInMillis() + 604800000L;
                    Log.i("Next Millis = ", "" + interval);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, neww.getTimeInMillis(), interval, pendingIntent);
                } else if (item.equals("Wednesday")) {
                    Log.i("hoda", "" + item);
                    neww.set(java.util.Calendar.DAY_OF_WEEK, 5);
                    long interval = neww.getTimeInMillis() + 604800000L;
                    Log.i("Next Millis = ", "" + interval);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, neww.getTimeInMillis(), interval, pendingIntent);
                } else {
                    Log.i("hoda", "" + item);
                    neww.set(java.util.Calendar.DAY_OF_WEEK, 6);
                    long interval = neww.getTimeInMillis() + 604800000L;
                    Log.i("Next Millis = ", "" + interval);
                }
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, neww.getTimeInMillis(), pendingIntent);
                Log.i("hoda", "" + casetest);
            }
            intentArray.add(pendingIntent);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Log.e("Hamada", "Hamada1");
            Log.e("Hamada", resultCode + "");

            if (resultCode == 1) {
                arrayList = (ArrayList<String>) data.getSerializableExtra("Array");
            }
        }
    }

    //abdalla add notes 3_18
    public static void addNotes(ArrayList<String> arrayList) {

        notes = arrayList;
        if (notes != null)
            Log.e("u2", notes.size() + " not hopless case");

    }

    void addNotes() {
        for (int ii = 0; ii < notes.size(); ii++) {
            Log.e("u2", notes.size() + " Zombiee");
            Note note = new Note();
            note.setTripId(myNewTrip.getTripId());
            note.setTextNote("??????????????????????");
            note.setTextNote(notes.get(ii));
            note.setChecked(545);
            note.setStatus("OFF");
            Tripbase tripbase2 = new Tripbase(getApplication());
            int v = (int) tripbase2.CreateNote(note);
        }
    }
}
