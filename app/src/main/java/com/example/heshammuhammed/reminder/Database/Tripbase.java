package com.example.heshammuhammed.reminder.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.heshammuhammed.reminder.DTO.Note;
import com.example.heshammuhammed.reminder.DTO.Trip;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by HeshamMuhammed on 3/4/2018.
 */

public class Tripbase {

    SQL sql;

    public Tripbase(Context context) {
        sql = new SQL(context);
    }


    public long CreateTrip(Trip trip) {
        long ID = (long) 0.0;
        try {
            SQLiteDatabase sqLiteDatabase = sql.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(SQL.TRIP_NAME, trip.getTripName());
            contentValues.put(SQL.START_POINT_NAME, trip.getStartPointName());
            contentValues.put(SQL.END_POINT_NAME, trip.getEndPointName());
            contentValues.put(SQL.START_POINT_LATITUDE, trip.getStartPointLatitude());
            contentValues.put(SQL.START_POINT_LONGITUDE, trip.getStartPointLongitude());
            contentValues.put(SQL.END_POINT_LATITUDE, trip.getEndPointLatitude());
            contentValues.put(SQL.END_POINT_LONGITUDE, trip.getEndPointLongitude());
            contentValues.put(SQL.START_DATE, trip.getStartDate().toString());
            contentValues.put(SQL.END_DATE, trip.getEndDate().toString());
            contentValues.put(SQL.STATUS, trip.getStatus());
            contentValues.put(SQL.DISTANCE, trip.getDistance());
            contentValues.put(SQL.IMAGE_LINK, trip.getImageLink());
            //abdalla _3_17
            contentValues.put(SQL.ROUNDTRIP, trip.getRoundTrip());
            ID = sqLiteDatabase.insert(SQL.TABLE_NAME_TRIP, null, contentValues);
            Log.e("Hamada", "Ana Fl SQL " + ID);
        } catch (Exception e) {
            Log.e("Databaseee", " error" + e.getMessage().toString());
            Log.e("Databaseee", e.getMessage().toString());
        }
        return ID;
    }

    public int getProfilesCount() {
        String countQuery = "SELECT  * FROM " + SQL.TABLE_NAME_NOTE;
        SQLiteDatabase db = sql.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        Log.e("Hamada", "Number of Rows !!! " + count);
        cursor.close();
        return count;
    }

    public long CreateNote(Note note) {
        long ID = (long) 0.0;
        try {
            SQLiteDatabase sqLiteDatabase = sql.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(SQL.TRIP_ID, note.getTripId());
            contentValues.put(SQL.TEXT_NOTE, note.getTextNote());
            contentValues.put(SQL.TEXT_STATUS, note.getStatus());
            contentValues.put(SQL.CHECKED, note.getChecked());
            ID = sqLiteDatabase.insert(SQL.TABLE_NAME_NOTE, null, contentValues);
        } catch (Exception e) {
            Log.e("12345", e.getMessage().toString());
        }
        return ID;
    }

    public ArrayList<Note> getSpecificNotes(Note note) {
        SQLiteDatabase sqLiteDatabase = sql.getWritableDatabase();
        String columns[] = {SQL.ID_NOTE, SQL.ID_Trip, SQL.TEXT_NOTE, SQL.TEXT_STATUS, SQL.CHECKED};
        Cursor cursor = sqLiteDatabase.query(SQL.TABLE_NAME_NOTE, columns, SQL.TRIP_ID + " = '" + note.getTripId() + "'", null, null, null, null);
        ArrayList<Note> arrayList = new ArrayList<>();
        Log.e("Hamada", "Note ID " + note.getTripId());
        Log.e("Hamada", "SQL ID " + SQL.ID_Trip);
        Log.e("Hamada", "?????????????");
        while (cursor.moveToNext()) {
            Note tempNote = new Note();
            tempNote.setIdNote(cursor.getInt(0));
            tempNote.setTripId(cursor.getInt(1));
            tempNote.setTextNote(cursor.getString(2));
            tempNote.setStatus(cursor.getString(3));
            tempNote.setChecked(cursor.getInt(4));
            arrayList.add(tempNote);
            Log.e("Hamada", "ID is " + cursor.getInt(0) + "");
            Log.e("Hamada", "Message is " + cursor.getString(2) + "");
        }
        return arrayList;
    }
    public ArrayList<Note> getSpecificNotes(Trip trip) {
        SQLiteDatabase sqLiteDatabase = sql.getWritableDatabase();
        String columns[] = {SQL.ID_NOTE, SQL.ID_Trip, SQL.TEXT_NOTE, SQL.TEXT_STATUS, SQL.CHECKED};
        Cursor cursor = sqLiteDatabase.query(SQL.TABLE_NAME_NOTE, columns, SQL.TRIP_ID + " = '" + trip.getTripId() + "'", null, null, null, null);
        ArrayList<Note> arrayList = new ArrayList<>();
        Log.e("Hamada", "Note ID " + trip.getTripId());
        Log.e("Hamada", "SQL ID " + SQL.ID_Trip);
        Log.e("Hamada", "?????????????");
        while (cursor.moveToNext()) {
            Note tempNote = new Note();
            tempNote.setIdNote(cursor.getInt(0));
            tempNote.setTripId(cursor.getInt(1));
            tempNote.setTextNote(cursor.getString(2));
            tempNote.setStatus(cursor.getString(3));
            tempNote.setChecked(cursor.getInt(4));
            arrayList.add(tempNote);
            Log.e("Hamada", "ID is " + cursor.getInt(0) + "");
            Log.e("Hamada", "Message is " + cursor.getString(2) + "");
        }
        return arrayList;
    }

