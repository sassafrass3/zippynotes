package com.banana.zippynotes.files;

import com.dropbox.sync.android.DbxRecord;

public class PhotoNoteRecord extends NoteRecord {
    public PhotoNoteRecord(DbxRecord record) {
        super(record);
    }

    @Override
    public <T> Class<T> getEditActivity() {
        return null;
    }
}
