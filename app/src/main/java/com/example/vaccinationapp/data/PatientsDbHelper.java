package com.example.vaccinationapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.vaccinationapp.data.PatientsContract.PatientEntry;

public class PatientsDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "patients.db";
    public static final int DATABASE_VERSION = 1;

    public static final String SQL_CREATE_PATIENTS_TABLE =
            "CREATE TABLE "+ PatientsContract.PatientEntry.TABLE_NAME + "("
                    + PatientsContract.PatientEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + PatientsContract.PatientEntry.COLUMN_NAME      + " TEXT NOT NULL, "
                    + PatientsContract.PatientEntry.COLUMN_SYSTEM_ID + " TEXT NOT NULL, "
                    + PatientsContract.PatientEntry.COLUMN_PWD + " INTEGER, "
                    + PatientsContract.PatientEntry.COLUMN_AGE + " INTEGER, "
                    + PatientsContract.PatientEntry.COLUMN_GENDER+ " INTEGER, "
                    + PatientsContract.PatientEntry.COLUMN_START_TIME + " INTEGER, "
                    + PatientsContract.PatientEntry.COLUMN_STOP_TIME + " INTEGER, "
                    + PatientsContract.PatientEntry.COLUMN_DATE + " INTEGER, "
                    + "UNIQUE (" + PatientsContract.PatientEntry.COLUMN_SYSTEM_ID + ") ON CONFLICT REPLACE);";

    public static final String SQL_DELETE_PATIENTS_TABLE = "DROP TABLE IF EXISTS " + PatientEntry.TABLE_NAME;


    public PatientsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PATIENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_PATIENTS_TABLE);
        onCreate(db);
    }
}