    public int DeleteTrip(Trip trip) {
        SQLiteDatabase sqLiteDatabase = sql.getReadableDatabase();
        String whereArgs[] = {Integer.toString(trip.getTripId())};
        String query = SQL.ID_Trip + "=?";
        int count = sqLiteDatabase.delete(SQL.TABLE_NAME_TRIP, query, whereArgs);
        return count;
    }
    public int DeleteAllNotes(Note node) {
        SQLiteDatabase sqLiteDatabase = sql.getReadableDatabase();
        String whereArgs[] = {Integer.toString(node.getTripId())};
        String query = SQL.ID_Trip + "=?";
        int count = sqLiteDatabase.delete(SQL.TABLE_NAME_NOTE, query, whereArgs);
        sqLiteDatabase.rawQuery("DELETE FROM "+SQL.TABLE_NAME_NOTE+" WHERE "+SQL.ID_Trip + "in",whereArgs);
        return count;
    }

    public int DeleteNotes(Trip trip){
        SQLiteDatabase sqLiteDatabase = sql.getReadableDatabase();
        String [] whereArgs = {trip.getTripId()+""};
        int count = sqLiteDatabase.delete(SQL.TABLE_NAME_NOTE,SQL.TRIP_ID+" =? ",whereArgs);
        Log.e("Dele","Deleted !!"+count);
        return count;
    }

    public ArrayList<Trip> SelectUpComingTrips() {
        ArrayList<Trip> trips = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = sql.getWritableDatabase();
        String[] columns = {SQL.ID_Trip, SQL.START_POINT_LATITUDE, SQL.START_POINT_LONGITUDE,
                SQL.END_POINT_LATITUDE, SQL.END_POINT_LONGITUDE, SQL.START_POINT_NAME, SQL.END_POINT_NAME
                , SQL.START_DATE, SQL.END_DATE, SQL.STATUS, SQL.DISTANCE, SQL.TRIP_NAME, SQL.IMAGE_LINK,SQL.ROUNDTRIP};

        Cursor cursor = sqLiteDatabase.query(SQL.TABLE_NAME_TRIP, columns, SQL.STATUS + " = '" + "UPCOMING" + "'"+" OR "+"  "+SQL.STATUS + " = '" + "PROGRESS"+"'", null, null, null, SQL.START_DATE+" DESC");
        Trip trip;
        while (cursor.moveToNext()) {
            try {
                trip = new Trip();
                trip.setTripId(cursor.getInt(0));
                trip.setStartPointLatitude(cursor.getDouble(1));
                trip.setStartPointLongitude(cursor.getDouble(2));
                trip.setEndPointLatitude(cursor.getDouble(3));
                trip.setEndPointLongitude(cursor.getDouble(4));
                trip.setStartPointName(cursor.getString(5));
                trip.setEndPointName(cursor.getString(6));
                trip.setStartDate(new Date(cursor.getString(7)));
                trip.setEndDate(new Date(cursor.getString(8)));
                trip.setStatus(cursor.getString(9));
                trip.setDistance(cursor.getDouble(10));
                trip.setTripName(cursor.getString(11));
                trip.setImageLink(cursor.getString(12));
                trip.setRoundTrip(cursor.getString(13));
                trips.add(trip);
            } catch (Exception e) {
                Log.i("Error", e.getMessage().toString());
            }
        }
        return trips;
    }

