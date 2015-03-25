package edu.avans.hartigehap.a1;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import edu.avans.hartigehap.a1.api.RestClient;
import me.denley.preferenceinjector.PreferenceInjector;


public class RestaurantActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new RestaurantFragment()).commit();
        }

        PreferenceInjector.inject(this, RestClient.getInstance());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_about) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.about_title)
                    .setMessage(Html.fromHtml(String.format(getString(R.string.about_message), getString(R.string.app_name), BuildConfig.VERSION_NAME)))
                    .setPositiveButton(R.string.ok, null)
                    .setIcon(R.mipmap.ic_launcher)
                    .create();
            dialog.show();
            ((TextView) dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
