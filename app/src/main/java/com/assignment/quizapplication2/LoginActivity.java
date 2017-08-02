package com.assignment.quizapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private Button mStartButton;
    private EditText mEditText;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditText = (EditText) findViewById(R.id.set_nickname);
        mStartButton = (Button) findViewById(R.id.enter_nickname);
        mUser = new User();

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.setmNickname(String.valueOf(mEditText.getText()));
                Intent intent = new Intent(LoginActivity.this, CategoryActivity.class);
                intent.putExtra("user", mUser);
                startActivity(intent);
            }
        });

        if (savedInstanceState != null) {
            String text = savedInstanceState.getString("text");
            mEditText.setText(text);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("text", mEditText.getText().toString());
    }
}
