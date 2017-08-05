package com.assignment.quizapplication2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    public static final String QUESTION_CLICKED = "question_clicked?";
    public static final String QUESTION_CHANGED = "question_changed?";


    private View mFragmentContainer;
    private Bundle mBundle;
    private int mCategoryId;
    private int mQuestionId;
    private ArrayList<Question> mQuestionList;
    private Question mQuestion;
    private Handler mHandler;
    private QuestionTimer mTimer;
    private boolean mCategoryClicked;
    private boolean mQuestionClicked;
    private boolean mQuestionChanged;

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
            mQuestionClicked = mBundle.getBoolean(QUESTION_CLICKED);
            mQuestionChanged = mBundle.getBoolean(QUESTION_CHANGED);
            if (mCategoryClicked && !mQuestionClicked)
                transactToQuestionList(mCategoryId);
            else if (mCategoryClicked && mQuestionClicked)
                transactToQuestion(mQuestionId);
        }
    }

    private Fragment getFragment(int index) {
        FragmentManager.BackStackEntry backStackEntry = getFragmentManager().getBackStackEntryAt(index);
        String tag = backStackEntry.getName();
        Log.d("Tag", tag);
        Fragment fragment = getFragmentManager().findFragmentByTag(tag);
        return fragment;
    }

    @Override
    public void onBackPressed() {
        int index = getFragmentManager().getBackStackEntryCount() - 1;
        int previousIndex = getFragmentManager().getBackStackEntryCount() - 2;
        Log.d("Index", String.valueOf(index));
        Log.d("Previous index", String.valueOf(previousIndex));
        if (index >= 0) {
            Fragment fragment = getFragment(index);
            if (fragment instanceof PointsFragment) {
                Log.d("Fragment class", fragment.getClass().toString());
                mCategoryClicked = false;
                FragmentTransaction ft = getFragmentManager().beginTransaction().remove(fragment);
                ft.commit();
                //mFragmentContainer.setVisibility(View.INVISIBLE);
                return;
            } else if (fragment instanceof QuestionFragment) {
                if (previousIndex >= 0) {
                    Fragment previousFragment = getFragment(previousIndex);
                    if (previousFragment instanceof PointsFragment) {
                        Log.d("Fragment class", previousFragment.getClass().toString());
                        transactToQuestionList(mCategoryId);
                        return;
                    } else if (previousFragment instanceof QuestionFragment) {
                        mQuestionId--;
                        Log.d("Fragment class", fragment.getClass().toString());
                        if (mQuestionId >= 0) {
                            transactToQuestion(mQuestionId);
                            Log.d("Fragment class", fragment.getClass().toString());
                            return;
                        } else {
                            transactToQuestionList(mCategoryId);
                            Log.d("Fragment class", fragment.getClass().toString());
                            return;
                        }
                    }
                }
            } else {
                super.onBackPressed();
            }
        } else
            super.onBackPressed();

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(CLICKED_CATEGORY_POSITION, mCategoryId);
        savedInstanceState.putInt(CLICKED_QUESTION_POSITION, mQuestionId);
        savedInstanceState.putParcelable(CLICKED_QUESTION, mQuestion);
        savedInstanceState.putParcelableArrayList(QUESTION_LIST, mQuestionList);
        savedInstanceState.putBoolean(CATEGORY_CLICKED, mCategoryClicked);
        savedInstanceState.putBoolean(QUESTION_CLICKED, mQuestionClicked);
        savedInstanceState.putBoolean(QUESTION_CHANGED, mQuestionChanged);
    }
    @Override
    public void categoryClicked(int position) {
        if (mFragmentContainer != null) {
            mFragmentContainer.setVisibility(View.VISIBLE);
            mCategoryClicked = true;
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
        mQuestionClicked = true;
        mQuestionChanged = true;
        transactToQuestion(position);
    }

    @Override
    public void answerClicked(View v, String clickedAnswer, boolean answerCorrect) {
        //Check without null
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

    public void goNext() {
        int quizFinished = isAllQuestionsAnswered(mQuestionId);
        if (quizFinished != -1) {

            mQuestionId = quizFinished;
            transactToQuestion(mQuestionId);
            mQuestionChanged = true;

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

    private void transactToQuestion(int position) {
        int previousID = mQuestionId;
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

        ft.replace(R.id.fragment_container, questionFragment, "question_replaced");

        if (mQuestionChanged)
            ft.addToBackStack("question_replaced");

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void transactToQuestionList(int position) {
        mCategoryId = position;
        mQuestionList = Category.mCategoryList.get(mCategoryId).getmQuestionList();
        PointsFragment pointsFragment = new PointsFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        pointsFragment.setmCategoryId(position);
        ft.replace(R.id.fragment_container, pointsFragment, "list_replaced");
        ft.addToBackStack("list_replaced");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}
