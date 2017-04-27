package osadchukdm.task4.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import osadchukdm.task4.R;
import osadchukdm.task4.data.LoadImage;

public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView galleryItem;


        public ViewHolder(View itemView) {
            super(itemView);
            galleryItem = (ImageView) itemView.findViewById(R.id.photoSmall);
        }

        public void loadImage(LoadImage downloadImage,String imagePath) {
            downloadImage.loadImage(galleryItem, Uri.parse(imagePath));
        }

}
