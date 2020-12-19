package com.example.vaccinationapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PatientsProvider extends ContentProvider {

    public static final int CODE_PATIENTS = 100;
    public static final int CODE_PATIENTS_WITH_ID = 101;

    public static final String CONTENT_DIR_BASE_TYPE = "vnd.android.cursor.dir";
    public static final String CONTENT_ITEM_BASE_TYPE = "vnd.android.cursor.item";

    public static final String CONTENT_LIST_TYPE =  CONTENT_DIR_BASE_TYPE+"/"+  PatientsContract.CONTENT_AUTHORITY+ "/"+PatientsContract.PATH_PATIENTS;
    public static final String CONTENT_ITEM_TYPE = CONTENT_ITEM_BASE_TYPE+"/"+ PatientsContract.CONTENT_AUTHORITY+ "/"+PatientsContract.PATH_PATIENTS;

    private PatientsDbHelper patientsDbHelper;
    public static final UriMatcher matcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher= new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(PatientsContract.CONTENT_AUTHORITY, PatientsContract.PatientEntry.TABLE_NAME,CODE_PATIENTS);
        matcher.addURI(PatientsContract.CONTENT_AUTHORITY, PatientsContract.PatientEntry.TABLE_NAME+"/#", CODE_PATIENTS_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        patientsDbHelper = new PatientsDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase  db = patientsDbHelper.getReadableDatabase();
        Cursor cursor;
        switch(matcher.match(uri)){
            case CODE_PATIENTS:
               cursor = db.query(PatientsContract.PatientEntry.TABLE_NAME, projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case CODE_PATIENTS_WITH_ID:
                selection = PatientsContract.PatientEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(PatientsContract.PatientEntry.TABLE_NAME, projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (matcher.match(uri)){
            case CODE_PATIENTS:
                return CONTENT_ITEM_TYPE;
            case CODE_PATIENTS_WITH_ID:
                return CONTENT_LIST_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase  db = patientsDbHelper.getWritableDatabase();
        long idInserted;
        switch(matcher.match(uri)){
            case CODE_PATIENTS:
                idInserted = db.insert(PatientsContract.PatientEntry.TABLE_NAME,null, values);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if( idInserted!=-1 )
            getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri,idInserted);

    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted;

        SQLiteDatabase db = patientsDbHelper.getWritableDatabase();

        /*
         * If we pass null as the selection to SQLiteDatabase#delete, our entire table will be
         * deleted. However, if we do pass null and delete all of the rows in the table, we won't
         * know how many rows were deleted. According to the documentation for SQLiteDatabase,
         * passing "1" for the selection will delete all rows and return the number of rows
         * deleted, which is what the caller of this method expects.
         */
        if (null == selection) selection = "1";

        switch (matcher.match(uri)) {

            case CODE_PATIENTS:
                numRowsDeleted = db.delete(PatientsContract.PatientEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case CODE_PATIENTS_WITH_ID:
                selection = PatientsContract.PatientEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};

                numRowsDeleted = db.delete(PatientsContract.PatientEntry.TABLE_NAME, selection,selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase  db = patientsDbHelper.getWritableDatabase();
        int updatedRows;
        switch(matcher.match(uri)){
            case CODE_PATIENTS:
                updatedRows= db.update(PatientsContract.PatientEntry.TABLE_NAME, values,selection,selectionArgs);
                break;

            case CODE_PATIENTS_WITH_ID:
                selection = PatientsContract.PatientEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                updatedRows = db.update(PatientsContract.PatientEntry.TABLE_NAME, values ,selection,selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if( updatedRows!=0 )
            getContext().getContentResolver().notifyChange(uri, null);

        return updatedRows;
    }
}
