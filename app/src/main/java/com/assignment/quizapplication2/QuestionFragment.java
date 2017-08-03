package com.assignment.quizapplication2;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;

public class QuestionFragment extends Fragment {

    private Context mContext;
    private TextView mRemainingTime;

    private static Handler msHandler = new Handler();
    private FragmentListener mListener;
    private Question mSelectedQuestion;
    private User mUser;
    private Timer mTimer;

    public void setmSelectedQuestion(Question question) {
        mSelectedQuestion = question;
    }

    public void setmUser(User user) {
        mUser = user;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        this.mListener = (FragmentListener) mContext;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {

            TextView nickname = (TextView) view.findViewById(R.id.nickname);
            TextView score = (TextView) view.findViewById(R.id.score);
            TextView question = (TextView) view.findViewById(R.id.question_text);
            mRemainingTime = (TextView) view.findViewById(R.id.remaining_time);
            ListView answerList = (ListView) view.findViewById(R.id.answer_list_view);

            nickname.setText(mUser.getmNickname());
            score.setText(String.valueOf(mUser.getmScore()));
            question.setText(mSelectedQuestion.getmText());

            HashMap<String, Boolean> answers = mSelectedQuestion.getmAnswerMap();
            answerList.setAdapter(new AnswerButtonAdapter(mContext, answers, mListener));
            mTimer = new Timer(mContext, mUser, mSelectedQuestion, mRemainingTime, msHandler);
            mTimer.setmRunning(true);
            msHandler.post(mTimer);
        }
    }
}
