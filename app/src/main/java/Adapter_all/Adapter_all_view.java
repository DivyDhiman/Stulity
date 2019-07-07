package Adapter_all;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.StudentFaclity.Stulity.AdDetails;
import com.StudentFaclity.Stulity.R;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Call_Back.RecyclerViewCallBack;
import Controllers.Config;
import Controllers.Controller;

public class Adapter_all_view extends RecyclerView.Adapter<Adapter_all_view.ViewHolder> {

    private Context context;
    private ArrayList<HashMap<String, Object>> data, dataChild;
    private HashMap<String, Object> data_sub, dataChildSub;
    private ArrayList<String> click_data = new ArrayList<>();
    private Controller controller;
    private String type;
    private int layout_pass;
    private Intent intent;
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private RecyclerViewCallBack recyclerViewCallBack;

    public Adapter_all_view(Context context, ArrayList<HashMap<String, Object>> data, int layout_pass, String type) {
        this.context = context;
        this.data = data;
        this.type = type;
        this.layout_pass = layout_pass;
    }

    public Adapter_all_view(Context context, ArrayList<HashMap<String, Object>> data, int layout_pass, String type,RecyclerViewCallBack recyclerViewCallBack) {
        this.context = context;
        this.data = data;
        this.type = type;
        this.layout_pass = layout_pass;
        this.recyclerViewCallBack = recyclerViewCallBack;
    }

    public void updateData(ArrayList<HashMap<String, Object>> dataUpdate) {
        data = dataUpdate;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(layout_pass, parent, false);
        controller = new Controller();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        data_sub = data.get(position);

        if (type.equals(context.getString(R.string.ad_adapter_view))) {
            call_method(1, holder, position);
        } else if (type.equals(context.getString(R.string.home_adapter_view))) {
            call_method(2, holder, position);
        } else if (type.equals(context.getString(R.string.home_child_adapter_view))) {
            call_method(3, holder, position);
        } else if (type.equals(context.getString(R.string.home_child_Ad_adapter_view))) {
            call_method(4, holder, position);
        } else if (type.equals(context.getString(R.string.home_child_location_adapter_view))) {
            call_method(5, holder, position);
        }else if (type.equals(context.getString(R.string.search_adapter_view))) {
            call_method(6, holder, position);
        }
    }

    private void call_method(int i, ViewHolder holder, final int position) {
        switch (i) {
            case 1:

                Glide.with(context)
                        .load(Config.AD_IMAGE_BASE_URL + data_sub.get("Image").toString())
                        .centerCrop()
                        .into(holder.image_view);

                holder.views.setText(data_sub.get("AdViews").toString());
                holder.price.setText(context.getString(R.string.price) + data_sub.get("Price").toString());
                holder.category_name.setText(context.getString(R.string.category) + data_sub.get("CatName").toString());
                holder.rating.setText(context.getString(R.string.rating) + data_sub.get("Rating").toString() + "/10");
                holder.rating_by_user.setText(context.getString(R.string.rating_by_user) + data_sub.get("RatingUsers").toString() + "/10");
                holder.location.setText(data_sub.get("Location").toString());
                holder.company.setText(data_sub.get("Company").toString());
                holder.time.setText(data_sub.get("TimePassed").toString());

                holder.parent_click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        data_sub = data.get(position);
                        intent = new Intent(context, AdDetails.class);
                        intent.putExtra("adId", data_sub.get("Adid").toString());
                        context.startActivity(intent);
                    }
                });
                break;

            case 2:
                holder.parent_txt.setText(data_sub.get("parent_txt").toString());

                holder.view_all_txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                if (data_sub.get("parent_txt").toString().equals(context.getString(R.string.offer_by_location))) {
                    if(data_sub.get("parent_size").toString().equals("0")){
                        holder.parent_home_adapter.setVisibility(View.GONE);
                    }else {
                        holder.parent_home_adapter.setVisibility(View.VISIBLE);
                        setData(data_sub, holder, 0);
                    }
                } else if (data_sub.get("parent_txt").toString().equals(context.getString(R.string.offer_by_category))) {
                    if(data_sub.get("parent_size").toString().equals("0")){
                        holder.parent_home_adapter.setVisibility(View.GONE);
                    }else {
                        holder.parent_home_adapter.setVisibility(View.VISIBLE);
                        setData(data_sub, holder, 1);
                    }
                } else if (data_sub.get("parent_txt").toString().equals(context.getString(R.string.list_item))) {
                    if(data_sub.get("parent_size").toString().equals("0")){
                        holder.parent_home_adapter.setVisibility(View.GONE);
                    }else {
                        holder.parent_home_adapter.setVisibility(View.VISIBLE);
                        setData(data_sub, holder, 2);
                    }
                }


                break;

            case 3:

