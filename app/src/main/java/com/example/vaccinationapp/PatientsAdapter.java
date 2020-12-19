package com.example.vaccinationapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.vaccinationapp.data.PatientsContract;

public class PatientsAdapter extends CursorAdapter {


    public PatientsAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.patient_list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int ind_id = cursor.getColumnIndex(PatientsContract.PatientEntry._ID);
        int ind_name = cursor.getColumnIndex(PatientsContract.PatientEntry.COLUMN_NAME);
        int ind_sysId = cursor.getColumnIndex(PatientsContract.PatientEntry.COLUMN_SYSTEM_ID);
        int ind_pwd = cursor.getColumnIndex(PatientsContract.PatientEntry.COLUMN_PWD);
        int int_age = cursor.getColumnIndex(PatientsContract.PatientEntry.COLUMN_DATE);

        TextView tv = (TextView) view.findViewById(R.id.list_item_patient_display);

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(""+cursor.getInt(ind_id)+"-");
        stringBuilder.append(cursor.getString(ind_name)+"-");
        stringBuilder.append(cursor.getString(ind_sysId));

        switch (cursor.getInt(ind_pwd)){
            case PatientsContract.PatientEntry.PWD_TRUE:
                stringBuilder.append(" true -");
                break;
            case PatientsContract.PatientEntry.PWD_FALSE:
                stringBuilder.append(" false -");
        }

        stringBuilder.append(""+cursor.getInt(int_age));

        tv.setText(stringBuilder.toString());

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