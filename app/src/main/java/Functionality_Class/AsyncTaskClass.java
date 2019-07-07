package Functionality_Class;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.StudentFaclity.Stulity.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Call_Back.ChildApiCallback;
import Call_Back.ParentApiCallback;
import Controllers.Config;


@SuppressWarnings("ALL")
public class AsyncTaskClass extends AsyncTask<String, String, String> {

    private Context context;
    private ApiResponse apiResponse = new ApiResponse();
    private JSONObject parameter;
    private String response, callingUI, requestType;
    private ParentApiCallback parentApiCallback;
    private ChildApiCallback childApiCallback;
    private JSONObject jsonObject;
    private boolean checkParentApi = false;
    private HashMap<String, Object> data, apiData;
    private String apiDataString;

    //All_api constructor
    public AsyncTaskClass(Object... args) {
        callingUI = (String) args[0];
        requestType = (String) args[1];
        context = (Context) args[2];

        try {
            checkParentApi = true;
            parentApiCallback = (ParentApiCallback) args[3];
        } catch (Exception e) {
            checkParentApi = false;
            childApiCallback = (ChildApiCallback) args[3];
        }

        if (requestType.equals(context.getString(R.string.get_type_api))) {
            data = (HashMap<String, Object>) args[4];
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    //Hit all api in Background Task
    @Override
    protected String doInBackground(String... args) {

        if (callingUI.equals(context.getString(R.string.email_check))) {
            response = apiResponse.ResponseGet(context, Config.BASE_URL + Config.BASE_REGISTER + Config.CHECK_EMAIL + data.get("email_id").toString());
        } else if (callingUI.equals(context.getString(R.string.phone_check))) {
            response = apiResponse.ResponseGet(context, Config.BASE_URL + Config.BASE_REGISTER + Config.CHECK_CONTACT + data.get("phone_number").toString());
        } else if (callingUI.equals(context.getString(R.string.otp_send))) {
            response = apiResponse.ResponseGet(context, Config.BASE_URL + Config.BASE_REGISTER + Config.OTP_SEND + data.get("phone_number").toString());
        } else if (callingUI.equals(context.getString(R.string.otp_verify))) {
            response = apiResponse.ResponseGet(context, Config.BASE_URL + Config.BASE_REGISTER + Config.OTP_VERIFY + Config.Contact + data.get("phone_number").toString() +
                    Config.OTP + data.get("otp_verify"));
        } else if (callingUI.equals(context.getString(R.string.register_api))) {
            response = apiResponse.ResponseGet(context, Config.BASE_URL + Config.BASE_REGISTER + Config.REGISTER + Config.FIRST_NAME +
                    data.get("first_name").toString() + Config.LAST_NAME +
                    data.get("last_name").toString() + Config.CONTACT +
                    data.get("phone_number").toString() + Config.EMAIL +
                    data.get("email_id").toString() + Config.PASSWORD +
                    data.get("password").toString() + Config.TERMS +
                    data.get("terms").toString());
        } else if (callingUI.equals(context.getString(R.string.login_api))) {

            response = apiResponse.ResponseGet(context, Config.BASE_URL + Config.BASE_REGISTER + Config.LOGIN +
                    data.get("uid").toString() + Config.PASSWORD + data.get("password").toString());

        } else if (callingUI.equals(context.getString(R.string.forgot_otp_send))) {
            response = apiResponse.ResponseGet(context, Config.BASE_URL + Config.BASE_REGISTER + Config.FORGOT_OTP + data.get("phone_number").toString());
        } else if (callingUI.equals(context.getString(R.string.forgot_otp_verify))) {
            response = apiResponse.ResponseGet(context, Config.BASE_URL + Config.BASE_REGISTER + Config.FORGOT_PASS + data.get("phone_number").toString()
                    + Config.OTP + data.get("otp_verify").toString());
        } else if (callingUI.equals(context.getString(R.string.forgot_otp_resend))) {
            response = apiResponse.ResponseGet(context, Config.BASE_URL + Config.BASE_REGISTER + Config.FORGOT_OTP + data.get("phone_number").toString());
        } else if (callingUI.equals(context.getString(R.string.change_password_api))) {
            response = apiResponse.ResponseGet(context, Config.BASE_URL + Config.BASE_REGISTER + Config.NEW_PASSWORD + data.get("session_id").toString()
                    + Config.PASSWORD + data.get("password").toString());
        } else if (callingUI.equals(context.getString(R.string.logout_api))) {
            response = apiResponse.ResponseGet(context, Config.BASE_URL + Config.BASE_REGISTER + Config.LOGOUT + data.get("session_id").toString());
        } else if (callingUI.equals(context.getString(R.string.get_profile_details_api))) {
            response = apiResponse.ResponseGet(context, Config.BASE_URL + Config.BASE_PROFILE + Config.GET_DETAILS + data.get("session_id").toString());
        } else if (callingUI.equals(context.getString(R.string.update_user_profile_api))) {

            response = apiResponse.ResponseGet(context, Config.BASE_URL + Config.BASE_PROFILE + Config.UPDATE +
                    data.get("session_id").toString() + Config.FIRST_NAME_AND +
                    data.get("fname").toString() + Config.LAST_NAME +
                    data.get("lname").toString() + Config.EMAIL +
                    data.get("email").toString() + Config.ADDRESS+
                    data.get("address").toString());

        }else if (callingUI.equals(context.getString(R.string.change_contact_send_otp_api))) {

            response = apiResponse.ResponseGet(context, Config.BASE_URL + Config.BASE_PROFILE + Config.UPDATE_PROFILE_OTP +
                    data.get("session_id").toString() + Config.CONTACT +
                    data.get("phone_number").toString());

        }else if (callingUI.equals(context.getString(R.string.change_contact_send_otp_resend))) {

            response = apiResponse.ResponseGet(context, Config.BASE_URL + Config.BASE_PROFILE + Config.UPDATE_PROFILE_OTP +
                    data.get("session_id").toString() + Config.CONTACT +
                    data.get("phone_number").toString());

        }else if (callingUI.equals(context.getString(R.string.change_contact_number_api))) {

            response = apiResponse.ResponseGet(context, Config.BASE_URL + Config.BASE_PROFILE + Config.CONTACT_UPDATE +
                    data.get("session_id").toString() + Config.CONTACT +
                    data.get("phone_number").toString()+Config.OTP+
                    data.get("otp_verify").toString());

        }else if (callingUI.equals(context.getString(R.string.ad_list_api))) {

            response = apiResponse.ResponseGet(context, Config.BASE_URL + Config.BASE_PROFILE + Config.AD_RESULT_BY_ID +
                    data.get("session_id").toString());

        }else if (callingUI.equals(context.getString(R.string.get_ad_details_api))) {

            response = apiResponse.ResponseGet(context, Config.BASE_URL + Config.BASE_SEARCH + Config.AD_BY_ID +
                    data.get("adid").toString());

        }else if (callingUI.equals(context.getString(R.string.get_contact_details_api))) {

            response = apiResponse.ResponseGet(context, Config.BASE_URL + Config.BASE_CONTACT + Config.GET_USER_DETAILS +
                    data.get("session_id").toString());

        }else if (callingUI.equals(context.getString(R.string.send_contact_us_api))) {

            response = apiResponse.ResponseGet(context, Config.BASE_URL + Config.BASE_CONTACT + Config.CONTACT_NAME +
                    data.get("name").toString()+Config.EMAIL+
                    data.get("email").toString()+Config.MOBILE+
                    data.get("phone_number").toString()+Config.UID+
                    data.get("uid").toString()+Config.MESSAGE+
                    data.get("message").toString());


        }else if (callingUI.equals(context.getString(R.string.dash_board_api))) {

            response = apiResponse.ResponseGet(context, Config.BASE_URL + Config.BASE_SEARCH + Config.DASHBOARD);

        }else if (callingUI.equals(context.getString(R.string.search_box_api))) {

            response = apiResponse.ResponseGet(context, Config.SEARCH_BOX_API+data.get("input").toString());

        }else if (callingUI.equals(context.getString(R.string.search_box_type_api))) {

            response = apiResponse.ResponseGet(context, Config.SEARCH_BOX_TYPE_API);

        }else if (callingUI.equals(context.getString(R.string.search_box_result_api))) {

            response = apiResponse.ResponseGet(context, Config.BASE_SEARCH_RESULT +
                    data.get("stype").toString()+Config.PCMAX+
                    data.get("pcmax").toString()+Config.PCMIN+
                    data.get("pcmin").toString()+Config.LOCNM+
                    data.get("SearchBoxType").toString()+Config.LOCTYPE+
                    data.get("IdentityName").toString()+Config.LOCVAL+
                    data.get("IdentityValue").toString()+Config.LOCID+
                    data.get("IdentityID").toString()+Config.CITYID+
                    data.get("cityid").toString()+Config.CATID+
                    data.get("CIdentityID").toString()+Config.CTNM+
                    data.get("CIdentityName").toString()+Config.CTYPE+
                    data.get("CIdentityValue").toString()+"&lastid=0");

        }

//        if (!response.equals(context.getString(R.string.error_Http_not_found)) && !response.equals(context.getString(R.string.error_Http_internal))
//         && !response.equals(context.getString(R.string.error_Http_other)) && !response.equals(context.getString(R.string.error))){
//
//        }

        return null;
    }

    @Override
    protected void onPostExecute(String file_url) {
        if (response == null) {
            response = context.getString(R.string.error);
        }

        if (response.equals(context.getString(R.string.error_Http_not_found))) {
            if (checkParentApi) {
                parentApiCallback.Data_call_back(callingUI, response);
            } else {
                childApiCallback.Data_call_back(callingUI, response);
            }
        } else if (response.equals(context.getString(R.string.error_Http_internal))) {
            if (checkParentApi) {
                parentApiCallback.Data_call_back(callingUI, response);
            } else {
                childApiCallback.Data_call_back(callingUI, response);
            }
        } else if (response.equals(context.getString(R.string.error_Http_other))) {
            if (checkParentApi) {
                parentApiCallback.Data_call_back(callingUI, response);
            } else {
                childApiCallback.Data_call_back(callingUI, response);
            }
        } else if (response.equals(context.getString(R.string.error))) {
            if (checkParentApi) {
                parentApiCallback.Data_call_back(callingUI, response);
            } else {
                childApiCallback.Data_call_back(callingUI, response);
            }
        } else {
            if (checkParentApi) {
                parentApiCallback.Data_call_back(callingUI, response);
            } else {
                childApiCallback.Data_call_back(callingUI, response);
            }
        }

    }
}
