package com.usamabennadeemspeechix.usama.speechix.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.usamabennadeemspeechix.usama.speechix.constants.Constants.CLIENT_APP_STATE;

public class Utils {


    public static void loggedInStatus(String status, Context context) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if (FirebaseAuth.getInstance().getCurrentUser().getEmail() != null) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference();
                //Todo write code to handle database exceptions
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").child(CLIENT_APP_STATE)
                        .setValue(status).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    public static void mirrorTextView(TextView textView) {



        textView.setScaleX(-1);
        textView.setScaleY(1);
        textView.setTranslationX(1);

    }

    public static void unMirrorTextView(TextView textView){
        textView.setScaleX(1);
        textView.setScaleY(1);
        textView.setTranslationX(0);
    }


}
