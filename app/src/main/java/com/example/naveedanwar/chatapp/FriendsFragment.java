package com.example.naveedanwar.chatapp;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {
    private ImageView online;
    private Toolbar toolbar;
    public RecyclerView mList;     // 1
    private DatabaseReference databaseReference,allFriendsReference;
    private final static int user =1;
    private FirebaseAuth mAuth;

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_friends, container, false);
        mList = (RecyclerView)view.findViewById(R.id.mList);   //  2
        mList.setHasFixedSize(true);                    //  3
        mList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAuth= FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Friends").child(mAuth.getCurrentUser().getUid());
        allFriendsReference = FirebaseDatabase.getInstance().getReference().child("Users");
        online = (ImageView)view.findViewById(R.id.online);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Friends,UserViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Friends, UserViewHolder>(
                Friends.class,
                R.layout.single_user_layout,
                UserViewHolder.class,
                databaseReference

        ) {
            @Override
            protected void populateViewHolder(final UserViewHolder viewHolder, Friends model, int position) {
                final String str = getRef(position).getKey().toString();
                //viewHolder.setDate(model.getDate());
                allFriendsReference.child(getRef(position).getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        viewHolder.setName(dataSnapshot.child("username").getValue().toString());
                        viewHolder.setStatus(dataSnapshot.child("status").getValue().toString());
                        viewHolder.setImage(dataSnapshot.child("thumbnail_image").getValue().toString(),getContext());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence charSequence[]=new CharSequence[]{"Open Profile","Send Message"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Select Option");
                        builder.setItems(charSequence, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i==0){
                                    Intent intent = new Intent(getContext(),ProfileActivity.class);
                                    intent.putExtra("user_id",str);
                                    startActivityForResult(intent,user);
                                }
                                else{
                                    Intent intent = new Intent(getContext(),ChatActivity.class);
                                    intent.putExtra("user_id",str);
                                    startActivityForResult(intent,user);
                                }

                            }
                        });
                        builder.show();
                    }
                });
            }


        };
        mList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{
        View mView;
        private TextView name;
        private TextView status;
        private CircleImageView images;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView =  itemView;
        }
        public void setDate(String date){

            //TextView name = (TextView)mView.findViewById(R.id.urstatus);
            //name.setText(date);
        }

        public void setName(String dbname){
            name = (TextView)mView.findViewById(R.id.urname);
            name.setText(dbname);
        }
        public void setStatus(String dbstatus){

            status = (TextView)mView.findViewById(R.id.urstatus);
            status.setText(dbstatus);

        }
        public void setImage(String dbimage, Context context){
            images = (CircleImageView)mView.findViewById(R.id.urimage);
            Picasso.with(context).load(dbimage).placeholder(R.drawable.crib).into(images);
        }

    }






}
