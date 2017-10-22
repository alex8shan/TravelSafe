package com.example.ridesafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PanicButton extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panic_button);

        Button btnCancel = (Button) findViewById(R.id.button5);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Backupload.class);
                stopService(intent);
            }
        });

        Button btnPanic = (Button) findViewById(R.id.button4);
        btnPanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference mRoot = FirebaseDatabase.getInstance().getReference();
                DatabaseReference mChild = mRoot.child("users/" + LoginActivity.Authid + "/panic");
                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();
                mChild.child("time").setValue(ts);
                mChild.child("lat").setValue(Backupload.lastPos.latitude);
                mChild.child("lon").setValue(Backupload.lastPos.longitude);
            }
        });
    }


}
