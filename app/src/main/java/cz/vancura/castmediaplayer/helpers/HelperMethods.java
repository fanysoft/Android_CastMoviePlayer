package cz.vancura.castmediaplayer.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        Log.d(TAG, "IsOnline returns " + isConnected);
        return isConnected;

    }

    // GUI - show SnackBar
    public void ShowSnackbar(Context context, View view, String text){

        final Snackbar mySnackbar = Snackbar.make(view, text, Snackbar.LENGTH_INDEFINITE);
        mySnackbar
                .setActionTextColor(context.getResources().getColor(R.color.accent))
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
