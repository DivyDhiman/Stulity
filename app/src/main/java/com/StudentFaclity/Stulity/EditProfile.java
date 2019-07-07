package com.StudentFaclity.Stulity;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import Call_Back.DialogCallBack;
import Call_Back.ParentApiCallback;
import Controllers.Controller;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private Controller controller;
    private ImageView menu_btn,appIcon;
    private TextView save_edit,first_name_heading,last_name_heading,email_heading,address_heading,contact_heading,contact_txt;
    private EditText first_name,last_name,email_id,address;
    private ParentApiCallback parentApiCallBack;
    private HashMap<String,Object> data;
    private String sessionId,name,email,contact,Address,usertype,ContactStatus;
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private boolean isEdit = false;
    private DialogCallBack dialogCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        context = EditProfile.this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }

        controller = new Controller();
        controller.initSharedPreference(context);
        sessionId = controller.getString(getString(R.string.session_id));
        set_action_bar();
        callBacks();
    }

    private void callBacks() {
        parentApiCallBack = new ParentApiCallback() {
            @Override
            public void Data_call_back(Object... args) {
                String callingUI = (String) args[0];
                String response = (String) args[1];

                Log.e("response","response"+response);

                if (callingUI.equals(getString(R.string.get_profile_details_api))) {
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
                        getData(response, 0);
                    }
                }else if (callingUI.equals(getString(R.string.update_user_profile_api))) {
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
                        controller.Toast_show(context,response);
                    }
                }else if (callingUI.equals(getString(R.string.change_contact_send_otp_api))) {
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
                        getData(response, 2);
                    }
                }
            }
        };

        dialogCallBack = new DialogCallBack() {
            @Override
            public void clickDialogItem(Object... args) {
                contact = (String) args[0];
                contact_txt.setText(contact);
                controller.customDialogDisable();
            }
        };

        initialise_view();
    }

    private void initialise_view() {
        first_name_heading = (TextView) findViewById(R.id.first_name_heading);
        last_name_heading = (TextView) findViewById(R.id.last_name_heading);
        email_heading = (TextView) findViewById(R.id.email_heading);
        address_heading = (TextView) findViewById(R.id.address_heading);
        contact_heading = (TextView) findViewById(R.id.contact_heading);
        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        email_id = (EditText) findViewById(R.id.email_id);
        address = (EditText) findViewById(R.id.address);
        contact_txt = (TextView) findViewById(R.id.contact_txt);

        contact_heading.setOnClickListener(this);
        contact_txt.setOnClickListener(this);

        changeView(2);

        if (controller.InternetCheck(context)) {
            controller.Pd_start(context);
            data = new HashMap<>();
            data.put("session_id", sessionId);
            controller.centralizedApiHitting(getString(R.string.get_profile_details_api), getString(R.string.get_type_api), context, parentApiCallBack, data);
        } else {
            controller.Toast_show(context, getString(R.string.enable_internet));
        }

    }

    private void getData(String response, int i) {
        switch (i){
            case 0:
                try {
                    controller.Pd_stop();
                    jsonArray = new JSONArray(response);
                    jsonObject = jsonArray.getJSONObject(0);

                    name = jsonObject.getString("name");
                    email = jsonObject.getString("email");
                    contact = jsonObject.getString("contact");
                    Address = jsonObject.getString("Address");
                    usertype = jsonObject.getString("usertype");
                    ContactStatus = jsonObject.getString("ContactStatus");

//                    first_name = (EditText) findViewById(R.id.first_name);
//                    last_name = (EditText) findViewById(R.id.last_name);

                    email_id.setText(email);
                    address.setText(Address);
                    contact_txt.setText(contact);
//                    save_edit.setEnabled(true);
                    save_edit.setText(getString(R.string.edit));

                } catch (JSONException e) {
                    controller.Pd_stop();
                    controller.Toast_show(context, getString(R.string.facing_some_technical_issue));
                    controller.Pd_stop();
                    e.printStackTrace();
                }
                break;

            case 1:
                try {
                    jsonArray = new JSONArray(response);
                    jsonObject = jsonArray.getJSONObject(0);

                    controller.Pd_stop();
                } catch (JSONException e) {
                    controller.Pd_stop();
                    controller.Toast_show(context, getString(R.string.facing_some_technical_issue));
                    controller.Pd_stop();
                    e.printStackTrace();
                }
                break;

            case 2:
                try {
                    jsonArray = new JSONArray(response);
                    jsonObject = jsonArray.getJSONObject(0);
                    controller.Pd_stop();

                    controller.customDialog(context, getString(R.string.change_contact_view), R.layout.otp_verify, dialogCallBack,data);
                } catch (JSONException e) {
                    controller.Pd_stop();
                    controller.Toast_show(context, getString(R.string.facing_some_technical_issue));
                    controller.Pd_stop();
                    e.printStackTrace();
                }
                break;
        }
    }

    private void set_action_bar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        menu_btn = (ImageView) toolbar.findViewById(R.id.menu_btn);
        appIcon = (ImageView) toolbar.findViewById(R.id.app_icon);
        save_edit = (TextView) toolbar.findViewById(R.id.save_edit);
        ImageView search_dashboard = (ImageView) toolbar.findViewById(R.id.search_dashboard);

        search_dashboard.setVisibility(View.GONE);
        menu_btn.setBackgroundResource(R.drawable.close_floating_btn);
        save_edit.setVisibility(View.VISIBLE);

        menu_btn.setOnClickListener(this);
        save_edit.setOnClickListener(this);
    }

    private void changeView(int i) {
        switch (i){
            case 0:
                if (controller.InternetCheck(context)) {
                    controller.Pd_start(context);
                    data = new HashMap<>();
                    data.put("session_id", sessionId);
                    data.put("fname", first_name.getText().toString());
                    data.put("lname", last_name.getText().toString());
                    data.put("email", email_id.getText().toString());
                    data.put("address", address.getText().toString());
                    controller.centralizedApiHitting(getString(R.string.update_user_profile_api), getString(R.string.get_type_api), context, parentApiCallBack, data);
                } else {
                    controller.Toast_show(context, getString(R.string.enable_internet));
                }

            break;

            case 1:
                isEdit = true;
                save_edit.setText(getString(R.string.save));
                first_name.setEnabled(true);
                last_name.setEnabled(true);
                email_id.setEnabled(true);
                address.setEnabled(true);
                break;

            case 2:
//                save_edit.setEnabled(false);
                first_name.setEnabled(false);
                last_name.setEnabled(false);
                email_id.setEnabled(false);
                address.setEnabled(false);
                break;

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.menu_btn:
                onBackPressed();
                break;

            case R.id.save_edit:
                if(isEdit){
                    changeView(0);
                }else {
                    changeView(1);
                }
                break;

            case R.id.contact_heading:
                if (controller.InternetCheck(context)) {
                    data = new HashMap<>();
                    data.put("session_id", sessionId);
                    data.put("phone_number", contact);
                    controller.customDialog(context, getString(R.string.change_contact_view), R.layout.otp_verify, dialogCallBack,data);
                } else {
                    controller.Toast_show(context, getString(R.string.enable_internet));
                }
                break;

            case R.id.contact_txt:
                if (controller.InternetCheck(context)) {
                    data = new HashMap<>();
                    data.put("session_id", sessionId);
                    data.put("phone_number", contact);
                    controller.customDialog(context, getString(R.string.change_contact_view), R.layout.otp_verify, dialogCallBack,data);
                } else {
                    controller.Toast_show(context, getString(R.string.enable_internet));
                }
                break;
        }
    }

}