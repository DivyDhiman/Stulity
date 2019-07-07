package Functionality_Class;

import android.content.Context;
import android.widget.Toast;

public class Toast_all {

    public void Toast_show(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}