package com.example.christy.projectganga;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class User_profile extends AppCompatActivity {

    Button bt_googleMap;
    String name,phone,address,lat,lng;
    TextView tv_name,tv_email;

    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<Map> details;
    String id;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        sp =getSharedPreferences("user", Context.MODE_PRIVATE);
        sp.edit();

        username = sp.getString("username","nothing");

        details = new ArrayList<Map>();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Users");

        tv_name = (TextView) findViewById(R.id.name);
        tv_email = (TextView) findViewById(R.id.email);


        final Query query = myRef.orderByChild("id").equalTo(username);
        //Toast.makeText(Register.this,email,Toast.LENGTH_SHORT).show();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dsp :dataSnapshot.getChildren()) {
                        //Log.d("ss: ", String.valueOf(dsp.getValue()));
                        details.add((Map) (dsp.getValue()));
                    }

                    //Toast.makeText(Water_plant_details_view.this,"ss "+company_id,Toast.LENGTH_SHORT).show();
                    tv_name.setText(details.get(0).get("name").toString());
                    tv_email.setText(details.get(0).get("email").toString());


                }else {
                    Toast.makeText(User_profile.this,"data load failed",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        details.clear();


    }
}
