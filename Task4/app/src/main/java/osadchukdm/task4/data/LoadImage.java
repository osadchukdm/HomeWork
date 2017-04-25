package osadchukdm.task4.data;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;

import osadchukdm.task4.constants.Constants;

public class LoadImage {

    private Handler handler;

    public LoadImage() {
        handler = new Handler();
    }

    public void loadImage(final ImageView view, final Uri uri) {
        Thread loadTask = new Thread(new Runnable() {
            @Override
            public void run() {

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(uri.getPath(), options);
                options.inSampleSize = calculateInSampleSize(options, options.outWidth/Constants.RESIZE_IMAGE,
                        options.outHeight/Constants.RESIZE_IMAGE);
                options.inJustDecodeBounds = false;
                Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath(), options);
                try {
                    bitmap = rotateImageIfRequired(bitmap,uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                final Bitmap finalBitmap = bitmap;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (view != null)
                            view.setImageBitmap(finalBitmap);
                    }
                });
            }
        });
        loadTask.start();
    }


    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth /
                    inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage)
            throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(),
                img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }
}


