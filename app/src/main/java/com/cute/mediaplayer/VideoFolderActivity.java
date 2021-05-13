
package com.cute.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import java.util.ArrayList;

public class VideoFolderActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    VideoFolderAdapter videoFolderAdapter;
    String myFolderName;
    private PublisherAdView mPublisherAdView;
    ArrayList<VideoFiles> videoFilesArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_folder);

        recyclerView = findViewById(R.id.folderVideoRV);
        recyclerView.setHasFixedSize(true);
        myFolderName = getIntent().getStringExtra("folderName");

        mPublisherAdView = findViewById(R.id.publisherAdView);
        PublisherAdRequest adRequest1 = new PublisherAdRequest.Builder().build();
        mPublisherAdView.loadAd(adRequest1);

        if (myFolderName != null )
        {
            videoFilesArrayList = getAllVideo(this, myFolderName);
        }

        if (videoFilesArrayList.size() > 0)
        {
            videoFolderAdapter = new VideoFolderAdapter(this, videoFilesArrayList);
            recyclerView.setAdapter(videoFolderAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        }
    }
    public static ArrayList<VideoFiles> getAllVideo(Context context,String folderName) {
        ArrayList<VideoFiles> tempVideoFiles = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME
        };
        String selection = MediaStore.Video.Media.DATA + " like?";
        String[] selectionArgs = new String[]{"%" + folderName + "%"};
        Cursor cursor = context.getContentResolver().query(uri, projection, selection,selectionArgs,null);
        if (cursor != null){
            while (cursor.moveToNext())
            {
                String id = cursor.getString(0);
                String path = cursor.getString(1);
                String title = cursor.getString(2);
                String size = cursor.getString(3);
                String dateAdded = cursor.getString(4);
                String duration = cursor.getString(5);
                String fileName = cursor.getString(6);
                String bucket_name = cursor.getString(7);
                VideoFiles videoFiles = new VideoFiles(id,path,title,fileName,size,dateAdded,duration);
                if (folderName.endsWith(bucket_name))
                {
                    tempVideoFiles.add(videoFiles);
                }

            }
            cursor.close();
        }

        return tempVideoFiles;
    }

}