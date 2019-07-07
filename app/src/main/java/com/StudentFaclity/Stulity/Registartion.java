package com.StudentFaclity.Stulity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.victor.loading.newton.NewtonCradleLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Call_Back.DialogCallBack;
import Call_Back.ParentApiCallback;
import Controllers.Config;
import Controllers.Controller;

public class Registartion extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private Controller controller;
    private Intent intent;
    private NewtonCradleLoading newtonCradleLoading;
    private TextView splash_title, terms_conditions;
    private EditText registration_firstname, registration_lastname, registration_phonenumber, registration_password, registration_confirm_password, registration_email, registration_confirm_email;
    private Button registration_btn;
    private String first_name, last_name, phone_number, email_id, password;
    private ParentApiCallback parentApiCallback;
    private HashMap<String, Object> data;
    private DialogCallBack dialogCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        context = Registartion.this;

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

                if (callingUI.equals(getString(R.string.email_check))) {
                    if (response.equals(getString(R.string.error_Http_not_found))) {
                        controller.Toast_show(context, getString(R.string.unable_to_connect_to_network));
                        EditableTrue();
                        controller.LoadingStop(newtonCradleLoading);
                    } else if (response.equals(getString(R.string.error_Http_internal))) {
                        controller.Toast_show(context, getString(R.string.unable_to_connect_to_network));
                        EditableTrue();
                        controller.LoadingStop(newtonCradleLoading);
                    } else if (response.equals(getString(R.string.error_Http_other))) {
                        controller.Toast_show(context, getString(R.string.facing_some_technical_issue));
                        EditableTrue();
                        controller.LoadingStop(newtonCradleLoading);
                    } else if (response.equals(getString(R.string.error))) {
                        controller.Toast_show(context, getString(R.string.facing_some_technical_issue));
                        EditableTrue();
                        controller.LoadingStop(newtonCradleLoading);
                    } else {
                        if (response.equals("\"\"")) {
                            controller.centralizedApiHitting(getString(R.string.phone_check), getString(R.string.get_type_api), context, parentApiCallback, data);
                            registration_btn.setText(getString(R.string.checking_phone_number));
                        } else {
                            controller.Toast_show(context, response);
                            EditableTrue();
                        }
                    }
                } else if (callingUI.equals(getString(R.string.phone_check))) {
                    if (response.equals(getString(R.string.error_Http_not_found))) {
                        controller.Toast_show(context, getString(R.string.unable_to_connect_to_network));
                        EditableTrue();
                        controller.LoadingStop(newtonCradleLoading);
                    } else if (response.equals(getString(R.string.error_Http_internal))) {
                        controller.Toast_show(context, getString(R.string.unable_to_connect_to_network));
                        EditableTrue();
                        controller.LoadingStop(newtonCradleLoading);
                    } else if (response.equals(getString(R.string.error_Http_other))) {
                        controller.Toast_show(context, getString(R.string.facing_some_technical_issue));
                        EditableTrue();
                        controller.LoadingStop(newtonCradleLoading);
                    } else if (response.equals(getString(R.string.error))) {
                        controller.Toast_show(context, getString(R.string.facing_some_technical_issue));
                        EditableTrue();
                        controller.LoadingStop(newtonCradleLoading);
                    } else {
                        if (response.equals("\"\"")) {
                            controller.centralizedApiHitting(getString(R.string.otp_send), getString(R.string.get_type_api), context, parentApiCallback, data);
                            registration_btn.setText(getString(R.string.sending_otp));
                        } else {
                            controller.Toast_show(context, response);
                            EditableTrue();
                        }
                    }
                } else if (callingUI.equals(getString(R.string.otp_send))) {
                    if (response.equals(getString(R.string.error_Http_not_found))) {
                        controller.Toast_show(context, getString(R.string.unable_to_connect_to_network));
                        EditableTrue();
                        controller.LoadingStop(newtonCradleLoading);
                    } else if (response.equals(getString(R.string.error_Http_internal))) {
                        controller.Toast_show(context, getString(R.string.unable_to_connect_to_network));
                        EditableTrue();
                        controller.LoadingStop(newtonCradleLoading);
                    } else if (response.equals(getString(R.string.error_Http_other))) {
                        controller.Toast_show(context, getString(R.string.facing_some_technical_issue));
                        EditableTrue();
                        controller.LoadingStop(newtonCradleLoading);
                    } else if (response.equals(getString(R.string.error))) {
                        controller.Toast_show(context, getString(R.string.facing_some_technical_issue));
                        EditableTrue();
                        controller.LoadingStop(newtonCradleLoading);
                    } else {
                        if (response.equals("\"\"")) {
                            controller.customDialog(context, getString(R.string.otp_verify_screen), R.layout.otp_verify, dialogCallBack,data);
                            registration_btn.setText(getString(R.string.checking_otp));
                        } else {
                            controller.Toast_show(context, response);
                            EditableTrue();
                        }
                    }
                } else if (callingUI.equals(getString(R.string.register_api))) {
                    if (response.equals(getString(R.string.error_Http_not_found))) {
                        controller.Toast_show(context, getString(R.string.unable_to_connect_to_network));
                        EditableTrue();
                        controller.LoadingStop(newtonCradleLoading);
                    } else if (response.equals(getString(R.string.error_Http_internal))) {
                        controller.Toast_show(context, getString(R.string.unable_to_connect_to_network));
                        EditableTrue();
                        controller.LoadingStop(newtonCradleLoading);
                    } else if (response.equals(getString(R.string.error_Http_other))) {
                        controller.Toast_show(context, getString(R.string.facing_some_technical_issue));
                        EditableTrue();
                        controller.LoadingStop(newtonCradleLoading);
                    } else if (response.equals(getString(R.string.error))) {
                        controller.Toast_show(context, getString(R.string.facing_some_technical_issue));
                        EditableTrue();
                        controller.LoadingStop(newtonCradleLoading);
                    } else {
                        callRegisterMethod(response);
                    }
                }

            }
        };

        dialogCallBack = new DialogCallBack() {
            @Override
            public void clickDialogItem(Object... args) {
                String callingUI = (String) args[0];
                if (callingUI.equals(context.getString(R.string.send))) {
                    controller.centralizedApiHitting(getString(R.string.register_api), getString(R.string.get_type_api), context, parentApiCallback, data);
                    registration_btn.setText(getString(R.string.registering));
                    controller.customDialogDisable();
                } else if (callingUI.equals(context.getString(R.string.cancel))) {
                    EditableTrue();
                    controller.LoadingStop(newtonCradleLoading);
                    controller.customDialogDisable();
                }
            }
        };

        initialise();
    }

    private void callRegisterMethod(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String status = jsonObject.getString("Status");
            if(status.equals(getString(R.string.status_ok))){
                String sessionId = jsonObject.getString("Message");
                controller.saveString(getString(R.string.session_id),sessionId);
                intent_type();
            }else {
                controller.Toast_show(context, getString(R.string.unable_to_connect_to_network));
                EditableTrue();
                controller.LoadingStop(newtonCradleLoading);
            }
        } catch (JSONException e) {
            controller.Toast_show(context, getString(R.string.facing_some_technical_issue));
            EditableTrue();
            controller.LoadingStop(newtonCradleLoading);
            e.printStackTrace();
        }

    }

    private void initialise() {
        splash_title = (TextView) findViewById(R.id.splash_title);
        newtonCradleLoading = (NewtonCradleLoading) findViewById(R.id.newton_cradle_loading);
        registration_firstname = (EditText) findViewById(R.id.registration_firstname);
        registration_lastname = (EditText) findViewById(R.id.registration_lastname);
        registration_phonenumber = (EditText) findViewById(R.id.registration_phonenumber);
        registration_password = (EditText) findViewById(R.id.registration_password);
        registration_confirm_password = (EditText) findViewById(R.id.registration_confirm_password);
        registration_email = (EditText) findViewById(R.id.registration_email);
        registration_confirm_email = (EditText) findViewById(R.id.registration_confirm_email);
        registration_btn = (Button) findViewById(R.id.registration_btn);
        terms_conditions = (TextView) findViewById(R.id.terms_conditions);

        terms_conditions.setOnClickListener(this);

        registration_btn.setOnClickListener(this);
    }

    private void check_all() {
        if (controller.Check_all_empty(registration_firstname)) {
            registration_firstname.setError(getString(R.string.empty_first_name));
        } else if (controller.Check_all_empty(registration_lastname)) {
            registration_lastname.setError(getString(R.string.empty_last_name));
        } else if (controller.Check_all_empty(registration_phonenumber)) {
            registration_phonenumber.setError(getString(R.string.empty_username));
        } else if (controller.Check_all_empty(registration_password)) {
            registration_password.setError(getString(R.string.empty_password));
        } else if (controller.Check_all_empty(registration_confirm_password)) {
            registration_confirm_password.setError(getString(R.string.empty_confirm_password));
        } else if (controller.Check_all_empty(registration_email)) {
            registration_email.setError(getString(R.string.empty_email));
        } else if (controller.Check_all_empty(registration_confirm_email)) {
            registration_confirm_email.setError(getString(R.string.empty_confirm_email));
        } else if (!controller.Check_all_email(registration_email)) {
            registration_email.setError(getString(R.string.valid_email));
        } else if (!controller.Password_length(registration_password, 6, 20)) {
            registration_password.setError(getString(R.string.password_length));
        } else if (!controller.Check_all_matches(registration_confirm_password, registration_password.getText().toString())) {
            registration_password.setError(getString(R.string.password_not_match));
        } else {
            first_name = registration_firstname.getText().toString();
            last_name = registration_lastname.getText().toString();
            phone_number = registration_phonenumber.getText().toString();
            email_id = registration_email.getText().toString();
            password = registration_password.getText().toString();

            data = new HashMap<>();
            data.put("first_name", first_name);
            data.put("last_name", last_name);
            data.put("phone_number", phone_number);
            data.put("email_id", email_id);
            data.put("password", password);
            data.put("terms", "1");

            if (!controller.Check_all_matches(registration_confirm_password, password)) {
                registration_confirm_password.setError(getString(R.string.matches_password));
            } else if (!controller.Check_all_matches(registration_confirm_email, email_id)) {
                registration_email.setError(getString(R.string.matches_email));
            } else {
                if (!controller.InternetCheck(context)) {
                    controller.Toast_show(context, getString(R.string.enable_internet));
                } else {
                    EditableFalse();
                    controller.LoadingStart(newtonCradleLoading);
                    controller.centralizedApiHitting(getString(R.string.email_check), getString(R.string.get_type_api), context, parentApiCallback, data);
                    registration_btn.setText(getString(R.string.checking_email));
                }
            }
        }
    }

    public void EditableFalse() {
        splash_title.setVisibility(View.GONE);
        newtonCradleLoading.setVisibility(View.VISIBLE);

        registration_firstname.setEnabled(false);

        registration_lastname.setEnabled(false);

        registration_phonenumber.setEnabled(false);

        registration_password.setEnabled(false);

        registration_confirm_password.setEnabled(false);

        registration_email.setEnabled(false);

        registration_confirm_email.setEnabled(false);

        registration_btn.setClickable(false);
        terms_conditions.setClickable(false);

        registration_btn.setText(getString(R.string.Pleasewait));

    }

    public void EditableTrue() {
        splash_title.setVisibility(View.VISIBLE);
        newtonCradleLoading.setVisibility(View.GONE);

        registration_firstname.setEnabled(true);

        registration_lastname.setEnabled(true);

        registration_phonenumber.setEnabled(true);

        registration_password.setEnabled(true);

        registration_confirm_password.setEnabled(true);

        registration_email.setEnabled(true);

        registration_confirm_email.setEnabled(true);

        registration_btn.setClickable(true);
        terms_conditions.setClickable(true);

        registration_btn.setText(getString(R.string.register));

    }

    private void intent_type() {
        intent = new Intent(context, Dash_board.class);
        startActivity(intent);
        controller.Animation_forward(context);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        controller.Animation_backward(context);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.registration_btn:
                check_all();
                break;

            case R.id.terms_conditions:

                if (controller.InternetCheck(context)) {
                    intent = new Intent(context, CentralizedWebView.class);
                    intent.putExtra("url", Config.TERMS_OF_SERVICE);
                    startActivity(intent);
                    controller.Animation_up(context);
                } else {
                    controller.Toast_show(context, getString(R.string.enable_internet));
                }
                break;
        }
    }
}