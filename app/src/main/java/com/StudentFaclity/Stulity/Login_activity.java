package com.StudentFaclity.Stulity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.victor.loading.newton.NewtonCradleLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import Call_Back.DialogCallBack;
import Call_Back.ParentApiCallback;
import Controllers.Controller;

public class Login_activity extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener {

    private NewtonCradleLoading newton_cradle_loading;
    private ImageView logo;
    private EditText et_username, et_password, dialog_email_id;
    private Button btn_login, btn_register, skipBtn;
    private TextView forgot_password, dialog_password_reset_steps, dialog_ok;
    private Context context;
    private Controller controller;
    private String username, password, forgot_email, anim_check = "first";
    private Intent intent;
    private Dialog forgot_pass_dialog;
    private ImageView send_image;
    private LinearLayout send_image_text_parent, cancel_submit_parent;
    private Animation mAnim = null;
    private ParentApiCallback parentApiCallback;
    private DialogCallBack dialogCallBack;
    private HashMap<String, Object> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        context = Login_activity.this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }

        controller = new Controller();
        controller.initSharedPreference(context);

        callBacks();
    }

    private void callBacks() {
        parentApiCallback = new ParentApiCallback() {
            @Override
            public void Data_call_back(Object... args) {
                String callingUI = (String) args[0];
                String response = (String) args[1];

                if (callingUI.equals(getString(R.string.login_api))) {
                    if (response.equals(getString(R.string.error_Http_not_found))) {
                        controller.Toast_show(context, getString(R.string.unable_to_connect_to_network));
                        EditableTrue();
                        controller.LoadingStop(newton_cradle_loading);
                    } else if (response.equals(getString(R.string.error_Http_internal))) {
                        controller.Toast_show(context, getString(R.string.unable_to_connect_to_network));
                        EditableTrue();
                        controller.LoadingStop(newton_cradle_loading);
                    } else if (response.equals(getString(R.string.error_Http_other))) {
                        controller.Toast_show(context, getString(R.string.facing_some_technical_issue));
                        EditableTrue();
                        controller.LoadingStop(newton_cradle_loading);
                    } else if (response.equals(getString(R.string.error))) {
                        controller.Toast_show(context, getString(R.string.facing_some_technical_issue));
                        EditableTrue();
                        controller.LoadingStop(newton_cradle_loading);
                    } else {
                        getLoginData(response);
                    }
                }
            }
        };

        dialogCallBack = new DialogCallBack() {
            @Override
            public void clickDialogItem(Object... args) {

            }
        };

        initialise();
    }

    private void initialise() {

        skipBtn = (Button) findViewById(R.id.skip_btn);
        newton_cradle_loading = (NewtonCradleLoading) findViewById(R.id.newton_cradle_loading);
        logo = (ImageView) findViewById(R.id.logo);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);

        skipBtn.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        forgot_password.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.skip_btn:
                callIntentLogin();
                break;

            case R.id.btn_login:
                if (controller.Check_all_empty(et_username)) {
                    et_username.setError(getString(R.string.empty_email_phonenumber));
                } else if (controller.Check_all_empty(et_password)) {
                    et_password.setError(getString(R.string.empty_password));
                } else {
                    if (controller.InternetCheck(context)) {
                        username = et_username.getText().toString();
                        password = et_password.getText().toString();

                        data = new HashMap<>();
                        data.put("uid", username);
                        data.put("password", password);

                        EditableFalse();
                        controller.LoadingStart(newton_cradle_loading);
                        controller.centralizedApiHitting(getString(R.string.login_api), getString(R.string.get_type_api), context, parentApiCallback, data);
                        btn_login.setText(getString(R.string.loginin));
                    } else {
                        controller.Toast_show(context, getString(R.string.enable_internet));
                    }
                }

                break;

            case R.id.btn_register:
                intent = new Intent(context, Registartion.class);
                startActivity(intent);
                controller.Animation_forward(context);
                break;

            case R.id.forgot_password:
                controller.customDialog(context, getString(R.string.forgot_password_screen), R.layout.otp_verify, dialogCallBack,data);
