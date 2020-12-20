package com.example.vaccinationapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vaccinationapp.data.PatientsContract;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DisplayPatientsActivity extends AppCompatActivity {

    LinearLayout linearLayout ;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_patients);

        getSupportActionBar().setTitle("Patient's Details");

        linearLayout = (LinearLayout) findViewById(R.id.display_patients_linearLayout);
        progressBar = (ProgressBar) findViewById(R.id.display_patients_progressBar);

        Intent intent = getIntent();

        Uri uri = intent.getData();
         FetchPatientDetailsTask task =new FetchPatientDetailsTask();
         task.execute(uri);


    }

    private void showLinearLayout(){
        progressBar.setVisibility(View.INVISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
    }

    private void showProgressBar(){

        linearLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void patientAbsent(View view){
        new AlertDialog.Builder(this)
                .setTitle("Cancel Appointment")
                .setMessage("Do you want to cancel the patient's appointment?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"Cancel",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ListPatientsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    
    public void patientPresent(View view){
        Intent intent = new Intent(this,PatientPresentActivity.class);
        TextView tvSysId = (TextView) findViewById(R.id.sysIdTextView);
        intent.putExtra(Intent.EXTRA_TEXT, tvSysId.getText().toString());

        startActivity(intent);
    }

    
    

    private class FetchPatientDetailsTask extends AsyncTask<Uri, Void, Cursor>{

        @Override
        protected void onPreExecute() {
            showProgressBar();
        }

        @Override
        protected Cursor doInBackground(Uri... uris) {
            Cursor cursor = getContentResolver().query(uris[0],null,null,null, null);
            return cursor;

        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            cursor.moveToFirst();
            int ind_name = cursor.getColumnIndex(PatientsContract.PatientEntry.COLUMN_NAME);
            int ind_sysId = cursor.getColumnIndex(PatientsContract.PatientEntry.COLUMN_SYSTEM_ID);
            int ind_pwd = cursor.getColumnIndex(PatientsContract.PatientEntry.COLUMN_PWD);
            int ind_age = cursor.getColumnIndex(PatientsContract.PatientEntry.COLUMN_AGE);
            int ind_gender = cursor.getColumnIndex(PatientsContract.PatientEntry.COLUMN_GENDER);
            int ind_date = cursor.getColumnIndex(PatientsContract.PatientEntry.COLUMN_DATE);
            int ind_startTime = cursor.getColumnIndex(PatientsContract.PatientEntry.COLUMN_START_TIME);
            int ind_stopTime = cursor.getColumnIndex(PatientsContract.PatientEntry.COLUMN_STOP_TIME);

            SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("E, dd MMM yyyy");

            ImageView img = (ImageView) findViewById(R.id.imageView);

            img.setImageResource(R.drawable.ic_baseline_account_circle_24);

            TextView tvName = (TextView) findViewById(R.id.nametTextView);
            TextView tvSysId = (TextView) findViewById(R.id.sysIdTextView);
            TextView tvPwd = (TextView) findViewById(R.id.pwdTextView);
            TextView tvAge = (TextView) findViewById(R.id.ageTextView);
            TextView tvGender = (TextView) findViewById(R.id.genderTextView);
            TextView tvDate = (TextView) findViewById(R.id.dateTextView);
            TextView tvSlot = (TextView) findViewById(R.id.timeSlotTextView);

            tvName.setText("Name: "+cursor.getString(ind_name));
            tvSysId.setText("System ID: "+cursor.getString(ind_sysId));

            switch (cursor.getInt(ind_pwd)){
                case PatientsContract.PatientEntry.PWD_TRUE:
                    tvPwd.setText("PWD: YES");
                case PatientsContract.PatientEntry.PWD_FALSE:
                    tvPwd.setText("PWD: NO");
            }

            tvAge.setText("Age: "+cursor.getInt(ind_age));

            switch (cursor.getInt(ind_gender)){
                case PatientsContract.PatientEntry.GENDER_MALE:
                    tvGender.setText("Gender: MALE");
                    break;
                case PatientsContract.PatientEntry.GENDER_FEMALE:
                    tvGender.setText("Gender: FEMALE");
                    break;
                case PatientsContract.PatientEntry.GENDER_OTHER:
                    tvGender.setText("Gender: OTHER");
            }

            Date date1 = new Date(cursor.getLong(ind_date));

            tvDate.setText(dateFormat2.format(date1));

            Date startTimeDate = new Date(cursor.getLong(ind_startTime));
            Date stopTimeDate = new Date(cursor.getLong(ind_stopTime));

            tvSlot.setText("Slot: "+ dateFormat1.format(startTimeDate)+" - "+dateFormat1.format(stopTimeDate));
            showLinearLayout();
        }
    }
}