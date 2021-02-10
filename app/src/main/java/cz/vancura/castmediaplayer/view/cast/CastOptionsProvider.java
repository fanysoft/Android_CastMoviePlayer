package cz.vancura.castmediaplayer.view.cast;

import android.content.Context;

import com.google.android.gms.cast.framework.CastOptions;
import com.google.android.gms.cast.framework.OptionsProvider;
import com.google.android.gms.cast.framework.SessionProvider;

import java.util.List;

import cz.vancura.castmediaplayer.R;

/*

Cast class - registered in Manifest
 */

public class CastOptionsProvider implements OptionsProvider {


    @Override
    public CastOptions getCastOptions(Context context) {
        CastOptions castOptions = new CastOptions.Builder()
                .setReceiverApplicationId(context.getString(R.string.cast_app_id))
                .build();
        return castOptions;
    }
    @Override

    public List<SessionProvider> getAdditionalSessionProviders(Context context) {
        return null;
    }
}