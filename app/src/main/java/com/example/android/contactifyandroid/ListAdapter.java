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
    private List<SMSData> smsList;

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

        Messages messages = new Messages();
        smsList = messages.populateInbox(smsList, senderNumber, senderMessage);

        Button sendText = (Button) rowView.findViewById(R.id.sendButton);
        EditText inputNumber = (EditText) rowView.findViewById(R.id.enterNumber);
        EditText inputMessage = (EditText) rowView.findViewById(R.id.enterMessage);

        sendText(sendText, inputNumber, inputMessage);

        return rowView;
    }

    public void sendText(Button sendText, EditText inputNumber, EditText inputMessage) {

        final EditText inputNumber_local = inputNumber;
        final EditText inputMessage_local = inputMessage;

        sendText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(inputNumber_local.getText() != null && inputMessage_local.getText() != null) {
                    String outboundNumber = inputNumber_local.getText().toString();
                    String outboundMessage = inputMessage_local.getText().toString();
                    Toast.makeText(getContext(), "Sending your message...", Toast.LENGTH_SHORT).show();
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(outboundNumber, null, outboundMessage, null, null);

                    //clear the text fields
                    inputNumber_local.setHint("Enter recipient's number");
                    inputMessage_local.setHint("Enter your message here...");
                }
                else {
                    Toast.makeText(getContext(), "Please complete necessary fields", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
