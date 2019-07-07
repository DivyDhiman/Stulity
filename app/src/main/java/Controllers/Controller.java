package Controllers;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.StudentFaclity.Stulity.R;
import com.victor.loading.book.BookLoading;
import com.victor.loading.newton.NewtonCradleLoading;

import Functionality_Class.Alert_dialog;
import Functionality_Class.Animationall;
import Functionality_Class.AsyncTaskClass;
import Functionality_Class.Check_all;
import Functionality_Class.CustomDialog;
import Functionality_Class.SharedPreferenceFileAll;
import Functionality_Class.Toast_all;

/**
 * Created by Abhay dhiman
 */

//Controller centralized call calling in whole application extended Application
public class Controller extends Application {

    private Check_all checkAll = new Check_all();
    private Animationall animationall = new Animationall();
    private ProgressDialog pDialog;
    private Toast_all toast_all = new Toast_all();
    private CustomDialog customDialog = new CustomDialog();
    private SharedPreferenceFileAll sharedPreferenceFileAll;
    private Alert_dialog alert_dialog = new Alert_dialog();


    //Check if Edit Text has empty value or not by calling class CheckAll method Empty_CHeck from controller class
    public boolean Check_all_empty(EditText editText) {
        return checkAll.EmptyCheck_edittext(editText);
    }


    //Check if Edit Text has email formate like "@ etc value or not by calling class CheckAll method Email_edittext from controller class
    public boolean Check_all_email(EditText editText) {
        return checkAll.Email_edittext(editText);
    }

    //Check if Edit Text password nad confirm password value are matches or not by calling class CheckAll method Check_all_confirmpassword from controller class
    public boolean Check_all_matches(EditText editText, String match) {
        return checkAll.Matches_edittext(editText, match);
    }

    //Check if Edit Text password nad confirm password value are matches or not by calling class CheckAll method Check_all_confirmpassword from controller class
    public boolean Matches_textview(String textView, String match) {
        return checkAll.Matches_textview(textView, match);
    }

    public boolean Password_length(EditText editText, int start, int end) {
        return checkAll.Password_length(editText, start, end);
    }

    public void Animation_forward(Context context) {
        animationall.Animallforward(context);
    }

    public void Animation_backward(Context context) {
        animationall.Animallbackward(context);
    }

    public void Animation_up(Context context) {
        animationall.Animallslide_up(context);
    }

    public void Animation_down(Context context) {
        animationall.Animallslide_down(context);
    }

    //Check Internet connections
    public boolean InternetCheck(Context context) {
        return checkAll.isNetWorkStatusAvialable(context);
    }

    public void Toast_show(Context context, String message) {
        toast_all.Toast_show(context, message);
    }

    //Start Progress Dialog
    public void Pd_start(Context context) {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage(context.getString(R.string.Pleasewait));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    //Stop Progress Dialog
    public void Pd_stop() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

    public void customDialog(Object... args) {
        customDialog.DialogShow(args);
    }

    public void customDialogDisable() {
        customDialog.DisableDialog();
    }

    public void LoadingStart(NewtonCradleLoading newtonCradleLoading) {
        newtonCradleLoading.setVisibility(View.VISIBLE);
        newtonCradleLoading.start();
    }

    public void LoadingStop(NewtonCradleLoading newtonCradleLoading) {
        newtonCradleLoading.setVisibility(View.GONE);
        newtonCradleLoading.stop();
    }

    public void LoadingStartDailog(BookLoading bookLoading) {
        bookLoading.setVisibility(View.VISIBLE);
        bookLoading.start();
    }

    public void LoadingStopDailog(BookLoading bookLoading) {
        bookLoading.setVisibility(View.GONE);
        bookLoading.stop();
    }

    public void centralizedApiHitting(Object... args) {
        new AsyncTaskClass(args).execute();
    }


    public void initSharedPreference(Context context) {
        sharedPreferenceFileAll = new SharedPreferenceFileAll(context);
    }

    public void saveString(String key, String value) {
        sharedPreferenceFileAll.setString(key, value);
    }

    public String getString(String key) {
        return sharedPreferenceFileAll.getString(key);
    }

    public void removeSharedPreferences(String key) {
        sharedPreferenceFileAll.removeSharedPreferences(key);
    }

    public void alertDialogShow(Object... args) {
        alert_dialog.alertDialogShow(args);
    }

}