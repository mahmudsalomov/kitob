package com.ehsanfahad.soliyev;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;


public class MainActivity extends AppCompatActivity
{

    private TextView textView;
    private ImageView imageView;


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        textView = findViewById(R.id.textView);
//        imageView = findViewById(R.id.imageView);
//        showInfo();
//    }
private PDFView pdfView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pdfView=findViewById(R.id.pdfViewer);
//        pdfView.useBestQuality(true);
        pdfView.enableSwipe(true);
        pdfView.fitToWidth();
        pdfView.fromAsset("amaliyot.pdf").load();
    }

//    private void showInfo()
//    {
//        String info = "app name: " + BuildConfig.APP_NAME;
//        info += "\npackage name: " + getPackageName();
//        info += "\ntheme color: #" + Integer.toHexString(ContextCompat.getColor(this, R.color.colorPrimary));
//
//        textView.setText(info);
//        imageView.setImageResource(R.drawable.app_icon);
//    }

}
