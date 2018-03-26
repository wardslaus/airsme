package com.airsme.appconfig;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

/**
 * Created by M91p-04 on 1/24/2018.
 */

public class GlobalStorage {
    final Context context;
    FirebaseStorage storage;
    StorageReference storageReference;

    public GlobalStorage(Context context) {
        this.context = context;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    public void uploadImage(Uri filePath, String node) {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child(node);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

public void loadImage(final String url, final ImageView imageView) {
    if(url==null||url=="")return;
    StorageReference load = FirebaseStorage.getInstance().getReference()
            .child(url);
    load.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
        @Override
        public void onSuccess(Uri uri) {
            // Got the download URL for 'users/me/profile.png'
            // Pass it to Picasso to download, show in ImageView and caching
            Picasso.with(context).load(uri.toString()).into(imageView);
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception exception) {
            // Handle any errors
        }
    });
}
}
