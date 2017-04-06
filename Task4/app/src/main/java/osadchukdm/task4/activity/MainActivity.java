package osadchukdm.task4.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import android.support.v7.widget.RecyclerView;
import osadchukdm.task4.adapter.RecyclerAdapter;
import osadchukdm.task4.interfaces.RecyclerClick;
import osadchukdm.task4.R;
import osadchukdm.task4.constants.Constants;
import osadchukdm.task4.data.GalleryData;
import osadchukdm.task4.data.LoadImage;


public class MainActivity extends AppCompatActivity{

    private ImageView photo;
    private String imagePath=null;
    private Uri directory=null;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private ArrayList<String> data;
    private LoadImage loadImage;
    private Button makePhoto;
    private boolean isBottomAnimation;
    private Handler bottomAnimationHandler;
    private Runnable hideRunnable;
    private View bottomContainer;
    private LinearLayoutManager layoutManager;
    private GalleryData galleryData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        recyclerView = (RecyclerView) findViewById(R.id.galleryView);
        makePhoto = (Button) findViewById(R.id.buttonFoto);
        makePhoto.setBackgroundResource(R.drawable.camera);
        photo = (ImageView) findViewById(R.id.imagePreview);
        bottomContainer = findViewById(R.id.bottomContainer);

        bottomContainer.setVisibility(View.INVISIBLE);

        hideRunnable = new Runnable() {
            @Override
            public void run() {
                showBottomContainer(false);
            }
        };

        data=galleryData.getData();
        setAdapter(data);
        setClickListener();

        if(savedInstanceState==null && data.size()>0){
            imagePath=data.get(0);
            openImage(imagePath);
        }
    }

    private void init(){
        loadImage=new LoadImage();
        galleryData=new GalleryData();
        bottomAnimationHandler = new Handler();
        layoutManager = new LinearLayoutManager(this,Constants.HORIZONTAL,
                false);
    }

    private void setClickListener(){

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomContainer(bottomContainer.getVisibility() ==
                        View.INVISIBLE);
                queueHideRunnable();

            }
        });

        makePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoto();
                queueHideRunnable();
            }
        });

        recyclerAdapter.SetOnItemClickListener(new RecyclerClick() {
            @Override
            public void onItemClick(String positionImage) {
                openImage(positionImage);
                imagePath = positionImage;
                queueHideRunnable();
            }
        });

    }
    private void queueHideRunnable() {
        bottomAnimationHandler.removeCallbacks(hideRunnable);

        if (bottomContainer.getVisibility() == View.VISIBLE)
            bottomAnimationHandler.postDelayed(hideRunnable,
                    Constants.BOTTOM_ANIMATION_DURATION_INTERVAL);
    }

    private void showBottomContainer(final boolean show) {

        if (isBottomAnimation)
            return;

        isBottomAnimation = true;

        float startPos = show ? (float) bottomContainer.getMeasuredHeight() : 0f;
        float endPos = show ? 0f : (float) bottomContainer.getMeasuredHeight();

        ObjectAnimator translateY = ObjectAnimator.ofFloat(bottomContainer, "y",
                startPos, endPos);
        translateY.setDuration(Constants.BOTTOM_ANIMATION_DURATION);
        translateY.setInterpolator(new LinearInterpolator());
        translateY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                bottomContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isBottomAnimation = false;
                bottomContainer.setVisibility(show ? View.VISIBLE :
                        View.INVISIBLE);
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

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        imagePath = savedInstanceState.getString("key");

        if(imagePath!=null)
            openImage(imagePath);

        try{
            directory = Uri.parse(savedInstanceState.getString("resultImage"));
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("key", imagePath);

        if(directory!=null)
            outState.putString("resultImage", directory.toString());
    }

    private void setAdapter(final ArrayList<String> list){
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new RecyclerAdapter(list,loadImage);
        recyclerView.setAdapter(recyclerAdapter);

    }

    private void makePhoto(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        directory = galleryData.generateNewFileName();
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, directory);
        startActivityForResult(cameraIntent,Constants.CAMERA_RESULT);
    }

    private void openImage(String  path){
       loadImage.loadImage(photo,Uri.parse(path),Constants.HEIGHT_MAIN,
               Constants.WIDTH_MAIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {

        if(requestCode == Constants.CAMERA_RESULT && resultCode == RESULT_OK) {
            openImage(directory.toString());
            imagePath = directory.toString();
            recyclerAdapter.addImage(directory.toString());

        }
    }
}