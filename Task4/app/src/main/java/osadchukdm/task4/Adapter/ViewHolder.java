package osadchukdm.task4.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import osadchukdm.task4.R;
import osadchukdm.task4.constants.Constants;
import osadchukdm.task4.loadImage.LoadImage;

public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView photoSmall;

        public ViewHolder(View itemView) {
            super(itemView);
            photoSmall = (ImageView) itemView.findViewById(R.id.photoSmall);

        }

        public void loadImage(String imagePath) {

            LoadImage mLoadImage = new LoadImage();
            photoSmall.setImageBitmap(mLoadImage.LoadImage(imagePath,Constants.IMAGE_SMALL));


        }

}
