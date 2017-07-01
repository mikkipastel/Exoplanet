package com.mikkipastel.exoplanet.playlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikkipastel.exoplanet.R;

/**
 * Created by acer on 6/29/2017.
 */

public class listAdapter extends BaseAdapter {

    Context mContext;
    int[] mCover;
    String[] mSongName;

    public listAdapter(Context context, int[] cover, String[] songname){
        this.mContext = context;
        this.mCover = cover;
        this.mSongName = songname;
    }

    @Override
    public int getCount() {
        return mSongName.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null)
            view = mInflater.inflate(R.layout.listview_track, viewGroup, false);

        // set cover and track name
        ImageView cover = (ImageView)view.findViewById(R.id.imageTrack);
        cover.setImageResource(mCover[i]);

        TextView name = (TextView)view.findViewById(R.id.nameTrack);
        name.setText(mSongName[i]);

        return view;
    }
}
