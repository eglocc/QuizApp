package com.assignment.quizapplication2;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static com.assignment.quizapplication2.SignInActivity.sUser;

public class CategoryActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, PointsFragment.QuestionListListener, QuestionFragment.AnswerListener,
        QuizConstants {

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
    private boolean mCategoryCompletedHasBeenShowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        mFragmentContainer = findViewById(R.id.fragment_container);

        if (mFragmentContainer != null) {
            mHandler = new Handler();
        }

        if (savedInstanceState == null) {
            mBundle = getIntent().getExtras();
            if (mBundle != null) {
                mCategoryCompletedHasBeenShowed = mBundle.getBoolean(CATEGORY_COMPLETED_HAS_BEEN_SHOWED);
            } else {
                mBundle = new Bundle();
            }
        } else {
            mBundle = savedInstanceState;
            mQuestion = mBundle.getParcelable(CLICKED_QUESTION);
            mCategoryId = mBundle.getInt(CLICKED_CATEGORY_POSITION);
            mQuestionId = mBundle.getInt(CLICKED_QUESTION_POSITION);
            mQuestionList = mBundle.getParcelableArrayList(QUESTION_LIST);
            mCategoryClicked = mBundle.getBoolean(CATEGORY_CLICKED);
            mQuestionFragmentIsOn = mBundle.getBoolean(QUESTION_ON);
            mPointsFragmentIsOn = mBundle.getBoolean(POINTS_ON);
            mCategoryCompletedHasBeenShowed = mBundle.getBoolean(CATEGORY_COMPLETED_HAS_BEEN_SHOWED);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                mBundle = data.getExtras();
                PointsActivity.displayBundleOnLog(mBundle);
                mQuestionFragmentIsOn = mBundle.getBoolean(QUESTION_ON);
                mPointsFragmentIsOn = mBundle.getBoolean(POINTS_ON);
                mQuestionId = mBundle.getInt(CLICKED_QUESTION_POSITION);
                updateFrameLayout(false);
            }
            if (resultCode == Activity.RESULT_CANCELED) {

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
            backToMainActivity();
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
    public void questionClicked(int position) {
        mPointsFragmentIsOn = false;
        mQuestionFragmentIsOn = true;
        transactToQuestion(position);
    }

    @Override
    public void answerClicked(View v, String clickedAnswer, boolean answerCorrect) {

        //might be get NullPointerException

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
     * @param forward: true for next question / false for backpress
     */
    public void updateFrameLayout(boolean forward) {
        if (forward) {
            int unAnsweredQuestionIndex = findUnAnsweredQuestion(mQuestionId, mQuestionList);
            if (unAnsweredQuestionIndex != -1) {
                mQuestionId = unAnsweredQuestionIndex;
                transactToQuestion(mQuestionId);
            } else {
                if (!mCategoryCompletedHasBeenShowed) {
                    mCategoryCompletedHasBeenShowed = true;
                    mBundle.putBoolean(CATEGORY_COMPLETED_HAS_BEEN_SHOWED, mCategoryCompletedHasBeenShowed);
                    goToQuizFinishActivity();
                }
            }
        } else {
            if (mQuestionId > 0) {
                mQuestionId--;
                transactToQuestion(mQuestionId);
            } else {
                mQuestionFragmentIsOn = false;
                mPointsFragmentIsOn = true;
                transactToQuestionList(mCategoryId);
            }
        }
    }

    /**
     * Transacts the QuestionFragment according to position parameter
     *
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
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    /**
     * Transacts to PointsFragment according to position
     *
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

    private void backToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    private void goToQuizFinishActivity() {
        Intent intent = new Intent(this, QuizFinishActivity.class);
        intent.putExtras(mBundle);
        startActivityForResult(intent, 1);
    }

    /**
     * An algorithm for finding unanswered question
     *
     * @param id   should be current question id
     * @param list should be question list which is iterated
     * @return unanswered question index
     */
    @Override
    public int findUnAnsweredQuestion(int id, List<Question> list) {
        for (int i = id; i < list.size(); i++) {
            Question q = list.get(i);
            if (!q.getmHasBeenAnswered())
                return q.getmQuestionId();
        }
        for (int i = id; i >= 0; i--) {
            Question q = list.get(i);
            if (!q.getmHasBeenAnswered())
                return q.getmQuestionId();
        }
        return -1;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mFragmentContainer != null) {
            mCategoryClicked = true;
            mPointsFragmentIsOn = true;
            mQuestionFragmentIsOn = false;
            mBundle.putInt(CLICKED_CATEGORY_POSITION, position);
            mFragmentContainer.setVisibility(View.VISIBLE);
            transactToQuestionList(position);
        } else {
            int oldCategoryId = mBundle.getInt(CLICKED_CATEGORY_POSITION);
            boolean categoryChanged = oldCategoryId != position;
            if (categoryChanged)
                QuestionActivity.sQuizFinished = false;
            Intent intent = new Intent(this, PointsActivity.class);
            mBundle.putInt(CLICKED_CATEGORY_POSITION, position);
            intent.putExtras(mBundle);
            startActivity(intent);
        }
    }
}
