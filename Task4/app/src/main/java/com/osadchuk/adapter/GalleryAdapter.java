package com.osadchuk.adapter;


import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.osadchuk.R;
import com.osadchuk.adapter.holder.GalleryHolder;
import com.osadchuk.data.ImageLoader;
import com.osadchuk.interfaces.IClickListener;

import java.util.List;


public class GalleryAdapter extends RecyclerView.Adapter<GalleryHolder> {

    private List<Uri> files;
    private LayoutInflater layoutInflater;
    private IClickListener clickItemListener;
    private ImageLoader imageLoader;


    public void setItemClickListener(IClickListener listener) {
        clickItemListener = listener;
    }

    public GalleryAdapter(Context context, ImageLoader imageLoader, List<Uri> files) {
        this.files = files;
        this.imageLoader = imageLoader;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public GalleryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_gallery_thumb, parent, false);
        GalleryHolder holder = new GalleryHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final GalleryHolder holder, final int position) {
        holder.refresh(imageLoader, files.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickItemListener != null) {
                    clickItemListener.onThumbClick(files.get(position));
                }
            }
        });
    }

    public void addImage(Uri uri) {
        files.add(0, uri);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return files.size();
    }


}
