package com.example.jayvote.AdminPanel;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jayvote.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.HashMap;

public class Admin_Item_0 extends AppCompatActivity {
    //For Admin Create this activity to upload data on firebase
    EditText title;
    Button upload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_item0);
        title = (EditText) findViewById(R.id.title);
        upload = (Button) findViewById(R.id.upload_btn);


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Item_0").push();
                HashMap<String, Object> hashref = new HashMap();
                String Title = title.getText().toString();
                hashref.put("votes", "0");
                hashref.put("title", Title);
                hashref.put("Percentage", "0");
                String getRef = ref.toString();
                String key = getRef.split("Item_0/")[1].substring(0);

                hashref.put("key", key.toString());

                ref.setValue(hashref).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();

                    }
                });


            }
        });
    }

}
