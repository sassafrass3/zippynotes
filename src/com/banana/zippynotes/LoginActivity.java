package com.banana.zippynotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;

public class LoginActivity extends Activity {
    private static final int REQUEST_LINK_TO_DBX = 0;

    private DbxAccountManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dropbox_login);

        manager = DbxAccountManager.getInstance(
            getApplicationContext(),
            getString(R.string.db_app_key),
            getString(R.string.db_app_secret));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dropbox_login, menu);
        return true;
    }

    public void onLinkClick(View view) {
        manager.startLink(this, REQUEST_LINK_TO_DBX);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (manager.hasLinkedAccount())
            showNextActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_LINK_TO_DBX) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        if (resultCode == RESULT_OK)
            showNextActivity();
        else
            Toast.makeText(this, "Link to Dropbox failed.", Toast.LENGTH_SHORT).show();
    }

    private void showNextActivity() {
        if (getParent() == null)
            startActivity(new Intent(this, MenuActivity.class));
        else
            finish();
    }
}
