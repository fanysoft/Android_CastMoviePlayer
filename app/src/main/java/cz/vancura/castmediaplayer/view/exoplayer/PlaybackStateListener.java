package cz.vancura.castmediaplayer.view.exoplayer;

import android.util.Log;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;

public class PlaybackStateListener implements Player.EventListener {

    private static String TAG = "myTAG-PlaybackStateListener";

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        String stateString;

        switch (playbackState) {

            case ExoPlayer.STATE_IDLE:
                // The player has been instantiated but has not yet been prepared with a MediaSource.
                stateString = "ExoPlayer.STATE_IDLE      -";
                break;

            case ExoPlayer.STATE_BUFFERING:
                // The player is not able to play from the current position because not enough data has been buffered.
                stateString = "ExoPlayer.STATE_BUFFERING -";
                break;

            case ExoPlayer.STATE_READY:
                // The player is able to immediately play from the current position. This means the player will start playing media automatically if playWhenReady is true. If it is false the player is paused.
                stateString = "ExoPlayer.STATE_READY     -";
                // when playWhenReady: false - platey is paused
                // when playWhenReady: true - player is playing
                break;

            case ExoPlayer.STATE_ENDED:
                // The player has finished playing the media.
                stateString = "ExoPlayer.STATE_ENDED     -";
                break;

            default:
                stateString = "UNKNOWN_STATE             -";
                break;
        }
        Log.d(TAG, "onPlayerStateChanged - player state changed state to " + stateString + " playWhenReady: " + playWhenReady);
    }
}