    public ArrayList<Trip> SelectHistoryTrips() {
        ArrayList<Trip> trips = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = sql.getWritableDatabase();
        String[] columns = {SQL.ID_Trip, SQL.START_POINT_LATITUDE, SQL.START_POINT_LONGITUDE,
                SQL.END_POINT_LATITUDE, SQL.END_POINT_LONGITUDE, SQL.START_POINT_NAME, SQL.END_POINT_NAME
                , SQL.START_DATE, SQL.END_DATE, SQL.STATUS, SQL.DISTANCE, SQL.TRIP_NAME, SQL.IMAGE_LINK,SQL.ROUNDTRIP};

        Cursor cursor = sqLiteDatabase.query(SQL.TABLE_NAME_TRIP, columns, SQL.STATUS + " = '" + "CANCELLED" + "'"+" OR "+"  "+SQL.STATUS + " = '" + "DONE"+"'", null, null, null, null);
        Trip trip;
        while (cursor.moveToNext()) {
            try {
                trip = new Trip();
                trip.setTripId(cursor.getInt(0));
                trip.setStartPointLatitude(cursor.getDouble(1));
                trip.setStartPointLongitude(cursor.getDouble(2));
                trip.setEndPointLatitude(cursor.getDouble(3));
                trip.setEndPointLongitude(cursor.getDouble(4));
                trip.setStartPointName(cursor.getString(5));
                trip.setEndPointName(cursor.getString(6));
                trip.setStartDate(new Date(cursor.getString(7)));
                trip.setEndDate(new Date(cursor.getString(8)));
                trip.setStatus(cursor.getString(9));
                trip.setDistance(cursor.getDouble(10));
                trip.setTripName(cursor.getString(11));
                trip.setImageLink(cursor.getString(12));
                trip.setRoundTrip(cursor.getString(13));
                trips.add(trip);
            } catch (Exception e) {
                Log.i("Error", e.getMessage().toString());
            }
        }
        return trips;
    }

