package Functionality_Class;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.StudentFaclity.Stulity.R;
import com.victor.loading.book.BookLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import Call_Back.ChildApiCallback;
import Call_Back.DialogCallBack;
import Controllers.Config;
import Controllers.Controller;

@SuppressWarnings("ALL")
public class CustomDialog implements View.OnClickListener {

    private Context context;
    private Controller controller;
    private String type, session_id;
    private Dialog customDialog;
    private DialogCallBack dialogCallBack;
    private TextView heading, change_phone_number, resend, cancel_txt, send;
    private EditText edit_txt, edit_txt_second;
    private AutoCompleteTextView search_box,search_box_type;
    private BookLoading bookLoading;
    private ChildApiCallback childApiCallback;
    private ArrayList<HashMap<String,Object>> dataSearchAPIGet,dataSearchTypeAPIGet,dataTypeShow;
    private HashMap<String, Object> data, data_get,dataSearchAPIGetSub,dataSearchTypeAPIGetSUB,finalData;
    private boolean checktype = false;
    private String phoneNumber;

    public void DialogShow(Object... args) {
        this.context = (Context) args[0];
        this.type = (String) args[1];
        int layout = (int) args[2];
        this.dialogCallBack = (DialogCallBack) args[3];
        this.data_get = (HashMap<String, Object>) args[4];

        controller = new Controller();

        customDialog = new Dialog(context);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(layout);
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.setCancelable(false);
        customDialog.show();

        if (type.equals(context.getString(R.string.search_dialog_layout))) {
            heading = (TextView) customDialog.findViewById(R.id.heading);
            search_box = (AutoCompleteTextView) customDialog.findViewById(R.id.search_box);
            search_box_type = (AutoCompleteTextView) customDialog.findViewById(R.id.search_box_type);
            cancel_txt = (TextView) customDialog.findViewById(R.id.cancel_txt);
            send = (TextView) customDialog.findViewById(R.id.send);

        } else {
            heading = (TextView) customDialog.findViewById(R.id.heading);
            change_phone_number = (TextView) customDialog.findViewById(R.id.change_phone_number);
            resend = (TextView) customDialog.findViewById(R.id.resend);
            edit_txt = (EditText) customDialog.findViewById(R.id.edit_txt);
            edit_txt_second = (EditText) customDialog.findViewById(R.id.edit_txt_second);
            cancel_txt = (TextView) customDialog.findViewById(R.id.cancel_txt);
            send = (TextView) customDialog.findViewById(R.id.send);
            bookLoading = (BookLoading) customDialog.findViewById(R.id.bookloading);
        }

        call_view_type(type);

    }

