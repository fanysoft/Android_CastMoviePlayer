package cz.vancura.castmediaplayer.view.exoplayer;

import android.util.Log;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;

/*
ExoPlayer state listener
 */

public class PlaybackStateListener implements Player.EventListener {

    private static String TAG = "myTAG-PlaybackStateListener";

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        String stateString;

        switch (playbackState) {

            case ExoPlayer.STATE_IDLE:
                // The player has been instantiated but has not yet been prepared with a MediaSource.
                stateString = "ExoPlayer.STATE_IDLE      -";
                Log.d(TAG, "onPlayerStateChanged - player state changed state to " + stateString + " playWhenReady: " + playWhenReady);
                break;

            case ExoPlayer.STATE_BUFFERING:
                // The player is not able to play from the current position because not enough data has been buffered.
                stateString = "ExoPlayer.STATE_BUFFERING -";
                Log.d(TAG, "onPlayerStateChanged - player state changed state to " + stateString + " playWhenReady: " + playWhenReady);
                break;

            case ExoPlayer.STATE_READY:
                // The player is able to immediately play from the current position. This means the player will start playing media automatically if playWhenReady is true. If it is false the player is paused.
                stateString = "ExoPlayer.STATE_READY     -";
                Log.d(TAG, "onPlayerStateChanged - player state changed state to " + stateString + " playWhenReady: " + playWhenReady);

                if (playWhenReady){
                    Log.d(TAG, "onPlayerStateChanged - Player is plaing");
                }else{
                    Log.d(TAG, "onPlayerStateChanged - Player is paused");
                }
                // when playWhenReady: true - player is playing
                break;

            case ExoPlayer.STATE_ENDED:
                // The player has finished playing the media.
                stateString = "ExoPlayer.STATE_ENDED     -";
                Log.d(TAG, "onPlayerStateChanged - player state changed state to " + stateString + " playWhenReady: " + playWhenReady);
                break;

            default:
                stateString = "UNKNOWN_STATE             -";
                Log.d(TAG, "onPlayerStateChanged - player state changed state to " + stateString + " playWhenReady: " + playWhenReady);
                break;
        }

    }
}