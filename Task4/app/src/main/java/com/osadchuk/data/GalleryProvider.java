package com.osadchuk.data;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryProvider {

    private final String galleryFolderName = "gallery";

    private Context context;

    public GalleryProvider(Context context) {
        this.context = context;
    }

    public void destroy() {
        context = null;
    }

    private File getGalleryFolderPath() {
        File cacheFolder = context.getExternalFilesDir(null);
        if (cacheFolder == null) {
            cacheFolder = context.getCacheDir();
        }

        File imageCacheFolder = new File(cacheFolder, galleryFolderName);
        if (!imageCacheFolder.exists()) {
            imageCacheFolder.mkdirs();
        }
        return imageCacheFolder;
    }

    public List<Uri> getFiles() {
        List<Uri> files = new ArrayList<>();
        File cacheFolder = getGalleryFolderPath();
        File[] allFiles = cacheFolder.listFiles();

        for (int i = 0; i < allFiles.length; i++) {
            File file = allFiles[i];
            if (file.isFile())
                files.add(Uri.fromFile(file));
        }
        return files;
    }

    public File generateNewFileName() {
        File cacheFolder = getGalleryFolderPath();
        File file = new File(cacheFolder, String.valueOf(System.nanoTime()) + ".jpeg");
        return file;
    }

}
