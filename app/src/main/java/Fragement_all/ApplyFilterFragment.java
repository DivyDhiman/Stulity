package Fragement_all;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;

import com.StudentFaclity.Stulity.R;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Call_Back.ChildApiCallback;
import Controllers.Controller;
import Functionality_Class.CustomDialog;

/**
 * Created by Abhay0648 on 23-05-2017.
 */

public class ApplyFilterFragment extends Fragment {
    private View root_view;
    private Context context;
    private Controller controller;
    private String sessionId;
    private ChildApiCallback childApiCallback;
    private ArrayList<HashMap<String,Object>> dataSearchTypeAPIGet,dataTypeShow;
    private HashMap<String,Object> dataSearchTypeAPIGetSub,finalData;
    private AutoCompleteTextView search_box_type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.filter_bar, container, false);
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

                if (callingUI.equals(context.getString(R.string.search_box_type_api))) {
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

        initialise_view();
    }

    private void initialise_view() {
        search_box_type = (AutoCompleteTextView) root_view.findViewById(R.id.search_box_type);
        search_box_type.setAdapter(new searchBoxType(context, R.layout.list_item));

        search_box_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String data = String.valueOf(adapterView.getAdapter().getItem(i));
                for (int i_search=0; i_search < dataSearchTypeAPIGet.size(); i_search++){
                    if(data.equals(dataSearchTypeAPIGet.get(i_search).get("IdentityValue").toString())){

                        finalData.put("CIdentityID",dataSearchTypeAPIGet.get(i_search).get("CIdentityID").toString());
                        finalData.put("CIdentityName",dataSearchTypeAPIGet.get(i_search).get("CIdentityName").toString());
                        finalData.put("CIdentityValue",dataSearchTypeAPIGet.get(i_search).get("CIdentityValue").toString());
                        finalData.put("SearchBoxType", Uri.encode(data));
                    }
                }
            }
        });

        RangeSeekBar<Integer> rangeSeekBar = new RangeSeekBar<Integer>(context);
        // Set the range
        rangeSeekBar.setRangeValues(0, 50000);
        rangeSeekBar.setSelectedMinValue(0);
        rangeSeekBar.setSelectedMaxValue(0);

        LinearLayout layout = (LinearLayout) root_view.findViewById(R.id.seekbar_placeholder);
        layout.addView(rangeSeekBar);

        RangeSeekBar rangeSeekBarTextColorWithCode = (RangeSeekBar) root_view.findViewById(R.id.range_seekbar);
        rangeSeekBarTextColorWithCode.setTextAboveThumbsColorResource(R.color.button_bg);

        rangeSeekBarTextColorWithCode.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {

            }
        });
    }


    private void callSearchBoxTypeResult(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            dataSearchTypeAPIGet = new ArrayList<>();
            for (int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                dataSearchTypeAPIGetSub = new HashMap<>();

                dataSearchTypeAPIGetSub.put("CIdentityID",jsonObject.getString("CIdentityID"));
                dataSearchTypeAPIGetSub.put("CIdentityName",jsonObject.getString("CIdentityName"));
                dataSearchTypeAPIGetSub.put("CIdentityValue",jsonObject.getString("CIdentityValue"));
                dataSearchTypeAPIGet.add(dataSearchTypeAPIGetSub);
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

}
