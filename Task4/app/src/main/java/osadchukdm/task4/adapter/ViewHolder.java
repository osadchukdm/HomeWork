package osadchukdm.task4.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import osadchukdm.task4.R;
import osadchukdm.task4.constants.Constants;
import osadchukdm.task4.data.LoadImage;

public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView photoSmall;

        public ViewHolder(View itemView) {
            super(itemView);
            photoSmall = (ImageView) itemView.findViewById(R.id.photoSmall);
        }

        public void loadImage(LoadImage downloadImage,String imagePath) {

            downloadImage.loadImage(photoSmall, Uri.parse(imagePath),
                    Constants.HEIGHT_SMALL,Constants.WIDTH_SMALL);
        }

}
