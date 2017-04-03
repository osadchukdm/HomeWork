package osadchukdm.task4.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import osadchukdm.task4.R;
import osadchukdm.task4.Interface.RecyclerClick;


public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder>{

    ArrayList<String> mDataSet;
    RecyclerClick recyclerClick;

    public void SetOnItemClickListener(final RecyclerClick recyclerClick) {
        this.recyclerClick = recyclerClick;
    }

    public RecyclerAdapter(ArrayList<String> dataSet) {

        mDataSet = dataSet;

       }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    public void addImage(String data){
        mDataSet.add(data);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.loadImage(mDataSet.get(position));

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


