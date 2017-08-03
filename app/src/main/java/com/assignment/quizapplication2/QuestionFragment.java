package com.assignment.quizapplication2;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class QuestionFragment extends Fragment {

    static interface AnswerListener {
        void answerClicked(long id);
    }

    private Context mContext;
    private Question mSelectedQuestion;
    private User mUser;

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
            TextView remainingTime = (TextView) view.findViewById(R.id.remaining_time);
            nickname.setText(mUser.getmNickname());
            score.setText(String.valueOf(mUser.getmScore()));
            question.setText(mSelectedQuestion.getmText());
        }
    }
}
