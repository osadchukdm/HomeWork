package osadchukdm.task4.Activity;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;

import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;

import android.support.v7.widget.RecyclerView;

import osadchukdm.task4.Adaptor.RecyclerAdapter;
import osadchukdm.task4.Interface.RecyclerClick;
import osadchukdm.task4.R;


public class MainActivity extends AppCompatActivity{
    private static final int CAMERA_RESULT = 0;

    ImageView photo;
    Uri directory;
    File newFile;
    RecyclerView recyclerView;
    RecyclerAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    ArrayList<String> mDataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDataSet = new ArrayList<String>();

        photo = (ImageView) findViewById(R.id.ivPhoto);

        getDataSet();

        recyclerView = (RecyclerView) findViewById(R.id.rec);

        mLayoutManager = new LinearLayoutManager(this, 0, false);
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter(mDataSet);
        recyclerView.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new RecyclerClick() {
            @Override
            public void onItemClick(View view, int position) {
                Bitmap map = BitmapFactory.decodeFile(mDataSet.get(position));

                photo.setRotation(90);
                photo.setImageBitmap(map);
            }
        });

        Button makePhoto = (Button) findViewById(R.id.buttonFoto);
        makePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                directory = generateFileUri();
                if (directory == null) {
                    Toast.makeText(getApplicationContext(), "SD card not available", Toast.LENGTH_LONG).show();
                    return;
                }

                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, directory);

                startActivityForResult(cameraIntent, CAMERA_RESULT);
            }
        });

    }

    private void getDataSet() {
        File[] fList;
        File F = new File(Environment.getExternalStorageDirectory(),
                "CameraTest");

        fList = F.listFiles();

        for (int i = 0; i < fList.length; i++) {
            //Нужны только папки в место isFile() пишим isDirectory()
            if (fList[i].isFile())
                mDataSet.add(fList[i].getPath().toString());


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        photo.setImageURI(directory);

        photo.setRotation(90);
        mDataSet.add(directory.toString());
        recyclerView.getAdapter().notifyDataSetChanged();

    }

    private Uri generateFileUri() {

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return null;

        File path = new File(Environment.getExternalStorageDirectory(),
                "CameraTest");
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