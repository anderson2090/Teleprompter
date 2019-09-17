package com.usamabennadeemspeechix.usama.speechix.readScript;

import android.Manifest;
import android.app.Dialog;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pixplicity.easyprefs.library.Prefs;
import com.usamabennadeemspeechix.usama.speechix.ChangePasswordActivity;
import com.usamabennadeemspeechix.usama.speechix.MainActivity;
import com.usamabennadeemspeechix.usama.speechix.R;
import com.usamabennadeemspeechix.usama.speechix.addNewScript.database.DBHandler;
import com.usamabennadeemspeechix.usama.speechix.addNewScript.database.Script;
import com.usamabennadeemspeechix.usama.speechix.authentication.AuthenticationActivity;
import com.usamabennadeemspeechix.usama.speechix.authentication.ForgotPasswordActivity;
import com.usamabennadeemspeechix.usama.speechix.editScript.EditScriptActivity;
import com.usamabennadeemspeechix.usama.speechix.utils.RecyclerTouchListener;
import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.ALPHA_COLOR_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.AUTO_ROTATION_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.BACKGROUND_COLOR_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.BACK_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.CAMERA_FACING_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.CAMERA_ON_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.FONT_SIZE_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.FRONT_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.HIGHLIGHTER_COLOR_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.HIGHLIGHTER_HEIGHT;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.HIGHLIGHTER_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.HIGHLIGHTER_OPACITY_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.IS_HIGHLIGHTER_ON_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.LANDSCAPE_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.MILLISECONDS_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.MIRRORRED_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.ORIENTATION_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.PARCELABLE_SCRIPT_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.PIXELS_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.PORTRAIT_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.READ_ACTIVITY_REMOTE_STATE;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.REMOTE_APP_STATE;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.SCRIPT_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.TEXT_COLOR_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.VIDEO_QUALITY_KEY;
import static com.usamabennadeemspeechix.usama.speechix.utils.Utils.mirrorTextView;
import static com.usamabennadeemspeechix.usama.speechix.utils.Utils.unMirrorTextView;

public class ReadNavDrActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    int backGroundColor;
    int textColor;
    Script script;
    Bundle bundle;
    DBHandler dbHandler;
    TextView readScriptScriptBodyTextView;
    FancyButton fontSizeButton;
    Dialog backgroundDialog;
    Dialog textDialog;
    FancyButton textColorButton;
    LinearLayout backgroundLayout;
    FancyButton backgroundColorButton;
    ConstraintLayout scriptBodyScrollView;
    LinearLayout readActivityHighlighterLayout;

    FancyButton pixelsButton;
    FancyButton milliSecondsButton;
    String milliSeconds;
    String pixels;
    String fontSize;

    SwitchCompat highlighterSwitch;
    SwitchCompat mirroredSwitch;
    MenuItem signOutMenuItem;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String remoteStatus;
    private ValueEventListener remoteStateEventListener;
    DatabaseReference remoteStateDatabaseReference;
    FancyButton highlighterColorButton;
    FancyButton highlighterColorOpacityButton;
    FancyButton highlighterHeightButton;
    int highlighterColor;
    int opacity;
    FancyButton themeButton;
    private int alphaColor;
    private int height;

    private NavigationView navigationView;
    private Menu navigationMenu;
    private MenuItem loginMenuItem;
    private TextView navEmailTextView;
    private MenuItem forgotPasswordMenuItem;
    private MenuItem changePasswordMenuItem;
    FancyButton toggleCameraButton;

    SwitchCompat videoSwitch;
    CameraView cameraView;
    FancyButton videoQualityButton;
    private int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_nav_dr);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        setRemoteState("pause");


        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
        milliSeconds = Prefs.getString(MILLISECONDS_KEY, 10 + "");
        pixels = Prefs.getString(PIXELS_KEY, 5 + "");
        fontSize = Prefs.getString(FONT_SIZE_KEY, 14 + "");
        backGroundColor = Prefs.getInt(BACKGROUND_COLOR_KEY, Color.parseColor("#000000"));
        textColor = Prefs.getInt(TEXT_COLOR_KEY, Color.parseColor("#ffffff"));


        highlighterColorButton = findViewById(R.id.highlighter_color_button);
        highlighterColorOpacityButton = findViewById(R.id.highlightor_color_opacity_button);
        opacity = Prefs.getInt(HIGHLIGHTER_OPACITY_KEY, 0);
        highlighterColorOpacityButton.setText(opacity + "%");
        highlighterHeightButton = findViewById(R.id.highlightor_height_button);
        setHighlighterHeightButtonClickListener();


        backgroundLayout = findViewById(R.id.read_activity_root_layout);
        backgroundLayout.setBackgroundColor(backGroundColor);
        readActivityHighlighterLayout = findViewById(R.id.read_activity_highlighter_layout);
        readActivityHighlighterLayout.setBackgroundColor(Prefs.getInt(HIGHLIGHTER_COLOR_KEY, Color.parseColor("#800000ff")));

        backgroundColorButton = findViewById(R.id.background_color_button);
        // backgroundColorButton.setBackgroundColor(backGroundColor);

        readScriptScriptBodyTextView = findViewById(R.id.readScriptScriptBodyTextView);
        readScriptScriptBodyTextView.setTextColor(textColor);
        readScriptScriptBodyTextView.setTextSize(Float.parseFloat(fontSize));

        scriptBodyScrollView = findViewById(R.id.script_body_scroll_view);

        highlighterSwitch = findViewById(R.id.highlighter_switch);
        setHighlighterSwitchListener();

        setHighlighterColorButtonListener();

        setHighLighterOpacityButtonListener();


        milliSecondsButton = findViewById(R.id.milliseconds_button);
        milliSecondsButton.setText("in " + milliSeconds + " milliseconds");
        setMilliSecondButtonClickListener();


        pixelsButton = findViewById(R.id.read_activity_pixels_button);
        pixelsButton.setText("Scroll " + pixels + " pixels");
        setPixelsButtonClickListener();

        fontSizeButton = findViewById(R.id.font_size_button);
        fontSizeButton.setText(fontSize);
        setFontSizeButtonClickListener();
        Button playImageView = findViewById(R.id.play_button_image_view);
        playImageView.setOnClickListener(view -> {

            setRemoteState("play");
            startFullScreenActivity();

        });

        dbHandler = new DBHandler(this, null, null, 1);
        setScriptBodyText(readScriptScriptBodyTextView);


        bundle = getIntent().getExtras();
        if (bundle != null) {
            script = bundle.getParcelable(PARCELABLE_SCRIPT_KEY);
            if (script != null) {
                readScriptScriptBodyTextView.setText(script.getBody());
                setTitle(script.getName());
            }


        }

