package com.example.heshammuhammed.reminder.FragmentUpcomingTrips;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.heshammuhammed.reminder.Adapter.MyCustomAdapter;
import com.example.heshammuhammed.reminder.DTO.Trip;
import com.example.heshammuhammed.reminder.Database.Tripbase;
import com.example.heshammuhammed.reminder.R;
import java.util.ArrayList;


public class UpcomingFragment extends Fragment {

RecyclerView recyclerView;
MyCustomAdapter myCustomAdapter;
ArrayList<Trip> arrayList;
Tripbase tripbase;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        recyclerView = view.findViewById(R.id.recview);
        tripbase = new Tripbase(getActivity());
        //arrayList = tripbase.ViewAllTrips();
       arrayList = tripbase.SelectUpComingTrips();
     //   Toast.makeText(getActivity(),arrayList.size()+"",Toast.LENGTH_LONG).show();
        myCustomAdapter = new MyCustomAdapter(getActivity(),arrayList);
        recyclerView.setAdapter(myCustomAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }
}
