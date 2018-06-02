package com.example.heshammuhammed.reminder.EditingTrip;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.heshammuhammed.reminder.AddingTrip.AddTrip;
import com.example.heshammuhammed.reminder.AddingTrip.AddingList;
import com.example.heshammuhammed.reminder.AddingTrip.DownloadingIntent;
import com.example.heshammuhammed.reminder.AddingTrip.EditingTrip;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class EditTrip extends AppCompatActivity {

    boolean placeChageStartFlag;
    boolean placeChageEndFlag;

    EditText edtNameOfTrip;
    TimePicker edtTimePicker;
    DatePicker edtDatePicker;
    FloatingActionButton edtFloatingActionButton;
    Button edtSubmit, edtRepeat;
    Trip trip;

    //abdalla 3_18
    private String tripName;

    Switch edtRound;
    private LatLng Startlocation;
    private LatLng Endlocation;
    private String startPointName;
    private String endPointName;

    ArrayList<Note> arrayListNote;
    private static final String TAG = "StartAndEndTageEditTrip";

    Date startDate;
    java.util.Calendar current;
    int f = 0;
    ArrayList intentArray = new ArrayList<PendingIntent>();
    HashMap<Integer, Calendar> hm = new HashMap<Integer, java.util.Calendar>();
    String casetest;
    int nnumber;
    String item;
    PendingIntent pendingIntent;
    Intent intent;
    PendingIntent pi;
    AlarmManager alarmManager;
    private AwesomeValidation awesomeValidation;
    static boolean edtNotesFlag;

    //3_18_T_8
    ArrayList<String> arrayList;
    public static ArrayList<String> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);


        //edtNotes
        edtNotesFlag = false;

        arrayList = new ArrayList<>();

        placeChageStartFlag = false;
        placeChageEndFlag = false;
        //abdalla 3_18
        edtRound = findViewById(R.id.edtRound);
        edtRepeat = findViewById(R.id.edtRepeat);


        Intent intent = getIntent();
        trip = (Trip) intent.getSerializableExtra("edtTrip");
        final Tripbase tripbase2 = new Tripbase(this);
        ArrayList<Note> oldTripNotes = tripbase2.getSpecificNotes(trip);
        if (oldTripNotes != null) {
            for (int ii = 0; ii < oldTripNotes.size(); ii++) {
                Note oldNote = oldTripNotes.get(ii);
//                            note.setTripId(trip.getTripId());
                Log.e("3_18", oldNote.getTextNote());
                Log.e("3_18", oldNote.getStatus());
                Log.e("3_18", "" + oldNote.getChecked());
                arrayList.add(oldNote.getTextNote());
                //   Toast.makeText(this, "Trip Notes Updated successful", Toast.LENGTH_LONG).show();
            }
        }


        Log.e("hodaTest", " name  = " + trip.getTripId());
        edtNameOfTrip = findViewById(R.id.edtNameOfTrip);
        edtTimePicker = findViewById(R.id.edtTimePicker);
        edtDatePicker = findViewById(R.id.edtDatePicker);
        edtFloatingActionButton = findViewById(R.id.edtFloatingActionButton);
        edtSubmit = findViewById(R.id.edtSubmit);
        edtRepeat = findViewById(R.id.edtRepeat);


        edtRound.setChecked(trip.getRoundTrip().equals("ROUND"));

        edtNameOfTrip.setText(trip.getTripName());
        Calendar cal = Calendar.getInstance();
        cal.setTime(trip.getStartDate());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        edtTimePicker.setCurrentHour(trip.getStartDate().getHours());
        edtTimePicker.setCurrentMinute(trip.getStartDate().getMinutes());
        Log.e("hodaTest", " H = " + trip.getStartDate().getHours() + " M = " + trip.getStartDate().getMinutes());
        Log.e("hodaTest", " y = " + trip.getStartDate().getYear() + " m = " + trip.getStartDate().getMonth() + " d =" + trip.getStartDate().getDay());
        // edtDatePicker.updateDate(trip.getStartDate().getYear(),trip.getStartDate().getMonth(),trip.getStartDate().getDay());
        edtDatePicker.init(year, month, day, null);
        final PlaceAutocompleteFragment autocompleteFragmentStart = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_Start_edt);

        final PlaceAutocompleteFragment autocompleteFragmentEnd = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_End_edt);
        autocompleteFragmentStart.setText(trip.getStartPointName());
        autocompleteFragmentEnd.setText(trip.getEndPointName());


        //3_18
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.edtNameOfTrip, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);


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
                placeChageStartFlag = true;
                // TODO: Get info about the selected place.
                Startlocation = place.getLatLng();
                startPointName = place.getName().toString();
                Log.e(TAG, "Place: " + place.getName() + "Startlocation " + Startlocation);//get place details here
            }

            @Override
            public void onError(Status status) {
                placeChageStartFlag = false;
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
                placeChageEndFlag = true;
                // TODO: Get info about the selected place.
                Endlocation = place.getLatLng();
                endPointName = place.getName().toString();
                Log.e(TAG, "Place: " + place.getName() + "Endlocation " + Endlocation);//get place details here
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                placeChageEndFlag = false;
                Endlocation = null;
                endPointName = null;
                Log.i(TAG, "An error occurred: " + status);
            }
        });


        edtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Boolean switchState = false;
                switchState = edtRound.isChecked();
                if (switchState) {
                    trip.setRoundTrip("ROUND");
                } else {
                    trip.setRoundTrip("NOTROUND");
                }


                //hoda 3_18
                SharedPreferences sharedd;
                sharedd = getSharedPreferences("mypreff", 0);
                casetest = sharedd.getString("test", "non");
                nnumber = sharedd.getInt("number", 0);
                item = sharedd.getString("item", "non");
                //hoda 3_18 end

                if (awesomeValidation.validate()) {
                    tripName = edtNameOfTrip.getText().toString();
                    trip.setTripName(tripName);
                }
                current = java.util.Calendar.getInstance();
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.set(edtDatePicker.getYear(), edtDatePicker.getMonth(), edtDatePicker.getDayOfMonth(), edtTimePicker.getCurrentHour(), edtTimePicker.getCurrentMinute(), 00);
                //abdalla 3_13 add new Trip
                startDate = new Date(cal.getTimeInMillis());
                Log.e("startDate", startDate.toString());
                //end Trip
                if (cal.compareTo(current) <= 0) {
                    Toast.makeText(getApplicationContext(), "Invalid Date/Time",
                            Toast.LENGTH_LONG).show();
                } else {
                    setAlarm(cal);
                    trip.setStartDate(startDate);
                    trip.setEndDate(new Date());
                }
                if (Startlocation != null && Endlocation != null) {

                    Log.e("lol", Startlocation.toString());
                    Log.e("lol", Endlocation.toString());

//
                    //abdalla key soultion 3_15
                    String urls = "https://maps.googleapis.com/maps/api/directions/json?origin=" + Startlocation.latitude + "," + Startlocation.longitude + "&destination=" + Endlocation.latitude + "," + Endlocation.longitude + "&mode=driving&sensor=false&key=AIzaSyCbVvAmsj1o1LB0S8GoFoz_p8Ciiu5eOZ8";
                    Intent launchDownloadService = new Intent(EditTrip.this, DownloadingIntent.class);
                    launchDownloadService.putExtra("url", urls);
                    Log.e("Hamada", urls);
                    trip.setStartPointLatitude(Startlocation.latitude);
                    trip.setStartPointLongitude(Startlocation.longitude);
                    trip.setEndPointLatitude(Endlocation.latitude);
                    trip.setEndPointLongitude(Endlocation.longitude);
                    trip.setStartPointName(startPointName);
                    trip.setEndPointName(endPointName);
                    trip.setImageLink(urls);
                    launchDownloadService.putExtra("trip", trip);

                    startService(launchDownloadService);
                } else if (Startlocation != null) {
//
                    //abdalla key soultion 3_15
                    String urls = "https://maps.googleapis.com/maps/api/directions/json?origin=" + Startlocation.latitude + "," + Startlocation.longitude + "&destination=" + trip.getEndPointLatitude() + "," + trip.getStartPointLatitude() + "&mode=driving&sensor=false&key=AIzaSyCbVvAmsj1o1LB0S8GoFoz_p8Ciiu5eOZ8";
                    Intent launchDownloadService = new Intent(EditTrip.this, DownloadingIntent.class);
                    launchDownloadService.putExtra("url", urls);
                    Log.e("Hamada", urls);
                    trip.setStartPointLatitude(Startlocation.latitude);
                    trip.setStartPointLongitude(Startlocation.longitude);
                    trip.setStartPointName(startPointName);
                    launchDownloadService.putExtra("trip", trip);
                    startService(launchDownloadService);
                } else if (Endlocation != null) {
//                    Tripbase tripbase2 = new Tripbase(getApplication());
//                    int v = (int) tripbase2.DeleteNotes(trip);
                    //abdalla key soultion 3_15
                    String urls = "https://maps.googleapis.com/maps/api/directions/json?origin=" + trip.getStartPointLatitude() + "," + trip.getStartPointLongitude() + "&destination=" + Endlocation.latitude + "," + Endlocation.longitude + "&mode=driving&sensor=false&key=AIzaSyCbVvAmsj1o1LB0S8GoFoz_p8Ciiu5eOZ8";
                    Intent launchDownloadService = new Intent(EditTrip.this, DownloadingIntent.class);
                    launchDownloadService.putExtra("url", urls);
                    Log.e("Hamada", urls);
                    trip.setEndPointLatitude(Endlocation.latitude);
                    trip.setEndPointLongitude(Endlocation.longitude);
                    trip.setEndPointName(endPointName);
                    launchDownloadService.putExtra("trip", trip);
                    startService(launchDownloadService);
                }


                if (awesomeValidation.validate()) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                            abdalla 3_18
                    setAlarm(cal);

                    tripbase2.updateTrip(trip);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Enter Valid Name",
                            Toast.LENGTH_LONG).show();
                }

                if (edtNotesFlag) {
                    Intent EditingTrip2 = new Intent(getApplicationContext(), EditingTrip.class);

                    EditingTrip2.putExtra("trip1", trip);
                    EditingTrip2.putExtra("c", "y");
                    Log.e("keyTrip", "" + trip.getTripId());
                    startService(EditingTrip2);
//
//                    EditingTrip editingTrip= new EditingTrip();
//                    editingTrip.editNotes(trip);
                }

            }
        });

        edtRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tripbase tripbase2 = new Tripbase(getApplication());
                int v = (int) tripbase2.DeleteNotes(trip);
                //abdalla 3_17
                hm.remove(trip.getTripId());
                Intent intentAlarm = new Intent(getApplicationContext(), MyReceiver.class);
                pi = PendingIntent.getBroadcast(getApplicationContext(), trip.getTripId(), intentAlarm, 0);
                alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pi);
                startActivity(new Intent(EditTrip.this, reapatingActivity.class));
            }
        });

        edtFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtNotesFlag = true;
                //      Toast.makeText(getApplicationContext(), "Yes", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), AddingList.class);

                // Hamada
                if (true) {
                    Log.e("Array", arrayList.size() + "");
                    intent.putExtra("myarraylist", arrayList);
                    arrayList.get(0);
                    Tripbase tripbase2 = new Tripbase(getApplication());

                    intent.putExtra("InfArraylist", "fromEditTrip");
                }
                // Hamada
                startActivityForResult(intent, 1);

            }
        });
        edtNotesFlag = false;
    }


    private void setAlarm(java.util.Calendar cal) {
        //calend.add(cal);
        hm.put(trip.getTripId(), cal);
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
            intent.putExtra("myNewTrip", trip);
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




}
