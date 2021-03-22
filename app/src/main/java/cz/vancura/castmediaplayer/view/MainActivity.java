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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cz.vancura.castmediaplayer.helpers.HelperMethods;
import cz.vancura.castmediaplayer.model.MoviePOJO;
import cz.vancura.castmediaplayer.R;
import cz.vancura.castmediaplayer.view.exoplayer.PlayerActivity;
import cz.vancura.castmediaplayer.view.recyclerView.ListItemClickListener;
import cz.vancura.castmediaplayer.view.recyclerView.MovieAdapter;
import cz.vancura.castmediaplayer.viewmodel.MainActivityViewModel;



/*
MainActivity - class + view
 */

public class MainActivity extends AppCompatActivity implements ListItemClickListener {

    private static String TAG = "myTAG-MainActivity";

    public Context context;
    public View MainView;

    // Recycler View
    static RecyclerView mRecyclerView;
    private MovieAdapter movieAdapter;

    // GUI
    private ImageView imageRecyclerViewStyle, imageRecyclerViewSort;
    private ProgressBar progressBar;

    // MVVM ViewModel
    private static MainActivityViewModel mainActivityViewModel;

    boolean RecyclerViewCollumns = true;
    boolean SortByIdLowToHigh = true;

    HelperMethods helperMethods;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "started");
        context = this;
        MainView = findViewById(R.id.mainView);

        // ViewModel
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        // LiveData Observer - for Error
        final Observer<String> errorObserver = error -> {
            // Update the UI
            Log.d(TAG, "error observer - update GUI now");
            ShowError(error);
        };
        mainActivityViewModel.getErrorLiveData().observe(this, errorObserver);


        // LiveData Observer - for List of MoviePOJO
        final Observer<List<MoviePOJO>> dataObserver = list -> {
            // Update the UI
            Log.d(TAG, "data observer - update GUI now");
            ShowRecyclerView();

        };
        mainActivityViewModel.getMoviePOJOListLiveData().observe(this, dataObserver);


        // GUI
        imageRecyclerViewStyle = findViewById(R.id.imageViewMainViewStyle);
        imageRecyclerViewSort = findViewById(R.id.imageViewMainViewSort);
        progressBar = findViewById(R.id.progressBar);

        // ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // helper class
        helperMethods = new HelperMethods();

        // Recycler View init
        mRecyclerView = findViewById(R.id.recycler_view);
        SetupRecyclerView(RecyclerViewCollumns);


        // is online ?
        if(helperMethods.IsOnline(context)){

            // GUI - loading ..
            ShowLoading();

            // online
            mainActivityViewModel.HttpGetData();
        }else{

            // GUI - offline
            ShowError("Offline - to be online is better...");

        }

        // Images - onClick - user selection : sort RecyclerView
        imageRecyclerViewSort.setOnClickListener(view -> {
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
                    Collections.sort(MainActivityViewModel.moviePOJOList, (moviePOJO1, moviePOJO2) -> {
                        if (moviePOJO1.getMovieId() > moviePOJO2.getMovieId()) {
                            if (SortByIdLowToHigh) {
                                return 1;
                            } else {
                                return -1;
                            }
                        } else if (moviePOJO1.getMovieId() < moviePOJO2.getMovieId()) {
                            if (SortByIdLowToHigh) {
                                return -1;
                            } else {
                                return 1;
                            }
                        }
                        return 0;
                    });

                    // code block
                    break;


                default:
                    // code block
            }


            ShowRecyclerView();

        });


        // Images - onClick - user selection : show RecyclerView with columns or not
        imageRecyclerViewStyle.setOnClickListener(view -> {
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
    private void ShowRecyclerView(){

        Log.d(TAG, "ShowRecyclerView");

        // hide ProgressBar
        progressBar.setVisibility(View.GONE);
        // show RecyclerView
        mRecyclerView.setVisibility(View.VISIBLE);

        // refresh
        movieAdapter.notifyDataSetChanged();

    }

    // GUI - error
    private void ShowError(String error){

        Log.d(TAG, "ShowError");

        // SnackBar
        helperMethods.ShowSnackbar(context, MainView, error);

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

        // Lint: should use if instead of switch
        if (item.getItemId() == R.id.action_refresh) {
            Log.d(TAG, "Refresh from menu");
            mainActivityViewModel.HttpGetData();
            return true;
        }
        return super.onOptionsItemSelected(item);


    }


    // RecyclerView onCLick callback - play selected video
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
        MainActivityViewModel.moviePOJOList.remove(position);
        // refresh RecyclerView
        movieAdapter.notifyDataSetChanged();
        // user info
        helperMethods.ShowSnackbar(this, MainView, "Movie deleted - Refresh to undo");
    }

}