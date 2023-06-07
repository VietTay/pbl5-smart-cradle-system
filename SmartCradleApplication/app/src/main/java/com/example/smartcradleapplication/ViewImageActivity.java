package com.example.smartcradleapplication;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

//public class ViewImageActivity extends AppCompatActivity {
//    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//    private DatabaseReference databaseReference = firebaseDatabase.getReference();
//    private DatabaseReference first = databaseReference.child("images");
//    ImageView imageView;
//    private StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images"); // đường dẫn thư mục lưu trữ
//
//    @Override
//    protected void onCreate(Bundle saveInstanceState){
//        super.onCreate(saveInstanceState);
//        setContentView(R.layout.activity_viewimage);
//        imageView = findViewById(R.id.imageView);
//
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
//            @Override
//            public void onSuccess(ListResult listResult) {
//                final StorageReference[] newestImageRef = {null};
//                final long[] newestImageTime = {Long.MIN_VALUE};
//                for (StorageReference item : listResult.getItems()) {
//                    item.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
//                        @Override
//                        public void onSuccess(StorageMetadata storageMetadata) {
//                            long timeCreated = storageMetadata.getCreationTimeMillis();
//                            if (timeCreated > newestImageTime[0]) {
//                                newestImageTime[0] = timeCreated;
//                                newestImageRef[0] = item;
//                            }
//                            if (newestImageRef[0] != null) {
//                                newestImageRef[0].getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                    @Override
//                                    public void onSuccess(Uri uri) {
//                                        Picasso.get().load(uri).into(imageView);
//                                    }
//                                });
//                            }
//                        }
//                    });
//                }
//            }
//        });
//
//        first.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                HashMap<String, String> data = (HashMap<String, String>) dataSnapshot.getValue();
//                if (data != null) {
//                    List<String> keys = new ArrayList<>(data.keySet());
//                    Collections.sort(keys, Collections.reverseOrder());
//                    String newestKey = keys.get(0);
//                    String link = data.get(newestKey);
//                    Picasso.get().load(link).into(imageView);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }
//}
public class ViewImageActivity extends AppCompatActivity {
    private ImageView imageView;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewimage);
        imageView = findViewById(R.id.imageView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                final StorageReference[] newestImageRef = {null};
                final long[] newestImageTime = {Long.MIN_VALUE};
                for (StorageReference item : listResult.getItems()) {
                    item.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                        @Override
                        public void onSuccess(StorageMetadata storageMetadata) {
                            long timeCreated = storageMetadata.getCreationTimeMillis();
                            if (timeCreated > newestImageTime[0]) {
                                newestImageTime[0] = timeCreated;
                                newestImageRef[0] = item;
                            }
                            if (newestImageRef[0] != null) {
                                newestImageRef[0].getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Picasso.get().load(uri).into(imageView);
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }
}


