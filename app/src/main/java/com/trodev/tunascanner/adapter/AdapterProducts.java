package com.trodev.tunascanner.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MediaContent;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.trodev.tunascanner.R;
import com.trodev.tunascanner.models.ModelProduct;

import java.util.ArrayList;

public class AdapterProducts extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //variable
    private Context context;
    private ArrayList<ModelProduct> productArrayList;

    private static final String TAG = "PRODUCT_TAG";

    //view type cpnstant
    private static final int VIEW_TYPE_CONTENT = 0;
    private static final int VIEW_TYPE_AD = 1;

    public AdapterProducts(Context context, ArrayList<ModelProduct> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate
        View view;
        if (viewType == VIEW_TYPE_CONTENT) {
            //Return View row_products.xml
            view = LayoutInflater.from(context).inflate(R.layout.row_products, parent, false);
            return new HolderProduct(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.row_native_ads, parent, false);
            return new HolderNativeAd(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Manage data of products and Native
        if (getItemViewType(position) == VIEW_TYPE_CONTENT) {
            ModelProduct model = productArrayList.get(position);

            String title = model.getTitle();
            String description = model.getDescription();
            float ratings = model.getRating();

            //instance of our HolderProduct to access UI Views of row-products.xml
            HolderProduct holderProduct = (HolderProduct) holder;

            holderProduct.titleTv.setText(title);
            holderProduct.descriptionTv.setText(description);
            holderProduct.ratingBar.setRating(ratings);

            //handle item click
            holderProduct.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //implement your on click functionality here
                    Toast.makeText(context, "" + title, Toast.LENGTH_SHORT).show();
                }
            });
        } else if (getItemViewType(position) == VIEW_TYPE_AD) {
            //view type is nativead, setup, show nativead

            //load ad'
            AdLoader adLoader = new AdLoader.Builder(context, context.getString(R.string.native_ad_test))
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                            Log.d(TAG, "onNativeAdLoaded: ");
                            //Ads is loaded now we can see it
                            //Natives Ads
                            HolderNativeAd holderNativeAd = (HolderNativeAd) holder;
                            displayNativeAd(holderNativeAd, nativeAd);
                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdClicked() {
                            super.onAdClicked();
                            Log.d(TAG, "onAdClicked: Ad Clicked");
                        }

                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            Log.d(TAG, "onAdClosed: Ad Closed");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            super.onAdFailedToLoad(loadAdError);
                            Log.d(TAG, "onAdFailedToLoad: Ads Failed due to " + loadAdError.getMessage());
                        }

                        @Override
                        public void onAdImpression() {
                            super.onAdImpression();
                            Log.d(TAG, "onAdImpression: ");
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
                    })
                    .withNativeAdOptions(new NativeAdOptions.Builder().build()).build();
            adLoader.loadAd(new AdRequest.Builder().build());

        }

    }

    private void displayNativeAd(HolderNativeAd holderNativeAd, NativeAd nativeAd) {

        /*-----------Get ads assets from the native objects-----------------*/
        String headline = nativeAd.getHeadline();
        String body = nativeAd.getBody();
        String callToAction = nativeAd.getCallToAction();
        NativeAd.Image icon = nativeAd.getIcon();
        String price = nativeAd.getPrice();
        String store = nativeAd.getStore();
        Double starRating = nativeAd.getStarRating();
        String advertiser = nativeAd.getAdvertiser();
        MediaContent mediaContent = nativeAd.getMediaContent();

        /*---------same assets aren't guaranteed to be in every Nativead, so we need to check before displaying them------------*/
        if (headline == null) {
            holderNativeAd.ads_headline.setVisibility(View.INVISIBLE);

        } else {
            //have headline, show view
            holderNativeAd.ads_headline.setVisibility(View.VISIBLE);
            holderNativeAd.ads_headline.setText(headline);
        }

        if (body == null) {
            holderNativeAd.ad_body.setVisibility(View.INVISIBLE);

        } else {
            //have headline, show view
            holderNativeAd.ad_body.setVisibility(View.VISIBLE);
            holderNativeAd.ad_body.setText(body);
        }

        if (icon == null) {
            holderNativeAd.ad_app_icon.setVisibility(View.INVISIBLE);

        } else {
            //have headline, show view
            holderNativeAd.ad_app_icon.setVisibility(View.VISIBLE);
            holderNativeAd.ad_app_icon.setImageDrawable(icon.getDrawable());
        }

        if (starRating == null) {
            holderNativeAd.ad_starts.setVisibility(View.INVISIBLE);

        } else {
            //have headline, show view
            holderNativeAd.ad_starts.setVisibility(View.VISIBLE);
            holderNativeAd.ad_starts.setRating(starRating.floatValue());
        }


        if (price == null) {
            holderNativeAd.ad_price.setVisibility(View.INVISIBLE);

        } else {
            //have headline, show view
            holderNativeAd.ad_price.setVisibility(View.VISIBLE);
            holderNativeAd.ad_price.setText(price);
        }

        if (store == null) {
            holderNativeAd.ad_store.setVisibility(View.INVISIBLE);

        } else {
            //have headline, show view
            holderNativeAd.ad_store.setVisibility(View.VISIBLE);
            holderNativeAd.ad_store.setText(store);
        }


        if (advertiser == null) {
            holderNativeAd.ad_advertiser.setVisibility(View.INVISIBLE);

        } else {
            //have headline, show view
            holderNativeAd.ad_advertiser.setVisibility(View.VISIBLE);
            holderNativeAd.ad_advertiser.setText(advertiser);
        }

        if (mediaContent == null) {
            holderNativeAd.media_view.setVisibility(View.INVISIBLE);
        } else {
            holderNativeAd.media_view.setVisibility(View.VISIBLE);
            holderNativeAd.media_view.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            holderNativeAd.media_view.setMediaContent(mediaContent);
        }

        if (callToAction == null) {
            holderNativeAd.ad_call_to_action.setVisibility(View.INVISIBLE);

        } else {
            //have headline, show view
            holderNativeAd.ad_call_to_action.setVisibility(View.VISIBLE);
            holderNativeAd.ad_call_to_action.setText(callToAction);
            holderNativeAd.navtiveAdView.setCallToActionView(holderNativeAd.ad_call_to_action);
        }

        // ad native ad to nativeAdView
        holderNativeAd.navtiveAdView.setNativeAd(nativeAd);


    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public int getItemViewType(int position) {
        if (position % 5 == 0) {
            //offer five items show ad view
            return VIEW_TYPE_AD;
        } else {
            // show content
            return VIEW_TYPE_CONTENT;
        }
    }

    class HolderProduct extends RecyclerView.ViewHolder {
        //UI Views
        private ImageView iconIv;
        private TextView titleTv, descriptionTv;
        private RatingBar ratingBar;


        public HolderProduct(@NonNull View itemView) {
            super(itemView);
            iconIv = itemView.findViewById(R.id.iconIv);
            titleTv = itemView.findViewById(R.id.titleTv);
            descriptionTv = itemView.findViewById(R.id.descriptionTv);
            ratingBar = itemView.findViewById(R.id.ratingBar);

        }
    }

    class HolderNativeAd extends RecyclerView.ViewHolder {
        private ImageView ad_app_icon;
        private TextView ads_headline, ad_advertiser, ad_price, ad_store, ad_body;
        private Button ad_call_to_action;
        private MediaView media_view;
        private RatingBar ad_starts;
        private NativeAdView navtiveAdView;


        public HolderNativeAd(@NonNull View itemView) {
            super(itemView);

            ad_app_icon = itemView.findViewById(R.id.ad_app_icon);
            ads_headline = itemView.findViewById(R.id.ads_headline);
            ad_advertiser = itemView.findViewById(R.id.ad_advertiser);
            ad_price = itemView.findViewById(R.id.ad_price);
            ad_store = itemView.findViewById(R.id.ad_store);
            ad_call_to_action = itemView.findViewById(R.id.ad_call_to_action);
            ad_body = itemView.findViewById(R.id.ad_body);
            media_view = itemView.findViewById(R.id.media_view);
            ad_starts = itemView.findViewById(R.id.ad_starts);
            navtiveAdView = itemView.findViewById(R.id.navtiveAdView);

        }
    }
}
