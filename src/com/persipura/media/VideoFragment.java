package com.persipura.media;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import android.os.Bundle;

public class VideoFragment extends YouTubePlayerSupportFragment {

    public VideoFragment() { }

    public static VideoFragment newInstance(String url) {

        VideoFragment f = new VideoFragment();

        Bundle b = new Bundle();
        b.putString("url", url);

        f.setArguments(b);
        f.init();

        return f;
    }

    private void init() {

        initialize("AIzaSyBfr7GahwzUoeowkbR4zNqhO1-qF7P6J_w", new OnInitializedListener() {

            
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                
            }

			@Override
			public void onInitializationFailure(Provider arg0,
					YouTubeInitializationResult arg1) {
				// TODO Auto-generated method stub
				
			}

        });
    }
}