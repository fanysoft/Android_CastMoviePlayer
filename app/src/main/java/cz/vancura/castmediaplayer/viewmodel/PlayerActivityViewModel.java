package cz.vancura.castmediaplayer.viewmodel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import cz.vancura.castmediaplayer.helpers.HelperMethods;
import cz.vancura.castmediaplayer.model.retrofit.RetrofitAPIClient;
import cz.vancura.castmediaplayer.model.retrofit.RetrofitAPIInterface;
import cz.vancura.castmediaplayer.view.exoplayer.PlayerActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static cz.vancura.castmediaplayer.view.exoplayer.PlayerActivity.PlayerView;

/*
MVVM ViewModel for PlayerActitivy - bussiness logic here is Http Work
 */

public class PlayerActivityViewModel extends ViewModel {

    private static String TAG = "myTAG-PlayerActivityViewModel";

    // Http job - will touch REST API with movie id, PHP will increase views count +1
    public static void HttpPostViews(int movieId){
        Log.d(TAG, "HttpPostViews - movieId=" + movieId);


        // Retrofit interface
        RetrofitAPIInterface apiInterface = RetrofitAPIClient.getClient(true).create(RetrofitAPIInterface.class);
        // Retrofit HTTP call
        Call<String> call = apiInterface.postViews(movieId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.d(TAG, "HttpGetData - response code (200 is OK)=" + response.code());

                if (response.code() == 200) {
                    // ok
                    // try/catch while parsing data - server may change JSON structure
                    try{

                        String  text = response.body();
                        Log.d(TAG, "Response=" + text); // example Server response - views updated
                        // no futher action

                    }catch (Exception e){
                        String error = "Server ERROR " +  e.getLocalizedMessage();
                        Log.e(TAG,error);
                        HelperMethods.ShowSnackbar(PlayerActivity.context, PlayerView, error);
                    }

                }else{
                    //ng
                    Log.e(TAG, "Server response is not ok " + response.code());
                    HelperMethods.ShowSnackbar(PlayerActivity.context, PlayerView, "Server response is not OK");
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                String error = "Server ERROR " +  t.getLocalizedMessage();
                Log.e(TAG, "onFailure " + error);
                HelperMethods.ShowSnackbar(PlayerActivity.context, PlayerView, error);
                call.cancel();
            }
        });

    }


}
