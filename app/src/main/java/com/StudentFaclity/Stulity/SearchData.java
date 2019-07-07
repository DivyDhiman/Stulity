package com.StudentFaclity.Stulity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.victor.loading.newton.NewtonCradleLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import Adapter_all.Adapter_all_view;
import Call_Back.AlertDialogCallBack;
import Call_Back.DialogCallBack;
import Call_Back.MyCallbacks;
import Call_Back.ParentApiCallback;
import Call_Back.RecyclerViewCallBack;
import Controllers.Config;
import Controllers.Controller;
import Fragement_all.ApplyFilterFragment;
import Fragement_all.Profile;

/**
 * Created by Abhay0648 on 22-05-2017.
 */

@SuppressWarnings("ALL")
public class SearchData extends RuntimePermission implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Context context;
    private Controller controller;
    private HashMap<String, Object> data_pass;
    private ImageView menu_btn, appIcon, searchDashboard, closeSearch, search_result,filter_btn;
    private ArrayList<HashMap<String, Object>> data, searchData;
    private HashMap<String, Object> dataSub, dataGet, dataSend, searchDataSub, finalData;
    private ParentApiCallback parentApiCallback;
    private NewtonCradleLoading newtonCradleLoading;
    private String sessionId, currentCity, serachBoxData, classType;
    private RecyclerView listContent, search_content;
    private Adapter_all_view adapterAllView, adapterSearchView;
    private MyCallbacks myCallbacks;
    private LocationManager manager;
    private AlertDialogCallBack alertDialogCallback;
    private boolean get_location_onlocation_change, check_location;
    protected String mLastUpdateTime;
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected Location mCurrentLocation;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";
    private boolean checkLocationGetReady = false;
    private Handler handler;
    private EditText searchEt;
    private LinearLayout parentSearch;
    private boolean isSearch = false;
    private RecyclerViewCallBack recyclerViewCallBack;
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_data);

        context = SearchData.this;
        controller = new Controller();
        controller.initSharedPreference(context);

        get_location_onlocation_change = true;
        check_location = false;
        mLastUpdateTime = "";
        updateValuesFromBundle(savedInstanceState);

        controller.initSharedPreference(context);
        sessionId = controller.getString(getString(R.string.session_id));
        classType = getIntent().getExtras().getString("class_type");
        currentCity = controller.getString(getString(R.string.current_city));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        set_action_bar();

        callBacks();
    }

    private void callBacks() {

        if (classType.equals(getString(R.string.search))) {
            finalData = new HashMap<>();
            finalData.put("SearchBox", "");
            finalData.put("SearchBoxType", "");
            finalData.put("IdentityID", "0");
            finalData.put("IdentityName", "");
            finalData.put("IdentityValue", "");
            finalData.put("CIdentityID", "0");
            finalData.put("CIdentityName", "");
            finalData.put("CIdentityValue", "0");
            finalData.put("stype", "9");
            finalData.put("pcmax", "0");
            finalData.put("pcmin", "0");
            finalData.put("cityid", Uri.encode("New Delhi"));
        }

        parentApiCallback = new ParentApiCallback() {
            @Override
            public void Data_call_back(Object... args) {
                String callingUI = (String) args[0];
                String response = (String) args[1];

                Log.e("Response", "Response" + response);
                Log.e("callingUI", "callingUI" + callingUI);

                if (callingUI.equals(getString(R.string.search_box_result_api))) {
                    if (response.equals(getString(R.string.error_Http_not_found))) {
                        controller.Toast_show(context, getString(R.string.unable_to_connect_to_network));
                    } else if (response.equals(getString(R.string.error_Http_internal))) {
                        controller.Toast_show(context, getString(R.string.unable_to_connect_to_network));
                    } else if (response.equals(getString(R.string.error_Http_other))) {
                        controller.Toast_show(context, getString(R.string.facing_some_technical_issue));
                    } else if (response.equals(getString(R.string.error))) {
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
                } else if (callingUI.equals(context.getString(R.string.search_box_api))) {
                    if (response.equals(context.getString(R.string.error_Http_not_found))) {
                    } else if (response.equals(context.getString(R.string.error_Http_internal))) {
                    } else if (response.equals(context.getString(R.string.error_Http_other))) {
                    } else if (response.equals(context.getString(R.string.error))) {
                    } else {
                        callSearchBoxResult(response);
                    }
                }
            }
        };

        alertDialogCallback = new AlertDialogCallBack() {
            @Override
            public void dialogCallback(Object... args) {
                String typeGet = (String) args[0];
                if (typeGet.equals(getString(R.string.negative_click))) {
                    if (currentCity.equals(Config.SHARED_PREFRENCE_NO_DATA_KEY_STRING)) {
                        controller.Toast_show(context, getString(R.string.need_city_location));
                        DialogInterface dialogInterface = (DialogInterface) args[1];
                        dialogInterface.cancel();
                        finish();
                    } else {
                        DialogInterface dialogInterface = (DialogInterface) args[1];
                        dialogInterface.cancel();
                    }
                } else if (typeGet.equals(getString(R.string.positive_click))) {
                    DialogInterface dialogInterface = (DialogInterface) args[1];
                    dialogInterface.cancel();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            }
        };


        recyclerViewCallBack = new RecyclerViewCallBack() {
            @Override
            public void recyclerViewCallBack(Object... args) {
                HashMap<String, Object> dataHashMap = (HashMap<String, Object>) args[0];
                finalData.put("SearchBox", serachBoxData);
                finalData.put("IdentityID", dataHashMap.get("IdentityID").toString());
                finalData.put("IdentityName", dataHashMap.get("IdentityName").toString());
                finalData.put("IdentityValue", dataHashMap.get("IdentityValue").toString());
                finalData.put("cityid", Uri.encode("New Delhi"));
            }
        };

        myCallbacks = new MyCallbacks() {
            @Override
            public void getPermissionValue(String permissionData) {
                if (permissionData.equals(getString(R.string.allow))) {
                        call_location();
                    currentCity = controller.getString(getString(R.string.current_city));

                    if (currentCity.equals(Config.SHARED_PREFRENCE_NO_DATA_KEY_STRING)) {
                        if (!manager.isProviderEnabled((LocationManager.GPS_PROVIDER))) {
                            controller.alertDialogShow(context, getString(R.string.enable_gps), getString(R.string.please_enable_gps)
                                    , getString(R.string.settings), getString(R.string.cancel), alertDialogCallback);
                        }else {
                            controller.Toast_show(context, getString(R.string.waiting_for_your_location));
                        }
                    } else {
                        controller.customDialogDisable();
                        HitSearchAPI();
                    }
                }
            }
        };

        initialise();
    }

    private void initialise() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawerLayout);
        listContent = (RecyclerView) findViewById(R.id.list_content);
        search_content = (RecyclerView) findViewById(R.id.search_content);
        newtonCradleLoading = (NewtonCradleLoading) findViewById(R.id.newton_cradle_loading);
        newtonCradleLoading.setLoadingColor(R.color.button_bg);

        data = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(listContent.getContext(), LinearLayoutManager.VERTICAL, false);
        listContent.setLayoutManager(linearLayoutManager);
        adapterAllView = new Adapter_all_view(context, data, R.layout.ad_adapter_view, getString(R.string.ad_adapter_view));
        listContent.setAdapter(adapterAllView);

        searchData = new ArrayList<>();
        LinearLayoutManager linearLayoutManagerSearch = new LinearLayoutManager(search_content.getContext(), LinearLayoutManager.VERTICAL, false);
        search_content.setLayoutManager(linearLayoutManagerSearch);
        adapterSearchView = new Adapter_all_view(context, searchData, R.layout.list_item, getString(R.string.search_adapter_view), recyclerViewCallBack);
        search_content.setAdapter(adapterSearchView);

        if (classType.equals(getString(R.string.search))) {
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Show_key_board();
                }
            }, 100);

            searchVisible();
        }
    }


    private void call_location() {
        get_location_onlocation_change = true;
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        checkLocationGetReady = true;
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

        if (get_location_onlocation_change) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

            currentCity = getCompleteAddressString(latitude, longitude);
            controller.saveString(getString(R.string.current_city), currentCity);
            get_location_onlocation_change = false;
        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String city;
        Geocoder gcd = new Geocoder(context, Locale.ENGLISH);
        try {
            List<Address> addresses = gcd.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                if (addresses.size() > 0) {
                    if (addresses.get(0).getLocality() != null) {
                        city = addresses.get(0).getLocality();
                    } else {
                        if (addresses.get(0).getSubAdminArea() != null) {
                            city = addresses.get(0).getSubAdminArea();
                        } else {
                            city = "new delhi";
                        }
                    }
                } else {
                    city = "new delhi";
                }
            } else {
                city = "new delhi";
            }
        } catch (IOException e) {
            city = "new delhi";
            e.printStackTrace();
        }
        return city;
    }


    //Update location from google fused api
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }
            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            }
        }
    }

    //synchronized google fused location api
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    //create location request
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }


    @Override
    public void onConnected(Bundle bundle) {

        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            check_location = true;
        }
        startLocationUpdates();

    }

    //check connection suspended
    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    //Location update
    protected void startLocationUpdates() {
        if (check_location) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    //location update close when activity closed
    protected void stopLocationUpdates() {
        if (check_location) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onResume() {
        super.onResume();
        if (checkLocationGetReady) {
            if (mGoogleApiClient.isConnected()) {
                startLocationUpdates();
            }
        }
    }

    //when activity goes on pause
    @Override
    protected void onPause() {

        if (checkLocationGetReady) {
            if (mGoogleApiClient.isConnected()) {
                stopLocationUpdates();
            }
        }
        super.onPause();
    }


    //when activity stops
    @Override
    protected void onStop() {
        if (checkLocationGetReady) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }


    private void HitSearchAPI() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Hide_keyboard();
            }
        }, 100);
        searchInvisible();
        controller.centralizedApiHitting(getString(R.string.search_box_result_api), getString(R.string.get_type_api), context, parentApiCallback, finalData);
        controller.customDialogDisable();
    }


    private void callSearchBoxResult(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            searchData = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                searchDataSub = new HashMap<>();

                searchDataSub.put("IdentityID", jsonObject.getString("IdentityID"));
                searchDataSub.put("IdentityName", jsonObject.getString("IdentityName"));
                searchDataSub.put("IdentityValue", jsonObject.getString("IdentityValue"));
                searchData.add(searchDataSub);
            }

            adapterSearchView.updateData(searchData);
            adapterSearchView.notifyDataSetChanged();
            search_content.setVisibility(View.VISIBLE);
            listContent.setVisibility(View.GONE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getData(String response) throws Exception {
        JSONArray adList = new JSONArray(response);

        if (adList.length() > 0) {
            data = new ArrayList<>();
            for (int i = 0; i < adList.length(); i++) {
                JSONObject adListSub = adList.getJSONObject(i);
                dataSub = new HashMap<>();
                dataSub.put("Adid", adListSub.getString("Adid"));
                dataSub.put("Image", adListSub.getString("Image"));
                dataSub.put("Price", adListSub.getString("Price"));
                dataSub.put("CatName", adListSub.getString("CatName"));
                dataSub.put("Company", adListSub.getString("Company"));
                dataSub.put("Rating", adListSub.getString("Rating"));
                dataSub.put("RatingUsers", adListSub.getString("RatingUsers"));
                dataSub.put("Location", adListSub.getString("Location"));
                dataSub.put("TimePassed", adListSub.getString("TimePassed"));
                dataSub.put("AdViews", adListSub.getString("AdViews"));
                data.add(dataSub);
            }

            controller.LoadingStop(newtonCradleLoading);
            newtonCradleLoading.setVisibility(View.GONE);
            adapterAllView.updateData(data);
            adapterAllView.notifyDataSetChanged();
            search_content.setVisibility(View.GONE);
            listContent.setVisibility(View.VISIBLE);

        } else {
            controller.LoadingStop(newtonCradleLoading);
        }
    }


    private void set_action_bar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        menu_btn = (ImageView) toolbar.findViewById(R.id.menu_btn);
        appIcon = (ImageView) toolbar.findViewById(R.id.app_icon);
        searchDashboard = (ImageView) toolbar.findViewById(R.id.search_dashboard);
        parentSearch = (LinearLayout) toolbar.findViewById(R.id.parent_search);
        searchEt = (EditText) toolbar.findViewById(R.id.search_et);
        closeSearch = (ImageView) toolbar.findViewById(R.id.close_search);
        search_result = (ImageView) toolbar.findViewById(R.id.search_result);
        filter_btn = (ImageView) toolbar.findViewById(R.id.filter_btn);

        filter_btn.setVisibility(View.VISIBLE);

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                serachBoxData = String.valueOf(charSequence);
                if (controller.InternetCheck(context)) {
                    HashMap<String, Object> data_get = new HashMap<>();
                    try {
                        data_get.put("input", URLEncoder.encode(serachBoxData, "utf8"));
                        controller.centralizedApiHitting(context.getString(R.string.search_box_api), context.getString(R.string.get_type_api), context, parentApiCallback, data_get);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    controller.Toast_show(context, context.getString(R.string.enable_internet));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        menu_btn.setOnClickListener(this);
        searchDashboard.setOnClickListener(this);
        closeSearch.setOnClickListener(this);
        search_result.setOnClickListener(this);
        filter_btn.setOnClickListener(this);

        replace_fragment();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_btn:
                onBackPressed();
                break;

            case R.id.search_dashboard:
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Show_key_board();
                    }
                }, 100);

                searchVisible();
                break;

            case R.id.close_search:
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Hide_keyboard();
                    }
                }, 100);
                searchInvisible();
                break;

            case R.id.search_result:
                if (Current_Build_version() >= Build.VERSION_CODES.LOLLIPOP) {
                    check_permissions(context, myCallbacks, Manifest.permission.ACCESS_COARSE_LOCATION, getString(R.string.allow), 24);
                } else {
                    if (!manager.isProviderEnabled((LocationManager.GPS_PROVIDER))) {
                        controller.alertDialogShow(context, getString(R.string.enable_gps), getString(R.string.please_enable_gps)
                                , getString(R.string.settings), getString(R.string.cancel), alertDialogCallback);
                    } else {
                        call_location();
                        currentCity = controller.getString(getString(R.string.current_city));
                        if (currentCity.equals(Config.SHARED_PREFRENCE_NO_DATA_KEY_STRING)) {
                            controller.Toast_show(context, getString(R.string.waiting_for_your_location));
                        } else {
                            controller.customDialogDisable();
                            HitSearchAPI();
                        }
                    }
                }

                break;

            case R.id.filter_btn:
                mDrawerLayout.openDrawer(Gravity.RIGHT);
                break;

        }
    }

    public void Hide_keyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchEt.getWindowToken(), 0);
    }

    public void Show_key_board() {
        InputMethodManager inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(0, 0);
    }

    private void searchVisible() {
        menu_btn.setVisibility(View.GONE);
        appIcon.setVisibility(View.GONE);
        searchDashboard.setVisibility(View.GONE);
        parentSearch.setVisibility(View.VISIBLE);
        search_content.setVisibility(View.VISIBLE);
        listContent.setVisibility(View.GONE);
        filter_btn.setVisibility(View.GONE);
    }

    private void searchInvisible() {
        menu_btn.setVisibility(View.VISIBLE);
        appIcon.setVisibility(View.VISIBLE);
        searchDashboard.setVisibility(View.VISIBLE);
        parentSearch.setVisibility(View.GONE);
        search_content.setVisibility(View.GONE);
        listContent.setVisibility(View.VISIBLE);
        filter_btn.setVisibility(View.VISIBLE);
    }


    private void replace_fragment() {
        ApplyFilterFragment applyfilterFragment = new ApplyFilterFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.replace_fragment, applyfilterFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            finish();
            controller.Animation_backward(context);
        }
    }

}