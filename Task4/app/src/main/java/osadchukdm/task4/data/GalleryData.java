package osadchukdm.task4.data;

import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import osadchukdm.task4.constants.Constants;

public class GalleryData {

    public File generateFilePath() {

        if (!Environment.getExternalStorageState().equals(Environment.
                MEDIA_MOUNTED))
            return null;
        File path = new File(Environment.getExternalStorageDirectory(),
                Constants.NAME_PATH);
        if (!path.exists())
            if (!path.mkdirs())
                return null;

        return path;
    }

    public ArrayList<String> getData() {
        ArrayList<String> data = new ArrayList<String>();
        File cacheFolder= generateFilePath();
        File[] fList = cacheFolder.listFiles();

        for (int i = 0; i < fList.length; i++) {
            if (fList[i].isFile())
                data.add(fList[i].getPath().toString());
        }
        return data;
    }

    public Uri generateNewFileName() {

        String timeStamp = String.valueOf(System.currentTimeMillis());
        File newFile = new File(generateFilePath().getPath() + File.separator +
                timeStamp + ".jpg");
        return Uri.fromFile(newFile);
    }
}
