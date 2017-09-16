package com.example.naveedanwar.chatapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    private Toolbar toolbar;
    public RecyclerView mList;     // 1
    private DatabaseReference databaseReference;
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
            protected void populateViewHolder(UserViewHolder viewHolder, Friends model, int position) {
                //viewHolder.setDate(model.getDate());
            }
        };
        mList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public UserViewHolder(View itemView) {
            super(itemView);
            mView =  itemView;
        }
        public void setDate(String date){

            TextView name = (TextView)mView.findViewById(R.id.urstatus);
            name.setText(date);
        }


    }






}
