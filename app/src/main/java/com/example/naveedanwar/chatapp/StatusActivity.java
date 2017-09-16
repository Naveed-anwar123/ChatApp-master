package com.example.naveedanwar.chatapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText status;
    private Button update;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
private ProgressDialog pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        toolbar = (Toolbar)findViewById(R.id.include2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        status = (EditText)findViewById(R.id.statusname);
        update = (Button)findViewById(R.id.updateStatus);
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        pg = new ProgressDialog(this);
        pg.setTitle("Status Update");
        pg.setMessage("Please wait.....");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inputStatus = status.getText().toString();
                if(!inputStatus.equals("")){
                    pg.show();
                    Toast.makeText(StatusActivity.this,inputStatus,Toast.LENGTH_LONG).show();

                    databaseReference.child("status").setValue(inputStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            //String url = task.getResult().downloadUri().toString();

                            if(task.isSuccessful()){
                                pg.dismiss();
                            }
                        }
                    });
                }

            }
        });
    }
}
