package com.jamiahus.getpartcode;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class StartScreenActivity extends Activity {

    Button startQrCodeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        startQrCodeButton = findViewById(R.id.button_startQrScan);
        startQrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start the QR Code Activity
                Intent QrCodeIntent = new Intent(getApplicationContext(), CaptureImageActivity.class);
                startActivity(QrCodeIntent);
            }
        });
    }
}
