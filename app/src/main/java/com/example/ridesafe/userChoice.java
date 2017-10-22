package com.example.ridesafe;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class userChoice extends AppCompatActivity {
    private final int PICK_CONTACT = 2345;
    private String contactNumber = "";
    private String message = "";
    private int minutes = 0;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference childRef = mRootRef.child("users/" + LoginActivity.Authid);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_choice);

        Button btnContact = (Button) findViewById(R.id.btnContact);
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        Button btnNext = (Button) findViewById(R.id.btnConfirm);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edtMsg = (EditText) findViewById(R.id.edtMsg);
                EditText edtMin = (EditText) findViewById(R.id.editText3);
                message = edtMsg.getText().toString();
                minutes = Integer.parseInt(edtMin.getText().toString());
                if (contactNumber != "" && message != "" && minutes != 0){
                    childRef.child("contact").setValue(contactNumber);
                    childRef.child("message").setValue(message);
                    childRef.child("minutes").setValue(minutes);
                }
                Intent intent = new Intent(getApplicationContext(), Backupload.class);
                intent.putExtra("ID", LoginActivity.Authid);
                startService(intent);
                //here it should be changed to Confirmation
                intent = new Intent(getApplicationContext(), PanicButton.class);
                startActivity(intent);
                userChoice.this.finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                Uri contactData = data.getData();
                String number = "";
                Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
                cursor.moveToFirst();
                String hasPhone = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                String contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                if (hasPhone.equals("1")) {
                    Cursor phones = getContentResolver().query
                            (ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                            + " = " + contactId, null, null);
                    while (phones.moveToNext()) {
                        number = phones.getString(phones.getColumnIndex
                                (ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[-() ]", "");
                    }
                    phones.close();
                    //Do something with number
                    contactNumber = number;
                    Toast.makeText(getApplicationContext(), number, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "This contact has no phone number", Toast.LENGTH_LONG).show();
                }
                cursor.close();
            }
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
    }
}

