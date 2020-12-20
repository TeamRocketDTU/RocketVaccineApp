package com.example.vaccinationapp.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class PatientsContract {
    public static final String CONTENT_AUTHORITY = "com.example.vaccinationapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final String PATH_PATIENTS = "patients";

    public static final class PatientEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_PATIENTS)
                .build();


        public static final String _ID = BaseColumns._ID;

        public static final String TABLE_NAME = "patients";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_SYSTEM_ID = "sys_id";

        public static final String COLUMN_AGE = "age";
        public static final String COLUMN_PWD = "pwd";
        public static final String COLUMN_GENDER = "gender";

        public static final String COLUMN_START_TIME = "start_time";
        public static final String COLUMN_STOP_TIME = "stop_time";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_CURRENT_STATE ="current_state";

        public static final int GENDER_MALE = 0;
        public static final int GENDER_FEMALE = 1;
        public static final int GENDER_OTHER = 2;

        public static final int PWD_TRUE = 1;
        public static final int PWD_FALSE = 0;

        public static final int CURRENT_STATE_PENDING = 0;
        public static final int CURRENT_STATE_DONE = 1;
        public static final int CURRENT_STATE_CANCELLED = 2;

        public static Uri getUriWithAppendedId(long id) {
            Uri uriWithId = ContentUris.withAppendedId(CONTENT_URI,id);
            return uriWithId;
        }


    }


}
