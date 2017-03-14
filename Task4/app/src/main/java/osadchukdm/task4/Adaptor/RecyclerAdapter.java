package osadchukdm.task4.Adaptor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;

import java.net.URI;
import java.util.ArrayList;

import osadchukdm.task4.Loading.LoadingFile;
import osadchukdm.task4.R;
import osadchukdm.task4.Interface.RecyclerClick;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    ArrayList<String> mDataSet;
    RecyclerClick recyclerClick;
    Context context;

    public void SetOnItemClickListener(final RecyclerClick recyclerClick) {
        this.recyclerClick = recyclerClick;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView photoSmall;
        final int HIGHT=100;
        final int WIGHT=100;

        public ViewHolder(View itemView) {
            super(itemView);
            photoSmall=(ImageView)itemView.findViewById(R.id.photoSmall);
        }
        public void loadImage(Context context, String imagePath) {
            Glide.with(context).load(imagePath).override(HIGHT, WIGHT).
                    centerCrop().into(photoSmall);
        }

    }

    // Конструктор
    public RecyclerAdapter(ArrayList<String> dataSet,Context con) {

        mDataSet = dataSet;
        context=con;
       }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_layout, parent, false);


        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

            holder.loadImage(context, mDataSet.get(position));

            holder.photoSmall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerClick.onItemClick(holder.getAdapterPosition());
                }
            });
    }

    @Override
    public int getItemCount() {

        return mDataSet.size();
    }
}


