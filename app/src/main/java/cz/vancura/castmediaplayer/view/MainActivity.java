package cz.vancura.castmediaplayer.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;

import cz.vancura.castmediaplayer.helpers.HelperMethods;
import cz.vancura.castmediaplayer.model.MoviePOJO;
import cz.vancura.castmediaplayer.R;
import cz.vancura.castmediaplayer.view.exoplayer.PlayerActivity;
import cz.vancura.castmediaplayer.view.recyclerView.ListItemClickListener;
import cz.vancura.castmediaplayer.view.recyclerView.MovieAdapter;
import cz.vancura.castmediaplayer.viewmodel.MainActivityViewModel;

import static cz.vancura.castmediaplayer.viewmodel.MainActivityViewModel.HttpGetData;

/*
MainActivity - class + view
 */

public class MainActivity extends AppCompatActivity implements ListItemClickListener {

    private static String TAG = "myTAG-MainActivity";

    public static Context context;
    public static View MainView;

    // Recycler View
    static RecyclerView mRecyclerView;
    private static MovieAdapter movieAdapter;

    // GUI
    private ImageView imageRecyclerViewStyle, imageRecyclerViewSort;
    private static ProgressBar progressBar;

    // MVVM
    private static MainActivityViewModel mainActivityViewModel;

    boolean RecyclerViewCollumns = true;
    boolean SortByIdLowToHigh = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "started");
        context = this;
        MainView = findViewById(R.id.mainView);

        // ViewModel
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        // GUI
        imageRecyclerViewStyle = findViewById(R.id.imageViewMainViewStyle);
        imageRecyclerViewSort = findViewById(R.id.imageViewMainViewSort);
        progressBar = findViewById(R.id.progressBar);

        // ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Recycler View init
        mRecyclerView = findViewById(R.id.recycler_view);
        SetupRecyclerView(RecyclerViewCollumns);

        // GUI - loading ..
        ShowLoading();


        // is online ?
        if(HelperMethods.IsOnline(context)){
            // online
            HttpGetData();
        }else{
            // offline
            ShowError("Offline - to be online is better...");
        }

        // Images - onClick - user selection : sort RecyclerView
        imageRecyclerViewSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "clicked - RecylerView Sort icon");

                // sort by - hardcoded - make user selection in dialog - to do idea
                String SortBy = "id";

                switch(SortBy) {
                    case "name":
                        Log.d(TAG, "sort by name");
                        // sort by - implement by Name - to do idea

                        // code block
                        break;
                    case "views":
                        Log.d(TAG, "sort by views");
                        // sort by - implement by views - to do idea

                        // code block
                        break;

                    case "id":
                        Log.d(TAG, "sort by id - SortByIdLowToHigh=" + SortByIdLowToHigh);

                        // reverse
                        SortByIdLowToHigh = ! SortByIdLowToHigh;

                        // Sort in order of movie id - FUNGUJE
                        Collections.sort(MainActivityViewModel.moviePOJOList, new Comparator<MoviePOJO>() {
                            public int compare(MoviePOJO moviePOJO1, MoviePOJO moviePOJO2) {
                                if(moviePOJO1.getMovieId() > moviePOJO2.getMovieId()) {
                                    if (SortByIdLowToHigh){
                                        return 1;
                                    }else{
                                        return -1;
                                    }
                                }else if(moviePOJO1.getMovieId() < moviePOJO2.getMovieId()) {
                                    if(SortByIdLowToHigh){
                                        return -1;
                                    }else{
                                        return 1;
                                    }
                                }
                                return 0;
                            }
                        });

                        // code block
                        break;


                    default:
                        // code block
                }


                RefreshRecyclerView();

            }
        });


        // Images - onClick - user selection : show RecyclerView with columns or not
        imageRecyclerViewStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "clicked - RecylerView Style icon - RecyclerViewCollumns=" + RecyclerViewCollumns);

                // set oposite value
                RecyclerViewCollumns = !RecyclerViewCollumns;

                if (RecyclerViewCollumns) {
                    // show columens
                    SetupRecyclerView(true);
                    // image change
                    imageRecyclerViewStyle.setImageResource(R.drawable.ic_view_list_24px_black);
                }else{
                    // hide columns
                    SetupRecyclerView(false);
                    // image change
                    imageRecyclerViewStyle.setImageResource(R.drawable.ic_view_stream_24px_black);
                }

            }
        });


   }


    // GUI - loading
    private void ShowLoading(){

        Log.d(TAG, "ShowLoading");

        // show ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        // hide RecyclerView
        mRecyclerView.setVisibility(View.GONE);

    }

    // GUI - loading done
    public static void ShowRecyclerView(){

        Log.d(TAG, "ShowRecyclerView");

        // hide ProgressBar
        progressBar.setVisibility(View.GONE);
        // show RecyclerView
        mRecyclerView.setVisibility(View.VISIBLE);

    }

    // GUI - error
    public static void ShowError(String error){

        Log.d(TAG, "ShowError");

        // SnackBar
        HelperMethods.ShowSnackbar(MainActivity.context, MainView, error);

        // ProgressBar hide
        progressBar.setVisibility(View.GONE);
    }



    // create RecyclerView
    private void SetupRecyclerView(Boolean showCollumns) {

        if (showCollumns) {
            Log.d(TAG, "SetupRecyclerView - with columns");
            // 2 colllumns for portait, 3 for landscape
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, 3));
            }
        }else{
            // no columns, rows below each other
            Log.d(TAG, "SetupRecyclerView - without columns");
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            mRecyclerView.setLayoutManager(mLayoutManager);
        }

        // adapter
        movieAdapter = new MovieAdapter(MainActivityViewModel.moviePOJOList, this, context);

        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(movieAdapter);

    }


    // refresh RecyclerView
    public static void RefreshRecyclerView(){
        Log.d(TAG, "RefreshRecyclerView");
        movieAdapter.notifyDataSetChanged();
    }


    // Menu - create
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    // Menu - selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_refresh:
                Log.d(TAG, "Refresh from menu");
                HttpGetData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }


    // RecyclerView onCLick callback - play it
    @Override
    public void onListItemClick(int position) {
        Log.d(TAG, "onListItemClick " + position);

        Log.d(TAG, "Starting PlayerActivity ..");

        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra("myPosition", position);
        startActivity(intent);

    }


    // RecyclerView onLongClick callback - delete
    @Override
    public void onLongClick(int position) {
        Log.d(TAG, "onLongClick " + position);
        // delete from list
        mainActivityViewModel.moviePOJOList.remove(position);
        // refresh RecyclerView
        movieAdapter.notifyDataSetChanged();
        // user info
        HelperMethods.ShowSnackbar(this, MainView, "Movie deleted - Refresh to undo");
    }

}