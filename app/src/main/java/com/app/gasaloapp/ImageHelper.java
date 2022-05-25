package com.app.gasaloapp;

import android.app.Activity;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.app.gasaloapp.Interfaces.ImageListener;
import com.app.gasaloapp.Interfaces.ImageUploadListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class ImageHelper
{

    public  static  void  uploadImage (Uri uri, ImageUploadListener imageUploadListener)
    {

        if(uri==null)
        {
            imageUploadListener.onUpload(true,"No Image Provided","");
            return;
        }


        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String ImagePath=String.format("images/%s.jpg" , String.valueOf(Calendar.getInstance().getTimeInMillis()));
        StorageReference imageRef = storageRef.child(ImagePath);

        imageRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onComplete (@NonNull @NotNull Task<UploadTask.TaskSnapshot> task)
            {

                if(task.isSuccessful())
                {

                    imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>()
                    {
                        @Override
                        public void onComplete (@NonNull @NotNull Task<Uri> task)
                        {

                            if(task.isSuccessful())
                            {
                                imageUploadListener.onUpload(false,ImagePath,task.getResult().toString());

                            }
                            else
                            {
                                imageUploadListener.onUpload(true,task.getException().getMessage(),"");
                            }


                        }
                    });

                }
                else
                {
                    imageUploadListener.onUpload(true,task.getException().getMessage(),"");
                }

            }
        });
    }

    public  static  void readImageFromGallery (Activity activity, ImageListener imageListener)
    {

        PickImageDialog.build(new IPickResult()
        {
            @Override
            public void onPickResult (PickResult r) {


                if(r.getError()==null)
                {
                    imageListener.onImageLoaded(false,r.getUri(),r.getBitmap());
                }else
                {
                    imageListener.onImageLoaded(true,null,null);
                }

            }
        }).show((FragmentActivity) activity);
    }
}