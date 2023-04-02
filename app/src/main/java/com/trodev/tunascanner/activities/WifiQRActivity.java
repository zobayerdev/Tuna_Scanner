package com.trodev.tunascanner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.trodev.tunascanner.R;

import java.util.Arrays;

public class WifiQRActivity extends AppCompatActivity {

    //declear interstitial ad
    private InterstitialAd mInterstitialAd = null;
    private static final String TAG = "INTERSTITIAL_TAG";

    String[] items = {"WPA", "WPA2", "WPA3"};
    String[] item_two = {"Yes", "No"};
    AutoCompleteTextView ssid, encryption;
    ArrayAdapter<String> adapterItems;

    public final static int QRCodeWidth = 500;
    Bitmap bitmap;
    private Button download, Generate;

    private EditText networkname, password;

    private ImageView imageView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_qractivity);

        // set title in activity
        getSupportActionBar().setTitle("Wifi QR");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initital mobile ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                Log.d(TAG, "onInitializationComplete: " + initializationStatus);
            }
        });

        //get test ads on a physical devices
        MobileAds.setRequestConfiguration(new RequestConfiguration.Builder()
                .setTestDeviceIds(Arrays.asList("TEST_DEVICE_ID1", "TEST_DEVICE_ID_N")).build()
        );

        loadInterstitialAd();

        ssid = findViewById(R.id.ssid_text);
        encryption = findViewById(R.id.encryption);
        networkname = findViewById(R.id.networkEt);
        password = findViewById(R.id.passeordEt);

        download = findViewById(R.id.downloadBtn);
        download.setVisibility(View.INVISIBLE);
        imageView = findViewById(R.id.imageIV);
        Generate = findViewById(R.id.Generate);

        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, items);
        ssid.setAdapter(adapterItems);

        ssid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, item_two);
        encryption.setAdapter(adapterItems);

        encryption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //ads open on
                showInterstitialAd();


                if (ssid.getText().toString().length() + networkname.getText().toString().length() + password.getText().toString().length() + encryption.getText().toString().length() == 0) {
                    Toast.makeText(WifiQRActivity.this, "Make sure your given Text..!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        bitmap = textToImageEncode("ssid :  " + ssid.getText().toString().trim() + "\nNetwork Name :  " + networkname.getText().toString().trim() + "\nPassword:  " + password.getText().toString().trim()
                                + "\nEncryption:  " + encryption.getText().toString().trim());
                        // + "\n\n\nMake by Altai Platforms"
                        imageView.setImageBitmap(bitmap);
                        download.setVisibility(View.VISIBLE);
                        download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "wifi_Identity", null);
                                Toast.makeText(WifiQRActivity.this, "Download Complete", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private Bitmap textToImageEncode(String value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(value, BarcodeFormat.DATA_MATRIX.QR_CODE, QRCodeWidth, QRCodeWidth, null);

        } catch (IllegalArgumentException e) {
            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];
        for (int y = 0; y < bitMatrixHeight; y++) {
            int offSet = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offSet + x] = bitMatrix.get(x, y) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }


    private void loadInterstitialAd() {
        // ad request to load interstitial ad
        AdRequest adRequest = new AdRequest.Builder().build();
        // change ads id on adUnit_id
        InterstitialAd.load(this, getResources().getString(R.string.interstitial_ad_live), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                // called when ads is failed
                Log.d(TAG, "onAdFailedToLoad: ");
                mInterstitialAd = null;
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                //called when ads loaded
                Log.d(TAG, "onAdLoaded: ");
                mInterstitialAd = interstitialAd;
            }
        });

    }

    private void showInterstitialAd() {
        if (mInterstitialAd != null) {
            Log.d(TAG, "showInterstitialAd: Ad was loaded");
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    Log.d(TAG, "onAdDismissedFullScreenContent: ");
                    //don't forget to set the ad reference to null so you don;t show the same ad again
                    mInterstitialAd = null;
                    loadInterstitialAd();
                    Toast.makeText(WifiQRActivity.this, "Ad is closes, here by you can perform the task ", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    Log.d(TAG, "onAdFailedToShowFullScreenContent: ");
                    mInterstitialAd = null;

                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                    Log.d(TAG, "onAdShowedFullScreenContent: ");
                }
            });

            mInterstitialAd.show(this);
        } else {
            Log.d(TAG, "showInterstitialAd: Ad was not loaded.....");
            //you may also do your task here if ad is not loaded
            Toast.makeText(WifiQRActivity.this, "Ad was not loaded, here by you can also perform the task ", Toast.LENGTH_SHORT).show();

        }
    }
}