//                Glide.with(context)
//                        .load(Config.AD_IMAGE_BASE_URL + data_sub.get("Image").toString())
//                        .centerCrop()
//                        .into(holder.image_child);

                holder.text_child.setText(data_sub.get("CatName").toString());

                holder.child_parent_click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                break;

            case 4:

                Glide.with(context)
                        .load(Config.AD_IMAGE_BASE_URL + data_sub.get("Image").toString())
                        .centerCrop()
                        .into(holder.image_child);

                holder.text_child.setText(data_sub.get("Company").toString());

                holder.child_parent_click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        data_sub = data.get(position);
                        intent = new Intent(context, AdDetails.class);
                        intent.putExtra("adId", data_sub.get("Adid").toString());
                        context.startActivity(intent);
                    }
                });
                break;

            case 5:

//                Glide.with(context)
//                        .load(Config.AD_IMAGE_BASE_URL + data_sub.get("Image").toString())
//                        .centerCrop()
//                        .into(holder.image_child);

                holder.text_child.setText(data_sub.get("AreaName").toString());

                holder.child_parent_click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                break;

            case 6:
                holder.text_child.setText(data_sub.get("IdentityValue").toString());

                holder.child_parent_click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        data_sub = data.get(position);
                        recyclerViewCallBack.recyclerViewCallBack(data_sub);
                    }
                });

                break;

        }

    }

    private void setData(HashMap<String, Object> data_sub, ViewHolder holder, int type) {

        switch (type) {
            case 0:
                try {
                    jsonArray = new JSONArray(data_sub.get("data").toString());
                    dataChild = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        dataChildSub = new HashMap<>();
                        dataChildSub.put("AreaID", jsonObject.getString("AreaID"));
                        dataChildSub.put("AreaName", jsonObject.getString("AreaName"));
                        dataChildSub.put("CityID", jsonObject.getString("CityID"));
                        dataChild.add(dataChildSub);
                    }
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.child_list_home.getContext(), LinearLayoutManager.HORIZONTAL, false);
                    holder.child_list_home.setLayoutManager(linearLayoutManager);
                    Adapter_all_view adapterAllView = new Adapter_all_view(context, dataChild, R.layout.home_child_location_adapter_view, context.getString(R.string.home_child_location_adapter_view));
                    holder.child_list_home.setAdapter(adapterAllView);
                    holder.child_list_home.setNestedScrollingEnabled(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case 1:
                try {
                    jsonArray = new JSONArray(data_sub.get("data").toString());
                    dataChild = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        dataChildSub = new HashMap<>();
                        dataChildSub.put("CatID", jsonObject.getString("CatID"));
                        dataChildSub.put("CatName", jsonObject.getString("CatName"));
                        dataChildSub.put("CatImage", jsonObject.getString("CatImage"));
                        dataChild.add(dataChildSub);
                    }
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.child_list_home.getContext(), LinearLayoutManager.HORIZONTAL, false);
                    holder.child_list_home.setLayoutManager(linearLayoutManager);
                    Adapter_all_view adapterAllView = new Adapter_all_view(context, dataChild, R.layout.home_child_adapter_view, context.getString(R.string.home_child_adapter_view));
                    holder.child_list_home.setAdapter(adapterAllView);
                    holder.child_list_home.setNestedScrollingEnabled(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case 2:
                try {
                    jsonArray = new JSONArray(data_sub.get("data").toString());
                    dataChild = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        dataChildSub = new HashMap<>();
                        dataChildSub.put("Adid", jsonObject.getString("Adid"));
                        dataChildSub.put("Image", jsonObject.getString("Image"));
                        dataChildSub.put("Price", jsonObject.getString("Price"));
                        dataChildSub.put("CatName", jsonObject.getString("CatName"));
                        dataChildSub.put("Company", jsonObject.getString("Company"));
                        dataChildSub.put("Rating", jsonObject.getString("Rating"));
                        dataChildSub.put("RatingUsers", jsonObject.getString("RatingUsers"));
                        dataChildSub.put("Location", jsonObject.getString("Location"));
                        dataChildSub.put("TimePassed", jsonObject.getString("TimePassed"));
                        dataChildSub.put("AdViews", jsonObject.getString("AdViews"));
                        dataChild.add(dataChildSub);
                    }

                    Log.e("Child","Child"+dataChildSub);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.child_list_home.getContext(), LinearLayoutManager.HORIZONTAL, false);
                    holder.child_list_home.setLayoutManager(linearLayoutManager);
                    Adapter_all_view adapterAllView = new Adapter_all_view(context, dataChild, R.layout.home_child_ad_adapter_view, context.getString(R.string.home_child_Ad_adapter_view));
                    holder.child_list_home.setAdapter(adapterAllView);
                    holder.child_list_home.setNestedScrollingEnabled(false);
                } catch (JSONException e) {
                    Log.e("exp","exp"+e);
                    e.printStackTrace();
                }
                break;
        }

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parent_click, child_parent_click,parent_home_adapter;
        TextView views, price, category_name, rating, rating_by_user, location, company, time, parent_txt, view_all_txt, text_child;
        ImageView image_view, image_child;
        RecyclerView child_list_home;

        public ViewHolder(View view) {
            super(view);
            if (type.equals(context.getString(R.string.ad_adapter_view))) {
                parent_click = (LinearLayout) view.findViewById(R.id.parent_click);
                image_view = (ImageView) view.findViewById(R.id.image_view);
                views = (TextView) view.findViewById(R.id.views);
                price = (TextView) view.findViewById(R.id.price);
                category_name = (TextView) view.findViewById(R.id.category_name);
                rating = (TextView) view.findViewById(R.id.rating);
                rating_by_user = (TextView) view.findViewById(R.id.rating_by_user);
                location = (TextView) view.findViewById(R.id.location);
                company = (TextView) view.findViewById(R.id.company);
                time = (TextView) view.findViewById(R.id.time);
            } else if (type.equals(context.getString(R.string.home_adapter_view))) {
                parent_home_adapter = (LinearLayout) view.findViewById(R.id.parent_home_adapter);
                parent_txt = (TextView) view.findViewById(R.id.parent_txt);
                view_all_txt = (TextView) view.findViewById(R.id.view_all_txt);
                child_list_home = (RecyclerView) view.findViewById(R.id.child_list_home);
            } else if (type.equals(context.getString(R.string.home_child_adapter_view))) {
                child_parent_click = (LinearLayout) view.findViewById(R.id.child_parent_click);
                text_child = (TextView) view.findViewById(R.id.text_child);
                image_child = (ImageView) view.findViewById(R.id.image_child);
            } else if (type.equals(context.getString(R.string.home_child_Ad_adapter_view))) {
                child_parent_click = (LinearLayout) view.findViewById(R.id.child_parent_click);
                text_child = (TextView) view.findViewById(R.id.text_child);
                image_child = (ImageView) view.findViewById(R.id.image_child);
            } else if (type.equals(context.getString(R.string.home_child_location_adapter_view))) {
                child_parent_click = (LinearLayout) view.findViewById(R.id.child_parent_click);
                text_child = (TextView) view.findViewById(R.id.text_child);
                image_child = (ImageView) view.findViewById(R.id.image_child);
            }else if (type.equals(context.getString(R.string.search_adapter_view))) {
                child_parent_click = (LinearLayout) view.findViewById(R.id.child_parent_click);
                text_child = (TextView) view.findViewById(R.id.text_child);
            }
        }
    }
}