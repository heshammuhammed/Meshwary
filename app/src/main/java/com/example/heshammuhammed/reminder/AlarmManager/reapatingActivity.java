package com.example.heshammuhammed.reminder.AlarmManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.heshammuhammed.reminder.AddingTrip.AddTrip;
import com.example.heshammuhammed.reminder.R;

import java.util.ArrayList;
import java.util.List;

public class reapatingActivity extends Activity  {
   Button save;
    RadioGroup rGroup;
    Spinner spinner;
    String radio;
    int number=0;
    EditText textnum;
    String item;
    RadioButton checkedRadioButton;
    SharedPreferences sharedd;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reapating);

        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
      //spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Saturday");
        categories.add("Sunday");
        categories.add("Monday");
        categories.add("Tuesday");
        categories.add("Wednesday");
        categories.add("Thursday");
        categories.add("Friday");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        item=spinner.getSelectedItem().toString();
        textnum=findViewById(R.id.editnum);
        sharedd = getSharedPreferences("mypreff", 0);
        editor = sharedd.edit();
        rGroup = (RadioGroup)findViewById(R.id.group);
        checkedRadioButton = (RadioButton)rGroup.findViewById(rGroup.getCheckedRadioButtonId());
        save=findViewById(R.id.save);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height= dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.54));
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(i);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked)
                {
                    radio=checkedRadioButton.getText().toString();
                    Log.i("hoda", "" +radio);
                    if((radio.equals("Every(Days)"))||(radio.equals("Every(Weeks)")))
                    {
                        textnum.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.INVISIBLE);
                    }
                    else if (radio.equals("Select Day Of Week"))
                    {
                        spinner.setVisibility(View.VISIBLE);
                        textnum.setVisibility(View.INVISIBLE);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                //      item = adapterView.getItemAtPosition(i).toString();
                                item=spinner.getSelectedItem().toString();
                                Log.i("hoda", ""+item);
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        Log.i("hoda", ""+"hii");
                    }
                    else
                    {
                        textnum.setVisibility(View.INVISIBLE);
                        spinner.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.print("hiii");
                number=Integer.parseInt(textnum.getText().toString());
                editor.putString("test",radio);
                editor.putInt("number",number);
                editor.putString("item",item);
                editor.commit();
                finish();
            }
        });
    }
}