    public int CancelledStatus(Trip trip) {
        SQLiteDatabase sqLiteDatabase = sql.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQL.STATUS, "CANCELLED");
        String whereArgs[] = {"UPCOMING", trip.getTripId() + ""};
        int a = sqLiteDatabase.update(SQL.TABLE_NAME_TRIP, contentValues, SQL.STATUS + " =? AND " + SQL.ID_Trip + " =? ", whereArgs);
        return a;
    }

    //abdalla_3_13
    public int updateTrip(Trip trip) {
        SQLiteDatabase sqLiteDatabase = sql.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQL.TRIP_NAME, trip.getTripName());
        contentValues.put(SQL.START_POINT_NAME, trip.getStartPointName());
        contentValues.put(SQL.END_POINT_NAME, trip.getEndPointName());
        contentValues.put(SQL.START_POINT_LATITUDE, trip.getStartPointLatitude());
        contentValues.put(SQL.START_POINT_LONGITUDE, trip.getStartPointLongitude());
        contentValues.put(SQL.END_POINT_LATITUDE, trip.getEndPointLatitude());
        contentValues.put(SQL.END_POINT_LONGITUDE, trip.getEndPointLongitude());
        contentValues.put(SQL.START_DATE, trip.getStartDate().toString());
        contentValues.put(SQL.END_DATE, trip.getEndDate().toString());
        contentValues.put(SQL.STATUS, trip.getStatus());
        contentValues.put(SQL.DISTANCE, trip.getDistance());
        contentValues.put(SQL.IMAGE_LINK, trip.getImageLink());
        //abdalla_3_17
        contentValues.put(SQL.ROUNDTRIP, trip.getRoundTrip());
        String Name[] = {trip.getTripId() + ""};
        int a = sqLiteDatabase.update(SQL.TABLE_NAME_TRIP, contentValues, SQL.ID_Trip + " =? ", Name);
        Log.e("TripSQL", "Trip Is Created ? " + a);
        Log.e("TripSQL", "Trip ID is " + trip.getTripId());
        Log.e("TripSQL", "Trip Name is " + trip.getTripName());
        Log.e("TripSQL", "Trip Start Date is " + trip.getStartDate());
        Log.e("TripSQL", "Trip End Date is " + trip.getEndDate());
        Log.e("TripSQL", "Trip Image is " + trip.getImageLink());
        Log.e("TripSQL", "Trip Start Long" + trip.getStartPointLongitude());
        Log.e("TripSQL", "Trip Start Lat " + trip.getStartPointLatitude());
        Log.e("TripSQL", "Trip End Long " + trip.getEndPointLongitude());
        Log.e("TripSQL", "Trip End Lat " + trip.getEndPointLatitude());
        Log.e("TripSQL", "Trip Start Point Name is " + trip.getStartPointName());
        Log.e("TripSQL", "Trip End Point Name is " + trip.getEndPointName());
        Log.e("TripSQL", "Trip Distance " + trip.getDistance());
        Log.e("TripSQL", "Trip Stataes " + trip.getStatus());
        return a;
    }

    public void ChangeNoteOn(ArrayList<Note> arrayList) {
        SQLiteDatabase sqLiteDatabase = sql.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < arrayList.size(); i++) {
            Note note = arrayList.get(i);
            contentValues.put(SQL.TEXT_STATUS, "ON");
            String Name[] = {note.getIdNote() + ""};
            sqLiteDatabase.update(SQL.TABLE_NAME_NOTE, contentValues, SQL.ID_NOTE + " =? ", Name);
        }
    }

    public void ChangeNoteOff(ArrayList<Note> arrayList) {
        SQLiteDatabase sqLiteDatabase = sql.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < arrayList.size(); i++) {
            Note note = arrayList.get(i);
            contentValues.put(SQL.TEXT_STATUS, "OFF");
            String Name[] = {note.getIdNote() + ""};
            sqLiteDatabase.update(SQL.TABLE_NAME_NOTE, contentValues, SQL.ID_NOTE + " =? ", Name);
        }
    }

    public void ChangeNoteOn2(Note note) {
        SQLiteDatabase sqLiteDatabase = sql.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQL.TEXT_STATUS, "ON");
        String Name[] = {note.getIdNote() + ""};
        int a = sqLiteDatabase.update(SQL.TABLE_NAME_NOTE, contentValues, SQL.ID_NOTE + " =? ", Name);
        Log.e("Is", "On " + note.getIdNote());
        Log.e("Is", "On " + a);
    }

    public void ChangeNoteOff2(Note note) {
        SQLiteDatabase sqLiteDatabase = sql.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQL.TEXT_STATUS, "OFF");
        String Name[] = {note.getIdNote() + ""};
        int a = sqLiteDatabase.update(SQL.TABLE_NAME_NOTE, contentValues, SQL.ID_NOTE + " =? ", Name);
        Log.e("Is", "Off " + note.getIdNote());
        Log.e("Is", "Off " + a);
    }
    //abdalla 3_17 start
    public void ChangeRoundState(Trip trip) {
        SQLiteDatabase sqLiteDatabase = sql.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQL.ROUNDTRIP, trip.getRoundTrip());
        String Name[] = {trip.getTripId() + ""};
        int a = sqLiteDatabase.update(SQL.TABLE_NAME_TRIP, contentValues, SQL.ID_Trip + " =? ", Name);
        Log.e("ChangeRoundState", "RoundState " + trip.getRoundTrip());
        Log.e("ChangeRoundState", "ROUND " + a);
    }
    //abdalla 3_17
    public void ChangeState(Trip trip) {
        SQLiteDatabase sqLiteDatabase = sql.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQL.STATUS, trip.getStatus());
        String Name[] = {trip.getTripId() + ""};
        int a = sqLiteDatabase.update(SQL.TABLE_NAME_TRIP, contentValues, SQL.ID_Trip + " =? ", Name);
        Log.e("Is", "ChangeState " + trip.getTripId());
        Log.e("Is", "ChangeState " + a);
    }



    public ArrayList<Trip> ViewAllTrips() {
        ArrayList<Trip> trips = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = sql.getWritableDatabase();
        String[] columns = {SQL.ID_Trip, SQL.START_POINT_LATITUDE, SQL.START_POINT_LONGITUDE,
                SQL.END_POINT_LATITUDE, SQL.END_POINT_LONGITUDE, SQL.START_POINT_NAME, SQL.END_POINT_NAME
                , SQL.START_DATE, SQL.END_DATE, SQL.STATUS, SQL.DISTANCE, SQL.TRIP_NAME, SQL.IMAGE_LINK, SQL.ROUNDTRIP};

        Cursor cursor = sqLiteDatabase.query(SQL.TABLE_NAME_TRIP, columns, null, null, null, null, null);
        Trip trip;
        while (cursor.moveToNext()) {
            try {
                trip = new Trip();
                trip.setTripId(cursor.getInt(0));
                trip.setStartPointLatitude(cursor.getDouble(1));
                trip.setStartPointLongitude(cursor.getDouble(2));
                trip.setEndPointLatitude(cursor.getDouble(3));
                trip.setEndPointLongitude(cursor.getDouble(4));
                trip.setStartPointName(cursor.getString(5));
                trip.setEndPointName(cursor.getString(6));
                trip.setStartDate(new Date(cursor.getString(7)));
                trip.setEndDate(new Date(cursor.getString(8)));
                trip.setStatus(cursor.getString(9));
                trip.setDistance(cursor.getDouble(10));
                trip.setTripName(cursor.getString(11));
                trip.setImageLink(cursor.getString(12));
                trips.add(trip);
            } catch (Exception e) {
                Log.i("Error", e.getMessage().toString());
            }
        }
        return trips;
    }
