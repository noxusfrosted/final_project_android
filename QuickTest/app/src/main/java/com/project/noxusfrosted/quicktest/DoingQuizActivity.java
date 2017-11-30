package com.project.noxusfrosted.quicktest;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class DoingQuizActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    private int score;
    private int currentQuestion;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
    private String correctChoice;
    private String questionName;
    private String quizName;
    private TextView currentScore;
    private TextView correctAnswerTextView;
    private TextView quizNameTextView;
    private TextView currentQuestionTextView;
    private Button choiceOneBtn;
    private Button choiceTwoBtn;
    private Button choiceThreeBtn;
    private Button choiceFourBtn;
    private Bundle bundle;
    private ArrayList<Button> buttonArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doing_quiz);

        score = 0;
        currentQuestion = 1;
        currentScore = findViewById(R.id.currentScore);
        correctAnswerTextView = findViewById(R.id.correctAnswerTextView);
        quizNameTextView = findViewById(R.id.quizNameTextView);
        currentQuestionTextView = findViewById(R.id.currentQuestion);
        choiceOneBtn = findViewById(R.id.answerOneBtn);
        choiceTwoBtn = findViewById(R.id.answerTwoBtn);
        choiceThreeBtn = findViewById(R.id.answerThreeBtn);
        choiceFourBtn = findViewById(R.id.answerFourBtn);
        bundle = getIntent().getExtras();
        quizName = bundle.getString("currentQuiz").trim();
        currentScore.setText("Your Score : 0");
        quizNameTextView.setText("Quiz : " + quizName);
        buttonArrayList = new ArrayList<>();
        buttonArrayList.add(choiceOneBtn);
        buttonArrayList.add(choiceTwoBtn);
        buttonArrayList.add(choiceThreeBtn);
        buttonArrayList.add(choiceFourBtn);

        onQuestionChanged(quizName, currentQuestion - 1);

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder aldial = new AlertDialog.Builder(DoingQuizActivity.this);
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

    public void onChooseOne(View view) {

        enableButton(buttonArrayList, false);
        findCorrectBtn(buttonArrayList, correctChoice);
        checkAnswer(choiceOneBtn);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onQuestionChanged(quizName, currentQuestion - 1);
            }
        }, 2000);
    }

    public void onChooseTwo(View view) {

        enableButton(buttonArrayList, false);
        findCorrectBtn(buttonArrayList, correctChoice);
        checkAnswer(choiceTwoBtn);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onQuestionChanged(quizName, currentQuestion - 1);
            }
        }, 2000);


    }

    public void onChooseThree(View view) {

        enableButton(buttonArrayList, false);
        findCorrectBtn(buttonArrayList, correctChoice);
        checkAnswer(choiceThreeBtn);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                onQuestionChanged(quizName, currentQuestion - 1);

            }
        }, 2000);


    }

    public void onChooseFour(View view) {

        enableButton(buttonArrayList, false);
        findCorrectBtn(buttonArrayList, correctChoice);
        checkAnswer(choiceFourBtn);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                onQuestionChanged(quizName, currentQuestion - 1);

            }
        }, 2000);

    }

    public void onQuestionChanged(final String quizName, final int currentQuestion) {


        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("quiz")
                .orderByChild("quizName")
                .equalTo(quizName)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                            try {

                                choice1 = childSnapshot.child("questionList")
                                        .child(String.valueOf(currentQuestion))
                                        .child("choice1").getValue().toString();
                                choice2 = childSnapshot.child("questionList")
                                        .child(String.valueOf(currentQuestion))
                                        .child("choice2").getValue().toString();
                                choice3 = childSnapshot.child("questionList")
                                        .child(String.valueOf(currentQuestion))
                                        .child("choice3").getValue().toString();
                                choice4 = childSnapshot.child("questionList")
                                        .child(String.valueOf(currentQuestion))
                                        .child("choice4").getValue().toString();
                                correctChoice = childSnapshot.child("questionList")
                                        .child(String.valueOf(currentQuestion))
                                        .child("correctAnswer").getValue().toString();
                                questionName = childSnapshot.child("questionList")
                                        .child(String.valueOf(currentQuestion))
                                        .child("nameQuestion").getValue().toString();


                            } catch (Exception e) {

                                finishedTest();

                            }


                        }

                        resetColorButton(buttonArrayList);
                        enableButton(buttonArrayList, true);

                        ArrayList<Button> forRandom = new ArrayList<>();
                        forRandom.add(choiceOneBtn);
                        forRandom.add(choiceTwoBtn);
                        forRandom.add(choiceThreeBtn);
                        forRandom.add(choiceFourBtn);

                        Collections.shuffle(forRandom);

                        forRandom.get(0).setText(choice1);
                        forRandom.get(1).setText(choice2);
                        forRandom.get(2).setText(choice3);
                        forRandom.get(3).setText(choice4);


                        currentQuestionTextView.setText(String.valueOf(currentQuestion + 1) + ". " + questionName);
                        correctAnswerTextView.setText(correctChoice);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });


    }

    public void finishedTest() {

        Intent intent = new Intent(DoingQuizActivity.this, ResultActivity.class);
        intent.putExtra("quizName", quizName);
        intent.putExtra("finalScore", score);
        intent.putExtra("lastQuestionNumber", currentQuestion - 1);

        startActivity(intent);
        finish();

    }

    public void checkAnswer(Button btnInput) {

        currentQuestion++;


        if (btnInput.getText().equals(correctAnswerTextView.getText())) {
            score++;
            MediaPlayer mp = MediaPlayer.create(DoingQuizActivity.this, R.raw.correct);
            mp.start();
        } else {
            btnInput.setBackgroundResource(R.drawable.rounded_choice_incorrect);
            MediaPlayer mp = MediaPlayer.create(DoingQuizActivity.this, R.raw.incorrect);
            mp.start();
        }

        currentScore.setText("Your Score : " + Integer.toString(score));

    }

    public void findCorrectBtn(ArrayList<Button> buttonArrayList, String correct) {
        for (Button button : buttonArrayList) {
            if (button.getText().toString().equals(correct)) {
                button.setBackgroundResource(R.drawable.rounded_choice_correct);
            }
        }
    }

    public void resetColorButton(ArrayList<Button> buttonArrayList) {
        for (Button button : buttonArrayList) {
            button.setBackgroundResource(R.drawable.rounded_choice_normal);
        }
    }

    public void enableButton(ArrayList<Button> buttonArrayList, Boolean b) {
        for (Button button : buttonArrayList) {
            button.setClickable(b);
        }
    }
}