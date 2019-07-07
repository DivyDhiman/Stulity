package Fragement_all;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;


import com.StudentFaclity.Stulity.CentralizedRecyclerView;
import com.StudentFaclity.Stulity.R;
import com.victor.loading.newton.NewtonCradleLoading;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Adapter_all.Adapter_all_view;
import Call_Back.ParentApiCallback;
import Controllers.Controller;

/**
 * Created by Abhay0648 on 22-01-2017.
 */

public class MyPostedAd extends Fragment {
    private View root_view;
    private Context context;
    private Controller controller;
    private NewtonCradleLoading newtonCradleLoading;
    private Handler handler;
    private ImageView menu_btn, appIcon;
    private RecyclerView listContent;
    private String sessionId;
    private ParentApiCallback parentApiCallback;
    private ArrayList<HashMap<String,Object>> data;
    private HashMap<String,Object> dataSub,dataSend;
    private Adapter_all_view adapterAllView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root_view = inflater.inflate(R.layout.my_posted_ad, container, false);
        context = root_view.getContext();
        controller = new Controller();
        controller.initSharedPreference(context);

        sessionId = controller.getString(getString(R.string.session_id));

        callBacks();

        return root_view;
    }

    private void callBacks() {

        parentApiCallback = new ParentApiCallback() {
            @Override
            public void Data_call_back(Object... args) {
                String callingUI = (String) args[0];
                String response = (String) args[1];

                if (callingUI.equals(getString(R.string.ad_list_api))) {
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
                            getData(response);
                        } catch (Exception e) {
                            controller.LoadingStop(newtonCradleLoading);
                            controller.Toast_show(context, getString(R.string.facing_some_technical_issue));
                            e.printStackTrace();
                        }
                    }
                }
            }
        };


        initialise_view(root_view);
    }

    private void initialise_view(View root_view) {
        listContent = (RecyclerView)root_view.findViewById(R.id.list_content);
        newtonCradleLoading = (NewtonCradleLoading) root_view.findViewById(R.id.newton_cradle_loading);
        newtonCradleLoading.setLoadingColor(R.color.button_bg);

        data = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(listContent.getContext(),LinearLayoutManager.VERTICAL,false);
        listContent.setLayoutManager(linearLayoutManager);
        adapterAllView = new Adapter_all_view(context,data,R.layout.ad_adapter_view,getString(R.string.ad_adapter_view));
        listContent.setAdapter(adapterAllView);

        if (controller.InternetCheck(context)) {
            controller.LoadingStart(newtonCradleLoading);
            dataSend = new HashMap<>();
            dataSend.put("session_id", sessionId);
            controller.centralizedApiHitting(getString(R.string.ad_list_api), getString(R.string.get_type_api), context, parentApiCallback, dataSend);
        } else {
            controller.Toast_show(context, getString(R.string.enable_internet));
        }
    }

    private void getData(String response) throws Exception {
        JSONArray adList = new JSONArray(response);

        if(adList.length() >0){
            data = new ArrayList<>();
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

            controller.LoadingStop(newtonCradleLoading);
            newtonCradleLoading.setVisibility(View.GONE);
            adapterAllView.updateData(data);
            adapterAllView.notifyDataSetChanged();
        }else {
            controller.LoadingStop(newtonCradleLoading);
        }
    }

}
