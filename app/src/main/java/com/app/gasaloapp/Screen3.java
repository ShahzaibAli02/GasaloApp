package com.app.gasaloapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

import com.app.gasaloapp.Interfaces.DataBaseResponse;
import com.app.gasaloapp.Interfaces.ImageListener;
import com.app.gasaloapp.Interfaces.ImageUploadListener;
import com.app.gasaloapp.Model.Entry;
import com.app.gasaloapp.Utils.DataBaseManager;
import com.app.gasaloapp.databinding.ActivityScreen3Binding;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Screen3 extends AppCompatActivity implements View.OnClickListener {


    public static final String EXTRA_ENTRY_KEY="ENTRY";
    ActivityScreen3Binding binding;
    Entry entry;
    Uri mUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityScreen3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        entry=(Entry) getIntent().getSerializableExtra(EXTRA_ENTRY_KEY);
        binding.btnSelectImage.setOnClickListener(this);
        entry.date=new SimpleDateFormat("dd-MM-yyyy hh:mm:aa", Locale.ENGLISH).format(new Date());
        binding.btnCurrentDate.setText(entry.date);
        binding.btnSubMit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view==binding.btnSelectImage)
        {
            ImageHelper.readImageFromGallery(this, new ImageListener() {
                @Override
                public void onImageLoaded(boolean error, Uri uri, Bitmap bitmap)
                {
                    mUri=uri;
                    binding.imageView.setVisibility(error?View.GONE:View.VISIBLE);
                    if(error)
                    {
                        Util.showCustomToast(Screen3.this,"Image Not Selected",true);
                    }
                    else
                    {
                        binding.imageView.setImageURI(uri);
                    }

                }
            });
        }

        if(view==binding.btnSubMit)
        {

            if(mUri==null)
            {
                Util.showCustomToast(Screen3.this,"Select Image",true);
            }
            else
            {
                entry.note= binding.etNote.getText().toString();
                entry.uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                saveToDB();
            }
        }

    }

    private void saveToDB()
    {

        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Uploading Image..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ImageHelper.uploadImage(mUri, new ImageUploadListener() {
            @Override
            public void onUpload(boolean error, String Message, String url) {

                progressDialog.dismiss();
                if(error)
                {
                    Util.showCustomToast(Screen3.this,Message,true);
                }
                else
                {
                    entry.image=url;
                    saveData();
                }
            }
        });

    }

    private void saveData() {
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Saving..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        DataBaseManager.updateTotalLitres(Screen2.tank);
        DataBaseManager.insertEntry(entry, new DataBaseResponse() {
            @Override
            public void onResponse(boolean isSuccess, String message, Object Data) {
                progressDialog.dismiss();
                Util.showCustomToast(Screen3.this, message,!isSuccess);
                if(isSuccess)
                {
                    finish();
                    startActivity(new Intent(Screen3.this,ReportActivity.class));
                }
            }
        });
    }
}