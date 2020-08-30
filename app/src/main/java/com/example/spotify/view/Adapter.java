package com.example.spotify.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotify.R;
import com.example.spotify.databinding.ItemBinding;
import com.example.spotify.model.Song;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context mContext;
    private List<Song> mListSong;
    private IsetOnClickListener mClickListener;

    public Adapter(Context mContext, IsetOnClickListener listener) {
        this.mContext = mContext;
        mClickListener = listener;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBinding binding = ItemBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        Song song = mListSong.get(position);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onClickListener(holder.getAdapterPosition());
            }
        });
        holder.nameSong.setText(song.getName());
        loadImage(holder, position);
    }

    private void loadImage(Adapter.ViewHolder holder , int position){
        Song song = mListSong.get(position);
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(song.getPath());
            byte[] artBytes = mmr.getEmbeddedPicture();
            if (artBytes != null) {
                Bitmap bm = BitmapFactory.decodeByteArray(artBytes, 0, artBytes.length);
                holder.imageSong.setImageBitmap(bm);
            } else {
                holder.imageSong.setImageResource(R.drawable.ic_music);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return mListSong == null ? 0 : mListSong.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout container;
        ImageView imageSong;
        TextView nameSong;

        public ViewHolder(@NonNull ItemBinding itemView) {
            super(itemView.getRoot());
            container = itemView.container;
            imageSong = itemView.imgSong;
            nameSong = itemView.songName;
        }
    }

    public void setData(List<Song> listSong){
        mListSong = listSong;
        notifyDataSetChanged();
    }

}
