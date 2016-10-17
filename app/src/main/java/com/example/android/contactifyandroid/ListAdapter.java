package com.example.android.contactifyandroid;

/**
 * Created by sidgupta on 12/10/2016.
 */

import android.telephony.SmsManager;
import android.widget.ArrayAdapter;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ListAdapter extends ArrayAdapter<SMSData> {

    // List context
    private final Context context;
    // List values
    private final List<SMSData> smsList;

    public ListAdapter(Context context, List<SMSData> smsList) {
        super(context, R.layout.activity_messages, smsList);
        this.context = context;
        this.smsList = smsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.activity_messages, parent, false);

        TextView senderNumber = (TextView) rowView.findViewById(R.id.smsNumberText);
        //senderNumber.setText(smsList.get(position).getBody());

        //initialize fields
        TextView senderMessage = (TextView) rowView.findViewById(R.id.smsBodyText);
        senderNumber.setText("");
        senderMessage.setText("");

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

        Button sendText = (Button) rowView.findViewById(R.id.sendButton);
        final EditText inputNumber = (EditText) rowView.findViewById(R.id.enterNumber);
        final EditText inputMessage = (EditText) rowView.findViewById(R.id.enterMessage);

        sendText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(inputNumber.getText() != null && inputMessage.getText() != null) {
                    String outboundNumber = inputNumber.getText().toString();
                    String outboundMessage = inputMessage.getText().toString();
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(outboundNumber, null, outboundMessage, null, null);
                }
                else {
                    Toast.makeText(getContext(), "Please complete necessary fields", Toast.LENGTH_LONG).show();
                }
            }
        });

        return rowView;
    }
}
