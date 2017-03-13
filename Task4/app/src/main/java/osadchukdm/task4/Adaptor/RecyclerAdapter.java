package osadchukdm.task4.Adaptor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.util.ArrayList;

import osadchukdm.task4.R;
import osadchukdm.task4.Interface.RecyclerClick;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    ArrayList<String> mDataSet;
    RecyclerClick recyclerClick;

    public void SetOnItemClickListener(final RecyclerClick recyclerClick) {
        this.recyclerClick = recyclerClick;
    }
    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        ImageView photoSmall;
        //private RecyclerClick recyclerClick;

        public ViewHolder(View itemView) {
            super(itemView);

            photoSmall=(ImageView)itemView.findViewById(R.id.photoSmall);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            recyclerClick.onItemClick(v,getAdapterPosition());
        }

    }

    // Конструктор
    public RecyclerAdapter(ArrayList<String> dataSet) {

        mDataSet = dataSet;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_layout, parent, false);

        // тут можно программно менять атрибуты лэйаута (size, margins, paddings и др.)

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    // Заменяет контент отдельного view (вызывается layout manager-ом)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

            Bitmap map = BitmapFactory.decodeFile(mDataSet.get(position));
            Log.d("may", mDataSet.get(position));
            Bitmap newq = Bitmap.createScaledBitmap(map, map.getWidth() / 12, map.getHeight() / 12, false);
            holder.photoSmall.setRotation(90);
            holder.photoSmall.setImageBitmap(newq);
   
    }
    // Возвращает размер данных (вызывается layout manager-ом)
    @Override
    public int getItemCount() {

        return mDataSet.size();
    }
}


