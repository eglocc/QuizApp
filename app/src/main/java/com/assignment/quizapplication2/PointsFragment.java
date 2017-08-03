package com.assignment.quizapplication2;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class PointsFragment extends Fragment {

    static interface ScoreListListener {
        void itemClicked(int position);
    }

    public static final String CURRENT_CATEGORY = "current_category";
    public static final String QUESTION_LIST = "question_list";
    public static final String CLICKED_QUESTION_POSITION = "clicked_question_position";

    private Context mContext;
    private DatabaseReference mDatabase;
    private ScoreListListener mListener;
    private GridView mGridView;

    private Category mCurrentCategory;
    private ArrayList<Question> mQuestions;

    public void setmCurrentCategory(Category c) {
        mCurrentCategory = c;
    }

    public void loadQuiz() {
        mQuestions = mCurrentCategory.getmQuiz().getMyQuestionBank();
        Query query = mDatabase.child("questions").orderByChild("mCategory").equalTo(mCurrentCategory.toString());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Question q = child.getValue(Question.class);
                    mQuestions.add(q);
                }

                Collections.sort(mQuestions);
                mGridView.setAdapter(new ButtonAdapter(mContext, mQuestions, mListener));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        this.mListener = (ScoreListListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_points_list, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.gridview);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadQuiz();

    }
}
