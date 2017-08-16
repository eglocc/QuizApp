package com.assignment.quizapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.assignment.quizapplication2.QuizConstants.FROM_CATEGORY_TO_LOGIN;
import static com.assignment.quizapplication2.QuizConstants.NICKNAME;

public class LoginActivity extends AppCompatActivity {

    static User sUser;

    private Button mFireBasePanelButton;

    private Button mStartButton;
    private EditText mEditText;

    private Bundle mBundle;
    private boolean mComingFromCategoryActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditText = (EditText) findViewById(R.id.set_nickname);
        mStartButton = (Button) findViewById(R.id.enter_nickname);
        mFireBasePanelButton = (Button) findViewById(R.id.firebase);

        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mComingFromCategoryActivity = mBundle.getBoolean(FROM_CATEGORY_TO_LOGIN);
            mEditText.setText(sUser.getmNickname());
        }

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = mEditText.getText().toString();
                sUser.setmNickname(nickname);
                Intent i = new Intent(LoginActivity.this, CategoryActivity.class);
                if (mComingFromCategoryActivity)
                    i.putExtras(mBundle);
                startActivity(i);
            }
        });

        if (mFireBasePanelButton != null) {
            if (sUser.getmNickname().equals("eGlocc")) {
                mFireBasePanelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(LoginActivity.this, FirebaseActivity.class);
                        startActivity(intent);

                    }
                });
            }

        }


        if (savedInstanceState != null) {
            String text = savedInstanceState.getString(NICKNAME);
            mEditText.setText(text);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(NICKNAME, mEditText.getText().toString());
    }
}
