package com.example.android.contactifyandroid;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.SmsManager;

public class Messages extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        //This code is an alternate way of reading SMS messages
        //messages are written to the verbose log for confirmation of correct functionality

        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                String msgData = "";
                for(int idx=0;idx<cursor.getColumnCount();idx++)
                {
                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
                }
                // use msgData
                Log.v("TAG", msgData); //for testing purposes
            } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
        }
        */

        List<SMSData> smsList = new ArrayList<SMSData>();

        //PERMISSIONS
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_SMS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                final int MY_PERMISSIONS_REQUEST_READ_SMS = 0;
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_SMS},
                        MY_PERMISSIONS_REQUEST_READ_SMS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


        Uri uri = Uri.parse("content://sms/inbox");
        Cursor c= getContentResolver().query(uri, null, null ,null,null);
        startManagingCursor(c);

        // Read the sms data and store it in the list
        if(c.moveToFirst()) {
            for(int i=0; i < c.getCount(); i++) {
                SMSData sms = new SMSData();
                sms.setBody(c.getString(c.getColumnIndexOrThrow("body")).toString());
                sms.setNumber(c.getString(c.getColumnIndexOrThrow("address")).toString());
                smsList.add(sms);
                c.moveToNext();
            }
        }
        c.close();

        // Set smsList in the ListAdapter
        setListAdapter(new ListAdapter(this, smsList));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        SMSData sms = (SMSData)getListAdapter().getItem(position);

        Toast.makeText(getApplicationContext(), sms.getBody(), Toast.LENGTH_LONG).show();
    }

    public List<SMSData> populateInbox(List<SMSData> smsList, TextView senderNumber, TextView senderMessage) {

        //read each inbox message and print to TextView
        for(int i = 0; i < smsList.size(); i++) {
            String currentNumberList, currentBodyList, addNumber, addBody, newNumberList, newBodyList;
            SMSData catcher;

            //Retrieve current list of numbers from TextView...
            if(senderNumber.getText() != null) {
                currentNumberList = (senderNumber.getText()).toString();
                currentNumberList += "\n\n";
            }
            else {
                currentNumberList = "";
            }

            //Retrieve current list of messages from TextView...
            if(senderMessage.getText() != null) {
                currentBodyList = (senderMessage.getText()).toString();
                currentBodyList += "\n\n";
            }
            else {
                currentBodyList = "";
            }

            //retrieve a single message from the list of stored inbox messages
            catcher = smsList.get(i);

            //parse message
            addNumber = catcher.getNumber();
            addBody = catcher.getBody();

            //append new message (number and text) to string containing printed messages
            newNumberList = currentNumberList + addNumber;
            newBodyList = currentBodyList + addBody;

            //refresh printed list
            senderNumber.setText(newNumberList);
            senderMessage.setText(newBodyList);
        }
        return smsList;
    }
}
