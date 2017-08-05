package com.assignment.quizapplication2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.List;

public class PointButtonAdapter extends BaseAdapter {

    private Context mContext;
    private List<Question> mQuestionList;
    private PointsFragment.QuestionListListener mScoreListListener;

    public PointButtonAdapter(Context c, List<Question> list, PointsFragment.QuestionListListener listListener) {
        this.mContext = c;
        this.mQuestionList = list;
        this.mScoreListListener = listListener;
    }

    @Override
    public int getCount() {
        return mQuestionList.size();
    }

    @Override
    public Object getItem(int position) {
        return mQuestionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Question question = mQuestionList.get(position);
        final String score = String.valueOf(question.getmScore());
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.grid_item, null);
        }
        final Button button = (Button) convertView.findViewById(R.id.score_button);
        button.setText(score);

        if (mQuestionList.get(position).getmAnsweredCorrectly())
            button.setBackground(mContext.getDrawable(R.drawable.score_button_green));
        else if (mQuestionList.get(position).getmAnsweredWrong())
            button.setBackground(mContext.getDrawable(R.drawable.score_button_red));
        else if (mQuestionList.get(position).getmRemainingTime() <= 0)
            button.setBackground(mContext.getDrawable(R.drawable.score_button_blue));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScoreListListener.questionClicked(position);
            }
        });

        return convertView;
    }
}
