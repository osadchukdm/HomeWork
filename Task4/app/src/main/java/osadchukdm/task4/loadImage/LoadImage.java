package osadchukdm.task4.loadImage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class LoadImage{
    public Bitmap LoadImage(String imagePath,int resize) {

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        int mPhotoWidth = bitmap.getWidth()/resize;
        int mPhotoHeight = bitmap.getHeight()/resize;
        Bitmap bmHalf = Bitmap.createScaledBitmap(
                bitmap, mPhotoHeight, mPhotoWidth, false);

        return bmHalf;
    }
}
