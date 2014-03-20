package com.banana.zippynotes.files;

import com.dropbox.sync.android.DbxRecord;

public class TextNoteRecord extends NoteRecord {
    public TextNoteRecord(DbxRecord record) {
        super(record);
    }

    @Override
    public <T> Class<T> getEditActivity() {
        return null;
    }
}
