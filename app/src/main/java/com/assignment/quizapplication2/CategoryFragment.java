package com.assignment.quizapplication2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class CategoryFragment extends ListFragment {

    static interface CategoryListListener {
        void itemClicked(long id);
    }

    private CategoryListListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mListener = (CategoryListListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (mListener != null)
            mListener.itemClicked(id);
    }
}
