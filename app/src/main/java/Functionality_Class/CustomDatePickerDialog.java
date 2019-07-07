package Functionality_Class;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Abhay dhiman
 */

//Custom Date picker class for picking class expiry date
public class CustomDatePickerDialog extends DatePickerDialog implements DatePicker.OnDateChangedListener {

    private DatePickerDialog mDatePicker;

    //CustomDatePickerDialog constructor
    @SuppressLint("NewApi")
    public CustomDatePickerDialog(Context context, int theme, OnDateSetListener callBack,
                                  int year, int monthOfYear, int dayOfMonth) {
        super(context, theme, callBack, year, monthOfYear, dayOfMonth);
        mDatePicker = new DatePickerDialog(context, theme, callBack, year, monthOfYear, dayOfMonth);

        mDatePicker.getDatePicker().init(2016, 7, 24, this);
        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        updateTitle(year, monthOfYear);

    }

    public void onDateChanged(DatePicker view, int year,
                              int month, int day) {
        updateTitle(year, month);
    }

    private void updateTitle(int year, int month) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mDatePicker.setTitle(getFormat().format(mCalendar.getTime()));

    }

    public DatePickerDialog getPicker() {
        return this.mDatePicker;
    }


     //The format for dialog tile,and you can override this method
    public SimpleDateFormat getFormat() {
        return new SimpleDateFormat("MMM, yyyy");
    }
}
