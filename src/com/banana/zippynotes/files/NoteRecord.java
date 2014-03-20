package com.banana.zippynotes.files;

import com.dropbox.sync.android.DbxRecord;

// this makes more sense as an interface but interfaces can't extend
// abstract classes so this will have to do
public abstract class NoteRecord extends FileSystemEntry {
    /**
     * @return The class of the activity that is used to edit this type of note
     */
    public abstract <T> Class<T> getEditActivity();

    public NoteRecord(DbxRecord record) {
        super(record);
    }
}
