package cz.vancura.castmediaplayer.view.exoplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaLoadRequestData;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.CastState;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.IntroductoryOverlay;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.images.WebImage;

import cz.vancura.castmediaplayer.R;
import cz.vancura.castmediaplayer.helpers.HelperMethods;
import cz.vancura.castmediaplayer.viewmodel.MainActivityViewModel;
import cz.vancura.castmediaplayer.viewmodel.PlayerActivityViewModel;

public class PlayerActivity extends AppCompatActivity {

    private static String TAG = "myTAG-PlayerActivity";

    // ViewModel
    private static PlayerActivityViewModel playerActivityViewModel;
    public static Context context;
    public static View PlayerView;
    TextView textViewName, textViewDescr, textViewLicence, textViewViews;
    private static int positon;

    // ExoPlayer
    private static com.google.android.exoplayer2.ui.PlayerView exoPlayerView;
    private static SimpleExoPlayer player;
    private static PlaybackStateListener playbackStateListener;
    private static boolean playWhenReady = false;
    private static int currentWindow = 0;
    private static long playbackPosition = 0;
    private static String videoUrl = "";

    // Cast
    private CastContext mCastContext;
    private CastStateListener mCastStateListener;
    private IntroductoryOverlay mIntroductoryOverlay;
    private MenuItem mediaRouteMenuItem;
    private static CastSession mCastSession;
    private SessionManagerListener<CastSession> mSessionManagerListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Log.d(TAG, "onCreate");
        context = this;

        // view
        PlayerView = findViewById(R.id.playerView);
        exoPlayerView = findViewById(R.id.exo_video_view);

        // ViewModel
        playerActivityViewModel = new ViewModelProvider(this).get(PlayerActivityViewModel.class);

        // GUI
        textViewName = findViewById(R.id.textViewPlayerName);
        textViewDescr = findViewById(R.id.textViewPlayerDescr);
        textViewLicence = findViewById(R.id.textViewPlayerLicence);
        textViewViews = findViewById(R.id.textViewPlayerViews);

        // receive data sent from MainActivity
        Intent intent = getIntent();
        positon = intent.getIntExtra("myPosition",0);
        Log.d(TAG, "Recived data from MainActivity - positon=" + positon);


        // TODO media - pridat 2 svoje videa dron
        

        // moview info
        int movieId = MainActivityViewModel.moviePOJOList.get(positon).getMovieId();
        String movieName = MainActivityViewModel.moviePOJOList.get(positon).getMovieName();
        String movieUrl = MainActivityViewModel.moviePOJOList.get(positon).getMovieUrl();
        String movieDescr = "About\n"+ MainActivityViewModel.moviePOJOList.get(positon).getMovieDescription();
        String movieLicence = "Source\n" + MainActivityViewModel.moviePOJOList.get(positon).getMovieLicence();
        String movieViews = "Views " + String.valueOf(MainActivityViewModel.moviePOJOList.get(positon).getMovieViews());

        String text = movieName + " " + movieUrl + " " + movieDescr + " " + movieLicence + " " + movieViews;
        Log.d(TAG, "We will play movie : " + text);

        // GUI
        textViewName.setText(movieName);
        textViewDescr.setText(movieDescr);
        textViewLicence.setText(movieLicence);
        textViewViews.setText(movieViews);

        // count of views +1 at server side
        Log.d(TAG, "views +1 - calling Retrofit in ViewModel ...");
        PlayerActivityViewModel.HttpPostViews(movieId);

        // create video player
        videoUrl = movieUrl;
        ExoPlayerCreate();
        playbackStateListener = new PlaybackStateListener();

        // Cast Listener
        setupCastListener();

        // Cast context
        mCastContext = CastContext.getSharedInstance(context);

        // Cast Session
        mCastSession = mCastContext.getSessionManager().getCurrentCastSession();

