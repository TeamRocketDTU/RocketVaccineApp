package com.example.vaccinationapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.vaccinationapp.data.PatientsContract;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VaccineAppUtils {
    public static final String TEST_JSON ="{\"date\": 1608369923, \"center id\": \"9246859317\", \"number\": 5, \"patients\": [{\"name\": \"Namita Jhaveri\", \"sysid\": \"6ZOC0R\", \"pwd\": true, \"age\": 31, \"gender\": \"O\", \"time slot\": [1609482923, 1609490123]}, {\"name\": \"Vicky Mukti\", \"sysid\": \"2TXA0D\", \"pwd\": true, \"age\": 80, \"gender\": \"M\", \"time slot\": [1609153968, 1609161168]}, {\"name\": \"Mohit Nagma\", \"sysid\": \"DOPIGB\", \"pwd\": false, \"age\": 28, \"gender\": \"M\", \"time slot\": [1610630166, 1610637366]}, {\"name\": \"Parmer Balay\", \"sysid\": \"ICLEYS\", \"pwd\": true, \"age\": 35, \"gender\": \"M\", \"time slot\": [1608538561, 1608545761]}, {\"name\": \"Naresh Anil\", \"sysid\": \"9C2MD9\", \"pwd\": false, \"age\": 59, \"gender\": \"F\", \"time slot\": [1609225975, 1609233175]}]}";

    public static final String URL_FOR_GET ="https://jsonplaceholder.typicode.com/todos/1";

    public static final String URL_FOR_POST = "";
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");




    public static int writeJSONToDb(Context context, String jsonString){

        try {

            JSONObject obj = new JSONObject(jsonString);

            long date = obj.getLong("date");
            JSONArray arr = obj.getJSONArray("patients");

            ContentValues vales = new ContentValues();
            JSONObject patientObj;

            for (int i = 0; i < arr.length(); i++) {
                vales.clear();
                patientObj = arr.getJSONObject(i);

                vales.put(PatientsContract.PatientEntry.COLUMN_NAME, patientObj.getString("name"));
                vales.put(PatientsContract.PatientEntry.COLUMN_SYSTEM_ID, patientObj.getString("sysid"));

                if (patientObj.getBoolean("pwd"))
                    vales.put(PatientsContract.PatientEntry.COLUMN_PWD, PatientsContract.PatientEntry.PWD_TRUE);
                else
                    vales.put(PatientsContract.PatientEntry.COLUMN_PWD, PatientsContract.PatientEntry.PWD_FALSE);


                vales.put(PatientsContract.PatientEntry.COLUMN_AGE, patientObj.getInt("age"));

                if (patientObj.getString("gender").equals("M"))
                    vales.put(PatientsContract.PatientEntry.COLUMN_GENDER, PatientsContract.PatientEntry.GENDER_MALE);
                else if (patientObj.getString("gender").equals("F"))
                    vales.put(PatientsContract.PatientEntry.COLUMN_GENDER, PatientsContract.PatientEntry.GENDER_FEMALE);
                else
                    vales.put(PatientsContract.PatientEntry.COLUMN_GENDER, PatientsContract.PatientEntry.GENDER_OTHER);

                vales.put(PatientsContract.PatientEntry.COLUMN_START_TIME, patientObj.getJSONArray("time slot").getLong(0));
                vales.put(PatientsContract.PatientEntry.COLUMN_STOP_TIME, patientObj.getJSONArray("time slot").getLong(1));
                vales.put(PatientsContract.PatientEntry.COLUMN_DATE, date);

                context.getContentResolver().insert(PatientsContract.PatientEntry.CONTENT_URI, vales);
            }
                return 0;
        }
        catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public static String getResponseFromServer(){
        Log.d("ServerResone","Called");
        OkHttpClient client = new OkHttpClient();
         final StringBuilder resultFromServer = new StringBuilder();

        final Request request = new Request.Builder()
                .url(URL_FOR_GET)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    Log.d("Rseponse","Received");

                }
            }
        });
        return resultFromServer.toString();
    }

    public static int sendDataToServer(String json) {
        int responseVode = 0;
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(URL_FOR_POST)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if(!response.isSuccessful())
                responseVode = -1;
        } catch (IOException e) {
            e.printStackTrace();
            responseVode = -1;
        }
        return  responseVode;
    }
}
