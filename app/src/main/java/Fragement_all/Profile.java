package Fragement_all;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.StudentFaclity.Stulity.CentralizedWebView;
import com.StudentFaclity.Stulity.ContactUs;
import com.StudentFaclity.Stulity.Dash_board;
import com.StudentFaclity.Stulity.EditProfile;
import com.StudentFaclity.Stulity.Login_activity;
import com.StudentFaclity.Stulity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import Call_Back.ChildApiCallback;
import Call_Back.DialogCallBack;
import Controllers.Config;
import Controllers.Controller;

public class Profile extends Fragment implements View.OnClickListener {
    private View root_view;
    private LinearLayout signout_parent, seller_parent, post_add_parent, transition_history_parent,
            about_us_parent, contact_us_parent,terms_service_parent;
    private TextView mobile_number, balance, edit_profile;
    private Context context;
    private Controller controller;
    private ChildApiCallback childApiCallback;
    private HashMap<String, Object> data;
    private String sessionId;
    private Intent intent;
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private String status, message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.profile, container, false);
        context = root_view.getContext();
        controller = new Controller();
        controller.initSharedPreference(context);
        sessionId = controller.getString(getString(R.string.session_id));
        callBacks();
        return root_view;
    }

    private void callBacks() {
        childApiCallback = new ChildApiCallback() {
            @Override
            public void Data_call_back(Object... args) {
                String callingUI = (String) args[0];
                String response = (String) args[1];

                if (callingUI.equals(getString(R.string.logout_api))) {
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
                } else if (callingUI.equals(getString(R.string.get_profile_details_api))) {
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
                        getData(response, 1);
                    }
                }
            }
        };

        initialise_view();
    }

    private void initialise_view() {
        mobile_number = (TextView) root_view.findViewById(R.id.mobile_number);
        balance = (TextView) root_view.findViewById(R.id.balance);
        edit_profile = (TextView) root_view.findViewById(R.id.edit_profile);

        seller_parent = (LinearLayout) root_view.findViewById(R.id.seller_parent);
        post_add_parent = (LinearLayout) root_view.findViewById(R.id.post_add_parent);
        transition_history_parent = (LinearLayout) root_view.findViewById(R.id.transition_history_parent);
        about_us_parent = (LinearLayout) root_view.findViewById(R.id.about_us_parent);
        contact_us_parent = (LinearLayout) root_view.findViewById(R.id.contact_us_parent);
        terms_service_parent = (LinearLayout) root_view.findViewById(R.id.terms_service_parent);
        signout_parent = (LinearLayout) root_view.findViewById(R.id.signout_parent);

        edit_profile.setOnClickListener(this);
        seller_parent.setOnClickListener(this);
        post_add_parent.setOnClickListener(this);
        transition_history_parent.setOnClickListener(this);
        about_us_parent.setOnClickListener(this);
        contact_us_parent.setOnClickListener(this);
        terms_service_parent.setOnClickListener(this);
        signout_parent.setOnClickListener(this);

        if (sessionId.equals(Config.SHARED_PREFRENCE_NO_DATA_KEY_STRING)) {
            callLoginTypeMethod(0);
        } else {
            if (controller.InternetCheck(context)) {
                data = new HashMap<>();
                data.put("session_id", sessionId);
                controller.centralizedApiHitting(getString(R.string.get_profile_details_api), getString(R.string.get_type_api), context, childApiCallback, data);
            } else {
                controller.Toast_show(context, getString(R.string.enable_internet));
            }
            callLoginTypeMethod(1);
        }
    }

    private void callLoginTypeMethod(int i) {
        switch (i) {
            case 0:
                mobile_number.setText(getString(R.string.dummy_mobile_number));
                balance.setText(getString(R.string.batuva_balace) + " " + getString(R.string.dummy_balance));
                edit_profile.setVisibility(View.GONE);
                transition_history_parent.setVisibility(View.GONE);
                signout_parent.setVisibility(View.GONE);
                break;
            case 1:
                mobile_number.setText(getString(R.string.dummy_mobile_number));
                balance.setText(getString(R.string.batuva_balace) + " " + getString(R.string.dummy_balance));
                edit_profile.setVisibility(View.VISIBLE);
                transition_history_parent.setVisibility(View.VISIBLE);
                signout_parent.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.seller_parent:
                if (controller.InternetCheck(context)) {
                } else {
                    controller.Toast_show(context, getString(R.string.enable_internet));
                }
                break;

            case R.id.post_add_parent:
                if (controller.InternetCheck(context)) {
                    intent = new Intent(context, CentralizedWebView.class);
                    intent.putExtra("url",Config.POST_AD);
                    startActivity(intent);
                } else {
                    controller.Toast_show(context, getString(R.string.enable_internet));
                }
                break;

            case R.id.transition_history_parent:
                if (controller.InternetCheck(context)) {
                } else {
                    controller.Toast_show(context, getString(R.string.enable_internet));
                }
                break;

            case R.id.about_us_parent:
                if (controller.InternetCheck(context)) {
                    intent = new Intent(context, CentralizedWebView.class);
                    intent.putExtra("url",Config.ABOUT_US);
                    startActivity(intent);
                } else {
                    controller.Toast_show(context, getString(R.string.enable_internet));
                }
                break;

            case R.id.contact_us_parent:
                if (controller.InternetCheck(context)) {
                    intent = new Intent(context, ContactUs.class);
                    startActivity(intent);
                } else {
                    controller.Toast_show(context, getString(R.string.enable_internet));
                }
                break;

            case R.id.terms_service_parent:
                if (controller.InternetCheck(context)) {
                    intent = new Intent(context, CentralizedWebView.class);
                    intent.putExtra("url",Config.TERMS_OF_SERVICE);
                    startActivity(intent);
                } else {
                    controller.Toast_show(context, getString(R.string.enable_internet));
                }
                break;

            case R.id.signout_parent:
                if (controller.InternetCheck(context)) {
                    data = new HashMap<>();
                    data.put("session_id", sessionId);
                    controller.Pd_start(context);
                    controller.centralizedApiHitting(getString(R.string.logout_api), getString(R.string.get_type_api), context, childApiCallback, data);
                } else {
                    controller.Toast_show(context, getString(R.string.enable_internet));
                }
                break;

            case R.id.edit_profile:
                if (controller.InternetCheck(context)) {
                    intent = new Intent(context, EditProfile.class);
                    startActivity(intent);
                    controller.Animation_backward(context);
                } else {
                    controller.Toast_show(context, getString(R.string.enable_internet));
                }
                break;
        }
    }

    private void getData(String response, int type) {
        switch (type) {
            case 0:
                try {
                    jsonArray = new JSONArray(response);
                    jsonObject = jsonArray.getJSONObject(0);
                    status = jsonObject.getString("Status");
                    message = jsonObject.getString("Message");
                    if (status.equals(getString(R.string.status_ok))) {
                        String sessionId = jsonObject.getString("Message");
                        controller.saveString(getString(R.string.session_id), sessionId);
                        controller.Pd_stop();
                        callIntentLogout();
                    } else {
                        controller.Toast_show(context, message);
                        controller.Pd_stop();
                    }
                } catch (JSONException e) {
                    controller.Toast_show(context, getString(R.string.facing_some_technical_issue));
                    controller.Pd_stop();
                    e.printStackTrace();
                }
                break;

            case 1:
                try {
                    jsonArray = new JSONArray(response);
                    jsonObject = jsonArray.getJSONObject(0);

                    String mobileNumber = jsonObject.getString("contact");
                    mobile_number.setText(mobileNumber);

                } catch (JSONException e) {
                    controller.Toast_show(context, getString(R.string.facing_some_technical_issue));
                    controller.Pd_stop();
                    e.printStackTrace();
                }
                break;

        }
    }

    private void callIntentLogout() {
        controller.removeSharedPreferences(getString(R.string.session_id));
        intent = new Intent(context, Login_activity.class);
        startActivity(intent);
        controller.Animation_backward(context);
    }
}