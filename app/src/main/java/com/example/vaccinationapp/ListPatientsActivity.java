package com.example.vaccinationapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vaccinationapp.data.PatientsContract;

public class ListPatientsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String[] projections = {
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

        getSupportActionBar().setTitle("Patients");

        listView = (ListView) findViewById(R.id.listView_patients);
        adapter = new PatientsAdapter(this, null);

        listView.setAdapter(adapter);
        Intent intent = getIntent();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = PatientsContract.PatientEntry.getUriWithAppendedId(id);

                Intent intent = new Intent(getApplicationContext(), DisplayPatientsActivity.class);
                intent.setData(uri);

                startActivity(intent);

            }
        });


        if (intent != null)
            if (intent.hasExtra(Intent.EXTRA_TEXT))
                Toast.makeText(this, intent.getStringExtra(Intent.EXTRA_TEXT), Toast.LENGTH_SHORT).show();


        getLoaderManager().initLoader(LOADER_ID, null, this);

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d("Loader", "Calling loader create");
        if (id == LOADER_ID)
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
        Log.d("Loader", "Loading finished");
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_list_patients, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuItem_delete_all:
                getContentResolver().delete(PatientsContract.PatientEntry.CONTENT_URI,null,null);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
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
