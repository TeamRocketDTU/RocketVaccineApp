package com.example.vaccinationapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vaccinationapp.data.PatientsContract;
import com.example.vaccinationapp.utils.VaccineAppUtils;

public class PatientPresentActivity extends AppCompatActivity {
    EditText edtTxt ;
    Button finBtn;

    Uri currentPatient;
    String sysId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_present);
        getSupportActionBar().setTitle("Administer the Vaccine");

        currentPatient = getIntent().getData();
        sysId = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        edtTxt = (EditText) findViewById(R.id.lotNoEditText);
        finBtn = (Button) findViewById(R.id.finBtn);
    }


    public void finishVaccination(View view) {
        String lotNo = edtTxt.getText().toString();
        Toast.makeText(this, lotNo, Toast.LENGTH_SHORT).show();
        DeliverLotNumberTask task = new DeliverLotNumberTask();
        task.execute("");

    }

    public void enterLotNumber(View view) {
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = getCurrentFocus();

        if (v == null)
            v = new View(getApplicationContext());

        mgr.hideSoftInputFromWindow(v.getWindowToken(), 0, null);

        String lotNo = edtTxt.getText().toString();
        if(lotNo!=null && !lotNo.isEmpty()){
                finBtn.setEnabled(true);
        }
        else{
            Toast.makeText(this, "Please Enter a valid lot number", Toast.LENGTH_SHORT).show();
        }
    }

    private class DeliverLotNumberTask extends AsyncTask<String, Void, Integer>{
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Integer doInBackground(String... strings) {
            ContentValues values = new ContentValues();
            values.put(PatientsContract.PatientEntry.COLUMN_CURRENT_STATE, PatientsContract.PatientEntry.CURRENT_STATE_DONE);

            getContentResolver().update(currentPatient,values,null);
            String res = VaccineAppUtils.getResponseFromServer();
            Log.d("Response", res);
            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Intent intent = new Intent(getApplicationContext(), ListPatientsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }
}