//        if (bundle != null) {
//            String scriptBody = bundle.getString(SCRIPT_KEY);
//            if (scriptBody != null) {
//                readScriptScriptBodyTextView.setText(scriptBody);
//            }
//        }


        setBackgroundColorButtonClickListener();

        textColorButton = findViewById(R.id.text_color_button);
        //  textColorButton.setBackgroundColor(textColor);
        setTextColorButtonClickListener();

        int opacity = Prefs.getInt(HIGHLIGHTER_OPACITY_KEY, 50);
        setOpacity(opacity);


        if (firebaseUser != null) {
            // readRemoteState();
            remoteStateEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    String remoteState = (String) dataSnapshot.getValue();

                    if (remoteState != null && remoteState.equalsIgnoreCase("play")) {
                        startFullScreenActivity();
                    }

                    // ...
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message

                    // ...
                }
            };
            remoteStateDatabaseReference = firebaseDatabase.getReference(firebaseUser.getUid())
                    .child("status").child(READ_ACTIVITY_REMOTE_STATE);
            remoteStateDatabaseReference.addValueEventListener(remoteStateEventListener);
        }


        themeButton = findViewById(R.id.theme_button);
        setThemeButtonOnCLickListener();
        height = Prefs.getInt(HIGHLIGHTER_HEIGHT, 200);
        highlighterHeightButton.setText(height + "");
        setHighlighterHeight(height);


        mirroredSwitch = findViewById(R.id.mirrored_switch);
        setMirrorredListener();


        navDrawerEmailListener();
        navigationMenuListener();

        videoSwitch = findViewById(R.id.recorder_switch);
        setVideoSwitchListener();

        toggleCameraButton = findViewById(R.id.flip_camera_button);
        toggleCameraButton.setFontIconSize(30);
        setToggleCameraButtonListener();

        videoQualityButton = findViewById(R.id.video_quality_button);
        setVideoQualityButtonListener();


        if (Prefs.getBoolean(CAMERA_ON_KEY, false)) {
            videoSwitch.setChecked(true);
            askForPermissions();
        }

    }

    private void setVideoQualityButtonListener() {
        String[] qualities = {"480p", "720p", "1080p", "2160p"};
        index = Prefs.getInt(VIDEO_QUALITY_KEY, 0);
        videoQualityButton.setText(qualities[index]);
        videoQualityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (index == qualities.length - 1) {
                    index = 0;
                    videoQualityButton.setText(qualities[index]);
                    Prefs.putInt(VIDEO_QUALITY_KEY, index);
                } else {
                    index++;
                    videoQualityButton.setText(qualities[index]);
                    Prefs.putInt(VIDEO_QUALITY_KEY, index);
                }
            }
        });
    }

    private void setToggleCameraButtonListener() {

        toggleCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cameraView.getFacing() == CameraKit.Constants.FACING_FRONT) {
                    cameraView.setFacing(CameraKit.Constants.FACING_BACK);
                    Prefs.putString(CAMERA_FACING_KEY, BACK_KEY);
                } else {
                    cameraView.setFacing(CameraKit.Constants.FACING_FRONT);
                    Prefs.putString(CAMERA_FACING_KEY, FRONT_KEY);
                }
            }
        });

    }


    private void setVideoSwitchListener() {
        videoSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {

            if (isChecked) {
                askForPermissions();
            } else {
                turnOffCamera();

            }


        });
    }

    private void askForPermissions() {
        Dexter.withActivity(ReadNavDrActivity.this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

                if (report.areAllPermissionsGranted()) {
                    turnOnCamera();

                }

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                DialogOnAnyDeniedMultiplePermissionsListener.Builder
                        .withContext(getApplicationContext())
                        .withTitle("Camera & audio permission")
                        .withMessage("Both camera and audio permission are needed to take pictures of your cat")
                        .withButtonText(android.R.string.ok)
                        /*.withIcon(R.mipmap.my_icon)*/
                        .build();
                token.continuePermissionRequest();
            }
        }).check();
    }

    private void turnOffCamera() {
        if (cameraView != null) {
            cameraView.stop();
            cameraView.setVisibility(View.GONE);
            cameraView = null;
            toggleCameraButton.setVisibility(View.GONE);
            videoQualityButton.setVisibility(View.GONE);
            Prefs.putBoolean(CAMERA_ON_KEY, false);
        }
    }

    private void turnOnCamera() {
        cameraView = findViewById(R.id.camera_view);

        cameraView.setVisibility(View.VISIBLE);

        if (Prefs.getString(CAMERA_FACING_KEY, BACK_KEY).equalsIgnoreCase(BACK_KEY)) {
            cameraView.setFacing(CameraKit.Constants.FACING_BACK);
        } else if (Prefs.getString(CAMERA_FACING_KEY, BACK_KEY).equalsIgnoreCase(FRONT_KEY)) {
            cameraView.setFacing(CameraKit.Constants.FACING_FRONT);
        }

        cameraView.start();
        toggleCameraButton.setVisibility(View.VISIBLE);
        videoQualityButton.setVisibility(View.VISIBLE);
        Prefs.putBoolean(CAMERA_ON_KEY, true);
    }

    private void navDrawerEmailListener() {
        View header = navigationView.getHeaderView(0);
        navEmailTextView = header.findViewById(R.id.nav_email);


        if (firebaseUser != null) {
            navEmailTextView.setText(firebaseUser.getEmail());
        } else {
            navEmailTextView.setText("You are not logged in");
        }
    }

    private void setMirrorredListener() {
        if (Prefs.getBoolean(MIRRORRED_KEY, false)) {
            mirroredSwitch.setChecked(true);
            mirrorTextView(readScriptScriptBodyTextView);
        } else {
            mirroredSwitch.setChecked(false);
            unMirrorTextView(readScriptScriptBodyTextView);
        }
        mirroredSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {

                mirrorTextView(readScriptScriptBodyTextView);

                Prefs.putBoolean(MIRRORRED_KEY, true);
            } else {
                unMirrorTextView(readScriptScriptBodyTextView);

                Prefs.putBoolean(MIRRORRED_KEY, false);
            }

        });
    }

    private void setThemeButtonOnCLickListener() {
        themeButton.setOnClickListener(view -> {
            Dialog themeDialog = new Dialog(ReadNavDrActivity.this);
            themeDialog.setContentView(R.layout.theme_dialog);
            themeDialog.setCanceledOnTouchOutside(true);
            themeDialog.setCancelable(true);
            themeDialog.show();

            RecyclerView themeRecyclerView = themeDialog.findViewById(R.id.theme_recyclerView);
            LinearLayoutManager themeRecyclerViewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            themeRecyclerView.setLayoutManager(themeRecyclerViewLayoutManager);
            ThemesRecyclerAdapter themesRecyclerAdapter = new ThemesRecyclerAdapter();
            themeRecyclerView.setAdapter(themesRecyclerAdapter);
            themeRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), themeRecyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    implementTheme(view);
                    themeDialog.dismiss();
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        });
    }

    private void setTextColorButtonClickListener() {
        textColorButton.setOnClickListener(view -> {
//            textDialog = new Dialog(ReadActivity.this);
//            textDialog.setContentView(R.layout.text_colors_dialog);
//            textDialog.show();

            Dialog textDialog = new Dialog(ReadNavDrActivity.this);
            textDialog.setContentView(R.layout.text_color_dialog);
            textDialog.setCanceledOnTouchOutside(true);
            textDialog.setCancelable(true);
            textDialog.show();

            RecyclerView textRecyclerView = textDialog.findViewById(R.id.text_color_recyclerView);
            LinearLayoutManager textRecyclerViewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            textRecyclerView.setLayoutManager(textRecyclerViewLayoutManager);
            TextColorRecyclerAdapter themesRecyclerAdapter = new TextColorRecyclerAdapter();
            textRecyclerView.setAdapter(themesRecyclerAdapter);
            textRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), textRecyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    setTextColor(view);
                    textDialog.dismiss();
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        });
    }

    private void setBackgroundColorButtonClickListener() {
        backgroundColorButton.setOnClickListener(view -> {
//            backgroundDialog = new Dialog(ReadActivity.this);
//            backgroundDialog.setContentView(R.layout.background_colors_dialog);
//            backgroundDialog.show();

            Dialog textDialog = new Dialog(ReadNavDrActivity.this);
            textDialog.setContentView(R.layout.text_color_dialog);
            textDialog.setCanceledOnTouchOutside(true);
            textDialog.setCancelable(true);
            textDialog.show();

            RecyclerView textRecyclerView = textDialog.findViewById(R.id.text_color_recyclerView);
            LinearLayoutManager textRecyclerViewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            textRecyclerView.setLayoutManager(textRecyclerViewLayoutManager);
            TextColorRecyclerAdapter themesRecyclerAdapter = new TextColorRecyclerAdapter();
            textRecyclerView.setAdapter(themesRecyclerAdapter);
            textRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), textRecyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    setBackgroundColor(view);
                    textDialog.dismiss();
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));


        });
    }

    private void setFontSizeButtonClickListener() {
        fontSizeButton.setOnClickListener(view -> {

            AlertDialog.Builder fontSizeAlert = new AlertDialog.Builder(ReadNavDrActivity.this);
            EditText enterFontSizeEditText = new EditText(ReadNavDrActivity.this);
            enterFontSizeEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            enterFontSizeEditText.requestFocus();
            enterFontSizeEditText.setText(Prefs.getString(FONT_SIZE_KEY, 14 + ""));
            enterFontSizeEditText.setGravity(Gravity.CENTER);
            fontSizeAlert.setView(enterFontSizeEditText);
            fontSizeAlert.setMessage("Enter the font size");
            fontSizeAlert.setPositiveButton("OK", (dialogInterface, i) -> {
                if ((isEmpty(enterFontSizeEditText)) || (Float.parseFloat(enterFontSizeEditText.getText().toString()) < 14)) {
                    fontSize = 14 + "";
                    fontSizeButton.setText(fontSize);

                } else {
                    fontSize = enterFontSizeEditText.getText().toString();
                    readScriptScriptBodyTextView.setTextSize(Float.parseFloat(fontSize));
                    fontSizeButton.setText(fontSize);
                }
                Prefs.putString(FONT_SIZE_KEY, fontSize);
            });
            fontSizeAlert.setNegativeButton("Cancel", (dialogInterface, i) -> {

            });
            fontSizeAlert.show();
        });
    }

    private void setPixelsButtonClickListener() {
        pixelsButton.setOnClickListener(view -> {
            AlertDialog.Builder pixelsAlert = new AlertDialog.Builder(ReadNavDrActivity.this);
            EditText enterPixelsEditText = new EditText(ReadNavDrActivity.this);
            enterPixelsEditText.setGravity(Gravity.CENTER);
            enterPixelsEditText.requestFocus();
            enterPixelsEditText.setText(Prefs.getString(PIXELS_KEY, 5 + ""));
            enterPixelsEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            pixelsAlert.setMessage("Enter the number of pixels you want to scroll in a specific time.");
            pixelsAlert.setView(enterPixelsEditText);
            pixelsAlert.setPositiveButton("OK", (dialogInterface, i) -> {

                if ((isEmpty(enterPixelsEditText)) || (Float.parseFloat(enterPixelsEditText.getText().toString()) < 1)) {
                    pixels = 1 + "";
                    pixelsButton.setText("Scroll " + pixels + " pixels");
                } else {
                    pixels = enterPixelsEditText.getText().toString();
                    pixelsButton.setText("Scroll " + pixels + " pixels");
                }
                Prefs.putString(PIXELS_KEY, pixels);
            });
            pixelsAlert.setNegativeButton("Cancel", (dialogInterface, i) -> {
                //Dismiss
            });
            pixelsAlert.show();
        });
    }

    private void setMilliSecondButtonClickListener() {
        milliSecondsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder millisecondsAlert = new AlertDialog.Builder(ReadNavDrActivity.this);
                EditText enterMillisecondsEditText = new EditText(ReadNavDrActivity.this);
                enterMillisecondsEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                enterMillisecondsEditText.requestFocus();
                enterMillisecondsEditText.setText(Prefs.getString(MILLISECONDS_KEY, 10 + ""));
                enterMillisecondsEditText.setGravity(Gravity.CENTER);
                millisecondsAlert.setView(enterMillisecondsEditText);
                millisecondsAlert.setMessage("Enter the number of milliseconds");
                millisecondsAlert.setPositiveButton("OK", (dialogInterface, i) -> {

                    if ((isEmpty(enterMillisecondsEditText)) || (Float.parseFloat(enterMillisecondsEditText.getText().toString()) < 1)) {
                        milliSeconds = 1 + "";
                        milliSecondsButton.setText("in " + milliSeconds + " milliseconds");
                    } else {
                        milliSeconds = enterMillisecondsEditText.getText().toString();
                        milliSecondsButton.setText("in " + milliSeconds + " milliseconds");
                    }
                    Prefs.putString(MILLISECONDS_KEY, milliSeconds);
                    Log.i(MILLISECONDS_KEY, milliSeconds);
                });
                millisecondsAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                millisecondsAlert.show();

            }
        });
    }

    private void setHighLighterOpacityButtonListener() {
        highlighterColorOpacityButton.setOnClickListener(view -> {

            AlertDialog.Builder highlighterOpacityALert = new AlertDialog.Builder(this);
            EditText opacityEditText = new EditText(this);
            opacityEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            opacityEditText.setGravity(Gravity.CENTER);
            highlighterOpacityALert.setView(opacityEditText);
            highlighterOpacityALert.setMessage("Enter opacity between 1 and 255");
            highlighterOpacityALert.setPositiveButton("OK", (dialogInterface, i) -> {
                if (isEmpty(opacityEditText) || (Integer.parseInt(opacityEditText.getText().toString()) < 1)) {
                    opacity = 1;
                } else if (Integer.parseInt(opacityEditText.getText().toString()) > 255) {
                    opacity = 255;
                } else {
                    opacity = Integer.parseInt(opacityEditText.getText().toString());
                }
                Prefs.putInt(HIGHLIGHTER_OPACITY_KEY, opacity);
                highlighterColorOpacityButton.setText(opacity + "");

//                String hexOpacityValue = "00";
//
//
                //                String hexColor = String.format("#%06X", (0xFFFFFF & highlighterCurrentColor));
//                hexColor = hexColor.concat(hexOpacityValue);
//                int colorWithOpacity = Color.parseColor(hexColor);
//                readActivityHighlighterLayout.setBackgroundColor(colorWithOpacity);
                setOpacity(opacity);


            });
            highlighterOpacityALert.setNegativeButton("Cancel", (dialogInterface, i) -> {

            });
            highlighterOpacityALert.show();


        });
    }

    private void setOpacity(int opacity) {
        ColorDrawable highlighterDrawable = (ColorDrawable) readActivityHighlighterLayout.getBackground();
        int highlighterCurrentColor = highlighterDrawable.getColor();


        int currentColor = Prefs.getInt(HIGHLIGHTER_COLOR_KEY, highlighterCurrentColor);
        alphaColor = ColorUtils.setAlphaComponent(currentColor, opacity);

        readActivityHighlighterLayout.setBackgroundColor(alphaColor);
    }

    private void setHighlighterColorButtonListener() {
        highlighterColorButton.setOnClickListener(view -> {
            Dialog highlighterColorDialog = new Dialog(ReadNavDrActivity.this);
            highlighterColorDialog.setContentView(R.layout.text_color_dialog);
            highlighterColorDialog.setCanceledOnTouchOutside(true);
            highlighterColorDialog.setCancelable(true);
            highlighterColorDialog.show();

            RecyclerView textRecyclerView = highlighterColorDialog.findViewById(R.id.text_color_recyclerView);
            LinearLayoutManager textRecyclerViewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            textRecyclerView.setLayoutManager(textRecyclerViewLayoutManager);
            TextColorRecyclerAdapter themesRecyclerAdapter = new TextColorRecyclerAdapter();
            textRecyclerView.setAdapter(themesRecyclerAdapter);
            textRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), textRecyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    ColorDrawable drawable = (ColorDrawable) view.findViewById(R.id.text_color_layout).getBackground();
                    highlighterColor = drawable.getColor();
                    readActivityHighlighterLayout.setBackgroundColor(highlighterColor);
                    highlighterColorDialog.dismiss();
                    Prefs.putInt(HIGHLIGHTER_COLOR_KEY, highlighterColor);
                    int opacity = Prefs.getInt(HIGHLIGHTER_OPACITY_KEY, 50);
                    setOpacity(opacity);

                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));


        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.read_activity_menu, menu);
        return true;
    }

    // How you doin compiler?

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.readActivityMenuEdit) {
            //Toast.makeText(getApplicationContext(), "Start edit activity", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), EditScriptActivity.class);
            intent.putExtra(PARCELABLE_SCRIPT_KEY, script);
            startActivity(intent);
            return true;
        } else if (id == R.id.readActivityMenuDelete) {
            // Display confirmation backgroundDialog
            AlertDialog.Builder confirmDialog = new AlertDialog.Builder(this);
            confirmDialog.setIcon(R.drawable.ic_warning_black_24dp);
            confirmDialog.setMessage("Are you sure?");

            confirmDialog.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                if (dbHandler.deleteScript(script.getName())) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                }
            });
            confirmDialog.setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });
            confirmDialog.show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void setScriptBodyText(TextView readScriptScriptBodyTextView) {

    }


    public void setBackgroundColor(View view) {

        ColorDrawable drawable = (ColorDrawable) view.findViewById(R.id.text_color_layout).getBackground();
        backGroundColor = drawable.getColor();

        // backgroundColorButton.setBackgroundColor(backGroundColor);
        backgroundLayout.setBackgroundColor(backGroundColor);
        Prefs.putInt(BACKGROUND_COLOR_KEY, backGroundColor);
        // backgroundDialog.dismiss();
    }

    public void setTextColor(View view) {
        ColorDrawable drawable = (ColorDrawable) view.findViewById(R.id.text_color_layout).getBackground();
        textColor = drawable.getColor();
        readScriptScriptBodyTextView.setTextColor(textColor);
        //textColorButton.setBackgroundColor(textColor);
        Prefs.putInt(TEXT_COLOR_KEY, textColor);
//        textDialog.show();
//        textDialog.dismiss();
    }

    public void implementTheme(View view) {
        //set text color
        TextView themeCardTextView = view.findViewById(R.id.theme_card_text);
        readScriptScriptBodyTextView.setTextColor(themeCardTextView.getCurrentTextColor());
        // textColorButton.setBackgroundColor(textColor);
        Prefs.putInt(TEXT_COLOR_KEY, themeCardTextView.getCurrentTextColor());

        // set background color
        ColorDrawable backgroundDrawable = (ColorDrawable) view.findViewById(R.id.theme_card_layout).getBackground();
        backGroundColor = backgroundDrawable.getColor();
        // backgroundColorButton.setBackgroundColor(backGroundColor);
        backgroundLayout.setBackgroundColor(backGroundColor);
        Prefs.putInt(BACKGROUND_COLOR_KEY, backGroundColor);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ColorDrawable backgroundDrawable = (ColorDrawable) backgroundLayout.getBackground();
        int backGroundColor = backgroundDrawable.getColor();

        int textColor = readScriptScriptBodyTextView.getCurrentTextColor();
        outState.putInt(TEXT_COLOR_KEY, textColor);
        outState.putInt(BACKGROUND_COLOR_KEY, backGroundColor);
        outState.putIntArray("ARTICLE_SCROLL_POSITION",
                new int[]{scriptBodyScrollView.getScrollX(), scriptBodyScrollView.getScrollY()});
        outState.putString(FONT_SIZE_KEY, fontSizeButton.getText().toString());
        outState.putString(PIXELS_KEY, pixelsButton.getText().toString());
        outState.putString(MILLISECONDS_KEY, milliSecondsButton.getText().toString());
        outState.putInt(HIGHLIGHTER_COLOR_KEY, highlighterColor);
        outState.putInt(HIGHLIGHTER_OPACITY_KEY, opacity);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            int backGroundColor = savedInstanceState.getInt(BACKGROUND_COLOR_KEY);
            int textColor = savedInstanceState.getInt(TEXT_COLOR_KEY);
            //textColorButton.setBackgroundColor(textColor);
            readScriptScriptBodyTextView.setTextColor(textColor);
            backgroundLayout.setBackgroundColor(backGroundColor);
            //backgroundColorButton.setBackgroundColor(backGroundColor);

            fontSizeButton.setText(savedInstanceState.getString(FONT_SIZE_KEY));
            readScriptScriptBodyTextView.setTextSize(Float.parseFloat(savedInstanceState.getString(FONT_SIZE_KEY)));

            final int[] position = savedInstanceState.getIntArray("ARTICLE_SCROLL_POSITION");
            if (position != null)
                scriptBodyScrollView.post(() -> scriptBodyScrollView.scrollTo(position[0], position[1]));

            pixelsButton.setText(savedInstanceState.getString(PIXELS_KEY));
            milliSecondsButton.setText(savedInstanceState.getString(MILLISECONDS_KEY));
            highlighterColor = savedInstanceState.getInt(HIGHLIGHTER_COLOR_KEY);
            opacity = savedInstanceState.getInt(HIGHLIGHTER_OPACITY_KEY);
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseUser != null) {

            remoteStateDatabaseReference.removeEventListener(remoteStateEventListener);
        }

       /* ColorDrawable backgroundDrawable = (ColorDrawable) backgroundLayout.getBackground();
        int backGroundColor = backgroundDrawable.getColor();
         getIntent().putExtra(BACKGROUND_COLOR_KEY,backGroundColor);*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (cameraView != null) {
            if (!cameraView.isStarted()) {
                if (Prefs.getString(CAMERA_FACING_KEY, BACK_KEY).equalsIgnoreCase(BACK_KEY)) {
                    cameraView.setFacing(CameraKit.Constants.FACING_BACK);
                } else if (Prefs.getString(CAMERA_FACING_KEY, BACK_KEY).equalsIgnoreCase(FRONT_KEY)) {
                    cameraView.setFacing(CameraKit.Constants.FACING_FRONT);
                }
                cameraView.start();
            }

        }



        /*if(getIntent()!=null) {
            int backGroundColor = getIntent().getIntExtra(BACKGROUND_COLOR_KEY, 0);
            backgroundLayout.setBackgroundColor(backGroundColor);
        }*/

        setRemoteState("pause");
