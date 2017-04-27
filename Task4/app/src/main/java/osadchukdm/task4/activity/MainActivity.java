package osadchukdm.task4.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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

    private ImageView mainImage;
    private String imagePath;
    private Uri directory;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private LoadImage loadMainImage;
    private Button makePhoto;
    private View viewContainer;
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
            viewContainer.setVisibility(View.VISIBLE);
        else
            viewContainer.setVisibility(View.INVISIBLE);

        data=galleryData.getData();
        setAdapter(data);
        settingClickListener(mainImage,makePhoto,recyclerAdapter);

        if(instanceState==null && data.size()>0){
            imagePath=data.get(0);
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
                showBottomContainer(viewContainer.getVisibility(), viewContainer);
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
        imagePath = savedInstanceState.getString("pathMainImage");

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

        outState.putString("pathMainImage", imagePath);
        outState.putBoolean("settingVisible",isVisible);
        if(directory!=null)
            outState.putString("resultImage", directory.toString());
    }

    private void setAdapter(final ArrayList<String> list){

        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new RecyclerAdapter(list, loadMainImage,this);
        recyclerView.setAdapter(recyclerAdapter);

    }

    private void makePhoto(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        directory = galleryData.generateNewFileName();
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, directory);
        startActivityForResult(cameraIntent,Constants.CAMERA_RESULT);
    }

    private void openImage(String  path){
        loadMainImage.loadImage(mainImage,Uri.parse(path));
        mainImage.startAnimation(AnimationUtils.loadAnimation(this,R.anim.show_main_photo));
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