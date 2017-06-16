package com.travel.tracker.forms;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.koushikdutta.async.future.Future;
import com.travel.tracker.common.GPSLocation;
import com.travel.tracker.database.Tables;
import com.travel.tracker.gloabls.Global;
import com.travel.tracker.R;
import com.travel.tracker.adapters.CustomAdapter;
import com.travel.tracker.database.SQLBase;
import com.travel.tracker.serverdata.upload;
import com.travel.tracker.utils.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class driver_info extends AppCompatActivity implements OnClickListener {
    Future<File> uploading;;
    /*Database variable*/
    SQLBase db;
    /* form variable */
    EditText edtSarthiName, edtMob1,edtMob2,edtAadhar,edtHowMany,edtVehNo,edtYrsOld,edtBatch,edtPrefLoc,edtRemarks, ddlVehicleType;
    Switch swhOwnership,swhInterested,swhShift;
    CheckBox chkOla,chkJugnoo,chkGauto,chkInsurance,chkPUC,chkLicense;
    Button btnTakePhoto;
    ImageView imgPicPrev;
    Spinner sprDdl;
    Bundle bundle;
    /*Variables*/
    String autoid ="";

    /*Constants*/
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri profilepic1Uri; // file url to store image/video
    private static final String IMAGE_DIRECTORY_NAME = "goyo_prof_images";

    private String[] vehicleTypes ={"Car","Rikshow","Bike"};
    private String profilepic1Url = "";
    private String profilepic1Url_upload = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_info);
        this.setTitle("Driver Info");
        initAllControls();
        setListners();
        ddlVehicleType.setOnClickListener(this);
        ddlitemclick();
        db = new SQLBase(getApplicationContext());
        fillEditform();
    }


    //set action bar button menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_driver_info_activity, menu);
        return true;
    }


    //action bar menu button click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_driver_info_save:
                if(validate()){
                    save();
                }
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    /*fill all controls*/
    private void initAllControls(){
        /*Edit text box*/
        edtSarthiName = (EditText)findViewById(R.id.edtSarthiName);
        edtMob1 = (EditText)findViewById(R.id.edtMob1);
        edtMob2 = (EditText)findViewById(R.id.edtMob2);
        edtAadhar = (EditText)findViewById(R.id.edtAadhar);
        edtHowMany = (EditText)findViewById(R.id.edtHowMany);
        edtVehNo = (EditText)findViewById(R.id.edtVehNo);
        edtYrsOld = (EditText)findViewById(R.id.edtYrsOld);
        edtBatch = (EditText)findViewById(R.id.edtBatch);
        edtPrefLoc = (EditText)findViewById(R.id.edtPrefLoc);
        edtRemarks = (EditText)findViewById(R.id.edtRemarks);

        ddlVehicleType = (EditText)findViewById(R.id.ddlVehicleType);
        ddlVehicleType.setCursorVisible(false);
        ddlVehicleType.setFocusable(false);
        ddlVehicleType.setShowSoftInputOnFocus(false);


        /*Switch*/
        swhOwnership = (Switch)findViewById(R.id.swhOwnership);
        swhInterested = (Switch)findViewById(R.id.swhInterested);
        swhShift = (Switch)findViewById(R.id.swhShift);
        /*Check Boxes*/
        chkOla = (CheckBox)findViewById(R.id.chkOla);
        chkJugnoo = (CheckBox)findViewById(R.id.chkJugnoo);
        chkGauto = (CheckBox)findViewById(R.id.chkGauto);

        chkLicense = (CheckBox)findViewById(R.id.chkLicense);
        chkInsurance = (CheckBox)findViewById(R.id.chkInsurance);
        chkPUC = (CheckBox)findViewById(R.id.chkPUC);

        /*Spinner*/
        sprDdl = (Spinner)findViewById(R.id.spnrDdl);

        //buttons
        btnTakePhoto = (Button)findViewById(R.id.btnTakePhoto);

        //image view
        imgPicPrev = (ImageView)findViewById(R.id.imgPicPrev);

        /*Text View*/
        clearAll();

    }

    private void save(){
        try {
            GPSLocation l = new GPSLocation(this.getApplicationContext());
            Location loc = l.getLocation();
            String lat = "";
            String lon = "";
            if (loc != null) {
                lat = "" + loc.getLatitude();
                lon = "" + loc.getLongitude();
            }

            String drivingShift = swhShift.isChecked() ? "Day" : "Night";
            String alUsing = (chkGauto.isChecked() ? "GAuto|" : "") + (chkOla.isChecked() ? "OLA|" : "") + (chkJugnoo.isChecked() ? "Jugnoo|" : "");
            String doYouHv = (chkInsurance.isChecked() ? "Insurance|" : "") + (chkLicense.isChecked() ? "License|" : "") + (chkPUC.isChecked() ? "PUC|" : "");
            //profilepic1Url = profilepic1Uri != null ? profilepic1Uri.getPath() : profilepic1Url;
            String proIsNew = profilepic1Uri != null ? "false" : profilepic1Url.equals("") ? "false" : "true";

            if (autoid.equals("")) {

                db.DRIVER_INFO_INSERT(edtSarthiName.getText().toString(), edtMob1.getText().toString(), edtMob2.getText().toString(),
                        edtAadhar.getText().toString(),
                        swhOwnership.isChecked() + "", edtVehNo.getText().toString(), edtYrsOld.getText().toString(),
                        edtBatch.getText().toString(), edtHowMany.getText().toString(), drivingShift, alUsing, swhInterested.isChecked() + "",
                        doYouHv, edtPrefLoc.getText().toString(), common.dateandtime(this.getApplicationContext()) + "", "Test", "false",
                        ddlVehicleType.getText().toString(), edtRemarks.getText().toString(), lat, lon,
                        profilepic1Url ,"false","doc1","","doc2","","doc3","","doc4","");
                Toast.makeText(getApplicationContext(), "Saved Successfully", Toast.LENGTH_SHORT).show();
            } else {
                db.DRIVER_INFO_UPDATE(autoid, edtSarthiName.getText().toString(), edtMob1.getText().toString(), edtMob2.getText().toString(),
                        edtAadhar.getText().toString(),
                        swhOwnership.isChecked() + "", edtVehNo.getText().toString(), edtYrsOld.getText().toString(),
                        edtBatch.getText().toString(), edtHowMany.getText().toString(), drivingShift, alUsing, swhInterested.isChecked() + "",
                        doYouHv, edtPrefLoc.getText().toString(), common.dateandtime(this.getApplicationContext()) + "", "Test", "false",
                        ddlVehicleType.getText().toString(), edtRemarks.getText().toString(), lat, lon, profilepic1Url ,proIsNew,"doc1","","doc2","","doc3","","doc4","");
                Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
            }
            clearAll();
            upload o = new upload();
            o.checkServerDataDriverInfo(getApplicationContext());
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error while saving data " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean validate(){
        if(edtSarthiName.getText().toString().equals("")) {
            edtSarthiName.setError("Require!");
            edtSarthiName.findFocus();
            edtSarthiName.requestFocus(View.FOCUS_UP);
            return false;

        }
        if(edtMob1.getText().toString().equals(""))
        {
            edtMob1.setError("Require!");
            edtMob1.findFocus();
            edtMob1.requestFocus(View.FOCUS_UP);
            return false;
        }

        return  true;
    }

    private  void setListners(){
        swhOwnership.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtHowMany.setVisibility(View.VISIBLE);
                    edtHowMany.setText("");
                } else {
                    edtHowMany.setVisibility(View.GONE);
                    edtHowMany.setText("");
                }
            }
        });
        btnTakePhoto.setOnClickListener(this);

    }
    private void fillVehicleTypes(){
        sprDdl.setAdapter(new CustomAdapter(driver_info.this, R.layout.layout_ddl, vehicleTypes));
        sprDdl.performClick();
    }

    private void ddlitemclick(){
        sprDdl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ddlVehicleType.setText(vehicleTypes[position].toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fillEditform(){
        bundle = getIntent().getExtras();
        if(bundle==null){return;}
        if(!bundle.containsKey("autoid")){   return;    }
        String Id = bundle.get("autoid").toString();
        autoid = Id;
        List<HashMap<String, String>> data = db.DRIVER_INFO_GET("where autoid = " + Id);
        if(data.size() == 1){
            HashMap<String, String> _d = data.get(0);
            edtSarthiName.setText(_d.get(Tables.tbl_driver_info.sarthinm));
            edtMob1.setText(_d.get(Tables.tbl_driver_info.mob1));
            edtMob2.setText(_d.get(Tables.tbl_driver_info.mob2));
            edtAadhar.setText(_d.get(Tables.tbl_driver_info.adharno));
            edtHowMany.setText(_d.get(Tables.tbl_driver_info.howmny));
            edtVehNo.setText(_d.get(Tables.tbl_driver_info.vehno));
            edtYrsOld.setText(_d.get(Tables.tbl_driver_info.yrsold));
            edtBatch.setText(_d.get(Tables.tbl_driver_info.btchno));
            edtPrefLoc.setText(_d.get(Tables.tbl_driver_info.prefloc));
            edtRemarks.setText(_d.get(Tables.tbl_driver_info.remarks));
            ddlVehicleType.setText(_d.get(Tables.tbl_driver_info.vehtype));

             /*Switch*/
            swhOwnership.setChecked(Boolean.parseBoolean(_d.get(Tables.tbl_driver_info.ownrship).toString().equals("true") ? "true" : "false"));
            swhInterested.setChecked(Boolean.parseBoolean(_d.get(Tables.tbl_driver_info.goyointr).toString().equals("true") ? "true" : "false"));
            swhShift.setChecked(Boolean.parseBoolean(_d.get(Tables.tbl_driver_info.driving).toString().equalsIgnoreCase("day") ? "true" : "false"));
        /*Check Boxes*/
            chkOla.setChecked(Boolean.parseBoolean(_d.get(Tables.tbl_driver_info.alruseing).toString().contains("OLA") ? "true" : "false"));
            chkJugnoo.setChecked(Boolean.parseBoolean(_d.get(Tables.tbl_driver_info.alruseing).toString().contains("Jugnoo") ? "true" : "false"));
            chkGauto.setChecked(Boolean.parseBoolean(_d.get(Tables.tbl_driver_info.alruseing).toString().contains("GAuto") ? "true" : "false"));

            chkLicense.setChecked(Boolean.parseBoolean(_d.get(Tables.tbl_driver_info.doyohv).toString().contains("License") ? "true" : "false"));
            chkInsurance.setChecked(Boolean.parseBoolean(_d.get(Tables.tbl_driver_info.doyohv).toString().contains("Insurance") ? "true" : "false"));
            chkPUC.setChecked(Boolean.parseBoolean(_d.get(Tables.tbl_driver_info.doyohv).toString().contains("PUC") ? "true" : "false"));

            profilepic1Url = _d.get(Tables.tbl_driver_info.profpic);
            profilepic1Url_upload = _d.get(Tables.tbl_driver_info.profpic_upload);


            if(!profilepic1Url.equals("")){
                final Bitmap bitmap = BitmapFactory.decodeFile(Global.ExternalPath.getAbsolutePath() + Global.Image_Path + "/" + profilepic1Url);
                imgPicPrev.setImageBitmap(bitmap);
            }
        }
    }

    private void clearAll(){
        autoid = "";
        profilepic1Url_upload= "";
        profilepic1Url ="";

        edtSarthiName.setText("");
        edtMob1.setText("");
        edtMob2.setText("");
        edtAadhar.setText("");
        edtHowMany.setText("");
        edtVehNo.setText("");
        edtYrsOld.setText("");
        edtBatch.setText("");
        edtPrefLoc.setText("");
        edtRemarks.setText("");

        ddlVehicleType.setText("");
        /*Switch*/
        swhOwnership.setChecked(true);
        swhInterested.setChecked(true);
        swhShift.setChecked(true);
        /*Check Boxes*/
        chkOla.setChecked(false);
        chkJugnoo.setChecked(false);
        chkGauto.setChecked(false);

        chkLicense.setChecked(false);
        chkInsurance.setChecked(false);
        chkPUC.setChecked(false);
        imgPicPrev.setImageBitmap(null);
    }


    @Override
    protected  void onDestroy(){
        db.close();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.ddlVehicleType:
                fillVehicleTypes();
                break;
            case R.id.btnTakePhoto:
                this.captureImage();
                break;

        }
    }


    //camera image capture

    /*
* Capturing Camera Image will lauch camera app requrest image capture
*/
    public void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        profilepic1Uri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, profilepic1Uri);
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
              /*  Toast.makeText(this.thisContext,
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();*/
            } else {
                // failed to capture image
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
            final int THUMBNAIL_SIZE = 500;
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();
            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;
            final Bitmap bitmap = BitmapFactory.decodeFile(profilepic1Uri.getPath(),options);
            final Bitmap ThumbImage = scaleBitmap(bitmap,THUMBNAIL_SIZE);

            imgPicPrev.setImageBitmap(ThumbImage);

            profilepic1Url = profilepic1Url.equals("") ? common.deviceId(getApplicationContext()) + System.currentTimeMillis() + ".jpg" : profilepic1Url;
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(!profilepic1Url.equals("") ?  profilepic1Uri.getPath() : profilepic1Url);
                ThumbImage.compress(Bitmap.CompressFormat.JPEG, 90, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    moveFile(profilepic1Uri.getPath(), Global.ExternalPath.getAbsolutePath() + Global.Image_Path, profilepic1Url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private Bitmap scaleBitmap(Bitmap bm, Integer maxHeightWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        Log.v("Pictures", "Width and height are " + width + "--" + height);

        if (width > height) {
            // landscape
            float ratio = (float) width / maxHeightWidth;
            width = maxHeightWidth;
            height = (int)(height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / maxHeightWidth;
            height = maxHeightWidth;
            width = (int)(width / ratio);
        } else {
            // square
            height = maxHeightWidth;
            width = maxHeightWidth;
        }

        Log.v("Pictures", "after scaling Width and height are " + width + "--" + height);

        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
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
        } else {
            return null;
        }

        return mediaFile;
    }

    private void moveFile(String inputFile, String outputDir, String outputFile) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputDir);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputFile);
            out = new FileOutputStream(outputDir + "/" +outputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(inputFile).delete();


        }

        catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }

}
