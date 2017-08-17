package com.assignment.quizapplication2;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class HighScoreAdapter extends ArrayAdapter<User> {

    public HighScoreAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<User> users) {
        super(context, resource, textViewResourceId, users);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
        TextView text2 = (TextView) view.findViewById(android.R.id.text2);

        text1.setText(getItem(position).getmNickname().toString());
        text2.setText(Integer.toString(getItem(position).getmScore()));

        return view;
    }
}
