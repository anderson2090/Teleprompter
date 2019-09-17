package com.usamabennadeemspeechix.usama.speechix;

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
import com.google.firebase.auth.FirebaseUser;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.usamabennadeemspeechix.usama.speechix.addNewScript.database.Script;
import com.usamabennadeemspeechix.usama.speechix.readScript.ReadNavDrActivity;

import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.PARCELABLE_SCRIPT_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.SCRIPT_KEY;

public class ChangePasswordActivity extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty
    @Password
    EditText passwordEditText;

    @NotEmpty
    @ConfirmPassword
    EditText confirmPasswordEditText;
    private Validator validator;

    FancyButton changePasswordButton;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    ProgressBar progressBar;
    Script script;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        validator = new Validator(this);
        validator.setValidationListener(this);

        progressBar = findViewById(R.id.change_password_progress_bar);

        passwordEditText = findViewById(R.id.change_password_edit_text);
        confirmPasswordEditText = findViewById(R.id.change_confirm_password_edit_text);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            script = bundle.getParcelable(PARCELABLE_SCRIPT_KEY);
        }

        changePasswordButton = findViewById(R.id.change_password_button);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        progressBar.setVisibility(View.VISIBLE);
        firebaseUser.updatePassword(passwordEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Password Updated", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), ReadNavDrActivity.class);
                    intent.putExtra(PARCELABLE_SCRIPT_KEY,script);
                    startActivity(intent);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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