        // Cast CastStateListener
        mCastStateListener = new CastStateListener() {
            @Override
            public void onCastStateChanged(int newState) {

                switch(newState) {
                    case 1:
                        Log.d(TAG, "mCastStateListener - onCastStateChanged - state 1 - NO_DEVICES_AVAILABLE - No Cast devices are available");
                        break;
                    case 2:
                        Log.d(TAG, "mCastStateListener - onCastStateChanged - state 2 - NOT_CONNECTED - Cast devices are available, but a Cast session is not established.");
                        break;
                    case 3:
                        Log.d(TAG, "mCastStateListener - onCastStateChanged - state 3 - CONNECTING - A Cast session is being established.");
                        break;
                    case 4:
                        Log.d(TAG, "mCastStateListener - onCastStateChanged - state 4 - CONNECTED - A Cast session is established.");
                        break;
                    default:
                        // empty
                }

                // there was found Cast device
                if (newState != CastState.NO_DEVICES_AVAILABLE) {
                    showIntroductoryOverlay();
                }
            }
        };


    }


    //********** ExoPlayer methods *******

    // ExoPlayer create
    private static void ExoPlayerCreate(){
        Log.d(TAG, "ExoPlayerCreate()");

        // create player
        player = new SimpleExoPlayer.Builder(context).build();
        exoPlayerView.setPlayer(player);

    }

    // ExoPlayer init
    private static void ExoPlayerInitialize(){
        Log.d(TAG, "ExoPlayerInitialize()");

        Uri uri = Uri.parse(videoUrl);

        MediaSource mediaSource = buildMediaSource(uri);

        player.setPlayWhenReady(playWhenReady);
        player.prepare(mediaSource, false, false);

        // add listener
        Log.d(TAG, "add playbackStateListener");
        player.addListener(playbackStateListener);

    }

    // ExoPlayer MediaSource
    private static MediaSource buildMediaSource(Uri uri) {

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, "exoplayer-codelab");
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);

    }

    // ExoPlayer release
    private static void ExoPlayerRelease(){
        Log.d(TAG, "ExoPlayerRelease()");

        // remove listener
        Log.d(TAG, "remove playbackStateListener");
        player.removeListener(playbackStateListener);

        currentWindow = player.getCurrentWindowIndex();
        player.release();
        player = null;

    }

    // ExoPlayer stop
    private static void ExoPlayerStop(){
        player.setPlayWhenReady(false);
    }

    // ExoPlayer play
    private static void ExoPlayerPlay(){
        player.setPlayWhenReady(true);
    }



    //********** Cast methods *******

    // Cast - Cast Button
    private void showIntroductoryOverlay() {
        Log.d(TAG, "showIntroductoryOverlay");

        if (mIntroductoryOverlay != null) {
            // Log.d(TAG, "showIntroductoryOverlay - remove");
            mIntroductoryOverlay.remove();
        }

        if ((mediaRouteMenuItem != null) && mediaRouteMenuItem.isVisible()) {
            // Log.d(TAG, "showIntroductoryOverlay - show cast");
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mIntroductoryOverlay = new IntroductoryOverlay.Builder(
                            PlayerActivity.this, mediaRouteMenuItem)
                            .setTitleText("Introducing Cast")
                            .setSingleTime()
                            .setOnOverlayDismissedListener(
                                    new IntroductoryOverlay.OnOverlayDismissedListener() {
                                        @Override
                                        public void onOverlayDismissed() {
                                            mIntroductoryOverlay = null;
                                        }
                                    })
                            .build();
                    mIntroductoryOverlay.show();
                }
            });
        }
    }

    // Cast - Session Manager Listener
    private void setupCastListener() {
        mSessionManagerListener = new SessionManagerListener<CastSession>() {

            @Override
            public void onSessionEnded(CastSession session, int error) {
                onApplicationDisconnected();
                Log.d(TAG, "SessionManagerListener - onSessionEnded");
            }

            @Override
            public void onSessionResumed(CastSession session, boolean wasSuspended) {
                onApplicationConnected(session);
                Log.d(TAG, "SessionManagerListener - onSessionResumed");
            }

            @Override
            public void onSessionResumeFailed(CastSession session, int error) {
                onApplicationDisconnected();
                Log.d(TAG, "SessionManagerListener - onSessionResumeFailed");
            }

            @Override
            public void onSessionStarted(CastSession session, String sessionId) {
                onApplicationConnected(session);
                Log.d(TAG, "SessionManagerListener - onSessionStarted");
            }

            @Override
            public void onSessionStartFailed(CastSession session, int error) {
                onApplicationDisconnected();
                Log.d(TAG, "SessionManagerListener - onSessionStartFailed");
            }

            @Override
            public void onSessionStarting(CastSession session) {
                Log.d(TAG, "SessionManagerListener - onSessionStarting");
            }

            @Override
            public void onSessionEnding(CastSession session) {
                Log.d(TAG, "SessionManagerListener - onSessionEnding");
            }

            @Override
            public void onSessionResuming(CastSession session, String sessionId) {
                Log.d(TAG, "SessionManagerListener - onSessionResuming");
            }

            @Override
            public void onSessionSuspended(CastSession session, int reason) {
                Log.d(TAG, "SessionManagerListener - onSessionSuspended");
            }

            private void onApplicationConnected(CastSession castSession) {
                Log.d(TAG, "SessionManagerListener - onApplicationConnected... ");

                mCastSession = castSession;

                // Start Casting - auto play on
                StartCast(true);

                // cast device was just connected - we can stop local player
                ExoPlayerStop();

                // Show SnackBar
                HelperMethods.ShowSnackbar(PlayerActivity.context, PlayerView, "Casting started");

                invalidateOptionsMenu();
            }

            private void onApplicationDisconnected() {

                Log.d(TAG, "SessionManagerListener - onApplicationDisconnected... ");

                // cast device was dissconnected - we can resume local player or show info to user
                ExoPlayerPlay();

                // Show SnackBar
                HelperMethods.ShowSnackbar(PlayerActivity.context, PlayerView, "Casting stopped");

                invalidateOptionsMenu();
            }
        };
    }

    // Cast - start remote play - called when player started playback from PlaybackStateListener
    public static void StartCast(Boolean autoPlay){
        Log.d(TAG, "StartCast");

        if (mCastSession != null && mCastSession.isConnected()) {
            Log.d(TAG, "StartCast - Cast session is connected - we can play on REMOTE");
            loadRemoteMedia(0, autoPlay);
        } else {
            Log.d(TAG, "StartCast - Cast session is NOT connected - we have to play on LOCAL");
        }

    }


    // Cast - menu with Cast Button
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.browse, menu);
        mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu, R.id.media_route_menu_item);
        return true;
    }


    // Cast - loadRemoteMedia
    private static void loadRemoteMedia(int position, boolean autoPlay) {
        Log.d(TAG, "loadRemoteMedia");

        if (mCastSession == null) {
            return;
        }
        RemoteMediaClient remoteMediaClient = mCastSession.getRemoteMediaClient();
        if (remoteMediaClient == null) {
            return;
        }
        remoteMediaClient.load(new MediaLoadRequestData.Builder()
                .setMediaInfo(buildMediaInfo()) // see method buildMediaInfo()
                .setAutoplay(autoPlay)
                .setCurrentTime(position).build());
    }

    // Cast - buildMediaInfo - pass movie data to Cast
    private static MediaInfo buildMediaInfo() {
        Log.d(TAG, "loadRemoteMedia");

        MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);

        // pass movie info
        String movieName = MainActivityViewModel.moviePOJOList.get(positon).getMovieName();
        String movieImageUrl = MainActivityViewModel.moviePOJOList.get(positon).getMovieImageUrl();
        String movieUrl = MainActivityViewModel.moviePOJOList.get(positon).getMovieUrl();

        //movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, mSelectedMedia.getStudio());
        movieMetadata.putString(MediaMetadata.KEY_TITLE, movieName);
        movieMetadata.addImage(new WebImage(Uri.parse(movieImageUrl)));
        //movieMetadata.addImage(new WebImage(Uri.parse(mSelectedMedia.getImage(1))));

        return new MediaInfo.Builder(movieUrl)
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                //.setContentType("videos/mp4")
                .setMetadata(movieMetadata)
                //.setStreamDuration(mSelectedMedia.getDuration() * 1000)
                .build();

    }



    // Lifecycle


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Lifecycle - onResume");

        ExoPlayerInitialize();

        // cast SessionManagerListener on
        mCastContext.getSessionManager().addSessionManagerListener(mSessionManagerListener, CastSession.class);

        // cast CastStateListenerlistener on
        mCastContext.addCastStateListener(mCastStateListener);
    }



    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Lifecycle - onPause");

        ExoPlayerRelease();

        // cast SessionManagerListener off
        mCastContext.getSessionManager().removeSessionManagerListener(mSessionManagerListener, CastSession.class);

        // cast CastStateListenerlistener off
        mCastContext.removeCastStateListener(mCastStateListener);
    }


}
