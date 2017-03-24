package osadchukdm.task4.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.File;
import java.util.ArrayList;
import android.support.v7.widget.RecyclerView;
import com.bumptech.glide.Glide;
import osadchukdm.task4.Adaptor.RecyclerAdapter;
import osadchukdm.task4.Interface.RecyclerClick;
import osadchukdm.task4.R;

public class MainActivity extends AppCompatActivity{

    private static final int CAMERA_RESULT = 0;
    private final int HIGHT=800;
    private final int WIGHT=800;
    private final int HORIZONTAL=0;
    private final int SIZE=2;
    private final int VISIBLE=1;


    ImageView photo;
    String imagePath;
    Uri directory;
    File newFile;
    RecyclerView recyclerView;
    RecyclerAdapter mAdapter;
    Context context;

    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context=getApplicationContext();

        photo = (ImageView) findViewById(R.id.ivPhoto);
        recyclerView = (RecyclerView) findViewById(R.id.rec);

        initAdaptor(getDataSet());

        final Button makePhoto = (Button) findViewById(R.id.buttonFoto);
        makePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoto();
            }
        });

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
        if(mDataSet!=null) {
            mLayoutManager = new LinearLayoutManager(this, HORIZONTAL, false);
            recyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new RecyclerAdapter(mDataSet, context);
            recyclerView.setAdapter(mAdapter);

            mAdapter.SetOnItemClickListener(new RecyclerClick() {
                @Override
                public void onItemClick(int position) {
                    paint(mDataSet.get(position));
                    imagePath = mDataSet.get(position);

                }
            });
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
    }

    private void checkFirstLastItems() {

        int firstAlphaPosition = mLayoutManager.findFirstVisibleItemPosition();
        int lastAlphaPosition = mLayoutManager.findLastVisibleItemPosition();

        View view = mLayoutManager.findViewByPosition(firstAlphaPosition);
        int locations[] = new int[SIZE];
        view.getLocationOnScreen(locations);
        float alpha = 1 - Math.abs((float) locations[0] /
                view.getMeasuredWidth());
        Log.d("+++", String.valueOf(alpha));
        view.setAlpha(alpha);

        view = mLayoutManager.findViewByPosition(lastAlphaPosition);
        locations = new int[SIZE];
        view.getLocationOnScreen(locations);
        alpha = Math.abs((float)(recyclerView.getMeasuredWidth() -
                locations[0]) / view.getMeasuredWidth());
        Log.d("+++", "va "+String.valueOf(alpha));
        view.setAlpha(alpha);
        checkCompleteItem(view);

    }

    private void checkCompleteItem(View completeView){
        int firstCompletePosition = mLayoutManager.findFirstCompletelyVisibleItemPosition();
        int lastCompletePosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
        for (int i = firstCompletePosition; i <= lastCompletePosition; i++) {
            completeView = mLayoutManager.findViewByPosition(i);
            completeView.setAlpha(VISIBLE);
        }
    }

    private void makePhoto(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        directory = generateFileUri();
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, directory);
        startActivityForResult(cameraIntent, CAMERA_RESULT);
    }

    private void paint(String  path){

        Glide.with(context).load(path).
                override(HIGHT,WIGHT).
                centerCrop().into(photo);
    }

    private ArrayList<String> getDataSet() {
                ArrayList<String> data= new ArrayList<String>();
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
                    return null;
                }
        return data;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("+++","resume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("+++","destroy");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {

        if(requestCode == CAMERA_RESULT && resultCode == RESULT_OK) {
            paint(directory.toString());
            imagePath = directory.toString();
            mAdapter.addImage(directory.toString());
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