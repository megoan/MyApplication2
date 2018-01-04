package com.example.shmuel.myapplication.controller;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * Created by shmuel on 02/01/2018.
 */

public class FireBaseImages {

    public static String uploadImageAndGetUrl(Bitmap bitmap, String folder,String imageName)
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef;
        imageRef = storageRef.child(folder+"/"+imageName+".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                String url=downloadUrl.toString();
            }
        });
       return imageRef.getDownloadUrl().getResult().toString();

    }

}
