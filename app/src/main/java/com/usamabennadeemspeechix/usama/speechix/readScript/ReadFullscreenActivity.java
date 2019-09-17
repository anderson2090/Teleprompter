package com.usamabennadeemspeechix.usama.speechix.readScript;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixplicity.easyprefs.library.Prefs;
import com.usamabennadeemspeechix.usama.speechix.R;
import com.usamabennadeemspeechix.usama.speechix.utils.Utils;

import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.ALPHA_COLOR_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.AUTO_ROTATION_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.BACKGROUND_COLOR_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.FONT_SIZE_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.HIGHLIGHTER_HEIGHT;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.HIGHLIGHTER_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.IS_HIGHLIGHTER_ON_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.IS_MIRRORED_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.LANDSCAPE_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.MILLISECONDS_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.MIRRORRED_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.ORIENTATION_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.PARCELABLE_SCRIPT_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.PIXELS_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.PORTRAIT_KEY;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.READ_ACTIVITY_REMOTE_STATE;
import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.TEXT_COLOR_KEY;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ReadFullscreenActivity extends AppCompatActivity {

    FrameLayout rootLayout;
    ScrollView fullScreenScrollView;
    final Handler timerHandler = new Handler();
    int pixels;
    int milliseconds;
    Button pausePlayButton;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    Boolean isPlaying = true;
    boolean highlighterOn;
    RelativeLayout highlighterLayout;


    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    private int alphaColor;
    private TextView scriptTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_read_fullscreen);
        setTitle(R.string.app_name);

        if (Prefs.getString(ORIENTATION_KEY, AUTO_ROTATION_KEY).equals(PORTRAIT_KEY)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (Prefs.getString(ORIENTATION_KEY, AUTO_ROTATION_KEY).equals(LANDSCAPE_KEY)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (Prefs.getString(ORIENTATION_KEY, AUTO_ROTATION_KEY).equals(AUTO_ROTATION_KEY)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        setRemoteState("null");
        readRemoteState();

        rootLayout = findViewById(R.id.read_full_screen_root_layout);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_script_text_view);

        fullScreenScrollView = findViewById(R.id.fullscreen_scroll_view);
        highlighterLayout = findViewById(R.id.highlighter_layout);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        pausePlayButton = findViewById(R.id.full_screen_pause_play_button);
        pausePlayButton.setOnClickListener(view -> {
            if (pausePlayButton.getText().toString().equalsIgnoreCase("pause")) {
                if (isPlaying) {
                    pause();
                }
//                timerHandler.removeCallbacks(timerRunnable);
//                pausePlayButton.setText(R.string.play);

            } else {
                if (!isPlaying) {
                    play();
                }
//                timerHandler.postDelayed(timerRunnable, 0);
//                pausePlayButton.setText(R.string.pause);

            }
        });
        findViewById(R.id.full_screen_pause_play_button).setOnTouchListener(mDelayHideTouchListener);

        // Get Script from bundle
        Bundle bundle = getIntent().getExtras();
        String script = null;
        if (bundle != null) {
            script = bundle.getString(PARCELABLE_SCRIPT_KEY);
        }
        scriptTextView = (TextView) mContentView;
        scriptTextView.setText(script);

        if (bundle != null) {
            float fontSize = bundle.getFloat(FONT_SIZE_KEY);
            //Toast.makeText(getApplicationContext(),fontSize+"",Toast.LENGTH_SHORT).show();
            scriptTextView.setTextSize(fontSize);

            int textColor = bundle.getInt(TEXT_COLOR_KEY);
            scriptTextView.setTextColor(textColor);

            int color = bundle.getInt(BACKGROUND_COLOR_KEY);
            rootLayout.setBackgroundColor(color);

            alphaColor = bundle.getInt(ALPHA_COLOR_KEY);

            pixels = Integer.parseInt(bundle.getString(PIXELS_KEY));
            milliseconds = Integer.parseInt(bundle.getString(MILLISECONDS_KEY));
            highlighterOn = bundle.getBoolean(IS_HIGHLIGHTER_ON_KEY);

            //Set Highlighter's height

            setHighlighterHeight(bundle.getInt(HIGHLIGHTER_HEIGHT));


            if (Prefs.getBoolean(MIRRORRED_KEY, false)) {
                Utils.mirrorTextView(scriptTextView);
            }


            //Toast.makeText(getApplicationContext(), bundle.getString(PIXELS_KEY) + "\n" + bundle.getString(MILLISECONDS_KEY), Toast.LENGTH_SHORT).show();

        }


        TextView countDownTextView = findViewById(R.id.count_down_text_view);
        RelativeLayout countDownLayout = findViewById(R.id.count_down_layout);

        new CountDownTimer(4000, 1000) {

            public void onTick(long millisUntilFinished) {
                countDownTextView.setText(millisUntilFinished / 1000 + "");
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                countDownLayout.setVisibility(View.GONE);
                if (Prefs.getBoolean(HIGHLIGHTER_KEY, false)) {
                    highlighterLayout.setVisibility(View.VISIBLE);
                    highlighterLayout.setBackgroundColor(alphaColor);
                }

                timerHandler.postDelayed(timerRunnable, 0);

            }

        }.start();

    }


    private void setHighlighterHeight(int height) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) highlighterLayout.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;

        highlighterLayout.setLayoutParams(layoutParams);
    }

    private void play() {

        timerHandler.postDelayed(timerRunnable, 0);
        pausePlayButton.setText(R.string.pause);
        isPlaying = true;

    }

    private void pause() {
        timerHandler.removeCallbacks(timerRunnable);
        pausePlayButton.setText(R.string.play);
        isPlaying = false;

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            fullScreenScrollView.smoothScrollBy(0, pixels);         // 5 is how many pixels you want it to scroll vertically by
            timerHandler.postDelayed(this, milliseconds);     // 10 is how many milliseconds you want this thread to run
            // Log.i("PIXELS", pixels + "");
            // Log.i("MILLISECONDS", milliseconds + "");

        }
    };

   /* start.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            timerHandler.postDelayed(timerRunnable, 0);
        }
    });

    stop.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            timerHandler.removeCallbacks(timerRunnable);
        }
    });*/


    public void readRemoteState() {

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String remoteState = (String) dataSnapshot.getValue();
                //Todo Create script state in database "Playing " and "paused"
                if (remoteState != null) {
                    if (remoteState.equalsIgnoreCase("play")) {
                        if (!isPlaying) {
                            play();
                        }

//                        timerHandler.postDelayed(timerRunnable, 0);
//                        pausePlayButton.setText(R.string.pause);
                        // pausePlayButton.performClick();
                    } else if (remoteState.equalsIgnoreCase("pause")) {
                        if (isPlaying) {
                            pause();
                        }
//                        timerHandler.removeCallbacks(timerRunnable);
//                        pausePlayButton.setText(R.string.play);
                        // pausePlayButton.performClick();

                    } else if (remoteState.equalsIgnoreCase("Up")) {
                        if (!isPlaying) {
                            fullScreenScrollView.smoothScrollBy(0, 200);
                        }
                    } else if (remoteState.equalsIgnoreCase("Down")) {
                        if (!isPlaying) {
                            fullScreenScrollView.smoothScrollBy(0, -200);
                        }
                    } else if (remoteState.equalsIgnoreCase("decrease_font")) {
                        if (!isPlaying) {
                            float px = scriptTextView.getTextSize();
                            float sp = px / getResources().getDisplayMetrics().scaledDensity;
                            sp--;
                            scriptTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);

                        }
                    } else if (remoteState.equalsIgnoreCase("increase_font")) {
                        if (!isPlaying) {
                            float px = scriptTextView.getTextSize();
                            float sp = px / getResources().getDisplayMetrics().scaledDensity;
                            sp++;
                            scriptTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                        }
                    } else if (remoteState.equalsIgnoreCase("increase_speed")) {
                        if (isPlaying) {
                            pixels = pixels + 1;
                            Toast.makeText(getApplicationContext(), pixels + "", Toast.LENGTH_SHORT).show();
                        }
                    } else if (remoteState.equalsIgnoreCase("decrease_speed")) {
                        if (isPlaying) {
                            if (pixels >= 2) {
                                pixels = pixels - 1;
                                Toast.makeText(getApplicationContext(), pixels + "", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        if (firebaseUser != null) {
            DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseUser.getUid())
                    .child("status").child(READ_ACTIVITY_REMOTE_STATE);
            databaseReference.addValueEventListener(postListener);
        }

    }

    public void setRemoteState(String state) {

        if (firebaseUser != null) {

            DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseUser.getUid())
                    .child("status").child(READ_ACTIVITY_REMOTE_STATE);
            databaseReference.setValue(state);
        }
    }
}


