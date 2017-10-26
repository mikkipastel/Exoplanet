package com.mikkipastel.exoplanet;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class PlanetAssistance {

    public static void setImageUrl(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }
}
