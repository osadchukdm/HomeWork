package osadchukdm.task4.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import osadchukdm.task4.R;
import osadchukdm.task4.interfaces.RecyclerClick;
import osadchukdm.task4.data.LoadImage;

public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder>{

    ArrayList<String> items;
    LoadImage loadGalleryItem;
    RecyclerClick recyclerClick;
    Context context;

    public void SetOnItemClickListener(final RecyclerClick recyclerClick) {
        this.recyclerClick = recyclerClick;
    }

    public RecyclerAdapter(ArrayList<String> dataSet, LoadImage loadImage, Context appContext) {

        items = dataSet;
        this.loadGalleryItem =loadImage;
        this.context=appContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.loadImage(loadGalleryItem, Uri.parse(items.get(position)));
        holder.galleryItem.startAnimation(AnimationUtils.loadAnimation(context, R.anim.show_items));

        holder.galleryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerClick != null)
                    recyclerClick.onItemClick(items.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}


