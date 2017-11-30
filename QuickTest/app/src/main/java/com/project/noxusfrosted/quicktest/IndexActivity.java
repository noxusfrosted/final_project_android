package com.project.noxusfrosted.quicktest;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class IndexActivity extends AppCompatActivity {


    private String userId;
    private String userName;

    private TextView textView;
    private ProfilePictureView profilePictureView;

    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        userId = Profile.getCurrentProfile().getId();
        userName = Profile.getCurrentProfile().getFirstName();

        profilePictureView = findViewById(R.id.friendProfilePicture);
        textView = findViewById(R.id.userNameTextView);
        final TextView mFirebaseTextView = findViewById(R.id.testTextView);

        textView.setText("Hi ! " + userName);
        profilePictureView.setProfileId(userId);


        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map map = (Map) dataSnapshot.getValue();
                String value = String.valueOf(map.get("connectStatus"));
                mFirebaseTextView.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void onViewScoreBtn(View view){
        Intent intent = new Intent(IndexActivity.this, ChooseQuizScoreActivity.class);
        startActivity(intent);
    }

    public void onLogoutBtn(View view) {

        AlertDialog.Builder aldial = new AlertDialog.Builder(IndexActivity.this);
        aldial.setMessage("Are you sure you want to logout ?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        logOutFacebook();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alertDialog = aldial.create();
        alertDialog.setTitle("Logout");
        alertDialog.show();

    }

    public void onCreateQuizBtn(View view) {

        Intent intent = new Intent(this, StartCreateTestActivity.class);
        startActivity(intent);

    }

    public void onDoQuizBtn(View view) {

        Intent intent = new Intent(IndexActivity.this, ChooseQuizActivity.class);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder aldial = new AlertDialog.Builder(IndexActivity.this);
        aldial.setMessage("Are you sure you want to logout ?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        logOutFacebook();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alertDialog = aldial.create();
        alertDialog.setTitle("Logout");
        alertDialog.show();

    }

    public void logOutFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}