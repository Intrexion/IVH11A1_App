package edu.avans.hartigehap.a1;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import edu.avans.hartigehap.a1.api.RestaurantsApi;


public class MainActivity extends ActionBarActivity implements RestaurantsApi.OnGetRestaurantListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            RestaurantsApi.getRestaurants(this);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onGetRestaurants(List<String> restaurants) {
        getSupportFragmentManager().beginTransaction().add(R.id.container, new MainFragment(restaurants)).commit();
    }

    @Override
    public void onGetRestaurantsFailed() {
        Toast.makeText(this, "Failed to load restaurants.", Toast.LENGTH_SHORT).show();
    }
}
