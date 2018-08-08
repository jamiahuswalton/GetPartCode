package com.jamiahus.getpartcode;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

    ImageView myImage;
    TextView showResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myImage = findViewById(R.id.myImage);
        showResults = findViewById(R.id.show_results);

        FirebaseApp.initializeApp(this);

        Button myButton = findViewById(R.id.start_scann);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BarcodeStuff();
            }
        });


    }

    private void BarcodeStuff (){

        //Code Recieved from https://firebase.google.com/docs/ml-kit/android/read-barcodes?authuser=0

        Bitmap myBitmap = BitmapFactory.decodeResource(
                getApplicationContext().getResources(),
                R.drawable.longercode);
        myImage.setImageBitmap(myBitmap);

        //Log.d("Bitmap", "BarcodeStuff: " + (myBitmap == null));
        //myImage.setImageBitmap(myBitmap);

        FirebaseVisionImage myImage = FirebaseVisionImage.fromBitmap(myBitmap);

        FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance().getVisionBarcodeDetector();

        Task<List<FirebaseVisionBarcode>> result = detector.detectInImage(myImage).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
            @Override
            public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
                Toast.makeText(getApplicationContext(),"Task Completed",Toast.LENGTH_LONG).show();

                Log.d("Value", "Value Type: " + firebaseVisionBarcodes);
                if (firebaseVisionBarcodes.isEmpty()){
                    //This means a barcode or a QR code was not detected
                    String message = "Barcode not detected. Please try a different image.";
                    Toast.makeText(
                            getApplicationContext(),
                            message,
                            Toast.LENGTH_LONG).show();
                }
                for (FirebaseVisionBarcode barcode: firebaseVisionBarcodes) {
                    Rect bounds = barcode.getBoundingBox();
                    Point[] corners = barcode.getCornerPoints();

                    String rawValue = barcode.getRawValue();

                    int valueType = barcode.getValueType();
                    // See API reference for complete list of supported types

                    //Log.d("Value", "Value Type: " + valueType);
                    //Log.d("Value", "Text Value (Should be 7): " + FirebaseVisionBarcode.TYPE_TEXT);
                    switch (valueType) {
                        case FirebaseVisionBarcode.TYPE_TEXT:
                            String QrValue = barcode.getDisplayValue();
                            //Log.d("Value (Type Text)", "Text Value: " + QrValue);
                            showResults.setText("QR Value: " + QrValue);
                            break;
                        default:
                            Toast.makeText(getApplicationContext(),
                                    "Code Unknown or not supproted. Please try again",
                                    Toast.LENGTH_LONG);
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
