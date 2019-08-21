package com.taksycraft.testapplicatons.firebasedatabase;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.taksycraft.testapplicatons.R;

public class RegistationActivity extends AppCompatActivity {


    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private EditText etInput;
    private String TAG  = RegistationActivity.class.getClass().getSimpleName();
//    https://codelabs.developers.google.com/codelabs/firebase-android/#1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registation);
        etInput=(EditText)findViewById(R.id.etInput);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(DatabaseConstants.USER_PATH);
        setTitle("User Registration");
        etInput.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onClkRegister(null);
                    handled = true;
                }
                return handled;
            }
        });
    }

    public void onClkRegister(View view) {
        final String str = etInput.getText().toString();
        if(TextUtils.isEmpty(str))
            toast("User name should not be empty");
        else
        {
            String modifiedStrig = str.replaceAll("[^A-Za-z0-9]", "");
            if(str.length()!= modifiedStrig.length())
                toast("User Name should not have any specialized character ");
            else{
                FirebaseDatabase.getInstance().getReference(DatabaseConstants.USER_PATH).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() );
                        try {
                                myRef.child(str)
                                        .setValue(str).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        toast(e.getMessage()+"");
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        etInput.setText("");
                                        toast("Registration has done successfully");
                                        Intent intent = new Intent(RegistationActivity.this, UsersActivity.class);
                                        intent.putExtra(DatabaseConstants.CURRENT_USER_NAME,str);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() );
                    }
                });

            }
        }
    }

    private void toast(String s) {
        Toast.makeText(this,s+"",Toast.LENGTH_LONG).show();
    }
}
