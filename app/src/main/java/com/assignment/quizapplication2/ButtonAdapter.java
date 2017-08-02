package com.assignment.quizapplication2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.List;

public class ButtonAdapter extends BaseAdapter {

    private Context mContext;
    private List<Question> mQuestionList;

    public ButtonAdapter(Context c, List<Question> list) {
        this.mContext = c;
        this.mQuestionList = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final Question question = mQuestionList.get(position);
        final String score = String.valueOf(question.getmScore());
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.grid_item, null);
        }

        final Button button = (Button) convertView.findViewById(R.id.score_button);
        Log.d("score", score);
        button.setText(score);

        return convertView;
    }
}
