package com.example.heshammuhammed.reminder.MainHome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heshammuhammed.reminder.AddingTrip.AddTrip;
import com.example.heshammuhammed.reminder.DTO.Trip;
import com.example.heshammuhammed.reminder.Database.Tripbase;
 import com.example.heshammuhammed.reminder.FragmentHistory.HistoryFragment;
import com.example.heshammuhammed.reminder.FragmentProfile.ProfileFragment;
import com.example.heshammuhammed.reminder.FragmentUpcomingTrips.UpcomingFragment;
import com.example.heshammuhammed.reminder.HomePage;
import com.example.heshammuhammed.reminder.R;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Bitmap bitmap;
    FileInputStream fileInputStream;
    ImageView imageView;
    SharedPreferences share;
    SharedPreferences.Editor editor;
    String check;
    TextView emailText;
    TextView nameText;
    String email;
    String name;
//abdalla add trip
    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authListener;
//end abdalla add trip


    JSONObject obj,profile_pic_data,profile_pic_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//abdalla 3_14
        Intent intent = getIntent();
        String jsondata = intent.getStringExtra("userProfile");
//end abdalla 3_14

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //abdalla add trip

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {

                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, HomePage.class));
                    finish();
                }
            }
        };

//end abdalla add trip
        share = getSharedPreferences("mypre", 0);

        editor = share.edit();
        check = share.getString("form", "no");
        email = share.getString("mail", "Not Exists");
        name = share.getString("name", "Not Exists");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        emailText = view.findViewById(R.id.emailtextview);
        nameText = view.findViewById(R.id.nametextview);
        imageView = view.findViewById(R.id.imageView);
        //abdalla start 3/14
        if (share .getString("logged", "").toString().equals("logged")) {
            nameText.setText(share.getString("name","NA"));
            emailText.setText(share.getString("mail","NA"));
            String image = share.getString("image"," ");
            if( !image.equalsIgnoreCase("") ){
                byte[] b = Base64.decode(image, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                imageView.setImageBitmap(bitmap);
            }

        } else if(jsondata!=null){
            try {

                obj = new JSONObject(jsondata);
                nameText.setText(obj.get("name").toString());
                emailText.setText(obj.get("email").toString());
                // gender.setText(obj.get("gender").toString());
               profile_pic_data = new JSONObject(obj.getString("picture").toString());
                profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
                Picasso.with(this).load(profile_pic_url.getString("url")).into(imageView);

            } catch (JSONException e) {
               e.printStackTrace();
            }


            }else if(!share .getString("userProfile", "").toString().equals("logout")){
                try{
            obj = new JSONObject(share .getString("userProfile", "").toString());
            nameText.setText(obj.get("name").toString());
            emailText.setText(obj.get("email").toString());
            // gender.setText(obj.get("gender").toString());
            profile_pic_data = new JSONObject(obj.getString("picture").toString());
            profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
            Picasso.with(this).load(profile_pic_url.getString("url")).into(imageView);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        }



        //abdalla  3/14

        try {
            fileInputStream = openFileInput("Image");
            bitmap = BitmapFactory.decodeStream(fileInputStream);
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(null,bitmap);
            roundedBitmapDrawable.setCircular(true);
             imageView.setImageDrawable(roundedBitmapDrawable);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        UpcomingFragment blankFragment = new UpcomingFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.contentmain, blankFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {


        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navig1ation view item clicks here.
        int id = item.getItemId();

            if (id == R.id.triphistory) {
         //   Toast.makeText(getApplicationContext(), "History", Toast.LENGTH_LONG).show();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contentmain, new HistoryFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.logout) {
                FirebaseAuth.getInstance().signOut();
                FacebookSdk.sdkInitialize(getApplicationContext());
                LoginManager.getInstance().logOut();
            Intent intent = new Intent(this, HomePage.class);
            intent.putExtra("log", "out");
                editor.putString("userProfile", "logout");
            Log.i("tst1 ", "logout");
            check = "ss";
            editor.remove("logged");
            editor.commit();
            finish();
            startActivity(intent);
        } else if (id == R.id.addtrip) {
           //     abdalla 3/12
                Trip myNewTrip = new Trip();
               //  myRef.setValue(myNewTrip);
               Tripbase tripbase = new Tripbase(this);
                myNewTrip.setStartDate(new Date());
                myNewTrip.setEndDate(new Date());
               int i = (int) tripbase.CreateTrip(myNewTrip);
                myNewTrip.setTripId(i);
                Log.e("HamadaTrip","Is Trip Created "+i+"");
                Intent intent = new Intent(this, AddTrip.class);
                intent.putExtra("myNewTrip",myNewTrip);
               startActivity(intent);
        }
        else if (id == R.id.home){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contentmain, new UpcomingFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if (id == R.id.userprofile&&share .getString("logged", "").toString().equals("logged")){

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contentmain, new ProfileFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

}
