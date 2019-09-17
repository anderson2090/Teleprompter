package com.usamabennadeemspeechix.usama.speechix.addNewScript;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.usamabennadeemspeechix.usama.speechix.MainActivity;
import com.usamabennadeemspeechix.usama.speechix.R;
import com.usamabennadeemspeechix.usama.speechix.addNewScript.database.DBHandler;
import com.usamabennadeemspeechix.usama.speechix.addNewScript.database.Script;

import java.util.List;

public class AddNewScriptActivity extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty
    private EditText scriptNameEditText;

    @NotEmpty
    private EditText scriptBodyEditText;

    Bundle bundle;

    final String PARCELABLE_SCRIPT_KEY = "SCRIPT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_script);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Add New Script");

        bundle = getIntent().getExtras();


        scriptNameEditText = findViewById(R.id.scriptNameEditText);
        scriptBodyEditText = findViewById(R.id.scriptBodyEditText);

        if (bundle != null) {
            Script script = bundle.getParcelable(PARCELABLE_SCRIPT_KEY);
            if (script != null) {
                scriptNameEditText.setText(script.getName());
                scriptBodyEditText.setText(script.getBody());
            }
        }

        Validator validator = new Validator(this);
        validator.setValidationListener(this);

        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener((View view) -> validator.validate());

        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(view -> backToMainActivity());



    }


    @Override
    public void onValidationSucceeded() {
        //Toast.makeText(this, "Validation successful", Toast.LENGTH_SHORT).show();
        DBHandler dbHandler = new DBHandler(this, null, null, 1);
        String scriptName = scriptNameEditText.getText().toString();
        if (dbHandler.checkIsDataAlreadyInDBorNot(scriptName)) {
            Toast.makeText(getApplicationContext(), "You already have a script named \"" + scriptName + "\"", Toast.LENGTH_SHORT).show();
        } else {
            addScript();
            backToMainActivity();
            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
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

    public void addScript() {
        DBHandler dbHandler = new DBHandler(this, null, null, 1);
        Script script = new Script();
        script.setName(scriptNameEditText.getText().toString());
        script.setBody(scriptBodyEditText.getText().toString());
        dbHandler.addScript(script);

    }

    public void backToMainActivity() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
