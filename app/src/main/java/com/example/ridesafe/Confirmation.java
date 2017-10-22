package com.example.ridesafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Confirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        Button btnCancel = (Button) findViewById(R.id.cancelButton);
        Button btnCheck = (Button) findViewById(R.id.checkLocation);
        Button btnPanic = (Button) findViewById(R.id.panicButton);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Backupload.class);
                stopService(intent);
            }
        });



    }
}
