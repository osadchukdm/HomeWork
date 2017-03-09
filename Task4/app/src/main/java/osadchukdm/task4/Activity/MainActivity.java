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
import android.widget.Toast;
import java.io.File;

import android.support.v7.widget.RecyclerView;

import osadchukdm.task4.Adaptor.RecyclerAdapter;
import osadchukdm.task4.R;


public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_RESULT = 0;

    ImageView photo;
    Uri directory;
    File newFile;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    String[] myDataset;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context=getApplicationContext();

        recyclerView=(RecyclerView)findViewById(R.id.rec);

        myDataset = getDataSet();
        bb();
        // если мы уверены, что изменения в контенте не изменят размер layout-а RecyclerView
        // передаем параметр true - это увеличивает производительность
      //  recyclerView.setHasFixedSize(true);

        // используем linear layout manager
      /*  mLayoutManager = new LinearLayoutManager(this,0,false);
        recyclerView.setLayoutManager(mLayoutManager);
        // создаем адаптер
        mAdapter = new RecyclerAdapter(myDataset,context);
        recyclerView.setAdapter(mAdapter);
*/
/*

        Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageView image=new ImageView(MainActivity.this);
                image.setImageURI(Uri.fromFile(newFile));
                image.setRotation(90);
                image.setMaxHeight(15);
                image.setMaxWidth(15);
                image.setScaleType(ImageView.ScaleType.MATRIX);
                //Bitmap createScaledBitmap=(image,15,15,false);
                //(Bitmap src,int dstWidth,int dstHeight,boolean filter)

                //android:scaleType="fitStart"
                //image.setBackgroundResource(filesArray[i]);
                recyclerView.addView(image);
            }
        });*/
        photo=(ImageView)findViewById(R.id.ivPhoto);
        Button makePhoto=(Button)findViewById(R.id.buttonFoto);
        makePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                directory = generateFileUri();
                if (directory == null) {
                    Toast.makeText(getApplicationContext(), "SD card not available", Toast.LENGTH_LONG).show();
                    return;
                }

                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, directory);

                startActivityForResult(cameraIntent,CAMERA_RESULT);
            }
        });

    }

    private void bb(){
        mLayoutManager = new LinearLayoutManager(this,0,false);
        recyclerView.setLayoutManager(mLayoutManager);
        // создаем адаптер
        mAdapter = new RecyclerAdapter(myDataset,context);
        recyclerView.setAdapter(mAdapter);
    }

    private String[] getDataSet() {
        String[] mDataSet = new String[100];
        File []fList;
        File F = new File(Environment.getExternalStorageDirectory(),
                "CameraTest");

        fList = F.listFiles();

        for(int i=0; i<fList.length; i++)
        {
            //Нужны только папки в место isFile() пишим isDirectory()
            if(fList[i].isFile())
                mDataSet[i]=(fList[i].getPath().toString());

        }
        return mDataSet;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        photo.setImageURI(directory);
        photo.setRotation(90);
    }

    private Uri generateFileUri() {

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return null;

        File path = new File (Environment.getExternalStorageDirectory(),
                "CameraTest");
        if (! path.exists()){
            if (! path.mkdirs()){
                return null;
            }
        }

        String timeStamp = String.valueOf(System.currentTimeMillis());
        newFile = new File(path.getPath() + File.separator + timeStamp + ".jpg");
        return Uri.fromFile(newFile);
    }

}