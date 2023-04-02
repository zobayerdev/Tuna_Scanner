package com.trodev.tunascanner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
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

public class ContactActivity extends AppCompatActivity {

    EditText name, phone, email, wpnum, fbid, twid, add, company, country;
    private ImageView imageView;
    public final static int QRCodeWidth = 500;
    Bitmap bitmap;
    private Button download, Generate;

    //declear interstitial ad
    private InterstitialAd mInterstitialAd = null;
    private static final String TAG = "INTERSTITIAL_TAG";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        //initital mobile ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                Log.d(TAG, "onInitializationComplete: " + initializationStatus);
            }
        });

        //get test ads on a physical devices
        MobileAds.setRequestConfiguration(new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("TEST_DEVICE_ID1", "TEST_DEVICE_ID_N")).build()
        );

        loadInterstitialAd();


        // set title in activity
        getSupportActionBar().setTitle("Create Contact QR");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // init all edittext id
        name = findViewById(R.id.nameET);
        email = findViewById(R.id.emailET);
        phone = findViewById(R.id.phoneET);
        wpnum = findViewById(R.id.wpET);
        fbid = findViewById(R.id.facebookET);
        twid = findViewById(R.id.twitterEt);
        add = findViewById(R.id.addressEt);
        company = findViewById(R.id.companyET);
        country = findViewById(R.id.countryET);

        // init all button
        download = findViewById(R.id.downloadBtn);
        download.setVisibility(View.INVISIBLE);
        imageView = findViewById(R.id.imageIV);
        Generate = findViewById(R.id.Generate);


        Generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                workProcess();
                showInterstitialAd();
            }
        });


    }

    private void workProcess() {

        if (name.getText().toString().trim().length() + email.getText().toString().length()
                + phone.getText().toString().length() + wpnum.getText().toString().length()
                + fbid.getText().toString().length() + twid.getText().toString().length()
                + add.getText().toString().length() + company.getText().toString().length()
                + country.getText().toString().length() == 0) {
            Toast.makeText(ContactActivity.this, "Make sure your given Text..!", Toast.LENGTH_SHORT).show();
        } else {
            try {
                bitmap = textToImageEncode("Name:  " + name.getText().toString() + "\nE-mail:  " + email.getText().toString().trim() +
                        "\nPhone:   " + phone.getText().toString().trim() + "\nWhatsapp:  " + wpnum.getText().toString().trim() + "\nFacebook I'd:  " + fbid.getText().toString().trim()
                        + "\nTwitter I'd:  " + twid.getText().toString().trim() + "\nAddress:  " + add.getText().toString().trim() + "\nCompany Name:  " + company.getText().toString().trim()
                        + "\nCountry:  " + country.getText().toString().trim());   // + "\n\n\nMake by Altai Platforms"
                imageView.setImageBitmap(bitmap);
                download.setVisibility(View.VISIBLE);
                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Contact_Identity"
                                , null);
                        Toast.makeText(ContactActivity.this, "Download Complete", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }


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
                    Toast.makeText(ContactActivity.this, "Ad is closes, here by you can perform the task ", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ContactActivity.this, "Ad was not loaded, here by you can also perform the task ", Toast.LENGTH_SHORT).show();

        }
    }

    private Bitmap textToImageEncode(String value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE, QRCodeWidth, QRCodeWidth, null);

        } catch (IllegalArgumentException e) {
            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];
        for (int y = 0; y < bitMatrixHeight; y++) {
            int offSet = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offSet + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}