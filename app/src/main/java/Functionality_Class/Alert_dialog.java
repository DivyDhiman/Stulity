package Functionality_Class;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.StudentFaclity.Stulity.R;

import Call_Back.AlertDialogCallBack;

public class Alert_dialog {

    private AlertDialog.Builder alertDialog;
    private Context context;
    private String title,message,positiveButtonName,negativeButtonName;
    private AlertDialogCallBack alertDialogCallBack;

    public void alertDialogShow(Object...args) {
        context = (Context) args[0];
        title = (String) args[1];
        message = (String) args[2];
        positiveButtonName = (String) args[3];
        negativeButtonName = (String) args[4];
        this.alertDialogCallBack = (AlertDialogCallBack) args[5];

        alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(positiveButtonName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                alertDialogCallBack.dialogCallback(context.getString(R.string.positive_click),dialog);
            }
        });

        alertDialog.setNegativeButton(negativeButtonName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                alertDialogCallBack.dialogCallback(context.getString(R.string.negative_click),dialog);
            }
        });
        alertDialog.show();
    }

}
