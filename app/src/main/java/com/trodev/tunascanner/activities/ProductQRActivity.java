package com.trodev.tunascanner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.trodev.tunascanner.R;

import java.util.Arrays;


public class ProductQRActivity extends AppCompatActivity {

    public final static int QRCodeWidth = 500;
    Bitmap bitmap;
    private Button download, Generate;
    private EditText makeET, expireET, productET, companyET;
    private ImageView imageView;

    private static final String TAG = "REWARDED_INTER_TAG";
    private RewardedInterstitialAd mRewardedInterstitialAd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_qractivity);

        // set title in activity
        getSupportActionBar().setTitle("Create Product QR");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // init ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                Log.d(TAG, "onInitializationComplete: " + initializationStatus.toString());

            }
        });

        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("TEST_DEVICE_ID!_HERE", "TEST_DEVICE_ID!_HERE")).build()

        );

        makeET = findViewById(R.id.makeDateET);
        expireET = findViewById(R.id.expiredateET);
        productET = findViewById(R.id.productCodeET);
        companyET = findViewById(R.id.companyET);

        download = findViewById(R.id.downloadBtn);
        download.setVisibility(View.INVISIBLE);
        imageView = findViewById(R.id.imageIV);
        Generate = findViewById(R.id.Generate);

        Generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workingProcess();
            }
        });

    }

    // ######################################################################
    // Ads Settings in android studio
    // load ad when activity starts and show on button click
    private void loadRewardedInterstitialAd() {
        RewardedInterstitialAd.load(this, getString(R.string.reward_interstitial_ad_test), new AdRequest.Builder().build(),
                new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedInterstitialAd rewardedInterstitialAd) {
                        super.onAdLoaded(rewardedInterstitialAd);
                        /*Called when as is loaded*/
                        Log.d(TAG, "onAdLoaded:  ");
                        mRewardedInterstitialAd = rewardedInterstitialAd;
                        showRewardedInterstitialAd();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        /*Called when ad failed to load*/
                        mRewardedInterstitialAd = null;
                    }
                }
        );
    }

    private void showRewardedInterstitialAd() {
        if (mRewardedInterstitialAd != null) {
            //ad was loaded
            Log.d(TAG, "showRewardedInterstitialAd: Ad was loaded");
            mRewardedInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                    Log.d(TAG, "onAdClicked: ");
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    Log.d(TAG, "onAdDismissedFullScreenContent: ");
                    mRewardedInterstitialAd = null;
                    loadRewardedInterstitialAd();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    Log.d(TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());
                    mRewardedInterstitialAd = null;
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

            mRewardedInterstitialAd.show(this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    Toast.makeText(ProductQRActivity.this, "Show Ads", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.d(TAG, "showRewardedInterstitialAd: Ads was not loaded....");
            Toast.makeText(this, "Ads was not loaded....", Toast.LENGTH_SHORT).show();
            loadRewardedInterstitialAd();
        }
    }


    // ################################################################################################################
    // create qr code on user
    private void workingProcess() {
        if (makeET.getText().toString().trim().length() + expireET.getText().toString().length()
                + productET.getText().toString().length() + companyET.getText().toString().length() == 0) {
            Toast.makeText(ProductQRActivity.this, "Make sure your given Text..!", Toast.LENGTH_SHORT).show();
        } else {
            try {
                bitmap = textToImageEncode("Manufacture Date:  " + makeET.getText().toString() + "\nExpire Date:  " + expireET.getText().toString().trim() +
                        "\nProduct Code:  " + productET.getText().toString().trim() + "\nCompany Name:  " + companyET.getText().toString().trim());   // + "\n\n\nMake by Altai Platforms"
                imageView.setImageBitmap(bitmap);
                download.setVisibility(View.VISIBLE);
                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Product_Identity"
                                , null);
                        Toast.makeText(ProductQRActivity.this, "Download Complete", Toast.LENGTH_SHORT).show();
                        showRewardedInterstitialAd();
                    }
                });
            } catch (WriterException e) {
                e.printStackTrace();
            }
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