package com.jamiahus.getpartcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CaptureImageActivity extends Activity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView displayImage;
    private Bitmap imageBitmap;

    public static final String UPLOAD_IMAGE_EXTRA_NAME = "UploadImage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_image);

        displayImage = findViewById(R.id.imageView_Captured_Image);

        Button takePhoto = findViewById(R.id.button_takePhoto);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get an image
                getPhotoWithQRCode();
            }
        });

        Button scanPhoto = findViewById(R.id.button_scanPhoto);
        scanPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Null", "onClick: " + isImageNull(imageBitmap));
                //Make sure the image is not null
                if (!isImageNull(imageBitmap)){
                    //Send to Activity to scan photo
                    Intent startQRCodeActivity = new Intent(getApplicationContext(),MainActivity.class);
                    startQRCodeActivity.putExtra(UPLOAD_IMAGE_EXTRA_NAME, imageBitmap);

                    //Start activity
                    startActivity(startQRCodeActivity);
                } else {
                    String message = "Image did not load correctly, please try taking another photo.";
                    Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void getPhotoWithQRCode() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            assert extras != null;
            imageBitmap = (Bitmap) extras.get("data");
            if (isImageNull(imageBitmap)){
                String message = "Image did not load correctly, please try taking another photo.";
                Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();
            }
            displayImage.setImageBitmap(imageBitmap);
        }
    }

    private boolean isImageNull(Bitmap imageToCheck){
        return imageToCheck == null;
    }
}
