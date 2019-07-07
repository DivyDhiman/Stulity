package Functionality_Class;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Abhay dhiman
 */

//Checking Functionality class
public class Check_all {

    //Check is Edit Text is empty or not
    public boolean EmptyCheck_edittext(EditText editText) {
        if (editText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    //Check is email id is correct in format or not
    public boolean Email_edittext(EditText editText) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(editText.getText().toString());
        return matcher.matches();
    }

    //Check password and confirm password matched or not
    public boolean Matches_edittext(EditText editText,String match) {
        if(editText.getText().toString().equals(match)){
            return true;
        }else {
            return false;
        }
    }

    //Check password and confirm password matched or not
    public boolean Matches_textview(String editText,String match) {
        if(editText.equals(match)){
            return true;
        }else {
            return false;
        }
    }

    public boolean Password_length(EditText editText,int start, int end){
        if (editText.getText().toString().trim().length() >= start && editText.getText().toString().trim().length() <= end) {
            return true;
        }else {
            return false;
        }
    }
    //Format date
    public String StringDate_dashed(String date_) {
        try {
            SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+SSSS");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleFormatter.parse(date_);
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            return null;
        }

    }

    //Check internet connectivity
    public boolean isNetWorkStatusAvialable(Context applicationContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager)applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null){
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null) {
                if (networkInfo.isConnected())
                    return true;
            }
        }
        return false;
    }
}