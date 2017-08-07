/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    TextView loginTextView;
    TextView signUpTextView;
    Button loginButton;
    Button signUpButton;
    EditText usernameEditText;
    EditText passwordEditText;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        loginTextView = (TextView) findViewById(R.id.loginTextView);
        signUpTextView = (TextView) findViewById(R.id.signUpTextView);

        loginButton = (Button) findViewById(R.id.loginButton);
        signUpButton = (Button) findViewById(R.id.signUpButton);

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        loginButton.setVisibility(View.VISIBLE);
        signUpTextView.setVisibility(View.VISIBLE);

        signUpButton.setVisibility(View.INVISIBLE);
        loginTextView.setVisibility(View.INVISIBLE);

//        loginButton.setOnClickListener(this);         // <-- Cannot work with my code
//        signUpButton.setOnClickListener(this);        // <-- This is how rob did his
        relativeLayout.setOnClickListener(this);

        passwordEditText.setOnKeyListener(this);

        intent = new Intent(getApplicationContext(), UserListActivity.class);

        if(ParseUser.getCurrentUser() != null){
            startActivity(intent);
        }


    }

    @Override
    public void onClick(View v) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void switchToSignUp(View view) {
        signUpButton.setVisibility(View.VISIBLE);
        loginTextView.setVisibility(View.VISIBLE);

        loginButton.setVisibility(View.INVISIBLE);
        signUpTextView.setVisibility(View.INVISIBLE);
    }

    public void switchToLogin(View view) {
        loginButton.setVisibility(View.VISIBLE);
        signUpTextView.setVisibility(View.VISIBLE);

        signUpButton.setVisibility(View.INVISIBLE);
        loginTextView.setVisibility(View.INVISIBLE);
    }

    private boolean isEmpty(EditText editText) {
        Log.i(editText.getText().toString(), Integer.toString(editText.getText().toString().length()));
        return editText.getText().toString().trim().length() == 0;
        // Rob's code
        /*if (editText.getText().toString().matches("")) {
            return true;
        } else {
            return false;
        }*/

    }

    public void signUp(View view) {

         onClick(view);

        if (isEmpty(usernameEditText) || isEmpty(passwordEditText)) {
            Toast.makeText(this, "A username and password are required", Toast.LENGTH_SHORT).show();
        } else {
            ParseUser user = new ParseUser();
            user.setUsername(usernameEditText.getText().toString());
            user.setPassword(passwordEditText.getText().toString());

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.i("Sign Up", "Successful");
                        startActivity(intent);

                    } else {
                        Log.i("Sign up", "Failed: " + e.getMessage());
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void login(View view) {

         onClick(view);

        if (isEmpty(usernameEditText) || isEmpty(passwordEditText)) {
            Toast.makeText(this, "A username and password are required", Toast.LENGTH_SHORT).show();
        } else {
            ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {

                    if (user != null) {
                        Log.i("Login", "Successful");
                        startActivity(intent);
                    } else {
                        Log.i("Login", "Failed: " + e.getMessage());
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
            if (loginButton.getVisibility() == View.VISIBLE) {
                login(v);
            } else {
                signUp(v);
            }
        }

        return false;
    }
}