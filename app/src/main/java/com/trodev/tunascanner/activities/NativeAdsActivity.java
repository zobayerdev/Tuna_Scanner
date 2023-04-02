package com.trodev.tunascanner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.trodev.tunascanner.R;
import com.trodev.tunascanner.adapter.AdapterProducts;
import com.trodev.tunascanner.models.ModelProduct;

import java.util.ArrayList;
import java.util.Arrays;

public class NativeAdsActivity extends AppCompatActivity {

    //Tag for Debuging
    private static final String TAG = "NATIVE_ADS_TAG";

    private RecyclerView productRv;
    private long pressedTime;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_ads);
        //actionbar title
        setTitle("Natives ADs");

        productRv = findViewById(R.id.productRv);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                Log.e(TAG, "onInitializationComplete: " + initializationStatus);
            }
        });

        MobileAds.setRequestConfiguration(new RequestConfiguration.Builder()
                .setTestDeviceIds(Arrays.asList("ADD_TEST_DEVICE_ID_HERE", "ADD_TEST_DEVICE_ID_HERE")).build()
        );

        loadProducts();
    }

    private void loadProducts() {


        String[] titles = {
                "Android 1.0",
                "Android 1.1",
                "Android 1.5",
                "Android 1.6",
                "Android 2.0",
                "Android 2.0.1",
                "Android 2.1",
                "Android 2.2",
                "Android 2.3",
                "Android 2.3.3",
                "Android 3.0",
                "Android 3.1",
                "Android 3.2",
                "Android 4.0",
                "Android 4.0.3"
        };

        String[] description = {
                "Android Alpha - September 23, 2008",
                "Android Beta - February  9  2009",
                "Android Cupcake - April  27,  2009",
                "Android Donut - September 15, 2009",
                "Android Eclair - October 27, 2009",
                "Android Eclair - December 3, 2009",
                "Android Eclair - January 11, 2010",
                "Android Froyo - May 20,  2010",
                "Android Gingerbread - December  6, 2010",
                "Android Gingerbread - February 22, 2011",
                "Android Honeycomb - May 10, 2011",
                "Android Honeycomb - July 15, 2011",
                "Android Q - September 3, 2019",
                "Android R - September 8, 2020",
                "Android S - October 4, 2021"
        };

        //init arraylist
        ArrayList<ModelProduct> productArrayList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            // add data to model
            ModelProduct model = new ModelProduct(R.drawable.ic_android_black, titles[i], description[i], 3.5f);
            //add model to arraylist
            productArrayList.add(model);
        }

        //setup dapter
        AdapterProducts adapterProducts = new AdapterProducts(this, productArrayList);
        //set adapter
        productRv.setAdapter(adapterProducts);

    }

    // In this code, android lifecycle exit on 2 times back-pressed.
    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            Toast.makeText(this, "Press again to Message QR", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(NativeAdsActivity.this, MessageActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
}