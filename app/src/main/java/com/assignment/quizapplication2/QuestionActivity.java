package com.assignment.quizapplication2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class QuestionActivity extends AppCompatActivity {

    private Bundle mBundle;
    private int mQuestionId;
    private Category mCurrentCategory;
    private List<Question> mQuestions;
    private QuestionFragment mQuestionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        mBundle = new Bundle();
        mBundle = getIntent().getExtras();

        mQuestionFragment = (QuestionFragment) getFragmentManager().findFragmentById(R.id.question_fragment);
        mQuestionId = mBundle.getInt(PointsFragment.CLICKED_QUESTION_POSITION);
        mCurrentCategory = mBundle.getParcelable(PointsFragment.CURRENT_CATEGORY);
        mQuestions = mBundle.getParcelableArrayList(PointsFragment.QUESTION_LIST);
        mQuestionFragment.setmQuestionId(mQuestionId);
        mQuestionFragment.setmQuestions(mQuestions);
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        QuestionFragment question = new QuestionFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.question_fragment, question);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }*/
}
