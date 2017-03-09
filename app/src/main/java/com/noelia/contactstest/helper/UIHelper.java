package com.noelia.contactstest.helper;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.noelia.contactstest.model.ContactPhone;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by nochita on 07/03/2017.
 */
public class UIHelper {

    private static final String TAG = UIHelper.class.getName();

    public static String formatDateToUserFriendly(String serverDate){
        final String serviceFormat = "yyyy-MM-dd"; // i.e. "2000-01-31",
        final String userFriendlyFormat = "dd MMM yyyy";

        String userFriendlyString = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(serviceFormat);
        Date dateTaken = null;
        try {
            dateTaken = simpleDateFormat.parse(serverDate);
            simpleDateFormat.applyPattern(userFriendlyFormat);
            userFriendlyString = simpleDateFormat.format(dateTaken);
        } catch (ParseException e) {
            Log.e(TAG, "error parsing date " + serverDate);
            e.printStackTrace();
        }

        return userFriendlyString;
    }

    public static void populatePhonesIntoContainer(List<ContactPhone> phoneList, ViewGroup container,
                                                   Context context){
        for(ContactPhone phone : phoneList){
            TextView textView = new TextView(context);
            if(phone.isValid()){
                textView.setText(phone.toString());
                container.addView(textView);
            }
        }
    }
}
