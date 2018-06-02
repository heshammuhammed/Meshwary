package com.example.heshammuhammed.reminder.AddingTrip;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.example.heshammuhammed.reminder.EditingTrip.EditTrip;
import com.example.heshammuhammed.reminder.R;

import java.util.ArrayList;

public class AddingList extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    ArrayList<String> arrayList = new ArrayList<>();
    ListView listView;
    ArrayAdapter arrayAdapter;
    EditText editText;
    String from;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_list);
        floatingActionButton = findViewById(R.id.addnewtext);
        listView = findViewById(R.id.notelist);
        // Hamada
        Intent intent = getIntent();

        if (from == null) from = "fail";
        Log.e("Array", "I Am Here 0");
        if (intent.getSerializableExtra("myarraylist") != null) {
            Log.e("Array", "I Am Here 1");
            Log.e("Array", ((ArrayList<String>) intent.getSerializableExtra("myarraylist")).size() + " it is size");
            arrayList = (ArrayList<String>) intent.getSerializableExtra("myarraylist");
            from = intent.getStringExtra("InfArraylist");
        }
        Log.e("Array", "I Am Here 2");
        //abdalla 3_18_7_30
        //  awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        //  awesomeValidation.addValidation(AddingList.this, R.id.myedittext, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);

        // Hamada
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(AddingList.this);
                View v = layoutInflater.inflate(R.layout.edittext_dialog, null, false);
                editText = v.findViewById(R.id.myedittext);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddingList.this, R.style.myDialog);
                alertDialog.setTitle("Enter Your Note");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!editText.getText().toString().trim().equals(""))
                            arrayList.add(editText.getText().toString());
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                     //   Toast.makeText(AddingList.this, "No " + editText.getText(), Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog.setView(v);
                alertDialog.show();
                arrayAdapter.notifyDataSetChanged();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int Position = i;
             //   Toast.makeText(AddingList.this, arrayList.get(i), Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(AddingList.this, R.style.myDialog);
                builder.setTitle("Do You Want To Delete This Note");
                builder.setCancelable(false);
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        arrayList.remove(Position);
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int postion = i;
                LayoutInflater layoutInflater = LayoutInflater.from(AddingList.this);
                View v = layoutInflater.inflate(R.layout.edittext_dialog, null, false);
                editText = v.findViewById(R.id.myedittext);
                editText.setText(arrayList.get(postion));
                AlertDialog.Builder builder = new AlertDialog.Builder(AddingList.this, R.style.myDialog);
                builder.setTitle("Do You Want To Edit This Note");
                builder.setView(v);
                builder.setCancelable(false);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!editText.getText().toString().trim().equals(""))
                            arrayList.set(postion, editText.getText().toString());
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
                builder.show();
            }
        });

    }

    @Override
    public void onBackPressed() {

        DownloadingIntent.AAAA(arrayList);
        EditingTrip.AAAA(arrayList);
        Intent intent = new Intent();
        intent.putExtra("Array", arrayList);
        setResult(1, intent);
        finish();
    }
}