//        if(firebaseUser!=null) {
//
//            remoteStateDatabaseReference.addValueEventListener(remoteStateEventListener);
//        }
    }

    @Override
    protected void onPause() {

        if (cameraView != null) {
            cameraView.stop();
        }

        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (firebaseUser != null) {

            remoteStateDatabaseReference.addValueEventListener(remoteStateEventListener);
        }
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

    public void isRemoteLoggedIn() {
        ValueEventListener remoteStateListener = null;


        if (firebaseUser != null) {
            remoteStateListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    String value = (String) dataSnapshot.getValue();
//                    if (value != null && value.equalsIgnoreCase("logged in")) {
                    //Todo Fix button color
//                        remoteImageButton.setImageResource(R.drawable.ic_settings_remote_green_24dp);
//                    } else {
//                        remoteImageButton.setImageResource(R.drawable.ic_settings_remote_red_24dp);
//                    }
                    // ...
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //  Toast.makeText(getApplicationContext(), databaseError + "", Toast.LENGTH_SHORT).show();
                }
            };
        }
        if (firebaseUser != null) {
            DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseUser.getUid())
                    .child("status").child(REMOTE_APP_STATE);
            databaseReference.addValueEventListener(remoteStateListener);
        }


    }

    public void startFullScreenActivity() {
        Intent intent = null;
        if (Prefs.getBoolean(CAMERA_ON_KEY, false)) {
            intent = new Intent(getApplicationContext(), ReadFullscreenCameraActivity.class);
//            if (Prefs.getString(CAMERA_FACING_KEY, BACK_KEY).equalsIgnoreCase(BACK_KEY)) {
//                intent.putExtra(CAMERA_FACING_KEY, BACK_KEY);
//            } else if (Prefs.getString(CAMERA_FACING_KEY, BACK_KEY).equalsIgnoreCase(FRONT_KEY)) {
//                intent.putExtra(CAMERA_FACING_KEY, FRONT_KEY);
//            }
        } else if (!Prefs.getBoolean(CAMERA_ON_KEY, false)) {
            intent = new Intent(getApplicationContext(), ReadFullscreenActivity.class);
        }
        if (intent != null) {
            intent.putExtra(PARCELABLE_SCRIPT_KEY, script.getBody());
            intent.putExtra(FONT_SIZE_KEY, Float.parseFloat(fontSizeButton.getText().toString()));
            ColorDrawable backgroundDrawable = (ColorDrawable) backgroundLayout.getBackground();
            int backGroundColor = backgroundDrawable.getColor();
            intent.putExtra(BACKGROUND_COLOR_KEY, backGroundColor);
            intent.putExtra(TEXT_COLOR_KEY, readScriptScriptBodyTextView.getCurrentTextColor());
            intent.putExtra(PIXELS_KEY, pixels);
            intent.putExtra(MILLISECONDS_KEY, milliSeconds);
            intent.putExtra(ALPHA_COLOR_KEY, alphaColor);
            intent.putExtra(HIGHLIGHTER_HEIGHT, height);

            intent.putExtra(CAMERA_ON_KEY, Prefs.getBoolean(CAMERA_ON_KEY, false));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


            //intent.putExtra(BACKGROUND_COLOR_KEY, backGroundColor);

            startActivity(intent);
        }
    }

    public void setRemoteState(String state) {
        if (firebaseUser != null) {
            DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseUser.getUid())
                    .child("status").child(READ_ACTIVITY_REMOTE_STATE);
            databaseReference.setValue(state).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //  Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void readRemoteState() {

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String remoteState = (String) dataSnapshot.getValue();

                if (remoteState != null && remoteState.equalsIgnoreCase("play")) {
                    startFullScreenActivity();
                }

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                // ...
            }
        };
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseUser.getUid())
                .child("status").child(READ_ACTIVITY_REMOTE_STATE);
        databaseReference.addValueEventListener(postListener);

    }

    public void setHighlighterSwitchListener() {

        if (Prefs.getBoolean(HIGHLIGHTER_KEY, false)) {
            highlighterSwitch.setChecked(true);
            makeHighLighterVisible();
        } else {
            highlighterSwitch.setChecked(false);
            makeHighLighterInvisible();
        }

        highlighterSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                makeHighLighterVisible();
            } else {
                makeHighLighterInvisible();
            }
        });
    }

    private void makeHighLighterInvisible() {
        readActivityHighlighterLayout.setVisibility(View.GONE);
        highlighterColorOpacityButton.setVisibility(View.GONE);
        highlighterColorButton.setVisibility(View.GONE);
        highlighterHeightButton.setVisibility(View.GONE);
        Prefs.putBoolean(HIGHLIGHTER_KEY, false);
    }

    private void makeHighLighterVisible() {
        readActivityHighlighterLayout.setVisibility(View.VISIBLE);
        highlighterColorOpacityButton.setVisibility(View.VISIBLE);
        highlighterColorButton.setVisibility(View.VISIBLE);
        highlighterHeightButton.setVisibility(View.VISIBLE);
        Prefs.putBoolean(HIGHLIGHTER_KEY, true);
    }

    public void setHighlighterHeightButtonClickListener() {
        highlighterHeightButton.setOnClickListener(view -> {

            AlertDialog.Builder highlighterSizeAlert = new AlertDialog.Builder(ReadNavDrActivity.this);
            EditText highlighterSizeEditText = new EditText(ReadNavDrActivity.this);
            highlighterSizeEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            highlighterSizeEditText.setGravity(Gravity.CENTER);
            highlighterSizeAlert.setView(highlighterSizeEditText);
            highlighterSizeAlert.setMessage("Set highlighter's height in pixels");
            highlighterSizeAlert.setPositiveButton("OK", (dialogInterface, i) -> {
                height = Integer.parseInt(highlighterSizeEditText.getText().toString());

                if ((isEmpty(highlighterSizeEditText)) || height < 50) {
                    height = 50;
                    highlighterHeightButton.setText(height + "");
                    setHighlighterHeight(height);

                } else {
                    highlighterHeightButton.setText(height + "");
                    setHighlighterHeight(height);
                }

            });
            highlighterSizeAlert.setNegativeButton("Cancel", (dialogInterface, i) -> {

            });
            highlighterSizeAlert.show();

        });
    }

    private void setHighlighterHeight(int height) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) readActivityHighlighterLayout.getLayoutParams();
