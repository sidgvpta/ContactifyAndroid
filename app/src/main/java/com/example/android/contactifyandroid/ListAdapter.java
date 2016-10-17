package com.example.android.contactifyandroid;

/**
 * Created by sidgupta on 12/10/2016.
 */

import android.widget.ArrayAdapter;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        TextView senderMessage = (TextView) rowView.findViewById(R.id.smsBodyText);
        senderNumber.setText("");
        senderMessage.setText("");

        for(int i = 0; i < smsList.size(); i++) {
            String currentNumberList, currentBodyList, addNumber, addBody, newNumberList, newBodyList;
            SMSData catcher;

            if(senderNumber.getText() != null) {
                currentNumberList = (senderNumber.getText()).toString();
                currentNumberList += "\n\n";
            }
            else {
                currentNumberList = "";
            }

            if(senderMessage.getText() != null) {
                currentBodyList = (senderMessage.getText()).toString();
                currentBodyList += "\n\n";
            }
            else {
                currentBodyList = "";
            }

            catcher = smsList.get(i);

            addNumber = catcher.getNumber();
            addBody = catcher.getBody();

            newNumberList = currentNumberList + addNumber;
            newBodyList = currentBodyList + addBody;

            senderNumber.setText(newNumberList);
            senderMessage.setText(newBodyList);
        }

        return rowView;
    }

}
