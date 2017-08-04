package com.assignment.quizapplication2;

import android.content.Context;
import android.util.Log;
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
    private Question mSelectedQuestion;
    private HashMap<String, Boolean> mAnswerMap;
    private List<String> mAnswerSet;
    private QuestionFragment.AnswerListener mAnswerListListener;

    public AnswerButtonAdapter(Context c, Question selected, QuestionFragment.AnswerListener answerListener) {
        this.mContext = c;
        this.mSelectedQuestion = selected;
        this.mAnswerMap = selected.getmAnswerMap();
        this.mAnswerListListener = answerListener;

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
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final String answer = mAnswerSet.get(position);
        Log.d("answer", answer);
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.answer_list_item, null);
        }
        final Button button = (Button) convertView.findViewById(R.id.answer_button);

        if (mAnswerMap.get(answer)) {
            button.setTag("true_answer");
        }

        if (answer.length() > 3 && answer.charAt(3) == '_')
            button.setText(answer.substring(4));
        else
            button.setText(answer);

        if (mSelectedQuestion.getmHasBeenAnswered() || mSelectedQuestion.getmRemainingTime() < 1) {
            button.setEnabled(false);
            if (mAnswerMap.get(answer)) {
                Log.d("first if", "here");
                button.setBackground(mContext.getDrawable(R.drawable.rounded_button_green));
            }
            if (mSelectedQuestion.getmAnsweredWrong()) {
                Log.d("second if", "here");
                if (answer.equals(mSelectedQuestion.getmClickedAnswer())) {
                    Log.d("third if", "here");
                    button.setBackground(mContext.getDrawable(R.drawable.rounded_button_red));
                }
            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerCorrect = mAnswerMap.get(answer);
                mAnswerListListener.answerClicked(v, answer, answerCorrect);
            }
        });

        return convertView;
    }
}
