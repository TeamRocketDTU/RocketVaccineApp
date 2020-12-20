package com.example.vaccinationapp;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vaccinationapp.data.PatientsContract;

public class PatientsAdapter extends CursorAdapter {


    public PatientsAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.d("Adapter","new View called");
        return LayoutInflater.from(context).inflate(R.layout.patient_list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.d("Adapter","bind View called");
//        int ind_id = cursor.getColumnIndex(PatientsContract.PatientEntry._ID);
        int ind_name = cursor.getColumnIndex(PatientsContract.PatientEntry.COLUMN_NAME);
        int ind_sysId = cursor.getColumnIndex(PatientsContract.PatientEntry.COLUMN_SYSTEM_ID);
        int ind_gender = cursor.getColumnIndex(PatientsContract.PatientEntry.COLUMN_GENDER);

        ImageView img = (ImageView) view.findViewById(R.id.image_person);

        img.setImageResource(R.drawable.ic_baseline_account_box_24);

        TextView tvName = (TextView) view.findViewById(R.id.list_item_patient_name_display);
        TextView tvSysId = (TextView) view.findViewById(R.id.list_item_patient_sysid_display);
        TextView tvGender = (TextView) view.findViewById(R.id.list_item_patient_gender_display);

        tvName.setText(cursor.getString(ind_name));
        tvSysId.setText(cursor.getString(ind_sysId));



        switch (cursor.getInt(ind_gender)){
            case PatientsContract.PatientEntry.GENDER_MALE:
                tvGender.setText("MALE");
                break;
            case PatientsContract.PatientEntry.GENDER_FEMALE:
                tvGender.setText("FEMALE");
                break;
            case PatientsContract.PatientEntry.GENDER_OTHER:
                tvGender.setText("OTHER");
        }


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