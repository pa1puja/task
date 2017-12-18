package com.example.christy.projectganga;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.Dash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class Register extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    EditText et_email,et_password,et_name;
    Spinner sp_type;
    Button bt_register,bt_login,bt_c_register;

    FirebaseDatabase database;
    DatabaseReference myRef;

    String name,email,password,type;

    ArrayList<Map> details;

    SharedPreferences sp;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sp =getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = sp.edit();

        details = new ArrayList<Map>();



        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("users");

        et_email = (EditText) findViewById(R.id.email);
        et_name = (EditText) findViewById(R.id.name);
        et_password = (EditText) findViewById(R.id.password);


        bt_register = (Button) findViewById(R.id.register);
        bt_register.setOnClickListener(this);

        bt_c_register = (Button) findViewById(R.id.c_register);
        bt_c_register.setOnClickListener(this);

        bt_login = (Button) findViewById(R.id.login);
        bt_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                email = et_email.getText().toString();
                password = et_password.getText().toString();
                name = et_name.getText().toString();
                //Log.d("l1 ",type);
                if(email.length() !=0 && password.length() !=0 && name.length() != 0) {
                    final Query query = myRef.orderByChild("email").equalTo(email);

                    //Toast.makeText(Register.this,email,Toast.LENGTH_SHORT).show();
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Log.d("ram ","s");
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                    details.add((Map) (dsp.getValue()));
                                }
                                Log.d("l2 ", "s");
                                if (details.get(0).get("email").toString().equals(email)) {

                                    Toast.makeText(Register.this, "email exist", Toast.LENGTH_SHORT).show();
                                } else {
                                    String key = myRef.push().getKey();
                                    myRef.child(key).child("email").setValue(email);
                                    myRef.child(key).child("name").setValue(name);
                                    myRef.child(key).child("password").setValue(password);
                                    myRef.child(key).child("type").setValue(type);
                                    Toast.makeText(Register.this, "Registration success", Toast.LENGTH_SHORT).show();
                                    //redirecting to login

                                    editor.putString("username", email);
                                    editor.putString("company_id", "0");

                                    Intent seetha = new Intent(Register.this, Dashboard_regular_user.class);
                                    startActivity(seetha);
                                    finish();
                                }

                            } else {
                                String key = myRef.push().getKey();
                                myRef.child(key).child("email").setValue(email);
                                myRef.child(key).child("name").setValue(name);
                                myRef.child(key).child("password").setValue(password);

                                editor.putString("username", email);
                                editor.putString("company_id", "0");

                                Toast.makeText(Register.this, "Registration success", Toast.LENGTH_SHORT).show();
                                Intent seetha = new Intent(Register.this, Dashboard_regular_user.class);
                                startActivity(seetha);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });
                    details.clear();
                }else{
                    Toast.makeText(Register.this,"Enter details without empty",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.login:
                Intent seetha = new Intent(this,Login.class);
                startActivity(seetha);
                break;
            case R.id.c_register:
                Intent seetha2 = new Intent(Register.this,Register2.class);
                startActivity(seetha2);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        type = parent.getItemAtPosition(pos).toString();
        //Toast.makeText(Register.this,"ss "+type,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
