package com.assignment.quizapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.List;

public class QuestionActivity extends AppCompatActivity implements QuestionFragment.AnswerListener {

    private Bundle mBundle;
    private QuestionFragment mQuestionFragment;

    private int mCategoryId;
    private int mQuestionId;
    private Question mQuestion;
    private List<Question> mQuestionList;
    private static User msUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        mBundle = getIntent().getExtras();

        if (savedInstanceState == null) {
            String nickname = mBundle.getString(LoginActivity.NICKNAME);
            msUser.setmNickname(nickname);
        }

        mQuestionFragment = (QuestionFragment) getFragmentManager().findFragmentById(R.id.question_fragment);

        mCategoryId = mBundle.getInt(CategoryActivity.CLICKED_CATEGORY_POSITION);
        mQuestionId = mBundle.getInt(PointsActivity.CLICKED_QUESTION_POSITION);
        mQuestionList = Category.mCategoryList.get(mCategoryId).getmQuestionList();
        mQuestion = mQuestionList.get(mQuestionId);
        mQuestionFragment.setmSelectedQuestion(mQuestion);
        mQuestionFragment.setmUser(msUser);
    }

    @Override
    public void answerClicked(View v, String clickedAnswer, boolean answerCorrect) {
        mQuestion.setmHasBeenAnswered(true);
        mQuestion.setmClickedAnswer(clickedAnswer);

        ListView answers = (ListView) v.getParent();
        int answer_button_count = answers.getChildCount();
        for (int i = 0; i < answer_button_count; i++) {
            answers.getChildAt(i).setOnClickListener(null);
        }

        if (answerCorrect) {
            mQuestion.setmAnsweredCorrectly(true);
            msUser.answeredCorrectly(mQuestion.getmScore());
            v.setBackground(getDrawable(R.drawable.rounded_button_green));
        } else {
            mQuestion.setmAnsweredWrong(true);
            msUser.answeredWrong(mQuestion.getmScore());
            v.setBackground(getDrawable(R.drawable.rounded_button_red));
        }

        if (mQuestionId < mQuestionList.size() - 1) {
            mQuestionId++;
            mBundle.putParcelable("user", msUser);
            mBundle.putInt(PointsActivity.CLICKED_QUESTION_POSITION, mQuestionId);
            Intent intent = new Intent(QuestionActivity.this, QuestionActivity.class);
            intent.putExtras(mBundle);
            startActivity(intent);
        } else {
            Log.d("Else", "finish");
        }
    }
}
