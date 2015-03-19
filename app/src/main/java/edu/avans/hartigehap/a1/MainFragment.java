package edu.avans.hartigehap.a1;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.Format;
import java.util.Calendar;

import edu.avans.hartigehap.a1.timepicker.TimePickerDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener {
    private Format dateFormat;

    private EditText editTextDate;
    private EditText editTextTimeStart;
    private EditText editTextTimeEnd;

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        rootView.requestFocus();
        dateFormat = android.text.format.DateFormat.getLongDateFormat(getActivity());

        Spinner spinner = (Spinner) rootView.findViewById(R.id.form_restaurant);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.form_restaurants, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        editTextDate = (EditText) rootView.findViewById(R.id.form_date);
        editTextDate.setOnClickListener(this);
        editTextTimeStart = (EditText) rootView.findViewById(R.id.form_time_start);
        editTextTimeStart.setOnClickListener(this);
        editTextTimeEnd = (EditText) rootView.findViewById(R.id.form_time_end);
        editTextTimeEnd.setOnClickListener(this);

        setupPickers();

        return rootView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        hideSoftKeyboard();

        switch (id) {
            case R.id.form_date:
                datePickerDialog.show();
                break;
            case R.id.form_time_start:
            case R.id.form_time_end:
                timePickerDialog.show();
                break;
        }
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    private void setupPickers() {
        final Calendar calendar = Calendar.getInstance();

        // Date picker
        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                editTextDate.setText(dateFormat.format(calendar.getTime()));
            }
        };
        datePickerDialog = new DatePickerDialog(getActivity(), dateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        final String timeFormat = getString(R.string.time_format);
        TimePickerDialog.TimeChangedListener listener = new TimePickerDialog.TimeChangedListener() {
            @Override
            public void onTimeChanged(int hourStart, int minuteStart, int hourEnd, int minuteEnd) {
                editTextTimeStart.setText(String.format(timeFormat, hourStart, minuteStart));
                editTextTimeEnd.setText(String.format(timeFormat, hourEnd, minuteEnd));
            }
        };
        timePickerDialog = new TimePickerDialog(getActivity(), listener);
    }
}