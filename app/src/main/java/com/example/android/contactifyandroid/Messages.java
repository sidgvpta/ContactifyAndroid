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

public class Messages extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        List<SMSData> smsList = new ArrayList<SMSData>();

        //PERMISSIONS
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_SMS)) {

                // Show an explanation to the user *asynchronously* -- don't block
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

        //initialize fields
        TextView senderNumber = (TextView) findViewById(R.id.smsNumberText);
        TextView senderMessage = (TextView) findViewById(R.id.smsBodyText);
        senderNumber.setText("");
        senderMessage.setText("");

        Messages messages = new Messages();
        smsList = messages.populateInbox(smsList, senderNumber, senderMessage);

        Button sendText = (Button) findViewById(R.id.sendButton);
        EditText inputNumber = (EditText) findViewById(R.id.enterNumber);
        EditText inputMessage = (EditText) findViewById(R.id.enterMessage);

        sendText(sendText, inputNumber, inputMessage);
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

    public void sendText(Button sendText, EditText inputNumber, EditText inputMessage) {

        final EditText inputNumber_local = inputNumber;
        final EditText inputMessage_local = inputMessage;

        //PERMISSIONS
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                final int MY_PERMISSIONS_REQUEST_READ_SMS = 0;
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_READ_SMS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        sendText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(inputNumber_local.getText() != null && inputMessage_local.getText() != null) {
                    String outboundNumber = inputNumber_local.getText().toString();
                    String outboundMessage = inputMessage_local.getText().toString();
                    Toast.makeText(Messages.this, "Sending your message...", Toast.LENGTH_SHORT).show();
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(outboundNumber, null, outboundMessage, null, null);

                    //clear the text fields
                    inputNumber_local.setHint("Enter recipient's number");
                    inputMessage_local.setHint("Enter your message here...");
                }
                else {
                    Toast.makeText(Messages.this, "Please complete necessary fields", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
