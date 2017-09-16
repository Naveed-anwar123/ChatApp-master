package com.example.naveedanwar.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Toolbar tb;
    private ViewPager viewPager;
    private TabLayout tableLayout;
    private SectionViewPager mSectionViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        tb =(Toolbar)findViewById(R.id.tb);
        setSupportActionBar(tb);
        getSupportActionBar().setTitle("Chat App");
        FirebaseUser user = mAuth.getCurrentUser();
        viewPager =(ViewPager)findViewById(R.id.viewPager);
        mSectionViewPager = new SectionViewPager(getSupportFragmentManager());
        viewPager.setAdapter(mSectionViewPager);

        tableLayout =(TabLayout)findViewById(R.id.tabLayout);
        tableLayout.setupWithViewPager(viewPager);

        if(user == null){
            startActivity(new Intent(MainActivity.this,StartActivity.class));
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(MainActivity.this,StartActivity.class));
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu1,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
         if(item.getItemId() == R.id.logout){
             mAuth.signOut();
             startActivity(new Intent(MainActivity.this,StartActivity.class));

         }
         else if(item.getItemId() == R.id.setting){
             startActivity(new Intent(MainActivity.this,AccountSettingsActivity.class));

         }
         else if(item.getItemId() == R.id.users){
             startActivity(new Intent(MainActivity.this,AllUsersActivity.class));

         }

        return true;
    }
}
