package com.banana.zippynotes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem.PathListener.Mode;
import com.dropbox.sync.android.DbxPath;

public class MenuActivity extends DropboxActivity {
    private Map<String, DbxFileInfo> filenameToFileInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadNotes();
    }

    private void editNote(DbxPath path) {
        Intent intent = new Intent(this, EditTextNoteActivity.class);
        intent.putExtra("path", path.toString());
        startActivity(intent);
    }

    public void onNewNoteClick(View view) {
        try {
            String filename = ((EditText) findViewById(R.id.editText1)).getText().toString();

            DbxPath path = new DbxPath(DbxPath.ROOT, filename + ".txt");

            // close immediately so that the editor can open it
            // this is a little silly but it'll probably work fine
            getFileSystem().create(path).close();

            editNote(path);
        } catch (DbxException e) {
            handleException(e);
        }
    }

    public void onEditNoteClick(View view) {
        String filename = ((Spinner) findViewById(R.id.spinner1))
            .getSelectedItem().toString();

        editNote(filenameToFileInfo.get(filename).path);
    }

    private List<DbxFileInfo> readNotes() throws DbxException {
        List<DbxFileInfo> notes = new ArrayList<DbxFileInfo>();

        for (DbxFileInfo fileInfo : getFileSystem().listFolder(DbxPath.ROOT))
            if (!fileInfo.isFolder && fileInfo.path.toString().endsWith(".txt"))
                notes.add(fileInfo);

        return notes;
    }

    private void loadNotes() {
        try {
            List<DbxFileInfo> notes = readNotes();
            filenameToFileInfo = new HashMap<String, DbxFileInfo>();

            String[] entries = new String[notes.size()];

            for (int i = 0; i < notes.size(); i++) {
                DbxFileInfo fileInfo = notes.get(i);

                entries[i] = fileInfo.path.getName();
                filenameToFileInfo.put(fileInfo.path.getName(), fileInfo);
            }

            ((Spinner) findViewById(R.id.spinner1)).setAdapter(
                new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_spinner_item,
                    entries));
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    protected void onPathUpdated(DbxPath path, Mode mode) {
        if (path.equals(DbxPath.ROOT))
            loadNotes();
    }
}
