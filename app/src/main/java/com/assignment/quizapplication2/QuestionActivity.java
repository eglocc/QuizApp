package com.assignment.quizapplication2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class QuestionActivity extends AppCompatActivity implements FragmentListener {

    private Bundle mBundle;
    private QuestionFragment mQuestionFragment;

    private int mCategoryId;
    private int mQuestionId;
    private Question mQuestion;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        mUser = new User();
        mBundle = getIntent().getExtras();
        mQuestionFragment = (QuestionFragment) getFragmentManager().findFragmentById(R.id.question_fragment);

        String nickname = mBundle.getString(LoginActivity.NICKNAME);
        mUser.setmNickname(nickname);

        mCategoryId = mBundle.getInt(CategoryActivity.CLICKED_CATEGORY_POSITION);
        mQuestionId = mBundle.getInt(PointsActivity.CLICKED_QUESTION_POSITION);
        mQuestion = Category.mCategoryList.get(mCategoryId).getmQuestionList().get(mQuestionId);
        mQuestionFragment.setmSelectedQuestion(mQuestion);
        mQuestionFragment.setmUser(mUser);
    }

    @Override
    public void itemClicked(int position) {

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
