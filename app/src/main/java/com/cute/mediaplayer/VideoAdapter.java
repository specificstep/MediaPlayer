package com.cute.mediaplayer;

import android.content.ContentUris;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {
    private Context mContext;
    static ArrayList<VideoFiles> videoFiles;
    View view;

    public VideoAdapter(Context mContext, ArrayList<VideoFiles> videoFiles) {
        this.mContext = mContext;
        this.videoFiles = videoFiles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(mContext).inflate(R.layout.video_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.fileName.setText(videoFiles.get(i).getTitle());
        String time = videoFiles.get(i).getDuration();
        if(time != null){
            long millis = Long.parseLong(time);
            long minutes = (millis/1000)/60;
            int seconds = (int)(millis/1000)%60;
            String n_seconds;
            if (seconds<10)
                n_seconds = "0" + String.valueOf(seconds);
            else
                n_seconds = String.valueOf(seconds);
            String video_duration = String.valueOf(minutes) + ":" + n_seconds;
            myViewHolder.videoDuration.setText(video_duration);
        }
        Glide.with(mContext)
                .load(new File(videoFiles.get(i).getPath()))
                .into(myViewHolder.thumbnail);
        myViewHolder.fileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PlayerActivity.class);
                intent.putExtra("position", i);
                intent.putExtra("sender","FilesIsSending");
                mContext.startActivity(intent);
            }
        });
        myViewHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PlayerActivity.class);
                intent.putExtra("position", i);
                intent.putExtra("sender","FilesIsSending");
                mContext.startActivity(intent);
            }
        });
        myViewHolder.menu_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mContext,view);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId())
                        {
                            case R.id.delete:
                                File file = new File(videoFiles.get(i).getPath());
                                final Uri trackuri= ContentUris.withAppendedId(
                                        android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI, Long.parseLong(videoFiles.get(i).getId()));
                                mContext.getContentResolver().delete(trackuri, null,null);
                                videoFiles.remove(i);
                                notifyItemRemoved(i);
                                notifyItemRangeChanged(i,videoFiles.size());
                                if(file.exists()){
                                    boolean deleted = file.delete();
                                }
                                break;
                        }
                        return true;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoFiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail, menu_more;
        TextView fileName,videoDuration;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            menu_more = itemView.findViewById(R.id.menu_more);
            fileName = itemView.findViewById(R.id.video_file_name);
            videoDuration = itemView.findViewById(R.id.video_duration);
        }
    }
}
