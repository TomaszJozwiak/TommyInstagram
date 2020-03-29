package com.example.tommyinstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    Boolean signupModeActive = true;
    TextView changeSignupModeTextView;
    EditText passwordEditText;

    public void showUserList() {

        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            signUp(view);
        }

        return false;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.changeSignupModeTextView) {

            Button signupButton = (Button) findViewById(R.id.signupButton);
            if (signupModeActive) {

                signupModeActive = false;
                changeSignupModeTextView.setText("Click here to create an account");
                signupButton.setText("Login");

            } else {

                signupModeActive = true;
                changeSignupModeTextView.setText("Already have an account? Click here to login");
                signupButton.setText("Signup");

            }
        } else if (view.getId() == R.id.backgroundLayout || view.getId() == R.id.logoImageView) {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }
    }

    public void signUp(View view) {

        EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (username.matches("") || password.matches("")) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        } else {

            if (signupModeActive) {

                ParseUser user = new ParseUser();

                user.setUsername(username);
                user.setPassword(password);

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(MainActivity.this, "SignUp successful", Toast.LENGTH_SHORT).show();
                            showUserList();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {

                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            showUserList();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Tommy Instagram");

        changeSignupModeTextView = (TextView) findViewById(R.id.changeSignupModeTextView);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        ConstraintLayout backgroundLayout = (ConstraintLayout) findViewById(R.id.backgroundLayout);
        ImageView logoImageView = (ImageView) findViewById(R.id.logoImageView);

        changeSignupModeTextView.setOnClickListener(this);
        backgroundLayout.setOnClickListener(this);
        logoImageView.setOnClickListener(this);
        passwordEditText.setOnKeyListener(this);

        if (ParseUser.getCurrentUser() != null) {
            showUserList();
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

}
