package osadchukdm.task4.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
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
    private String imagePath;
    private Uri directory;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private LoadImage loadImage;
    private Button makePhoto;
    private View bottomContainer;
    private LinearLayoutManager layoutManager;
    private GalleryData galleryData;
    private boolean isVisible=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settingApp(savedInstanceState);
    }

    private void settingApp(Bundle instanceState){
        ArrayList<String> data;
        init();
        findViews();
        makePhoto.setBackgroundResource(R.drawable.camera);

        if(instanceState!=null)
            isVisible = instanceState.getBoolean("settingVisible");

        if(isVisible)
            bottomContainer.setVisibility(View.VISIBLE);
        else
            bottomContainer.setVisibility(View.INVISIBLE);

        data=galleryData.getData();
        setAdapter(data);
        settingClickListener(photo,makePhoto,recyclerAdapter);

        if(instanceState==null && data.size()>0){
            imagePath=data.get(0);
            openImage(imagePath);
        }
    }

    private void init(){
        loadImage=new LoadImage();
        galleryData=new GalleryData();
        layoutManager = new LinearLayoutManager(this,Constants.HORIZONTAL,
                false);
    }

    private void findViews(){
        recyclerView = (RecyclerView) findViewById(R.id.galleryView);
        makePhoto = (Button) findViewById(R.id.buttonFoto);
        photo = (ImageView) findViewById(R.id.imagePreview);
        bottomContainer = findViewById(R.id.bottomContainer);
    }

    private void settingClickListener(ImageView mainPhoto,Button createPhoto,
                                      RecyclerAdapter galleryPhoto){
        mainPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomContainer(bottomContainer.getVisibility(), bottomContainer);
                }
        });

        createPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoto();
            }
        });

        galleryPhoto.SetOnItemClickListener(new RecyclerClick() {
            @Override
            public void onItemClick(String positionImage) {
                imagePath = positionImage;
                openImage(imagePath);
            }
        });
    }

    private void showBottomContainer(final int show, View animationView) {
        if(show!=View.VISIBLE){
            animationView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.visible));
            animationView.setVisibility(View.VISIBLE);
            isVisible=true;
        }  else{
            animationView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.invis));
            animationView.setVisibility(View.INVISIBLE);
            isVisible=false;
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        imagePath = savedInstanceState.getString("key");

        if(imagePath!=null)
            openImage(imagePath);

        try{
            directory = Uri.parse(savedInstanceState.getString("resultImage"));
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("key", imagePath);
        outState.putBoolean("settingVisible",isVisible);
        if(directory!=null)
            outState.putString("resultImage", directory.toString());
    }

    private void setAdapter(final ArrayList<String> list){

        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new RecyclerAdapter(list,loadImage,this);
        recyclerView.setAdapter(recyclerAdapter);

    }

    private void makePhoto(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        directory = galleryData.generateNewFileName();
        //imagePath=directory.toString();
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, directory);
        startActivityForResult(cameraIntent,Constants.CAMERA_RESULT);
    }

    private void openImage(String  path){
        loadImage.loadImage(photo,Uri.parse(path));
        photo.startAnimation(AnimationUtils.loadAnimation(this,R.anim.res));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {

        if(requestCode == Constants.CAMERA_RESULT && resultCode == RESULT_OK) {
            imagePath=directory.toString();
            openImage(imagePath);
            layoutManager.scrollToPosition(recyclerAdapter.getItemCount()-1);
        }
    }

}