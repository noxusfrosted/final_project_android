package com.project.noxusfrosted.quicktest;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.noxusfrosted.quicktest.model.Question;
import com.project.noxusfrosted.quicktest.model.Quiz;

import java.util.ArrayList;
import java.util.Collections;

public class AddQuestionActivity extends AppCompatActivity {

    private Button addButton;
    private Button clearButton;

    private Quiz quiz;

    private int count;
    private int questionNumber;

    private String quizName;
    private String question;
    private String correctAnswer;
    private String incorrect1;
    private String incorrect2;
    private String incorrect3;

    private EditText questionEditText;
    private EditText correctAnswerEditText;
    private EditText incorrectEditText1;
    private EditText incorrectEditText2;
    private EditText incorrectEditText3;
    private TextView textView;

    private Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        clearButton = findViewById(R.id.btnClear);
        addButton = findViewById(R.id.btnAddQuestion);
        textView = findViewById(R.id.createTextView);
        questionEditText = findViewById(R.id.questionEditText);
        correctAnswerEditText = findViewById(R.id.correctAnswerEditText);
        incorrectEditText1 = findViewById(R.id.incorrectEditText1);
        incorrectEditText2 = findViewById(R.id.incorrectEditText2);
        incorrectEditText3 = findViewById(R.id.incorrectEditText3);

        addButton.setClickable(true);
        addButton.setClickable(true);

        quiz = new Quiz();
        count = 1;
        bundle = getIntent().getExtras();
        quizName = bundle.getString("quizName", "Quiz name not found!!!");
        quiz.setQuizName(quizName);
        questionNumber = bundle.getInt("questionNumber", -1);
        quiz.setQuestionNumber(questionNumber);
        textView.setText(quizName + " # " + count);


    }

    public void onAddQuestionBtn(View view) {

        boolean checker = true;

        question = questionEditText.getText().toString().trim();
        correctAnswer = correctAnswerEditText.getText().toString().trim();
        incorrect1 = incorrectEditText1.getText().toString().trim();
        incorrect2 = incorrectEditText2.getText().toString().trim();
        incorrect3 = incorrectEditText3.getText().toString().trim();

        ArrayList<EditText> editTextArrayList = new ArrayList<>();
        editTextArrayList.add(questionEditText);
        editTextArrayList.add(correctAnswerEditText);
        editTextArrayList.add(incorrectEditText1);
        editTextArrayList.add(incorrectEditText2);
        editTextArrayList.add(incorrectEditText3);

        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add(correctAnswer);
        stringArrayList.add(incorrect1);
        stringArrayList.add(incorrect2);
        stringArrayList.add(incorrect3);


        if ((isEditTextEmpty(editTextArrayList))) {
            checker = false;
        }

        if (haveSameValueInput(stringArrayList)) {

            checker = false;
            Toast.makeText(AddQuestionActivity.this, "Duplicate Choice !",
                    Toast.LENGTH_SHORT).show();


        }

        if (checker) {

            addButton.setClickable(false);
            addButton.setClickable(false);


            Question qs = new Question(question, correctAnswer, incorrect1,
                    incorrect2, incorrect3, correctAnswer);
            quiz.addQuestion(qs);

            if (questionNumber > 1) {

                questionNumber -= 1;
                count++;

                Toast.makeText(AddQuestionActivity.this, "Added",
                        Toast.LENGTH_SHORT).show();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        clearTextView();
                        textView.setText(quizName + " # " + count);
                        addButton.setClickable(true);
                        clearButton.setClickable(true);

                    }
                }, 1996);


            } else {

                Toast.makeText(AddQuestionActivity.this, "Finish",
                        Toast.LENGTH_SHORT).show();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        addQuizToFirebase(quiz);

                        Intent intent = new Intent(AddQuestionActivity.this,
                                IndexActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }, 1996);


            }
        }


    }

    private boolean haveSameValueInput(ArrayList<String> stringArrayList) {

        for (String string : stringArrayList) {
            if (string.matches("")) {

            } else if ((Collections.frequency(stringArrayList, string)) > 1) {
                return true;
            }
        }
        return false;
    }


    public void addQuizToFirebase(Quiz quiz) {

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mMessagesRef = mRootRef.child("quiz");
        mMessagesRef.push().setValue(quiz);

    }

    public void onClearBtn(View view) {

        clearTextView();
        Toast.makeText(AddQuestionActivity.this, "Clear!!!",
                Toast.LENGTH_SHORT).show();

    }

    public void clearTextView() {

        questionEditText.setText("");
        correctAnswerEditText.setText("");
        incorrectEditText1.setText("");
        incorrectEditText2.setText("");
        incorrectEditText3.setText("");
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder aldial = new AlertDialog.Builder(
                AddQuestionActivity.this);
        aldial.setMessage("Are you sure you want to exit ?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(AddQuestionActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
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

    public boolean isEditTextEmpty(ArrayList<EditText> editTextArrayList) {

        int count = 0;

        for (EditText editText : editTextArrayList) {

            if (editText.getText().toString().trim().matches("")) {
                editText.setError("This field cannot be Blank");
                count++;
            }
        }

        if (count > 0) {
            return true;
        }
        return false;
    }

}