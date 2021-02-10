package cz.vancura.castmediaplayer.viewmodel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import cz.vancura.castmediaplayer.helpers.HelperMethods;
import cz.vancura.castmediaplayer.model.MoviePOJO;
import cz.vancura.castmediaplayer.model.retrofit.RetrofitAPIClient;
import cz.vancura.castmediaplayer.model.retrofit.RetrofitAPIInterface;
import cz.vancura.castmediaplayer.model.retrofit.RetrofitPOJO;
import cz.vancura.castmediaplayer.view.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static cz.vancura.castmediaplayer.view.MainActivity.MainView;

public class MainActivityViewModel extends ViewModel {

    private static String TAG = "myTAG-MainActivityViewModel";

    public static List<MoviePOJO> moviePOJOList = new ArrayList<>();


    // download list of movies from REST Api
    public static void HttpGetData() {
        Log.d(TAG, "HttpGetData");

        // empty list
        moviePOJOList.clear();

        // Retrofit interface
        RetrofitAPIInterface apiInterface = RetrofitAPIClient.getClient(false).create(RetrofitAPIInterface.class);
        // Retrofit HTTP call
        Call<List<RetrofitPOJO>> call = apiInterface.getMovies();
        call.enqueue(new Callback<List<RetrofitPOJO>>() {
            @Override
            public void onResponse(Call<List<RetrofitPOJO>> call, Response<List<RetrofitPOJO>> response) {

                Log.d(TAG, "HttpGetData - response code (200 is OK)=" + response.code());

                if (response.code() == 200) {
                    // ok

                    // try/catch while parsing data - server may change JSON structure
                    try{

                        for (RetrofitPOJO retrofitPOJO : response.body()) {
                            String data = retrofitPOJO.name + " " + retrofitPOJO.urlMovie + "\n";
                            Log.d(TAG,"response data loop =" +  data);

                            // create POJO
                            MoviePOJO moviePOJO = new MoviePOJO(retrofitPOJO.id, retrofitPOJO.name, retrofitPOJO.urlMovie, retrofitPOJO.urlIcon, retrofitPOJO.descr, retrofitPOJO.source, retrofitPOJO.duration, retrofitPOJO.views);
                            // add to List
                            moviePOJOList.add(moviePOJO);

                        }

                        // refresh RecyclerView
                        MainActivity.RefreshRecyclerView();

                    }catch (Exception e){
                        String error = "Server ERROR " +  e.getLocalizedMessage();
                        Log.e(TAG,error);
                        HelperMethods.ShowSnackbar(MainActivity.context, MainView, error);
                    }

                }else{
                    //ng
                    Log.e(TAG, "Server response is not ok " + response.code());
                    HelperMethods.ShowSnackbar(MainActivity.context, MainView, "Server response is not OK");
                }


            }

            @Override
            public void onFailure(Call<List<RetrofitPOJO>> call, Throwable t) {
                String error = "Server ERROR " +  t.getLocalizedMessage();
                Log.e(TAG, "onFailure " + error);
                HelperMethods.ShowSnackbar(MainActivity.context, MainView, error);
                call.cancel();
            }
        });


        // refresh RecyclerView
        MainActivity.RefreshRecyclerView();

    }


}
