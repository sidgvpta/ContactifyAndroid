package com.example.android.contactifyandroid;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.SmsManager;
import android.support.v7.widget.CardView;

import org.w3c.dom.Text;

public class Messages extends AppCompatActivity {

    List<SMSData> smsList = new ArrayList<SMSData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        readMessages();
        populateInbox();

        Button sendText = (Button) findViewById(R.id.sendButton);
        EditText inputNumber = (EditText) findViewById(R.id.enterNumber);
        EditText inputMessage = (EditText) findViewById(R.id.enterMessage);
        sendText(sendText, inputNumber, inputMessage);
    }

    public void readMessages(){
        checkPermissions(Manifest.permission.READ_SMS);
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
    }

    public void populateInbox() {

        LinearLayout messageBlock = (LinearLayout) findViewById(R.id.messageBlock);

        //read each inbox message and print to TextView
        for(int i = 0; i < smsList.size(); i++) {
            String addNumber, addBody;
            SMSData catcher;
            TextView newSMSnumber = new TextView(this);
            TextView newSMSbody = new TextView(this);
            LinearLayout cardViewFormat = new LinearLayout(this);
            cardViewFormat.setOrientation(LinearLayout.VERTICAL);
            CardView newSMS = new CardView(this);
            newSMS.setUseCompatPadding(true);
            newSMS.setPreventCornerOverlap(false);

            /*
            CardView.LayoutParams params = new CardView.LayoutParams(
                    CardView.LayoutParams.MATCH_PARENT,
                    CardView.LayoutParams.WRAP_CONTENT
            );

            Resources r = this.getResources();
            int px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    40,
                    r.getDisplayMetrics()
            );

            params.bottomMargin = px;

            newSMS.setLayoutParams(params);
            newSMS.setPadding(px, px, px, px);
            */

            //retrieve a single message from the list of stored inbox messages
            catcher = smsList.get(i);

            //parse message
            addNumber = catcher.getNumber();
            addBody = catcher.getBody();

            newSMSnumber.setText(addNumber);
            newSMSnumber.setTypeface(Typeface.DEFAULT_BOLD);
            newSMSbody.setText(addBody);
            cardViewFormat.addView(newSMSnumber);
            cardViewFormat.addView(newSMSbody);
            newSMS.addView(cardViewFormat);

            messageBlock.addView(newSMS, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    public void sendText(Button sendText, EditText inputNumber, EditText inputMessage) {

        final EditText inputNumber_local = inputNumber;
        final EditText inputMessage_local = inputMessage;

        checkPermissions(Manifest.permission.SEND_SMS);
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

    public void checkPermissions(String permissionType) {

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);

        if (ContextCompat.checkSelfPermission(this,
                permissionType)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    permissionType)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                final int MY_PERMISSIONS_REQUEST_READ_SMS = 0;
                ActivityCompat.requestPermissions(this,
                        new String[]{permissionType},
                        MY_PERMISSIONS_REQUEST_READ_SMS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
}
