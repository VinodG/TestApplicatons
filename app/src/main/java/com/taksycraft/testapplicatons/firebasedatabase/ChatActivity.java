package com.taksycraft.testapplicatons.firebasedatabase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.taksycraft.testapplicatons.R;
import com.taksycraft.testapplicatons.common.CalendarUtils;
import com.taksycraft.testapplicatons.customviews.BackgroundDrawable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    String filePaht = "https://firebasestorage.googleapis.com/v0/b/testapplicatons.appspot.com/o/image.jpg?alt=media&token=6eb30df6-5ab1-45df-babc-f2cd52658157";
//String filePaht = "https://via.placeholder.com/600/92c952";

    private static final int FROM_COLOR = Color.RED;
    private static final int TO_COLOR = Color.BLUE;
    private static final int REQUEST_GALLERY = 100;
    private static final int REQUEST_TAKE_PHOTO = 101;
    private String TAG = ChatActivity.this.getClass().getSimpleName();
    private String selectedUserName;
    private String currentUserName;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseRecyclerOptions<ChatDO> options;
    private FirebaseRecyclerAdapter<ChatDO, ChatHolder> chatAdapter;
    private RecyclerView rvChat;
    private EditText etInput;
    private TextView tvCurrentUserName;
    private String chatKeyNode;
    private Bitmap bitmap;
    private String currentPhotoPath;
    private File capturedFile;
    private Button btnPickImage;
    private StorageReference storageRef;
    private String uploadedImagePath = "";
    private Translator englishTeluguTranslator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        rvChat = (RecyclerView) findViewById(R.id.rvChat);
        etInput = (EditText) findViewById(R.id.etInput);
        btnPickImage = (Button) findViewById(R.id.btnPickImage);
        btnPickImage.setVisibility(View.VISIBLE);
        initLoader();
        etInput.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onClkPush(null);
                    handled = true;
                }
                return handled;
            }
        });
        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureIntent();
            }
        });
        tvCurrentUserName = (TextView) findViewById(R.id.tvCurrentUserName);
        selectedUserName = getIntent().getStringExtra(DatabaseConstants.SELECTED_USER_NAME);
        setTitle("To : " + selectedUserName.toUpperCase() + "");
        currentUserName = getIntent().getStringExtra(DatabaseConstants.CURRENT_USER_NAME);
        tvCurrentUserName.setText(currentUserName);
        ArrayList<String> list = new ArrayList<>();
        list.add(selectedUserName);
        list.add(currentUserName);
        Collections.sort(list);
        chatKeyNode = list.get(0) + "_TO_" + list.get(1);
        TranslatorOptions optionsForLang =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.TELUGU)
