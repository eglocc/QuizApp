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

import static com.assignment.quizapplication2.LoginActivity.sUser;

public class QuestionFragment extends Fragment {

    interface AnswerListener {
        void answerClicked(View v, String clickedAnswer, boolean answerCorrect);
    }

    private Context mContext;
    private AnswerListener mListener;
    private QuestionTimer mTimer;
    private Handler mHandler;

    private TextView mRemainingTimeView;
    private TextView mRemainingTimeLabel;
    private TextView mScoreView;
    private TextView mNicknameView;
    private TextView mQuestionTextView;
    private ListView mAnswerListView;
    private TextView mPointsView;

    private Question mSelectedQuestion;
    private int mCategoryId;
    private int mQuestionId;

    public void setmCategoryId(int id) {
        mCategoryId = id;
    }

    public void setmQuestionId(int id) {
        mQuestionId = id;
    }

    public void setmTimer(QuestionTimer timer) {
        mTimer = timer;
    }

    public void setmHandler(Handler handler) {
        mHandler = handler;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        this.mListener = (AnswerListener) mContext;
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

            mRemainingTimeView = (TextView) view.findViewById(R.id.remaining_time);
            mRemainingTimeLabel = (TextView) view.findViewById(R.id.remaining_time_label);
            mScoreView = (TextView) view.findViewById(R.id.score);
            mPointsView = (TextView) view.findViewById(R.id.points); // might be null, exists only for layout-large

            mNicknameView = (TextView) view.findViewById(R.id.nickname);
            mQuestionTextView = (TextView) view.findViewById(R.id.question_text);
            mAnswerListView = (ListView) view.findViewById(R.id.answer_list_view);

            mSelectedQuestion = Category.mCategoryList.get(mCategoryId).getmQuestionList().get(mQuestionId);

            mTimer.setmRemainingTimeView(mRemainingTimeView);
            mTimer.setmScoreView(mScoreView);
            mTimer.setmAnswerListView(mAnswerListView);
            mTimer.setmSelectedQuestion(mSelectedQuestion);

            //check if this view exists in current layout
            if (mPointsView != null)
                mPointsView.setText(String.valueOf(mSelectedQuestion.getmScore()));

            mNicknameView.setText(sUser.getmNickname());
            mScoreView.setText(String.valueOf(sUser.getmScore()));
            mQuestionTextView.setText(mSelectedQuestion.getmText());

            mAnswerListView.setAdapter(new AnswerButtonAdapter(mContext, mSelectedQuestion, mListener));

            if (mSelectedQuestion.getmHasBeenAnswered()) {
                mRemainingTimeLabel.setVisibility(View.GONE);
                if (mSelectedQuestion.getmAnsweredCorrectly()) {
                    mRemainingTimeView.setText(getString(R.string.correct));
                    mRemainingTimeView.setBackground(mContext.getDrawable(R.drawable.score_button_green));
                } else if (mSelectedQuestion.getmAnsweredWrong()) {
                    mRemainingTimeView.setText(getString(R.string.wrong));
                    mRemainingTimeView.setBackground(mContext.getDrawable(R.drawable.score_button_red));
                } else if (mSelectedQuestion.getmRanOutOfTime()) {
                    mRemainingTimeView.setBackground(mContext.getDrawable(R.drawable.score_button_red));
                    mRemainingTimeView.setText(getString(R.string.times_up));
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mSelectedQuestion.getmHasBeenAnswered()) {
            mTimer.setmRunning(true);
            mHandler.post(mTimer);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mTimer.setmRunning(false);
        mHandler.removeCallbacks(mTimer);
    }
}
