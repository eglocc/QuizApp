package com.assignment.quizapplication2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = SignInActivity.class.getSimpleName();

    private static final String msOnAuthStateChangedSignedIn = "onAuthStateChanged:signed_in:";
    private static final String msOnAuthStateChangedSignedOut = "onAuthStateChanged:signed_out";
    private static final String msCreateAccount = "createAccount:";
    private static final String msSignIn = "signIn";
    private static final String msCreateUserSuccess = "createUser:success";
    private static final String msCreateUserFailed = "createUser:failed";
    private static final String msSignInSuccess = "signIn:success";
    private static final String msSignInFailed = "signIn:failed";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabaseRef;
    private DatabaseReference mUsersRef;

    private EditText mNameField;
    private EditText mSurnameField;
    private EditText mNicknameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mDatabaseRef = FirebaseDatabase.getInstance();
        mUsersRef = mDatabaseRef.getReference("users");

        mNameField = (EditText) findViewById(R.id.set_name);
        mSurnameField = (EditText) findViewById(R.id.set_surname);
        mNicknameField = (EditText) findViewById(R.id.set_nickname);
        findViewById(R.id.create_account_button).setOnClickListener(this);
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, msOnAuthStateChangedSignedIn);
                } else {
                    // User is signed out
                    Log.d(TAG, msOnAuthStateChangedSignedOut);
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();

            //updateUI(currentUser);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        hideProgressDialog();
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, msCreateAccount + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, msCreateUserSuccess);
                    FirebaseUser user = mAuth.getCurrentUser();
                    Map<String, Object> users = new HashMap<>();
                    users.put(user.getUid(), new User(mNameField.getText().toString() + " " + mSurnameField.getText().toString(), mNicknameField.getText().toString()));
                    mUsersRef.updateChildren(users);
                    updateUI(user);
                } else {
                    Log.w(TAG, msCreateUserFailed, task.getException());
                    Toast.makeText(SignInActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                }

                hideProgressDialog();
            }
        });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, msSignIn + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, msSignInSuccess);
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    Log.w(TAG, msSignInFailed, task.getException());
                    Toast.makeText(SignInActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                }

                hideProgressDialog();
            }
        });
    }

    private void signOut() {
        mAuth.signOut();
        //updateUI(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_account_button:
                createAccount(mNameField.getText().toString().toLowerCase() + mSurnameField.getText().toString().toLowerCase() + "@ergizgizer.com", mNicknameField.getText().toString());
                break;
            case R.id.sign_in_button:
                signIn(mNameField.getText().toString().toLowerCase() + mSurnameField.getText().toString().toLowerCase() + "@ergizgizer.com", mNicknameField.getText().toString());
                break;
        }
    }

    private boolean validateForm() {
        boolean valid = true;
        String name = mNameField.getText().toString();
        if (TextUtils.isEmpty(name)) {
            mNameField.setError("Required.");
            valid = false;
        } else {
            mNameField.setError(null);
        }

        String surname = mSurnameField.getText().toString();
        if (TextUtils.isEmpty(surname)) {
            mSurnameField.setError("Required.");
            valid = false;
        } else {
            mSurnameField.setError(null);
        }

        String nickname = mNicknameField.getText().toString();
        if (TextUtils.isEmpty(surname)) {
            mNicknameField.setError("Required.");
            valid = false;
        } else {
            mNicknameField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            Intent i = new Intent(SignInActivity.this, CategoryActivity.class);
            startActivity(i);
        }
    }
}
