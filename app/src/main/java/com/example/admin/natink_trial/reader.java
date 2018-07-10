package com.example.admin.natink_trial;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;


public class reader extends AppCompatActivity {
    ListView listView ;
    ArrayList<String> StoreContacts, selectcontacts ;
    ArrayAdapter<String> arrayAdapter ;
    Cursor cursor ;
    String name, phonenumber ;
    public  static final int RequestPermissionCode  = 1 ;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        listView = (ListView)findViewById(R.id.listview1);

        StoreContacts = new ArrayList<String>();

        EnableRuntimePermission();

        searchView=(SearchView) findViewById(R.id.searcher);



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                arrayAdapter.getFilter().filter(newText);
                listView.setAdapter(arrayAdapter);
                return false;
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(reader.this,emergencylist.class);

                    intent.putExtra("mylist", selectcontacts);

                startActivity(intent);

                finish();
            }
        });

    }
    public void GetContactsIntoArrayList(){

        cursor =  getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        while (cursor.moveToNext()) {

            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            StoreContacts.add("Name: "+name + "\n"+ "Phone: " + phonenumber);
        }

        cursor.close();

    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                reader.this,
                Manifest.permission.READ_CONTACTS))
        {

            Toast.makeText(reader.this,"CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();
            conti();
        } else {

            ActivityCompat.requestPermissions(reader.this,new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(reader.this,"Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();
                    conti();
                } else {

                    Toast.makeText(reader.this,"Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


    public void read_cont(View view) {
        EnableRuntimePermission();
    }
    public void conti(){
        GetContactsIntoArrayList();

        arrayAdapter = new ArrayAdapter<String>(
                reader.this,
                R.layout.contacts_list_view,
                R.id.text1, StoreContacts
        );

        listView.setAdapter(arrayAdapter);
        selectcontacts = new ArrayList<String>();
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(reader.this,"onclick", Toast.LENGTH_LONG).show();

            }


        });*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                /*String string=arrayAdapter.getItem(i);*/

                //selectcontacts.add(contacts.get(i));
                AlertDialog.Builder builder = new AlertDialog.Builder(reader.this);
                builder.setCancelable(false);
                builder.setTitle("     Confirm to Add");

                builder.setIcon(R.drawable.ic_contacts_black_24dp);
                builder.setMessage("Are you sure you want to Add this number as Emergency contact number.");
                builder.setPositiveButton("yes.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String string=arrayAdapter.getItem(i);
                        Toast.makeText(getApplicationContext(),"Item Clicked: "+ string,Toast.LENGTH_SHORT).show();
                        selectcontacts.add(string);
                        //    selectcontacts.add(o);
                        //   Toast.makeText(getApplicationContext(),selectcontacts
                        //       +" "+contacts.get(i)+"  "+i + " Yes was Clicked",Toast.LENGTH_SHORT).show();
                        // Toast.makeText(getApplicationContext(),o
                        //       +" "+listView.getItemAtPosition(i)+"  "+i + " Yes was Clicked",Toast.LENGTH_SHORT).show();
                        //finish();
                    }
                })
                        .setNegativeButton("No.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //    Toast.makeText(getApplicationContext(),"No was Clicked",Toast.LENGTH_SHORT).show();
                                //      finish();
                                dialog.cancel();
                            }
                        });

                // Create the AlertDialog object and return it
                builder.create().show();
                // Toast.makeText(getApplicationContext(),"Item Clicked: "+i,Toast.LENGTH_SHORT).show();
            }
        });

    }
}
