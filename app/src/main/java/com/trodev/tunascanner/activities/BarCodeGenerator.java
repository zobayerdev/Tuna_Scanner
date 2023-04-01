package com.trodev.tunascanner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.trodev.tunascanner.R;

import java.util.ArrayList;
import java.util.Arrays;

public class BarCodeGenerator extends AppCompatActivity {

    private EditText title, details;
    private Button generate, download;
    private ImageView imageView;
    Bitmap bitmap;

    // ads setting
    private static final String TAG = "BANNER_AD_TAG";

    //declear ad view
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_generator);

        // set title in activity
        getSupportActionBar().setTitle("Create Bar QR");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.titleET);
        details = findViewById(R.id.detailsET);
        imageView = findViewById(R.id.imageIV);

        generate = findViewById(R.id.Generate);
        download = findViewById(R.id.downloadBtn);
        // invisible download button ,  when user create a bar code then its show.
        download.setVisibility(View.INVISIBLE);

        //initial  banner ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                Log.d(TAG, "onInitializationComplete: ");
            }
        });

        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("","")).build()
        );

        //init banner id
        adView = findViewById(R.id.bannerAd);

        //ad request
        AdRequest adRequest = new AdRequest.Builder().build();
        // load ad
        adView.loadAd(adRequest);

        //setup ad listener
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
                Log.d(TAG, "onAdClicked: ");
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.d(TAG, "onAdClosed: ");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d(TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d(TAG, "onAdLoaded: ");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Log.d(TAG, "onAdOpened: ");
            }

            @Override
            public void onAdSwipeGestureClicked() {
                super.onAdSwipeGestureClicked();
            }
        });


        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // #####################################
                // Text to Bar Code all coded is here.................
                /*     MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(title.getText().toString() + details.getText().toString(), BarcodeFormat.CODE_128, imageView.getWidth(), imageView.getHeight());
                    Bitmap bitmap = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Bitmap.Config.RGB_565);
                    for (int i = 0; i < imageView.getWidth(); i++) {
                        for (int j = 0; j < imageView.getHeight(); j++) {
                            bitmap.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                        }
                    }
                } catch (WriterException e) {
                    throw new RuntimeException(e);
                }*/
                // ##################################

                if (title.getText().toString().length() + details.getText().toString().length() == 0) {
                    Toast.makeText(BarCodeGenerator.this, "Make sure your given Text..!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        bitmap = textToImageEncode("Title:  " + title.getText().toString() + "\n Details:  " + details.getText().toString().trim());
                        imageView.setImageBitmap(bitmap);
                        download.setVisibility(View.VISIBLE);
                        Bitmap finalBitmap = bitmap;
                        // download image to gallery
                        download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MediaStore.Images.Media.insertImage(getContentResolver(), finalBitmap, "Contact_Identity"
                                        , null);
                                Toast.makeText(BarCodeGenerator.this, "Download Complete", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (WriterException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    @Override
    protected void onPause() {
        if(adView != null)
        {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(adView != null)
        {
            adView.resume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if(adView != null)
        {
            adView.destroy();
        }
        super.onDestroy();
    }

    private Bitmap textToImageEncode(String value) throws WriterException {
        BitMatrix bitMatrix;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            bitMatrix = multiFormatWriter.encode(title.getText().toString() + details.getText().toString(), BarcodeFormat.CODE_128, imageView.getWidth(), imageView.getHeight()); //CODE_128

        } catch (IllegalArgumentException e) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Bitmap.Config.RGB_565); //RGB_565
        for (int i = 0; i < imageView.getWidth(); i++) {
            for (int j = 0; j < imageView.getHeight(); j++) {
                bitmap.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
            }
        }
        return bitmap;
    }
}