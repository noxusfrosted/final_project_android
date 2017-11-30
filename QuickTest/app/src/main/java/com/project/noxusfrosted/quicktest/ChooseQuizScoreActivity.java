package com.project.noxusfrosted.quicktest;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class ChooseQuizScoreActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private ListView mListView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> quizNameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_quiz_score);
        mListView = findViewById(android.R.id.list);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("quiz");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                collectQuizName((Map<String, Object>) dataSnapshot.getValue());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void collectQuizName(Map<String, Object> value) {

        quizNameList.clear();
        for (Map.Entry<String, Object> entry : value.entrySet()) {
            Map singleQuiz = (Map) entry.getValue();
            quizNameList.add((String) singleQuiz.get("quizName"));
        }

        Collections.sort(quizNameList);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.item_quizlistview, R.id.item_quizName, quizNameList);
        mListView.setAdapter(arrayAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String currentQuiz = quizNameList.get(i).toString();
                Intent intent = new Intent(ChooseQuizScoreActivity.this, ScoreBoardActivity.class);
                intent.putExtra("currentQuiz", currentQuiz);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
