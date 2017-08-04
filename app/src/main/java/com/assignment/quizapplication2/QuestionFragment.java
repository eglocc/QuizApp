package com.assignment.quizapplication2;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class QuestionFragment extends Fragment {

    static interface AnswerListener {
        void answerClicked(View v, String clickedAnswer, boolean answerCorrect);
    }

    private Context mContext;
    private AnswerListener mListener;
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
        this.mListener = (AnswerListener) mContext;
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
            ListView answerList = (ListView) view.findViewById(R.id.answer_list_view);

            nickname.setText(mUser.getmNickname());
            score.setText(String.valueOf(mUser.getmScore()));
            question.setText(mSelectedQuestion.getmText());

            answerList.setAdapter(new AnswerButtonAdapter(mContext, mSelectedQuestion, mListener));

            if (mSelectedQuestion.getmHasBeenAnswered()) {
                TextView tv = (TextView) getView().findViewById(R.id.remaining_time_label);
                TextView mRemainingTimeView = (TextView) getView().findViewById(R.id.remaining_time);
                tv.setVisibility(View.GONE);
                if (mSelectedQuestion.getmAnsweredCorrectly()) {
                    mRemainingTimeView.setText("Correct");
                    mRemainingTimeView.setBackground(mContext.getDrawable(R.drawable.score_button_green));
                } else if (mSelectedQuestion.getmAnsweredWrong()) {
                    mRemainingTimeView.setText("Wrong");
                    mRemainingTimeView.setBackground(mContext.getDrawable(R.drawable.score_button_red));
                }
            }
        }
    }
}
