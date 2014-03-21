package com.banana.zippynotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxFileSystem.PathListener;
import com.dropbox.sync.android.DbxFileSystem.PathListener.Mode;
import com.dropbox.sync.android.DbxPath;

public abstract class DropboxActivity extends Activity {
    private DbxAccountManager accountManager;
    private DbxFileSystem fileSystem;

    /**
     * Called when a folder/file has its contents updated. Should
     * force an update to UI if the path is being viewed
     */
    protected abstract void onPathUpdated(DbxPath path, Mode mode);

    /**
     * @return The FileSystemManager. Null if not logged in yet
     */
    protected DbxFileSystem getFileSystem() {
        return fileSystem;
    }

    private PathListener pathListener = new PathListener() {
        @Override
        public void onPathChange(DbxFileSystem fs, DbxPath path, Mode mode) {
            onPathUpdated(path, mode);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accountManager = DbxAccountManager.getInstance(
            getApplicationContext(),
            getString(R.string.db_app_key),
            getString(R.string.db_app_secret));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!accountManager.hasLinkedAccount()) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        try {
            if (fileSystem == null) {
                fileSystem = DbxFileSystem.forAccount(accountManager.getLinkedAccount());
                fileSystem.addPathListener(
                    pathListener, DbxPath.ROOT, Mode.PATH_OR_DESCENDANT);
            }
        } catch (DbxException e) {
            // TODO: ... should I do something else here? like try to log in again?
            handleException(e);
        }
    }

    protected void handleException(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
    }
}
