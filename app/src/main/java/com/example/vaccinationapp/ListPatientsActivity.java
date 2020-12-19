package com.example.vaccinationapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vaccinationapp.data.PatientsContract;

public class ListPatientsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String[] projections ={
            PatientsContract.PatientEntry._ID,
            PatientsContract.PatientEntry.COLUMN_NAME,
            PatientsContract.PatientEntry.COLUMN_SYSTEM_ID,
            PatientsContract.PatientEntry.COLUMN_PWD,
            PatientsContract.PatientEntry.COLUMN_AGE,
            PatientsContract.PatientEntry.COLUMN_GENDER,
            PatientsContract.PatientEntry.COLUMN_START_TIME,
            PatientsContract.PatientEntry.COLUMN_STOP_TIME,
            PatientsContract.PatientEntry.COLUMN_DATE
    };

    public static final int LOADER_ID = 88;

    ListView listView;
    PatientsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_patients);

        listView = (ListView) findViewById(R.id.listView_patients);
        adapter = new PatientsAdapter(this, null);

        listView.setAdapter(adapter);
        Intent intent= getIntent();


        if(intent!=null)
            if(intent.hasExtra(Intent.EXTRA_TEXT))
                Toast.makeText(this, intent.getStringExtra(Intent.EXTRA_TEXT), Toast.LENGTH_SHORT).show();


            getLoaderManager().initLoader(LOADER_ID, null, this);

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if(id == LOADER_ID)
            return new CursorLoader(getApplicationContext(),
                    PatientsContract.PatientEntry.CONTENT_URI,
                    projections,
                    null,
                    null,
                    null
            );

        else
            return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

//    @NonNull
//    @Override
//    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
//        if(id == LOADER_ID)
//            return new CursorLoader(getApplicationContext(),
//                    PatientsContract.PatientEntry.CONTENT_URI,
//                    projections,
//                    null,
//                    null,
//                    null
//            );
//
//        else
//            return null;
//    }
//
//    @Override
//    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
//        adapter.swapCursor(data);
//    }
//
//    @Override
//    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
//        adapter.swapCursor(null);
//    }

/*     D/SQL COMMAND: CREATE TABLE patients(
_id INTEGER PRIMARY KEY AUTOINCREMENT,
name TEXT NOT NULL,
sys_id TEXT NOT NULL,
pwd INTEGER,
age INTEGER,
gender INTEGER,
start_time INTEGER,
stop_time INTEGER,
date TEXT,
UNIQUE (sys_id) ON CONFLICT REPLACE);
 */

}