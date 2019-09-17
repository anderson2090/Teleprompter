package com.usamabennadeemspeechix.usama.speechix.authentication;

import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.usamabennadeemspeechix.usama.speechix.R;
import com.usamabennadeemspeechix.usama.speechix.addNewScript.database.Script;
import com.usamabennadeemspeechix.usama.speechix.readScript.ReadNavDrActivity;

import java.util.List;

import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.PARCELABLE_SCRIPT_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.SCRIPT_KEY;
import static com.usamabennadeemspeechix.usama.speechix.utils.Utils.loggedInStatus;

public class AuthenticationActivity extends AppCompatActivity implements Validator.ValidationListener {

    //Todo Write code to handle errors for authentication and database operations

    @NotEmpty
    @Email
    EditText emailEditText;

    @NotEmpty
    @Password
    EditText passwordEditText;
    @NotEmpty
    @ConfirmPassword
    EditText confirmPasswordEditText;

    TextInputLayout confirmPasswordTextInputLayout;
    Button loginOrSignUpButton;
    TextView loginOrSignUpTextView;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    ProgressBar authProgressBar;
    Script script;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        Validator validator = new Validator(this);
        validator.setValidationListener(this);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            script = bundle.getParcelable(PARCELABLE_SCRIPT_KEY);
        }

        confirmPasswordTextInputLayout = findViewById(R.id.authConfirmPasswordTextInputLayout);
        authProgressBar = findViewById(R.id.auth_progress_bar);
        emailEditText = findViewById(R.id.auth_activity_email_edit_text);
        passwordEditText = findViewById(R.id.auth_activity_password_edit_text);
        confirmPasswordEditText = findViewById(R.id.auth_activity_confirm_password_edit_text);

        loginOrSignUpButton = findViewById(R.id.auth_activity_login_or_sign_up_button);
        loginOrSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        loginOrSignUpTextView = findViewById(R.id.login_or_sign_up_text_view);
        loginOrSignUpTextView.setPaintFlags(loginOrSignUpTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        loginOrSignUpTextView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                if (loginOrSignUpTextView.getText().toString().equalsIgnoreCase("Or Sign Up")) {
                    loginOrSignUpTextView.setText("Or Login");
                    loginOrSignUpButton.setText("Sign Up");
                    // confirmPasswordEditText.setVisibility(View.VISIBLE);
                    confirmPasswordTextInputLayout.setVisibility(View.VISIBLE);
                } else {
                    loginOrSignUpTextView.setText("Or Sign Up");
                    loginOrSignUpButton.setText("Login");
                    // confirmPasswordEditText.setVisibility(View.GONE);
                    confirmPasswordTextInputLayout.setVisibility(View.GONE);
                }
            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("button", loginOrSignUpButton.getText().toString());
        outState.putString("textView", loginOrSignUpTextView.getText().toString());
        if (confirmPasswordTextInputLayout.getVisibility() == View.VISIBLE) {
            outState.putBoolean("visibility", true);
        } else {
            outState.putBoolean("visibility", false);
        }
        super.onSaveInstanceState(outState);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            loginOrSignUpButton.setText(savedInstanceState.getString("button"));
            loginOrSignUpTextView.setText(savedInstanceState.getString("textView"));
            if (savedInstanceState.getBoolean("visibility")) {
                confirmPasswordTextInputLayout.setVisibility(View.VISIBLE);
            } else {
                confirmPasswordTextInputLayout.setVisibility(View.GONE);
            }

        }

    }

    @Override
    public void onValidationSucceeded() {
        //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
        if (loginOrSignUpButton.getText().toString().equalsIgnoreCase("Sign Up")) {
            authProgressBar.setVisibility(View.VISIBLE);
            createUserAccount(emailEditText.getText().toString(), passwordEditText.getText().toString());

        } else {
            authProgressBar.setVisibility(View.VISIBLE);
            signIn(emailEditText.getText().toString(), passwordEditText.getText().toString());
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void createUserAccount(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    authProgressBar.setVisibility(View.GONE);
                    loggedInStatus("Logged in", getApplicationContext());
                    sendConfirmationEmail();
                    Intent intent = new Intent(getApplicationContext(), ReadNavDrActivity.class);
                    intent.putExtra(PARCELABLE_SCRIPT_KEY, script);
                    startActivity(intent);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                authProgressBar.setVisibility(View.GONE);
            }
        });
    }

    public void signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    loggedInStatus("Logged in", getApplicationContext());
                    Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
                    authProgressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(getApplicationContext(), ReadNavDrActivity.class);
                    intent.putExtra(PARCELABLE_SCRIPT_KEY, script);
                    startActivity(intent);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {

                    Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                    authProgressBar.setVisibility(View.GONE);
                } else if (e instanceof FirebaseAuthInvalidUserException) {
                    Toast.makeText(getApplicationContext(), "Incorrect Email Address", Toast.LENGTH_SHORT).show();
                    authProgressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    authProgressBar.setVisibility(View.GONE);
                }
            }
        });


    }

    public void sendConfirmationEmail() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "A confirmation email has been sent to your email address", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


}
