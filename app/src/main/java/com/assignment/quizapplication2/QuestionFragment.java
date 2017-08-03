package com.assignment.quizapplication2;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class QuestionFragment extends Fragment {

    static interface AnswerListener {
        void answerClicked(long id);
    }

    private List<Question> mQuestions;
    private int mQuestionId;
    private Context mContext;
    private Bundle mBundle;
    private User mUser;


    public void displayBundleOnLog() {
        for (String s : mBundle.keySet()) {
            switch (s) {
                case LoginActivity.USER:
                    Log.d(s, ((User) mBundle.get(s)).getmNickname());
                    break;
                case CategoryFragment.CATEGORY_LIST:
                    List<Category> categoryList = (ArrayList<Category>) mBundle.get(s);
                    Log.d(s, categoryList.toString());
                    break;
                case PointsFragment.QUESTION_LIST:
                    List<Question> questionList = (ArrayList<Question>) mBundle.get(s);
                    for (Question q : questionList) {
                        Log.d("Question_" + String.valueOf(q.getmQuestionId()), q.getmText());
                    }
                    break;
                default:
                    Log.d(s, mBundle.get(s).toString());
            }
        }
    }

    public void setmQuestionId(int id) {
        this.mQuestionId = id;
    }

    public void setmQuestions(List<Question> questions) {
        this.mQuestions = questions;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    /*@Override
    public void onStart() {
        super.onStart();
        View view = getView();
        TextView nickname = (TextView) view.findViewById(R.id.nickname);
        TextView score = (TextView) view.findViewById(R.id.score);
        TextView question = (TextView) view.findViewById(R.id.question_text);
        TextView remainingTime = (TextView) view.findViewById(R.id.remaining_time);
        Question q = mQuestions.get(mQuestionId);
        //nickname.setText(mUser.getmNickname());
        //score.setText(String.valueOf(mUser.getmScore()));
        question.setText(q.getmText());
        remainingTime.setText(String.valueOf(q.getmRemainingTime()));
    }*/
}
