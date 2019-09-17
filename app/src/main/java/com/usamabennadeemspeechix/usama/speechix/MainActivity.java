package com.usamabennadeemspeechix.usama.speechix;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.OpenableColumns;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.usamabennadeemspeechix.usama.speechix.addNewScript.AddNewScriptActivity;
import com.usamabennadeemspeechix.usama.speechix.addNewScript.database.DBHandler;
import com.usamabennadeemspeechix.usama.speechix.addNewScript.database.Script;
import com.usamabennadeemspeechix.usama.speechix.readScript.ReadNavDrActivity;
import com.usamabennadeemspeechix.usama.speechix.scriptsMenu.ScriptMenuRecyclerAdapter;
import com.usamabennadeemspeechix.usama.speechix.utils.RecyclerTouchListener;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import io.fabric.sdk.android.Fabric;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener,
        SearchView.OnQueryTextListener {

    private static final int OPEN_REQUEST_CODE = 41;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ScriptMenuRecyclerAdapter adapter;

    Parcelable listState;
    final String STATE_KEY = "STATE";
    final String PARCELABLE_SCRIPT_KEY = "SCRIPT";
    DBHandler dbHandler;
    TextView tempTextView;

    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaBtn;
    private RapidFloatingActionHelper rfabHelper;
    List<String> scriptNames;
    View contextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





        rfaLayout = findViewById(R.id.activity_main_rfal);
        rfaBtn = findViewById(R.id.activity_main_rfab);

        tempTextView = findViewById(R.id.tempTextView);
        // start addNewScriptActivity when user taps on the floating action button
        startAddNewScriptActivity();
        dbHandler = new DBHandler(this, null, null, 1);
        scriptNames = dbHandler.getAllScriptNames();

        recyclerView = findViewById(R.id.script_menu_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ScriptMenuRecyclerAdapter(scriptNames);
        recyclerView.setAdapter(adapter);
        // abstracted away click listener initialization
        setRecyclerViewItemClickListner();

        setUpExpandableFloatingActionButton();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);


        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);


        return true;
    }

    private void setUpExpandableFloatingActionButton() {
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(getApplicationContext());
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Write Script")
                .setLabelSizeSp(15)
                .setResId(R.drawable.ic_keyboard_black_24dp)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setLabelColor(0xff283593)
                .setLabelSizeSp(14)
                .setWrapper(0)

        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Import Script")
                .setLabelSizeSp(15)
                .setResId(R.drawable.ic_import_export_black_24dp)
                .setIconNormalColor(0xff4e342e)
                .setIconPressedColor(0xff3e2723)
                .setLabelColor(0xff056f00)
                .setLabelSizeSp(14)
                .setWrapper(1)
        );
        rfaContent
                .setItems(items)
                .setIconShadowColor(0xff888888)

        ;
        rfabHelper = new RapidFloatingActionHelper(
                getApplicationContext(),
                rfaLayout,
                rfaBtn,
                rfaContent
        ).build();
    }


    private void displayImportDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.make_sure);
        alertDialogBuilder.setPositiveButton(R.string.got_it, (dialogInterface, i) -> openFile());
        alertDialogBuilder.show();
    }

    public void startAddNewScriptActivity() {
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), AddNewScriptActivity.class)));


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        listState = layoutManager.onSaveInstanceState();
        outState.putParcelable(STATE_KEY, listState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listState != null) {
            layoutManager.onRestoreInstanceState(listState);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null)
            listState = savedInstanceState.getParcelable(STATE_KEY);
    }

    public void setRecyclerViewItemClickListner() {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                TextView scriptNameTextView = view.findViewById(R.id.script_card_name);
                String scriptName = scriptNameTextView.getText().toString();
                Script script = dbHandler.getScript(scriptName);
                Intent intent = new Intent(getApplicationContext(), ReadNavDrActivity.class);
                intent.putExtra(PARCELABLE_SCRIPT_KEY, script);
                // Toast.makeText(getApplicationContext(), scriptName + " is selected!" + position, Toast.LENGTH_SHORT).show();

                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }


    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        Uri currentUri = null;

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == OPEN_REQUEST_CODE) {

                if (resultData != null) {
                    currentUri = resultData.getData();

                    //            String content =readFileContent(currentUri);
                    FileTask fileTask = new FileTask();
                    fileTask.execute(currentUri);
                    String content = null;
                    try {
                        content = fileTask.get()[1];
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }


                    //  String displayName = dumpImageMetaData(currentUri);
                    String displayName = null;
                    try {
                        displayName = fileTask.get()[0];
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    //tempTextView.setText(content);
                    Script script = new Script();
                    script.setName(displayName);
                    script.setBody(content);
                    Intent intent = new Intent(getApplicationContext(), AddNewScriptActivity.class);
                    intent.putExtra(PARCELABLE_SCRIPT_KEY, script);
                    startActivity(intent);
                }
            }
        }
    }

    public void openFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        startActivityForResult(intent, OPEN_REQUEST_CODE);
    }

    public String readFileContent(Uri uri) throws IOException {
        InputStream inputStream =
                getContentResolver().openInputStream(uri);
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(
                        inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String currentline;
        while ((currentline = reader.readLine()) != null) {
            stringBuilder.append(currentline + "\n");
        }
        inputStream.close();
        return stringBuilder.toString();
    }

    public String dumpImageMetaData(Uri uri) {

        String displayName = null;

        // The query, since it only applies to a single document, will only return
        // one row. There's no need to filter, sort, or select fields, since we want
        // all fields for one document.
        Cursor cursor = getApplicationContext().getContentResolver()
                .query(uri, null, null, null, null, null);

        try {
            // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (cursor != null && cursor.moveToFirst()) {

                // Note it's called "Display Name".  This is
                // provider-specific, and might not necessarily be the file name.
                displayName = cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                Log.i("TAG", "Display Name: " + displayName);

                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                // If the size is unknown, the value stored is null.  But since an
                // int can't be null in Java, the behavior is implementation-specific,
                // which is just a fancy term for "unpredictable".  So as
                // a rule, check if it's null before assigning to an int.  This will
                // happen often:  The storage API allows for remote files, whose
                // size might not be locally known.
                String size = null;
                if (!cursor.isNull(sizeIndex)) {
                    // Technically the column stores an int, but cursor.getString()
                    // will do the conversion automatically.
                    size = cursor.getString(sizeIndex);
                } else {
                    size = "Unknown";
                }
                Log.i("TAG", "Size: " + size);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return displayName;
    }

    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {

        if (position == 0) {
            startActivity(new Intent(getApplicationContext(), AddNewScriptActivity.class));
        } else if (position == 1) {
            displayImportDialog();
        }

    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        if (position == 0) {
            startActivity(new Intent(getApplicationContext(), AddNewScriptActivity.class));
        } else if (position == 1) {
            displayImportDialog();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        String userInput = s.toLowerCase();
        List<String> newList = new ArrayList<>();
        for (String name : scriptNames) {
            if (name.toLowerCase().contains(userInput)) {
                newList.add(name);
            }
        }
        adapter.updateList(newList);

        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public class FileContentTask extends AsyncTask<Uri, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* Toast toast= Toast.makeText(MainActivity.this,
                    "Your string here", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();*/


        }

        @Override
        protected String doInBackground(Uri... uris) {

            String content = null;
            try {
                content = readFileContent(uris[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    public class FileTask extends AsyncTask<Uri, Void, String[]> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast toast= Toast.makeText(MainActivity.this,
                    "Reading your file", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();


        }

        @Override
        protected String[] doInBackground(Uri... uris) {

            String name = dumpImageMetaData(uris[0]);
            String content = null;
            try {
                content = readFileContent(uris[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new String[]{name, content};
        }
    }
}