    private void call_view_type(String type) {
        if (type.equals(context.getString(R.string.otp_verify_screen))) {
            resend.setVisibility(View.GONE);
            change_phone_number.setVisibility(View.GONE);
            heading.setText(context.getString(R.string.otp_verify));
            resend.setText(context.getString(R.string.resend));
            edit_txt.setHint(context.getString(R.string.please_enter_otp));
            cancel_txt.setText(context.getString(R.string.cancel));
            send.setText(context.getString(R.string.send));

            resend.setOnClickListener(this);
            cancel_txt.setOnClickListener(this);
            send.setOnClickListener(this);

            childApiCallback = new ChildApiCallback() {
                @Override
                public void Data_call_back(Object... args) {
                    String callingUI = (String) args[0];
                    String response = (String) args[1];

                    if (response.equals(context.getString(R.string.error_Http_not_found))) {
                        controller.Toast_show(context, context.getString(R.string.unable_to_connect_to_network));
                        controller.LoadingStopDailog(bookLoading);
                        HideBookLocder(0);
                    } else if (response.equals(context.getString(R.string.error_Http_internal))) {
                        controller.Toast_show(context, context.getString(R.string.unable_to_connect_to_network));
                        controller.LoadingStopDailog(bookLoading);
                        HideBookLocder(0);
                    } else if (response.equals(context.getString(R.string.error_Http_other))) {
                        controller.Toast_show(context, context.getString(R.string.facing_some_technical_issue));
                        controller.LoadingStopDailog(bookLoading);
                        HideBookLocder(0);
                    } else if (response.equals(context.getString(R.string.error))) {
                        controller.Toast_show(context, context.getString(R.string.facing_some_technical_issue));
                        controller.LoadingStopDailog(bookLoading);
                        HideBookLocder(0);
                    } else {
                        if (response.equals("\"\"")) {
                            dialogCallBack.clickDialogItem(context.getString(R.string.send), response);
                            send.setText(context.getString(R.string.send));
                            HideBookLocder(0);
                        } else {
                            controller.Toast_show(context, response);
                            send.setText(context.getString(R.string.send));
                            HideBookLocder(0);
                        }
                    }
                }
            };
        } else if (type.equals(context.getString(R.string.forgot_password_screen))) {
            change_phone_number.setVisibility(View.GONE);
            resend.setVisibility(View.GONE);
            edit_txt_second.setVisibility(View.GONE);

            heading.setText(context.getString(R.string.forgot_password));
            change_phone_number.setText(context.getString(R.string.edit_phone_no));
            resend.setText(context.getString(R.string.resend));
            edit_txt.setHint(context.getString(R.string.please_enter_phone_number));
            cancel_txt.setText(context.getString(R.string.cancel));
            send.setText(context.getString(R.string.proceed));

            change_phone_number.setOnClickListener(this);
            resend.setOnClickListener(this);
            cancel_txt.setOnClickListener(this);
            send.setOnClickListener(this);

            childApiCallback = new ChildApiCallback() {
                @Override
                public void Data_call_back(Object... args) {
                    String callingUI = (String) args[0];
                    String response = (String) args[1];

                    if (callingUI.equals(context.getString(R.string.forgot_otp_send))) {
                        if (response.equals(context.getString(R.string.error_Http_not_found))) {
                            controller.Toast_show(context, context.getString(R.string.unable_to_connect_to_network));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(1);
                        } else if (response.equals(context.getString(R.string.error_Http_internal))) {
                            controller.Toast_show(context, context.getString(R.string.unable_to_connect_to_network));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(1);
                        } else if (response.equals(context.getString(R.string.error_Http_other))) {
                            controller.Toast_show(context, context.getString(R.string.facing_some_technical_issue));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(1);
                        } else if (response.equals(context.getString(R.string.error))) {
                            controller.Toast_show(context, context.getString(R.string.facing_some_technical_issue));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(1);
                        } else {
                            try {
                                getForgotPasswordOtpSendData(response, 0);
                            } catch (Exception e) {
                                controller.Toast_show(context, context.getString(R.string.facing_some_technical_issue));
                                HideBookLocder(1);
                                e.printStackTrace();
                            }
                        }
                    } else if (callingUI.equals(context.getString(R.string.forgot_otp_resend))) {
                        if (response.equals(context.getString(R.string.error_Http_not_found))) {
                            controller.Toast_show(context, context.getString(R.string.unable_to_connect_to_network));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(2);
                        } else if (response.equals(context.getString(R.string.error_Http_internal))) {
                            controller.Toast_show(context, context.getString(R.string.unable_to_connect_to_network));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(2);
                        } else if (response.equals(context.getString(R.string.error_Http_other))) {
                            controller.Toast_show(context, context.getString(R.string.facing_some_technical_issue));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(2);
                        } else if (response.equals(context.getString(R.string.error))) {
                            controller.Toast_show(context, context.getString(R.string.facing_some_technical_issue));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(3);
                        } else {
                            try {
                                getForgotPasswordOtpSendData(response, 1);
                            } catch (Exception e) {
                                controller.Toast_show(context, context.getString(R.string.facing_some_technical_issue));
                                HideBookLocder(2);
                                e.printStackTrace();
                            }
                        }
                    } else if (callingUI.equals(context.getString(R.string.forgot_otp_verify))) {
                        if (response.equals(context.getString(R.string.error_Http_not_found))) {
                            controller.Toast_show(context, context.getString(R.string.unable_to_connect_to_network));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(2);
                        } else if (response.equals(context.getString(R.string.error_Http_internal))) {
                            controller.Toast_show(context, context.getString(R.string.unable_to_connect_to_network));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(2);
                        } else if (response.equals(context.getString(R.string.error_Http_other))) {
                            controller.Toast_show(context, context.getString(R.string.facing_some_technical_issue));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(2);
                        } else if (response.equals(context.getString(R.string.error))) {
                            controller.Toast_show(context, context.getString(R.string.facing_some_technical_issue));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(2);
                        } else {
                            session_id = String.valueOf(response.replaceAll("^\"|\"$", ""));
                            HideBookLocder(3);
                        }
                    } else if (callingUI.equals(context.getString(R.string.change_password_api))) {
                        if (response.equals(context.getString(R.string.error_Http_not_found))) {
                            controller.Toast_show(context, context.getString(R.string.unable_to_connect_to_network));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(3);
                        } else if (response.equals(context.getString(R.string.error_Http_internal))) {
                            controller.Toast_show(context, context.getString(R.string.unable_to_connect_to_network));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(3);
                        } else if (response.equals(context.getString(R.string.error_Http_other))) {
                            controller.Toast_show(context, context.getString(R.string.facing_some_technical_issue));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(3);
                        } else if (response.equals(context.getString(R.string.error))) {
                            controller.Toast_show(context, context.getString(R.string.facing_some_technical_issue));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(3);
                        } else {
                            try {
                                getForgotPasswordOtpSendData(response, 2);
                            } catch (Exception e) {
                                controller.Toast_show(context, context.getString(R.string.facing_some_technical_issue));
                                HideBookLocder(3);
                                e.printStackTrace();
                            }
                        }
                    }
                }
            };

        } else if (type.equals(context.getString(R.string.change_contact_view))) {
            change_phone_number.setVisibility(View.GONE);
            resend.setVisibility(View.GONE);
            edit_txt_second.setVisibility(View.GONE);

            heading.setText(context.getString(R.string.forgot_password));
            change_phone_number.setText(context.getString(R.string.edit_phone_no));
            resend.setText(context.getString(R.string.resend));
            edit_txt.setHint(context.getString(R.string.please_enter_phone_number));
            cancel_txt.setText(context.getString(R.string.cancel));
            send.setText(context.getString(R.string.proceed));

            edit_txt.setText(data_get.get("phone_number").toString());

            change_phone_number.setOnClickListener(this);
            resend.setOnClickListener(this);
            cancel_txt.setOnClickListener(this);
            send.setOnClickListener(this);

            childApiCallback = new ChildApiCallback() {
                @Override
                public void Data_call_back(Object... args) {
                    String callingUI = (String) args[0];
                    String response = (String) args[1];
                    if (callingUI.equals(context.getString(R.string.change_contact_send_otp_api))) {
                        if (response.equals(context.getString(R.string.error_Http_not_found))) {
                            controller.Toast_show(context, context.getString(R.string.unable_to_connect_to_network));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(1);
                        } else if (response.equals(context.getString(R.string.error_Http_internal))) {
                            controller.Toast_show(context, context.getString(R.string.unable_to_connect_to_network));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(1);
                        } else if (response.equals(context.getString(R.string.error_Http_other))) {
                            controller.Toast_show(context, context.getString(R.string.facing_some_technical_issue));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(1);
                        } else if (response.equals(context.getString(R.string.error))) {
                            controller.Toast_show(context, context.getString(R.string.facing_some_technical_issue));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(1);
                        } else {
                            controller.Toast_show(context, response);
                            HideBookLocder(2);
                        }
                    } else if (callingUI.equals(context.getString(R.string.change_contact_send_otp_resend))) {
                        if (response.equals(context.getString(R.string.error_Http_not_found))) {
                            controller.Toast_show(context, context.getString(R.string.unable_to_connect_to_network));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(2);
                        } else if (response.equals(context.getString(R.string.error_Http_internal))) {
                            controller.Toast_show(context, context.getString(R.string.unable_to_connect_to_network));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(2);
                        } else if (response.equals(context.getString(R.string.error_Http_other))) {
                            controller.Toast_show(context, context.getString(R.string.facing_some_technical_issue));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(2);
                        } else if (response.equals(context.getString(R.string.error))) {
                            controller.Toast_show(context, context.getString(R.string.facing_some_technical_issue));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(3);
                        } else {
                            controller.Toast_show(context, response);
                            HideBookLocder(2);
                        }
                    } else if (callingUI.equals(context.getString(R.string.change_contact_number_api))) {
                        if (response.equals(context.getString(R.string.error_Http_not_found))) {
                            controller.Toast_show(context, context.getString(R.string.unable_to_connect_to_network));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(2);
                        } else if (response.equals(context.getString(R.string.error_Http_internal))) {
                            controller.Toast_show(context, context.getString(R.string.unable_to_connect_to_network));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(2);
                        } else if (response.equals(context.getString(R.string.error_Http_other))) {
                            controller.Toast_show(context, context.getString(R.string.facing_some_technical_issue));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(2);
                        } else if (response.equals(context.getString(R.string.error))) {
                            controller.Toast_show(context, context.getString(R.string.facing_some_technical_issue));
                            controller.LoadingStopDailog(bookLoading);
                            HideBookLocder(2);
                        } else {
                            controller.Toast_show(context, response);
                            dialogCallBack.clickDialogItem(data.get("phone_number").toString());
                        }
                    }
                }
            };
        } else if (type.equals(context.getString(R.string.search_dialog_layout))) {
            search_box.setAdapter(new searchBoxData(context, R.layout.list_item));
            search_box_type.setAdapter(new searchBoxType(context, R.layout.list_item));

            search_box.setHint(context.getString(R.string.Location));
            search_box_type.setHint(context.getString(R.string.what_you_are_looking_for));
            heading.setText(context.getString(R.string.search));
            send.setOnClickListener(this);
            cancel_txt.setOnClickListener(this);

            finalData = new HashMap<>();
            finalData.put("SearchBox","");
            finalData.put("SearchBoxType","");
            finalData.put("IdentityID","0");
            finalData.put("IdentityName","");
            finalData.put("IdentityValue","");
            finalData.put("CIdentityID","0");
            finalData.put("CIdentityName","");
            finalData.put("CIdentityValue","0");
            finalData.put("stype","9");
            finalData.put("pcmax","0");
            finalData.put("pcmin","0");
            finalData.put("cityid", Uri.encode("New Delhi"));

            search_box.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String data = String.valueOf(adapterView.getAdapter().getItem(i));

                    for (int i_search=0; i_search < dataSearchAPIGet.size(); i_search++){
                        if(data.equals(dataSearchAPIGet.get(i_search).get("IdentityValue").toString())){
                            finalData.put("IdentityID",dataSearchAPIGet.get(i_search).get("IdentityID").toString());
                            finalData.put("IdentityName",dataSearchAPIGet.get(i_search).get("IdentityName").toString());
                            finalData.put("IdentityValue",dataSearchAPIGet.get(i_search).get("IdentityValue").toString());
                            finalData.put("SearchBox",Uri.encode(data));
                            break;
                        }
                    }
                 }
            });

            search_box_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String data = String.valueOf(adapterView.getAdapter().getItem(i));
                    for (int i_search=0; i_search < dataSearchTypeAPIGet.size(); i_search++){
                        if(data.equals(dataSearchTypeAPIGet.get(i_search).get("IdentityValue").toString())){

                            finalData.put("CIdentityID",dataSearchTypeAPIGet.get(i_search).get("CIdentityID").toString());
                            finalData.put("CIdentityName",dataSearchTypeAPIGet.get(i_search).get("CIdentityName").toString());
                            finalData.put("CIdentityValue",dataSearchTypeAPIGet.get(i_search).get("CIdentityValue").toString());
                            finalData.put("SearchBoxType",Uri.encode(data));
                        }
                    }
                }
            });


            childApiCallback = new ChildApiCallback() {
                @Override
                public void Data_call_back(Object... args) {
                    String callingUI = (String) args[0];
                    String response = (String) args[1];

                    Log.e("Response","Responsesss"+response);
                    if (callingUI.equals(context.getString(R.string.search_box_api))) {
                        if (response.equals(context.getString(R.string.error_Http_not_found))) {
                        } else if (response.equals(context.getString(R.string.error_Http_internal))) {
                        } else if (response.equals(context.getString(R.string.error_Http_other))) {
                        } else if (response.equals(context.getString(R.string.error))) {
                        } else {
                            callSearchBoxResult(response);
                        }
                    }else if (callingUI.equals(context.getString(R.string.search_box_type_api))) {
                        if (response.equals(context.getString(R.string.error_Http_not_found))) {
                        } else if (response.equals(context.getString(R.string.error_Http_internal))) {
                        } else if (response.equals(context.getString(R.string.error_Http_other))) {
                        } else if (response.equals(context.getString(R.string.error))) {
                        } else {
                            callSearchBoxTypeResult(response);
                        }
                    }
                }
            };

            if (controller.InternetCheck(context)) {
                data_get = new HashMap<>();
                controller.centralizedApiHitting(context.getString(R.string.search_box_type_api), context.getString(R.string.get_type_api), context, childApiCallback, data_get);
            } else {
                controller.Toast_show(context, context.getString(R.string.enable_internet));
            }


        }

    }

    private void callSearchBoxResult(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            dataSearchAPIGet = new ArrayList<>();
            for (int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                dataSearchAPIGetSub = new HashMap<>();

                dataSearchAPIGetSub.put("IdentityID",jsonObject.getString("IdentityID"));
                dataSearchAPIGetSub.put("IdentityName",jsonObject.getString("IdentityName"));
                dataSearchAPIGetSub.put("IdentityValue",jsonObject.getString("IdentityValue"));
                dataSearchAPIGet.add(dataSearchAPIGetSub);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callSearchBoxTypeResult(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            dataSearchTypeAPIGet = new ArrayList<>();
            for (int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                dataSearchTypeAPIGetSUB = new HashMap<>();

                dataSearchTypeAPIGetSUB.put("CIdentityID",jsonObject.getString("CIdentityID"));
                dataSearchTypeAPIGetSUB.put("CIdentityName",jsonObject.getString("CIdentityName"));
                dataSearchTypeAPIGetSUB.put("CIdentityValue",jsonObject.getString("CIdentityValue"));
                dataSearchTypeAPIGet.add(dataSearchTypeAPIGetSUB);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    class searchBoxData extends ArrayAdapter<String> implements Filterable {

        public searchBoxData(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return dataSearchAPIGet.size();
        }

        @Override
        public String getItem(int index) {
            return dataSearchAPIGet.get(index).get("IdentityValue").toString();
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        autocomplete(constraint.toString());
                        filterResults.values = dataSearchAPIGet;
                        filterResults.count = dataSearchAPIGet.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }


    class searchBoxType extends ArrayAdapter<String> implements Filterable {

        public searchBoxType(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return dataTypeShow.size();
        }

        @Override
        public String getItem(int index) {
            return dataTypeShow.get(index).get("CIdentityName").toString();
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        dataTypeShow = new ArrayList<>();

                        for (int i=0; i < dataSearchTypeAPIGet.size();i++){
                            String text_get = dataSearchTypeAPIGet.get(i).get("CIdentityName").toString().toLowerCase();
                            if(text_get.contains(constraint)){
                                dataTypeShow.add(dataSearchTypeAPIGet.get(i));
                            }
                        }

                        filterResults.values = dataTypeShow;
                        filterResults.count = dataTypeShow.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };


            return filter;
        }
    }


    public void autocomplete(String input) {
        try {
            StringBuilder sb = new StringBuilder(Config.SEARCH_BOX_API);
            sb.append("?cnm=" + URLEncoder.encode(input, "utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (controller.InternetCheck(context)) {
            data_get = new HashMap<>();
            try {
                data_get.put("input", URLEncoder.encode(input, "utf8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            controller.centralizedApiHitting(context.getString(R.string.search_box_api), context.getString(R.string.get_type_api), context, childApiCallback, data_get);
        } else {
            controller.Toast_show(context, context.getString(R.string.enable_internet));
        }

    }


    private void getForgotPasswordOtpSendData(String response, int i) throws Exception {
        switch (i) {
            case 0:
                JSONArray otpSend = new JSONArray(response);
                JSONObject otpSendSub = otpSend.getJSONObject(0);
                String status = otpSendSub.getString("Status");
                String message = otpSendSub.getString("Message");

                if (status.equals(context.getString(R.string.status_ok))) {
                    HideBookLocder(2);
                } else {
                    HideBookLocder(1);
                    controller.Toast_show(context, message);
                }
                break;

            case 1:
                JSONArray otpResend = new JSONArray(response);
                JSONObject otpResendSub = otpResend.getJSONObject(0);
                String statusResend = otpResendSub.getString("Status");
                String messageResend = otpResendSub.getString("Message");

                if (statusResend.equals(context.getString(R.string.status_ok))) {
                    HideBookLocder(2);
                } else {
                    HideBookLocder(2);
                    controller.Toast_show(context, messageResend);
                }
                break;

            case 2:
                JSONArray passwordChange = new JSONArray(response);
                JSONObject passwordChangeSub = passwordChange.getJSONObject(0);
                String statusPasswordChange = passwordChangeSub.getString("Status");
                String messagePasswordChange = passwordChangeSub.getString("Message");

                if (statusPasswordChange.equals(context.getString(R.string.status_ok))) {
                    controller.Toast_show(context, messagePasswordChange);
                    customDialog.dismiss();
                } else {
                    HideBookLocder(3);
                    controller.Toast_show(context, messagePasswordChange);
                }
                break;

        }
    }

    public void DisableDialog() {
        if (customDialog != null)
            customDialog.dismiss();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_txt:
                if (type.equals(context.getString(R.string.otp_verify_screen))) {
                    dialogCallBack.clickDialogItem(context.getString(R.string.cancel));
                } else if (type.equals(context.getString(R.string.forgot_password_screen))) {
                    customDialog.dismiss();
                } else if (type.equals(context.getString(R.string.change_contact_view))) {
                    customDialog.dismiss();
                } else if (type.equals(context.getString(R.string.search_dialog_layout))) {
                    dialogCallBack.clickDialogItem(context.getString(R.string.cancel));
                }
                break;

            case R.id.send:
                if (type.equals(context.getString(R.string.otp_verify_screen))) {
                    if (controller.Check_all_empty(edit_txt)) {
                        edit_txt.setError(context.getString(R.string.empty_otp));
                    } else {
                        data = new HashMap<>();
                        data.put("phone_number", data_get.get("phone_number").toString());
                        data.put("otp_verify", edit_txt.getText().toString());

                        ShowBookLocder(0);
                        controller.LoadingStartDailog(bookLoading);
                        send.setText(context.getString(R.string.verify_otp));
                        controller.centralizedApiHitting(context.getString(R.string.otp_verify), context.getString(R.string.get_type_api), context, childApiCallback, data);
                    }

                } else if (type.equals(context.getString(R.string.forgot_password_screen))) {
                    if (controller.Check_all_empty(edit_txt)) {
                        if (send.getText().toString().equals(context.getString(R.string.send))) {
                            edit_txt.setError(context.getString(R.string.empty_otp));
                        } else if (send.getText().toString().equals(context.getString(R.string.proceed))) {
                            edit_txt.setError(context.getString(R.string.empty_phone_number));
                        } else if (send.getText().toString().equals(context.getString(R.string.change))) {
                            edit_txt.setError(context.getString(R.string.please_enter_new_password));
                        }
                    } else {
                        if (send.getText().toString().equals(context.getString(R.string.proceed))) {
                            data = new HashMap<>();
                            data.put("phone_number", edit_txt.getText().toString());

                            ShowBookLocder(1);
                            controller.LoadingStartDailog(bookLoading);
                            send.setText(context.getString(R.string.sending_otp));
                            controller.centralizedApiHitting(context.getString(R.string.forgot_otp_send), context.getString(R.string.get_type_api), context, childApiCallback, data);
                        } else if (send.getText().toString().equals(context.getString(R.string.send))) {
                            data.put("otp_verify", edit_txt.getText().toString());

                            ShowBookLocder(2);
                            controller.LoadingStartDailog(bookLoading);
                            send.setText(context.getString(R.string.verify_otp));
                            controller.centralizedApiHitting(context.getString(R.string.forgot_otp_verify), context.getString(R.string.get_type_api), context, childApiCallback, data);
                        } else if (send.getText().toString().equals(context.getString(R.string.change))) {
                            if (!controller.Password_length(edit_txt, 6, 20)) {
                                edit_txt.setError(context.getString(R.string.password_length));
                            } else if (controller.Check_all_empty(edit_txt_second)) {
                                edit_txt_second.setError(context.getString(R.string.empty_confirm_password));
                            } else if (!controller.Check_all_matches(edit_txt_second, edit_txt.getText().toString())) {
                                edit_txt_second.setError(context.getString(R.string.password_not_match));
                            } else {
                                data.put("session_id", session_id);
                                data.put("password", edit_txt.getText().toString());

                                ShowBookLocder(4);
                                controller.LoadingStartDailog(bookLoading);
                                controller.centralizedApiHitting(context.getString(R.string.change_password_api), context.getString(R.string.get_type_api), context, childApiCallback, data);
                            }
                        }
                    }
                } else if (type.equals(context.getString(R.string.change_contact_view))) {

                    if (send.getText().toString().equals(context.getString(R.string.proceed))) {
                        data = new HashMap<>();
                        data.put("session_id", data_get.get("session_id").toString());
                        data.put("phone_number", edit_txt.getText().toString());

                        ShowBookLocder(1);
                        controller.LoadingStartDailog(bookLoading);
                        send.setText(context.getString(R.string.sending_otp));
                        controller.centralizedApiHitting(context.getString(R.string.change_contact_send_otp_api), context.getString(R.string.get_type_api), context, childApiCallback, data);
                    } else if (send.getText().toString().equals(context.getString(R.string.send))) {
                        data.put("otp_verify", edit_txt.getText().toString());

                        ShowBookLocder(2);
                        controller.LoadingStartDailog(bookLoading);
                        send.setText(context.getString(R.string.verify_otp));
                        controller.centralizedApiHitting(context.getString(R.string.change_contact_number_api), context.getString(R.string.get_type_api), context, childApiCallback, data);
                    }
                } else if (type.equals(context.getString(R.string.search_dialog_layout))) {
                    dialogCallBack.clickDialogItem(context.getString(R.string.send),finalData);
                }
                break;

            case R.id.change_phone_number:
                HideBookLocder(1);
                break;

            case R.id.resend:
                if (type.equals(context.getString(R.string.forgot_password_screen))) {
                    ShowBookLocder(3);
                    controller.centralizedApiHitting(context.getString(R.string.forgot_otp_resend), context.getString(R.string.get_type_api), context, childApiCallback, data);
                } else if (type.equals(context.getString(R.string.change_contact_view))) {
                    ShowBookLocder(3);
                    controller.centralizedApiHitting(context.getString(R.string.change_contact_send_otp_resend), context.getString(R.string.get_type_api), context, childApiCallback, data);
                }
                break;
        }
    }

    public void ShowBookLocder(int type) {
        switch (type) {
            case 0:
                edit_txt.setEnabled(false);
                edit_txt.setVisibility(View.GONE);
                bookLoading.setVisibility(View.VISIBLE);

                resend.setClickable(false);
                send.setClickable(false);
                cancel_txt.setClickable(false);
                cancel_txt.setVisibility(View.GONE);
                break;

            case 1:
                edit_txt.setEnabled(false);
                edit_txt.setVisibility(View.GONE);
                edit_txt_second.setVisibility(View.GONE);
                bookLoading.setVisibility(View.VISIBLE);

                send.setClickable(false);
                cancel_txt.setClickable(false);
                cancel_txt.setVisibility(View.GONE);
                send.setText(context.getString(R.string.sending_otp));
                break;

            case 2:
                edit_txt.setEnabled(false);
                edit_txt.setVisibility(View.GONE);
                edit_txt_second.setVisibility(View.GONE);
                change_phone_number.setVisibility(View.GONE);
                resend.setVisibility(View.GONE);
                bookLoading.setVisibility(View.VISIBLE);

                send.setClickable(false);
                cancel_txt.setClickable(false);
                cancel_txt.setVisibility(View.GONE);
                send.setText(context.getString(R.string.verify_otp));
                break;

            case 3:
                edit_txt.setEnabled(false);
                edit_txt.setVisibility(View.GONE);
                edit_txt_second.setVisibility(View.GONE);
                change_phone_number.setVisibility(View.GONE);
                resend.setVisibility(View.GONE);
                bookLoading.setVisibility(View.VISIBLE);

                send.setClickable(false);
                cancel_txt.setClickable(false);
                cancel_txt.setVisibility(View.GONE);
                send.setText(context.getString(R.string.resending_otp));
                break;

            case 4:
                edit_txt.setEnabled(false);
                edit_txt_second.setEnabled(false);
                edit_txt.setVisibility(View.GONE);
                edit_txt_second.setVisibility(View.GONE);
                change_phone_number.setVisibility(View.GONE);
                resend.setVisibility(View.GONE);
                bookLoading.setVisibility(View.VISIBLE);

                send.setClickable(false);
                cancel_txt.setClickable(false);
                cancel_txt.setVisibility(View.GONE);
                send.setText(context.getString(R.string.changing_password));
                break;

        }
    }

    public void HideBookLocder(int type) {
        switch (type) {
            case 0:
                edit_txt.setEnabled(true);
                edit_txt.setVisibility(View.VISIBLE);
                bookLoading.setVisibility(View.GONE);

                resend.setClickable(true);
                send.setClickable(true);
                cancel_txt.setClickable(true);
                cancel_txt.setVisibility(View.VISIBLE);
                send.setText(context.getString(R.string.send));
                break;

            case 1:
                edit_txt.setEnabled(true);
                edit_txt.setVisibility(View.VISIBLE);
                edit_txt_second.setVisibility(View.GONE);
                change_phone_number.setVisibility(View.GONE);
                resend.setVisibility(View.GONE);
                bookLoading.setVisibility(View.GONE);

                if (data != null) {
                    if (data.size() > 0) {
                        edit_txt.setText(data.get("phone_number").toString());
                    } else {
                        edit_txt.setText("");
                    }
                } else {
                    edit_txt.setText("");
                }

                heading.setText(context.getString(R.string.forgot_password));
                edit_txt.setHint(context.getString(R.string.please_enter_phone_number));

                send.setClickable(true);
                cancel_txt.setClickable(true);
                cancel_txt.setVisibility(View.VISIBLE);
                send.setText(context.getString(R.string.proceed));
                break;

            case 2:
                edit_txt.setEnabled(true);
                edit_txt.setVisibility(View.VISIBLE);
                edit_txt_second.setVisibility(View.GONE);
                change_phone_number.setVisibility(View.VISIBLE);
                resend.setVisibility(View.VISIBLE);
                bookLoading.setVisibility(View.GONE);

                heading.setText(context.getString(R.string.otp_verify));
                edit_txt.setHint(context.getString(R.string.please_enter_otp));

                edit_txt.setText("");
                send.setClickable(true);
                cancel_txt.setClickable(true);
                cancel_txt.setVisibility(View.VISIBLE);
                send.setText(context.getString(R.string.send));
                break;

            case 3:
                edit_txt.setEnabled(true);
                edit_txt_second.setEnabled(true);
                edit_txt.setVisibility(View.VISIBLE);
                edit_txt_second.setVisibility(View.VISIBLE);
                change_phone_number.setVisibility(View.GONE);
                resend.setVisibility(View.GONE);
                bookLoading.setVisibility(View.GONE);

                heading.setText(context.getString(R.string.change_password));
                edit_txt.setHint(context.getString(R.string.enter_new_password));
                edit_txt_second.setHint(context.getString(R.string.confirm_change_new_password));

                edit_txt.setText("");
                send.setClickable(true);
                cancel_txt.setClickable(true);
                cancel_txt.setVisibility(View.VISIBLE);
                send.setText(context.getString(R.string.change));
                break;

        }
    }
}