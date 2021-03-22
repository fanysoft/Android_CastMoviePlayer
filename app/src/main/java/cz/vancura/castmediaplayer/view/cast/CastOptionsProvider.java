package cz.vancura.castmediaplayer.view.cast;

import android.content.Context;

import com.google.android.gms.cast.framework.CastOptions;
import com.google.android.gms.cast.framework.OptionsProvider;
import com.google.android.gms.cast.framework.SessionProvider;

import java.util.List;

import cz.vancura.castmediaplayer.R;

/*
Cast class - registered in Manifest
source : google docs
 */

public class CastOptionsProvider implements OptionsProvider {


    @Override
    public CastOptions getCastOptions(Context context) {
        return new CastOptions.Builder()
                .setReceiverApplicationId(context.getString(R.string.cast_app_id))
                .build();
    }
    @Override

    public List<SessionProvider> getAdditionalSessionProviders(Context context) {
        return null;
    }
}