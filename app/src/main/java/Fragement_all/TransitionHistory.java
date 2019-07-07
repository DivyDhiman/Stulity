package Fragement_all;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.StudentFaclity.Stulity.R;

import java.util.ArrayList;
import java.util.HashMap;

import Adapter_all.Adapter_all_view;

/**
 * Created by Abhay0648 on 22-01-2017.
 */

public class TransitionHistory extends Fragment {
    private View root_view;
    private RecyclerView e_book_list;
    private ArrayList<HashMap<String, Object>> data;
    private HashMap<String, Object> data_sub;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root_view = inflater.inflate(R.layout.transation_history, container, false);
        context = root_view.getContext();
        initialise_view(root_view);
        return root_view;
    }

    private void initialise_view(View root_view) {
        e_book_list = (RecyclerView) root_view.findViewById(R.id.notification_list);
        data = new ArrayList<>();

        data_sub = new HashMap<>();
        data_sub.put("cash", context.getString(R.string.batuva_cash_recharge));
        data_sub.put("to", "To: 9999999999");
        data_sub.put("time", "January 12, 09:04");
        data_sub.put("logo", R.drawable.pay_logo);
        data.add(data_sub);

        data_sub = new HashMap<>();
        data_sub.put("cash", context.getString(R.string.batuva_cash_recharge));
        data_sub.put("to", "To: 99998899999");
        data_sub.put("time", "January 14, 05:04");
        data_sub.put("logo", R.drawable.pay_logo);
        data.add(data_sub);

        data_sub = new HashMap<>();
        data_sub.put("cash", context.getString(R.string.batuva_cash_added));
        data_sub.put("to", "From: 4569999900553245");
        data_sub.put("time", "January 10, 02:04");
        data_sub.put("logo", R.drawable.add_money);
        data.add(data_sub);

        data_sub = new HashMap<>();
        data_sub.put("cash", context.getString(R.string.batuva_cash_send));
        data_sub.put("to", "To: 4565155632111");
        data_sub.put("time", "January 05, 02:04");
        data_sub.put("logo", R.drawable.send_to_bank);
        data.add(data_sub);

        data_sub = new HashMap<>();
        data_sub.put("cash", context.getString(R.string.batuva_cash_added));
        data_sub.put("to", "From: 4569999900853245");
        data_sub.put("time", "January 10, 03:04");
        data_sub.put("logo", R.drawable.add_money);
        data.add(data_sub);

        data_sub = new HashMap<>();
        data_sub.put("cash", context.getString(R.string.batuva_cash_send));
        data_sub.put("to", "To: 4565553652222");
        data_sub.put("time", "January 05, 08:04");
        data_sub.put("logo", R.drawable.send_to_bank);
        data.add(data_sub);

        Adapter_all_view adapter_ebook = new Adapter_all_view(e_book_list.getContext(), data,R.layout.transation_history_adapter,getString(R.string.type_e_book));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(e_book_list.getContext());
        e_book_list.setLayoutManager(linearLayoutManager);
        e_book_list.setHasFixedSize(true);
        e_book_list.setAdapter(adapter_ebook);

    }
}