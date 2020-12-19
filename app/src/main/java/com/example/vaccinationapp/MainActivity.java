package com.example.vaccinationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText centreID ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        centreID =  (EditText) findViewById(R.id.centreIdEditText);
    }

    public void searchId(View view) {
        String id = centreID.getText().toString();

        if(id != null && !id.isEmpty()){
            long centreIdInt = Long.parseLong(id);
            //////code process here //////////
            Intent intent = new Intent(this, ListPatientsActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, id);

            startActivity(intent);

        }

    }
}