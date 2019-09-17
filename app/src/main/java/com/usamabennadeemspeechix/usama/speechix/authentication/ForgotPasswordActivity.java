package com.usamabennadeemspeechix.usama.speechix.authentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.usamabennadeemspeechix.usama.speechix.R;
import com.usamabennadeemspeechix.usama.speechix.addNewScript.database.Script;
import com.usamabennadeemspeechix.usama.speechix.readScript.ReadNavDrActivity;

import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.PARCELABLE_SCRIPT_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.SCRIPT_KEY;

public class ForgotPasswordActivity extends AppCompatActivity implements Validator.ValidationListener {


    @NotEmpty
    @Email
    EditText emailEditText;
    private Validator validator;
    FirebaseAuth firebaseAuth;

    ProgressBar progressBar;
    Script script;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        firebaseAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.forgot_password_email_edit_text);
        progressBar = findViewById(R.id.forgot_password_progress_bar);

        validator = new Validator(this);
        validator.setValidationListener(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            script = bundle.getParcelable(PARCELABLE_SCRIPT_KEY);
        }


        FancyButton resetButton = findViewById(R.id.forgot_password_reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validator.validate();
            }
        });


    }

    @Override
    public void onValidationSucceeded() {
        progressBar.setVisibility(View.VISIBLE);
        String email = emailEditText.getText().toString();
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Instructions to reset password has been sent to your e-mail address", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(getApplicationContext(), ReadNavDrActivity.class);
                    intent.putExtra(PARCELABLE_SCRIPT_KEY, script);
                    startActivity(intent);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
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
}
