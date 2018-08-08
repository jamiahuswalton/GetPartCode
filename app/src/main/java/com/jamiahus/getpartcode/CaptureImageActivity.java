package com.jamiahus.getpartcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class CaptureImageActivity extends Activity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView displayImage;

    Button takePhoto;
    Button scanPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_image);

        displayImage = findViewById(R.id.imageView_Captured_Image);

        takePhoto = findViewById(R.id.button_takePhoto);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get an image
                getPhotoWithQRCode();
            }
        });

        scanPhoto = findViewById(R.id.button_scanPhoto);
        scanPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Send to Activity to scan photo
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
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            displayImage.setImageBitmap(imageBitmap);
        }
    }
}
