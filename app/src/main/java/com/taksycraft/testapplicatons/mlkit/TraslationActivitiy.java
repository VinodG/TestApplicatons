package com.taksycraft.testapplicatons.mlkit;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.taksycraft.testapplicatons.R;

public class TraslationActivitiy extends AppCompatActivity {
    private static final String TAG = "TraslationActivitiy";

    private EditText etInput;
    private TextToSpeech t1;
    private Translator engToTeluguTranslator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traslation_activitiy);
        etInput = (EditText)findViewById(R.id.etInput);
        newMLkit();
    }

    private void newMLkit() {
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.TELUGU).build();
        engToTeluguTranslator  = Translation.getClient(options);
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        engToTeluguTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void v) {
                                // Model downloaded successfully. Okay to start translating.
                                // (Set a flag, unhide the translation UI, etc.)
                                Log.e(TAG, "onSuccess: "  );
                                toast("Success");
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Model couldn’t be downloaded or other internal error.
                                // ...
                                Log.e(TAG, "onFailure: "  );
                                toast(e.getMessage());
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
            engToTeluguTranslator.translate(str)
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
