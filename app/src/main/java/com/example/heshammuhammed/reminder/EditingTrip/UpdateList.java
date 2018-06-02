package com.example.heshammuhammed.reminder.EditingTrip;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.heshammuhammed.reminder.AddingTrip.DownloadingIntent;
import com.example.heshammuhammed.reminder.R;

import java.util.ArrayList;

/**
 * Created by abdalla on 3/18/2018.
 */

public class UpdateList extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    ArrayList<String> arrayList = new ArrayList<>();
    ListView listView;
    ArrayAdapter arrayAdapter;
    EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_list);
        floatingActionButton = findViewById(R.id.addnewtext);
        listView = findViewById(R.id.notelist);
        // Hamada
        Intent intent = getIntent();
        Log.e("Array","I Am Here 0");
        if (intent.getSerializableExtra("myarraylist")!= null){
            Log.e("Array","I Am Here 1");
            Log.e("Array",((ArrayList<String>) intent.getSerializableExtra("myarraylist")).size()+" it is size");
            arrayList = (ArrayList<String>) intent.getSerializableExtra("myarraylist");
        }
        Log.e("Array","I Am Here 2");
        // Hamada
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(UpdateList.this);
                View v = layoutInflater.inflate(R.layout.edittext_dialog, null, false);
                editText = v.findViewById(R.id.myedittext);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateList.this, R.style.myDialog);
                alertDialog.setTitle("Enter Your Note");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        arrayList.add(editText.getText().toString());
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
            //            Toast.makeText(UpdateList.this, "No " + editText.getText(), Toast.LENGTH_LONG).show();
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
              //  Toast.makeText(UpdateList.this, arrayList.get(i), Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateList.this, R.style.myDialog);
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
                LayoutInflater layoutInflater = LayoutInflater.from(UpdateList.this);
                View v = layoutInflater.inflate(R.layout.edittext_dialog, null, false);
                editText = v.findViewById(R.id.myedittext);
                editText.setText(arrayList.get(postion));
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateList.this, R.style.myDialog);
                builder.setTitle("Do You Want To Edit This Note");
                builder.setView(v);
                builder.setCancelable(false);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        arrayList.set(postion,editText.getText().toString());
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
        Intent intent = new Intent();
        intent.putExtra("Array",arrayList);
        setResult(1,intent);
        finish();
    }
}
