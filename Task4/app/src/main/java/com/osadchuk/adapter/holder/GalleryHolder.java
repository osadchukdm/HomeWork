package com.osadchuk.adapter.holder;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.osadchuk.R;
import com.osadchuk.data.ImageLoader;

public class GalleryHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;

    public GalleryHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.imageThumb);
    }

    public void refresh(ImageLoader imageLoader, Uri imageUri) {
        imageLoader.displayThumb(imageView, imageUri);
    }

}