//                dialog_forgot_password();
                break;

            case R.id.dialog_ok:
                forgot_pass_dialog.dismiss();
                break;
            case R.id.dialog_cancel:
                forgot_pass_dialog.dismiss();
                break;
            case R.id.dialog_submit:

                if (controller.Check_all_empty(dialog_email_id)) {
                    dialog_email_id.setError(getString(R.string.empty_email));
                } else if (!controller.Check_all_email(dialog_email_id)) {
                    dialog_email_id.setError(getString(R.string.valid_email));
                } else {
                    if (controller.InternetCheck(context)) {
                        forgot_email = dialog_email_id.getText().toString();
                        send_image_text_parent.setVisibility(View.VISIBLE);
                        dialog_ok.setVisibility(View.VISIBLE);
                        cancel_submit_parent.setVisibility(View.GONE);
                        dialog_email_id.setVisibility(View.GONE);
                        dialog_password_reset_steps.setText("");

                        mAnim = AnimationUtils.loadAnimation(context, R.anim.rotate_once);
                        mAnim.setAnimationListener(this);
                        send_image.clearAnimation();
                        send_image.setAnimation(mAnim);

                    } else {
                        controller.Toast_show(context, getString(R.string.enable_internet));
                    }
                }
                break;

        }
    }

    private void callIntentLogin() {
        intent = new Intent(context, Dash_board.class);
        startActivity(intent);
        controller.Animation_forward(context);
    }


    public void EditableFalse() {
        logo.setVisibility(View.GONE);
        newton_cradle_loading.setVisibility(View.VISIBLE);

        et_username.setEnabled(false);

        et_password.setEnabled(false);

        skipBtn.setClickable(false);
        forgot_password.setClickable(false);
        btn_login.setClickable(false);
        btn_register.setClickable(false);

        btn_login.setText(getString(R.string.Pleasewait));
    }

    public void EditableTrue() {
        logo.setVisibility(View.VISIBLE);
        newton_cradle_loading.setVisibility(View.GONE);

        et_username.setEnabled(true);

        et_password.setEnabled(true);

        skipBtn.setClickable(true);
        forgot_password.setClickable(true);
        btn_login.setClickable(true);
        btn_register.setClickable(true);

        btn_login.setText(getString(R.string.login));
    }

    private void getLoginData(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String status = jsonObject.getString("Status");
            String message = jsonObject.getString("Message");
            if(status.equals(getString(R.string.status_ok))){
                String sessionId = jsonObject.getString("Message");
                controller.saveString(getString(R.string.session_id),sessionId);
                callIntentLogin();
            }else {
                controller.Toast_show(context, message);
                EditableTrue();
                controller.LoadingStop(newton_cradle_loading);
            }
        } catch (JSONException e) {
            controller.Toast_show(context, getString(R.string.facing_some_technical_issue));
            EditableTrue();
            controller.LoadingStop(newton_cradle_loading);
            e.printStackTrace();
        }
    }


    private void dialog_forgot_password() {
        forgot_pass_dialog = new Dialog(context);
        forgot_pass_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        forgot_pass_dialog.setContentView(R.layout.custom_dialog_pass);
        forgot_pass_dialog.setCanceledOnTouchOutside(false);
        forgot_pass_dialog.setCancelable(false);
        send_image_text_parent = (LinearLayout) forgot_pass_dialog.findViewById(R.id.send_image_text_parent);
        send_image = (ImageView) forgot_pass_dialog.findViewById(R.id.send_image);
        send_image.setBackgroundResource(R.drawable.process_send);

        dialog_password_reset_steps = (TextView) forgot_pass_dialog.findViewById(R.id.dialog_password_reset_steps);
        cancel_submit_parent = (LinearLayout) forgot_pass_dialog.findViewById(R.id.cancel_submit_parent);
        dialog_ok = (TextView) forgot_pass_dialog.findViewById(R.id.dialog_ok);
        TextView dialog_cancel = (TextView) forgot_pass_dialog.findViewById(R.id.dialog_cancel);
        TextView dialog_submit = (TextView) forgot_pass_dialog.findViewById(R.id.dialog_submit);
        dialog_email_id = (EditText) forgot_pass_dialog.findViewById(R.id.dialog_email_id);
        dialog_ok.setOnClickListener(this);
        dialog_cancel.setOnClickListener(this);
        dialog_submit.setOnClickListener(this);

        send_image_text_parent.setVisibility(View.GONE);
        dialog_ok.setVisibility(View.GONE);

        forgot_pass_dialog.show();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        switch (anim_check) {
            case "first":
                send_image.setBackgroundResource(R.drawable.send_email);
                mAnim = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                mAnim.setAnimationListener(this);
                send_image.clearAnimation();
                send_image.setAnimation(mAnim);
                anim_check = "second";
                break;

            case "second":
                dialog_password_reset_steps.setText(getString(R.string.password_reset_steps));
                mAnim = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                mAnim.setAnimationListener(this);
                dialog_password_reset_steps.clearAnimation();
                dialog_password_reset_steps.setAnimation(mAnim);
                anim_check = "third";
                break;

            case "third":
                anim_check = "first";
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        controller.Animation_backward(context);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}