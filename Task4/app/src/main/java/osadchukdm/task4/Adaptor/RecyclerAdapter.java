package osadchukdm.task4.Adaptor;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.io.File;
import osadchukdm.task4.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private String[] mDataset;
    private Context contextt;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // наш пункт состоит только из одного TextView
        ImageView photoSmall;

        public ViewHolder(View v) {
            super(v);
            photoSmall=(ImageView)v.findViewById(R.id.photoSmall);

        }
    }

    // Конструктор
    public RecyclerAdapter(String[] dataset,Context con) {
        mDataset = dataset;
        contextt=con;
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
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(mDataset[position]!=null){
            Log.d("may", mDataset[position]);
            File imagePath = new File(mDataset[position]);
            Picasso.with(contextt)
                    .load(imagePath)
                    .resize(50, 50)
                    .into(holder.photoSmall);
       }

    }
    // Возвращает размер данных (вызывается layout manager-ом)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}


