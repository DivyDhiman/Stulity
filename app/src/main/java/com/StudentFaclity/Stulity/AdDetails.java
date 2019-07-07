package com.StudentFaclity.Stulity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.victor.loading.newton.NewtonCradleLoading;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import Call_Back.ParentApiCallback;
import Controllers.Config;
import Controllers.Controller;

public class AdDetails extends AppCompatActivity {
    private Context context;
    private Controller controller;
    private ImageView menu_btn, appIcon,image_view;
    private TextView views,price, category_name, rating, rating_by_user,location,company,time;
    private HashMap<String,Object> dataSub,dataSend;
    private String adId,sessionId;
    private ParentApiCallback parentApiCallback;
    private NewtonCradleLoading newtonCradleLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_deatils);

        context = AdDetails.this;
        controller = new Controller();
        controller.initSharedPreference(context);

        adId = getIntent().getExtras().getString("adId");
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

                if (callingUI.equals(getString(R.string.get_ad_details_api))) {
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
        image_view = (ImageView) findViewById(R.id.image_view);
        views = (TextView) findViewById(R.id.views);
        price = (TextView) findViewById(R.id.price);
        category_name = (TextView) findViewById(R.id.category_name);
        rating = (TextView) findViewById(R.id.rating);
        rating_by_user = (TextView) findViewById(R.id.rating_by_user);
        location = (TextView) findViewById(R.id.location);
        company = (TextView) findViewById(R.id.company);
        time = (TextView) findViewById(R.id.time);
        newtonCradleLoading = (NewtonCradleLoading) findViewById(R.id.newton_cradle_loading);

        if (controller.InternetCheck(context)) {
            controller.LoadingStart(newtonCradleLoading);
            dataSend = new HashMap<>();
            dataSend.put("adid", adId);
            controller.centralizedApiHitting(getString(R.string.get_ad_details_api), getString(R.string.get_type_api), context, parentApiCallback, dataSend);
        } else {
            controller.Toast_show(context, getString(R.string.enable_internet));
        }
    }


    private void getData(String response) throws Exception {
        JSONArray adList = new JSONArray(response);

        if(adList.length() >0){
                JSONObject adListSub = adList.getJSONObject(0);
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

            controller.LoadingStop(newtonCradleLoading);
            newtonCradleLoading.setVisibility(View.GONE);
            setData();
        }else {
            controller.LoadingStop(newtonCradleLoading);
            controller.Toast_show(context, getString(R.string.oops_something_went_wrong));
        }
    }

    private void setData() {
        Glide.with(context)
                .load(Config.AD_IMAGE_BASE_URL+dataSub.get("Image").toString())
                .centerCrop()
                .into(image_view);

        views.setText(dataSub.get("AdViews").toString());
        price.setText(context.getString(R.string.price)+dataSub.get("Price").toString());
        category_name.setText(context.getString(R.string.category)+dataSub.get("CatName").toString());
        rating.setText(context.getString(R.string.rating)+dataSub.get("Rating").toString()+"/10");
        rating_by_user.setText(context.getString(R.string.rating_by_user)+dataSub.get("RatingUsers").toString()+"/10");
        location.setText(dataSub.get("Location").toString());
        company.setText(dataSub.get("Company").toString());
        time.setText(dataSub.get("TimePassed").toString());
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
