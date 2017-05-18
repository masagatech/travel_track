package com.masaga.goyorider.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.masaga.goyorider.Interface.CameraImageCapture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mTech on 09-Mar-2017.
 */
public class camera extends Activity {
    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private Uri fileUri; // file url to store image/video
    private static final String IMAGE_DIRECTORY_NAME = "goyo_prof_images";
    private Context thisContext;
    private  CameraImageCapture listner;

    public camera(Context c, CameraImageCapture listner){
        this.thisContext = c;
        this.listner = listner;

    }

    /*
 * Capturing Camera Image will lauch camera app requrest image capture
 */
    public void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                this.listner.onCancel("User cancelled image capture");
              /*  Toast.makeText(this.thisContext,
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();*/
            } else {
                // failed to capture image
                this.listner.onError("Sorry! Failed to capture image");
              /*  Toast.makeText(this.thisContext,
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();*/
            }
        }
    }

    /*
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {
        try {
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();
            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;
            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            this.listner.onCapture(bitmap);
            //imgPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            this.listner.onError(e.getMessage());
            e.printStackTrace();
        }
    }
        /**
         * Creating file uri to store image/video
         */
        public Uri getOutputMediaFileUri(int type) {
            return Uri.fromFile(getOutputMediaFile(type));
        }

        /*
     * returning image / video
     */
        private static File getOutputMediaFile(int type) {

            // External sdcard location
            File mediaStorageDir = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    IMAGE_DIRECTORY_NAME);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                            + IMAGE_DIRECTORY_NAME + " directory");
                    return null;
                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            File mediaFile;
            if (type == MEDIA_TYPE_IMAGE) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "IMG_" + timeStamp + ".jpg");
            } else if (type == MEDIA_TYPE_VIDEO) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "VID_" + timeStamp + ".mp4");
            } else {
                return null;
            }

            return mediaFile;
        }
}


