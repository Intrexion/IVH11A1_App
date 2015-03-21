package edu.avans.hartigehap.a1;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;

public class ReservationActivity extends ActionBarActivity {
    public static final String ARGUMENT_RESTAURANT = "edu.avans.hartigehap.a1.ARGUMENT_RESTAURANT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        if (savedInstanceState == null) {
            String restaurant = getIntent().getStringExtra(RestaurantFragment.EXTRA_RESTAURANT);
            setTitle(String.format(getString(R.string.reservation_title), restaurant));

            Bundle bundle = new Bundle();
            bundle.putString(ARGUMENT_RESTAURANT, restaurant);

            ReservationFragment fragment = new ReservationFragment();
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reservations, menu);
        return true;
    }
}
