package com.taksycraft.testapplicatons.camera_gallery

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.*
import android.media.ExifInterface
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.taksycraft.testapplicatons.R
import com.theartofdev.edmodo.cropper.CropImage
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class PickMediaActivity(var context: Activity, var listen: ( String,  Bitmap?, String?) -> Unit)  {
    var app_preference: SharedPreferences? = null
    var selectedImageUri: Uri? = null
    var currentImageUri: Uri? = null
    var mCurrentPhotoPath: String? = null
    init{
        imagePath = ""
    }

    fun ShowDialogOptionForMidiaPick() {
        val isSDPresent = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
        val item = 1
        if (item == 0) {
            if (isSDPresent) GetPhotoFromCamera() else Toast.makeText(context, "SD card is not available", Toast.LENGTH_SHORT).show()
        } else if (item == 1) {
            if (isSDPresent) GetPhotoFromGallery() else Toast.makeText(context, "SD card is not available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun GetPhotoFromCamera() {
        try {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // Ensure that there's a camera activity to handle the
            // intent
            if (takePictureIntent
                            .resolveActivity(context.packageManager) != null) {
                // Create the File where the photo should go
                try {
                    currentImageUri = createImageFile(context)
                    //Save in shared preference
                    val editor = context.getSharedPreferences("fileName",Activity.MODE_PRIVATE).edit()
                    editor.putString("currentImageUri", currentImageUri.toString())
                    editor.commit()
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
                // Continue only if the File was successfully
                // created
                if (currentImageUri != null) {
                    val activity = context as AppCompatActivity
                    takePictureIntent.putExtra(
                            MediaStore.EXTRA_OUTPUT,
                            currentImageUri)
                    activity.startActivityForResult(takePictureIntent,
                            SELECT_FILE_CAMERA)
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    protected fun GetPhotoFromGallery() {
        try {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            val activity = context as Activity
            activity.startActivityForResult(Intent.createChooser(intent, selectImageToUpload), SELECT_FILE_GALLERY)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(context: Context): Uri {
        var image: File? = null
        try {
            // Create an image file name
            val timeStamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
            var storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            if (storageDir.exists()) {
                image = File.createTempFile(timeStamp,  /* prefix */
                        ".jpg",  /* suffix */
                        storageDir /* directory */
                )
            } else {
                storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                if (storageDir.exists()) {
                    image = File.createTempFile(timeStamp,  /* prefix */
                            ".jpg",  /* suffix */
                            storageDir /* directory */
                    )
                }
            }
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = image!!.absolutePath
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

//        return Uri.fromFile(image);
        return FileProvider.getUriForFile(context, context.packageName + ".provider", image!!)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun getRealPathFromURI(context: Context, contentURI: Uri?): String {
        var filePath = ""
        var cursor: Cursor? = null
        try {
            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
            val column = arrayOf(MediaStore.Images.Media.DATA)
            if (isKitKat
                    && DocumentsContract.isDocumentUri(context,
                            contentURI)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(contentURI)) {
                    val docId = DocumentsContract
                            .getDocumentId(contentURI)
                    val split = docId.split(":".toRegex()).toTypedArray()
                    val type = split[0]
                    if ("primary".equals(type, ignoreCase = true)) {
                        filePath = Environment.getExternalStorageDirectory()
                                .toString() + "/" + split[1]
                    } else {
                        val file = Environment.getExternalStorageDirectory()
                        var fileExtSDPath = System
                                .getenv("SECONDARY_STORAGE")
                        if (null == fileExtSDPath
                                || fileExtSDPath.length == 0) {
                            fileExtSDPath = System
                                    .getenv("EXTERNAL_SDCARD_STORAGE")
                        }
                        filePath = file.toString() + "/" + split[1]
                    }
                } else if (isDownloadsDocument(contentURI)) {
                    val wholeID = DocumentsContract
                            .getDocumentId(contentURI)
                    val id = wholeID.split(":".toRegex()).toTypedArray()
                    val type = id[0]
                    val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            java.lang.Long.valueOf(wholeID))
                    cursor = context.contentResolver.query(contentUri, column,
                            null, null, null)
                    val columnIndex = cursor.getColumnIndex(column[0])
                    if (cursor.moveToFirst()) {
                        filePath = cursor.getString(columnIndex)
                    }
                    cursor.close()
                } else if (isMediaDocument(contentURI)) {
                    val docId = DocumentsContract
                            .getDocumentId(contentURI)
                    val split = docId.split(":".toRegex()).toTypedArray()
                    val type = split[0]
                    val id = docId.split(":".toRegex()).toTypedArray()[1]
                    val sel = MediaStore.Images.Media._ID + "=?"
                    var contentUri: Uri? = null
                    if ("image" == type) {
                        cursor = context.contentResolver.query(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                column, sel, arrayOf(id), null)
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        cursor = context.contentResolver.query(contentUri, column,
                                null, null, null)
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        cursor = context.contentResolver.query(contentUri, column,
                                null, null, null)
                    }
                    val columnIndex = cursor!!.getColumnIndex(column[0])
                    if (cursor.moveToFirst()) {
                        filePath = cursor.getString(columnIndex)
                    }
                    cursor.close()
                }
            } else {
                cursor = context.contentResolver.query(contentURI, column, null, null, null)
                if (cursor == null) { // Source is Dropbox or other similar local file path
                    filePath = contentURI!!.path
                } else {
                    val columnIndex = cursor.getColumnIndex(column[0])
                    if (cursor.moveToFirst()) {
                        filePath = cursor.getString(columnIndex)
                    }
                    cursor.close()
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return filePath
    }

    fun activityResult(activity: Activity, screenName: String, requestCode: Int, resultCode: Int, data: Intent, isCrop: Boolean) {
        try {
            Log.i(TAG, "result code$resultCode")
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == SELECT_FILE_GALLERY) {
                    try {
                        selectedImageUri = data.data
                        imagePath = getRealPathFromURI(activity, selectedImageUri)
                        if (!TextUtils.isEmpty(imagePath)) {
                            if (isCrop) {
                                performCrop(activity, screenName, selectedImageUri)
                            } else setIconToReferenceActivity(activity, screenName, null, imagePath)
                            //decodeFile(activity, screenName, imagePath);
                        } else {
                            showToast(activity, selectedImageNotExist)
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        showToast(activity, internal_error)
                    }
                } else if (requestCode == SELECT_FILE_CAMERA) {
                    try {
                        app_preference = activity.getSharedPreferences("fileName", Activity.MODE_PRIVATE)
                        app_preference?.let {
                            val currentUri = it.getString("currentImageUri", "")
                            currentImageUri = Uri.parse(currentUri)
                            galleryAddPic()
                            if (mCurrentPhotoPath == null) {
                                imagePath = currentImageUri?.getPath()
                            } else imagePath = mCurrentPhotoPath
                            if (!TextUtils.isEmpty(imagePath)) {
                                if (isCrop) {
                                    performCrop(activity, screenName, currentImageUri)
                                } else setIconToReferenceActivity(activity, screenName, null, imagePath)
                                // decodeFile(activity, screenName, imagePath);
                            } else {
                                showToast(activity, selectedImageNotExist)
                            }
                        }

                    } catch (e: Exception) {
                        showToast(activity, internal_error)
                    }
                } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    val filename: String
                    try {
                        val result = CropImage.getActivityResult(data)
                        if (resultCode == Activity.RESULT_OK) {
                            val resultUri = result.uri
                            try {
                                val thePic = MediaStore.Images.Media.getBitmap(activity.contentResolver, resultUri)
                                val date = Calendar.getInstance()
                                filename = if (screenName.equals(my_profile, ignoreCase = true)) {
                                    "profile_bayat.jpg"
                                } else {
                                    date.timeInMillis.toString() + ".jpg"
                                }
                                imagePath = Environment.getExternalStorageDirectory().toString() + "/" + activity.getString(R.string.app_name) + "/" + filename
                                val stream = ByteArrayOutputStream()
                                thePic.compress(Bitmap.CompressFormat.JPEG, 95, stream)
                                saveImageGallery(activity, thePic, filename, null)
                                val file = File(imagePath)
                                if (file.exists()) {
                                    val bitmap = BitmapFactory.decodeFile(imagePath)
                                    setIconToReferenceActivity(activity, screenName, bitmap, imagePath)
                                } else {
                                    decodeFile(activity, screenName, imagePath)
                                }
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                            val error = result.error
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun showToast(activity: Activity, selectedImageNotExist: String) {
        try {
            Toast.makeText(activity, selectedImageNotExist, Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun decodeFile(context: Context, screenName: String, filePath: String?) {
        var bitmap: Bitmap? = null
        var imgRatio = 0f
        try {
            bitmap = null
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            var bmp = BitmapFactory.decodeFile(filePath, options)
            var actualHeight = options.outHeight
            var actualWidth = options.outWidth
            val maxHeight = 816.0f
            val maxWidth = 612.0f
            if (actualHeight != 0) imgRatio = actualWidth / actualHeight.toFloat()
            if (actualHeight < 0) {
                performCrop(context as Activity, screenName, currentImageUri)
                return
            }
            val maxRatio = maxWidth / maxHeight
            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight
                    actualWidth = (imgRatio * actualWidth).toInt()
                    actualHeight = maxHeight.toInt()
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth
                    actualHeight = (imgRatio * actualHeight).toInt()
                    actualWidth = maxWidth.toInt()
                } else {
                    actualHeight = maxHeight.toInt()
                    actualWidth = maxWidth.toInt()
                }
            }
            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)
            options.inJustDecodeBounds = false
            options.inDither = false
            options.inPurgeable = true
            options.inInputShareable = true
            options.inTempStorage = ByteArray(16 * 1024)
            try {
                bmp = BitmapFactory.decodeFile(filePath, options)
            } catch (exception: OutOfMemoryError) {
                exception.printStackTrace()
            }
            try {
                bitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
            } catch (exception: OutOfMemoryError) {
                exception.printStackTrace()
            }
            val ratioX = actualWidth / options.outWidth.toFloat()
            val ratioY = actualHeight / options.outHeight.toFloat()
            val middleX = actualWidth / 2.0f
            val middleY = actualHeight / 2.0f
            val scaleMatrix = Matrix()
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
            val canvas = Canvas(bitmap)
            canvas.matrix = scaleMatrix
            canvas.drawBitmap(bmp, middleX - bmp.width / 2, middleY - bmp.height / 2, Paint(Paint.FILTER_BITMAP_FLAG))
            val exif: ExifInterface
            try {
                exif = ExifInterface(filePath)
                val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
                Log.i("EXIF", "Exif: $orientation")
                val matrix = Matrix()
                if (orientation == 6) {
                    matrix.postRotate(90f)
                    Log.i("EXIF", "Exif: $orientation")
                } else if (orientation == 3) {
                    matrix.postRotate(180f)
                    Log.i("EXIF", "Exif: $orientation")
                } else if (orientation == 8) {
                    matrix.postRotate(270f)
                    Log.i("EXIF", "Exif: $orientation")
                }
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap!!.width, bitmap.height, matrix, true)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            var out: FileOutputStream? = null
            try {
                out = FileOutputStream(filePath)
                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, out)

                //GraphicsUtil graphicUtil = new GraphicsUtil();
                //Bitmap cropedBitmap = graphicUtil.getCircleBitmap(bitmap, 14);
                setIconToReferenceActivity(context, screenName, bitmap, filePath)
            } catch (ex: FileNotFoundException) {
                ex.printStackTrace()
            }
        } catch (ex: OutOfMemoryError) {
            ex.printStackTrace()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        try {
            if (height > reqHeight || width > reqWidth) {
                val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
                val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
                inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
            }
            val totalPixels = width * height.toFloat()
            val totalReqPixelsCap = reqWidth * reqHeight * 2.toFloat()
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return inSampleSize
    }

    private fun performCrop(context: Activity, screenName: String, tempUri: Uri?) {
        try {
            CropImage.activity(tempUri)
                    .setAspectRatio(1, 1)
                    .start(context)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun galleryAddPic() {
        try {
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            mediaScanIntent.data = currentImageUri
            context.sendBroadcast(mediaScanIntent)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun checkPermission(context: Context, permissions: Array<String>, permKey: String): Boolean {
        var isPermissionGranted = false
        try {
            val checker = PermissionsChecker(context)
            if (IsNeverAskAgainPermission(context, permKey)) {
                try {
                    if (!permKey.equals(locationNeverAskAgain, ignoreCase = true)
                            && !permKey.equals(receivceSMSNeverAskAgain, ignoreCase = true)) {
                        val activity = context as Activity
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package", context.getPackageName(), null)
                        intent.data = uri
                        activity.startActivity(intent)
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            } else {
                if (checker.lacksPermissions(*permissions)) {
                    requestPermissions(context, permissions)
                } else {
                    isPermissionGranted = true
                    if (permKey.equals(cameraNeverAskAgain, ignoreCase = true)) ShowDialogOptionForMidiaPick()
                    //allPermissionsGranted();
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return isPermissionGranted
    }

    private fun requestPermissions(context: Context,   permissions: Array<String>) {
        try {
            val activity = context as Activity
            ActivityCompat.requestPermissions(activity, permissions, PERMISSION_REQUEST_CODE)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    fun activityPermissionsResult(context: Context, requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        try {
            if (requestCode == PERMISSION_REQUEST_CODE) {
                //allPermissionsGranted();
                try {
                    for (i in permissions.indices) {
                        val permission = permissions[i]
                        val grantResult = grantResults[i]
                        if (grantResult == PackageManager.PERMISSION_DENIED) {
                            val activity = context as Activity
                            DetectUserChoiceOnPermission(activity, permission)
                        }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            } else {
                showDeniedResponse(grantResults)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun showDeniedResponse(grantResults: IntArray) {
        try {
            if (grantResults.size > 1) {
                for (i in grantResults.indices) {
                    if (grantResults[i] != 0) {
                        toast("Permission is denied" + permissionFeatures.values()[i])
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun toast(str: String) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show()
    }


    private fun DetectUserChoiceOnPermission(activity: Activity, permission: String?) {
        try {
            var showRationale = false
            when (permission) {
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                    showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
                    if (!showRationale) {
                        // user also CHECKED "never ask again"
                        // you can either enable some fall back,
                        // again the permission and directing to
                        // the app setting
                        SetToSharePreference(activity, cameraNeverAskAgain, true)
                    }
                }
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION -> {
                    showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
                    if (!showRationale) {
                        // user also CHECKED "never ask again"
                        // you can either enable some fall back,
                        // again the permission and directing to
                        // the app setting
                        SetToSharePreference(activity, locationNeverAskAgain, true)
                    }
                }
                Manifest.permission.READ_PHONE_STATE -> {
                    showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
                    if (!showRationale) {
                        // user also CHECKED "never ask again"
                        // you can either enable some fall back,
                        // again the permission and directing to
                        // the app setting
                        SetToSharePreference(activity, locationNeverAskAgain, true)
                    }
                }
                else -> {
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun SetToSharePreference(activity: Activity, key: String?, isBoolean: Boolean) {
        try {
            val editor = activity.getSharedPreferences("Permission", Activity.MODE_PRIVATE).edit()
            editor.putBoolean(key, isBoolean)
            editor.commit()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun IsNeverAskAgainPermission(context: Context, key: String?): Boolean {
        var isTrue = false
        try {
            app_preference = context.getSharedPreferences("Permission", Activity.MODE_PRIVATE)
            app_preference?.let {
                isTrue = it.getBoolean(key, false)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return isTrue
    }


    private fun setIconToReferenceActivity(context: Context, screenName: String, bitmap: Bitmap?, filePath: String?) {
        try {
            toast(screenName)
            listen.invoke(screenName , bitmap,  filePath )
            //            if (!StringUtils.isBlank(screenName) && screenName.equalsIgnoreCase(context.getString(R.string.my_profile))) {
//                ProfileActivity profileActivity = (ProfileActivity) context;
//                profileActivity.setProfile(bitmap, filePath);
//            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun saveImageGallery(context: Context, bitmap: Bitmap, filename: String?, imgActualPath: String?): String {
        var file: File? = null
        var galleryDirectory: File
        val directory: File
        val root: String
        val byteArrayData: ByteArray
        val localBitMap: Bitmap
        val fos: FileOutputStream
        var isGallery = false
        try {
            root = Environment.getExternalStorageDirectory().toString()
            directory = File(root + "/" + context.getString(R.string.app_name))
            if (!directory.exists()) directory.mkdirs()
            filename?.let {
                if (it.endsWith(".jpg")
                        || it.endsWith(".png")
                        || it.endsWith(".jpeg")) {
                    file = File(directory, it)
                    isGallery = true
                }
            }
            try {
                file?.let {
                    if(it.exists()) it.delete()
                    fos = FileOutputStream(file, false)
                    if (isGallery) {
                        if (imgActualPath != null) {
                            localBitMap = BitmapFactory.decodeFile(imgActualPath)
                            val stream = ByteArrayOutputStream()
                            localBitMap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                            byteArrayData = stream.toByteArray()
                            fos.write(byteArrayData)
                        } else bitmap.compress(Bitmap.CompressFormat.JPEG, 95, fos)
                    }
                    fos.flush()
                    fos.close()
                    MediaScannerConnection
                            .scanFile(context, arrayOf(it.absolutePath), arrayOf("image/jpeg"), null)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            } catch (ex: OutOfMemoryError) {
                ex.printStackTrace()
            }
            refreshGallery(context, "$directory/$filename")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private fun refreshGallery(context: Context, url: String) {
        try {
            val file = File(url)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                val f = File("file://" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES))
                val contentUri = Uri.fromFile(file)
                mediaScanIntent.data = contentUri
                context.sendBroadcast(mediaScanIntent)
            } else {
                context.sendBroadcast(Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse(url)))
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    private enum class permissionFeatures {
        CAMERA
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 0
        var imagePath: String? = null
        private const val TAG = "PickMediaActivity"
        private const val locationNeverAskAgain = "locationNeverAskAgain"
        private const val selectImageToUpload = "selectImageToUpload"
        private const val selectedImageNotExist = "selectedImageNotExist"
        private const val gpsDisableEnableText = "gpsDisableEnableText"
        var cameraNeverAskAgain = "cameraNeverAskAgain"
        private const val internal_error = "internal_error"
        private const val receivceSMSNeverAskAgain = "receivceSMSNeverAskAgain"
        var my_profile = "my_profile"
        private const val SELECT_FILE_GALLERY = 1
        private const val SELECT_FILE_CAMERA = 2
        fun isExternalStorageDocument(uri: Uri?) = "com.android.externalstorage.documents" == uri?.getAuthority() ?: ""

        fun isDownloadsDocument(uri: Uri?) = "com.android.providers.downloads.documents" == uri?.getAuthority() ?: ""

        fun isMediaDocument(uri: Uri?) = "com.android.providers.media.documents" == uri?.getAuthority() ?: ""
    }
}