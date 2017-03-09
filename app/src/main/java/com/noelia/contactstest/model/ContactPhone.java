package com.noelia.contactstest.model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by nochita on 07/03/2017.
 */
public class ContactPhone implements Serializable {

    private String type;
    private String number;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isValid(){
        return !TextUtils.isEmpty(type) && !TextUtils.isEmpty(number);
    }

    @Override
    public String toString() {
        return type + ": " + number;
    }
}
