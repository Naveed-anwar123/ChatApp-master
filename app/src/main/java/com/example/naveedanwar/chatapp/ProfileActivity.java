package com.example.naveedanwar.chatapp;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {



    private TextView name , status, count;
    private ImageView imageView,online;
    private Button sendFriendRequest;
    String uid;
    private DatabaseReference databaseReference , requestReference , friendReference,notificationReference;
    private ProgressDialog pg;
    private Button sendbtn , declinebtn;
    private String current_status;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        uid = getIntent().getStringExtra("user_id");
        name = (TextView)findViewById(R.id.dpname);
        status = (TextView)findViewById(R.id.dpstatus);
        count = (TextView)findViewById(R.id.dpcount);
        imageView = (ImageView)findViewById(R.id.dp);
        //  Typeface face= Typeface.createFromAsset(getAssets(), "fonts/Pacifico-Regular.ttf");
        // name.setTypeface(face);
        sendbtn = (Button)findViewById(R.id.sendbtn);
        declinebtn = (Button)findViewById(R.id.declinebtn);
        current_status = "not_friends";
        pg = new ProgressDialog(this);
        pg.setTitle("Loading");
        pg.setMessage("Pleas wait...");
        pg.show();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        requestReference = FirebaseDatabase.getInstance().getReference().child("FriendRequests");
        friendReference = FirebaseDatabase.getInstance().getReference().child("Friends");
        notificationReference = FirebaseDatabase.getInstance().getReference().child("Notifications");
        mAuth = FirebaseAuth.getInstance();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("username").getValue().toString());
                status.setText(dataSnapshot.child("status").getValue().toString());
                String uri = dataSnapshot.child("image").getValue().toString();

                Picasso.with(ProfileActivity.this).load(uri).placeholder(R.drawable.crib).into(imageView);
                pg.dismiss();

                requestReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(uid)){

                            String stat = dataSnapshot.child(uid).child("request_type").getValue().toString();
                            if(stat.equals("received")){
                                current_status = "received";
                                sendbtn.setText("Accept Friend Request");
                                declinebtn.setVisibility(View.VISIBLE);
                                declinebtn.setEnabled(true);
                            }
                            else if (stat.equals("sent")){
                                current_status = "sent";
                                sendbtn.setText("Cancel Friend Request");
                                declinebtn.setVisibility(View.INVISIBLE);
                                declinebtn.setEnabled(false);
                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                friendReference.child(mAuth.getCurrentUser().getUid()).child(uid).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Toast.makeText(ProfileActivity.this,"works",Toast.LENGTH_LONG).show();
                        //if(dataSnapshot.hasChild(uid)){
                        current_status = "now_friends";
                        sendbtn.setText("Unfriend this Person");
                        declinebtn.setVisibility(View.INVISIBLE);
                        declinebtn.setEnabled(false);
                        //}
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(current_status.equals("not_friends")){
                    requestReference.child(mAuth.getCurrentUser().getUid()).child(uid).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                requestReference.child(uid).child(mAuth.getCurrentUser().getUid()).child("request_type").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        HashMap <String,String>hashMap = new HashMap<String, String>();
                                        hashMap.put("from",mAuth.getCurrentUser().getUid());

                                        hashMap.put("type","request");
                                        notificationReference.child(uid).push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                sendbtn.setEnabled(true);
                                                sendbtn.setText("Cancel Friend Request");

                                                current_status = "friends";
                                            }
                                        });



                                    }
                                });

                            }
                        }
                    });

                }
                else if(current_status.equals("friends")){
                    requestReference.child(mAuth.getCurrentUser().getUid()).child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            requestReference.child(uid).child(mAuth.getCurrentUser().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    current_status = "not_friends";
                                    sendbtn.setText("Send Friend Request");
                                }
                            });
                        }
                    });
                }


                else if (current_status.equals("received")){

                    final Date currentTime = Calendar.getInstance().getTime();
                    friendReference.child(mAuth.getCurrentUser().getUid()).child(uid).child("date").setValue(currentTime.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            friendReference.child(uid).child(mAuth.getCurrentUser().getUid()).child("date").setValue(currentTime.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    requestReference.child(mAuth.getCurrentUser().getUid()).child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            requestReference.child(uid).child(mAuth.getCurrentUser().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    current_status = "now_friends";
                                                    sendbtn.setText("Unfriend this person");

                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                else if(current_status.equals("now_friends")){
                    friendReference.child(mAuth.getCurrentUser().getUid()).child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            friendReference.child(uid).child(mAuth.getCurrentUser().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    current_status="not_friends";
                                    sendbtn.setText("Send Friend Request");
                                }
                            });
                        }
                    });
                }

            }
        });




    }





    @Override
    protected void onResume() {
        super.onResume();


    }
}