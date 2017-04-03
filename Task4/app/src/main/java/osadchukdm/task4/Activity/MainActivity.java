package osadchukdm.task4.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.support.v7.widget.RecyclerView;

import osadchukdm.task4.Adapter.RecyclerAdapter;
import osadchukdm.task4.Interface.RecyclerClick;
import osadchukdm.task4.R;
import osadchukdm.task4.constants.Constants;
import osadchukdm.task4.loadImage.LoadImage;


public class MainActivity extends AppCompatActivity{

    ImageView photo;
    String imagePath;
    Uri directory=null;
    File newFile;
    RecyclerView recyclerView;
    RecyclerAdapter mAdapter;
    ArrayList<String> data;
    LoadImage loadImage;

    private final int CHECK_INTERVAL = 10 * 1000;

    private LinearLayoutManager mLayoutManager;
     Timer timer;
     TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadImage=new LoadImage();

        recyclerView = (RecyclerView) findViewById(R.id.rec);
        final Button makePhoto = (Button) findViewById(R.id.buttonFoto);
        makePhoto.setBackgroundResource(R.drawable.camera);


        photo = (ImageView) findViewById(R.id.ivPhoto);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setAnimation(recyclerView);
                setAnimation2(makePhoto);

            }
        });


        initAdaptor(getDataSet());
        try {
            paint(data.get(0).toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        makePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoto();
            }
        });

    }

    public  void setAnimation2(View view){
        final Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.translate);
        view.startAnimation(animation2);
        view.setVisibility(View.VISIBLE);
    }

    public  void setAnimation(View view){
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        view.startAnimation(animation);
        view.setAlpha(View.FOCUSABLES_TOUCH_MODE);
    }
    public  void setAnimation3(View view){
        final Animation animation3 = AnimationUtils.loadAnimation(this, R.anim.al);
        view.startAnimation(animation3);
        view.setVisibility(View.INVISIBLE);
    }

    public  void setAnimation4(View view){
        final Animation animation4 = AnimationUtils.loadAnimation(this, R.anim.tr);
        view.startAnimation(animation4);
        view.setAlpha(0);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        imagePath = savedInstanceState.getString("key");
        paint(imagePath);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("key", imagePath);
    }

    private void initAdaptor(final ArrayList<String> mDataSet){
            mLayoutManager = new LinearLayoutManager(this,Constants.HORIZONTAL, false);
            recyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new RecyclerAdapter(mDataSet);
            recyclerView.setAdapter(mAdapter);

            mAdapter.SetOnItemClickListener(new RecyclerClick() {
                @Override
                public void onItemClick(int position) {
                    paint(mDataSet.get(position));
                    imagePath = mDataSet.get(position);

                }
            });
    }
           /* recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    checkFirstLastItems();
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    checkFirstLastItems();
                }
            });
    }

    private void checkFirstLastItems() {
    try {
        int firstAlphaPosition = mLayoutManager.findFirstVisibleItemPosition();
        int lastAlphaPosition = mLayoutManager.findLastVisibleItemPosition();

        View view = mLayoutManager.findViewByPosition(firstAlphaPosition);
        int locations[] = new int[Constants.SIZE];
        view.getLocationOnScreen(locations);
        float alpha = 1 - Math.abs((float) locations[0] /
            view.getMeasuredWidth());
        view.setAlpha(alpha);

        view = mLayoutManager.findViewByPosition(lastAlphaPosition);
        locations = new int[Constants.SIZE];
        view.getLocationOnScreen(locations);
        alpha = Math.abs((float) (recyclerView.getMeasuredWidth() -
            locations[0]) / view.getMeasuredWidth());
        view.setAlpha(alpha);
        checkCompleteItem(view);
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
}

    private void checkCompleteItem(View completeView){

        int firstCompletePosition = mLayoutManager.findFirstCompletelyVisibleItemPosition();
        int lastCompletePosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
        for (int i = firstCompletePosition; i <= lastCompletePosition; i++) {
            completeView = mLayoutManager.findViewByPosition(i);
            completeView.setAlpha(Constants.VISIBLE);
        }
    }
*/
    private void makePhoto(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        directory = generateFileUri();
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, directory);
        startActivityForResult(cameraIntent,Constants.CAMERA_RESULT);
    }

    private void paint(String  path){

        photo.setImageBitmap(loadImage.LoadImage(path,Constants.IMAGE_BIG));


    }

    private ArrayList<String> getDataSet() {
                data= new ArrayList<String>();
                File[] fList;
                try {
                    File F = new File(Environment.getExternalStorageDirectory(),
                            getString(R.string.name_path));

                    fList = F.listFiles();

                    for (int i = 0; i < fList.length; i++) {
                        if (fList[i].isFile())
                            data.add(fList[i].getPath().toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        return data;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {

        if(requestCode == Constants.CAMERA_RESULT && resultCode == RESULT_OK) {
            paint(directory.toString());
            imagePath = directory.toString();
            mAdapter.addImage(directory.toString());
            mLayoutManager.scrollToPosition(mAdapter.getItemCount() - 1);
            //checkFirstLastItems();
        }
    }

    private Uri generateFileUri() {

        if (!Environment.getExternalStorageState().equals(Environment.
                MEDIA_MOUNTED))
            return null;

        File path = new File(Environment.getExternalStorageDirectory(),
                getString(R.string.name_path));
        if (!path.exists()) {
            if (!path.mkdirs()) {
                return null;
            }
        }

        String timeStamp = String.valueOf(System.currentTimeMillis());
        newFile = new File(path.getPath() + File.separator + timeStamp + ".jpg");
        return Uri.fromFile(newFile);

    }
}