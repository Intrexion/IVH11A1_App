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
    private TextView textViewStart, textViewEnd;
    private Time startTime, endTime;

    public TimePickerDialog(Context context, TimeChangedListener listener) {
        super(context);

        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_time_picker, null);
        setButton(BUTTON_POSITIVE, context.getString(R.string.ok), this);
        setButton(BUTTON_NEGATIVE, context.getString(R.string.cancel), this);
        setTitle(R.string.timepicker_title);
        setView(view);

        RangeSeekBar rangeSeekBar = (RangeSeekBar) view.findViewById(R.id.time_picker_seekbar);
        rangeSeekBar.setOnRangeSeekBarChangeListener(this);
        rangeSeekBar.setNotifyWhileDragging(true);
        rangeSeekBar.setSelectedMinValue(12);
        rangeSeekBar.setSelectedMaxValue(18);

        textViewStart = (TextView) view.findViewById(R.id.time_picker_label_start);
        textViewEnd = (TextView) view.findViewById(R.id.time_picker_label_end);

        startTime = new Time(18, 0);
        endTime = new Time(21, 0);

        updateSummary();
        setListener(listener);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            mListener.onTimeChanged(startTime, endTime);
        }
    }

    public void setListener(TimeChangedListener listener) {
        mListener = listener;
    }

    @Override
    public void onRangeSeekBarValuesChanged(RangeSeekBar bar, int minValue, int maxValue) {
        double minTime = 12 + (minValue / 2d);
        double maxTime = 12 + (maxValue / 2d);

        startTime.setTime((int) Math.floor(minTime), (int) ((minTime % 1) * 60));
        endTime.setTime((int) Math.floor(maxTime), (int) ((maxTime % 1) * 60));

        updateSummary();
    }

    private void updateSummary() {
        textViewStart.setText(startTime.toString());
        textViewEnd.setText(endTime.toString());
    }

    public static interface TimeChangedListener {
        public void onTimeChanged(Time startTime, Time endTime);
    }
}