// hesham 3_16
public ArrayList<Note> ViewAllNotes() {
    ArrayList<Note> notes = new ArrayList<>();
    SQLiteDatabase sqLiteDatabase = sql.getWritableDatabase();
    String[] columns = {SQL.ID_NOTE, SQL.TRIP_ID, SQL.TEXT_NOTE,
            SQL.TEXT_STATUS, SQL.CHECKED};
    Cursor cursor = sqLiteDatabase.query(SQL.TABLE_NAME_NOTE, columns, null, null, null, null, null);
    Note note;
    while (cursor.moveToNext()) {
        try {
            note = new Note();
            note.setIdNote(cursor.getInt(0));
            note.setTripId(cursor.getInt(1));
            note.setTextNote(cursor.getString(2));
            note.setStatus(cursor.getString(3));
            note.setChecked(cursor.getInt(4));
            notes.add(note);
        } catch (Exception e) {
            Log.i("Error", e.getMessage().toString());
        }
    }
    return notes;
}
// end hesham 3_16
    public ArrayList<Trip> ViewAllNotes(Note note) {
        return null;
    }

    class SQL extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "MAINDB";
        private static final int DATABASE_VERSION = 1;
        private static final String TABLE_NAME_TRIP = "Trip";
        private static final String ID_Trip = "_id";
        private static final String TRIP_NAME = "TripName";
        private static final String START_POINT_LATITUDE = "StartPointLatitude";
        private static final String START_POINT_LONGITUDE = "StartPointLongitude";
        private static final String END_POINT_LATITUDE = "EndPointLatitude";
        private static final String END_POINT_LONGITUDE = "EndPointLongitude";
        private static final String START_POINT_NAME = "StartPointName";
        private static final String END_POINT_NAME = "EndPointName";
        private static final String START_DATE = "StartDate";
        private static final String END_DATE = "EndDate";
        private static final String STATUS = "Status";
        private static final String DISTANCE = "Distance";
        private static final String IMAGE_LINK = "ImageLink";
        private static final String ROUNDTRIP = "RoundTrip";
        private static final String CREATETABLETRIP = "CREATE TABLE " + TABLE_NAME_TRIP + " (" + ID_Trip + " INTEGER PRIMARY KEY,"
                + TRIP_NAME + " TEXT,"
                + START_POINT_NAME + " TEXT," + END_POINT_NAME + " TEXT,"
                + START_POINT_LATITUDE + " REAL," + START_POINT_LONGITUDE + " REAL,"
                + END_POINT_LATITUDE + " REAL," + END_POINT_LONGITUDE + " REAL,"
                + START_DATE + " TEXT," + END_DATE + " TEXT,"
                + STATUS + " TEXT," + DISTANCE + " INTEGER, " +IMAGE_LINK + " TEXT,"+ ROUNDTRIP + " TEXT)";
        private static final String DROPTABLETRIP = "DROP TABLE IF EXISTS " + TABLE_NAME_TRIP;


        private static final String TABLE_NAME_NOTE = "Note";
        private static final String ID_NOTE = "_id";
        private static final String TRIP_ID = "TripID";
        private static final String TEXT_NOTE = "TextNote";
        private static final String TEXT_STATUS = "Status";
        private static final String CHECKED = "Checked";
        private static final String CREATETABLENOTE = "CREATE TABLE " + TABLE_NAME_NOTE +
                " (" +
                ID_NOTE + " INTEGER PRIMARY KEY, " +
                TRIP_ID + " INTEGER, " + TEXT_NOTE + " TEXT, " +
                TEXT_STATUS + " TEXT, " + CHECKED + " INTEGER);";


        private static final String DROPTABLENOTE = "DROP TABLE IF EXISTS " + TABLE_NAME_NOTE;
        private Context context;

        public SQL(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try {
                Log.e("DosNext", "Table Created");
                sqLiteDatabase.execSQL(CREATETABLETRIP);
                sqLiteDatabase.execSQL(CREATETABLENOTE);
                Log.e("DosNext", " Fully Table Created");
            } catch (Exception e) {
                Log.e("DosNext", e.getMessage().toString());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROPTABLETRIP);
            sqLiteDatabase.execSQL(DROPTABLENOTE);
            // onCreate(sqLiteDatabase);
        }
    }
}