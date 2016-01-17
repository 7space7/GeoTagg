package com.ua.viktor.geotagg.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by viktor on 16.01.16.
 */
public class TagsContract {
    public static final String CONTENT_AUTHORITY = "com.ua.viktor.geotagg.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class TagEntry implements BaseColumns {
        // table name
        public static final String TABLE_TAGS = "tag";
        // columns
        public static final String _ID = "_id";
        public static final String COLUMN_ICON = "icon";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_COORD_LAT = "coord_lat";
        public static final String COLUMN_COORD_LONG = "coord_long";
        public static final String COLUMN_DATE = "date";
        // create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_TAGS).build();
        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_TAGS;
        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_TAGS;

        // for building URIs on insertion
        public static Uri buildTagsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
