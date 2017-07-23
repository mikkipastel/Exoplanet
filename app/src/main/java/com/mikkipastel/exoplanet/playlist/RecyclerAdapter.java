package com.mikkipastel.exoplanet.playlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikkipastel.exoplanet.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    int[] mCover;
    String[] mSongName;

    ItemListener mListener;

    protected int mItemCount = 0;

    public RecyclerAdapter(ItemListener listener, Context context, int[] cover, String[] songname){
        this.mContext = context;
        this.mCover = cover;
        this.mSongName = songname;

        this.mListener = listener;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_track, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // set cover and track name
        ViewHolder mHolder = (ViewHolder) holder;
        mHolder.cover.setImageResource(mCover[position]);
        mHolder.name.setText(mSongName[position]);
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
            cover = (ImageView)view.findViewById(R.id.imageTrack);
            name = (TextView)view.findViewById(R.id.nameTrack);
        }
    }

    @Override
    public int getItemCount() {
        int itemCount = mItemCount;
        if (itemCount == 0) {
            itemCount = mSongName.length;
        }
        return itemCount;
    }

}
