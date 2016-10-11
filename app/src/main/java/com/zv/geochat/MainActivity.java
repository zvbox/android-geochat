package com.zv.geochat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadUserNameFromPreferences();

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText edtUserName = (EditText) findViewById(R.id.edtUserName);
                String userName = edtUserName.getText().toString();
                saveToPreferences(userName);

                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(intent);
            }
        });

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                // Check the LogCat to get your test device ID
                //.addTestDevice("C04B1BFFB0774708339BC273F8A43708")
                .build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        buildShareAction(menu);
        return true;
    }

    private void buildShareAction(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_share);
        ShareActionProvider shareActionProvider = new ShareActionProvider(this);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text));
        shareIntent.setType("text/plain");
        MenuItemCompat.setActionProvider(item, shareActionProvider);
        shareActionProvider.setShareIntent(shareIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_about) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.about_title))
                    .setMessage(getString(R.string.about_app_description))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();

            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    private void loadUserNameFromPreferences() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String userName = prefs.getString(Constants.PREF_KEY_USER_NAME, "User Name");

        EditText edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtUserName.setText(userName);
    }

    private void saveToPreferences(String userName) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.PREF_KEY_USER_NAME ,userName);
        editor.apply();
    }
}
