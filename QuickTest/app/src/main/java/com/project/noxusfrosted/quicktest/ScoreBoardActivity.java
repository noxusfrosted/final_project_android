package com.project.noxusfrosted.quicktest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.noxusfrosted.quicktest.model.UserScore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreBoardActivity extends AppCompatActivity {

    private ListView listView;
    private TextView textView;
    private Bundle bundle;
    private String quizName;
    private DatabaseReference databaseReference;

    private ArrayList<String> scoreList = new ArrayList<>();
    private ArrayList<String> userList = new ArrayList<>();
    private ArrayList<String> facebookIDList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);

        listView = findViewById(R.id.customListView);
        listView.setAdapter(new CustomAdapter(getApplicationContext()));
        textView = findViewById(R.id.quizNameTextView);
        bundle = getIntent().getExtras();
        quizName = bundle.getString("currentQuiz", "n/a");
        textView.setText(quizName);
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("tableScore").child(quizName);
        Query query = databaseReference.orderByChild("score");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userList.clear();
                scoreList.clear();
                facebookIDList.clear();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    UserScore userScore = postSnapshot.getValue(UserScore.class);
                    userList.add(userScore.getUserName());
                    scoreList.add(String.valueOf(userScore.getScore()));


                }
                Collections.reverse(scoreList);
                Collections.reverse(userList);

                listView.setAdapter(new CustomAdapter(getApplicationContext()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

}

    public class CustomAdapter extends BaseAdapter{

        public Context mContext;
        public LayoutInflater mInflater;

        public CustomAdapter(Context context){
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return scoreList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if(convertView == null){
                // load layout
                convertView = mInflater.inflate(R.layout.item_score_custom_listview,
                        null);
                holder = new ViewHolder();
                holder.userNameTextView = convertView.findViewById(R.id.item_userName);
                holder.scoreTextView = convertView.findViewById(R.id.item_userScore);
                convertView.setTag(holder);
            }else{
                // rebind widget
                holder = (ViewHolder) convertView.getTag();
            }

            holder.userNameTextView.setText(userList.get(i));
            holder.scoreTextView.setText(scoreList.get(i));
            return convertView;
        }

        public class ViewHolder{
            TextView userNameTextView;
            TextView scoreTextView;

        }
    }
}
