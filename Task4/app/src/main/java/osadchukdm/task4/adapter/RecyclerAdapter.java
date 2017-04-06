package osadchukdm.task4.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import osadchukdm.task4.R;
import osadchukdm.task4.interfaces.RecyclerClick;
import osadchukdm.task4.data.LoadImage;

public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder>{

    ArrayList<String> pathList;
    LoadImage loadImage;
    RecyclerClick recyclerClick;

    public void SetOnItemClickListener(final RecyclerClick recyclerClick) {
        this.recyclerClick = recyclerClick;
    }

    public RecyclerAdapter(ArrayList<String> dataSet, LoadImage loadImage) {

        pathList = dataSet;
        this.loadImage=loadImage;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void addImage(String data){
        pathList.add(data);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.loadImage(loadImage, pathList.get(position));

        holder.photoSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerClick != null)
                    recyclerClick.onItemClick(pathList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return pathList.size();
    }
}


