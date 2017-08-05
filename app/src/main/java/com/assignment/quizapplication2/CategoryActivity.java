package com.assignment.quizapplication2;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import static com.assignment.quizapplication2.LoginActivity.sUser;

public class CategoryActivity extends AppCompatActivity
        implements CategoryFragment.CategoryListListener, PointsFragment.QuestionListListener, QuestionFragment.AnswerListener {

    public static final String CLICKED_CATEGORY_POSITION = "clicked_category_position";
    public static final String CLICKED_QUESTION_POSITION = "clicked_question_position";
    public static final String CLICKED_QUESTION = "clicked_question";
    public static final String QUESTION_LIST = "question_list";
    public static final String CATEGORY_CLICKED = "category_clicked?";
    public static final String QUESTION_ON = "question_on?";
    public static final String POINTS_ON = "points_on?";

    private View mFragmentContainer;
    private Bundle mBundle;
    private int mCategoryId;
    private int mQuestionId;
    private ArrayList<Question> mQuestionList;
    private Question mQuestion;
    private Handler mHandler;
    private QuestionTimer mTimer;
    private boolean mCategoryClicked;
    private boolean mQuestionFragmentIsOn;
    private boolean mPointsFragmentIsOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        mFragmentContainer = findViewById(R.id.fragment_container);

        if (mFragmentContainer != null) {
            mHandler = new Handler();
        }

        if (savedInstanceState == null) {
            mBundle = new Bundle();
        } else {
            mBundle = savedInstanceState;
            mQuestion = mBundle.getParcelable(CLICKED_QUESTION);
            mCategoryId = mBundle.getInt(CLICKED_CATEGORY_POSITION);
            mQuestionId = mBundle.getInt(CLICKED_QUESTION_POSITION);
            mQuestionList = mBundle.getParcelableArrayList(QUESTION_LIST);
            mCategoryClicked = mBundle.getBoolean(CATEGORY_CLICKED);
            mQuestionFragmentIsOn = mBundle.getBoolean(QUESTION_ON);
            mPointsFragmentIsOn = mBundle.getBoolean(POINTS_ON);

            if (mCategoryClicked) {
                if (mPointsFragmentIsOn)
                    transactToQuestionList(mCategoryId);
                else if (mQuestionFragmentIsOn)
                    transactToQuestion(mQuestionId);
            } else {
                mFragmentContainer.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mQuestionFragmentIsOn)
            updateFrameLayout(false);
        else if (mPointsFragmentIsOn) {
            mFragmentContainer.setVisibility(View.INVISIBLE);
            mPointsFragmentIsOn = false;
        } else {
            backToLoginActivity();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(CLICKED_CATEGORY_POSITION, mCategoryId);
        savedInstanceState.putInt(CLICKED_QUESTION_POSITION, mQuestionId);
        savedInstanceState.putParcelable(CLICKED_QUESTION, mQuestion);
        savedInstanceState.putParcelableArrayList(QUESTION_LIST, mQuestionList);
        savedInstanceState.putBoolean(CATEGORY_CLICKED, mCategoryClicked);
        savedInstanceState.putBoolean(QUESTION_ON, mQuestionFragmentIsOn);
        savedInstanceState.putBoolean(POINTS_ON, mPointsFragmentIsOn);
    }

    @Override
    public void categoryClicked(int position) {
        mCategoryClicked = true;
        mPointsFragmentIsOn = true;
        if (mFragmentContainer != null) {
            mCategoryClicked = true;
            mFragmentContainer.setVisibility(View.VISIBLE);
            transactToQuestionList(position);
        } else {
            Intent intent = new Intent(this, PointsActivity.class);
            mBundle.putInt(CLICKED_CATEGORY_POSITION, position);
            intent.putExtras(mBundle);
            startActivity(intent);
        }
    }

    @Override
    public void questionClicked(int position) {
        mPointsFragmentIsOn = false;
        mQuestionFragmentIsOn = true;
        transactToQuestion(position);
    }

    @Override
    public void answerClicked(View v, String clickedAnswer, boolean answerCorrect) {
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

        updateFrameLayout(true);
    }

    /**
     * Updates FrameLayout according to forward parameter
     *
     * @param forward: true for next question / false for previous question
     */
    public void updateFrameLayout(boolean forward) {
        int quizFinished = isAllQuestionsAnswered(mQuestionId);

        if (quizFinished != -1) {

            if (forward) {
                if (mQuestionId < mQuestionList.size() - 1)
                    mQuestionId++;
                else
                    mQuestionId = quizFinished;

                transactToQuestion(mQuestionId);
            } else {
                if (mQuestionId > 0) {
                    mQuestionId--;
                    transactToQuestion(mQuestionId);
                } else {
                    transactToQuestionList(mCategoryId);
                    mQuestionFragmentIsOn = false;
                    mPointsFragmentIsOn = true;
                }
            }
        } else {
            //should be CongratsActivity
            backToLoginActivity();
        }
    }


    /**
     * An algorithm for finding out whether all questions is answered or not
     * @param id (!)might be removed
     * @return
     */
    private int isAllQuestionsAnswered(int id) {
        for (Question q : mQuestionList) {
            if (!q.getmHasBeenAnswered() && q.getmRemainingTime() > 0) {
                id = q.getmQuestionId();
                return id;
            }
        }
        return -1;
    }

    /**
     * Transacts the QuestionFragment according to position parameter
     * @param position should be question id
     */
    private void transactToQuestion(int position) {
        mQuestionId = position;
        mQuestion = Category.mCategoryList.get(mCategoryId).getmQuestionList().get(mQuestionId);
        QuestionFragment questionFragment = new QuestionFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        questionFragment.setmCategoryId(mCategoryId);
        questionFragment.setmQuestionId(mQuestionId);
        questionFragment.setmHandler(mHandler);

        mTimer = new QuestionTimer(this, mHandler);

        questionFragment.setmTimer(mTimer);

        ft.replace(R.id.fragment_container, questionFragment, "visible_fragment");

        //if (mQuestionChanged)
        ft.addToBackStack(null);

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    /**
     * Transacts to PointsFragment according to position
     * @param position should be category id
     */
    public void transactToQuestionList(int position) {
        mCategoryId = position;
        mQuestionList = Category.mCategoryList.get(mCategoryId).getmQuestionList();
        PointsFragment pointsFragment = new PointsFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        pointsFragment.setmCategoryId(position);
        ft.replace(R.id.fragment_container, pointsFragment, "visible fragment");
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    private void backToLoginActivity() {
        Intent intent = new Intent(CategoryActivity.this, LoginActivity.class);
        intent.putExtras(mBundle);
        startActivity(intent);
    }
}
