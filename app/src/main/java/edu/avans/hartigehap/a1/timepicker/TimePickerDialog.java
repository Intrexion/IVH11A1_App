package edu.avans.hartigehap.a1.timepicker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import edu.avans.hartigehap.a1.R;

public class TimePickerDialog extends AlertDialog implements DialogInterface.OnClickListener, RangeSeekBar.OnRangeSeekBarChangeListener {
    private TimeChangedListener mListener;
    private TextView textViewStart;
    private TextView textViewEnd;
    private String timeFormat;

    int mHourStart = 18, mMinuteStart = 0, mHourEnd = 21, mMinuteEnd = 0;

    public TimePickerDialog(Context context, TimeChangedListener listener) {
        super(context);
        setTitle(R.string.timepicker_title);
        timeFormat = context.getString(R.string.time_format);

        final Context themeContext = getContext();
        final LayoutInflater inflater = LayoutInflater.from(themeContext);
        final View view = inflater.inflate(R.layout.dialog_time_picker, null);
        setView(view);
        setButton(BUTTON_POSITIVE, themeContext.getString(R.string.ok), this);
        setButton(BUTTON_NEGATIVE, themeContext.getString(R.string.cancel), this);

        RangeSeekBar rangeSeekBar = (RangeSeekBar) view.findViewById(R.id.time_picker_seekbar);
        rangeSeekBar.setOnRangeSeekBarChangeListener(this);
        rangeSeekBar.setNotifyWhileDragging(true);
        rangeSeekBar.setSelectedMinValue(12);
        rangeSeekBar.setSelectedMaxValue(18);

        textViewStart = (TextView) view.findViewById(R.id.time_picker_label_start);
        textViewEnd = (TextView) view.findViewById(R.id.time_picker_label_end);
        updateSummary();

        setListener(listener);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            mListener.onTimeChanged(mHourStart, mMinuteStart, mHourEnd, mMinuteEnd);
        }
    }

    public void setListener(TimeChangedListener listener) {
        mListener = listener;
    }

    @Override
    public void onRangeSeekBarValuesChanged(RangeSeekBar bar, int minValue, int maxValue) {
        double minTime = 12 + (minValue / 2d);
        double maxTime = 12 + (maxValue / 2d);

        mHourStart = (int) Math.floor(minTime);
        mMinuteStart = (int) ((minTime % 1) * 60);
        mHourEnd = (int) Math.floor(maxTime);
        mMinuteEnd = (int) ((maxTime % 1) * 60);

        updateSummary();
    }

    private void updateSummary() {
        textViewStart.setText(String.format(timeFormat, mHourStart, mMinuteStart));
        textViewEnd.setText(String.format(timeFormat, mHourEnd, mMinuteEnd));
    }

    public static interface TimeChangedListener {
        public void onTimeChanged(int hourStart, int minuteStart, int hourEnd, int minuteEnd);
    }
}
