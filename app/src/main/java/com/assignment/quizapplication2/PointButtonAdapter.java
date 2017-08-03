package com.assignment.quizapplication2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.List;

public class PointButtonAdapter extends BaseAdapter {

    private Context mContext;
    private List<Question> mQuestionList;
    private FragmentListener mScoreListListener;

    public PointButtonAdapter(Context c, List<Question> list, FragmentListener listListener) {
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
        Log.d("question", question.getmText());
        final Button button = (Button) convertView.findViewById(R.id.score_button);
        button.setText(score);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScoreListListener.itemClicked(position);
            }
        });

        return convertView;
    }
}
