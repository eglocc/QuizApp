package com.assignment.quizapplication2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class ButtonAdapter extends BaseAdapter {

    public static final String QUESTION_LIST = "question_list";
    public static final String CLICKED_QUESTION_POSITION = "clicked_question_position";

    private Context mContext;
    private List<Question> mQuestionList;
    private PointsFragment.ScoreListListener mScoreListListener;
    private Bundle mBundle;

    public ButtonAdapter(Context c, List<Question> list, PointsFragment.ScoreListListener listListener, Bundle bundle) {
        this.mContext = c;
        this.mQuestionList = list;
        this.mScoreListListener = listListener;
        this.mBundle = bundle;
        mBundle.putParcelableArrayList(QUESTION_LIST, (ArrayList<Question>) mQuestionList);
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
        Log.d("score", score);
        button.setText(score);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBundle.putInt(CLICKED_QUESTION_POSITION, position);
                mScoreListListener.itemClicked(position);
            }
        });

        return convertView;
    }
}
