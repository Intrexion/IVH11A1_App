package edu.avans.hartigehap.a1.api;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class RestaurantsApi {
    public static void getRestaurants(final OnGetRestaurantListener listener) {
        RestClient.get("restaurants", null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                ArrayList<String> restaurants = new ArrayList<>();

                try {
                    for (int i = 0; i < response.length(); ++i) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        restaurants.add(jsonObject.getString("id"));
                    }
                    listener.onGetRestaurants(restaurants);
                } catch (JSONException e) {
                    Log.e(RestaurantsApi.class.toString(), "Exception while parsing JSON", e);
                    listener.onGetRestaurantsFailed();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(RestaurantsApi.class.toString(), "Retrieving restaurants failed: " + statusCode, throwable);
                listener.onGetRestaurantsFailed();
            }
        });
    }

    public static interface OnGetRestaurantListener {
        public void onGetRestaurants(List<String> restaurants);
        public void onGetRestaurantsFailed();
    }
}
