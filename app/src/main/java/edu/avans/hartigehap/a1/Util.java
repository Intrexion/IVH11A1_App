package edu.avans.hartigehap.a1;

import android.telephony.PhoneNumberUtils;
import android.util.Patterns;
import android.widget.EditText;

public class Util {

    /**
     * Empty constructor to prevent initialisation.
     */
    private Util() {}

    public static boolean isValidEmailAddress(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPhoneNumber(CharSequence phoneNumber) {
        return PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber.toString());
    }

    public static boolean isValidHost(CharSequence host) {
        return Patterns.IP_ADDRESS.matcher(host).matches() || Patterns.DOMAIN_NAME.matcher(host).matches();
    }

    public static boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    public static boolean isValidPort(int port) {
        return (port >= 0 && port <= 65535);
    }
}
