package com.example.naveedanwar.chatapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUsersActivity extends AppCompatActivity {

    private Toolbar toolbar;
    public  RecyclerView mList;     // 1
    private DatabaseReference databaseReference;
    private final static int user =1;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = AllUsersActivity.this;
        setContentView(R.layout.activity_all_users);
        mList = (RecyclerView)findViewById(R.id.mList);   //  2
        mList.setHasFixedSize(true);                    //  3
        mList.setLayoutManager(new LinearLayoutManager(this));//  4
        toolbar = (Toolbar)findViewById(R.id.usertoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");

    }
    //  6
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Users,UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UserViewHolder>(
                Users.class,
                R.layout.single_user_layout,
                UserViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, Users model, int position) {
                viewHolder.setName(model.getUsername());
                viewHolder.setStatus(model.getStatus());
                viewHolder.setThumbnail_image(model.getThumbnail_image());
                final String userid = getRef(position).getKey();
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(AllUsersActivity.this,ProfileActivity.class);
                        i.putExtra("user_id",userid);
                        startActivityForResult(i,user);

                    }
                });
            }
        };
        mList.setAdapter(firebaseRecyclerAdapter);
    }



//  5

    public static class UserViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public UserViewHolder(View itemView) {
            super(itemView);
            mView =  itemView;
        }

        public void setName(String names){

            TextView name = (TextView)mView.findViewById(R.id.urname);
            name.setText(names);
        }
        public void setStatus(String ustatus){
            TextView status = (TextView)mView.findViewById(R.id.urstatus);
            status.setText(ustatus);
        }

        public void setThumbnail_image(String uimage){
            CircleImageView image = (CircleImageView)mView.findViewById(R.id.urimage);
            Picasso.with(context).load(uimage).placeholder(R.drawable.crib).into(image);
        }


    }
}
