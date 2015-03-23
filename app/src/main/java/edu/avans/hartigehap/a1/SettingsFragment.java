package edu.avans.hartigehap.a1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Fragment containing the preferences.
 */
public class SettingsFragment extends PreferenceFragment {
    EditTextPreference prefApiHost;
    EditTextIntegerPreference prefApiPort;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        prefApiHost = (EditTextPreference) findPreference("pref_api_host");
        prefApiHost.setOnPreferenceChangeListener(changeListenerHost);
        prefApiHost.setSummary(prefApiHost.getText());

        prefApiPort = (EditTextIntegerPreference) findPreference("pref_api_port");
        prefApiPort.setOnPreferenceChangeListener(changeListenerPort);
        prefApiPort.setSummary(prefApiPort.getText());
    }

    private final Preference.OnPreferenceChangeListener changeListenerHost = new Preference.OnPreferenceChangeListener() {
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String host = (String) newValue;

            if (Util.isValidHost(host)) {
                prefApiHost.setSummary(host);
                return true;
            } else {
                Toast.makeText(getActivity(), R.string.settings_api_host_invalid, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    };

    private final Preference.OnPreferenceChangeListener changeListenerPort = new Preference.OnPreferenceChangeListener() {
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            int port = Integer.parseInt((String) newValue);

            if (Util.isValidPort(port)) {
                prefApiPort.setSummary(String.valueOf(port));
                return true;
            } else {
                Toast.makeText(getActivity(), R.string.settings_api_port_invalid, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    };
}
