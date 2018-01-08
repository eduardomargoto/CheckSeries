package br.com.etm.checkseries.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by eduardo on 07/01/18.
 */

public class DbProvider extends ContentProvider {

    private static final int SHOW = 100;
    private static final int SHOW_BY_ID = 101;

    private static final int EPISODE = 200;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private DbHelper dbHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(Contract.AUTHORITY, Contract.PATH_SHOW, SHOW);
        matcher.addURI(Contract.AUTHORITY, Contract.PATH_EPISODE, EPISODE);
        matcher.addURI(Contract.AUTHORITY, Contract.PATH_SHOW_BY_ID, SHOW_BY_ID);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor returnCursor;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        switch (uriMatcher.match(uri)) {
            case SHOW:
                returnCursor = db.query(
                        Contract.Show.TABLE_NAME + ", " + Contract.Episode.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case SHOW_BY_ID:
                returnCursor = db.query(
                        Contract.Show.TABLE_NAME,
                        projection,
                        Contract.Show._ID + " = ? ",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI:" + uri);
        }

        Context context = getContext();
        if (context != null){
            returnCursor.setNotificationUri(context.getContentResolver(), uri);
        }

        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case SHOW:
                return Contract.Show.CONTENT_TYPE;
            case SHOW_BY_ID:
                return Contract.Show.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri;

        switch (uriMatcher.match(uri)) {
            case SHOW:
                db.insert(
                        Contract.Show.TABLE_NAME,
                        null,
                        values
                );
                returnUri = Contract.Show.URI;
                break;
            case EPISODE:
                db.insert(
                        Contract.Episode.TABLE_NAME,
                        null,
                        values
                );
                returnUri = Contract.Show.URI;
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI:" + uri);
        }

        Context context = getContext();
        if (context != null){
            context.getContentResolver().notifyChange(uri, null);
        }

        db.close();
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted;

        if (null == selection) {
            selection = "1";
        }
        switch (uriMatcher.match(uri)) {
            case SHOW:
                rowsDeleted = db.delete(
                        Contract.Show.TABLE_NAME,
                        selection,
                        selectionArgs
                );

                break;
            case EPISODE:
                rowsDeleted = db.delete(
                        Contract.Episode.TABLE_NAME,
                        selection,
                        selectionArgs
                );

                break;
            default:
                throw new UnsupportedOperationException("Unknown URI:" + uri);
        }

        if (rowsDeleted != 0) {
            Context context = getContext();
            if (context != null){
                context.getContentResolver().notifyChange(uri, null);
            }
        }

        db.close();
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
            final SQLiteDatabase db = dbHelper.getWritableDatabase();
            int rowsUpdated = 0;
            switch (uriMatcher.match(uri)) {
                case SHOW:
                    rowsUpdated = db.update(
                            Contract.Show.TABLE_NAME,
                            values,
                            selection,
                            selectionArgs
                    );
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown URI:" + uri);
            }

            if (rowsUpdated != 0) {
                Context context = getContext();
                if (context != null){
                    context.getContentResolver().notifyChange(uri, null);
                }
            }

            db.close();
            return rowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case SHOW:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        db.insert(
                                Contract.Show.TABLE_NAME,
                                null,
                                value
                        );
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                Context context = getContext();
                if (context != null) {
                    context.getContentResolver().notifyChange(uri, null);
                }

                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        dbHelper.close();
        super.shutdown();
    }
}
