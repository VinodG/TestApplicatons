package com.taksycraft.testapplicatons.mlkit;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.taksycraft.testapplicatons.R;

public class TraslationActivitiy extends AppCompatActivity {

    private EditText etInput;
    private FirebaseTranslator englishGermanTranslator;
    private TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traslation_activitiy);
        etInput = (EditText)findViewById(R.id.etInput);
        FirebaseTranslatorOptions options =
                new FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(FirebaseTranslateLanguage.EN)
                        .setTargetLanguage(FirebaseTranslateLanguage.TE)
//                        .setTargetLanguage(FirebaseTranslateLanguage.HI)
                        .build();
        englishGermanTranslator =
                FirebaseNaturalLanguage.getInstance().getTranslator(options);
        translate();
    }


    private void translate() {

        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        englishGermanTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void v) {
                                toast("Success");
                                // Model downloaded successfully. Okay to start translating.
                                // (Set a flag, unhide the translation UI, etc.)
                                englishGermanTranslator.translate("What is your name")
                                        .addOnSuccessListener(
                                                new OnSuccessListener<String>() {
                                                    @Override
                                                    public void onSuccess(@NonNull String translatedText) {
                                                        toast(translatedText + "");
                                                        etInput.setText("");
                                                    }
                                                })
                                        .addOnFailureListener(
                                                new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        toast(e.getMessage());
                                                    }
                                                });

                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                toast(e.getMessage());
                                // Model couldn’t be downloaded or other internal error.
                                // ...
                            }
                        });


    }
    private void toast(String message) {
        try {
            Toast.makeText(TraslationActivitiy.this, message, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClkTranslate(View view) {
        String str = etInput.getText().toString();
        if (!TextUtils.isEmpty(str)) {
            englishGermanTranslator.translate(str)
                    .addOnSuccessListener(
                            new OnSuccessListener<String>() {
                                @Override
                                public void onSuccess(@NonNull String translatedText) {
                                    // Translation successful.
                                    toast(translatedText + "");
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Error.
                                    // ...
                                    toast(e.getMessage());
                                }
                            });
        } else {
            toast("Input should not be empty");
        }

    }
}
