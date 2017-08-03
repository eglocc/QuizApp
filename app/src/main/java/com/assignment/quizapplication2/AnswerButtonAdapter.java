package com.assignment.quizapplication2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnswerButtonAdapter extends BaseAdapter {

    private Context mContext;
    private HashMap<String, Boolean> mAnswerMap;
    private List<String> mAnswerSet;
    private FragmentListener mAnswerListListener;

    public AnswerButtonAdapter(Context c, HashMap<String, Boolean> map, FragmentListener listListener) {
        this.mContext = c;
        this.mAnswerMap = map;
        this.mAnswerListListener = listListener;

        mAnswerSet = new ArrayList<>();
        for (String s : mAnswerMap.keySet()) {
            mAnswerSet.add(s);
        }
    }

    @Override
    public int getCount() {
        return mAnswerMap.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final String answer = mAnswerSet.get(position);
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.answer_list_item, null);
        }
        final Button button = (Button) convertView.findViewById(R.id.answer_button);
        button.setText(answer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnswerListListener.itemClicked(position);
            }
        });

        return convertView;
    }
}
