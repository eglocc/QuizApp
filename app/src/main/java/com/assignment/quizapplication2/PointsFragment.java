package com.assignment.quizapplication2;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class PointsFragment extends Fragment {

    private Context mContext;
    private DatabaseReference mDatabase;
    private FragmentListener mListener;
    private GridView mGridView;

    private int mCategoryId;

    public void setmCategoryId(int id) {
        this.mCategoryId = id;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        this.mListener = (FragmentListener) mContext;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_points_list, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.gridview);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        ArrayList<Question> questions = Category.mCategoryList.get(mCategoryId).getmQuestionList();
        mGridView.setAdapter(new PointButtonAdapter(mContext, questions, mListener));
    }
}
