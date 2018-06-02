package com.example.heshammuhammed.reminder.FragmentProfile;



import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.heshammuhammed.reminder.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class ProfileFragment extends Fragment {
    TextView editName;
    TextView editPhone;
    TextView editEmail;
    ImageView fragmentImage;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FileInputStream fileInputStream;
    Bitmap bitmap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        editName = view.findViewById(R.id.profilename);
        editPhone = view.findViewById(R.id.profilephone);
        editEmail = view.findViewById(R.id.profileemail);
         fragmentImage = view.findViewById(R.id.fragmentImage);
        sharedPreferences =  getActivity().getSharedPreferences("mypre", 0);
        try {
            fileInputStream = getActivity().openFileInput("Image");
            bitmap = BitmapFactory.decodeStream(fileInputStream);
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(null,bitmap);
            roundedBitmapDrawable.setCircular(true);
            fragmentImage.setImageDrawable(roundedBitmapDrawable);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        editor = sharedPreferences.edit();
        editName.setText(sharedPreferences.getString("name", "Not Exists"));
        editPhone.setText(sharedPreferences.getString("ph", "Not Exists"));
        editEmail.setText(sharedPreferences.getString("mail", "Not Exists"));

        return view;
    }
}