//                        .setTargetLanguage(FirebaseTranslateLanguage.HI)
                        .build();
        englishTeluguTranslator =
                Translation.getClient(optionsForLang) ;
        download();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child(chatKeyNode);
        Query chatQuery = myRef;//.limitToLast(50);
        options = new FirebaseRecyclerOptions.Builder<ChatDO>()
                .setQuery(chatQuery, ChatDO.class)
                .build();
        rvChat.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        chatAdapter = new FirebaseRecyclerAdapter<ChatDO, ChatHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ChatHolder chatHolder, int i, @NonNull ChatDO chatDO) {
                chatHolder.tvMessage.setText(chatDO.msg);
                chatHolder.ivImage.setVisibility(View.GONE);
                chatHolder.pbLoader.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(chatDO.imageUrl)) {
                    chatHolder.pbLoader.setVisibility(View.VISIBLE);
                    chatHolder.ivImage.setVisibility(View.VISIBLE);
                    Glide.with(ChatActivity.this).load(chatDO.imageUrl).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            chatHolder.pbLoader.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(chatHolder.ivImage);
                }

                chatHolder.tvMessage.setText(chatDO.msg);
                chatHolder.tvFrom.setText(chatDO.userName);
                try {
                    chatHolder.tvTimeStamp.setText(CalendarUtils.getData((Long) chatDO.serverTimeStamp, CalendarUtils.DATE_MAIN_AM_PM));
                } catch (Exception e) {
                    chatHolder.tvTimeStamp.setText(chatDO.timeStamp);
                    e.printStackTrace();
                }

                if (chatDO.isMsgRead)
                    chatHolder.tvMessage.setTextColor(Color.BLACK);
                else
                    chatHolder.tvMessage.setTextColor(Color.GRAY);

                if (chatDO.friendName.equalsIgnoreCase(currentUserName)) {
//                    chatHolder.tvTimeStamp.setGravity(Gravity.LEFT);
//                    chatHolder.tvFrom.setGravity(Gravity.LEFT);
//                    chatHolder.tvMessage.setGravity(Gravity.LEFT);
                    chatHolder.llParent.setGravity(Gravity.LEFT);
                    chatHolder.llDrawable.setGravity(Gravity.LEFT);
                    chatHolder.llDrawable.setBackground(
                            new BackgroundDrawable()
                                    .setCornerRadius(12)
                                    .setBackgroundColor(Color.parseColor("#bdd0ef"))
                                    .getDrawable()
                    );
                    chatHolder.tvFrom.setTextColor(FROM_COLOR);
                    if (chatDO.isMsgRead == false) {
                        chatDO.isMsgRead = true;
                        try {
                            myRef.child(chatDO.key).setValue(chatDO);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    chatHolder.llParent.setGravity(Gravity.RIGHT);
                    chatHolder.llDrawable.setGravity(Gravity.RIGHT);
                    chatHolder.llDrawable.setBackground(
                            new BackgroundDrawable()
                                    .setCornerRadius(12)
                                    .setBackgroundColor(Color.parseColor("#edd9b8"))
                                    .getDrawable()
                    );
//                    chatHolder.tvTimeStamp.setGravity(Gravity.RIGHT);
//                    chatHolder.tvFrom.setGravity(Gravity.RIGHT);
//                    chatHolder.tvMessage.setGravity(Gravity.RIGHT);
                    chatHolder.tvFrom.setTextColor(TO_COLOR);
                }
            }

            @NonNull
            @Override
            public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_item, null);
//                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new ChatHolder(view);
            }
        };
        chatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = chatAdapter.getItemCount();
                rvChat.scrollToPosition(positionStart);
                ChatDO obj = chatAdapter.getItem(positionStart);
                onTranslate(obj.msg);
