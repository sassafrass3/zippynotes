package com.banana.zippynotes.files;

import java.util.ArrayList;
import java.util.List;

import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFields;
import com.dropbox.sync.android.DbxRecord;
import com.dropbox.sync.android.DbxTable.QueryResult;

/*-
 * Tables:
 *      textNotes
 *      photoNotes
 *      folders      
 */
public class FileSystemManager {
    private final DbxDatastore datastore;

    public FileSystemManager(DbxDatastore datastore) {
        this.datastore = datastore;
    }

    public List<FileSystemEntry> getRootEntries() throws DbxException, Exception {
        return getFolderChildren("");
    }

    public List<FileSystemEntry> getFolderChildren(String parentId)
            throws DbxException, Exception {
        List<FileSystemEntry> entries = new ArrayList<FileSystemEntry>();

        QueryResult queryResult = datastore.getTable("fileSystemEntries")
            .query(new DbxFields().set("parentId", parentId));

        // TODO: this might suck performance-wise. hopefully won't be too
        // bad for the number of notes we'll be using
        for (DbxRecord record : queryResult) {
            entries.add(
                (FileSystemEntry) Class.forName(record.getString("className"))
                    .getConstructor(DbxRecord.class)
                    .newInstance(record));
        }

        return entries;
    }
}
