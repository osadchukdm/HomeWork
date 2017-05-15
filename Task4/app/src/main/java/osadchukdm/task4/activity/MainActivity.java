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

import java.io.File;
import java.util.ArrayList;
import android.support.v7.widget.RecyclerView;
import osadchukdm.task4.adapter.RecyclerAdapter;
import osadchukdm.task4.interfaces.RecyclerClick;
import osadchukdm.task4.R;
import osadchukdm.task4.constants.Constants;
import osadchukdm.task4.data.GalleryData;
import osadchukdm.task4.data.LoadImage;


public class MainActivity extends AppCompatActivity{

    private ArrayList<String> adapterData;
    private ImageView mainImage;
    private Uri imagePath;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private LoadImage loadMainImage;
    private Button makePhoto;
    private View viewContainer;
    private LinearLayoutManager layoutManager;
    private GalleryData galleryData;
    private boolean isVisible=false;
    private boolean rotate=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settingApp(savedInstanceState);
    }

    private void settingApp(Bundle instanceState){
        init();
        findViews();
        makePhoto.setBackgroundResource(R.drawable.camera);

        if(instanceState!=null)
            isVisible = instanceState.getBoolean("settingVisible");

        if(isVisible)
            viewContainer.setVisibility(View.VISIBLE);
        else
            viewContainer.setVisibility(View.INVISIBLE);
        adapterData =galleryData.getData();
        setAdapter(adapterData);
        settingClickListener(mainImage,makePhoto,recyclerAdapter);

        if(instanceState==null && adapterData.size()>0){
            imagePath=Uri.parse(adapterData.get(0));
            openImage(imagePath);
        }
    }

    private void init(){
        loadMainImage =new LoadImage();
        galleryData=new GalleryData();
        layoutManager = new LinearLayoutManager(this,Constants.HORIZONTAL,
                false);
    }

    private void findViews(){
        recyclerView = (RecyclerView) findViewById(R.id.galleryView);
        makePhoto = (Button) findViewById(R.id.buttonFoto);
        mainImage = (ImageView) findViewById(R.id.imagePreview);
        viewContainer = findViewById(R.id.container);
    }

    private void settingClickListener(ImageView mainPhoto,Button createPhoto,
                                      RecyclerAdapter galleryPhoto){
        mainPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContainer(viewContainer.getVisibility(), viewContainer);
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
                imagePath = Uri.parse(positionImage);
                openImage(imagePath);
            }
        });
    }

    private void showContainer(final int show, View animationView) {
        if(show!=View.VISIBLE){
            animationView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_recycler));
            animationView.setVisibility(View.VISIBLE);
            isVisible=true;
        }  else{
            animationView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.hide_recycler));
            animationView.setVisibility(View.INVISIBLE);
            isVisible=false;
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        imagePath = Uri.parse(savedInstanceState.getString("pathMainImage"));
        if(imagePath!=null)
            openImage(imagePath);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("pathMainImage", imagePath.toString());
        outState.putBoolean("settingVisible",isVisible);

    }

    private void setAdapter(final ArrayList<String> list){

        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new RecyclerAdapter(list, loadMainImage,this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();

    }

    private void makePhoto(){
        rotate=false;
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imagePath = galleryData.generateNewFileName();
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imagePath);
        startActivityForResult(cameraIntent,Constants.CAMERA_RESULT);
    }

    private void openImage(Uri path){
        loadMainImage.loadImage(mainImage,path);
        mainImage.startAnimation(AnimationUtils.loadAnimation(this,R.anim.show_main_photo));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if(requestCode == Constants.CAMERA_RESULT && resultCode == RESULT_OK) {
            if(!rotate)
                adapterData.add(imagePath.toString());
            recyclerAdapter.notifyDataSetChanged();
            openImage(imagePath);
            layoutManager.scrollToPosition(recyclerAdapter.getItemCount()-1);
        }
    }
}