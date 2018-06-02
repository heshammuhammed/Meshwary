package com.example.heshammuhammed.reminder.MainHome;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.heshammuhammed.reminder.DTO.Trip;
import com.example.heshammuhammed.reminder.R;

public class Trip_Details extends AppCompatActivity {

    Intent intent;
    Trip trip;
    TextView tripName , startPoint , endPoint , startDate , endDate , tripstatus , distance;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_trip__details);
        tripName = findViewById(R.id.tripnameview);
        startPoint = findViewById(R.id.startpointview);
        endPoint = findViewById(R.id.endpointview);
        startDate = findViewById(R.id.startdateview);
        endDate = findViewById(R.id.enddateview);
        tripstatus = findViewById(R.id.tripstatusview);
        distance = findViewById(R.id.tripdisview);
        imageView = findViewById(R.id.myimagedetails);
        String    fullLink="";
          intent = getIntent();
        trip = (Trip) intent.getSerializableExtra("TRIPDETAILS");
     //   Toast.makeText(this,trip.getTripId()+" ",Toast.LENGTH_LONG).show();

        tripName.setText(trip.getTripName());
        tripName.setTypeface(tripName.getTypeface(), Typeface.BOLD_ITALIC);
        startPoint.setText(trip.getStartPointName());
        endPoint.setText(trip.getEndPointName());
        startDate.setText(trip.getStartDate().toString());
        endDate.setText(trip.getEndDate().toString());
        distance.setText(trip.getDistance()+"");
        tripstatus.setText(trip.getStatus());
        if(trip.getStatus().equals("UPCOMING")||trip.getStatus().equals("PROGRESS")||trip.getStatus().equals("CANCELLED"))  {
            endDate.setText("------------");
        }

    }

}
