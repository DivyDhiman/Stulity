package com.StudentFaclity.Stulity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.victor.loading.newton.NewtonCradleLoading;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Adapter_all.Adapter_all_view;
import Call_Back.ParentApiCallback;
import Controllers.Controller;

/**
 * Created by Abhay0648 on 21-05-2017.
 */

public class CentralizedRecyclerView extends AppCompatActivity {
    private Context context;
    private Controller controller;
    private NewtonCradleLoading newtonCradleLoading;
    private Handler handler;
    private ImageView menu_btn, appIcon;
    private RecyclerView listContent;
    private String class_type,sessionId;
    private ParentApiCallback parentApiCallback;
    private ArrayList<HashMap<String,Object>> data;
    private HashMap<String,Object> dataSub,dataSend;
    private Adapter_all_view adapterAllView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.centralized_recycler_view);

        context = CentralizedRecyclerView.this;
        controller = new Controller();
        controller.initSharedPreference(context);

        class_type = getIntent().getExtras().getString("class_type");
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

                if (callingUI.equals(getString(R.string.ad_list_api))) {
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
                            getData(response);
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
        listContent = (RecyclerView) findViewById(R.id.list_content);

        data = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(listContent.getContext(),LinearLayoutManager.VERTICAL,false);
        listContent.setLayoutManager(linearLayoutManager);
        adapterAllView = new Adapter_all_view(context,data,R.layout.ad_adapter_view,getString(R.string.ad_adapter_view));
        listContent.setAdapter(adapterAllView);

        if (controller.InternetCheck(context)) {
            dataSend = new HashMap<>();
            dataSend.put("session_id", sessionId);
            controller.centralizedApiHitting(getString(R.string.ad_list_api), getString(R.string.get_type_api), context, parentApiCallback, data);
        } else {
            controller.Toast_show(context, getString(R.string.enable_internet));
        }
    }

    private void getData(String response) throws Exception {
        JSONArray adList = new JSONArray(response);

        if(adList.length() >0){
            for (int i =0; i < adList.length() ;i++){
                JSONObject adListSub = adList.getJSONObject(i);
                dataSub = new HashMap<>();

                dataSub.put("Adid",adListSub.getString("Adid"));
                dataSub.put("Image",adListSub.getString("Image"));
                dataSub.put("Price",adListSub.getString("Price"));
                dataSub.put("CatName",adListSub.getString("CatName"));
                dataSub.put("Company",adListSub.getString("Company"));
                dataSub.put("Rating",adListSub.getString("Rating"));
                dataSub.put("RatingUsers",adListSub.getString("RatingUsers"));
                dataSub.put("Location",adListSub.getString("Location"));
                dataSub.put("TimePassed",adListSub.getString("TimePassed"));
                dataSub.put("AdViews",adListSub.getString("AdViews"));
                data.add(dataSub);
            }

            adapterAllView.updateData(data);
            adapterAllView.notifyDataSetChanged();
        }

    }

    private void set_action_bar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        menu_btn = (ImageView) toolbar.findViewById(R.id.menu_btn);
        appIcon = (ImageView) toolbar.findViewById(R.id.app_icon);
        ImageView search_dashboard = (ImageView) toolbar.findViewById(R.id.search_dashboard);

        search_dashboard.setVisibility(View.GONE);
        menu_btn.setBackgroundResource(R.drawable.close_floating_btn);

        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        controller.Animation_backward(context);
    }
}
