package com.assignment.quizapplication2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseActivity extends AppCompatActivity {

    private DatabaseReference mRef;
    private Button mSyncButton;
    private CategoryFactory mCategoryFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

        mRef = FirebaseDatabase.getInstance().getReference("categories");
        mCategoryFactory = new CategoryFactory();
        mSyncButton = (Button) findViewById(R.id.sync);

        if (mRef != null) {
            mSyncButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sync();
                }
            });
        }
    }

    private void sync() {
        Category category = mCategoryFactory.createCategory("Music");
        if (category != null) {
            DatabaseReference newRef = mRef.push();
            newRef.setValue(category);
            Log.d("Sync", "Successful");
        } else
            Log.d("Sync", "Failed");
    }
}
