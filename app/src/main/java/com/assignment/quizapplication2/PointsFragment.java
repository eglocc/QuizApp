package com.assignment.quizapplication2;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PointsFragment extends Fragment {

    static interface ScoreListListener {
        void itemClicked(int position);
    }

    private Context mContext;
    private DatabaseReference mDatabase;
    private ScoreListListener mListener;
    private List<Category> mCategoryList;
    private Category mCurrentCategory;
    private User mUser;
    private ArrayList<Question> mQuestions;
    private GridView mGridView;

    public void loadQuiz() {
        Query query = mDatabase.child("questions").orderByChild("mCategory").equalTo(mCurrentCategory.toString());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Question q = child.getValue(Question.class);
                    mQuestions.add(q);
                    Log.d("ID", String.valueOf(q.getmId()));
                    //Question question = child.getValue(Question.class);

                    /*Log.d("question", question.getmText());
                    Log.d("question_category", question.getmCategory());
                    Log.d("questions", String.valueOf(questions.size()));
                    questions.add(question);
                    Log.d("questions", String.valueOf(questions.size()));
                    Log.d("user", mUser.getmNickname());*/
                }

                //Collections.sort(mQuestions);
                for (Question q : mQuestions) {
                    Log.d("ID_sorted", String.valueOf(q.getmId()));
                }

                mGridView.setAdapter(new ButtonAdapter(mContext, mQuestions));
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (mListener != null)
                            mListener.itemClicked(position);
                    }
                });
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
        Intent intent = getActivity().getIntent();
        Bundle b = intent.getExtras();
        mUser = b.getParcelable("user");
        mCategoryList = b.getParcelableArrayList("category_list");
        int clickedPosition = b.getInt("clicked_position");
        mCurrentCategory = mCategoryList.get(clickedPosition);
        mQuestions = mCurrentCategory.getmQuiz().getMyQuestionBank();
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
