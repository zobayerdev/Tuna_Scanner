package com.trodev.tunascanner;

import com.trodev.tunascanner.models.ModelPdf;

public interface RvListenerPdf {

    void onPdfClick(ModelPdf modelPdf, int position);

    void onPdfMoreClick(ModelPdf modelPdf, int position);
}
