package com.trodev.tunascanner.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.nativead.NativeAd;
import com.trodev.tunascanner.R;
import com.trodev.tunascanner.activities.BarCodeGenerator;
import com.trodev.tunascanner.activities.ContactActivity;
import com.trodev.tunascanner.activities.MessageActivity;
import com.trodev.tunascanner.activities.NativeAdsActivity;
import com.trodev.tunascanner.activities.ProductQRActivity;
import com.trodev.tunascanner.activities.ScanGalleryActivity;
import com.trodev.tunascanner.activities.ScannerActivity;
import com.trodev.tunascanner.activities.URLActivity;
import com.trodev.tunascanner.activities.WifiQRActivity;


public class HomeFragment extends Fragment {
    private CardView contact, product_qr, weburl, message, barcode, scanqrbar, scangallery, wifi;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        contact = view.findViewById(R.id.contact);
        product_qr = view.findViewById(R.id.product_qr);
        weburl = view.findViewById(R.id.weburl);
        message = view.findViewById(R.id.message);
        barcode = view.findViewById(R.id.barcode);
        scanqrbar = view.findViewById(R.id.scanqrbar);
        scangallery = view.findViewById(R.id.scangallery);
        wifi = view.findViewById(R.id.wifi);

        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WifiQRActivity.class);
                startActivity(intent);
            }
        });

        product_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProductQRActivity.class);
                startActivity(intent);
            }
        });

        weburl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), URLActivity.class);
                startActivity(intent);
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NativeAdsActivity.class);
                startActivity(intent);
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ContactActivity.class);
                startActivity(intent);
            }
        });

        barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BarCodeGenerator.class);
                startActivity(intent);
            }
        });

        scanqrbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScannerActivity.class);
                startActivity(intent);
            }
        });

        scangallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScanGalleryActivity.class);
                startActivity(intent);
            }
        });


        return view;

    }
}