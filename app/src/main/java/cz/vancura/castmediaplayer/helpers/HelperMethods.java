package cz.vancura.castmediaplayer.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import cz.vancura.castmediaplayer.R;

/*
Various Java classes used in whole app
could be Singleton
 */

public class HelperMethods {

    private static String TAG = "myTAG-HelperMethods";

    // Is online - check network connection
    public boolean IsOnline(Context context){

        boolean isConnected;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Anndroid 6 and better
            Network nw = connectivityManager.getActiveNetwork();
            if (nw == null) isConnected=false;

            NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
            isConnected = actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
        } else {
            // getActiveNetworkInfo() is deprecated in API 29 Android 10
            // https://stackoverflow.com/questions/57277759/getactivenetworkinfo-is-deprecated-in-api-29
            NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
            isConnected = nwInfo != null && nwInfo.isConnected();
        }

        Log.d(TAG, "IsOnline returns " + isConnected);
        return isConnected;

    }

    // GUI - show SnackBar
    public void ShowSnackbar(Context context, View view, String text){

        final Snackbar mySnackbar = Snackbar.make(view, text, Snackbar.LENGTH_INDEFINITE);
        mySnackbar
                .setActionTextColor(ContextCompat.getColor(context, R.color.accent))
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mySnackbar.dismiss();
                    }
                });
        mySnackbar.setActionTextColor(ContextCompat.getColor(context, R.color.accent));
        mySnackbar.show();

    }


}
