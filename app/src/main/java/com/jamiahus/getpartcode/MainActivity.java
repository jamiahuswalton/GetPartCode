package com.jamiahus.getpartcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.graphics.Bitmap;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.util.List;

public class MainActivity extends Activity {

    private ImageView myImage;
    private Bitmap imageBitmap;
    private TextView showResults;

    Button buttonStartScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get the intent that created started this activity
        Intent intentThatStartedActivity = getIntent();
        //Get the bundle of extras
        Bundle intentBundle = intentThatStartedActivity.getExtras();
        assert intentBundle != null;
        //Get image data
        imageBitmap = (Bitmap) intentBundle.get(CaptureImageActivity.UPLOAD_IMAGE_EXTRA_NAME);
        if (imageBitmap == null) {
            String message = "Image was not received. Please retake image.";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        myImage = findViewById(R.id.myImage);
        //Set display as uploaded image
        myImage.setImageBitmap(imageBitmap);
        //Find show results text view
        showResults = findViewById(R.id.show_results);

        FirebaseApp.initializeApp(this);

        buttonStartScan = findViewById(R.id.start_scann);
        buttonStartScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BarcodeStuff();
            }
        });
    }

    private void BarcodeStuff (){

        //Structure of Code Recieved from https://firebase.google.com/docs/ml-kit/android/read-barcodes?authuser=0
        myImage.setImageBitmap(imageBitmap);

        FirebaseVisionImage myImage = FirebaseVisionImage.fromBitmap(imageBitmap);

        FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance().getVisionBarcodeDetector();

        Task<List<FirebaseVisionBarcode>> result = detector.detectInImage(myImage).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
            @Override
            public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
                if (firebaseVisionBarcodes.isEmpty()){
                    //This means a barcode or a QR code was not detected
                    String message = "Barcode not detected. Please try a different image.";
                    Toast.makeText(
                            getApplicationContext(),
                            message,
                            Toast.LENGTH_LONG).show();
                }
                for (FirebaseVisionBarcode barcode: firebaseVisionBarcodes) {

                    int valueType = barcode.getValueType();
                    // See API reference for complete list of supported types

                    switch (valueType) {
                        case FirebaseVisionBarcode.TYPE_TEXT:
                            String QrValue = barcode.getDisplayValue();
                            String displayText = "QR Value: " + QrValue;
                            showResults.setText(displayText);
                            break;
                        default:
                            String message = "Code Unknown or not supproted. Please try again";
                            Toast.makeText(getApplicationContext(),
                                    message,
                                    Toast.LENGTH_LONG).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Task NOT Completed",Toast.LENGTH_LONG).show();
            }
        });


    }
}
