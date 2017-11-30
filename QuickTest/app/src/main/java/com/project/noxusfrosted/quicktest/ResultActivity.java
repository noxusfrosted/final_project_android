package com.project.noxusfrosted.quicktest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.noxusfrosted.quicktest.model.UserScore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    private int yourScore;
    private int currentQuestion;
    private TextView finalScore;
    private TextView quizNameTextView;

    private String facebookID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        finalScore = findViewById(R.id.finalScore);

        quizNameTextView = findViewById(R.id.quizNameTextView);
        Bundle bundle = getIntent().getExtras();
        yourScore = bundle.getInt("finalScore", -1);
        currentQuestion = bundle.getInt("lastQuestionNumber", -1);
        final String quizName = bundle.getString("quizName", "n/a");
        final String userName = Profile.getCurrentProfile().getFirstName() + " " +
                Profile.getCurrentProfile().getLastName();

        quizNameTextView.setText("Quiz : " + quizName);
        finalScore.setText(Integer.toString(yourScore) + " / " + Integer.toString(currentQuestion));
        facebookID = Profile.getCurrentProfile().getId();

        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference()
                .child("tableScore").child(quizName);
        Query query = dRef.orderByChild("facebookID").equalTo(facebookID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserScore userScore = new UserScore();
                userScore.setScore(yourScore);
                userScore.setUserName(userName);
                userScore.setFacebookID(facebookID);

                if (dataSnapshot.exists()) {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        UserScore inFireBaseUser = postSnapshot.getValue(UserScore.class);

                        if (inFireBaseUser.getScore() < userScore.getScore()) {

                            String key = postSnapshot.getKey();

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                    .getReference().child("tableScore").child(quizName).child(key);
                            databaseReference.child("score").setValue(userScore.getScore());

                        }

                    }


                } else {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                            .getReference().child("tableScore").child(quizName);
                    databaseReference.push().setValue(userScore);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void onBackClickBtn(View view) {
        finish();
    }

    public void onShareScoreBtn(View view) {

        Bitmap bitmap = createBitmapFromView(getWindow().getDecorView()
                .findViewById(android.R.id.content));
        saveBitmap(bitmap);
        Uri contentUri = getUriFile();
        shareIntent(contentUri);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private Uri getUriFile() {
        File imagePath = new File(getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        return FileProvider.getUriForFile(this,
                "com.project.noxusfrosted.quicktest.fileprovider", newFile);
    }

    private void shareIntent(Uri contentUri) {
        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        }
    }

    private Bitmap createBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight()
                , Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(c);
        return bitmap;
    }

    private void saveBitmap(Bitmap bitmap) {
        try {
            File cachePath = new File(this.getCacheDir(), "images");
            cachePath.mkdirs();
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}