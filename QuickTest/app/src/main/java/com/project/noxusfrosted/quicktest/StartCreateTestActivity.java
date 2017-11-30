package com.project.noxusfrosted.quicktest;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class StartCreateTestActivity extends AppCompatActivity {


    private int questionNumber = 5;
    private ArrayList<String> allQuizArrayList = new ArrayList<>();
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_create_test);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("quiz");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                collectQuizName((Map<String, Object>) dataSnapshot.getValue());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }




    public void onStartBtn(View view) {

        EditText quizNameEditText = findViewById(R.id.quizNameEditText);
        String quizName = quizNameEditText.getText().toString().trim();


        if (quizName.matches("")) {
            quizNameEditText.setError("This field cannot be Blank");
        } else if (allQuizArrayList.contains(quizName)) {
            quizNameEditText.setError
                    ("This Quiz Name is already created, Please change your Quiz Name");
        } else {
            Intent intent = new Intent(this, AddQuestionActivity.class);
            intent.putExtra("quizName", quizName);
            intent.putExtra("questionNumber", questionNumber);
            startActivity(intent);
            finish();
        }
    }


    private void collectQuizName(Map<String, Object> value) {

        for (Map.Entry<String, Object> entry : value.entrySet()) {

            Map singleQuiz = (Map) entry.getValue();
            allQuizArrayList.add((String) singleQuiz.get("quizName"));
        }
    }

    public void onQuestion5Check(View view) {
        this.questionNumber = 5;
    }

    public void onQuestion10Check(View view) {
        this.questionNumber = 10;
    }


    @Override
    public void onBackPressed() {
        backToIndex();
    }

    public void onBackBtn(View view) {

        backToIndex();

    }

    public void backToIndex(){

        AlertDialog.Builder aldial = new AlertDialog.Builder(StartCreateTestActivity.this);
        aldial.setMessage("Are you sure you want to exit ?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alertDialog = aldial.create();
        alertDialog.setTitle("Exit");
        alertDialog.show();
    }
}