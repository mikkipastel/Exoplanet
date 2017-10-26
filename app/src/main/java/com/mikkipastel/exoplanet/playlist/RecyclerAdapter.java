package com.mikkipastel.exoplanet.playlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mikkipastel.exoplanet.PlanetAssistance;
import com.mikkipastel.exoplanet.R;
import com.mikkipastel.exoplanet.playlist.service.MusicList;

import java.util.List;

import static java.security.AccessController.getContext;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ItemListener mListener;
    Context mContext;
    List mItems;

    public RecyclerAdapter(ItemListener listener, Context context, List items){
        this.mListener = listener;
        this.mContext = context;
        this.mItems = items;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_track, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MusicList list = (MusicList) mItems.get(position);

        // set cover and track name
        ViewHolder mHolder = (ViewHolder) holder;
        PlanetAssistance.setImageUrl(mContext, list.getCover(), mHolder.cover);
        mHolder.name.setText(list.getFilename());
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(v, position);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView cover;
        TextView name;

        public ViewHolder(View view) {
            super(view);
            cover = view.findViewById(R.id.imageTrack);
            name = view.findViewById(R.id.nameTrack);
        }
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

}
