package com.example.flo;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Region;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.LanguageCodes;
import com.microsoft.projectoxford.vision.contract.Line;
import com.microsoft.projectoxford.vision.contract.OCR;
import com.microsoft.projectoxford.vision.contract.Word;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private VisionServiceClient client;
    private Bitmap bitmap;
    private Uri mUriPhotoTaken;
    private File mFilePhotoTaken;
    private Uri imagUrl;
    DatabaseReference noteDatabase;
    public String result;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_record:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main, new recordFragment()).commit();
                    break;
                case R.id.navigation_memos:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main, new msgFragment()).commit();
                    break;
                case R.id.navigation_photo:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main, new photoFragment()).commit();
                    break;
                case R.id.navigation_settings:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main, new settingsFragment()).commit();
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main, new msgFragment()).commit();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (client==null){
            client = new VisionServiceRestClient(getString(R.string.subscription_key), getString(R.string.subscription_apiroot));
        }

        //Offline Capabilities
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        // Write a message to the database
        noteDatabase = FirebaseDatabase.getInstance().getReference("Notes");
    }

    public void clickPic(View view) {
        /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        }*/
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null) {
            // Save the photo taken to a temporary file.
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                mFilePhotoTaken = File.createTempFile(
                        "IMG_",  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );
                // Create the File where the photo should go
                // Continue only if the File was successfully created
                if (mFilePhotoTaken != null) {
                    mUriPhotoTaken = FileProvider.getUriForFile(this,
                            "com.example.flo.fileprovider",
                            mFilePhotoTaken);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriPhotoTaken);

                    // Finally start camera activity
                    startActivityForResult(intent, 1);
                }
            } catch (IOException e) {
                e.getMessage();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 1:
                if(resultCode == RESULT_OK){
                    imagUrl = Uri.fromFile(mFilePhotoTaken);
                    /*Bundle extras = imageReturnedIntent.getExtras();
                    bitmap = (Bitmap) extras.get("data");*/
                    bitmap = ImageHelper.loadSizeLimitedBitmapFromUri(
                            imagUrl, getContentResolver());
                    if (bitmap != null) {
                        doRecognize();
                    }
                }
                break;
            default:
                break;
        }
    }

    public void doRecognize() {
        //place_ocr_btn.setEnabled(false);
        //mgetOCR.setText("Analyzing...");

        try {
            new doRequest().execute();
        } catch (Exception e)
        {
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG);
            /*mEditPlaceName.setText("Error encountered. Exception is: " + e.toString());*/
        }
    }

    private String process() throws VisionServiceException, IOException {
        Gson gson = new Gson();

        // Put the image into an input stream for detection.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

        OCR ocr;
        ocr = this.client.recognizeText(inputStream, LanguageCodes.AutoDetect, true);

        String result = gson.toJson(ocr);
        Log.d("result", result);

        return result;
    }

    private class doRequest extends AsyncTask<String, String, String> {
        // Store error message
        private Exception e = null;

        public doRequest() {
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                return process();
            } catch (Exception e) {
                this.e = e;    // Store error
            }

            return null;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            // Display based on error existence

            if (e != null) {
                /*mEditPlaceName.setText("Error: " + e.getMessage());*/
                this.e = null;
            } else {
                Gson gson = new Gson();
                OCR r = gson.fromJson(data, OCR.class);

                result = "";
                for (com.microsoft.projectoxford.vision.contract.Region reg : r.regions) {
                    for (Line line : reg.lines) {
                        for (Word word : line.words) {
                            result += word.text + " ";
                        }
                        result += "\n";
                    }
                    result += "\n\n";
                }
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Result")
                        .setMessage(result)

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                addNote(result);
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("Discard", null)
                        //.setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            //place_ocr_btn.setEnabled(true);
        }
    }

    public void addNote(String result) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MMMM d, yyyy 'at' h:mm a");
        String spdate = format.format(calendar.getTime());
        String sptime = "waste";
        String spcontent = result;

        String id = noteDatabase.push().getKey();

        Note potHole = new Note(id,spdate,sptime,spcontent);

        noteDatabase.child(id).setValue(potHole);

        Toast.makeText(this,"Note Added",Toast.LENGTH_LONG).show();
    }

}
