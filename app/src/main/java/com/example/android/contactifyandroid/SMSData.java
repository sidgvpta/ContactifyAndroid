package com.example.android.contactifyandroid;

/**
 * Created by sidgupta on 12/10/2016.
 */
public class SMSData {
    // Number from which the sms was sent
    private String number;
    // SMS text body
    private String body;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
