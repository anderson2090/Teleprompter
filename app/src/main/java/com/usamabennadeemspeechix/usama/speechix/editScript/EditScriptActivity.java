package com.usamabennadeemspeechix.usama.speechix.editScript;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.usamabennadeemspeechix.usama.speechix.R;
import com.usamabennadeemspeechix.usama.speechix.readScript.ReadNavDrActivity;
import com.usamabennadeemspeechix.usama.speechix.addNewScript.database.DBHandler;
import com.usamabennadeemspeechix.usama.speechix.addNewScript.database.Script;

import java.util.List;

public class EditScriptActivity extends AppCompatActivity implements Validator.ValidationListener {

    final String PARCELABLE_SCRIPT_KEY = "SCRIPT";
    final String UPDATED_PARCELABLE_SCRIPT_KEY = "UPDATED SCRIPT";
    @NotEmpty
    EditText scriptNameEditText;
    @NotEmpty
    EditText scriptBodyEditText;

    Button saveButton;
    Button cancelButton;
    Script script;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_script);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        scriptNameEditText = findViewById(R.id.editScriptScriptNameEditText);
        scriptBodyEditText = findViewById(R.id.editScriptScriptBodyEditText);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            script = bundle.getParcelable(PARCELABLE_SCRIPT_KEY);
            if (script != null) {
                setTitle("Edit "+"'"+script.getName()+"'");
            }
        }
        populateEditTexts();

        Validator validator = new Validator(this);
        validator.setValidationListener(this);

        saveButton = findViewById(R.id.editScriptAddButton);
        saveButton.setOnClickListener(view -> validator.validate());

        cancelButton = findViewById(R.id.editScriptCancelButton);
        cancelButton.setOnClickListener(view -> finish());



    }

    public void populateEditTexts() {


        if (script != null) {
            scriptNameEditText.setText(script.getName());
            scriptBodyEditText.setText(script.getBody());
        }
    }

    @Override
    public void onValidationSucceeded() {
        // Update body
        DBHandler dbHandler = new DBHandler(this, null, null, 1);
        dbHandler.updateRow(script.get_id(),scriptNameEditText.getText().toString(),
                scriptBodyEditText.getText().toString());
        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
        // Load updated script
        Intent intent = new Intent(getApplicationContext(), ReadNavDrActivity.class);
        Script updatedScript = dbHandler.getScript(scriptNameEditText.getText().toString());
        intent.putExtra(PARCELABLE_SCRIPT_KEY, updatedScript);
        startActivity(intent);
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