//                playBeep();

            }
        });
        rvChat.setAdapter(chatAdapter);
    }

    public void playBeep() {

        try {
            Uri notification = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
                    notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClkPush(View view) {
        String str = etInput.getText().toString();
        if (TextUtils.isEmpty(str))
            Toast.makeText(this, "String should not be empty ", Toast.LENGTH_SHORT).show();
        else {
            ChatDO chatDO = new ChatDO();
            chatDO.msg = str;
            chatDO.userName = currentUserName;
            chatDO.friendName = selectedUserName;
            chatDO.imageUrl = uploadedImagePath;
            chatDO.timeStamp = CalendarUtils.getData(
                    Calendar.getInstance().getTimeInMillis(), CalendarUtils.DATE_MAIN_AM_PM);
            chatDO.key = myRef.push().getKey();
            myRef.child(chatDO.key).setValue(chatDO).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e(TAG, "success");
                    uploadedImagePath = "";

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    uploadedImagePath = "";
                    Log.e(TAG, "failure");
                }
            });
            etInput.setText("");
        }
    }


    public void pushData(String imagePath) {

        ChatDO chatDO = new ChatDO();
        chatDO.msg = etInput.getText().toString();
        chatDO.userName = currentUserName;
        chatDO.friendName = selectedUserName;
        chatDO.imageUrl = imagePath;
        chatDO.timeStamp = CalendarUtils.getData(
                Calendar.getInstance().getTimeInMillis(), CalendarUtils.DATE_MAIN_AM_PM);
        chatDO.key = myRef.push().getKey();
        myRef.child(chatDO.key).setValue(chatDO).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e(TAG, "success");
                uploadedImagePath = "";
                etInput.setText("");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                uploadedImagePath = "";
                Log.e(TAG, "failure");
            }
        });
    }

    @Override
    public void onPause() {
//        chatAdapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        chatAdapter
                .startListening();
    }

    public void onClkPickImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + "-> " + storageDir.getAbsolutePath());

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void takePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            capturedFile = null;
            try {
                capturedFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (capturedFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        /*defined provider authories in menifesto file*/
                        "com.taksycraft.testapplicatons.fileproviderforcamera",
                        capturedFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        InputStream stream = null;
        if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            try {
                if (bitmap != null) {
                    bitmap.recycle();
                }
                stream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(stream);
//                ivPreview .setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (stream != null)
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            showLoader("Uploading Image....");
            try {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageRef = storageReference.child(CalendarUtils.getData(Calendar.getInstance().getTimeInMillis(),
                        CalendarUtils.DATE_MAIN) + ".jpg");
                if (bitmap != null) {
                    bitmap.recycle();
                }
                //rotate image
                ExifInterface ei = new ExifInterface(capturedFile.getPath());
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                bitmap = BitmapFactory.decodeFile(capturedFile.getPath());
                Log.e(TAG, "Before -> " + bitmap.getWidth() + " X " + bitmap.getHeight());
                int actualWidth = bitmap.getWidth();
                int actualHeight = bitmap.getHeight();
                int maxDim = Math.max(actualWidth, actualHeight);

                Bitmap rotatedBitmap = null;
                switch (orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = rotateImage(bitmap, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = rotateImage(bitmap, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotatedBitmap = rotateImage(bitmap, 270);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        rotatedBitmap = bitmap;
                }
                bitmap = Bitmap.createScaledBitmap(rotatedBitmap, (int) (400 * (rotatedBitmap.getWidth() / (float) maxDim)), (int) (400 * (rotatedBitmap.getHeight() / (float) maxDim))/*(400*actualHeight)/maxDim*/, false);
                File file = new File(capturedFile.getAbsolutePath());
                if (file.exists())
                    file.delete();

                FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] bitmapdata = bos.toByteArray();
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
                Log.e(TAG, "After -> " + bitmap.getWidth() + " X " + bitmap.getHeight());
                ByteArrayOutputStream bosStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bosStream);

                UploadTask uploadTask = storageRef.putBytes(bosStream.toByteArray());
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return storageRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        hideLoader();
                        if (task.isSuccessful()) {
                            toast("Image is uploaded successfully");
                            Uri downloadUri = task.getResult();
                            uploadedImagePath = downloadUri.toString();
                            Log.e(TAG, "onComplete -> " + downloadUri + "");
                            pushData(uploadedImagePath);
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideLoader();
                        toast(e.getMessage());
                    }
                });
                //below code do not give downloadabe url
//            uploadTask.addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    exception.printStackTrace();
//                    // Handle unsuccessful uploads
//                }
//            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Log.e(TAG, new Exception().getStackTrace()[0].getMethodName());
//                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                    // ...
//                }
//            });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (stream != null)
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    public Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private void toast(String message) {
        try {
            Toast.makeText(ChatActivity.this, message, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class ChatHolder extends RecyclerView.ViewHolder {
        public final TextView tvMessage;
        private final TextView tvTimeStamp;
        private final TextView tvFrom;
        private final LinearLayout llParent;
        private final LinearLayout llDrawable;
        private final ImageView ivImage;
        private final ProgressBar pbLoader;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            llParent = (LinearLayout) itemView.findViewById(R.id.llParent);
            llDrawable = (LinearLayout) itemView.findViewById(R.id.llDrawable);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            tvFrom = (TextView) itemView.findViewById(R.id.tvFrom);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            pbLoader = (ProgressBar) itemView.findViewById(R.id.pbLoader);
        }
    }

    private ProgressDialog progressDialog;

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void initLoader() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading.....");
    }

    private void showLoader(String str) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.setMessage(str + "");
            progressDialog.show();
        }
    }

    private void hideLoader() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    public void onTranslate(String str) {
        if (!TextUtils.isEmpty(str)) {
            englishTeluguTranslator.translate(str)
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
    private void download() {

        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        englishTeluguTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void v) {
                                toast("Model is downloaded Successfully");
                                // Model downloaded successfully. Okay to start translating.
                                // (Set a flag, unhide the translation UI, etc.)
                                englishTeluguTranslator.translate("What is your name")
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

}
