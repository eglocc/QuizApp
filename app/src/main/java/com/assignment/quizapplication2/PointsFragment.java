package com.assignment.quizapplication2;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PointsFragment extends Fragment {

    private DatabaseReference mDatabase;
    private List<Category> mCategoryList;
    private Category mCurrentCategory;
    private User mUser;

    public Quiz loadQuiz() {
        Query query = mDatabase.child("questions").orderByChild("mCategory").equalTo(mCurrentCategory.toString());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Question> questions = mCurrentCategory.getmQuiz().getMyQuestionBank();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Question question = child.getValue(Question.class);
                    Log.d("question", question.getmText());
                    Log.d("question_category", question.getmCategory());
                    Log.d("questions", String.valueOf(questions.size()));
                    questions.add(question);
                    Log.d("questions", String.valueOf(questions.size()));
                    Log.d("user", mUser.getmNickname());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent = getActivity().getIntent();
        Bundle b = intent.getExtras();
        mUser = b.getParcelable("user");
        mCategoryList = b.getParcelableArrayList("category_list");
        int clickedPosition = b.getInt("clicked_position");
        mCurrentCategory = mCategoryList.get(clickedPosition);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_points, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadQuiz();
    }
}
