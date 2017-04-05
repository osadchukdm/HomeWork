package com.osadchuk.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.osadchuk.R;
import com.osadchuk.adapter.GalleryAdapter;
import com.osadchuk.data.GalleryProvider;
import com.osadchuk.data.ImageLoader;
import com.osadchuk.interfaces.IClickListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int CAMERA_REQUEST = 1;
    private final int BOTTOM_ANIMATION_DURATION_INTERVAL = 10000;
    private final int BOTTOM_ANIMATION_DURATION = 600;

    private RecyclerView galleryRecyclerView;
    private ImageView imagePreview;
    private GalleryProvider galleryProvider;
    private Uri currentCapturedPhotoPath;
    private GalleryAdapter adapter;
    private LinearLayoutManager layoutManager;

    private ImageLoader imageLoader;

    private boolean isBottomAnimation;

    private Handler bottomAnimationHandler;
    private Runnable hideRunnable;
    private View bottomContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        galleryRecyclerView = (RecyclerView) findViewById(R.id.galleryView);
        imagePreview = (ImageView) findViewById(R.id.imagePreview);
        bottomContainer = findViewById(R.id.bottomContainer);

        Button makePhoto=(Button) findViewById(R.id.buttonTakePhoto);
        makePhoto.setBackgroundResource(R.drawable.camera);

        makePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
                queueHideRunnable();
            }
        });

        bottomAnimationHandler = new Handler();
        hideRunnable = new Runnable() {
            @Override
            public void run() {
                showBottomContainer(false);
            }
        };

        imagePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomContainer(bottomContainer.getVisibility() == View.INVISIBLE);
                queueHideRunnable();
            }
        });

        imageLoader = new ImageLoader();

        galleryProvider = new GalleryProvider(this);
        initPreviewList(galleryProvider.getFiles());
    }

    private void queueHideRunnable() {
        bottomAnimationHandler.removeCallbacks(hideRunnable);

        if (bottomContainer.getVisibility() == View.VISIBLE)
            bottomAnimationHandler.postDelayed(hideRunnable, BOTTOM_ANIMATION_DURATION_INTERVAL);
    }

    private void showBottomContainer(final boolean show) {

        if (isBottomAnimation)
            return;

        isBottomAnimation = true;

        float startPos = show ? (float) bottomContainer.getMeasuredHeight() : 0f;
        float endPos = show ? 0f : (float) bottomContainer.getMeasuredHeight();

        ObjectAnimator translateY = ObjectAnimator.ofFloat(bottomContainer, "y", startPos, endPos);
        translateY.setDuration(BOTTOM_ANIMATION_DURATION);
        translateY.setInterpolator(new LinearInterpolator());
        translateY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                bottomContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isBottomAnimation = false;
                bottomContainer.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        translateY.start();
    }

    private void openPreviewImage(Uri uri) {
        imageLoader.displayPreview(imagePreview, uri);
    }

    private void initPreviewList(List<Uri> uries) {
        layoutManager = new LinearLayoutManager(this, 0, false);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        galleryRecyclerView.setLayoutManager(layoutManager);
        adapter = new GalleryAdapter(this, imageLoader, uries);
        galleryRecyclerView.setAdapter(adapter);

        // select first image
        if (uries.size() > 0) {
            openPreviewImage(uries.get(0));
        }

        // hide bottom container
        bottomContainer.setVisibility(View.INVISIBLE);

        adapter.setItemClickListener(new IClickListener() {
            @Override
            public void onThumbClick(Uri uri) {
                openPreviewImage(uri);
                queueHideRunnable();
            }
        });
    }

    private void addPhotoToGalleryList(Uri uri) {
        adapter.addImage(uri);
        openPreviewImage(uri);
    }

    @Override
    protected void onDestroy() {
        imageLoader.destroy();
        galleryProvider.destroy();
        bottomAnimationHandler.removeCallbacks(null);
        super.onDestroy();
    }

    private void takePhoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        currentCapturedPhotoPath = Uri.fromFile(galleryProvider.generateNewFileName());
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentCapturedPhotoPath);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // get uri captured of image
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            addPhotoToGalleryList(currentCapturedPhotoPath);
        }
    }
}
