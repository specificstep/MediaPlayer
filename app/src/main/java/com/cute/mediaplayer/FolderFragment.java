package com.cute.mediaplayer;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import static com.cute.mediaplayer.LoadInterstitialAd.LoadInterstitialAds.showinterstitialAd;
import static com.cute.mediaplayer.MainActivity.folderList;
import static com.cute.mediaplayer.MainActivity.videoFiles;



public class FolderFragment extends Fragment {
    FolderAdapter folderAdapter;
    RecyclerView recyclerView;
    private PublisherAdView mPublisherAdView;
    public FolderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_folder, container, false);
        recyclerView = view.findViewById(R.id.foldersRV);

        mPublisherAdView = view.findViewById(R.id.publisherAdView);
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        mPublisherAdView.loadAd(adRequest);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        showinterstitialAd(getActivity(), adRequest1);
        if (folderList != null && folderList.size() > 0 && videoFiles != null)
        {
            folderAdapter = new FolderAdapter(folderList,videoFiles,getContext());
            recyclerView.setAdapter(folderAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL, false));
        }
        return view;
    }
}