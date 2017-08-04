package com.assignment.quizapplication2;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import static com.assignment.quizapplication2.LoginActivity.sUser;

public class CategoryActivity extends AppCompatActivity
        implements CategoryFragment.CategoryListListener, PointsFragment.QuestionListListener, QuestionFragment.AnswerListener {

    public static final String CLICKED_CATEGORY_POSITION = "clicked_category_position";


    private View mFragmentContainer;
    private Bundle mBundle;
    private int mCategoryId;
    private int mQuestionId;
    private List<Question> mQuestionList;
    private Question mQuestion;
    private Handler mHandler;
    private QuestionTimer mTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        mFragmentContainer = findViewById(R.id.fragment_container);
        mBundle = new Bundle();

        if (mFragmentContainer != null) {
            mHandler = new Handler();
        }
    }

    @Override
    public void categoryClicked(int position) {
        if (mFragmentContainer != null) {
            mCategoryId = position;
            mQuestionList = Category.mCategoryList.get(mCategoryId).getmQuestionList();
            PointsFragment pointsFragment = new PointsFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            pointsFragment.setmCategoryId(position);
            ft.replace(R.id.fragment_container, pointsFragment);
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        } else {
            Intent intent = new Intent(this, PointsActivity.class);
            mBundle.putInt(CLICKED_CATEGORY_POSITION, position);
            intent.putExtras(mBundle);
            startActivity(intent);
        }
    }

    @Override
    public void questionClicked(int position) {
        mQuestionId = position;
        mQuestion = Category.mCategoryList.get(mCategoryId).getmQuestionList().get(mQuestionId);
        QuestionFragment questionFragment = new QuestionFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        questionFragment.setmCategoryId(mCategoryId);
        questionFragment.setmQuestionId(position);
        questionFragment.setmSelectedQuestion(mQuestion);
        questionFragment.setmHandler(mHandler);

        mTimer = new QuestionTimer(this, mQuestion, mHandler);

        questionFragment.setmTimer(mTimer);
        ft.replace(R.id.fragment_container, questionFragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    public void answerClicked(View v, String clickedAnswer, boolean answerCorrect) {

        if (mQuestion != null) {
            mQuestion.setmHasBeenAnswered(true);
            mQuestion.setmClickedAnswer(clickedAnswer);

            ListView answers = (ListView) findViewById(R.id.answer_list_view);
            int answer_button_count = answers.getChildCount();
            for (int i = 0; i < answer_button_count; i++) {
                answers.getChildAt(i).setOnClickListener(null);
            }

            if (answerCorrect) {
                mQuestion.setmAnsweredCorrectly(true);
                sUser.answeredCorrectly(mQuestion.getmScore());
                v.setBackground(getDrawable(R.drawable.rounded_button_green));
            } else {
                mQuestion.setmAnsweredWrong(true);
                sUser.answeredWrong(mQuestion.getmScore());
                v.setBackground(getDrawable(R.drawable.rounded_button_red));
            }

            goNext();
        }
    }

    public void goNext() {
        int quizFinished = isAllQuestionsAnswered(mQuestionId);
        if (quizFinished != -1) {

            mQuestionId = quizFinished;
            QuestionFragment questionFragment = new QuestionFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            questionFragment.setmCategoryId(mCategoryId);
            questionFragment.setmQuestionId(mQuestionId);
            mQuestion = Category.mCategoryList.get(mCategoryId).getmQuestionList().get(mQuestionId);
            questionFragment.setmSelectedQuestion(mQuestion);
            questionFragment.setmHandler(mHandler);

            mTimer = new QuestionTimer(this, mQuestion, mHandler);

            questionFragment.setmTimer(mTimer);
            ft.replace(R.id.fragment_container, questionFragment);
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();

        } else {
            Intent intent = new Intent(CategoryActivity.this, LoginActivity.class);
            intent.putExtras(mBundle);
            startActivity(intent);
        }
    }

    private int isAllQuestionsAnswered(int id) {
        for (Question q : mQuestionList) {
            if (!q.getmHasBeenAnswered() && q.getmRemainingTime() > 0) {
                id = q.getmQuestionId();
                return id;
            }
        }
        return -1;
    }
}
