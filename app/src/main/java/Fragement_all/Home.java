package Fragement_all;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class Home extends Fragment {
    private View root_view;
    private Context context;
    private Controller controller;
    private RecyclerView dash_board_list;
    private NewtonCradleLoading newtonCradleLoading;
    private ParentApiCallback parentApiCallback;
    private ArrayList<HashMap<String, Object>> data;
    private HashMap<String, Object> dataSub, data_send;
    private Adapter_all_view adapterAllView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root_view = inflater.inflate(R.layout.home, container, false);
        context = root_view.getContext();
        controller = new Controller();

        callBacks();
        return root_view;
    }

    private void callBacks() {
        parentApiCallback = new ParentApiCallback() {
            @Override
            public void Data_call_back(Object... args) {
                String callingUI = (String) args[0];
                String response = (String) args[1];

                Log.e("response","Response"+response);

                if (callingUI.equals(getString(R.string.dash_board_api))) {
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
                            controller.Toast_show(context,getString(R.string.oops_something_went_wrong));
                            controller.LoadingStop(newtonCradleLoading);
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        initialise_view(root_view);
    }

    private void initialise_view(View root_view) {
        dash_board_list = (RecyclerView) root_view.findViewById(R.id.dash_board_list);
        newtonCradleLoading = (NewtonCradleLoading) root_view.findViewById(R.id.newton_cradle_loading);

        data = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(dash_board_list.getContext(), LinearLayoutManager.VERTICAL, false);
        dash_board_list.setLayoutManager(linearLayoutManager);
        adapterAllView = new Adapter_all_view(context, data, R.layout.home_adapter_view, getString(R.string.home_adapter_view));
        dash_board_list.setAdapter(adapterAllView);

        if (controller.InternetCheck(context)) {
            newtonCradleLoading.setLoadingColor(R.color.button_bg);
            controller.LoadingStart(newtonCradleLoading);
            data_send = new HashMap<>();
            controller.centralizedApiHitting(getString(R.string.dash_board_api), getString(R.string.get_type_api), context, parentApiCallback, data_send);
        } else {
            controller.Toast_show(context, getString(R.string.enable_internet));
        }
    }


    private void getData(String response) throws Exception {
        JSONArray jsonArray = new JSONArray(response);
        if (jsonArray.length() > 0) {
            data = new ArrayList<>();
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONArray jsonArraySub;
            jsonArraySub = jsonObject.getJSONArray("Locations");
            dataSub = new HashMap<>();
            dataSub.put("parent_txt", getString(R.string.offer_by_location));
            dataSub.put("parent_size", String.valueOf(jsonArraySub.length()));
            dataSub.put("data", jsonArraySub.toString());
            data.add(dataSub);

            jsonArraySub = jsonObject.getJSONArray("Categories");
            dataSub = new HashMap<>();
            dataSub.put("parent_txt", getString(R.string.offer_by_category));
            dataSub.put("parent_size", String.valueOf(jsonArraySub.length()));
            dataSub.put("data", jsonArraySub.toString());
            data.add(dataSub);

            jsonArraySub = jsonObject.getJSONArray("LatestListings");
            dataSub = new HashMap<>();
            dataSub.put("parent_txt", getString(R.string.list_item));
            dataSub.put("parent_size", String.valueOf(jsonArraySub.length()));
            dataSub.put("data", jsonArraySub.toString());
            data.add(dataSub);

            controller.LoadingStop(newtonCradleLoading);
            adapterAllView.updateData(data);
            adapterAllView.notifyDataSetChanged();
        } else {
            controller.Toast_show(context, getString(R.string.no_item));
        }
    }
}