//        height = dpToPx(height);
        layoutParams.height = height;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;

        readActivityHighlighterLayout.setLayoutParams(layoutParams);
        Prefs.putInt(HIGHLIGHTER_HEIGHT, height);
    }

    public int dpToPx(int dp) {
        float density = getApplicationContext().getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }




    /*public void createTable() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if (FirebaseAuth.getInstance().getCurrentUser().getEmail() != null) {
                DatabaseReference databaseReference = firebaseDatabase.getReference();
                //Todo write code to handle database exceptions
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue(0);
            }
        }
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            super.onBackPressed();
        }
    }

    public void navigationMenuListener() {
        navigationMenu = navigationView.getMenu();
        loginMenuItem = navigationMenu.findItem(R.id.nav_login);
        signOutMenuItem = navigationMenu.findItem(R.id.nav_signout);
        forgotPasswordMenuItem = navigationMenu.findItem(R.id.nav_forgot_password);
        changePasswordMenuItem = navigationMenu.findItem(R.id.nav_change_password);

        if (firebaseUser != null) {
            loginMenuItem.setVisible(false);
            signOutMenuItem.setVisible(true);
            forgotPasswordMenuItem.setVisible(false);
            changePasswordMenuItem.setVisible(true);
        } else {
            loginMenuItem.setVisible(true);
            signOutMenuItem.setVisible(false);
            forgotPasswordMenuItem.setVisible(true);
            changePasswordMenuItem.setVisible(false);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            if (firebaseUser == null) {
                Intent intent = new Intent(getApplicationContext(), AuthenticationActivity.class);
                // intent.putExtra(SCRIPT_KEY, readScriptScriptBodyTextView.getText().toString());
                if (script != null) {
                    intent.putExtra(PARCELABLE_SCRIPT_KEY, script);
                }
                startActivity(intent);
            }
        } else if (id == R.id.nav_signout) {
            if (firebaseUser != null) {
                firebaseAuth.signOut();
                navEmailTextView.setText(R.string.you_are_not_logged_in);
                firebaseUser = null;
                Toast.makeText(getApplicationContext(), "You are signed out", Toast.LENGTH_SHORT).show();
            }
            if (!loginMenuItem.isVisible()) {
                loginMenuItem.setVisible(true);
            }
            if (signOutMenuItem.isVisible()) {
                signOutMenuItem.setVisible(false);
            }
            if (!forgotPasswordMenuItem.isVisible()) {
                forgotPasswordMenuItem.setVisible(true);
            }
            if (changePasswordMenuItem.isVisible()) {
                changePasswordMenuItem.setVisible(false);
            }
        } else if (id == R.id.nav_forgot_password) {
            Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
            intent.putExtra(PARCELABLE_SCRIPT_KEY, script);
            startActivity(intent);

        } else if (id == R.id.nav_change_password) {
            Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
            intent.putExtra(PARCELABLE_SCRIPT_KEY, script);
            startActivity(new Intent(intent));

        } else if (id == R.id.nav_lock_portrait) {

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            Prefs.putString(ORIENTATION_KEY, PORTRAIT_KEY);

        } else if (id == R.id.nav_lock_landscape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            Prefs.putString(ORIENTATION_KEY, LANDSCAPE_KEY);


        } else if (id == R.id.nav_auto_orientation) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
            Prefs.putString(ORIENTATION_KEY, AUTO_ROTATION_KEY);
        } else if (id == R.id.help_menu_item) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/pUMs13nQXPY")));
        }else if(id== R.id.privacy_policy_menu_item){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://usamabennadeem.blogspot.com/p/privacy-policy-usama-nadeem-built.html")));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
