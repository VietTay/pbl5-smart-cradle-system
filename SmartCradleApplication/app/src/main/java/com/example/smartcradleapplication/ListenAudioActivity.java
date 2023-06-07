package com.example.smartcradleapplication;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

//public class ListenAudioActivity extends AppCompatActivity {
//
//    private Button btn_audio;
//    private String urlaudio;
//    private MediaPlayer mediaPlayer;
//
//    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
//    private DatabaseReference databaseReference=firebaseDatabase.getReference();
////    private DatabaseReference child = databaseReference.child("audios/ad3");
//    private DatabaseReference child = databaseReference.child("audios");
//
//    @Override
//    protected void onCreate(Bundle saveInstanceState){
//        super.onCreate(saveInstanceState);
//        setContentView(R.layout.activity_listenaudio);
//        btn_audio=findViewById(R.id.btn_play);
//    }
//
//    public void playaudio(View view) {
//        btn_audio.setEnabled(false);
//        mediaPlayer=new MediaPlayer();
//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        try {
//            mediaPlayer.setDataSource(urlaudio);
//            mediaPlayer.prepare();
//            mediaPlayer.start();
//            Toast.makeText(this,"Playing Audio",Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            Toast.makeText(this,"Error Occupied = "+ e,Toast.LENGTH_SHORT).show();
//            Log.e("TAG", "Error message", e);
//
//        }
//
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        child.orderByChild("timestamp").limitToLast(1).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                urlaudio=dataSnapshot.getValue(String.class);
////                Toast.makeText(ListenAudioActivity.this,"Process complete ",Toast.LENGTH_SHORT).show();
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    urlaudio = postSnapshot.getValue(String.class);
//                    Toast.makeText(ListenAudioActivity.this, "Process complete", Toast.LENGTH_SHORT).show();
//                    Log.d("TAG",urlaudio);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(ListenAudioActivity.this,"Process canceled !",Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
public class ListenAudioActivity extends AppCompatActivity {

    private Button btn_audio;
    private String urlaudio;
    private MediaPlayer mediaPlayer;

    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference().child("audios");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listenaudio);
        btn_audio = findViewById(R.id.btn_play);
    }

    public void playaudio(View view) {
        btn_audio.setEnabled(false);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(urlaudio);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(this, "Playing Audio", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error Occupied = " + e, Toast.LENGTH_SHORT).show();
            Log.e("TAG", "Error message", e);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                final StorageReference[] newestAudioRef = {null};
                final long[] newestTimestamp = {Long.MIN_VALUE};
                for (StorageReference item : listResult.getItems()) {
                    item.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                        @Override
                        public void onSuccess(StorageMetadata storageMetadata) {
                            if (storageMetadata.getCreationTimeMillis() > newestTimestamp[0]) {
                                newestAudioRef[0] = item;
                                newestTimestamp[0] = storageMetadata.getCreationTimeMillis();
                            }
                            if (newestAudioRef[0] != null) {
                                newestAudioRef[0].getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        urlaudio = uri.toString();
//                                        Toast.makeText(ListenAudioActivity.this, "Process complete", Toast.LENGTH_SHORT).show();
                                        Log.d("TAG", urlaudio);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ListenAudioActivity.this, "Error Occupied = " + e, Toast.LENGTH_SHORT).show();
                                        Log.e("TAG", "Error message", e);
                                    }
                                });
                            } else {
                                Toast.makeText(ListenAudioActivity.this, "No audio found in storage", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ListenAudioActivity.this, "Error Occupied = " + e, Toast.LENGTH_SHORT).show();
                Log.e("TAG", "Error message", e);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

