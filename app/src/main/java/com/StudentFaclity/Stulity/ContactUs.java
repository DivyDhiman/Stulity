package com.StudentFaclity.Stulity;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.victor.loading.newton.NewtonCradleLoading;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import Call_Back.ParentApiCallback;
import Controllers.Config;
import Controllers.Controller;

/**
 * Created by Abhay0648 on 21-05-2017.
 */

public class ContactUs extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private Controller controller;
    private ImageView menu_btn, appIcon, image_view;
    private TextView save_edit;
    private EditText name_enter, email_enter, phone_number_enter,message_enter;
    private NewtonCradleLoading newtonCradleLoading;
    private ParentApiCallback parentApiCallback;
    private String sessionId, name, email, phoneNumber, uid,message;
    private HashMap<String, Object> data;
    private JSONArray jsonArray;
    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);

        context = ContactUs.this;
        controller = new Controller();
        controller.initSharedPreference(context);

        sessionId = controller.getString(getString(R.string.session_id));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }

        set_action_bar();
        callBacks();
    }

    private void callBacks() {
        parentApiCallback = new ParentApiCallback() {
            @Override
            public void Data_call_back(Object... args) {
                String callingUI = (String) args[0];
                String response = (String) args[1];

                if (callingUI.equals(getString(R.string.get_contact_details_api))) {
                    if (response.equals(getString(R.string.error_Http_not_found))) {
                        controller.LoadingStop(newtonCradleLoading);
                        controller.Toast_show(context, getString(R.string.unable_to_connect_to_network));
                    } else if (response.equals(getString(R.string.error_Http_internal))) {
                        controller.LoadingStop(newtonCradleLoading);
                        controller.Toast_show(context, getString(R.string.unable_to_connect_to_network));
                    } else if (response.equals(getString(R.string.error_Http_other))) {
                        controller.LoadingStop(newtonCradleLoading);
                        controller.Toast_show(context, getString(R.string.facing_some_technical_issue));
                    } else if (response.equals(getString(R.string.error))) {
                        controller.LoadingStop(newtonCradleLoading);
                        controller.Toast_show(context, getString(R.string.facing_some_technical_issue));
                    } else {
                        try {
                            getData(response, 0);
                        } catch (Exception e) {
                            controller.LoadingStop(newtonCradleLoading);
                            controller.Toast_show(context,getString(R.string.oops_something_went_wrong));
                            e.printStackTrace();
                        }
                    }
                }else  if (callingUI.equals(getString(R.string.send_contact_us_api))) {
                    if (response.equals(getString(R.string.error_Http_not_found))) {
                        controller.Pd_stop();
                        controller.Toast_show(context, getString(R.string.unable_to_connect_to_network));
                    } else if (response.equals(getString(R.string.error_Http_internal))) {
                        controller.Pd_stop();
                        controller.Toast_show(context, getString(R.string.unable_to_connect_to_network));
                    } else if (response.equals(getString(R.string.error_Http_other))) {
                        controller.Pd_stop();
                        controller.Toast_show(context, getString(R.string.facing_some_technical_issue));
                    } else if (response.equals(getString(R.string.error))) {
                        controller.Pd_stop();
                        controller.Toast_show(context, getString(R.string.facing_some_technical_issue));
                    } else {
                        try {
                            getData(response, 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        initialise();
    }

    private void initialise() {
        name_enter = (EditText) findViewById(R.id.name_enter);
        email_enter = (EditText) findViewById(R.id.email_enter);
        phone_number_enter = (EditText) findViewById(R.id.phone_number_enter);
        message_enter = (EditText) findViewById(R.id.message_enter);
        newtonCradleLoading = (NewtonCradleLoading) findViewById(R.id.newton_cradle_loading);

        if (!sessionId.equals(Config.SHARED_PREFRENCE_NO_DATA_KEY_STRING)) {
            if (controller.InternetCheck(context)) {
                newtonCradleLoading.setLoadingColor(R.color.button_bg);
                controller.LoadingStart(newtonCradleLoading);
                data = new HashMap<>();
                data.put("session_id", sessionId);
                controller.centralizedApiHitting(getString(R.string.get_contact_details_api), getString(R.string.get_type_api), context, parentApiCallback, data);
            } else {
                controller.Toast_show(context, getString(R.string.enable_internet));
            }
        } else {
            name = "";
            email = "";
            phoneNumber = "";
            uid = "0";
        }

    }

    private void set_action_bar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        menu_btn = (ImageView) toolbar.findViewById(R.id.menu_btn);
        appIcon = (ImageView) toolbar.findViewById(R.id.app_icon);
        ImageView search_dashboard = (ImageView) toolbar.findViewById(R.id.search_dashboard);
        save_edit = (TextView) toolbar.findViewById(R.id.save_edit);

        search_dashboard.setVisibility(View.GONE);
        save_edit.setVisibility(View.VISIBLE);
        menu_btn.setBackgroundResource(R.drawable.close_floating_btn);
        save_edit.setText(getString(R.string.send));

        menu_btn.setOnClickListener(this);
        save_edit.setOnClickListener(this);
    }

    private void getData(String response, int i) throws Exception {
        switch (i) {
            case 0:
                jsonArray = new JSONArray(response);
                jsonObject = jsonArray.getJSONObject(0);

                controller.LoadingStop(newtonCradleLoading);
                name = jsonObject.getString("name");
                email = jsonObject.getString("email");
                phoneNumber = jsonObject.getString("contact");
                uid = jsonObject.getString("uid");

                setData();
                break;

            case 1:
                jsonArray = new JSONArray(response);
                jsonObject = jsonArray.getJSONObject(0);

                String status = jsonObject.getString("Status");
                String message = jsonObject.getString("Message");

                controller.Pd_stop();
                controller.Toast_show(context,message);
                break;
        }
    }

    private void setData() {
        name_enter.setText(name);
        email_enter.setText(email);
        phone_number_enter.setText(phoneNumber);
    }

    @Override
    public void onBackPressed() {
        finish();
        controller.Animation_backward(context);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_btn:
                onBackPressed();
                break;

            case R.id.save_edit:
                if (controller.Check_all_empty(name_enter)) {
                    name_enter.setError(getString(R.string.please_enter_name));
                } else if (controller.Check_all_empty(email_enter)) {
                    email_enter.setError(getString(R.string.please_enter_name));
                } else if (controller.Check_all_empty(phone_number_enter)) {
                    phone_number_enter.setError(getString(R.string.enter_username));
                } else if (controller.Check_all_empty(message_enter)) {
                    message_enter.setError(getString(R.string.please_enter_message));
                } else {

                    name = name_enter.getText().toString();
                    email = email_enter.getText().toString();
                    phoneNumber = phone_number_enter.getText().toString();
                    message = message_enter.getText().toString();

                    data = new HashMap<>();
                    data.put("name", Uri.encode(name));
                    data.put("email", email);
                    data.put("phone_number", phoneNumber);
                    data.put("uid", uid);
                    data.put("message",  Uri.encode(message));

                    if (!controller.InternetCheck(context)) {
                        controller.Toast_show(context, getString(R.string.enable_internet));
                    } else {
                        controller.Pd_start(context);
                        controller.centralizedApiHitting(getString(R.string.send_contact_us_api), getString(R.string.get_type_api), context, parentApiCallback, data);
                    }
                }

                break;
        }
    }
}
