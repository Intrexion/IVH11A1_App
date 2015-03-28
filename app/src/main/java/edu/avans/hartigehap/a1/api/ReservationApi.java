package edu.avans.hartigehap.a1.api;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;

import edu.avans.hartigehap.a1.domain.Reservation;

@SuppressWarnings("deprecation")
public class ReservationApi {
    private static final Format dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void submitReservation(final Reservation reservation, final OnSubmitReservationListener listener) {
        RequestParams params = new RequestParams();
        params.put("restaurant.id", reservation.getRestaurant());
        params.put("startDate", dateFormat.format(reservation.getDate()) + " " + reservation.getStartTime());
        params.put("endDate", dateFormat.format(reservation.getDate()) + " " + reservation.getEndTime());
        params.put("customer.firstName", reservation.getFirstName());
        params.put("customer.lastName", reservation.getLastName());
        params.put("customer.partySize", reservation.getPersons());
        params.put("customer.email", reservation.getEmailAddress());
        params.put("customer.phone", reservation.getPhoneNumber());
        params.put("description", reservation.getComments());

        RestClient.getInstance().post("reservations", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String result = response.getString("result");
                    switch (result) {
                        case "OK":
                            String reservationCode = response.getJSONObject("reservation").getString("code");
                            listener.onReservationSuccessful(reservationCode);
                            break;
                        case "FAIL":
                            listener.onReservationFailed();
                            break;
                        default:
                            listener.onReservationError();
                            break;
                    }
                } catch (JSONException e) {
                    Log.e(ReservationApi.class.toString(), "Exception while parsing JSON", e);
                    listener.onReservationError();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(ReservationApi.class.toString(), "Submitting reservation failed: " + statusCode, throwable);
                listener.onReservationError();
            }
        });
    }

    public static interface OnSubmitReservationListener {
        public void onReservationSuccessful(String reservationCode);
        public void onReservationFailed();
        public void onReservationError();
    }
}
