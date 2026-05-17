package com.example.myapplication;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DemoContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.myapplication.demo.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/users");

    private static final String DB_NAME = "demo_provider.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_USERS = "users";

    private static final int USERS = 1;
    private static final int USER_ID = 2;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, "users", USERS);
        URI_MATCHER.addURI(AUTHORITY, "users/#", USER_ID);
    }

    private DemoDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DemoDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int match = URI_MATCHER.match(uri);

        if (match == USER_ID) {
            long id = ContentUris.parseId(uri);
            selection = appendSelection(selection, "_id = ?");
            selectionArgs = appendSelectionArgs(selectionArgs, String.valueOf(id));
        } else if (match != USERS) {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        String orderBy = TextUtils.isEmpty(sortOrder) ? "_id DESC" : sortOrder;
        Cursor cursor = db.query(TABLE_USERS, projection, selection, selectionArgs,
                null, null, orderBy);

        if (getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), CONTENT_URI);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = URI_MATCHER.match(uri);
        if (match == USERS) {
            return "vnd.android.cursor.dir/vnd." + AUTHORITY + ".users";
        }
        if (match == USER_ID) {
            return "vnd.android.cursor.item/vnd." + AUTHORITY + ".users";
        }
        throw new IllegalArgumentException("Unknown URI: " + uri);
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (URI_MATCHER.match(uri) != USERS) {
            throw new IllegalArgumentException("Insert unknown URI: " + uri);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insert(TABLE_USERS, null, values);
        if (id < 0) {
            return null;
        }
        Uri newUri = ContentUris.withAppendedId(CONTENT_URI, id);
        notifyDataChanged();
        return newUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = URI_MATCHER.match(uri);

        if (match == USER_ID) {
            long id = ContentUris.parseId(uri);
            selection = appendSelection(selection, "_id = ?");
            selectionArgs = appendSelectionArgs(selectionArgs, String.valueOf(id));
        } else if (match != USERS) {
            throw new IllegalArgumentException("Delete unknown URI: " + uri);
        }

        int count = db.delete(TABLE_USERS, selection, selectionArgs);
        if (count > 0) {
            notifyDataChanged();
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = URI_MATCHER.match(uri);

        if (match == USER_ID) {
            long id = ContentUris.parseId(uri);
            selection = appendSelection(selection, "_id = ?");
            selectionArgs = appendSelectionArgs(selectionArgs, String.valueOf(id));
        } else if (match != USERS) {
            throw new IllegalArgumentException("Update unknown URI: " + uri);
        }

        int count = db.update(TABLE_USERS, values, selection, selectionArgs);
        if (count > 0) {
            notifyDataChanged();
        }
        return count;
    }

    private void notifyDataChanged() {
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        }
    }

    private String appendSelection(@Nullable String oldSelection, @NonNull String newClause) {
        if (TextUtils.isEmpty(oldSelection)) {
            return newClause;
        }
        return "(" + oldSelection + ") AND " + newClause;
    }

    private String[] appendSelectionArgs(@Nullable String[] oldArgs, @NonNull String newArg) {
        if (oldArgs == null || oldArgs.length == 0) {
            return new String[]{newArg};
        }
        String[] result = new String[oldArgs.length + 1];
        System.arraycopy(oldArgs, 0, result, 0, oldArgs.length);
        result[oldArgs.length] = newArg;
        return result;
    }

    private static class DemoDbHelper extends SQLiteOpenHelper {

        DemoDbHelper(@Nullable Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_USERS + " ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "name TEXT NOT NULL,"
                    + "age INTEGER NOT NULL"
                    + ")");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        }
    }
}
