package com.example.naveedanwar.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by Naveed Anwar on 10/09/2017.
 */

public class RegisterActivity extends AppCompatActivity{

    private Button create;
    private EditText username , email , password;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference firebaseDatabase;
    private Toolbar tb;
    private ProgressDialog pg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username =(EditText)findViewById(R.id.reg_username);
        email =(EditText)findViewById(R.id.reg_email);
        password =(EditText)findViewById(R.id.reg_password);
        create= (Button)findViewById(R.id.create_account);
        mAuth = FirebaseAuth.getInstance();
        tb =(Toolbar)findViewById(R.id.include);
        setSupportActionBar(tb);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pg = new ProgressDialog(this);
        pg.setTitle("Registering Users");
        pg.setMessage("Creating account,Please wait...");
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname = username.getText().toString();
                String uemail = email.getText().toString();
                String upassword = password.getText().toString();

                if(!uname.equals("") && !uemail.equals("") && !upassword.equals(""))
                {
                    startRegister(uname,uemail,upassword);
                }
            }
        });
    }

    public void startRegister(final String uname , String uemail , String upassword){
        pg.show();
        mAuth.createUserWithEmailAndPassword(uemail, upassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = firebaseUser.getUid();
                        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);


                        HashMap<String,String> hashMap = new HashMap<String, String>();
                        hashMap.put("username",uname);
                        hashMap.put("status","Hi there!");
                        hashMap.put("image","default");
                        hashMap.put("thumbnail_image","default");

                        firebaseDatabase.setValue(hashMap);


                      //  Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            pg.dismiss();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();
                        }

                    }
                });
    }



}
