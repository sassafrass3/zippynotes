package com.banana.zippynotes.files;

import com.dropbox.sync.android.DbxRecord;

/*-
 * NOTE: if this schema changes, be sure to update FileSystemManager
 * 
 * Datastore fields:
 *      className  -- name of the root class; see comment on getter
 *      name       -- name of field
 *      path       -- path to file from the root directory 
 *      parentId   -- dbx id of the parent folder; empty string means root level 
 */
public class FileSystemEntry {
    protected DbxRecord record;

    public FileSystemEntry(DbxRecord record) {
        this.record = record;
    }

    // TODO: getIconResourcePath

    /**
     * Datastores are key/value and don't have type information,
     * which means we have to do it ourselves.
     * 
     * @return Name of base-level class (e.g. TextNoteRecord)
     */
    public String getClassName() {
        return record.getString("className");
    }

    /**
     * @return Name of the file
     */
    public String getName() {
        return record.getString("name");
    }

    /**
     * @return Relative path to the file from the root directory
     */
    public String getPath() {
        return record.getString("path");
    }

    /**
     * Id is the empty string ("") if it is a root-level entry
     * 
     * @return The id of the {@link Folder} that note is in
     */
    public String getParentId() {
        return record.getString("parentId");
    }
}
