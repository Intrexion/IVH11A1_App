package edu.avans.hartigehap.a1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.Format;
import java.util.Calendar;
import java.util.Date;

import edu.avans.hartigehap.a1.domain.Reservation;
import edu.avans.hartigehap.a1.api.ReservationApi;
import edu.avans.hartigehap.a1.timepicker.Time;
import edu.avans.hartigehap.a1.timepicker.TimePickerDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReservationFragment extends Fragment implements ReservationApi.OnSubmitReservationListener, View.OnClickListener {
    private String restaurant;
    private Date date;
    private Time startTime;
    private Time endTime;

    private Format dateFormat;
    private String errorEmpty;
    private String errorInvalid;
    private Drawable errorDrawable;

    private EditText editTextDate;
    private EditText editTextTimeStart;
    private EditText editTextTimeEnd;
    private EditText editTextPersons;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private EditText editTextPhone;
    private EditText editTextComments;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reservation, container, false);
        rootView.requestFocus();

        restaurant = getArguments().getString(ReservationActivity.ARGUMENT_RESTAURANT);
        dateFormat = android.text.format.DateFormat.getLongDateFormat(getActivity());
        errorEmpty = getString(R.string.form_error_empty);
        errorInvalid = getString(R.string.form_error_invalid);
        errorDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_error, null);
        errorDrawable.setBounds(0, 0, errorDrawable.getIntrinsicWidth(), errorDrawable.getIntrinsicHeight());

        editTextDate = (EditText) rootView.findViewById(R.id.form_date);
        editTextDate.setOnClickListener(this);
        editTextTimeStart = (EditText) rootView.findViewById(R.id.form_time_start);
        editTextTimeStart.setOnClickListener(this);
        editTextTimeEnd = (EditText) rootView.findViewById(R.id.form_time_end);
        editTextTimeEnd.setOnClickListener(this);
        editTextPersons = (EditText) rootView.findViewById(R.id.form_persons);
        editTextFirstName = (EditText) rootView.findViewById(R.id.form_firstname);
        editTextLastName = (EditText) rootView.findViewById(R.id.form_lastname);
        editTextEmail = (EditText) rootView.findViewById(R.id.form_email);
        editTextPhone = (EditText) rootView.findViewById(R.id.form_phone);
        editTextComments = (EditText) rootView.findViewById(R.id.form_comments);

        setupPickers();
        setHasOptionsMenu(true);

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
                date = calendar.getTime();
                editTextDate.setText(dateFormat.format(date));
                editTextDate.setError(null);
            }
        };
        datePickerDialog = new DatePickerDialog(getActivity(), dateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        TimePickerDialog.TimeChangedListener listener = new TimePickerDialog.TimeChangedListener() {
            @Override
            public void onTimeChanged(Time startTime, Time endTime) {
                ReservationFragment.this.startTime = startTime;
                ReservationFragment.this.endTime = endTime;
                editTextTimeStart.setText(startTime.toString());
                editTextTimeEnd.setText(endTime.toString());
                editTextTimeEnd.setError(null);
            }
        };
        timePickerDialog = new TimePickerDialog(getActivity(), listener);
    }

    private void submit() {
        if (isValidForm()) {
            progress = ProgressDialog.show(getActivity(), getString(R.string.reservation_submitting), getString(R.string.reservation_checking));

            int persons = Integer.parseInt(editTextPersons.getText().toString());
            String firstName = editTextFirstName.getText().toString();
            String lastName = editTextLastName.getText().toString();
            String emailAddress = editTextEmail.getText().toString();
            String phoneNumber = editTextPhone.getText().toString();
            String comments = editTextComments.getText().toString();

            Reservation reservation = new Reservation(restaurant, date, startTime, endTime, persons, firstName, lastName, emailAddress, phoneNumber, comments);
            ReservationApi.submitReservation(reservation, ReservationFragment.this);
        }
    }

    private boolean isValidForm() {
        boolean valid = true;

        if (date == null) {
            valid = false;
            editTextDate.setError(errorEmpty, errorDrawable);
        }

        if (endTime == null) {
            valid = false;
            editTextTimeEnd.setError(errorEmpty, errorDrawable);
        }

        if (Util.isEmpty(editTextPersons)) {
            valid = false;
            editTextPersons.setError(errorEmpty, errorDrawable);
        } else if (Integer.parseInt(editTextPersons.getText().toString()) <= 0) {
            valid = false;
            editTextPersons.setError(errorInvalid, errorDrawable);
        }

        if (Util.isEmpty(editTextFirstName)) {
            valid = false;
            editTextFirstName.setError(errorEmpty, errorDrawable);
        }

        if (Util.isEmpty(editTextLastName)) {
            valid = false;
            editTextLastName.setError(errorEmpty, errorDrawable);
        }

        if (Util.isEmpty(editTextEmail)) {
            valid = false;
            editTextEmail.setError(errorEmpty, errorDrawable);
        } else if (!Util.isValidEmailAddress(editTextEmail.getText())) {
            valid = false;
            editTextEmail.setError(errorInvalid, errorDrawable);
        }

        if (Util.isEmpty(editTextPhone)) {
            valid = false;
            editTextPhone.setError(errorEmpty, errorDrawable);
        } else if (!Util.isValidPhoneNumber(editTextPhone.getText())) {
            valid = false;
            editTextPhone.setError(errorInvalid, errorDrawable);
        }

        return valid;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.restaurant_submit:
                hideSoftKeyboard();
                submit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReservationSuccessful(final String reservationCode) {
        progress.dismiss();
        CharSequence dialogMessage = Html.fromHtml(String.format(getString(R.string.reservation_code_message), reservationCode));
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.reservation_successful)
                .setMessage(dialogMessage)
                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                }).setNeutralButton(R.string.copy, null)
                .show();

        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Activity.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(getString(R.string.reservation_code), reservationCode);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), R.string.reservation_code_copied, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onReservationFailed() {
        progress.dismiss();
        Toast.makeText(getActivity(), R.string.reservation_failed, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReservationError() {
        progress.dismiss();
        Toast.makeText(getActivity(), R.string.reservation_error, Toast.LENGTH_LONG).show();
    }
}
