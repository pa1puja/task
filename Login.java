package com.example.christy.projectganga;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener{

    EditText et_email,et_password;
    Button bt_login,bt_register,bt_c_login;

    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<Map> details;

    String email,password;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sp =getSharedPreferences("user", Context.MODE_PRIVATE);

        String username = sp.getString("username","nothing");
        if(!username.equals("nothing")){
            startActivity(new Intent(Login.this,Dashboard_regular_user.class));
        }

        editor = sp.edit();


        details = new ArrayList<Map>();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("users");

        et_email = (EditText) findViewById(R.id.email);
        et_password = (EditText) findViewById(R.id.password);

        bt_login = (Button) findViewById(R.id.login);
        bt_login.setOnClickListener(this);

        bt_c_login = (Button) findViewById(R.id.c_login);
        bt_c_login.setOnClickListener(this);

        bt_register = (Button) findViewById(R.id.register);
        bt_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login:
                email = et_email.getText().toString();
                password = et_password.getText().toString();
                //Toast.makeText(Login.this,"email: "+email+" Password: "+password,Toast.LENGTH_LONG).show();
                if(email.length() !=0 && password.length() !=0) {

                    final Query query = myRef.orderByChild("email").equalTo(email);
                    //Toast.makeText(Register.this,email,Toast.LENGTH_SHORT).show();
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                    //Log.d("ss: ", String.valueOf(dsp.getValue()));
                                    details.add((Map) (dsp.getValue()));
                                }
                                Log.d("mm: ", details.get(0).get("password").toString());
                                if (details.get(0).get("password").toString().equals(password)) {//login success
                                    Toast.makeText(Login.this, "Login Success", Toast.LENGTH_SHORT).show();
                                    editor.putString("username", email);
                                    editor.putString("company_details", "0");


                                    editor.commit();
                                    startActivity(new Intent(Login.this, Dashboard_regular_user.class));
                                    finish();
                                } else {//login failed
                                    Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(Login.this, "Email or password incorrect", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    details.clear();
                }else{
                    Toast.makeText(Login.this,"Enter details without empty",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.register:
                Intent seetha = new Intent(this,Register.class);
                startActivity(seetha);
                break;
            case R.id.c_login:
                Intent seetha2 = new Intent(Login.this,Login2.class);
                startActivity(seetha2);
                break;
        }
    }
}
