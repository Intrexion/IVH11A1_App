package edu.avans.hartigehap.a1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import edu.avans.hartigehap.a1.api.RestaurantsApi;
import me.denley.preferenceinjector.OnPreferenceChange;
import me.denley.preferenceinjector.PreferenceInjector;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantFragment extends Fragment implements RestaurantsApi.OnGetRestaurantListener, AdapterView.OnItemClickListener {
    public static final String EXTRA_RESTAURANT = "edu.avans.hartigehap.a1.EXTRA_RESTAURANT";

    boolean shouldRefresh;
    ListView listViewRestaurants;
    TextView textViewEmpty, textViewError;
    ProgressBar progressBar;
    ArrayAdapter<String> restaurantAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);

        listViewRestaurants = (ListView) view.findViewById(R.id.restaurantListView);
        textViewEmpty = (TextView) view.findViewById(R.id.restaurantsEmpty);
        textViewError = (TextView) view.findViewById(R.id.restaurantsError);
        progressBar = (ProgressBar) view.findViewById(R.id.restaurantsProgressBar);

        shouldRefresh = (savedInstanceState == null || savedInstanceState.getBoolean("should_refresh"));
        listViewRestaurants.setOnItemClickListener(this);
        PreferenceInjector.inject(getActivity(), this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (shouldRefresh) {
            shouldRefresh = false;

            listViewRestaurants.setEmptyView(progressBar);
            textViewError.setVisibility(View.GONE);
            textViewEmpty.setVisibility(View.GONE);

            if (restaurantAdapter != null) {
                restaurantAdapter.clear();
                restaurantAdapter.notifyDataSetChanged();
            }

            RestaurantsApi.getRestaurants(this);
        }
    }

    @Override
    public void onGetRestaurants(List<String> restaurants) {
        restaurantAdapter = new ArrayAdapter<>(getActivity(), R.layout.listview_item_arrow, restaurants);
        listViewRestaurants.setAdapter(restaurantAdapter);
        listViewRestaurants.setEmptyView(textViewEmpty);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onGetRestaurantsFailed() {
        listViewRestaurants.setEmptyView(textViewError);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ReservationActivity.class);
        String restaurant = (String) parent.getItemAtPosition(position);
        intent.putExtra(EXTRA_RESTAURANT, restaurant);
        getActivity().startActivity(intent);
    }

    @OnPreferenceChange({"pref_api_host", "pref_api_port"})
    public void preferencesUpdated() {
        shouldRefresh = true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("should_refresh", shouldRefresh);
    }
}
