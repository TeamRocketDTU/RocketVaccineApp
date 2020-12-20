package com.example.vaccinationapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vaccinationapp.data.PatientsContract;
import com.example.vaccinationapp.utils.VaccineAppUtils;

public class MainActivity extends AppCompatActivity {
    EditText centreID;
    ProgressBar progressBar;

    public static final String CURRENT_CENTRE_ID = "centre_id";
    private static final String[] projections = {
            PatientsContract.PatientEntry.COLUMN_NAME,
            PatientsContract.PatientEntry.COLUMN_SYSTEM_ID,
            PatientsContract.PatientEntry.COLUMN_PWD,
            PatientsContract.PatientEntry.COLUMN_AGE,
            PatientsContract.PatientEntry.COLUMN_GENDER,
            PatientsContract.PatientEntry.COLUMN_START_TIME,
            PatientsContract.PatientEntry.COLUMN_STOP_TIME,
            PatientsContract.PatientEntry.COLUMN_DATE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Login");
        centreID = (EditText) findViewById(R.id.centreIdEditText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void searchId(View view) {

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = getCurrentFocus();

        if (v == null)
            v = new View(getApplicationContext());

        mgr.hideSoftInputFromWindow(v.getWindowToken(), 0, null);

        String id = centreID.getText().toString();

        if(id == null || id.isEmpty()) {
            Toast.makeText(this, "Enter ID Please", Toast.LENGTH_SHORT).show();
            return;
        }

        //////code process here //////////

        SharedPreferences pref = this.getSharedPreferences("com.example.vaccinationapp", Context.MODE_PRIVATE);
        if (pref.contains(CURRENT_CENTRE_ID)) {
            if (pref.getString(CURRENT_CENTRE_ID, "").equals(id)) {
                Log.d("MainActivity", "New data not downloaded");
                startDisplayPatients();
            } else {
                pref.edit().putString(CURRENT_CENTRE_ID,id).apply();
                FetchDataTask task = new FetchDataTask();
                task.execute(VaccineAppUtils.TEST_JSON);
            }
        } else {
            pref.edit().putString(CURRENT_CENTRE_ID,id).apply();
            FetchDataTask task = new FetchDataTask();
            task.execute(VaccineAppUtils.TEST_JSON);
        }
    }

    private void startDisplayPatients(){
        Intent intent = new Intent(getApplicationContext(), ListPatientsActivity.class);
        startActivity(intent);
    }

    private class FetchDataTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            Log.d("MainActiviy","New Data Downloaded");
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Integer n) {
            progressBar.setVisibility(View.INVISIBLE);

            if (n == 0) {
                startDisplayPatients();
            } else
                Toast.makeText(MainActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
        }


        @Override
        protected Integer doInBackground(String... strings) {
            return VaccineAppUtils.writeJSONToDb(getApplicationContext(), strings[0]);
        }
    }
}



//                PatientsContract.PatientEntry.COLUMN_NAME,
//                PatientsContract.PatientEntry.COLUMN_SYSTEM_ID,
//                PatientsContract.PatientEntry.COLUMN_PWD,
//                PatientsContract.PatientEntry.COLUMN_AGE,
//                PatientsContract.PatientEntry.COLUMN_GENDER,
//                PatientsContract.PatientEntry.COLUMN_START_TIME,
//                PatientsContract.PatientEntry.COLUMN_STOP_TIME,
//                PatientsContract.PatientEntry.COLUMN_DATE

