package edu.avans.hartigehap.a1;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

public class EditTextIntegerPreference extends EditTextPreference {
    public EditTextIntegerPreference(Context context) {
        super(context);
    }

    public EditTextIntegerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextIntegerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected String getPersistedString(String defaultReturnValue) {
        return String.valueOf(getPersistedInt(-1));
    }

    @Override
    protected boolean persistString(String value) {
        return persistInt(Integer.valueOf(value));
    }
}
