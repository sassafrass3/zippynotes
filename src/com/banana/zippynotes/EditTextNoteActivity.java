package com.banana.zippynotes;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;

import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem.PathListener.Mode;
import com.dropbox.sync.android.DbxPath;

public class EditTextNoteActivity extends DropboxActivity {
    private DbxPath path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text_note);

        path = new DbxPath(getIntent().getStringExtra("path"));
    }

    private EditText getEditText() {
        return ((EditText) findViewById(R.id.noteText));
    }

    @Override
    public void onResume() {
        super.onResume();

        setText();
    }

    private void setText() {
        getEditText().setText(readFile(path));
    }

    private String readFile(DbxPath path) {
        DbxFile file = null;

        try {
            file = getFileSystem().open(path);

            return file.readString();
        } catch (Exception e) {
            Log.d("Edit Text Note", e.toString());
            handleException(e);
        } finally {
            if (file != null)
                file.close();
        }

        return "apples";  // error
    }

    @Override
    public void onPause() {
        DbxFile file = null;

        try {
            file = getFileSystem().open(path);

            file.writeString(getEditText().getText().toString());
        } catch (Exception e) {
            handleException(e);
        } finally {
            if (file != null)
                file.close();
        }

        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_text_note, menu);
        return true;
    }

    @Override
    protected void onPathUpdated(DbxPath path, Mode mode) {
        // we use this.path on the inside because it might be null if this
        // happens to be called before onCreate
        if (path.equals(this.path)) {
            // TODO
            Log.d("Edit Text Note", "Path changed!");
        }
    }
}
