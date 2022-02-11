package com.example.translator;


import static com.google.android.gms.vision.L.TAG;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class HasilActivity extends Activity {

    EditText mResultEt;
    TextView mResultHasil, txt_dari, txt_ke;
    ImageView mPreviewIv, iv_swipe;

    Button btn_translate;

    String teks, dari, ke;
    Uri imageUri;

    String originalKata = "";
    SweetAlertDialog pDialog;

    Boolean cekNilaiKeluar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil);

        mPreviewIv= findViewById(R.id.imageIv);
        mResultEt= findViewById(R.id.resultEt);
        mResultHasil= findViewById(R.id.resultHasil);
        txt_dari = findViewById(R.id.txt_dari);
        txt_ke = findViewById(R.id.txt_ke);

        imageUri= MainActivity.resultUri;
        teks= MainActivity.teks;
        dari= MainActivity.dari;
        ke= MainActivity.ke;

        txt_dari.setText(dari);
        txt_ke.setText(ke);

        mPreviewIv.setImageURI(imageUri);

        mResultEt.setText(teks);

        ImageView reload = findViewById(R.id.refreshIcon);


        originalKata = mResultEt.getText().toString();

        translate();

        reload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                translate();
            }
        });

        iv_swipe = findViewById(R.id.iv_swipe);
        iv_swipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dari == "Indonesia"){
                    dari = "English";
                    ke = "Indonesia";
                    txt_dari.setText("English");
                    txt_ke.setText("Indonesia");
                }
                else if(dari == "English"){
                    dari = "Indonesia";
                    ke = "English";
                    txt_dari.setText("Indonesia");
                    txt_ke.setText("English");
                }
            }
        });

        btn_translate = findViewById(R.id.btn_translate);
        btn_translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translate();
            }
        });

    }

    private void sweetAlertDialogTranslate(){
        pDialog = new SweetAlertDialog(HasilActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.setTitle("Translate Loading......");
        pDialog.show();
    }

    @Override
    public void onBackPressed() {
        if(cekNilaiKeluar == true){
            super.onBackPressed();
            return;
        }
        this.cekNilaiKeluar = true;
        Toast.makeText(this, "Tekan Sekali Lagi Untuk Keluar", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cekNilaiKeluar = false;
            }
        }, 2000);
    }

    private void translate(){
        String hasil = mResultEt.getText().toString();
//        TranslateAPI translateAPI = new TranslateAPI(
//                Language.ENGLISH,   //Source Language
//                Language.INDONESIAN,         //Target Language
//                hasil);           //Query Text

        TranslateAPI translateAPI = null;
        if(dari == "English"){
            translateAPI = new TranslateAPI(
                    Language.ENGLISH,   //Source Language
                    Language.INDONESIAN,         //Target Language
                    hasil);           //Query Text
        }
        else if(dari == "Indonesia"){
            translateAPI = new TranslateAPI(
                    Language.INDONESIAN,   //Source Language
                    Language.ENGLISH,         //Target Language
                    hasil);           //Query Text
        }

        sweetAlertDialogTranslate();

        translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
            @Override
            public void onSuccess(String translatedText) {
                Log.d(TAG, "onSuccess: "+translatedText);
                mResultHasil.setText(translatedText);
                pDialog.dismissWithAnimation();
            }

            @Override
            public void onFailure(String ErrorText) {
                mResultHasil.setText(ErrorText);
                pDialog.dismissWithAnimation();
                Log.d(TAG, "onFailure: "+ErrorText);
            }
        });
    }
}
